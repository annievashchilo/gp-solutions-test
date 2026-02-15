package propertyview.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import propertyview.dto.DataResponseDTO;
import propertyview.dto.HealthcheckDTO;

@RestController
@RequestMapping("/healthcheck")
public class HealthcheckController {

    @GetMapping
    public DataResponseDTO<HealthcheckDTO> handle() {
        return new DataResponseDTO<HealthcheckDTO>(new HealthcheckDTO("ok"));
    }
}
