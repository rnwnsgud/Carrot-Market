package com.guCoding.carrotMarket.domain.like;

import com.guCoding.carrotMarket.domain.BaseTimeEntity;
import com.guCoding.carrotMarket.domain.stuff.Stuff;
import com.guCoding.carrotMarket.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "like_tb",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "likes_uk",
                        columnNames = {"stuffId", "userId"}
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "stuff_tb")
    @ManyToOne(fetch = FetchType.LAZY)
    private Stuff stuff;

    @JoinColumn(name = "user_tb")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

}
