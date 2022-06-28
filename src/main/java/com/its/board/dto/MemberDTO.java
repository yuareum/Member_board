package com.its.board.dto;

import com.its.board.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
    private Long id;
    private String memberId;
    private String memberPassword;
    private String memberEmail;
    private String memberName;
    private int memberAge;
    private String memberMobile;
    private MultipartFile memberProfile;
    private String memberProfileName;
}
