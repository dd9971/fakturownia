package pl.fakturownia.bridge.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import pl.fakturownia.bridge.config.FakturowniaProperties;
import pl.fakturownia.bridge.dto.CreateInvoiceRequest;
import pl.fakturownia.bridge.dto.InvoiceStatusResponse;
import pl.fakturownia.client.api.InvoicesApi;
import pl.fakturownia.client.model.Invoice;
import pl.fakturownia.client.model.InvoiceCreateRequest;
import pl.fakturownia.client.model.InvoicePayload;
import pl.fakturownia.client.model.InvoicePosition;

@Service
public class FakturowniaInvoiceService {

    private final InvoicesApi invoicesApi;
    private final FakturowniaProperties properties;
    private final ObjectMapper objectMapper;

    public FakturowniaInvoiceService(InvoicesApi invoicesApi, FakturowniaProperties properties, ObjectMapper objectMapper) {
        this.invoicesApi = invoicesApi;
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    public InvoiceStatusResponse createInvoice(CreateInvoiceRequest request) {
        InvoicePayload payload = new InvoicePayload();
        payload.setKind(InvoicePayload.KindEnum.VAT);
        payload.setIssueDate(LocalDate.now());
        payload.setSellDate(LocalDate.now().toString());
        payload.setPaymentTo(LocalDate.now().plusDays(7));
        payload.setSellerName(request.sellerName());
        payload.setSellerTaxNo(request.sellerTaxNo());
        payload.setBuyerName(request.buyerName());
        payload.setBuyerTaxNo(request.buyerTaxNo());

        List<InvoicePosition> positions = request.positions().stream()
                .map(this::mapPosition)
                .toList();
        payload.setPositions(positions);

        InvoiceCreateRequest createRequest = new InvoiceCreateRequest();
        createRequest.setApiToken(properties.apiToken());
        createRequest.setInvoice(payload);

        Invoice created = invoicesApi.createInvoice(createRequest);
        return toStatusResponse(created);
    }

    public InvoiceStatusResponse getInvoiceStatus(Long invoiceId) {
        Invoice invoice = invoicesApi.getInvoiceById(properties.apiToken(), invoiceId, null, null);
        return toStatusResponse(invoice);
    }

    private InvoicePosition mapPosition(CreateInvoiceRequest.Position position) {
        InvoicePosition mapped = new InvoicePosition();
        mapped.setName(position.name());
        mapped.setQuantity(position.quantity());
        mapped.setTax(position.tax());
        mapped.setTotalPriceGross(position.totalPriceGross().setScale(2, RoundingMode.HALF_UP).toPlainString());
        return mapped;
    }

    private InvoiceStatusResponse toStatusResponse(Invoice invoice) {
        Map<String, Object> additional = extractAdditionalProperties(invoice);
        return new InvoiceStatusResponse(
                invoice.getId() != null ? invoice.getId().longValue() : null,
                asString(additional.get("gov_id")),
                invoice.getStatus(),
                asString(additional.get("gov_status"))
        );
    }

    private String asString(Object value) {
        return value == null ? null : String.valueOf(value);
    }

    private Map<String, Object> extractAdditionalProperties(Invoice invoice) {
        try {
            Object value = invoice.getClass().getMethod("getAdditionalProperties").invoke(invoice);
            if (value instanceof Map<?, ?> map) {
                Map<String, Object> result = new HashMap<>();
                map.forEach((k, v) -> result.put(String.valueOf(k), v));
                return result;
            }
        } catch (Exception ignored) {
            // fallback below
        }
        return objectMapper.convertValue(invoice, new TypeReference<>() {});
    }
}
