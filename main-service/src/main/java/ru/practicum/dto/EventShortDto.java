package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.Category;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventShortDto {

    private Long id;

    private String annotation;

    private String title;

    private String description;

    private Category category;

    private Long confirmedRequests;

    private String eventDate;

    private UserShortDto initiator;

    private Boolean paid;

    private Long views;
}
