package propertyview.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        var sqlInsert =
                "INSERT INTO hotels (name, brand, city, country, amenities) VALUES (?, ?, ?, ?, ?)";
        var sqlSelect = "SELECT * FROM hotels WHERE name = ?";
        Long createdId = null;
        try {
            Connection conn = getSqlDbConnectionUseCase.execute();
            var stmt = conn.prepareStatement(sqlInsert);
            stmt.setString(1, hotel.getName());
            stmt.setString(2, hotel.getBrand());
            stmt.setString(3, hotel.getCity());
            stmt.setString(4, hotel.getCountry());

            var objectMapper = new ObjectMapper();
            var amenities = objectMapper.writeValueAsString(hotel.getAmenities());
            stmt.setString(5, amenities);

            stmt.executeUpdate();

            var stmtSelect = conn.prepareStatement(sqlSelect);
            stmtSelect.setString(1, hotel.getName());
            var rs = stmtSelect.executeQuery();

            if (rs.next()) {
                createdId = rs.getLong("id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (createdId != null) {
            createdHotel = getById(createdId);
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
                var hotel = mapResultSetToHotelDTO(rs);
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
                hotel = mapResultSetToHotelDTO(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hotel;
    }

    @Override
    public HotelDTO update(HotelDTO hotel) {
        var sqlUpdate = "UPDATE hotels SET name = ?, city = ?, amenities = ? WHERE id = ?";
        try {
            Connection conn = getSqlDbConnectionUseCase.execute();
            var stmt = conn.prepareStatement(sqlUpdate);
            stmt.setString(1, hotel.getName());
            stmt.setString(2, hotel.getCity());

            var objectMapper = new ObjectMapper();
            var amenities = objectMapper.writeValueAsString(hotel.getAmenities());
            stmt.setString(3, amenities);

            stmt.setLong(4, hotel.getId());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getById(hotel.getId());
    }

    private HotelDTO mapResultSetToHotelDTO(ResultSet rs) throws SQLException {
        var objectMapper = new ObjectMapper();
        var json = rs.getString("amenities");
        List<String> amenities = new ArrayList<String>();
        try {
            amenities.addAll(0, objectMapper.readValue(json, new TypeReference<List<String>>() {}));
        } catch (JsonMappingException e) {
        } catch (JsonProcessingException e) {
        }
        return new HotelDTO(
                rs.getLong("id"), rs.getString("name"), rs.getString("city"), amenities);
    }
}
