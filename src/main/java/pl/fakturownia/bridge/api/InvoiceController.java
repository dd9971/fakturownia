package pl.fakturownia.bridge.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
@Tag(name = "Invoices", description = "Operacje na fakturach przez Fakturownia Bridge")
public class InvoiceController {

    private final FakturowniaInvoiceService invoiceService;

    public InvoiceController(FakturowniaInvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Utworzenie nowej faktury",
            description = "Tworzy fakturę przez API Fakturownia i zwraca ID faktury w Fakturowni oraz ID KSeF (jeśli dostępne).")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Faktura utworzona",
                    content = @Content(schema = @Schema(implementation = InvoiceStatusResponse.class))),
            @ApiResponse(responseCode = "400", description = "Błędne dane wejściowe"),
            @ApiResponse(responseCode = "502", description = "Błąd komunikacji z API Fakturownia")
    })
    public InvoiceStatusResponse createInvoice(@Valid @RequestBody CreateInvoiceRequest request) {
        return invoiceService.createInvoice(request);
    }

    @GetMapping("/{invoiceId}/status")
    @Operation(summary = "Pobranie statusu faktury i KSeF",
            description = "Zwraca status faktury z Fakturowni, status KSeF oraz identyfikatory faktury po obu stronach.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status pobrany",
                    content = @Content(schema = @Schema(implementation = InvoiceStatusResponse.class))),
            @ApiResponse(responseCode = "502", description = "Błąd komunikacji z API Fakturownia")
    })
    public InvoiceStatusResponse getInvoiceStatus(@PathVariable Long invoiceId) {
        return invoiceService.getInvoiceStatus(invoiceId);
    }

    @GetMapping(value = "/{invoiceId}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    @Operation(summary = "Pobranie PDF faktury",
            description = "Pobiera PDF faktury z Fakturowni i zwraca go jako plik application/pdf.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "PDF pobrany"),
            @ApiResponse(responseCode = "502", description = "Błąd komunikacji z API Fakturownia")
    })
    public ResponseEntity<byte[]> getInvoicePdf(@PathVariable Long invoiceId) {
        byte[] pdf = invoiceService.getInvoicePdf(invoiceId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=invoice-" + invoiceId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
