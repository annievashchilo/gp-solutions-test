package propertyview.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import propertyview.dto.HotelDTO;
import propertyview.usecase.GetSqlDbConnectionUseCase;

public class H2HotelRepositoryTest {
    @BeforeEach
    void setup() {
        GetSqlDbConnectionUseCase getSqlDbConnectionUseCase = new GetSqlDbConnectionUseCase();
        var query =
                "CREATE TABLE IF NOT EXISTS hotels ("
                        + "id BIGINT AUTO_INCREMENT PRIMARY KEY,"
                        + "name VARCHAR NOT NULL,"
                        + "CONSTRAINT hotel_name UNIQUE (name)"
                        + ");";
        try (var conn = getSqlDbConnectionUseCase.execute()) {
            conn.prepareStatement(query).execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    static void tearDown() {
        GetSqlDbConnectionUseCase getSqlDbConnectionUseCase = new GetSqlDbConnectionUseCase();
        var query = "drop table if exists hotels";
        try (var conn = getSqlDbConnectionUseCase.execute()) {
            conn.prepareStatement(query).execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testCanGetAllHotelsFromDb() throws Exception {
        // ARRANGE

        var repo = new H2HotelRepository();
        try {
            repo.create(new HotelDTO("Hotel A"));
            repo.create(new HotelDTO("Hotel B"));

            // ACT

            var result = repo.getAll();

            // ASSERT

            assertEquals(2, result.size());
            assertEquals("Hotel A", result.get(0).getName());
            assertEquals("Hotel B", result.get(1).getName());
        } finally {
            repo.deleteAll();
        }
    }
}
