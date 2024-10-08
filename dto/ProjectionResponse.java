package br.edu.usp.return_calculator.dto;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ProjectionResponse {
    @NotNull
    private BigDecimal totalReturnRate;

    @NotNull
    private BigDecimal totalAmount;

    public BigDecimal getTotalReturnRate() {
        return totalReturnRate;
    }

    public void setTotalReturnRate(BigDecimal totalReturnRate) {
        this.totalReturnRate = totalReturnRate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
