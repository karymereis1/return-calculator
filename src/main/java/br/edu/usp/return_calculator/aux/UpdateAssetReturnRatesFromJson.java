package br.edu.usp.return_calculator.aux;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class UpdateAssetReturnRatesFromJson {
    public static void execute() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            File inputFile = new File("src/main/java/br/edu/usp/return_calculator/aux/assets-to-update.json");
            JsonNode rootNode = objectMapper.readTree(inputFile);
            JsonNode assetsNode = rootNode.path("assets");

            Random random = new Random();
            BigDecimal minRate = new BigDecimal("0.0300");
            BigDecimal maxRate = new BigDecimal("0.1200");

            for (JsonNode assetNode : assetsNode) {
                BigDecimal randomRate = generateRandomRate(minRate, maxRate, random);
                ((ObjectNode) assetNode).put("returnRate", randomRate);
            }

            StringBuilder formattedJson = new StringBuilder();
            formattedJson.append("{\n  \"assets\": [\n");
            for (int i = 0; i < assetsNode.size(); i++) {
                JsonNode assetNode = assetsNode.get(i);
                formattedJson.append("    { \"asset\": \"")
                        .append(assetNode.path("asset").asText())
                        .append("\", \"returnRate\": ")
                        .append(assetNode.path("returnRate").asText())
                        .append(" }");
                if (i < assetsNode.size() - 1) {
                    formattedJson.append(",\n");
                }
            }
            formattedJson.append("\n  ]\n}");

            File outputFile = new File("src/main/java/br/edu/usp/return_calculator/aux/assets-updated.json");
            try (FileWriter fileWriter = new FileWriter(outputFile)) {
                fileWriter.write(formattedJson.toString());
            }

            System.out.println("Updated file saved at: " + outputFile.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static BigDecimal generateRandomRate(BigDecimal min, BigDecimal max, Random random) {
        BigDecimal range = max.subtract(min);
        double randomValue = min.doubleValue() + (range.doubleValue() * random.nextDouble());
        return BigDecimal.valueOf(randomValue).setScale(4, RoundingMode.HALF_UP);
    }
}
