package propertyview.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HotelDTO {
    private Long id;
    private String name;
    private String brand;
    private String city;
    private String country;
    private List<String> amenities;

    public HotelDTO() {}

    public HotelDTO(
            String name, String brand, String city, String country, List<String> amenities) {
        this.id = null;
        this.city = city;
        this.name = name;
        this.amenities = amenities;
        this.brand = brand;
        this.country = country;
    }

    @Override
    public String toString() {
        return "HotelDTO{id=%d, name='%s', brand = %s, city='%s', country='%s', amenities=%s}"
                .formatted(id, name, brand, city, country, amenities);
    }

    @JsonIgnore
    public String getAmenitiesAsJson() {
        var amenities = "[]";
        try {
            amenities = new ObjectMapper().writeValueAsString(getAmenities());
        } catch (JsonProcessingException e) {
        }
        return amenities;
    }
}
