package guinho.olympus.infrastructure.web.rest.restadvice;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError(
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        Instant timestamp,
        String message,
        Integer status
) {
    public static ApiError of(String message, Integer status) {
        return new ApiError(Instant.now(), message, status);
    }
}
