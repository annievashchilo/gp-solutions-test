package propertyview.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.util.UriComponentsBuilder;
import propertyview.dto.DataResponseDTO;
import propertyview.dto.HotelDTO;

@SpringBootTest
public class SearchControllerTest extends BaseControllerTest {

    @Test
    void testSearchByCity() throws Exception {
        var expectedHotels = installHotels();
        var expectedHotel = expectedHotels.get(0);

        String url = "/property-view/search";
        String key = "city";
        String value = expectedHotel.getCity();
        URI uri = UriComponentsBuilder.fromUriString(url).queryParam(key, value).build().toUri();

        var result = mockMvc.perform(get(uri)).andExpect(status().isOk()).andReturn();

        var response = result.getResponse();
        var textContent = response.getContentAsString();
        var payload =
                objectMapper.readValue(
                        textContent, new TypeReference<DataResponseDTO<List<HotelDTO>>>() {});

        var foundHotels = payload.getData();
        assertNotNull(foundHotels);
        assertEquals(2, foundHotels.size());

        var foundHotel = foundHotels.get(0);
        assertEquals(expectedHotel.getCity(), foundHotel.getCity());
    }

    @Test
    void testSearchByName() throws Exception {
        var expectedHotels = installHotels();
        var expectedHotel = expectedHotels.get(0);

        String url = "/property-view/search";
        String key = "name";
        String value = expectedHotel.getName();
        URI uri = UriComponentsBuilder.fromUriString(url).queryParam(key, value).build().toUri();

        var result = mockMvc.perform(get(uri)).andExpect(status().isOk()).andReturn();

        var response = result.getResponse();
        var textContent = response.getContentAsString();
        var payload =
                objectMapper.readValue(
                        textContent, new TypeReference<DataResponseDTO<List<HotelDTO>>>() {});

        var foundHotels = payload.getData();
        assertNotNull(foundHotels);
        assertEquals(1, foundHotels.size());

        var foundHotel = foundHotels.get(0);
        assertEquals(expectedHotel.getName(), foundHotel.getName());
    }

    @Test
    void testSearchByCountry() throws Exception {
        var expectedHotels = installHotels();
        var expectedHotel = expectedHotels.get(0);

        String url = "/property-view/search";
        String key = "country";
        String value = expectedHotel.getCountry();
        URI uri = UriComponentsBuilder.fromUriString(url).queryParam(key, value).build().toUri();

        var result = mockMvc.perform(get(uri)).andExpect(status().isOk()).andReturn();

        var response = result.getResponse();
        var textContent = response.getContentAsString();
        var payload =
                objectMapper.readValue(
                        textContent, new TypeReference<DataResponseDTO<List<HotelDTO>>>() {});

        var foundHotels = payload.getData();
        assertNotNull(foundHotels);
        assertEquals(3, foundHotels.size());

        var foundHotel = foundHotels.get(0);
        assertEquals(expectedHotel.getCountry(), foundHotel.getCountry());
    }

    @Test
    void testSearchByBrand() throws Exception {
        var expectedHotels = installHotels();
        var expectedHotel = expectedHotels.get(0);

        String url = "/property-view/search";
        String key = "brand";
        String value = expectedHotel.getBrand();
        URI uri = UriComponentsBuilder.fromUriString(url).queryParam(key, value).build().toUri();

        var result = mockMvc.perform(get(uri)).andExpect(status().isOk()).andReturn();

        var response = result.getResponse();
        var textContent = response.getContentAsString();
        var payload =
                objectMapper.readValue(
                        textContent, new TypeReference<DataResponseDTO<List<HotelDTO>>>() {});

        var foundHotels = payload.getData();
        assertNotNull(foundHotels);
        assertEquals(2, foundHotels.size());

        var foundHotel = foundHotels.get(0);
        assertEquals(expectedHotel.getBrand(), foundHotel.getBrand());
    }
}
