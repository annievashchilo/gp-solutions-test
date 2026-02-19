package propertyview;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import propertyview.repository.HotelRepository;
import propertyview.repository.JdbcHotelRepository;

@Configuration
public class Config {
    @Bean
    public HotelRepository provideHotelRepo() {
        return new JdbcHotelRepository();
    }
}
