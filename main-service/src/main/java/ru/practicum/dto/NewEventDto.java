package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewEventDto {
    private final String date = "yyyy-MM-dd HH:mm:ss";

    @Size(min = 20, max = 2000)
    @NotBlank
    String annotation;

    @NotNull
    @Positive
    Long category;

    @Size(min = 20, max = 7000)
    @NotBlank
    String description;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = date)
    LocalDateTime eventDate;

    @NotNull
    LocationDto location;

    Boolean paid = false;

    @PositiveOrZero
    Long participantLimit = 0L;

    Boolean requestModeration = true;

    @Size(min = 3, max = 120)
    @NotBlank
    String title;
}
