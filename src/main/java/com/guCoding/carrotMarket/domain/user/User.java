package com.guCoding.carrotMarket.domain.user;

import com.guCoding.carrotMarket.domain.BaseTimeEntity;
import com.guCoding.carrotMarket.domain.stuff.Stuff;
import com.guCoding.carrotMarket.dto.user.UserReqDto;
import com.guCoding.carrotMarket.dto.user.UserReqDto.EditReqDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "user_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 60)
    private String nickname;

    @Column(nullable = false, length = 60)
    private String password;

    @Column(nullable = false, unique = true)
    private String identifier;

    @Column(length = 40, unique = true)
    private String email;

    @Column(unique = true, length = 13)
    private String phoneNumber;

    @Column(length = 10)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserEnum role;

    @ElementCollection // 값 타입 컬렉션을 매핑할 때 사용
    @Enumerated(EnumType.STRING) // 별도 테이블이 생성된다.
    @CollectionTable(name = "TOWN_ENUM", joinColumns = @JoinColumn(name = "USER_ID")) // 값 타입 컬렉션을 매핑할 테이블에 대한 정보를 지정하는 역할
    @Column(nullable = false)
    private List<TownEnum> townEnums = new ArrayList();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Stuff> stuffs = new ArrayList<>();

    private double mannerTemp;

    @Builder
    public User(Long id, String nickname, String password, String identifier, String email, String phoneNumber, UserEnum role, List<TownEnum> townEnums, String username, double mannerTemp) {
        this.id = id;
        this.nickname = nickname;
        this.password = password;
        this.identifier = identifier;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.townEnums = townEnums;
        this.username = username;
        this.mannerTemp = mannerTemp;
    }

    // 수정 필요
    public void changeUserInfo(EditReqDto editReqDto) {
        if(!editReqDto.getEmail().isBlank()) {
            this.email = editReqDto.getEmail();
        }

        if(!editReqDto.getPhoneNumber().isBlank()) {
            this.phoneNumber = editReqDto.getPhoneNumber();
        }
    }


}
