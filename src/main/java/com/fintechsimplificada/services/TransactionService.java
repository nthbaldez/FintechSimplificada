package com.fintechsimplificada.services;

import com.fintechsimplificada.domain.transaction.Transaction;
import com.fintechsimplificada.domain.user.User;
import com.fintechsimplificada.dtos.TransactionDTO;
import com.fintechsimplificada.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class TransactionService {
    @Autowired
    private UserService userService;

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private NotificationService notificationService;

    public Transaction createTransaction(TransactionDTO transaction) throws Exception {
        User sender = this.userService.findUserById(transaction.senderId());
        User receiver = this.userService.findUserById(transaction.receiverId());

        this.userService.validateTransaction(sender, transaction.value());

        boolean isAuthorized = this.handleAuthorizeTransaction(sender, transaction.value());

        if (!isAuthorized) {
            throw new Exception("Transacão não autorizada.");
        }

        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(transaction.value());
        newTransaction.setSender(sender);
        newTransaction.setReceiver(receiver);
        newTransaction.setTimestamp(LocalDateTime.now());

        sender.setBalance(sender.getBalance().subtract(transaction.value()));
        receiver.setBalance(receiver.getBalance().add(transaction.value()));

        repository.save(newTransaction);
        userService.saveUser(sender);
        userService.saveUser(receiver);

//        this.notificationService.sendNotification(sender, "Transação realizada com sucesso");
//        this.notificationService.sendNotification(receiver, "Transferência recebida com sucesso.");
        return newTransaction;
    }

    public boolean handleAuthorizeTransaction(User sender, BigDecimal value) {
        String url = "https://run.mocky.io/v3/8fafdd68-a090-496f-8c9a-3442cf30dae6";

        ResponseEntity<Map> authorizationResponse = restTemplate.getForEntity(url, Map.class);

        if (authorizationResponse.getStatusCode() == HttpStatus.OK) {
            String responseMessage = (String) authorizationResponse.getBody().get("message");
            return "Autorizado".equalsIgnoreCase(responseMessage);
        } else {
            return false;
        }
    }
}
