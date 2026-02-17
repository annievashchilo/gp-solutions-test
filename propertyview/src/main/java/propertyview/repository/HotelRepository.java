package propertyview.repository;

import java.util.List;
import propertyview.dto.HotelDTO;

public interface HotelRepository {
    List<HotelDTO> getAll();

    HotelDTO getById(Long id);

    HotelDTO create(HotelDTO hotel);

    HotelDTO update(HotelDTO hotel);

    void deleteAll();
}
