package ru.practicum.explorewithme.controller.Admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.service.dao.CommentService;

@RestController
@RequestMapping(path = "/admin")
@RequiredArgsConstructor
public class CommentControllerAdmin {
    private final CommentService commentService;

    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable int commentId) {
        commentService.deleteCommentByAdmin(commentId);
    }
}
