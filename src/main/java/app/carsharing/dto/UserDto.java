package app.carsharing.dto;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Getter
@Setter
public class UserDto {
    private String email;
    private String firstName;
    private String lastName;

    @Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder()
                .append(email)
                .append(firstName)
                .append(lastName);
        return hcb.toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserDto)) {
            return false;
        }
        UserDto that = (UserDto) o;
        EqualsBuilder eb = new EqualsBuilder()
                .append(email, that.email)
                .append(firstName, that.firstName)
                .append(lastName, that.lastName);
        return eb.isEquals();
    }
}
