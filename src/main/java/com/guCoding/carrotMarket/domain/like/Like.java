package com.guCoding.carrotMarket.domain.like;

import com.guCoding.carrotMarket.domain.BaseTimeEntity;
import com.guCoding.carrotMarket.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "like_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_tb")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private boolean isLiked;


}
