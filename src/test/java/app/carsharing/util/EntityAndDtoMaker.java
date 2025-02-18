package app.carsharing.util;

import static app.carsharing.util.ConstantUtil.ID_1L_CORRECT;
import static app.carsharing.util.ConstantUtil.PRICE_10;
import static app.carsharing.util.ConstantUtil.STATUS_PENDING;
import static app.carsharing.util.ConstantUtil.TYPE_PAYMENT;

import app.carsharing.dto.CarDto;
import app.carsharing.dto.PaymentDto;
import app.carsharing.dto.RentalDto;
import app.carsharing.dto.UserDto;
import app.carsharing.dto.request.PaymentRequestDto;
import app.carsharing.dto.request.RentalAddRequestDto;
import app.carsharing.dto.request.UserUpdateProfileRequestDto;
import app.carsharing.dto.responce.PaymentResponseDto;
import app.carsharing.dto.responce.RentalAddResponseDto;
import app.carsharing.model.Car;
import app.carsharing.model.Payment;
import app.carsharing.model.Rental;
import app.carsharing.model.Role;
import app.carsharing.model.User;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public class EntityAndDtoMaker {
    public static Car createCar1L() {
        Car car = new Car();
        car.setId(ID_1L_CORRECT);
        car.setModel("Corolla");
        car.setBrand("Toyota");
        car.setType(Car.Type.UNIVERSAL);
        car.setInventory(5);
        car.setDaileFee(PRICE_10);

        return car;
    }

    public static CarDto createCarDto() {
        CarDto dto = new CarDto();
        dto.setModel("Corolla");
        dto.setBrand("Toyota");
        dto.setType("Universal");
        dto.setInventory(5);
        dto.setDaileFee(PRICE_10);

        return dto;
    }

    public static CarDto createCarDto10L() {
        CarDto dto = new CarDto();
        dto.setModel("model10");
        dto.setBrand("brand10");
        dto.setType("UNIVERSAL");
        dto.setInventory(5);
        dto.setDaileFee(BigDecimal.valueOf(50.99));

        return dto;
    }

    public static CarDto createCarDto16L() {
        CarDto dto = new CarDto();
        dto.setModel("model11");
        dto.setBrand("brand11");
        dto.setType("UNIVERSAL");
        dto.setInventory(5);
        dto.setDaileFee(BigDecimal.valueOf(50.99));

        return dto;
    }

    public static CarDto createCarDto17L() {
        CarDto dto = new CarDto();
        dto.setModel("model12");
        dto.setBrand("brand12");
        dto.setType("UNIVERSAL");
        dto.setInventory(5);
        dto.setDaileFee(BigDecimal.valueOf(50.99));

        return dto;
    }

    public static User createUser1L() {
        Role role = new Role();
        role.setId(ID_1L_CORRECT);
        role.setRole(Role.RoleName.ROLE_CUSTOMER);

        User user = new User();
        user.setId(ID_1L_CORRECT);
        user.setEmail("john@doe.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("password");
        user.setRoles(Set.of(role));
        user.setTelegramChatId("45789653215");

        return user;
    }

    public static UserDto createUserDto() {
        UserDto dto = new UserDto();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("john@doe.com");

        return dto;
    }

    public static UserUpdateProfileRequestDto createUpdateUserDto() {
        UserUpdateProfileRequestDto dto = new UserUpdateProfileRequestDto();
        dto.setFirstName("John");
        dto.setLastName("Doe");

        return dto;
    }

    public static Rental createRental() {
        Rental rental = new Rental();
        rental.setRentalDate(LocalDate.now());
        rental.setReturnDate(LocalDate.now().plusDays(1));
        rental.setActualReturnDate(LocalDate.now().plusDays(1));

        return rental;
    }

    public static RentalAddRequestDto createRentalAddRequestDto() {
        RentalAddRequestDto dto = new RentalAddRequestDto();
        dto.setRentalDate(LocalDate.now());
        dto.setReturnDate(LocalDate.now().plusDays(1));
        dto.setCarId(ID_1L_CORRECT);

        return dto;
    }

    public static RentalAddResponseDto createRentalAddResponseDto() {
        RentalAddResponseDto dto = new RentalAddResponseDto();
        dto.setRentalDate(LocalDate.now());
        dto.setReturnDate(LocalDate.now().plusDays(1));
        dto.setCarId(ID_1L_CORRECT);

        return dto;
    }

    public static RentalDto createRentalDtoIsActive() {
        RentalDto dto = new RentalDto();
        dto.setRentalDate(LocalDate.now());
        dto.setReturnDate(LocalDate.now().plusDays(1));
        dto.setActualReturnDate(null);
        dto.setCarId(ID_1L_CORRECT);
        dto.setUserId(ID_1L_CORRECT);

        return dto;
    }

    public static RentalDto createRentalDtoNotActive() {
        RentalDto dto = new RentalDto();
        dto.setRentalDate(LocalDate.now());
        dto.setReturnDate(LocalDate.now().plusDays(1));
        dto.setActualReturnDate(LocalDate.now().plusDays(1));
        dto.setCarId(ID_1L_CORRECT);
        dto.setUserId(ID_1L_CORRECT);

        return dto;
    }

    public static RentalDto createRentalDto30L() {
        RentalDto dto = new RentalDto();
        dto.setCarId(26L);
        dto.setCarId(26L);
        dto.setRentalDate(LocalDate.of(2024, 11, 25));
        dto.setReturnDate(LocalDate.of(2024, 11, 30));
        dto.setActualReturnDate(LocalDate.of(2024, 11, 30));

        return dto;
    }

    public static Payment createPayment() {
        Payment payment = new Payment();
        payment.setStatus(Payment.Status.PENDING);
        payment.setType(Payment.Type.PAYMENT);
        payment.setSessionUrl("https://sessionurl.com");
        payment.setSessionId("sessionid");
        payment.setAmount(PRICE_10);

        return payment;
    }

    public static PaymentDto createPaymentDto() {
        PaymentDto dto = new PaymentDto();
        dto.setStatus(STATUS_PENDING);
        dto.setType(TYPE_PAYMENT);
        dto.setRentalId(ID_1L_CORRECT);
        dto.setAmount(PRICE_10);

        return dto;
    }

    public static PaymentDto createPaymentDto40L() {
        PaymentDto dto = new PaymentDto();
        dto.setAmount(BigDecimal.valueOf(50.99));
        dto.setRentalId(40L);
        dto.setStatus("PAID");
        dto.setType("PAYMENT");

        return dto;
    }

    public static PaymentRequestDto createPaymentRequestDto() {
        return new PaymentRequestDto(ID_1L_CORRECT, Payment.Type.PAYMENT);
    }

    public static PaymentResponseDto createPaymentResponseDto() {
        return new PaymentResponseDto("sessionid", "https://sessionurl.com");
    }
}
