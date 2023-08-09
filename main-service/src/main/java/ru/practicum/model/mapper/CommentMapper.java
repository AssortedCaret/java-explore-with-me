package ru.practicum.model.mapper;

import ru.practicum.dto.CommentDto;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;
import ru.practicum.model.User;

import java.util.ArrayList;
import java.util.List;

public class CommentMapper {

    public static Comment makeComment(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setAuthor(User.builder().id(commentDto.getAuthor()).build());
        comment.setEvent(Event.builder().id(commentDto.getEvent()).build());
        comment.setText(commentDto.getText());
        comment.setCreatedOn(commentDto.getCreatedOn());
        return comment;
    }

    public static CommentDto makeCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setAuthor(comment.getAuthor().getId());
        commentDto.setEvent(comment.getEvent().getId());
        commentDto.setText(comment.getText());
        commentDto.setCreatedOn(comment.getCreatedOn());
        return commentDto;
    }

    public static List<CommentDto> makeCommentDtoList(List<Comment> comments) {
        List<CommentDto> commentDtoList = new ArrayList<>();
        for (Comment comment : comments)
            commentDtoList.add(makeCommentDto(comment));
        return commentDtoList;
    }

    public static List<Comment> makeCommentList(List<CommentDto> commentsDto) {
        List<Comment> commentList = new ArrayList<>();
        for (CommentDto commentDto : commentsDto)
            commentList.add(makeComment(commentDto));
        return commentList;
    }
}
