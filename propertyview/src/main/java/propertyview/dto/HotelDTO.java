package propertyview.dto;

public class HotelDTO {
    private Long id;
    private String name;

    public HotelDTO() {}

    public HotelDTO(String name) {
        this.id = null;
        this.name = name;
    }

    public HotelDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
