package propertyview;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}

@Component
@Order(0)
class InitDbListener implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired JdbcClient jdbcClient;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println("💕💕💕💕💕💕💕💕💕💕💕💕💕💕");
        try {
            jdbcClient
                    .sql(
                            "CREATE TABLE IF NOT EXISTS hotels ("
                                    + "id BIGINT AUTO_INCREMENT PRIMARY KEY,"
                                    + "name VARCHAR NOT NULL,"
                                    + "brand VARCHAR NOT NULL,"
                                    + "city VARCHAR NOT NULL,"
                                    + "country VARCHAR NOT NULL,"
                                    + "amenities VARCHAR NOT NULL,"
                                    + "CONSTRAINT hotel_name UNIQUE (name)"
                                    + ");")
                    .update();
        } catch (Exception e) {
            System.err.println(
                    "\n\n🚩 🚩 🚩 🚩\nFailed to initialize database: "
                            + e.getMessage()
                            + "\n🚩 🚩 🚩 🚩\n\n");
            e.printStackTrace();
            SpringApplication.exit(event.getApplicationContext(), () -> 1);
        }
    }
}
