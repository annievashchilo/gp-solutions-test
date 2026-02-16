package propertyview.repository;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import propertyview.dto.HotelDTO;
import propertyview.usecase.GetSqlDbConnectionUseCase;

public class H2HotelRepository implements HotelRepository {
    private static final GetSqlDbConnectionUseCase getSqlDbConnectionUseCase =
            new GetSqlDbConnectionUseCase();

    @Override
    public HotelDTO create(HotelDTO hotel) {
        HotelDTO createdHotel = null;
        var sqlInsert = "INSERT INTO hotels (name, city) VALUES (?, ?)";
        var sqlSelect = "SELECT * FROM hotels WHERE name = ?";
        try {
            Connection conn = getSqlDbConnectionUseCase.execute();
            var stmt = conn.prepareStatement(sqlInsert);
            stmt.setString(1, hotel.getName());
            stmt.setString(2, hotel.getCity());
            stmt.executeUpdate();

            var stmtSelect = conn.prepareStatement(sqlSelect);
            stmtSelect.setString(1, hotel.getName());
            var rs = stmtSelect.executeQuery();

            if (rs.next()) {
                var id = rs.getLong("id");
                var name = rs.getString("name");
                var city = rs.getString("city");
                createdHotel = new HotelDTO(id, name, city);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return createdHotel;
    }

    @Override
    public void deleteAll() {
        var sql = "DELETE FROM hotels";
        try {
            Connection conn = getSqlDbConnectionUseCase.execute();
            var stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<HotelDTO> getAll() {
        List<HotelDTO> hotels = new ArrayList<>();
        var sql = "SELECT * FROM hotels";
        try {
            Connection conn = getSqlDbConnectionUseCase.execute();
            var stmt = conn.prepareStatement(sql);
            var rs = stmt.executeQuery();
            while (rs.next()) {

                var hotel =
                        new HotelDTO(rs.getLong("id"), rs.getString("name"), rs.getString("city"));
                hotels.add(hotel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hotels;
    }

    @Override
    public HotelDTO getById(Long id) {
        HotelDTO hotel = null;
        var sql = "SELECT * FROM hotels WHERE id = ?";
        try {
            Connection conn = getSqlDbConnectionUseCase.execute();
            var stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                hotel = new HotelDTO(rs.getLong("id"), rs.getString("name"), rs.getString("city"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hotel;
    }

    public HotelDTO getByName(String name) {
        HotelDTO hotel = null;
        var sql = "SELECT * FROM hotels WHERE name = ?";
        try {
            Connection conn = getSqlDbConnectionUseCase.execute();
            var stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                hotel = new HotelDTO(rs.getLong("id"), rs.getString("name"), rs.getString("city"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hotel;
    }
}
