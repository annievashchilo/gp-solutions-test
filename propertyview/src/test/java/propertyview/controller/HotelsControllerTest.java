package propertyview.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
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
public class HotelsControllerTest {

    HotelRepository repo;
    MockMvc mockMvc;
    ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new HotelController()).build();
        this.objectMapper = new ObjectMapper();
        GetSqlDbConnectionUseCase getSqlDbConnectionUseCase = new GetSqlDbConnectionUseCase();
        var query =
                "CREATE TABLE IF NOT EXISTS hotels ("
                        + "id BIGINT AUTO_INCREMENT PRIMARY KEY,"
                        + "name VARCHAR NOT NULL,"
                        + "CONSTRAINT hotel_name UNIQUE (name)"
                        + ");";
        try (var conn = getSqlDbConnectionUseCase.execute()) {
            conn.prepareStatement(query).execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        this.repo = new H2HotelRepository();
        this.repo.deleteAll();
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

    @Test
    void testCanGetExistingHotel() throws Exception {
        var expectedHotels = installHotels();
        var expectedHotel = expectedHotels.get(0);

        var url = "/property-view/hotels/" + expectedHotel.getId();
        var result = mockMvc.perform(get(url)).andExpect(status().isOk()).andReturn();

        var response = result.getResponse();
        var textContent = response.getContentAsString();
        var payload =
                objectMapper.readValue(
                        textContent, new TypeReference<DataResponseDTO<HotelDTO>>() {});
        var foundHotel = payload.getData();
        assertNotNull(foundHotel);
        assertEquals(expectedHotel.getId(), foundHotel.getId());
        assertEquals(expectedHotel.getName(), foundHotel.getName());
    }

    @Test
    void testGetNonExistingHotel() throws Exception {
        var result =
                mockMvc.perform(get("/property-view/hotels/123124123"))
                        .andExpect(status().isNotFound())
                        .andReturn();

        var response = result.getResponse();
        var textContent = response.getContentAsString();
        var payload =
                objectMapper.readValue(
                        textContent, new TypeReference<DataResponseDTO<HotelDTO>>() {});
        var foundHotel = payload.getData();
        assertNull(foundHotel);
    }

    @Test
    void testCanCreateHotel() throws Exception {
        // arrange

        repo.deleteAll();

        var newHotel = new HotelDTO("Hotel C");
        var json = objectMapper.writeValueAsString(newHotel);

        // act

        var result =
                mockMvc.perform(
                                post("/property-view/hotels")
                                        .content(json)
                                        .contentType("application/json"))
                        .andExpect(status().isCreated())
                        .andReturn();

        // assert

        var response = result.getResponse();
        var textContent = response.getContentAsString();
        var payload =
                objectMapper.readValue(
                        textContent, new TypeReference<DataResponseDTO<HotelDTO>>() {});
        var createdHotel = payload.getData();
        assertNotNull(createdHotel);
        assertNotNull(createdHotel.getId());
        assertEquals(newHotel.getName(), createdHotel.getName());

        var foundHotel = repo.getById(createdHotel.getId());
        assertNotNull(foundHotel);
        assertNotNull(foundHotel.getId());
        assertEquals(createdHotel.getId(), foundHotel.getId());
        assertEquals(createdHotel.getName(), foundHotel.getName());
    }

    private List<HotelDTO> installHotels() {
        var hotels = List.of(new HotelDTO(1L, "Hotel A"), new HotelDTO(2L, "Hotel B"));
        hotels.forEach(hotel -> repo.create(hotel));
        return hotels;
    }
}
