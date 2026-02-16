package propertyview.dto;

public class HotelDTO {
    private Long id;
    private String name;
    private String city;

    public HotelDTO() {}

    public HotelDTO(String name, String city) {
        this.id = null;
        this.city = city;
        this.name = name;
    }

    public HotelDTO(Long id, String name, String city) {
        this.id = id;
        this.name = name;
        this.city = city;
    }

    public String getCity() {
        return city;
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
