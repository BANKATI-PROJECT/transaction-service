package ma.ensa.transaction_service.controllers;

import ma.ensa.transaction_service.entities.Transaction;
import ma.ensa.transaction_service.services.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
