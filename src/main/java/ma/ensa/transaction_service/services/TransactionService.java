package ma.ensa.transaction_service.services;

import ma.ensa.transaction_service.dtos.DepositRequest;
import ma.ensa.transaction_service.entities.Transaction;
import ma.ensa.transaction_service.enums.TransactionStatus;
import ma.ensa.transaction_service.enums.TransactionType;
import ma.ensa.transaction_service.feign.AccountManagementClientFeign;
import ma.ensa.transaction_service.feign.CmiClientFeign;
import ma.ensa.transaction_service.feign.PortefeuilleClientFeign;
import ma.ensa.transaction_service.model.Client;
import ma.ensa.transaction_service.model.Portefeuille;
import ma.ensa.transaction_service.model.RealCardCMI;
import ma.ensa.transaction_service.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
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
    @Value("${topic.transaction}")
    private String transactionTopic;
    @Value("${topic.transaction.deposit}")
    private String transactionTopicDeposit;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private AccountManagementClientFeign accountManagementClientFeign;
    @Autowired
    private CmiClientFeign cmiClientFeign;



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

        Client fromClient = accountManagementClientFeign.getClientById(fromPortefeuille.getClientId());
        Client toClient = accountManagementClientFeign.getClientById(toPortefeuille.getClientId());

        // Sauvegarder la transaction
        Transaction transaction = new Transaction();
        transaction.setFromPortefeuilleId(fromPortefeuilleId);
        transaction.setToPortefeuilleId(toPortefeuilleId);
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setType(TransactionType.TRANSFER);

        // Publier l'événement dans Kafka avec les emails
        String eventFrom = String.format(
                "{\"clientId\": \"%s\", \"email\": \"%s\", \"message\": \"Votre portefeuille a été débité de %.2f. Nouveau solde: %.2f\"}",
                fromClient.getId(), fromClient.getEmail(), amount, fromPortefeuille.getSolde()
        );

        String eventTo = String.format(
                "{\"clientId\": \"%s\", \"email\": \"%s\", \"message\": \"Votre portefeuille a été crédité de %.2f. Nouveau solde: %.2f\"}",
                toClient.getId(), toClient.getEmail(), amount, toPortefeuille.getSolde()
        );


        kafkaTemplate.send(transactionTopic, eventFrom);
        kafkaTemplate.send(transactionTopic, eventTo);

        return transactionRepository.save(transaction);
    }

    public void depositToPortefeuille(DepositRequest request) {

        Portefeuille portefeuille = portefeuilleClientFeign.getPortefeuille(request.getClientId());
        if (portefeuille == null) {
            throw new IllegalArgumentException("Portefeuille introuvable");
        }
        Client client = accountManagementClientFeign.getClientById(request.getClientId());

        RealCardCMI card = cmiClientFeign.getCardBySaveTokenAndNumber(request.getSaveToken(), request.getNumCard());
        if (card == null || card.getSolde() < request.getAmount()) {
            throw new IllegalArgumentException("Carte invalide ou solde insuffisant");
        }


        card.setSolde(card.getSolde() - request.getAmount());
        cmiClientFeign.updateCard(card);


        portefeuille.setSolde(portefeuille.getSolde() + request.getAmount());
        portefeuilleClientFeign.updatePortefeuille(portefeuille.getId(), portefeuille);


        Transaction transaction = new Transaction();
        transaction.setAmount(request.getAmount());
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setType(TransactionType.DEPOSIT);
        transactionRepository.save(transaction);


        String message = String.format(
                "{\"clientId\": \"%s\", \"email\": \"%s\", \"message\": \"Votre portefeuille a été crédité de %.2f. Nouveau solde: %.2f\"}",
                request.getClientId(), client.getEmail(), request.getAmount(), portefeuille.getSolde()
        );
        kafkaTemplate.send(transactionTopicDeposit, message);
    }


    public RealCardCMI getCardDetails(String saveToken, String numCard) {
        // Appel du service cmi-service pour récupérer les détails de la carte
        RealCardCMI card = cmiClientFeign.getCardBySaveTokenAndNumber(saveToken, numCard);
        if (card == null) {
            throw new IllegalArgumentException("Carte non trouvée");
        }
        return card;
    }
}

