package propertyview.controller;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import propertyview.dto.DataResponseDTO;
import propertyview.dto.HotelDTO;
import propertyview.repository.H2HotelRepository;
import propertyview.repository.HotelRepository;

@RestController
@RequestMapping("/property-view/search")
public class SearchController {
    private static final HotelRepository hotelRepo = new H2HotelRepository();

    @GetMapping
    public DataResponseDTO<List<HotelDTO>> search(@RequestParam Map<String, String> query) {
        var filteredHotels = hotelRepo.getAll(query);

        return new DataResponseDTO<List<HotelDTO>>(filteredHotels);
    }
}
