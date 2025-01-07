package ma.ensa.transaction_service.feign;

import ma.ensa.transaction_service.model.Client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "accountManagement-service",url = "https://5ed8-105-71-135-223.ngrok-free.app")
public interface AccountManagementClientFeign {
    @GetMapping("/api/client/{id}")
    Client getClientById(@PathVariable("id") Long id);
}
