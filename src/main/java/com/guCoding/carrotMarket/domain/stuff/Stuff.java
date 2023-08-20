package com.guCoding.carrotMarket.domain.stuff;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.guCoding.carrotMarket.domain.BaseTimeEntity;
import com.guCoding.carrotMarket.domain.like.Like;
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

    @JsonIgnore
    @OneToMany(mappedBy = "stuff")
    private List<Like> likes;

    @Transient
    private boolean likeState;

    @Transient
    private int likeCount;

    @Enumerated(EnumType.STRING)
    private TownEnum townEnum; // 거래장소, User 의 townEnums 에서 하나 가져왔다고 가정

    @JoinColumn(name = "user_id")
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
