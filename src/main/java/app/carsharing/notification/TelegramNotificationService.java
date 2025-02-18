package app.carsharing.notification;

import app.carsharing.exception.EntityNotFoundException;
import app.carsharing.exception.NotificationException;
import app.carsharing.model.Payment;
import app.carsharing.model.Rental;
import app.carsharing.model.User;
import app.carsharing.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class TelegramNotificationService implements NotificationService {
    private static final String TELEGRAM_API_URL = "https://api.telegram.org/bot";

    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    @Value("${telegram.bot.token}")
    private String botToken;

    @Override
    public void sendNotification(Long id, String message) throws NotificationException {
        User user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User not found by id: " + id));
        String telegramChatId = user.getTelegramChatId();
        if (telegramChatId == null) {
            throw new NotificationException("Telegram Chat ID not set for user with ID: "
                    + user.getId());
        }
        String url = String.format("%s%s/sendMessage?chat_id=%s&text=%s",
                TELEGRAM_API_URL, botToken, telegramChatId, message);
        try {
            restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            throw new NotificationException("Failed to send notification: " + e.getMessage());
        }
    }

    @Override
    public void sendRentalCreationNotification(Rental rental) throws NotificationException {
        String message = String.format(
                "🚗 New Rental Created!\n\n"
                        + "User: %s\nCar: %s %s\nStart Date: %s\nEnd Date: %s",
                rental.getUser().getFirstName(),
                rental.getCar().getBrand(), rental.getCar().getModel(),
                rental.getRentalDate(),
                rental.getReturnDate()
        );
        sendNotification(rental.getUser().getId(), message);
    }

    @Override
    public void sendRentalReturnNotification(Rental rental) throws NotificationException {
        String message = String.format(
                "🚗 Rental Returned:\n\n"
                        + "User: %s\nCar: %s %s\nStart Date: %s\nEnd Date: %s\nReturn Date: %s",
                rental.getUser().getFirstName(),
                rental.getCar().getBrand(), rental.getCar().getModel(),
                rental.getRentalDate(),
                rental.getReturnDate(),
                rental.getActualReturnDate()
        );
        sendNotification(rental.getUser().getId(), message);
    }

    @Override
    public void sendOverdueNotification(Rental rental) throws NotificationException {
        String message = String.format(
                "🚨 Overdue Rental Alert!\n\n"
                        + "User: %s\nCar: %s %s\nOriginal Return Date: %s\nRental Date: %s\n"
                        + "Please return the car.",
                rental.getUser().getFirstName(),
                rental.getCar().getBrand(), rental.getCar().getModel(),
                rental.getReturnDate(),
                rental.getRentalDate()
        );

        sendNotification(rental.getUser().getId(), message);
    }

    @Override
    public void sendNoOverdueRentalsNotification(Rental rental) throws NotificationException {
        String message = "✅ No rentals overdue today!";
        sendNotification(rental.getUser().getId(), message);
    }

    @Override
    public void sendPaymentSuccessNotification(Payment payment) throws NotificationException {
        String message = "✅ Payment was successful!";
        sendNotification(payment.getRental().getUser().getId(), message);
    }

    @Override
    public void sendPaymentCancelNotification(Payment payment) throws NotificationException {
        String message = "🚨 Payment was cancelled!";
        sendNotification(payment.getRental().getUser().getId(), message);
    }
}
