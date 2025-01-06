package ru.practicum.explorewithme.service.dao;

import ru.practicum.explorewithme.dto.comment.CommentDto;
import ru.practicum.explorewithme.dto.comment.NewCommentDto;
import ru.practicum.explorewithme.dto.comment.UpdateCommentDto;

import java.util.List;

public interface CommentService {
    CommentDto createComment(int userId, int eventId, NewCommentDto newComment);

    CommentDto updateComment(int userId, int commentId, UpdateCommentDto updateComment);

    void deleteComment(int userId, int commentId);

    void deleteCommentByAdmin(int commentId);

    List<CommentDto> getUsersEventsComments(int userId, int eventId);

    List<CommentDto> getUsersComments(int userId);
}
