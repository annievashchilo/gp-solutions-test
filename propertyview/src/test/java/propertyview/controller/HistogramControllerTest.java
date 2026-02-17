package propertyview.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import propertyview.dto.DataResponseDTO;
import propertyview.dto.HotelDTO;
import propertyview.repository.H2HotelRepository;
import propertyview.repository.HotelRepository;
import propertyview.usecase.GetSqlDbConnectionUseCase;

@SpringBootTest
public class HistogramControllerTest {

    HotelRepository hotelRepo;
    MockMvc mockMvc;
    ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new HistogramController()).build();
        this.objectMapper = new ObjectMapper();
        GetSqlDbConnectionUseCase getSqlDbConnectionUseCase = new GetSqlDbConnectionUseCase();
        var query =
                "CREATE TABLE IF NOT EXISTS hotels ("
                        + "id BIGINT AUTO_INCREMENT PRIMARY KEY,"
                        + "name VARCHAR NOT NULL,"
                        + "city VARCHAR NOT NULL,"
                        + "amenities VARCHAR NOT NULL,"
                        + "CONSTRAINT hotel_name UNIQUE (name)"
                        + ");";
        try (var conn = getSqlDbConnectionUseCase.execute()) {
            conn.prepareStatement(query).execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        this.hotelRepo = new H2HotelRepository();
        this.hotelRepo.deleteAll();
    }

    @AfterAll
    static void tearDown() {
        GetSqlDbConnectionUseCase getSqlDbConnectionUseCase = new GetSqlDbConnectionUseCase();
        var query = "drop table if exists hotels";
        try (var conn = getSqlDbConnectionUseCase.execute()) {
            conn.prepareStatement(query).execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGroupHotelsByAmenities() throws Exception {
        // ARRANGE

        installHotels();

        // ACT

        var result =
                mockMvc.perform(get("/property-view/histogram/amenities"))
                        .andExpect(status().isOk())
                        .andReturn();

        // ASSERT

        var response = result.getResponse();
        var textContent = response.getContentAsString();

        var payload =
                objectMapper.readValue(
                        textContent, new TypeReference<DataResponseDTO<Map<String, Long>>>() {});
        var amenities = payload.getData();
        assertNotNull(amenities);
        assertEquals(6, amenities.size());
        assertEquals(1, amenities.get("No Smoking"));
        assertEquals(2, amenities.get("Free WiFi"));
    }

    @Test
    void testGroupHotelsByCity() throws Exception {
        // ARRANGE

        installHotels();

        // ACT

        var result =
                mockMvc.perform(get("/property-view/histogram/city"))
                        .andExpect(status().isOk())
                        .andReturn();

        // ASSERT

        var response = result.getResponse();
        var textContent = response.getContentAsString();
        var payload =
                objectMapper.readValue(
                        textContent, new TypeReference<DataResponseDTO<Map<String, Long>>>() {});
        var cities = payload.getData();
        assertNotNull(cities);
        assertEquals(3, cities.size());
        assertEquals(1L, cities.get("Minsk"));
        assertEquals(2L, cities.get("Moskow"));
        assertEquals(1L, cities.get("Mogilev"));
    }

    private List<HotelDTO> installHotels() {
        var hotelA = new HotelDTO("Hotel A", "Minsk", List.of("No Smoking", "Free WiFi"));
        var hotelB = new HotelDTO("Hotel B", "Moskow", List.of("Free Parking", "Meeting Rooms"));
        var hotelC =
                new HotelDTO(
                        "Hotel C", "Moskow", List.of("Free Parking", "Room Service", "Concierge"));
        var hotelD = new HotelDTO("Hotel D", "Mogilev", List.of("Free WiFi"));

        var hotels = List.of(hotelA, hotelB, hotelC, hotelD);
        hotels.forEach(
                preparedHotel -> {
                    var createdHotel = hotelRepo.create(preparedHotel);
                    preparedHotel.setId(createdHotel.getId());
                });
        return hotels;
    }
}
