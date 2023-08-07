package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.model.enumModel.RequestStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestDto {
    private final String date = "yyyy-MM-dd HH:mm:ss";

    Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = date)
    LocalDateTime created;

    Long event;

    Long requester;

    RequestStatus status;
}
