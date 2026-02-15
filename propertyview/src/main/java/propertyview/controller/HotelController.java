package propertyview.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
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

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<DataResponseDTO<HotelDTO>> getHotelById(@PathVariable Long id) {
        var hotel = repo.getById(id);
        if (hotel == null) {
            var noHotel = new DataResponseDTO<HotelDTO>(null);
            return new ResponseEntity<>(noHotel, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(new DataResponseDTO<HotelDTO>(hotel));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DataResponseDTO<HotelDTO> createHotel(@RequestBody HotelDTO hotel) {
        var createdHotel = repo.create(hotel);
        return new DataResponseDTO<HotelDTO>(createdHotel);
    }
}
