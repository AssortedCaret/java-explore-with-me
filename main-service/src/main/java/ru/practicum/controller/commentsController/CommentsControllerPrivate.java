package ru.practicum.controller.commentsController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CommentDto;
import ru.practicum.serviceImpl.CommentService;

import javax.validation.Valid;
import java.rmi.AccessException;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/comments")
@RequiredArgsConstructor
public class CommentsControllerPrivate {
    private final CommentService commentService;
    private final String way = "/{commentId}";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@PathVariable Long userId,
                                    @RequestParam Long eventId,
                                    @Valid @RequestBody CommentDto commentDto) throws AccessException {
        log.info("CommentUserController: запрос пользователя с id='{}' на создание комментария {} к событию с id='{}'",
                userId, commentDto, eventId);
        return commentService.createComment(userId, eventId, commentDto);
    }

    @DeleteMapping(way)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long userId,
                              @PathVariable Long commentId) throws AccessException {
        log.info("CommentUserController: запрос пользователя с id='{}' на удаление комментария с id='{}'",
                userId, commentId);
        commentService.deleteCommentById(commentId, userId);
    }

    @PatchMapping(way)
    public CommentDto updateComment(@PathVariable Long userId,
                                    @PathVariable Long commentId,
                                    @Valid @RequestBody CommentDto commentDto) throws AccessException {
        log.info("CommentUserController: запрос пользователя с id='{}' на изменение комментария с id='{}', " +
                "новый комментарий: {}", userId, commentId, commentDto);
        return commentService.updateCommentById(commentId, userId, commentDto);
    }
}
