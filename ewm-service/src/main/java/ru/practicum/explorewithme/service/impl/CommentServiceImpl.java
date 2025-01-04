package ru.practicum.explorewithme.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.comment.CommentDto;
import ru.practicum.explorewithme.dto.comment.NewCommentDto;
import ru.practicum.explorewithme.dto.comment.UpdateCommentDto;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.exception.ValidationException;
import ru.practicum.explorewithme.mapper.CommentMapper;
import ru.practicum.explorewithme.model.*;
import ru.practicum.explorewithme.repository.CommentRepository;
import ru.practicum.explorewithme.repository.EventRepository;
import ru.practicum.explorewithme.repository.RequestRepository;
import ru.practicum.explorewithme.repository.UserRepository;
import ru.practicum.explorewithme.service.dao.CommentService;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final CommentRepository commentRepository;

    private static final String EVENT_NOT_FOUND_MSG = "Event with id=%d was not found";
    private static final String USER_NOT_FOUND_MSG = "User with id=%d was not found";
    private static final String COMMENT_NOT_FOUND_MSG = "Comment with id=%d was not found";
    private static final String ONLY_OWNER_OR_PARTICIPANT_MSG =
            "Only event's owner or participant allowed to leave comment";
    private static final String ONLY_COMMENT_OWNER_MSG =
            "Only comment's owner allowed to delete comment";
    private static final Integer SEC_IN_THREE_HOURS = 10800;

    @Override
    @Transactional
    public CommentDto createComment(int userId, int eventId, NewCommentDto newComment) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format(EVENT_NOT_FOUND_MSG, eventId)));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND_MSG, userId)));

        Optional<Request> confirmedUsersRequestOnEvent =
                requestRepository.getByRequesterIdAndEventIdAndStatusLike(userId, eventId, State.CONFIRMED);

        if (confirmedUsersRequestOnEvent.isEmpty() && !event.getInitiator().getId().equals(userId)) {
            throw new ValidationException(ONLY_OWNER_OR_PARTICIPANT_MSG);
        }

        Comment comment = CommentMapper.modelFromNewCommentDto(newComment);
        comment.setAuthor(user);
        comment.setEventId(eventId);
        comment.setCreated(Instant.now().plusSeconds(SEC_IN_THREE_HOURS));

        return CommentMapper.modelToCommentDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public CommentDto updateComment(int userId, int commentId, UpdateCommentDto updateComment) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(String.format(COMMENT_NOT_FOUND_MSG, commentId)));

        Comment updatedComment = CommentMapper.modelFromUpdateCommentDto(comment, updateComment);

        return CommentMapper.modelToCommentDto(commentRepository.save(updatedComment));
    }

    @Override
    @Transactional
    public void deleteComment(int userId, int commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(String.format(COMMENT_NOT_FOUND_MSG, commentId)));

        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ValidationException(ONLY_COMMENT_OWNER_MSG);
        }

        commentRepository.deleteById(commentId);
    }

    @Override
    @Transactional
    public void deleteCommentByAdmin(int commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(String.format(COMMENT_NOT_FOUND_MSG, commentId)));

        commentRepository.deleteById(commentId);
    }

    @Override
    public List<CommentDto> getUsersEventsComments(int userId, int eventId) {
        return commentRepository.findByEventId(eventId).stream()
                .map(CommentMapper::modelToCommentDto)
                .toList();
    }

    @Override
    public List<CommentDto> getUsersComments(int userId) {
        return commentRepository.findByAuthorId(userId).stream()
                .map(CommentMapper::modelToCommentDto)
                .toList();
    }
}
