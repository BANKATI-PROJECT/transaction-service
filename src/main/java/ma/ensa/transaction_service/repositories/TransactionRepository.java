package ma.ensa.transaction_service.repositories;

import ma.ensa.transaction_service.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
}
