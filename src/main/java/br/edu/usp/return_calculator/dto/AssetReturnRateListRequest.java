package br.edu.usp.return_calculator.dto;

import java.util.List;

public class AssetReturnRateListRequest {
    private List<AssetReturnRateRequest> assets;

    public List<AssetReturnRateRequest> getAssets() {
        return assets;
    }
}
