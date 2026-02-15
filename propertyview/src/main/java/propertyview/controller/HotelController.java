package propertyview.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import propertyview.dto.DataResponseDTO;
import propertyview.dto.HotelDTO;
import propertyview.repository.HotelRepository;
import propertyview.repository.JsonFileHotelRepository;

@RestController
@RequestMapping("/property-view/hotels")
public class HotelController {
    private static final HotelRepository repo = new JsonFileHotelRepository();

    @GetMapping
    public DataResponseDTO<List<HotelDTO>> getAllHotels() {
        var hotels = repo.getAll();
        return new DataResponseDTO<List<HotelDTO>>(hotels);
    }
}
