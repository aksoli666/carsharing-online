package app.carsharing.notification;

import app.carsharing.exception.NotificationException;
import app.carsharing.model.Rental;
import app.carsharing.repository.RentalRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NonAndOverdueRentalTelegramNotificationService {
    private final RentalRepository rentalRepository;
    private final NotificationService notificationService;

    @Scheduled(cron = "0 0 9 * * *")
    public void notifyNonOverdueRentals() throws NotificationException {
        List<Rental> nonOverdueRentals = rentalRepository
                .findByReturnDateAfterOrActualReturnDateIsNotNull(LocalDate.now());
        if (!nonOverdueRentals.isEmpty()) {
            for (Rental rental : nonOverdueRentals) {
                notificationService.sendNoOverdueRentalsNotification(rental);
            }
        }
    }

    @Scheduled(cron = "0 30 12 * * *")
    public void notifyOverdueRentals() throws NotificationException {
        List<Rental> overdueRentals = rentalRepository
                .findByReturnDateBeforeAndActualReturnDateIsNull(LocalDate.now());
        if (!overdueRentals.isEmpty()) {
            for (Rental rental : overdueRentals) {
                notificationService.sendOverdueNotification(rental);
            }
        }
    }
}
