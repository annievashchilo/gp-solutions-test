package propertyview.repository;

import java.util.List;
import java.util.Map;
import propertyview.dto.HotelDTO;

public interface HotelRepository {
    HotelDTO create(HotelDTO hotel);

    void deleteAll();

    List<HotelDTO> getAll();

    List<HotelDTO> getAll(Map<String, String> filterBy);

    HotelDTO getById(Long id);

    HotelDTO update(HotelDTO hotel);
}
