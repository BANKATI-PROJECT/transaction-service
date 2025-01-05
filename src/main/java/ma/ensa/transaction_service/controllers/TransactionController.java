package ma.ensa.transaction_service.controllers;

import ma.ensa.transaction_service.dtos.DepositRequest;
import ma.ensa.transaction_service.entities.Transaction;
import ma.ensa.transaction_service.model.RealCardCMI;
import ma.ensa.transaction_service.services.TransactionService;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final RestTemplate restTemplate;

    public TransactionController(TransactionService transactionService,RestTemplate restTemplate) {
        this.transactionService = transactionService;
        this.restTemplate = restTemplate;
    }

    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transferFunds(@RequestParam Long fromPortefeuilleId,
                                                     @RequestParam Long toPortefeuilleId,
                                                     @RequestParam Double amount) {
        Transaction transaction = transactionService.executeTransfer(fromPortefeuilleId, toPortefeuilleId, amount);
        return ResponseEntity.ok(transaction);
    }

    @PostMapping("/facture")
    public ResponseEntity<Transaction> createFactureTransaction(
            @RequestParam Long fromPortefeuilleId,
            @RequestParam Double amount,
            @RequestBody Object requestBody // Dynamically capture the request body
    ) {
        // Create a transaction
        Transaction transaction = transactionService.createFactureTransaction(fromPortefeuilleId, amount);

        // Forward the request to the remote server
        String remoteServerUrl = "https://iam-remote-server.example.com/"; // Replace with the actual URL
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(remoteServerUrl, entity, String.class);
            if(response.getStatusCode() == HttpStatusCode.valueOf(200)){
                transactionService.completeFactureTransaction(transaction.getId());
            }
            // Handle the remote server response if needed
        } catch (Exception e) {
            // Handle exceptions, e.g., logging or returning an error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok(transaction);
    }
    @PostMapping("/deposit")
    public ResponseEntity<String> depositToPortefeuille(@RequestBody DepositRequest request) {
        try {
            transactionService.depositToPortefeuille(request);
            return ResponseEntity.ok("Transaction effectuée avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/card/{saveToken}/{numCard}")
    public RealCardCMI getCard(@PathVariable("saveToken") String saveToken,
                               @PathVariable("numCard") String numCard) {
        // Appel du service transaction pour obtenir les détails de la carte
        return transactionService.getCardDetails(saveToken, numCard);
    }
}
