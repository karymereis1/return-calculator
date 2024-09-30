package br.edu.usp.return_calculator.controller;

import br.edu.usp.return_calculator.dto.AssetReturnRateListRequest;
import br.edu.usp.return_calculator.dto.AssetReturnRateRequest;
import br.edu.usp.return_calculator.model.AssetReturnRate;
import br.edu.usp.return_calculator.service.AssetReturnRateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/projections")
public class ProjectionController {
    private final AssetReturnRateService assetReturnRateService;

    public ProjectionController(AssetReturnRateService assetReturnRateService) {
        this.assetReturnRateService = assetReturnRateService;
    }

    @GetMapping("/postgres")
    public List<AssetReturnRate> getAllFromPostgres() {
        return assetReturnRateService.getAllFromPostgres();
    }

    @GetMapping("/datomic")
    public List<AssetReturnRate> getAllFromDatomic() {
        return assetReturnRateService.getAllFromDatomic();
    }

    @PostMapping("/postgres")
    public AssetReturnRate createAssetReturnRatePostgres(
            @Valid @RequestBody AssetReturnRateRequest assetReturnRateRequest
    ) {
        return assetReturnRateService.createAssetReturnRatePostgres(
                assetReturnRateRequest.getAsset(),
                assetReturnRateRequest.getReturnRate()
        );
    }

    @PostMapping("/datomic")
    public ResponseEntity<List<AssetReturnRate>> createAssetReturnRateDatomic(
            @Valid @RequestBody AssetReturnRateListRequest assetReturnRateListRequest
    ) { List<AssetReturnRate> createdAssets = new ArrayList<>();

        for (AssetReturnRateRequest assetReturnRateRequest : assetReturnRateListRequest.getAssets()) {
            AssetReturnRate createdAssetReturnRate = assetReturnRateService.createAssetReturnRateDatomic(
                    assetReturnRateRequest.getAsset(),
                    assetReturnRateRequest.getReturnRate()
            );
            createdAssets.add(createdAssetReturnRate);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAssets);
    }

    @PutMapping("/datomic")
    public ResponseEntity<List<AssetReturnRate>> updateAssetReturnRateDatomic(@Valid @RequestBody AssetReturnRateListRequest assetReturnRateListRequest) {
        List<AssetReturnRate> updatedAssetReturnRates = assetReturnRateService.updateAssetReturnRatesDatomic(assetReturnRateListRequest);
        return ResponseEntity.ok(updatedAssetReturnRates);
    }
}
