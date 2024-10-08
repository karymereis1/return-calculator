package br.edu.usp.return_calculator.dto;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class AssetAllocationRequest {
    @NotNull
    private String asset;

    @NotNull
    private BigDecimal percentage;

    public String getAsset() {
        return asset;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }
}
