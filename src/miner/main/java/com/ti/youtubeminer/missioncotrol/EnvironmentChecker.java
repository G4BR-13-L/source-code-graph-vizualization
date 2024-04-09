package com.ti.youtubeminer.missioncotrol;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties") // ou o caminho para o seu arquivo de propriedades
public class EnvironmentChecker {

    @Value(value = "${spring.profiles.active}")
    private String activeProfile;

    public boolean isTestEnvironment() {
        return "test".equals(activeProfile);
    }

    public boolean isProductionEnvironment() {
        return "prod".equals(activeProfile);
    }

    public boolean isDevelopmentEnviroment() {
        return "dev".equals(activeProfile);
    }


    // Outros métodos para verificar diferentes perfis, se necessário
}