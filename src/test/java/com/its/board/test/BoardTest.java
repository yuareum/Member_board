package com.its.board.test;

import com.its.board.common.PagingConst;
import com.its.board.dto.BoardDTO;
import com.its.board.dto.CommentDTO;
import com.its.board.dto.MemberDTO;
import com.its.board.entity.BoardEntity;
import com.its.board.entity.CommentEntity;
import com.its.board.entity.MemberEntity;
import com.its.board.repository.BoardRepository;
import com.its.board.repository.CommentRepository;
import com.its.board.repository.MemberRepository;
import com.its.board.service.BoardService;
import com.its.board.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class BoardTest {
    @Autowired
    private BoardService boardService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CommentRepository commentRepository;
    public BoardDTO newBoard(int i){
        BoardDTO board = new BoardDTO("테스트제목"+i,"테스트작성자"+i,"테스트내용"+i);
        return board;
    }

    @Test
    @Transactional
    @Rollback(value = false)
    public void newMember(){
        MemberDTO memberDTO = new MemberDTO("email1", "pw1", "name1" ,"010-0000-0000");
        memberRepository.save(MemberEntity.toSaveEntity(memberDTO));
    }

    @Test
    @Transactional
    @Rollback(value = true)
    @DisplayName("회원가입 test")
    public void memberSaveTest() throws IOException {
        MemberDTO member = new MemberDTO("email2","pw2","name2","mobile1");
        Long saveId = memberService.save(member);
        MemberDTO findDTO = memberService.findById(saveId);
        assertThat(member.getMemberEmail()).isEqualTo(findDTO.getMemberEmail());
    }
    @Test
    @Transactional
    @Rollback(value = true)
    @DisplayName("로그인 test")
    public void loginTest(){
        MemberEntity memberEntity = memberRepository.findByMemberEmail("email1").get();
        MemberDTO loginMemberDTO = new MemberDTO();
        loginMemberDTO.setMemberEmail(memberEntity.getMemberEmail());
        loginMemberDTO.setMemberPassword(memberEntity.getMemberPassword());
        MemberDTO loginResult = memberService.login(loginMemberDTO);
        assertThat(loginResult).isNotNull();
    }

    @Test
    @Transactional
    @Rollback(value = false)
    @DisplayName("게시글 작성 테스트")
    public void memberBoardSaveTest(){
        BoardDTO boardDTO = newBoard(1); // 저장할 게시글 객체
        // 회원 앤티티 객체를 같이 줘야하니까 위에서 저장한 이메일값으로 회원 앤티티 조회
        MemberEntity memberEntity = memberRepository.findByMemberEmail("email1").get();
        // 게시글 객체와 회원 앤티티로 boardEntity 객체 생성
        BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO, memberEntity);
        // 저장 수행.
        boardRepository.save(boardEntity);
    }
    @Test
    @Transactional
    @Rollback(value = true)
    @DisplayName("회원 목록 test")
    public void memberListTest() {
        List<MemberDTO> memberDTOList = memberService.findAll();
        assertThat(memberDTOList.size()).isEqualTo(5);
    }
    @Test
    @Transactional
    @Rollback(value = true)
    @DisplayName("회원 삭제 test")
    public void memberDeleteTest(){
        memberService.delete(4L);
        assertThat(memberService.findById(4L)).isNull();
    }

    @Test
    @Transactional
    @Rollback(value = false)
    @DisplayName("게시글 조회 테스트")
    public void memberBoardFindByIdTest(){
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(8L);
        if(optionalBoardEntity.isPresent()){
            BoardEntity boardEntity = optionalBoardEntity.get();
            System.out.println("boardEntity_id=" + boardEntity.getId());
            System.out.println("boardEntity_title=" + boardEntity.getBoardTitle());
            System.out.println("boardEntity_name=" + boardEntity.getMemberEntity().getMemberName());
        }
    }

    @Test
    @Transactional
    @Rollback(value = false)
    @DisplayName("게시글 삭제 테스트")
    public void deleteTest(){
        memberRepository.deleteById(8L);

    }

    @Test
    @Transactional
    @Rollback(value = false)
    @DisplayName("댓글 작성 테스트")
    public void commentSaveTest(){
        BoardDTO boardDTO = newBoard(99);
        MemberEntity memberEntity = memberRepository.findByMemberEmail("email1").get();
        BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO, memberEntity);
        Long savedBoardId = boardRepository.save(boardEntity).getId();
        BoardEntity findBoardEntity = boardRepository.findById(savedBoardId).get();
        CommentDTO commentDTO = new CommentDTO("email1", "댓글내용", savedBoardId);
        CommentEntity commentEntity = CommentEntity.toSaveEntity(commentDTO, memberEntity, findBoardEntity);
        commentRepository.save(commentEntity);
    }

    @Test
    @Transactional
    @Rollback(value = false)
    @DisplayName("댓글 목록 출력 테스트")
    public void commentListTest(){
        // 댓글이 들어있는 게시글 앤티티 조회
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(11L);
        // 게시글 앤티티의 댓글 목록 조회
        if(optionalBoardEntity.isPresent()){
            BoardEntity boardEntity = optionalBoardEntity.get();
            List<CommentEntity> commentEntityList = boardEntity.getCommentEntityList();
            for(CommentEntity commentEntity: commentEntityList){
                System.out.println("commentEntity.getId() = " + commentEntity.getId());
                System.out.println("commentEntity.getCommentContents() = " + commentEntity.getCommentContents());
            }
        }
    }

