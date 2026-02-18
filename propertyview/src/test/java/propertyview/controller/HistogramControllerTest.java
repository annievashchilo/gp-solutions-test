package propertyview.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import propertyview.dto.DataResponseDTO;

@SpringBootTest
public class HistogramControllerTest extends BaseControllerTest {

    @Test
    void testGroupByAmenities() throws Exception {
        installHotels();

        var result =
                mockMvcHistogram
                        .perform(get("/property-view/histogram/amenities"))
                        .andExpect(status().isOk())
                        .andReturn();

        var response = result.getResponse();
        var textContent = response.getContentAsString();

        var payload =
                objectMapper.readValue(
                        textContent, new TypeReference<DataResponseDTO<Map<String, Long>>>() {});
        var amenities = payload.getData();
        assertNotNull(amenities);
        assertEquals(7, amenities.size());
        assertEquals(1, amenities.get("No Smoking"));
        assertEquals(3, amenities.get("Free WiFi"));
    }

    @Test
    void testGroupByCity() throws Exception {

        installHotels();

        var result =
                mockMvcHistogram
                        .perform(get("/property-view/histogram/city"))
                        .andExpect(status().isOk())
                        .andReturn();

        var response = result.getResponse();
        var textContent = response.getContentAsString();
        var payload =
                objectMapper.readValue(
                        textContent, new TypeReference<DataResponseDTO<Map<String, Long>>>() {});
        var cities = payload.getData();
        assertNotNull(cities);
        assertEquals(3, cities.size());
        assertEquals(1L, cities.get("Mogilev"));
        assertEquals(2L, cities.get("Minsk"));
        assertEquals(2L, cities.get("Moskow"));
    }
}
