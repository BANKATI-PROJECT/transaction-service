package ma.ensa.transaction_service.entities;

import jakarta.persistence.*;
import ma.ensa.transaction_service.enums.TransactionStatus;
import ma.ensa.transaction_service.enums.TransactionType;

import java.time.LocalDateTime;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fromPortefeuilleId;
    private String toPortefeuilleId;
    private Double amount;
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status; // PENDING, COMPLETED, FAILED

    @Enumerated(EnumType.STRING)
    private TransactionType type; // TRANSFER, DEPOSIT, WITHDRAW

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFromPortefeuilleId() {
        return fromPortefeuilleId;
    }

    public void setFromPortefeuilleId(String fromPortefeuilleId) {
        this.fromPortefeuilleId = fromPortefeuilleId;
    }

    public String getToPortefeuilleId() {
        return toPortefeuilleId;
    }

    public void setToPortefeuilleId(String toPortefeuilleId) {
        this.toPortefeuilleId = toPortefeuilleId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }
}
