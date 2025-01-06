package ru.practicum.explorewithme.dto.comment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentDto {
    private Integer id;
    private String text;
    private Integer eventId;
    private Integer authorId;
    private String created;
}
