package br.edu.usp.return_calculator.service;

import br.edu.usp.return_calculator.dto.ProjectionRequest;
import br.edu.usp.return_calculator.dto.ProjectionResponse;
import br.edu.usp.return_calculator.dto.AssetAllocationRequest;
import br.edu.usp.return_calculator.model.AssetReturnRate;
import br.edu.usp.return_calculator.service.AssetReturnRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectionService {

    @Autowired
    private AssetReturnRateService assetReturnRateService;

    public ProjectionResponse calculateProjectionPostgres(ProjectionRequest request, Optional<LocalDateTime> asOf) {
        BigDecimal totalReturn = BigDecimal.ZERO;
        BigDecimal amountInvested = request.getAmountInvested();

        List<AssetAllocationRequest> assetAllocations = request.getAssetAllocation();
        if (assetAllocations.isEmpty()) {
            throw new IllegalArgumentException("No asset allocations provided.");
        }

        for (AssetAllocationRequest allocation : assetAllocations) {
            String asset = allocation.getAsset();
            BigDecimal allocationPercentage = allocation.getPercentage();

            AssetReturnRate assetReturnRate = assetReturnRateService.getByAssetPostgres(asset, asOf)
                    .orElseThrow(() -> new RuntimeException("Asset not found: " + asset));

            BigDecimal investedAmount = amountInvested.multiply(allocationPercentage);
            BigDecimal projectedReturn = investedAmount.multiply(assetReturnRate.getReturnRate());

            totalReturn = totalReturn.add(projectedReturn);
        }

        BigDecimal totalAmount = amountInvested.add(totalReturn).setScale(2, RoundingMode.HALF_UP);

        BigDecimal totalReturnRate = totalReturn.divide(amountInvested, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)); // em porcentagem

        ProjectionResponse response = new ProjectionResponse();
        response.setTotalReturnRate(totalReturnRate.setScale(2, RoundingMode.HALF_UP));
        response.setTotalAmount(totalAmount);
        return response;
    }


    public ProjectionResponse calculateProjectionDatomic(ProjectionRequest request, Optional<LocalDateTime> asOf) {
        BigDecimal totalReturn = BigDecimal.ZERO;
        BigDecimal amountInvested = request.getAmountInvested();

        List<AssetAllocationRequest> assetAllocations = request.getAssetAllocation();
        if (assetAllocations.isEmpty()) {
            throw new IllegalArgumentException("No asset allocations provided.");
        }

        for (AssetAllocationRequest allocation : assetAllocations) {
            String asset = allocation.getAsset();
            BigDecimal allocationPercentage = allocation.getPercentage();

            AssetReturnRate assetReturnRate = assetReturnRateService.getByAssetDatomic(asset, asOf)
                    .orElseThrow(() -> new RuntimeException("Asset not found: " + asset));

            BigDecimal investedAmount = amountInvested.multiply(allocationPercentage);
            BigDecimal projectedReturn = investedAmount.multiply(assetReturnRate.getReturnRate());

            totalReturn = totalReturn.add(projectedReturn);
        }

        BigDecimal totalAmount = amountInvested.add(totalReturn).setScale(2, RoundingMode.HALF_UP);

        BigDecimal totalReturnRate = totalReturn.divide(amountInvested, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)); // em porcentagem

        ProjectionResponse response = new ProjectionResponse();
        response.setTotalReturnRate(totalReturnRate.setScale(2, RoundingMode.HALF_UP));
        response.setTotalAmount(totalAmount);
        return response;
    }
}
