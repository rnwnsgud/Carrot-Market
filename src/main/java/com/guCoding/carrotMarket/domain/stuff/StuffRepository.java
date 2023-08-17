package com.guCoding.carrotMarket.domain.stuff;

import com.guCoding.carrotMarket.domain.user.TownEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StuffRepository extends JpaRepository<Stuff, Long> {
    List<Stuff> findByTownEnum(TownEnum townEnum);
}
