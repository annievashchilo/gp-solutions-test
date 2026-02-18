package propertyview.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import propertyview.dto.DataResponseDTO;
import propertyview.dto.HotelDTO;

@SpringBootTest
public class HotelsControllerTest extends BaseControllerTest {

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
    void testUpdateAmenitiesForExistingHotel() throws Exception {
        var expectedHotels = installHotels();
        var expectedHotel = expectedHotels.get(0);

        var url = "/property-view/hotels/" + expectedHotel.getId() + "/amenities";
        var expectedAmenities = List.of("Free WiFi", "Room Service", "Yes Smoking");
        var json = objectMapper.writeValueAsString(expectedAmenities);
        var result =
                mockMvc.perform(post(url).content(json).contentType("application/json"))
                        .andExpect(status().isOk())
                        .andReturn();

        url = "/property-view/hotels/" + expectedHotel.getId();
        result = mockMvc.perform(get(url)).andExpect(status().isOk()).andReturn();

        var response = result.getResponse();
        json = response.getContentAsString();
        var payload =
                objectMapper.readValue(json, new TypeReference<DataResponseDTO<HotelDTO>>() {});
        var updatedHotel = payload.getData();
        assertNotNull(updatedHotel);
        assertEquals(expectedHotel.getId(), updatedHotel.getId());
        assertEquals(expectedAmenities, updatedHotel.getAmenities());
    }

    @Test
    void testCanCreateHotel() throws Exception {

        repo.deleteAll();

        var newHotel =
                new HotelDTO(
                        "Hotel A", "Ania", "Minsk", "Belarus", List.of("No Smoking", "Free WiFi"));
        var json = objectMapper.writeValueAsString(newHotel);

        var result =
                mockMvc.perform(
                                post("/property-view/hotels")
                                        .content(json)
                                        .contentType("application/json"))
                        .andExpect(status().isCreated())
                        .andReturn();

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

        assertEquals(createdHotel.getAmenities(), foundHotel.getAmenities());
    }
}
