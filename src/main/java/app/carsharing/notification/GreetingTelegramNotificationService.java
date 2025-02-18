package app.carsharing.notification;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class GreetingTelegramNotificationService {
    private static final String TELEGRAM_API_URL = "https://api.telegram.org/bot";

    @Value("${telegram.bot.token}")
    private String botToken;
    private final RestTemplate restTemplate;
    private int lastUpdateId = 0;

    @Scheduled(fixedRate = 5000)
    public void pollTelegramUpdates() {
        String url = String.format("%s%s/getUpdates?offset=%d",
                TELEGRAM_API_URL, botToken, lastUpdateId + 1);
        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            List<Map<String, Object>> updates = (List<Map<String, Object>>) response.get("result");

            if (updates != null && !updates.isEmpty()) {
                for (Map<String, Object> update : updates) {
                    lastUpdateId = (int) update.get("update_id");
                    Map<String, Object> message = (Map<String, Object>) update.get("message");
                    if (message != null) {
                        String text = (String) message.get("text");
                        String chatId = String.valueOf(((Map<String, Object>) message
                                        .get("from")).get("id"));
                        if ("/start".equalsIgnoreCase(text)) {
                            sendGreetingMessage(chatId);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error polling Telegram updates: " + e.getMessage());
        }
    }

    public void sendGreetingMessage(String chatId) {
        String message = "👋 Welcome to the Car Sharing Notification Bot!\n\n"
                + "This bot is here to send you important updates about your rentals.\n\n"
                + "If you need assistance, contact our support team.\n\n"
                + "Thank you for using our service!";

        String url = String.format("%s%s/sendMessage?chat_id=%s&text=%s",
                TELEGRAM_API_URL, botToken, chatId, message);
        try {
            restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            System.err.println("Failed to send greeting message: " + e.getMessage());
        }
    }
}
