package propertyview.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import propertyview.dto.DataResponseDTO;
import propertyview.dto.HotelDTO;
import propertyview.repository.HotelRepository;
import propertyview.repository.JsonFileHotelRepository;

@SpringBootTest
public class HotelsControllerTest {

    HotelRepository repo;
    MockMvc mockMvc;
    ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new HotelController()).build();
        this.objectMapper = new ObjectMapper();
        this.repo = new JsonFileHotelRepository();
        this.repo.deleteAll();
    }

    @Test
    void testNoHotelsOnEmptyDb() throws Exception {
        var result =
                mockMvc.perform(get("/property-view/hotels"))
                        .andExpect(status().isOk())
                        .andReturn();

        var response = result.getResponse();
        var textContent = response.getContentAsString();
        var payload =
                objectMapper.readValue(
                        textContent, new TypeReference<DataResponseDTO<List<HotelDTO>>>() {});
        var hotels = payload.getData();
        assertNotNull(hotels);
        assertEquals(hotels.size(), 0);
    }

    @Test
    void testCanGetAllExistingHotels() throws Exception {
        var expectedHotels = installHotels();

        var result =
                mockMvc.perform(get("/property-view/hotels"))
                        .andExpect(status().isOk())
                        .andReturn();

        var response = result.getResponse();
        var textContent = response.getContentAsString();
        var payload =
                objectMapper.readValue(
                        textContent, new TypeReference<DataResponseDTO<List<HotelDTO>>>() {});
        var actualHotels = payload.getData();
        assertNotNull(actualHotels);
        assertEquals(expectedHotels.size(), actualHotels.size());
    }

    private List<HotelDTO> installHotels() {
        var hotels = List.of(new HotelDTO("Hotel A"), new HotelDTO("Hotel B"));
        hotels.forEach(hotel -> repo.create(hotel));
        return hotels;
    }
}