//    @Test
//    @Transactional
//    @Rollback(value = false)
//    @DisplayName("검색 테스트")
//    public void searchTest(){
//        Page<BoardDTO> boardDTOList = boardService.search("t");
//        System.out.println("boardDTOList = " + boardDTOList);
//    }

    @Test
    @Transactional
    @DisplayName("페이징 테스트")
    public void pagingTest(){
        int page = 1;
        Page<BoardEntity> boardEntities = boardRepository.findAll(PageRequest.of(page, PagingConst.PAGE_LIMIT, Sort.by(Sort.Direction.DESC, "id")));
        // Page 객체가 제공해주는 메서드 확인
        System.out.println("boardEntities.getContent() = " + boardEntities.getContent()); // 요청페이지에 들어있는 데이터
        System.out.println("boardEntities.getTotalElements() = " + boardEntities.getTotalElements()); // 전체 글갯수
        System.out.println("boardEntities.getNumber() = " + boardEntities.getNumber()); // 요청페이지(jpa 기준)
        System.out.println("boardEntities.getTotalPages() = " + boardEntities.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boardEntities.getSize() = " + boardEntities.getSize()); // 한페이지에 보여지는 글갯수
        System.out.println("boardEntities.hasPrevious() = " + boardEntities.hasPrevious()); // 이전페이지 존재 여부
        System.out.println("boardEntities.isFirst() = " + boardEntities.isFirst()); // 첫페이지인지 여부
        System.out.println("boardEntities.isLast() = " + boardEntities.isLast()); // 마지막페이지인지 여부

        Page<BoardDTO> boardList = boardEntities.map(
                board -> new BoardDTO(board.getId(),
                        board.getBoardTitle(),
                        board.getBoardWriter(),
                        board.getBoardHits(),
                        board.getCreatedTime()
                ));

        System.out.println("boardList.getContent() = " + boardList.getContent()); // 요청페이지에 들어있는 데이터
        System.out.println("boardList.getTotalElements() = " + boardList.getTotalElements()); // 전체 글갯수
        System.out.println("boardList.getNumber() = " + boardList.getNumber()); // 요청페이지(jpa 기준)
        System.out.println("boardList.getTotalPages() = " + boardList.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boardList.getSize() = " + boardList.getSize()); // 한페이지에 보여지는 글갯수
        System.out.println("boardList.hasPrevious() = " + boardList.hasPrevious()); // 이전페이지 존재 여부
        System.out.println("boardList.isFirst() = " + boardList.isFirst()); // 첫페이지인지 여부
        System.out.println("boardList.isLast() = " + boardList.isLast()); // 마지막페이지인지 여부
    }
}
