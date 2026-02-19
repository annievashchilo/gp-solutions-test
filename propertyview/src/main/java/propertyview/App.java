package propertyview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import propertyview.usecase.GetSqlDbConnectionUseCase;
import propertyview.usecase.InitDbUseCase;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}

@Component
@Order(0)
class CustomApplicationListener implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println("💕💕💕💕💕💕💕💕💕💕💕💕💕💕");
        GetSqlDbConnectionUseCase getSqlDbConnectionUseCase = new GetSqlDbConnectionUseCase();

        try (var conn = getSqlDbConnectionUseCase.execute()) {
            var initDb = new InitDbUseCase(conn);
            initDb.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
