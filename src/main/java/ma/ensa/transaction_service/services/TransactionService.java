package ma.ensa.transaction_service.services;

import ma.ensa.transaction_service.entities.Transaction;
import ma.ensa.transaction_service.enums.TransactionStatus;
import ma.ensa.transaction_service.enums.TransactionType;
import ma.ensa.transaction_service.feign.PortefeuilleClientFeign;
import ma.ensa.transaction_service.model.Portefeuille;
import ma.ensa.transaction_service.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private PortefeuilleClientFeign portefeuilleClientFeign;



    public Transaction executeTransfer(Long fromPortefeuilleId, Long toPortefeuilleId, Double amount) {
        // Vérification des portefeuilles
        Portefeuille fromPortefeuille = portefeuilleClientFeign.getPortefeuille(fromPortefeuilleId);
        Portefeuille toPortefeuille = portefeuilleClientFeign.getPortefeuille(toPortefeuilleId);

        if (fromPortefeuille == null || toPortefeuille == null) {
            throw new IllegalArgumentException("One or both portefeuilles not found");
        }

        // Vérification du solde
        if (fromPortefeuille.getSolde() < amount) {
            throw new IllegalStateException("Insufficient balance in the source portefeuille");
        }

        // Débiter et créditer
        fromPortefeuille.setSolde(fromPortefeuille.getSolde() - amount);
        toPortefeuille.setSolde(toPortefeuille.getSolde() + amount);

        portefeuilleClientFeign.updatePortefeuille(fromPortefeuilleId, fromPortefeuille);
        portefeuilleClientFeign.updatePortefeuille(toPortefeuilleId, toPortefeuille);

        // Sauvegarder la transaction
        Transaction transaction = new Transaction();
        transaction.setFromPortefeuilleId(fromPortefeuilleId);
        transaction.setToPortefeuilleId(toPortefeuilleId);
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setType(TransactionType.TRANSFER);

        return transactionRepository.save(transaction);
    }
}

