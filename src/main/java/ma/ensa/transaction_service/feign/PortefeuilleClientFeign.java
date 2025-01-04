package ma.ensa.transaction_service.feign;

import ma.ensa.transaction_service.model.Portefeuille;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "portefeuille-service")
public interface PortefeuilleClientFeign {
    @GetMapping("/api/portefeuilles/{id}")
    Portefeuille getPortefeuille(@PathVariable("id") Long id);

    @PutMapping("/api/portefeuilles/{id}")
    Portefeuille updatePortefeuille(@PathVariable("id") Long id, @RequestBody Portefeuille portefeuille);
}
