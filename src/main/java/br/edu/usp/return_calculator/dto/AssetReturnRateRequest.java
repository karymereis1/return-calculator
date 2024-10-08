package br.edu.usp.return_calculator.dto;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class AssetReturnRateRequest {
    @NotNull
    private String asset;
    @NotNull
    private BigDecimal returnRate;

    public String getAsset() {
        return asset;
    }

    public BigDecimal getReturnRate() {
        return returnRate;
    }
}
