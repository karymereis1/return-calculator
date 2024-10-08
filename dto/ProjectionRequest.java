package br.edu.usp.return_calculator.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;

public class ProjectionRequest {
    @NotNull
    @Positive
    private BigDecimal amountInvested;

    @NotNull
    private List<AssetAllocationRequest> assetAllocation;

    public BigDecimal getAmountInvested() {
        return amountInvested;
    }

    public List<AssetAllocationRequest> getAssetAllocation() {
        return assetAllocation;
    }
}
