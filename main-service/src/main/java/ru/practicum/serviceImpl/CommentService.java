package ru.practicum.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.practicum.dto.CommentDto;
import ru.practicum.exception.EventConflictException;
import ru.practicum.exception.NoFoundObjectException;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;
import ru.practicum.model.Request;
import ru.practicum.model.User;
import ru.practicum.model.enumModel.EventState;
import ru.practicum.model.enumModel.RequestStatus;
import ru.practicum.repository.comment.CommentRepository;
import ru.practicum.repository.events.EventsRepository;
import ru.practicum.repository.request.RequestRepository;

import javax.transaction.Transactional;
import java.rmi.AccessException;
import java.util.Objects;
import java.util.Optional;

import static ru.practicum.model.mapper.CommentMapper.makeComment;
import static ru.practicum.model.mapper.CommentMapper.makeCommentDto;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final EventsRepository eventsRepository;
    private final RequestRepository requestRepository;

    @Transactional
    public CommentDto createComment(Long userId, Long eventId, CommentDto commentDto) throws AccessException {
        User user = userService.getUserByIdIfExist(userId);

        Event event = getEventByIdIfExist(eventId);

        if (!Objects.equals(event.getState(), EventState.PUBLISHED)) {
            throw new EventConflictException("Статус события должен быть 'PUBLISHED'");
        }

        Optional<Request> optionalRequest = getRequestByUserIdAndEventId(userId, eventId);

        if (!Objects.equals(user.getId(), event.getInitiator().getId()) && (optionalRequest.isEmpty()
                || (!Objects.equals(optionalRequest.get().getStatus(), RequestStatus.CONFIRMED)))) {
            throw new AccessException(String.format("Пользователь с id='%s' не участвовал в событии с id='%s' " +
                    "и не может оставить комментарий", userId, eventId));
        }


        Optional<Comment> foundComment = commentRepository.findByEventIdAndAuthor(eventId, userId);
        if (foundComment.isPresent()) {
            throw new AccessException(String.format("Пользователь с id='%s' уже оставлял комментарий к событию " +
                    "с id='%s'", userId, eventId));
        }

        Comment comment = makeComment(commentDto);

        Comment savedComment = commentRepository.save(comment);
        return makeCommentDto(savedComment);
    }

    @Transactional
    public void deleteCommentById(Long commentId, Long userId) throws AccessException {
        Comment comment = getCommentByIdIfExist(commentId);

        checkUserIsAuthorComment(comment.getAuthor().getId(), userId, commentId);

        commentRepository.deleteById(commentId);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        checkExistCommentById(commentId);
        commentRepository.deleteById(commentId);
    }

    @Transactional
    public CommentDto updateCommentById(Long commentId, Long userId, CommentDto commentDto) throws AccessException {
        Comment foundComment = getCommentByIdIfExist(commentId);

        checkUserIsAuthorComment(foundComment.getAuthor().getId(), userId, commentId);

        String newText = commentDto.getText();
        if (StringUtils.hasLength(newText)) {
            foundComment.setText(newText);
        }

        Comment savedComment = commentRepository.save(foundComment);
        return makeCommentDto(savedComment);
    }

    private void checkUserIsAuthorComment(Long authorId, Long userId, Long commentId) throws AccessException {
        if (!Objects.equals(authorId, userId)) {
            throw new AccessException(String.format(
                    "Пользователь с id='%s' не является автором комментария с id='%s' и не может его удалить / изменить",
                    userId, commentId));
        }
    }

    private Comment getCommentByIdIfExist(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() ->
                new NoFoundObjectException(String.format("Комментарий с id='%s' не найден", commentId)));
    }

    private void checkExistCommentById(Long commentId) {
        if (commentRepository.countById(commentId) <= 0) {
            throw new NoFoundObjectException(String.format("Комментарий с id='%s' не найден", commentId));
        }
    }

    public void checkExistEventById(Long eventId) {
        eventsRepository.findById(eventId).orElseThrow(() ->
                new NoFoundObjectException(String.format("Событие с id='%s' не найдено", eventId)));
    }

    public Event getEventByIdIfExist(Long eventId) {
        return eventsRepository.findById(eventId).orElseThrow(() ->
                new NoFoundObjectException(String.format("Событие с id='%s' не найдено", eventId)));
    }

    public Optional<Request> getRequestByUserIdAndEventId(Long userId, Long eventId) {
        return requestRepository.findByEventIdAndRequesterId(eventId, userId);
    }
}
