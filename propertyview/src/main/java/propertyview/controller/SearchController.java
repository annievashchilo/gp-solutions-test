package propertyview.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import propertyview.dto.DataResponseDTO;
import propertyview.dto.HotelDTO;
import propertyview.repository.HotelRepository;

@RestController
@RequestMapping("/property-view/search")
@Tag(name = "Search", description = "APIs for search")
public class SearchController {
    @Autowired private HotelRepository hotelRepo;

    @GetMapping
    @Operation(
            summary = "Search for specific hotel",
            description = "Retrieve a list of all hotels matching search parameters")
    public DataResponseDTO<List<HotelDTO>> search(@RequestParam Map<String, String> query) {
        var filteredHotels = hotelRepo.getAll(query);

        return new DataResponseDTO<List<HotelDTO>>(filteredHotels);
    }
}
