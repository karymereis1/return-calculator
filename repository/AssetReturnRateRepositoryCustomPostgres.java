package br.edu.usp.return_calculator.repository;

import br.edu.usp.return_calculator.model.AssetReturnRate;
import jakarta.persistence.EntityManager;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;


@Repository
public class AssetReturnRateRepositoryCustomPostgres {
    private final EntityManager entityManager;
    private final AssetReturnRateRepositoryPostgres assetReturnRateRepositoryPostgres;


    @Autowired
    public AssetReturnRateRepositoryCustomPostgres(
            EntityManager entityManager,
            AssetReturnRateRepositoryPostgres assetReturnRateRepositoryPostgres) {
        this.entityManager = entityManager;
        this.assetReturnRateRepositoryPostgres = assetReturnRateRepositoryPostgres;
    }

    public AssetReturnRate save(AssetReturnRate assetReturnRate) {
        return assetReturnRateRepositoryPostgres.save(assetReturnRate);
    }

    public Optional<AssetReturnRate> findAssetReturnRateByAsset(String asset, Optional<LocalDateTime> asOf) {
        if (asOf.isPresent()) {
            return findByAssetByAuditReader(asset, asOf.get());
        } else {
            return assetReturnRateRepositoryPostgres.findByAsset(asset);
        }

    }

    public List<AssetReturnRate> findAll(Optional<LocalDateTime> asOf) {
        if (asOf.isPresent()) {
            return findAllByAuditReader(asOf.get());
        } else {
            return assetReturnRateRepositoryPostgres.findAll();
        }
    }

    public List<AssetReturnRate> findAllByAuditReader(LocalDateTime asOf) {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);

        LocalDateTime startOfDay = asOf.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        @SuppressWarnings("unchecked")
        List<AssetReturnRate> results = (List<AssetReturnRate>) auditReader.createQuery()
                .forRevisionsOfEntity(AssetReturnRate.class, true, true)
                .add(AuditEntity.property("updatedAt").ge(startOfDay))
                .add(AuditEntity.property("updatedAt").lt(endOfDay))
                .getResultList();

        return results;
    }

    public Optional<AssetReturnRate> findByAssetByAuditReader(String asset, LocalDateTime asOf) {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);

        LocalDateTime startOfDay = asOf.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        @SuppressWarnings("unchecked")
        List<AssetReturnRate> results = (List<AssetReturnRate>) auditReader.createQuery()
                .forRevisionsOfEntity(AssetReturnRate.class, true, true)
                .add(AuditEntity.property("updatedAt").ge(startOfDay))
                .add(AuditEntity.property("updatedAt").lt(endOfDay))
                .add(AuditEntity.property("asset").eq(asset))
                .getResultList();

        // Retorna o último elemento da lista, se existir, como um Optional
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(results.size() - 1));
    }
}
