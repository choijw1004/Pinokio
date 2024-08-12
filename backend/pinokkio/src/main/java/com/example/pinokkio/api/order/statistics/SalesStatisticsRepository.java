package com.example.pinokkio.api.order.statistics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SalesStatisticsRepository extends JpaRepository<SalesStatistics, UUID> {

    List<SalesStatistics> findByPosIdAndPeriodTypeAndYearAndPeriodBetween(
            UUID posId, PeriodType periodType, int year, int startPeriod, int endPeriod);

    Optional<SalesStatistics> findByPosIdAndPeriodTypeAndYearAndPeriod(
            UUID posId, PeriodType periodType, int year, int period);

    List<SalesStatistics> findByPosIdAndPeriodTypeAndYearBetween(
            UUID posId, PeriodType periodType, int startYear, int endYear);
}
