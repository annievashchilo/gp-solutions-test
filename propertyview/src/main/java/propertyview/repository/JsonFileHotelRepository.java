package propertyview.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import propertyview.dto.HotelDTO;

public class JsonFileHotelRepository implements HotelRepository {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Path storageFile =
            Paths.get(System.getProperty("user.dir"), "..", ".local", "hotels.json");

    @Override
    public List<HotelDTO> getAll() {
        try {
            String jsonContent = new String(Files.readAllBytes(storageFile));
            HotelDTO[] hotels = objectMapper.readValue(jsonContent, HotelDTO[].class);
            var result = new ArrayList<HotelDTO>();
            if (hotels != null) {
                result.addAll(Arrays.asList(hotels));
            }
            return result;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public void create(HotelDTO hotel) {
        try {
            var hotels = getAll();
            hotels.add(hotel);
            String jsonContent =
                    objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(hotels);
            Files.write(storageFile, jsonContent.getBytes());
        } catch (Exception e) {
            System.err.println("Error saving hotel: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        try {
            Files.delete(storageFile);
        } catch (Exception e) {
            System.err.println("Error deleting all hotels: " + e.getMessage());
        }
    }
}
