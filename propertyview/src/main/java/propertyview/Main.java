package propertyview;

import java.util.List;
import propertyview.dto.HotelDTO;
import propertyview.repository.JsonFileHotelRepository;

public class Main {
    public static void main(String[] args) {
        var repo = new JsonFileHotelRepository();
        repo.deleteAll();

        var hotels = List.of(new HotelDTO("Hotel A"), new HotelDTO("Hotel B"));
        hotels.forEach(hotel -> repo.create(hotel));

        hotels = repo.getAll();
        hotels.forEach(hotel -> System.out.println(hotel.getId() + ": " + hotel.getName()));
    }
}
