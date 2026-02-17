package propertyview.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import propertyview.dto.DataResponseDTO;
import propertyview.repository.H2HotelRepository;
import propertyview.repository.HotelRepository;

@RestController
@RequestMapping("/property-view/histogram")
public class HistogramController {
    private static final HotelRepository hotelRepo = new H2HotelRepository();

    @GetMapping("/{param}")
    public DataResponseDTO<Map<String, Long>> getHotelNameHistogram(@PathVariable String param) {
        var histogram = new HashMap<String, Long>();

        switch (param) {
            case "city":
                hotelRepo
                        .getAll()
                        .forEach(
                                hotel -> {
                                    var city = hotel.getCity();
                                    histogram.put(city, histogram.getOrDefault(city, 0L) + 1);
                                });
                break;
            case "amenities":
                hotelRepo
                        .getAll()
                        .forEach(
                                hotel -> {
                                    hotel.getAmenities()
                                            .forEach(
                                                    amenity ->
                                                            histogram.put(
                                                                    amenity,
                                                                    histogram.getOrDefault(
                                                                                    amenity, 0L)
                                                                            + 1));
                                });
                break;
        }

        return new DataResponseDTO<Map<String, Long>>(histogram);
    }
}
