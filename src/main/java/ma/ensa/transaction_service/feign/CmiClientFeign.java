package ma.ensa.transaction_service.feign;

import ma.ensa.transaction_service.model.RealCardCMI;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "cmi-service")
public interface CmiClientFeign {
    @GetMapping("/cmi-service/{saveToken}/{numCard}")
    RealCardCMI getCardBySaveTokenAndNumber(@PathVariable("saveToken") String saveToken,
                                            @PathVariable("numCard") String numCard);

    @PutMapping("/cmi-service")
    void updateCard(@RequestBody RealCardCMI card);
}
