package pl.fakturownia.bridge.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.List;

public record CreateInvoiceRequest(
        @NotBlank String sellerName,
        @NotBlank String sellerTaxNo,
        @NotBlank String buyerName,
        @NotBlank String buyerTaxNo,
        @NotEmpty List<@Valid Position> positions
) {

    public record Position(
            @NotBlank String name,
            @DecimalMin("0.0001") BigDecimal quantity,
            @NotBlank String tax,
            @DecimalMin("0.0") BigDecimal totalPriceGross
    ) {
    }
}
