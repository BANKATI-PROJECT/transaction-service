package ma.ensa.transaction_service.controllers;

import ma.ensa.transaction_service.dtos.DepositRequest;
import ma.ensa.transaction_service.entities.Transaction;
import ma.ensa.transaction_service.model.RealCardCMI;
import ma.ensa.transaction_service.services.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transferFunds(@RequestParam Long fromPortefeuilleId,
                                                     @RequestParam Long toPortefeuilleId,
                                                     @RequestParam Double amount) {
        Transaction transaction = transactionService.executeTransfer(fromPortefeuilleId, toPortefeuilleId, amount);
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
