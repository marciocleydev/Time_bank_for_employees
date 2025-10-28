package com.marciocleydev.Time_bank_for_employees.integrationtests.testcontainers;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;
import java.util.stream.Stream;

@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class) // Diz pro Spring Test usar a Initializer antes de criar o contexto
public class AbstractIntegrationTest {
    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext>{ // Inicializador que configura o ApplicationContext antes de iniciar

        static MySQLContainer<?> mysql = new MySQLContainer<>(DockerImageName.parse("mysql:8.4")
                .asCompatibleSubstituteFor("mysql")
        ); // Cria um container MySQL (imagem mysql:8.4) para os testes

        private static void startContainers() {
            Startables.deepStart(Stream.of(mysql)).join();
        } // Inicia o container e espera ele ficar pronto

        private static Map<String, Object> createConnectionConfiguration(){
            return Map.of(
                    "spring.datasource.url", mysql.getJdbcUrl(),
                    "spring.datasource.username", mysql.getUsername(),
                    "spring.datasource.password", mysql.getPassword()
            );
        } // Monta um Map com URL, usuário e senha obtidos do container

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            startContainers(); // Garante que o container esteja rodando antes de configurar o contexto
            ConfigurableEnvironment environment = applicationContext.getEnvironment(); // Pega o ambiente do Spring
            MapPropertySource testcontainers = new MapPropertySource("testcontainers",
                    createConnectionConfiguration());
            environment.getPropertySources().addFirst(testcontainers); // Adiciona as props do container com prioridade (sobrescreve outras)
        }
    }
}
