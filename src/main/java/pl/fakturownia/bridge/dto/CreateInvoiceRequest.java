package pl.fakturownia.bridge.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Żądanie utworzenia nowej faktury w Fakturowni")
public record CreateInvoiceRequest(
        @Schema(description = "Nazwa sprzedawcy", example = "Wystawca Sp. z o.o.")
        @NotBlank String sellerName,

        @Schema(description = "NIP sprzedawcy", example = "6272616681")
        @NotBlank String sellerTaxNo,

        @Schema(description = "Nazwa nabywcy", example = "Klient Sp. z o.o.")
        @NotBlank String buyerName,

        @Schema(description = "NIP nabywcy", example = "6272616681")
        @NotBlank String buyerTaxNo,

        @ArraySchema(schema = @Schema(implementation = Position.class),
                arraySchema = @Schema(description = "Pozycje faktury"))
        @NotEmpty List<@Valid Position> positions
) {

    @Schema(description = "Pozycja faktury")
    public record Position(
            @Schema(description = "Nazwa pozycji", example = "Usługa wdrożenia")
            @NotBlank String name,

            @Schema(description = "Ilość", example = "1")
            @DecimalMin("0.0001") BigDecimal quantity,

            @Schema(description = "Stawka VAT", example = "23")
            @NotBlank String tax,

            @Schema(description = "Wartość brutto pozycji", example = "123.00")
            @DecimalMin("0.0") BigDecimal totalPriceGross
    ) {
    }
}
