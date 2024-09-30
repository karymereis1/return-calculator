package br.edu.usp.return_calculator.service;

import br.edu.usp.return_calculator.dto.AssetReturnRateListRequest;
import br.edu.usp.return_calculator.dto.AssetReturnRateRequest;
import br.edu.usp.return_calculator.model.AssetReturnRate;
import br.edu.usp.return_calculator.repository.AssetReturnRateRepositoryDatomic;
import br.edu.usp.return_calculator.repository.AssetReturnRateRepositoryPostgres;
import jakarta.persistence.EntityManager;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AssetReturnRateService {
    private final AssetReturnRateRepositoryPostgres assetReturnRateRepositoryPostgres;
    private final AssetReturnRateRepositoryDatomic assetReturnRateRepositoryDatomic;
    private final EntityManager entityManager;

    @Autowired
    public AssetReturnRateService(
            AssetReturnRateRepositoryPostgres assetReturnRateRepositoryPostgres,
            AssetReturnRateRepositoryDatomic assetReturnRateRepositoryDatomic,
            EntityManager entityManager) {
        this.assetReturnRateRepositoryPostgres = assetReturnRateRepositoryPostgres;
        this.assetReturnRateRepositoryDatomic = assetReturnRateRepositoryDatomic;
        this.entityManager = entityManager;
    }

    public List<AssetReturnRate> getAllFromPostgres() {
        return assetReturnRateRepositoryPostgres.findAll();
    }

    public List<AssetReturnRate> getAllFromDatomic() {
        return assetReturnRateRepositoryDatomic.findAll();
    }

    public AssetReturnRate createAssetReturnRatePostgres(String asset, BigDecimal returnRate) {
        AssetReturnRate assetReturnRate = new AssetReturnRate();
        assetReturnRate.setId(UUID.randomUUID());
        assetReturnRate.setAsset(asset);
        assetReturnRate.setReturnRate(returnRate);
        assetReturnRate.setCreatedAt(LocalDateTime.now());
        assetReturnRate.setUpdatedAt(LocalDateTime.now());
        return assetReturnRateRepositoryPostgres.save(assetReturnRate);
    }

    public AssetReturnRate createAssetReturnRateDatomic(String asset, BigDecimal returnRate) {
        AssetReturnRate assetReturnRate = new AssetReturnRate();
        assetReturnRate.setId(UUID.randomUUID());
        assetReturnRate.setAsset(asset);
        assetReturnRate.setReturnRate(returnRate);
        assetReturnRate.setCreatedAt(LocalDateTime.now());
        assetReturnRate.setUpdatedAt(LocalDateTime.now());
        return assetReturnRateRepositoryDatomic.save(assetReturnRate);
    }

    public List<AssetReturnRate> updateAssetReturnRatesDatomic(AssetReturnRateListRequest assetReturnRateListRequest) {
        List<AssetReturnRate> updatedRates = new ArrayList<>();

        for (AssetReturnRateRequest request : assetReturnRateListRequest.getAssets()) {
            Optional<AssetReturnRate> maybeAssetReturnRate = assetReturnRateRepositoryDatomic.findAssetReturnRateByAsset(request.getAsset());
            maybeAssetReturnRate.ifPresent(asset -> {
                asset.setReturnRate(request.getReturnRate());
                asset.setUpdatedAt(LocalDateTime.now());
                updatedRates.add(assetReturnRateRepositoryDatomic.save(asset));
            });
        }

        return updatedRates;
    }


    public AssetReturnRate updateAssetReturnRatePostgres(UUID id, BigDecimal returnRate) {
        AssetReturnRate assetReturnRate = assetReturnRateRepositoryPostgres.findById(id).orElseThrow(() -> new RuntimeException("Asset not found"));
        assetReturnRate.setReturnRate(returnRate);
        assetReturnRate.setUpdatedAt(LocalDateTime.now());
        return assetReturnRateRepositoryPostgres.save(assetReturnRate);
    }

    public List<AssetReturnRate> getByUpdatedAtFromPostgres(LocalDateTime updatedAt) {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);

        @SuppressWarnings("unchecked")
        List<AssetReturnRate> results = (List<AssetReturnRate>) auditReader.createQuery()
                .forRevisionsOfEntity(AssetReturnRate.class, true, true)
                .add(AuditEntity.property("updatedAt").eq(updatedAt))
                .getResultList();

        return results;
    }
}
