package propertyview.repository;

import java.util.List;
import propertyview.dto.HotelDTO;

public interface HotelRepository {
    List<HotelDTO> getAll();

    void create(HotelDTO hotel);

    void deleteAll();
}
