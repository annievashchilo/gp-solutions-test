package propertyview.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;

public class GetSqlDbConnectionUseCaseTest {
    @Test
    public void test() throws Exception {
        var getDbConnection = new GetSqlDbConnectionUseCase();
        try (Connection conn = getDbConnection.execute()) {
            if (conn == null) {
                fail("Failed to establish a database connection.");
            } else {
                DatabaseMetaData meta = conn.getMetaData();
                assertEquals(
                        "H2 JDBC Driver", meta.getDriverName(), "Unexpected JDBC driver name.");

                ResultSet rs = conn.prepareStatement("SELECT 1").executeQuery();
                String result = "";
                if (!rs.next()) {
                    fail("Expected at least one row in the result set.");
                }
                result = rs.getString(1);
                assertEquals("1", result, "Unexpected result from test query.");
            }
        } catch (SQLException e) {
            fail("SQL Exception: " + e.getMessage());
        }
    }
}
