package ru.practicum.explorewithme.controller.Private;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.comment.CommentDto;
import ru.practicum.explorewithme.dto.comment.NewCommentDto;
import ru.practicum.explorewithme.dto.comment.UpdateCommentDto;
import ru.practicum.explorewithme.service.dao.CommentService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class CommentControllerPrivate {
    private final CommentService commentService;

    @PostMapping("/{userId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@PathVariable int userId,
                                    @RequestParam int eventId,
                                    @RequestBody NewCommentDto newComment) {
        return commentService.createComment(userId, eventId, newComment);
    }

    @PatchMapping("/{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto updateComment(@PathVariable int userId,
                                    @PathVariable int commentId,
                                    @RequestBody UpdateCommentDto updateComment) {
        return commentService.updateComment(userId, commentId, updateComment);
    }

    @GetMapping("/{userId}/events/{eventId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getUsersEventsComments(@PathVariable int userId,
                                                   @PathVariable int eventId) {
        return commentService.getUsersEventsComments(userId, eventId);
    }

    @GetMapping("/{userId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getUsersComments(@PathVariable int userId) {
        return commentService.getUsersComments(userId);
    }

    @DeleteMapping("/{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable int userId,
                              @PathVariable int commentId) {
        commentService.deleteComment(userId, commentId);
    }
}
