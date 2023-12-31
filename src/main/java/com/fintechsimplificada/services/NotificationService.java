package com.fintechsimplificada.services;

import com.fintechsimplificada.domain.user.User;
import com.fintechsimplificada.dtos.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {
    @Autowired
    private RestTemplate restTemplate;

    public void sendNotification(User user, String message) throws Exception {
        String email = user.getEmail();
        NotificationDTO notificationRequest = new NotificationDTO(email, message);

        String url = "http://o4d9z.mocklab.io/notify";
        ResponseEntity<String> notificationResponse = restTemplate.postForEntity(url, notificationRequest, String.class);
        System.out.println(notificationResponse.getStatusCode());
        if (!(notificationResponse.getStatusCode() == HttpStatus.OK)) {
            System.out.println("Erro ao enviar notificação");
            throw new Exception("Serviço de notificação está fora do ar!");
        }
    }
}
