package pl.fakturownia.bridge.dto;

public record InvoiceStatusResponse(
        Long fakturowniaInvoiceId,
        String ksefInvoiceId,
        String invoiceStatus,
        String ksefStatus
) {
}
