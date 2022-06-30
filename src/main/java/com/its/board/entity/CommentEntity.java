package com.its.board.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name = "comment_table")
public class CommentEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(length = 20, nullable = false)
    private String commentWriter;

    @Column(length = 200, nullable = false)
    private String commentContents;

    // 회원 댓글 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boardId")
    private BoardEntity boardEntity;

}
