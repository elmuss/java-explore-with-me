package ru.practicum.explorewithme.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.dto.comment.CommentDto;
import ru.practicum.explorewithme.dto.comment.NewCommentDto;
import ru.practicum.explorewithme.dto.comment.UpdateCommentDto;
import ru.practicum.explorewithme.model.Comment;

import java.util.Optional;

@UtilityClass
public class CommentMapper {
    public static Comment modelFromNewCommentDto(NewCommentDto newComment) {
        return Comment.builder()
                .text(newComment.getText())
                .build();
    }

    public static CommentDto modelToCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .eventId(comment.getEventId())
                .authorId(comment.getAuthor().getId())
                .created(DateMapper.stringFromInstant(comment.getCreated()))
                .build();
    }

    public static Comment modelFromUpdateCommentDto(Comment comment, UpdateCommentDto updateCommentDto) {
        comment.setText(Optional.ofNullable(updateCommentDto.getText())
                .filter(name -> !name.isBlank()).orElse(comment.getText()));

        return comment;
    }
}
