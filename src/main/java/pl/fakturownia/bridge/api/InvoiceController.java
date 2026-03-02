package pl.fakturownia.bridge.api;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.fakturownia.bridge.dto.CreateInvoiceRequest;
import pl.fakturownia.bridge.dto.InvoiceStatusResponse;
import pl.fakturownia.bridge.service.FakturowniaInvoiceService;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final FakturowniaInvoiceService invoiceService;

    public InvoiceController(FakturowniaInvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InvoiceStatusResponse createInvoice(@Valid @RequestBody CreateInvoiceRequest request) {
        return invoiceService.createInvoice(request);
    }

    @GetMapping("/{invoiceId}/status")
    public InvoiceStatusResponse getInvoiceStatus(@PathVariable Long invoiceId) {
        return invoiceService.getInvoiceStatus(invoiceId);
    }
}
