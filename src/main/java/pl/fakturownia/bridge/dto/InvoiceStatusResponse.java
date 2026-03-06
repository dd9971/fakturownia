package pl.fakturownia.bridge.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Wspólna odpowiedź statusowa z ID faktury Fakturownia i ID KSeF")
public record InvoiceStatusResponse(
        @Schema(description = "ID faktury po stronie Fakturowni", example = "123456")
        Long fakturowniaInvoiceId,

        @Schema(description = "Numer KSeF (gov_id). Może być null, gdy faktura nie jest jeszcze przetworzona w KSeF.",
                example = "5213704420-20260215-ABC123DEF456")
        String ksefInvoiceId,

        @Schema(description = "Status biznesowy faktury w Fakturowni", example = "issued")
        String invoiceStatus,

        @Schema(description = "Status techniczny KSeF (gov_status)", example = "ok")
        String ksefStatus
) {
}
