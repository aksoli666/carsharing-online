package app.carsharing.dto.request;

import app.carsharing.validation.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@FieldMatch(
        firstFieldName = "password",
        secondFieldName = "repeatPassword",
        message = "Password must match!"
)
@Getter
@Setter
public class UserRegisterRequestDto {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String password;
    @NotBlank
    private String repeatPassword;
    @NotBlank
    private String telegramChatId;

    @Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder()
                .append(email)
                .append(firstName)
                .append(lastName)
                .append(password)
                .append(repeatPassword)
                .append(telegramChatId);
        return hcb.toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserRegisterRequestDto)) {
            return false;
        }
        UserRegisterRequestDto that = (UserRegisterRequestDto) o;
        EqualsBuilder eb = new EqualsBuilder()
                .append(email, that.email)
                .append(firstName, that.firstName)
                .append(lastName, that.lastName)
                .append(password, that.password)
                .append(repeatPassword, that.repeatPassword)
                .append(telegramChatId, that.telegramChatId);
        return eb.isEquals();
    }
}
