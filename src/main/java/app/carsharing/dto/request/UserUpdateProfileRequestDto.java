package app.carsharing.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Getter
@Setter
public class UserUpdateProfileRequestDto {
    private String firstName;
    private String lastName;

    @Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder()
                .append(firstName)
                .append(lastName);
        return hcb.toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserUpdateProfileRequestDto)) {
            return false;
        }
        UserUpdateProfileRequestDto that = (UserUpdateProfileRequestDto) o;
        EqualsBuilder eb = new EqualsBuilder()
                .append(firstName, that.firstName)
                .append(lastName, that.lastName);
        return eb.isEquals();
    }
}
