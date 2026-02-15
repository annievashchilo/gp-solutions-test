package propertyview.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import propertyview.dto.DataResponseDTO;
import propertyview.dto.HotelDTO;

@RestController
@RequestMapping("/property-view/hotels")
public class HotelController {
    @GetMapping
    public DataResponseDTO<List<HotelDTO>> getAllHotels() {
        var hotels = new ArrayList<HotelDTO>();
        return new DataResponseDTO<List<HotelDTO>>(hotels);
    }
}
