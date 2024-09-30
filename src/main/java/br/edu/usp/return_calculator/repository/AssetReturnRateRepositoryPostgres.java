package br.edu.usp.return_calculator.repository;

import br.edu.usp.return_calculator.model.AssetReturnRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface AssetReturnRateRepositoryPostgres extends JpaRepository<AssetReturnRate, UUID> {
}
