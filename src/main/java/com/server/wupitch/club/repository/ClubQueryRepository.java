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

import java.util.Arrays;
import java.util.List;

import static com.server.wupitch.configure.entity.Status.*;

@RequiredArgsConstructor
@Repository
public class ClubQueryRepository implements ClubRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private OrderSpecifier<?>[] getSortedColumn(Sort sorts) {
        return sorts.toList().stream().map(x -> {
            Order order = x.getDirection().name() == "ASC" ? Order.ASC : Order.DESC;
            SimplePath<Object> filedPath = Expressions.path(Object.class, QClub.club, x.getProperty());
            return new OrderSpecifier(order, filedPath);
        }).toArray(OrderSpecifier[]::new);
    }

    @Override
    public Page<Club> findAllClub(Pageable pageable, Area area, List<Long> sportsList,
                                  List<Integer> days, Integer memberCountValue, List<Integer> ageList) {

        QClub qClub = QClub.club;
        QArea qArea = QArea.area;
        QSports qSports = QSports.sports;
        Boolean[] boolDays = new Boolean[8];
        Arrays.fill(boolDays, false);
        if(days != null) for (Integer day : days) boolDays[day] = true;

        Boolean[] boolAgeList = new Boolean[6];
        Arrays.fill(boolAgeList, false);
        if(ageList != null) for (Integer integer : ageList) boolAgeList[integer] = true;

        QueryResults<Club> result = queryFactory
                .select(qClub)
                .from(qClub)
                .leftJoin(qArea).on(qClub.area.eq(qArea).and(qArea.status.eq(VALID)))
                .leftJoin(qSports).on(qClub.sports.eq(qSports).and(qSports.status.eq(VALID)))
                .where(
                        areaEq(qClub, area), sportsEq(qClub, sportsList),
                        memberCountValueEq(qClub, memberCountValue),
                        dayEq(1, qClub, boolDays[1]), dayEq(2, qClub, boolDays[2]), dayEq(3, qClub, boolDays[3]), dayEq(4, qClub, boolDays[4]), dayEq(5, qClub, boolDays[5]), dayEq(6, qClub, boolDays[6]), dayEq(7, qClub, boolDays[7]),
                        ageEq(1, qClub, boolAgeList[1]), ageEq(2, qClub, boolAgeList[2]), ageEq(3, qClub, boolAgeList[3]), ageEq(4, qClub, boolAgeList[4]), ageEq(5, qClub, boolAgeList[5])
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getSortedColumn(pageable.getSort()))
                .fetchResults();

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    private BooleanExpression areaEq(QClub qClub, Area area) {
        if (area == null) return null;
        return qClub.area.eq(area);
    }

    private BooleanExpression sportsEq(QClub qClub, List<Long> sportsList) {
        if (sportsList == null) return null;
        return qClub.sports.sportsId.in(sportsList);
    }

    private BooleanExpression dayEq(int idx, QClub qClub, Boolean day) {
        if (!day) return null;
        if (idx == 1) return qClub.monday.eq(true);
        else if (idx == 2) return qClub.tuesday.eq(true);
        else if (idx == 3) return qClub.wednesday.eq(true);
        else if (idx == 4) return qClub.thursday.eq(true);
        else if (idx == 5) return qClub.friday.eq(true);
        else if (idx == 6) return qClub.saturday.eq(true);
        else return qClub.sunday.eq(true);
    }

    private BooleanExpression ageEq(int idx, QClub qClub, Boolean ageValue) {
        if (!ageValue) return null;
        if (idx == 1) return qClub.teenager.eq(true);
        else if (idx == 2) return qClub.twenties.eq(true);
        else if (idx == 3) return qClub.thirties.eq(true);
        else if (idx == 4) return qClub.forties.eq(true);
        else return qClub.moreAge.eq(true);
    }

//    private BooleanExpression startTimeEq(QClub qClub, Double startTime) {
//        if (startTime == null) return null;
//        return qClub.startTime.goe(startTime);
//    }
//
//    private BooleanExpression endTimeEq(QClub qClub, Double endTime) {
//        if (endTime == null) return null;
//        return qClub.endTime.loe(endTime);
//    }

    private BooleanExpression memberCountValueEq(QClub qClub, Integer memberCountValue) {
        if (memberCountValue == null) return null;
        if (memberCountValue == 1) {
            return qClub.memberCount.loe(10);
        } else if (memberCountValue == 2) {
            return qClub.memberCount.gt(10).and(qClub.memberCount.loe(30));
        } else if (memberCountValue == 3) {
            return qClub.memberCount.gt(30).and(qClub.memberCount.loe(50));
        } else if (memberCountValue == 4) {
            return qClub.memberCount.gt(50).and(qClub.memberCount.loe(70));
        } else {
            return qClub.memberCount.gt(70);
        }
    }

}
