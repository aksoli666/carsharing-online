package app.carsharing.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class CustomPageImpl<T> extends PageImpl<T> {

    @JsonCreator
    public CustomPageImpl(
            @JsonProperty("content") List<T> content,
            @JsonProperty("number") int number,
            @JsonProperty("size") int size,
            @JsonProperty("totalElements") long totalElements
    ) {
        super(content, PageRequest.of(number, size, Sort.unsorted()), totalElements);
    }

    @Override
    @JsonIgnore
    public Pageable getPageable() {
        return super.getPageable();
    }
}
