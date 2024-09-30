package br.edu.usp.return_calculator.config;

import datomic.Connection;
import datomic.Peer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DatomicConfig {

    private static final String URI = "datomic:dev://localhost:4334/return-calculator";

    @Bean
    public Connection connectToDatomic() {
        Connection conn = Peer.connect(URI);
        System.out.println("Conexão com Datomic estabelecida: " + URI);
        return conn;
    }
}
