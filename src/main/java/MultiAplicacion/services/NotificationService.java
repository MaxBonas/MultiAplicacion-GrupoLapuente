package MultiAplicacion.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {

    private final RestTemplate restTemplate;
    private final String webhookUrl = "https://grupolapuente0.webhook.office.com/webhookb2/45063d49-89a8-4a93-bff5-26ec635663b1@c4e48120-15ee-4d49-8e89-670e375ebca7/IncomingWebhook/43733b91ab64434caf833292473a4c64/7c46e2e2-2eb6-4645-a222-d38a54aab767";

    public NotificationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendAlertToAdmin(String username) {
        String payload = generatePayload(username);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(payload, headers);
        restTemplate.postForObject(webhookUrl, request, String.class);
    }

    private String generatePayload(String username) {
        return String.format("{ \"text\": \"El usuario %s no ha cumplido con el compromiso de seguridad. Ha quedado notificado con: - El compromiso con la seguridad es obligatorio. Se tomarán medidas si las preocupaciones persisten para garantizar la seguridad de todos en el lugar de trabajo. -\" }", username);
    }
}
