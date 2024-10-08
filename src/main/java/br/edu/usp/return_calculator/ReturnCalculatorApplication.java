package br.edu.usp.return_calculator;

import br.edu.usp.return_calculator.aux.UpdateAssetReturnRatesFromJson;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReturnCalculatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReturnCalculatorApplication.class, args);
		//UpdateAssetReturnRatesFromJson.execute();
	}
}
