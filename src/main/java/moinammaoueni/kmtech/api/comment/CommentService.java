package moinammaoueni.kmtech.api.comment;


import java.util.List;

import moinammaoueni.kmtech.api.comment.dto.CommentRequestDTO;
import moinammaoueni.kmtech.api.comment.dto.CommentResponseDTO;

public interface CommentService {

	Comment prepare(CommentRequestDTO request);

    List<CommentResponseDTO> findAll();

    CommentResponseDTO findById(Long id);

    CommentResponseDTO update(Long id, CommentRequestDTO request);

    void delete(Long id);
}