package app.carsharing.mapper;

import app.carsharing.config.MapperConfig;
import app.carsharing.dto.PaymentDto;
import app.carsharing.dto.responce.PaymentResponseDto;
import app.carsharing.model.Payment;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@Mapper(config = MapperConfig.class)
public interface PaymentMapper {
    @Mapping(source = "rental.id", target = "rentalId")
    PaymentDto toPaymentDto(Payment payment);

    PaymentResponseDto toPaymentResponseDto(Payment payment);

    List<PaymentDto> toPaymentDtoList(List<Payment> payments);

    default Page<PaymentDto> toPaymentDtoPage(Page<Payment> payments) {
        List<PaymentDto> paymentDtoList = toPaymentDtoList(payments.getContent());
        return new PageImpl<>(paymentDtoList, payments.getPageable(), payments.getTotalElements());
    }
}
