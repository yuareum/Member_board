package com.its.board.service;

import com.its.board.dto.CommentDTO;
import com.its.board.entity.BoardEntity;
import com.its.board.entity.CommentEntity;
import com.its.board.entity.MemberEntity;
import com.its.board.repository.BoardRepository;
import com.its.board.repository.CommentRepository;
import com.its.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    public List<CommentDTO> findAll(Long boardId) {
        List<CommentEntity> commentEntityList = commentRepository.findByBoardId(boardId);
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for(CommentEntity comment: commentEntityList){
            commentDTOList.add(CommentDTO.toCommentDTO(comment));
        }
        return commentDTOList;
    }

    public Long save(CommentDTO commentDTO) {
        MemberEntity memberEntity = memberRepository.findByMemberEmail(commentDTO.getCommentWriter()).get();
        BoardEntity boardEntity = boardRepository.findById(commentDTO.getBoardId()).get();
        Long saveId = commentRepository.save(CommentEntity.toSaveEntity(commentDTO,memberEntity, boardEntity)).getId();
        return saveId;
    }
}
