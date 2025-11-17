package aftnos.aftourismserver.ai.dto;

import lombok.Data;

import java.util.List;

@Data
public class AiSettingsDTO {
    private Boolean enabled = true;
    private List<Provider> providers;
    private List<Model> models;
    private String defaultModel;
    private String backupModel;

    @Data
    public static class Provider {
        private String name;
        private String apiKey;
        private Integer timeoutMs;
        private String endpoint;
        private Boolean enabled = true;
    }

    @Data
    public static class Model {
        private String name;
        private String provider;
        private Boolean enabled = true;
    }
}