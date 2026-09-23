package moinammaoueni.kmtech.api.comment;


import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import moinammaoueni.kmtech.api.auth.CurrentUser;

import moinammaoueni.kmtech.api.comment.dto.CommentRequestDTO;
import moinammaoueni.kmtech.api.comment.dto.CommentResponseDTO;
import moinammaoueni.kmtech.api.common.exception.ResourceNotFoundException;
import moinammaoueni.kmtech.api.user.User;


@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final CurrentUser currentUser;

    @Override
    public Comment prepare(CommentRequestDTO request) {

        User user = currentUser.get();

        Comment comment = commentMapper.toEntity(request);
        comment.setAuthor(user);

        return comment;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponseDTO> findAll() {

        return commentRepository.findAll()
                .stream()
                .map(commentMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CommentResponseDTO findById(Long id) {

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Commentaire introuvable"));

        return commentMapper.toResponse(comment);
    }

    @Override
    public CommentResponseDTO update(Long id, CommentRequestDTO request) {

        User user = currentUser.get();

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Commentaire introuvable"));

        if (!comment.getAuthor().getId().equals(user.getId())) {
            throw new IllegalArgumentException(
                    "Vous ne pouvez modifier que vos propres commentaires");
        }

        comment.setContent(request.getContent());

        return commentMapper.toResponse(comment);
    }

    @Override
    public void delete(Long id) {

        User user = currentUser.get();

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Commentaire introuvable"));

        if (!comment.getAuthor().getId().equals(user.getId())) {
            throw new IllegalArgumentException(
                    "Vous ne pouvez supprimer que vos propres commentaires");
        }

        commentRepository.delete(comment);
    }
}