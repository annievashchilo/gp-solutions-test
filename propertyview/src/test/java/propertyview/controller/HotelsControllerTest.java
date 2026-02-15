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

@SpringBootTest
public class HotelsControllerTest {

    MockMvc mockMvc;
    ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new HotelController()).build();
        this.objectMapper = new ObjectMapper();
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
}
