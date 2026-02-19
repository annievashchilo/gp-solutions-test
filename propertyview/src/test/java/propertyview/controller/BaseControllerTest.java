package propertyview.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import propertyview.dto.HotelDTO;
import propertyview.repository.H2HotelRepository;
import propertyview.repository.HotelRepository;
import propertyview.usecase.GetSqlDbConnectionUseCase;
import propertyview.usecase.InitDbUseCase;

public class BaseControllerTest {

    protected HotelRepository repo;
    protected MockMvc mockMvc;
    protected MockMvc mockMvcHistogram;
    protected MockMvc mockMvcSearch;
    protected ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new HotelController()).build();
        this.mockMvcHistogram = MockMvcBuilders.standaloneSetup(new HistogramController()).build();
        this.mockMvcSearch = MockMvcBuilders.standaloneSetup(new SearchController()).build();

        this.objectMapper = new ObjectMapper();
        GetSqlDbConnectionUseCase getSqlDbConnectionUseCase = new GetSqlDbConnectionUseCase();

        try (var conn = getSqlDbConnectionUseCase.execute()) {
            var initDb = new InitDbUseCase(conn);
            initDb.execute();
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

    protected List<HotelDTO> installHotels() {
        var hotelA =
                new HotelDTO(
                        "Hotel A", "Ania", "Minsk", "Belarus", List.of("No Smoking", "Free WiFi"));
        var hotelB =
                new HotelDTO(
                        "Hotel B",
                        "Boris",
                        "Moskow",
                        "Russia",
                        List.of("Free Parking", "Meeting Rooms"));
        var hotelC =
                new HotelDTO(
                        "Hotel C",
                        "Carl",
                        "Moskow",
                        "Russia",
                        List.of("Free Parking", "Room Service", "Concierge"));
        var hotelD = new HotelDTO("Hotel D", "Ania", "Mogilev", "Belarus", List.of("Free WiFi"));

        var hotelE =
                new HotelDTO(
                        "Hotel E",
                        "Barbariska",
                        "Minsk",
                        "Belarus",
                        List.of("Free WiFi", "Pet Friendly", "Free Parking"));

        var hotels = List.of(hotelA, hotelB, hotelC, hotelD, hotelE);
        hotels.forEach(
                preparedHotel -> {
                    var createdHotel = repo.create(preparedHotel);
                    preparedHotel.setId(createdHotel.getId());
                });
        return hotels;
    }
}
