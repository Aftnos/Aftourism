package aftnos.aftourismserver.ai.dto;

/**
 * LLM 输出的结构化建议动作。
 */
public class AiActionSuggestion {

    private String type;
    private String detail;

    public AiActionSuggestion() {
    }

    public AiActionSuggestion(String type, String detail) {
        this.type = type;
        this.detail = detail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
