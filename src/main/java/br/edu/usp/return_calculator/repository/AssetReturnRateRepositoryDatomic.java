package br.edu.usp.return_calculator.repository;

import br.edu.usp.return_calculator.model.AssetReturnRate;
import datomic.Connection;
import datomic.Database;
import datomic.Peer;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.*;


@Repository
public class AssetReturnRateRepositoryDatomic {
    private final Connection connection;

    public AssetReturnRateRepositoryDatomic(Connection connection) {
        this.connection = connection;
    }

    public AssetReturnRate save(AssetReturnRate assetReturnRate) {
        Date createdAtDate = Date.from(assetReturnRate.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant());
        Date updatedAtDate = Date.from(assetReturnRate.getUpdatedAt().atZone(ZoneId.systemDefault()).toInstant());
        List<Map<String, Object>> transaction = List.of(
                Map.of(
                        ":asset-return-rate/id", assetReturnRate.getId(),
                        ":asset-return-rate/asset", assetReturnRate.getAsset(),
                        ":asset-return-rate/return-rate", assetReturnRate.getReturnRate(),
                        ":asset-return-rate/created-at", createdAtDate,
                        ":asset-return-rate/updated-at", updatedAtDate
                )
        );
        System.out.println("Transacting: " + transaction);
        try {
            Object result = connection.transact(transaction).get(); // Chama a transação
            System.out.println("Transaction successful: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return assetReturnRate;
    }

    public Optional<AssetReturnRate> findAssetReturnRateByAsset(String asset) {
        String query = "[:find ?e ?id ?asset ?rate ?created-at ?updated-at " +
                " :where [?e :asset-return-rate/id ?id] " +
                "        [?e :asset-return-rate/asset ?asset] " +
                "        [?e :asset-return-rate/return-rate ?rate] " +
                "        [?e :asset-return-rate/created-at ?created-at] " +
                "        [?e :asset-return-rate/updated-at ?updated-at] " +
                "        [(= ?asset \"" + asset + "\")]]";

        Database db = connection.db();
        Set<List<Object>> results = (Set<List<Object>>) Peer.q(query, db);

        if (!results.isEmpty()) {
            List<Object> result = results.iterator().next();
            AssetReturnRate assetReturnRate = new AssetReturnRate();
            assetReturnRate.setId((UUID) result.get(1));
            assetReturnRate.setAsset((String) result.get(2));
            assetReturnRate.setReturnRate((BigDecimal) result.get(3));
            assetReturnRate.setCreatedAt(((java.util.Date) result.get(4)).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            assetReturnRate.setUpdatedAt(((java.util.Date) result.get(5)).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            return Optional.of(assetReturnRate);
        }
        return Optional.empty();
    }



    public List<AssetReturnRate> findAll() {
        String query = "[:find ?e ?id ?asset ?rate ?created-at ?updated-at " +
                " :where [?e :asset-return-rate/id ?id] " +
                "        [?e :asset-return-rate/asset ?asset] " +
                "        [?e :asset-return-rate/return-rate ?rate] " +
                "        [?e :asset-return-rate/created-at ?created-at] " +
                "        [?e :asset-return-rate/updated-at ?updated-at]]";

        Database db = connection.db();
        Set<List<Object>> results = (Set<List<Object>>) Peer.q(query, db);

        List<AssetReturnRate> assetReturnRates = new ArrayList<>();

        for (List<Object> result : results) {
            AssetReturnRate assetReturnRate = new AssetReturnRate();
            assetReturnRate.setId((UUID) result.get(1));
            assetReturnRate.setAsset((String) result.get(2));
            assetReturnRate.setReturnRate((BigDecimal) result.get(3));
            assetReturnRate.setCreatedAt(((java.util.Date) result.get(4)).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            assetReturnRate.setUpdatedAt(((java.util.Date) result.get(5)).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            assetReturnRates.add(assetReturnRate);
        }

        return assetReturnRates;
    }
}
