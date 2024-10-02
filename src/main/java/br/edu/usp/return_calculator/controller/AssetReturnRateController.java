package br.edu.usp.return_calculator.controller;

import br.edu.usp.return_calculator.dto.AssetReturnRateListRequest;
import br.edu.usp.return_calculator.model.AssetReturnRate;
import br.edu.usp.return_calculator.model.DataSource;
import br.edu.usp.return_calculator.service.AssetReturnRateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/asset-return-rate")
public class AssetReturnRateController {
    private final AssetReturnRateService assetReturnRateService;

    public AssetReturnRateController(AssetReturnRateService assetReturnRateService) {
        this.assetReturnRateService = assetReturnRateService;
    }

    @GetMapping
    public List<AssetReturnRate> getAllAssets(@RequestParam DataSource source) {
        return switch (source) {
            case POSTGRES -> assetReturnRateService.getAllFromPostgres();
            case DATOMIC -> assetReturnRateService.getAllFromDatomic();
        };
    }

    @PostMapping
    public ResponseEntity<List<AssetReturnRate>> createAssetReturnRates(
            @RequestParam DataSource source,
            @Valid @RequestBody AssetReturnRateListRequest assetReturnRateListRequest) {
        List<AssetReturnRate> updatedAssetReturnRates = switch (source) {
            case POSTGRES -> assetReturnRateService.createAssetReturnRatesPostgres(assetReturnRateListRequest);
            case DATOMIC -> assetReturnRateService.createAssetReturnRatesDatomic(assetReturnRateListRequest);
        };

        return ResponseEntity.ok(updatedAssetReturnRates);
    }

    @PutMapping
    public ResponseEntity<List<AssetReturnRate>> updateAssetReturnRates(
            @RequestParam DataSource source,
            @Valid @RequestBody AssetReturnRateListRequest assetReturnRateListRequest) {
        List<AssetReturnRate> updatedAssetReturnRates = switch (source) {
            case POSTGRES -> assetReturnRateService.updateAssetReturnRatesPostgres(assetReturnRateListRequest);
            case DATOMIC -> assetReturnRateService.updateAssetReturnRatesDatomic(assetReturnRateListRequest);
        };

        return ResponseEntity.ok(updatedAssetReturnRates);
    }
}
//assetReturnRateService.getByUpdatedAtFromPostgres(LocalDateTime.parse("2024-09-30T22:08:36.872543"));
