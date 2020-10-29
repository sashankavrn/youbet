package com.youbetcha.repository;

import com.youbetcha.model.payments.Transaction;
import com.youbetcha.model.payments.TransactionStage;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    Optional<Transaction> findByMerchantReference(String merchantReference);

    Optional<Transaction> findByMerchantReferenceAndTransactionStage(String merchantReference, TransactionStage ts);

    boolean existsByMerchantReferenceAndTransactionType(String merchantReference, TransactionStage initDeposit);

    @Query(nativeQuery = true, value = "SELECT * FROM transaction WHERE merchant_reference = :merchant_reference AND " +
            "created_date = (SELECT MAX(created_date) FROM transaction WHERE merchant_reference = :merchant_reference)")
    Optional<Transaction> findByMerchantReferenceAndLatestDate(@Param("merchant_reference") String merchantReference);

    @Query(nativeQuery = true, value = "SELECT * FROM transaction t1 WHERE t1.merchant_reference = :merchant_reference AND " +
    		"t1.transaction_stage = :transaction_stage AND " + 
            "t1.created_date = (SELECT MAX(t2.created_date) FROM transaction t2 WHERE t2.merchant_reference = :merchant_reference AND t2.transaction_stage = :transaction_stage)")
    Optional<Transaction> findByMerchantReferenceTransactionStageAndLatestDate(@Param("merchant_reference") String merchantReference, @Param("transaction_stage") String ts);

    List<Transaction> findAllByMerchantReference(String merchantReference);

    List<Transaction> findAllByTransactionStage(TransactionStage ts);
}
