package com.its.board.dto;

import com.its.board.entity.CommentEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    private Long boardId;
    private Long b_id;
    private String commentWriter;
    private String commentContents;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    public CommentDTO(String commentWriter, String commentContents, Long boardId) {
        this.commentWriter = commentWriter;
        this.commentContents = commentContents;
        this.boardId = boardId;
    }

    public static CommentDTO toCommentDTO(CommentEntity commentEntity) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(commentEntity.getId());
        commentDTO.setBoardId(commentEntity.getBoardId());
        commentDTO.setCommentWriter(commentEntity.getCommentWriter());
        commentDTO.setCommentContents(commentEntity.getCommentContents());
        commentDTO.setCreatedTime(commentEntity.getCreatedTime());
        commentDTO.setUpdatedTime(commentEntity.getUpdatedTime());
        return commentDTO;
    }
}
