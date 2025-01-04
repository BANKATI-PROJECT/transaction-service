package ma.ensa.transaction_service.enums;

public enum TransactionStatus {
    PENDING,   // La transaction est en attente de traitement
    COMPLETED, // La transaction a été réalisée avec succès
    FAILED     // La transaction a échoué
}
