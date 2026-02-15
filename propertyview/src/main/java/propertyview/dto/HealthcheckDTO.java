package propertyview.dto;

public class HealthcheckDTO {
    private String status;

    public HealthcheckDTO() {}

    public HealthcheckDTO(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
