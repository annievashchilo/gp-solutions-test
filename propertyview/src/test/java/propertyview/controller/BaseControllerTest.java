package propertyview.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import propertyview.dto.HotelDTO;
import propertyview.repository.HotelRepository;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration("src/main/java")
@ContextConfiguration
public class BaseControllerTest {
    protected static final ObjectMapper objectMapper = new ObjectMapper();
    protected MockMvc mockMvc;

    @Autowired protected HotelRepository hotelRepo;

    @BeforeEach
    void setUp(WebApplicationContext context) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @AfterEach
    void tearDown(TestInfo testInfo) {
        String testName = testInfo.getDisplayName();

        var leftOvers = hotelRepo.getAll();
        if (leftOvers.size() > 0) {
            System.err.println("WARNING: Hotels left after test: " + testName);
        }

        hotelRepo.deleteAll();
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
                    var createdHotel = hotelRepo.create(preparedHotel);
                    preparedHotel.setId(createdHotel.getId());
                });
        return hotels;
    }
}
