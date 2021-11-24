package com.server.wupitch.impromptu.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.SimplePath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.server.wupitch.area.Area;
import com.server.wupitch.area.QArea;
import com.server.wupitch.impromptu.entity.Impromptu;
import com.server.wupitch.impromptu.entity.QImpromptu;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.server.wupitch.configure.entity.Status.VALID;

@RequiredArgsConstructor
@Repository
public class ImpromptuQueryRepository implements ImpromptuRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    private OrderSpecifier<?>[] getSortedColumn(Sort sorts) {
        return sorts.toList().stream().map(x -> {
            Order order = x.getDirection().name() == "ASC" ? Order.ASC : Order.DESC;
            SimplePath<Object> filedPath = Expressions.path(Object.class, QImpromptu.impromptu, x.getProperty());
            return new OrderSpecifier(order, filedPath);
        }).toArray(OrderSpecifier[]::new);
    }

    @Override
    public Page<Impromptu> getAllImpromptuList(Pageable pageable, Area area,
                                               Integer scheduleIndex, List<Integer> days, Integer memberCountIndex) {
        QImpromptu qImpromptu = QImpromptu.impromptu;
        QArea qArea = QArea.area;

        QueryResults<Impromptu> result = queryFactory
                .select(qImpromptu)
                .from(qImpromptu)
//                .leftJoin(qArea).on(qImpromptu.area.eq(qArea).and(qArea.status.eq(VALID)))
                .where(
                        afterBoolean(qImpromptu),
                        areaEq(qImpromptu, area),
                        scheduleIndexEq(qImpromptu, scheduleIndex),
                        daysEq(qImpromptu, days),
                        memberCountIndexEq(qImpromptu, memberCountIndex)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getSortedColumn(pageable.getSort()))
                .fetchResults();

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    private BooleanExpression afterBoolean(QImpromptu qImpromptu) {
        return qImpromptu.date.after(LocalDate.now());
    }

    private BooleanExpression areaEq(QImpromptu qImpromptu, Area area) {
        if (area == null) return null;
        return qImpromptu.area.eq(area);
    }

    private BooleanExpression daysEq(QImpromptu qImpromptu, List<Integer> days) {
        if(days == null) return null;
        return qImpromptu.dayIdx.in(days);
    }

    private BooleanExpression scheduleIndexEq(QImpromptu qImpromptu, Integer scheduleIndex) {
        if(scheduleIndex == null) return null;
        LocalDate now = LocalDate.now();
        final int WEEK = 7;
        return qImpromptu.date.before(now.plusDays(WEEK * scheduleIndex +1));
    }

    private BooleanExpression memberCountIndexEq(QImpromptu qImpromptu, Integer memberCountIndex) {
        if(memberCountIndex == null) return null;
        if(memberCountIndex == 1) return qImpromptu.recruitmentCount.loe(5);
        else if(memberCountIndex == 2) return qImpromptu.recruitmentCount.loe(10).and(qImpromptu.recruitmentCount.gt(5));
        else if(memberCountIndex == 3) return qImpromptu.recruitmentCount.loe(15).and(qImpromptu.recruitmentCount.gt(10));
        else return qImpromptu.recruitmentCount.loe(20).and(qImpromptu.recruitmentCount.gt(15));
    }
}
