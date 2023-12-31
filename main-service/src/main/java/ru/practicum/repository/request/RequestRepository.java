package ru.practicum.repository.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Request;
import ru.practicum.model.enumModel.RequestStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByRequesterId(Long userId);

    Optional<Request> findByIdAndRequesterId(Long requestId, Long userId);

    List<Request> findAllByEventId(Long eventId);

    List<Request> findAllByEventIdAndStatus(Long id, RequestStatus pending);

    List<Request> findByRequesterIdAndEventId(Long userId, Long eventId);
}
