package br.edu.usp.return_calculator.controller;

import br.edu.usp.return_calculator.dto.AssetReturnRateListRequest;
import br.edu.usp.return_calculator.model.AssetReturnRate;
import br.edu.usp.return_calculator.dto.DataSource;
import br.edu.usp.return_calculator.service.AssetReturnRateService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/asset-return-rate")
public class AssetReturnRateController {
    private final AssetReturnRateService assetReturnRateService;

    public AssetReturnRateController(AssetReturnRateService assetReturnRateService) {
        this.assetReturnRateService = assetReturnRateService;
    }

    @GetMapping
    public List<AssetReturnRate> getAllAssets(
            @RequestParam DataSource source,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime asOf) {

        return switch (source) {
            case POSTGRES -> assetReturnRateService.getAllFromPostgres(Optional.ofNullable(asOf));
            case DATOMIC -> assetReturnRateService.getAllFromDatomic(Optional.ofNullable(asOf));
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
