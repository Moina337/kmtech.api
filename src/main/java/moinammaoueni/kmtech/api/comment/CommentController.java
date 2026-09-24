package moinammaoueni.kmtech.api.comment;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import moinammaoueni.kmtech.api.comment.dto.CommentRequestDTO;
import moinammaoueni.kmtech.api.comment.dto.CommentResponseDTO;
import moinammaoueni.kmtech.api.project.ProjectService;


@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

  
    @GetMapping
    public ResponseEntity<List<CommentResponseDTO>> findAll() {

        return ResponseEntity.ok(
                commentService.findAll()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponseDTO> findById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                commentService.findById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody CommentRequestDTO request) {

        return ResponseEntity.ok(
                commentService.update(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        commentService.delete(id);

        return ResponseEntity.noContent().build();
    }
    
   
}