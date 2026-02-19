package propertyview.usecase;

import java.sql.Connection;
import java.sql.SQLException;

public class InitDbUseCase {
    private final Connection conn;

    public InitDbUseCase(Connection conn) {
        this.conn = conn;
    }

    public void execute() throws SQLException {
        var query =
                "CREATE TABLE IF NOT EXISTS hotels ("
                        + "id BIGINT AUTO_INCREMENT PRIMARY KEY,"
                        + "name VARCHAR NOT NULL,"
                        + "brand VARCHAR NOT NULL,"
                        + "city VARCHAR NOT NULL,"
                        + "country VARCHAR NOT NULL,"
                        + "amenities VARCHAR NOT NULL,"
                        + "CONSTRAINT hotel_name UNIQUE (name)"
                        + ");";
        conn.prepareStatement(query).execute();
    }
}
