package ru.practicum.repository.events;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Event;
import ru.practicum.model.enumModel.EventState;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EventsRepository extends JpaRepository<Event, Long> {
    List<Event> findAll(Specification<Event> spec, Pageable pageable);

    List<Event> findAllByInitiatorId(Long userId, Pageable pageable);

    Optional<Event> findByIdAndInitiatorId(Long eventId, Long userId);

    List<Event> findAllByIdIn(Set<Long> events);

    Boolean existsByInitiatorIdAndId(Long userId, Long eventId);

    Optional<Event> findByIdAndState(Long eventId, EventState published);
}
