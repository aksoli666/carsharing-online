package app.carsharing.util;

import java.math.BigDecimal;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class ConstantUtil {
    public static final Long ID_1L_CORRECT = 1L;
    public static final Long ID_10L_CORRECT = 10L;
    public static final Long ID_15L_CORRECT = 15L;
    public static final Long ID_16L_CORRECT = 16L;
    public static final Long ID_30L_CORRECT = 30L;
    public static final Long ID_40L_CORRECT = 40L;
    public static final Long ID_41L_CORRECT = 41L;
    public static final Long ID_42L_CORRECT = 42L;
    public static final Long ID_43L_CORRECT = 43L;
    public static final Long ID_44L_CORRECT = 44L;
    public static final Long INCORRECT_ID = -100L;

    public static final BigDecimal PRICE_10 = BigDecimal.TEN;

    public static final int COUNT_CONTENT_1 = 1;
    public static final int COUNT_CONTENT_2 = 2;

    public static final String ROLE_CUSTOMER = "ROLE_CUSTOMER";
    public static final String STATUS_PENDING = "PENDING";

    public static final String TYPE_PAYMENT = "PAYMENT";

    public static final boolean IS_ACTIVE = true;
    public static final boolean NOT_ACTIVE = false;

    public static final Pageable pageable = PageRequest.of(0, 20);

    public static final String ADD_RENTAL_ID42_REPO =
            "INSERT INTO rentals (id, rental_date, return_date, actual_return_date, car_id, user_id) " +
                    "VALUES (42, '2024-11-25', '2024-11-30', null, 1, 1);";
    public static final String ADD_RENTAL_ID43_REPO =
            "INSERT INTO rentals (id, rental_date, return_date, actual_return_date, car_id, user_id) " +
                    "VALUES (43, '2024-11-25', '2024-11-30', '2024-11-30', 1, 1);";
    public static final String ADD_RENTAL_ID44_REPO =
            "INSERT INTO rentals (id, rental_date, return_date, actual_return_date, car_id, user_id) " +
                    "VALUES (44, '2024-11-25', '2024-11-30', '2024-11-30', 1, 1);";

    public static final String ADD_USER_REPO =
            "INSERT INTO users (id, email, password, first_name, last_name, telegram_chat_id, is_deleted) " +
                    "VALUES (1, 'email1@gmail.com', '1', 'first', 'last', '7355180204', 0);";
    public static final String ADD_CAR_REPO =
            "INSERT INTO cars (id, model, brand, type, inventory, daile_fee, is_deleted) " +
                    "VALUES (1, 'model1', 'brand11', 'UNIVERSAL', 5, 50.99, 0)";
    public static final String ADD_RENTAL_PAYMENT_REPO =
            "INSERT INTO rentals (id, rental_date, return_date, actual_return_date, car_id, user_id) " +
                    "VALUES (41, '2024-11-25', '2024-11-30', '2024-11-30', 1, 1);";
    public static final String ADD_PAYMENT_REPO =
            "INSERT INTO payments (id, status, type, session_url, session_id, amount) " +
                    "VALUES (41, 'PAID', 'PAYMENT', 'https://sessionurlnew.com', 'sessionidnew', 50.99);";

    public static final String ADD_CARS_SQL =
            "classpath:database/carsharing/car/add-cars.sql";
    public static final String ADD_CAR_SQL =
            "classpath:database/carsharing/car/add-car.sql";
    public static final String ADD_CAR_FOR_UPDATE_SQL =
            "classpath:database/carsharing/car/add-car-for-update.sql";
    public static final String UPDATE_CAR_SQL =
            "classpath:database/carsharing/car/update-car.sql";
    public static final String DELETE_CARS_SQL =
            "classpath:database/carsharing/car/delete-cars.sql";
    public static final String DELETE_CAR_SQL =
            "classpath:database/carsharing/car/delete-car.sql";
    public static final String DELETE_UPD_CAR_SQL =
            "classpath:database/carsharing/car/delete-upd-car.sql";

    public static final String ADD_CAR_FOR_RENTAL_SQL =
            "classpath:database/carsharing/car/add-car-for-rental.sql";
    public static final String ADD_USER_FOR_RENTAL_SQL =
            "classpath:database/carsharing/user/add-user-for-rental.sql";
    public static final String ADD_RENTAL_FOR_SET_RETURN_SQL =
            "classpath:database/carsharing/rental/add-rental-for-set-return-date.sql";
    public static final String DELETE_RENTAL_CAR_SQL =
            "classpath:database/carsharing/car/delete-rental-car.sql";
    public static final String DELETE_USER_FOR_RENTAL_SQL =
            "classpath:database/carsharing/user/delete-user-for-rental.sql";
    public static final String DELETE_UPD_RENTAL_SQL =
            "classpath:database/carsharing/rental/delete-upd-rental.sql";

    public static final String ADD_CAR_FOR_PAYMENT_SQL =
            "classpath:database/carsharing/car/add-car-for-payment.sql";
    public static final String ADD_USER_FOR_PAYMENT_SQL =
            "classpath:database/carsharing/user/add-user-for-payment.sql";
    public static final String ADD_RENTAL_FOR_PAYMENT_SQL =
            "classpath:database/carsharing/rental/add-rental-for-payment.sql";
    public static final String ADD_PAYMENT_SQL =
            "classpath:database/carsharing/payment/add-payment.sql";
    public static final String DELETE_CAR_FOR_PAYMENT_SQL =
            "classpath:database/carsharing/car/delete-car-for-payment.sql";
    public static final String DELETE_USER_FOR_PAYMENT_SQL =
            "classpath:database/carsharing/user/delete-user-for-paymen.sql";
    public static final String DELETE_RENTAL_FOR_PAYMENT_SQL =
            "classpath:database/carsharing/rental/delete-rental-for-payment.sql";
    public static final String DELETE_PAYMENT_SQL =
            "classpath:database/carsharing/payment/delete-payment.sql";

    public static final String URL_CARS_WITHOUT_ID = "/cars";
    public static final String URL_CARS_WITH_ID = "/cars/{id}";

    public static final String URL_RENTALS_WITH_ID = "/rentals/{id}";

    public static final String URL_PAYMENTS_WITHOUT_ID = "/payments";
    public static final String URL_PAYMENTS_WITH_ID = "/payments/{id}";
}
