package br.edu.usp.return_calculator.service;

import br.edu.usp.return_calculator.dto.AssetReturnRateListRequest;
import br.edu.usp.return_calculator.dto.AssetReturnRateRequest;
import br.edu.usp.return_calculator.model.AssetReturnRate;
import br.edu.usp.return_calculator.repository.AssetReturnRateRepositoryCustomDatomic;
import br.edu.usp.return_calculator.repository.AssetReturnRateRepositoryCustomPostgres;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AssetReturnRateService {
    private final AssetReturnRateRepositoryCustomPostgres assetReturnRateRepositoryCustomPostgres;
    private final AssetReturnRateRepositoryCustomDatomic assetReturnRateRepositoryCustomDatomic;


    @Autowired
    public AssetReturnRateService(
            AssetReturnRateRepositoryCustomPostgres assetReturnRateRepositoryCustomPostgres,
            AssetReturnRateRepositoryCustomDatomic assetReturnRateRepositoryCustomDatomic) {
        this.assetReturnRateRepositoryCustomPostgres = assetReturnRateRepositoryCustomPostgres;
        this.assetReturnRateRepositoryCustomDatomic = assetReturnRateRepositoryCustomDatomic;
    }

    public Optional<AssetReturnRate> getByAssetPostgres(String asset, Optional<LocalDateTime> asOf) {
        return assetReturnRateRepositoryCustomPostgres.findAssetReturnRateByAsset(asset, asOf);
    }

    public Optional<AssetReturnRate> getByAssetDatomic(String asset, Optional<LocalDateTime> asOf) {
        return assetReturnRateRepositoryCustomDatomic.findAssetReturnRateByAsset(asset, asOf);
    }

    public List<AssetReturnRate> getAllFromPostgres(Optional<LocalDateTime> asOf) {
        return assetReturnRateRepositoryCustomPostgres.findAll(asOf);
    }

    public List<AssetReturnRate> getAllFromDatomic(Optional<LocalDateTime> asOf) {
        return assetReturnRateRepositoryCustomDatomic.findAll(asOf);
    }

    public List<AssetReturnRate> createAssetReturnRatesPostgres(AssetReturnRateListRequest assetReturnRateListRequest) {
        List<AssetReturnRate> createdAssetReturnRates = new ArrayList<>();
        for (AssetReturnRateRequest request : assetReturnRateListRequest.getAssets()) {
            AssetReturnRate assetReturnRate = new AssetReturnRate();
            assetReturnRate.setId(UUID.randomUUID());
            assetReturnRate.setAsset(request.getAsset());
            assetReturnRate.setReturnRate(request.getReturnRate());
            assetReturnRate.setCreatedAt(LocalDateTime.now());
            assetReturnRate.setUpdatedAt(LocalDateTime.now());

            createdAssetReturnRates.add(assetReturnRateRepositoryCustomPostgres.save(assetReturnRate));
        }
        return createdAssetReturnRates;
    }

    public List<AssetReturnRate> createAssetReturnRatesDatomic(AssetReturnRateListRequest assetReturnRateListRequest) {
        List<AssetReturnRate> createdAssetReturnRates = new ArrayList<>();
        for (AssetReturnRateRequest request : assetReturnRateListRequest.getAssets()) {
            AssetReturnRate assetReturnRate = new AssetReturnRate();
            assetReturnRate.setId(UUID.randomUUID());
            assetReturnRate.setAsset(request.getAsset());
            assetReturnRate.setReturnRate(request.getReturnRate());
            assetReturnRate.setCreatedAt(LocalDateTime.now());
            assetReturnRate.setUpdatedAt(LocalDateTime.now());

            createdAssetReturnRates.add(assetReturnRateRepositoryCustomDatomic.save(assetReturnRate));
        }
        return createdAssetReturnRates;
    }

    public List<AssetReturnRate> updateAssetReturnRatesPostgres(AssetReturnRateListRequest assetReturnRateListRequest) {
        List<AssetReturnRate> updatedRates = new ArrayList<>();
        for (AssetReturnRateRequest request : assetReturnRateListRequest.getAssets()) {
            Optional<AssetReturnRate> maybeAssetReturnRate = assetReturnRateRepositoryCustomPostgres.findAssetReturnRateByAsset(request.getAsset(), Optional.empty());
            maybeAssetReturnRate.ifPresent(asset -> {
                asset.setReturnRate(request.getReturnRate());
                asset.setUpdatedAt(LocalDateTime.now());
                updatedRates.add(assetReturnRateRepositoryCustomPostgres.save(asset));
            });
        }
        return updatedRates;
    }

    public List<AssetReturnRate> updateAssetReturnRatesDatomic(AssetReturnRateListRequest assetReturnRateListRequest) {
        List<AssetReturnRate> updatedRates = new ArrayList<>();
        for (AssetReturnRateRequest request : assetReturnRateListRequest.getAssets()) {
            Optional<AssetReturnRate> maybeAssetReturnRate = assetReturnRateRepositoryCustomDatomic.findAssetReturnRateByAsset(request.getAsset(), Optional.empty());
            maybeAssetReturnRate.ifPresent(asset -> {
                asset.setReturnRate(request.getReturnRate());
                asset.setUpdatedAt(LocalDateTime.now());
                updatedRates.add(assetReturnRateRepositoryCustomDatomic.save(asset));
            });
        }
        return updatedRates;
    }
}
