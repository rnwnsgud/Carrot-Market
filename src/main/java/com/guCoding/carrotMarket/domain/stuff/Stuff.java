package com.guCoding.carrotMarket.domain.stuff;

import com.guCoding.carrotMarket.domain.BaseTimeEntity;
import com.guCoding.carrotMarket.domain.user.TownEnum;
import com.guCoding.carrotMarket.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "stuff_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stuff extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // title == 삽니다 => stuff : BUY
    @Column(nullable = false,length = 60)
    private String title;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "STUFF_ENUM", joinColumns = @JoinColumn(name = "STUFF_ID"))
    @Column(nullable = false)
    private List<StuffEnum> stuffEnums = new ArrayList<>(); // 삽니다, 전자기기, 가구/인테리어

    @Enumerated(EnumType.STRING)
    private TransactionEnum transactionEnum; // 판매하기, 나눔하기

    // 0 == transaction : 기부하기
    @Column(nullable = false)
    private int price;

    @Column(nullable = false,length = 100)
    private String description;

//    private boolean isLiked; Like 테이블 있어야하나? -> 이게 있으면, 하나의 유저는 여러 개의 상품을 좋아할 수 있으며, 하나의 상품도 여러 유저로부터 좋아요를 받을 수 있어서 다대다.
    // 테이블로 풀어야겠다.
    // 가격이 내려가면 알림

    @Enumerated(EnumType.STRING)
    private TownEnum townEnum; // 거래장소, User 의 townEnums 에서 하나 가져왔다고 가정

    @JoinColumn(name = "user_tb")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Builder
    public Stuff(Long id, String title, List<StuffEnum> stuffEnums, TransactionEnum transactionEnum, int price, String description, TownEnum townEnum, User user) {
        this.id = id;
        this.title = title;
        this.stuffEnums = stuffEnums;
        this.transactionEnum = transactionEnum;
        this.price = price;
        this.description = description;
        this.townEnum = townEnum;
        this.user = user;
    }
}
