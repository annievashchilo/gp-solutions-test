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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import propertyview.dto.DataResponseDTO;
import propertyview.dto.HotelDTO;
import propertyview.repository.H2HotelRepository;
import propertyview.repository.HotelRepository;

@RestController
@RequestMapping("/property-view/hotels")
@Tag(name = "Hotel Controller", description = "APIs for managing hotels")
public class HotelController {
    private static final HotelRepository repo = new H2HotelRepository();

    @Operation(summary = "Get all hotels", description = "Get all existing hotels from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hotels found",
                    content = @Content(schema = @Schema(implementation = HotelDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(schema = @Schema()))
    })
    @GetMapping
    public DataResponseDTO<List<HotelDTO>> getAllHotels() {
        var hotels = repo.getAll();
        return new DataResponseDTO<List<HotelDTO>>(hotels);
    }


    @Operation(summary = "Get hotel", description = "Get a specific hotel by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hotel successfully found",
                    content = @Content(schema = @Schema(implementation = HotelDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(schema = @Schema()))
    })
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

    @Operation(summary = "Create new hotel", description = "Add new hotel to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hotel created successfully",
                    content = @Content(schema = @Schema(implementation = HotelDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(schema = @Schema()))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DataResponseDTO<HotelDTO> createHotel(@RequestBody HotelDTO hotel) {
        var createdHotel = repo.create(hotel);
        return new DataResponseDTO<HotelDTO>(createdHotel);
    }

    @Operation(summary = "Update amenities for hotel", description = "Add new amenities for specific hotel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Amenities updated successfully",
                    content = @Content(schema = @Schema(implementation = HotelDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(schema = @Schema()))
    })
    @PostMapping("/{id}/amenities")
    @ResponseBody
    public DataResponseDTO<HotelDTO> updateHotelAmenities(
            @PathVariable Long id, @RequestBody List<String> newAmenities) {
        var hotel = repo.getById(id);

        if (hotel == null) {
            return null;
        }

        hotel.setAmenities(newAmenities);
        var updatedHotel = repo.update(hotel);
        return new DataResponseDTO<HotelDTO>(updatedHotel);
    }
}
