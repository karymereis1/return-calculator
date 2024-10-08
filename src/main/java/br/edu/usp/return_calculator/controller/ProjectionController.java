package br.edu.usp.return_calculator.controller;

import br.edu.usp.return_calculator.dto.ProjectionRequest;
import br.edu.usp.return_calculator.dto.ProjectionResponse;
import br.edu.usp.return_calculator.dto.DataSource;
import br.edu.usp.return_calculator.service.ProjectionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/projection")
public class ProjectionController {
    private final ProjectionService projectionService;

    public ProjectionController(ProjectionService projectionService) {
        this.projectionService = projectionService;
    }

    @PostMapping
    public ResponseEntity<ProjectionResponse> calculateProjection(
            @RequestParam DataSource source,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime asOf,
            @Valid @RequestBody ProjectionRequest projectionRequest) {

        ProjectionResponse projectionResponse;

        switch (source) {
            case POSTGRES:
                projectionResponse = projectionService.calculateProjectionPostgres(projectionRequest, Optional.ofNullable(asOf));
                break;
            case DATOMIC:
                projectionResponse = projectionService.calculateProjectionDatomic(projectionRequest, Optional.ofNullable(asOf));
                break;
            default:
                return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok(projectionResponse);
    }
}
