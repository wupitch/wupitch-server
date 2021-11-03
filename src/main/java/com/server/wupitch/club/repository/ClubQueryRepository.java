package com.server.wupitch.club.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.SimplePath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.server.wupitch.area.Area;
import com.server.wupitch.area.QArea;
import com.server.wupitch.club.Club;
import com.server.wupitch.club.QClub;
import com.server.wupitch.sports.entity.QSports;
import com.server.wupitch.sports.entity.Sports;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ClubQueryRepository implements ClubRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    private OrderSpecifier<?>[] getSortedColumn(Sort sorts) {
        return sorts.toList().stream().map(x -> {
            Order order = x.getDirection().name() == "ASC" ? Order.ASC : Order.DESC;
            SimplePath<Object> filedPath = Expressions.path(Object.class, QClub.club, x.getProperty());
            return new OrderSpecifier(order, filedPath);
        }).toArray(OrderSpecifier[]::new);
    }

    @Override
    public Page<Club> findAllClub(Pageable pageable, Area area, Sports sports,
                                  List<Integer> days, Integer startTime, Integer endTime, Integer memberCountValue, Integer ageValue) {

        QClub qClub = QClub.club;
        QArea qArea = QArea.area;
        QSports qSports = QSports.sports;
        Boolean monday = false;
        Boolean tuesday = false;
        Boolean wednesday = false;
        Boolean thursday = false;
        Boolean friday = false;
        Boolean saturday = false;
        Boolean sunday = false;
        for (Integer day : days) {
            if(day == 1) monday = true;
            else if(day == 2) tuesday = true;
            else if(day == 3) wednesday = true;
            else if(day == 4) thursday = true;
            else if(day == 5) friday = true;
            else if(day == 6) saturday = true;
            else sunday = true;
        }

        QueryResults<Club> result = queryFactory
                .select(qClub)
                .from(qClub)
                .join(qArea).fetchJoin()
                .join(qSports).fetchJoin()
                .where(
                        areaEq(qClub, area),sportsEq(qClub, sports),
                        startTimeEq(qClub,startTime), endTimeEq(qClub, endTime),
                        memberCountValueEq(qClub, memberCountValue), ageValueEq(qClub, ageValue),
                        dayEq(1,qClub, monday), dayEq(2,qClub, tuesday),dayEq(3,qClub, wednesday),dayEq(4,qClub, thursday),dayEq(5,qClub, friday),dayEq(6,qClub, saturday),dayEq(7,qClub, sunday)
                        )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getSortedColumn(pageable.getSort()))
                .fetchResults();

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    private BooleanExpression areaEq(QClub qClub, Area area) {
        if(area == null) return null;
        return qClub.area.eq(area);
    }

    private BooleanExpression sportsEq(QClub qClub, Sports sports) {
        if(sports == null) return null;
        return qClub.sports.eq(sports);
     }

    private BooleanExpression dayEq(int idx, QClub qClub, Boolean day) {
      if(!day) return null;
      if(idx==1) return qClub.monday;
      else if(idx==2) return qClub.tuesday;
      else if(idx==3) return qClub.wednesday;
      else if(idx==4) return qClub.thursday;
      else if(idx==5) return qClub.friday;
      else if(idx==6) return qClub.saturday;
      else return qClub.sunday;
    }

    private BooleanExpression startTimeEq(QClub qClub, Integer startTime) {
        if(startTime == null) return null;
        return qClub.startTime.goe(startTime);
    }

    private BooleanExpression endTimeEq(QClub qClub, Integer endTime) {
        if(endTime == null) return null;
        return qClub.endTime.loe(endTime);
    }

    private BooleanExpression memberCountValueEq(QClub qClub, Integer memberCountValue) {
        if(memberCountValue == null) return null;
        if (memberCountValue == 1){
            return qClub.memberCount.loe(10);
        }
        else if (memberCountValue == 2) {
            return qClub.memberCount.gt(10).and(qClub.memberCount.loe(30));
        }
        else if (memberCountValue == 3) {
            return qClub.memberCount.gt(30).and(qClub.memberCount.loe(50));
        }
        else if (memberCountValue == 4) {
            return qClub.memberCount.gt(50).and(qClub.memberCount.loe(70));
        }
        else {
            return qClub.memberCount.gt(70);
        }
    }

    private BooleanExpression ageValueEq(QClub qClub, Integer ageValue) {
        if(ageValue == null) return null;
        return qClub.ageValue.eq(ageValue);
    }
}