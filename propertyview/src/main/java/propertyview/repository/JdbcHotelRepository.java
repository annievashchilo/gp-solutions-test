package propertyview.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import propertyview.dto.HotelDTO;

public class JdbcHotelRepository implements HotelRepository {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired private JdbcClient jdbcClient;

    @Override
    public HotelDTO create(HotelDTO hotel) {
        jdbcClient
                .sql(
                        "INSERT INTO hotels (name, brand, city, country, amenities) "
                                + "VALUES (:name, :brand, :city, :country, :amenities)")
                .param("name", hotel.getName())
                .param("brand", hotel.getBrand())
                .param("city", hotel.getCity())
                .param("country", hotel.getCountry())
                .param("amenities", hotel.getAmenitiesAsJson())
                .update();

        var createdHotel =
                jdbcClient
                        .sql("SELECT * FROM hotels WHERE name = :name")
                        .param("name", hotel.getName())
                        .query(mapRowToHotel)
                        .single();

        return createdHotel;
    }

    @Override
    public void deleteAll() {
        jdbcClient.sql("DELETE FROM hotels").update();
    }

    @Override
    public List<HotelDTO> getAll() {
        return getAll(null);
    }

    @Override
    public List<HotelDTO> getAll(Map<String, String> filterBy) {
        var sqlBuilder = new StringBuilder("SELECT * FROM hotels");

        var sqlParams = new HashMap<String, Object>();
        if (filterBy != null && filterBy.size() > 0) {
            sqlBuilder.append(" WHERE ");
            int i = 0;
            for (var entry : filterBy.entrySet()) {
                var key = entry.getKey();
                sqlBuilder.append(key + " ILIKE :" + key + " ");
                if (i > 0) {
                    sqlBuilder.append(" AND ");
                }
                sqlParams.put(key, entry.getValue());
            }
        }

        var sql = sqlBuilder.toString();
        var stmt = jdbcClient.sql(sql);
        for (var entry : sqlParams.entrySet()) {
            stmt = stmt.param(entry.getKey(), entry.getValue());
        }

        var hotels = stmt.query(mapRowToHotel).list();
        return hotels;
    }

    @Override
    public HotelDTO getById(Long id) {
        HotelDTO hotel = null;
        try {
            hotel =
                    jdbcClient
                            .sql("SELECT * FROM hotels WHERE id = :id")
                            .param("id", id)
                            .query(mapRowToHotel)
                            .single();
        } catch (EmptyResultDataAccessException e) {
        }
        return hotel;
    }

    @Override
    public HotelDTO update(HotelDTO hotel) {
        var hotelId = hotel.getId();
        if (hotelId == null || hotelId == 0) {
            return null;
        }

        var sqlSetParams = new HashMap<String, Object>();
        sqlSetParams.put("amenities", hotel.getAmenitiesAsJson());
        sqlSetParams.put("brand", hotel.getBrand());
        sqlSetParams.put("city", hotel.getCity());
        sqlSetParams.put("country", hotel.getCountry());
        sqlSetParams.put("name", hotel.getName());

        var sqlBuilder = new StringBuilder("UPDATE hotels SET");
        int i = 0;
        for (var entry : sqlSetParams.entrySet()) {
            if (i > 0) sqlBuilder.append(",");
            var key = entry.getKey();
            sqlBuilder.append(" ").append(key).append(" = :").append(key).append(" ");
            i++;
        }
        sqlBuilder.append(" WHERE id = :id");

        var stmt = jdbcClient.sql(sqlBuilder.toString());
        for (var entry : sqlSetParams.entrySet()) {
            stmt = stmt.param(entry.getKey(), entry.getValue());
        }
        stmt = stmt.param("id", hotel.getId());
        stmt.update();

        return getById(hotel.getId());
    }

    private static final RowMapper<HotelDTO> mapRowToHotel =
            (resultSet, rowNum) -> {
                HotelDTO hotel = new HotelDTO();
                hotel.setId(resultSet.getLong("id"));
                hotel.setName(resultSet.getString("name"));
                hotel.setBrand(resultSet.getString("brand"));
                hotel.setCity(resultSet.getString("city"));
                hotel.setCountry(resultSet.getString("country"));

                try {
                    var json = resultSet.getString("amenities");
                    List<String> amenities =
                            objectMapper.readValue(json, new TypeReference<List<String>>() {});
                    hotel.setAmenities(amenities);
                } catch (Exception e) {
                    hotel.setAmenities(new ArrayList<String>());
                }

                return hotel;
            };
}
