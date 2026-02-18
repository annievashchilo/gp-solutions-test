package propertyview.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import propertyview.controller.BaseControllerTest;
import propertyview.dto.HotelDTO;

public class H2HotelRepositoryTest extends BaseControllerTest {

    @Test
    void testCanGetAllHotelsFromDb() throws Exception {
        // ARRANGE

        var repo = new H2HotelRepository();
        try {
            var hotelA =
                    new HotelDTO(
                            "Hotel A",
                            "Ania",
                            "Minsk",
                            "Belarus",
                            List.of("No Smoking", "Free WiFi"));
            var hotelB =
                    new HotelDTO(
                            "Hotel B",
                            "Boris",
                            "Moskow",
                            "Russia",
                            List.of("Free Parking", "Meeting Rooms"));
            repo.create(hotelA);
            repo.create(hotelB);

            // ACT

            var result = repo.getAll();

            // ASSERT

            assertEquals(2, result.size());
            assertEquals("Hotel A", result.get(0).getName());
            assertEquals("Hotel B", result.get(1).getName());
        } finally {
            repo.deleteAll();
        }
    }
}
