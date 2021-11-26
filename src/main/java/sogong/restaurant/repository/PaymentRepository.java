package sogong.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sogong.restaurant.domain.Payment;
import sogong.restaurant.summary.*;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {
    @Query(value = "select PaymentId,finalPrice,method,payTime,EmployeeId,BranchId,orderId from Payment where PaymentId = :id and BranchId = :branchId", nativeQuery = true)
    public Optional<Payment> findByIdAndManager(@Param(value = "id") Long id, @Param(value = "branchId") Long branchId);

    @Query(value = "select PaymentId,finalPrice,method,payTime,EmployeeId,BranchId,orderId from Payment where BranchId = :bid and payTime between :st and :en", nativeQuery = true)
    public List<Payment> findByManagerAndDateBetween(@Param(value = "bid") Long bid, @Param("st") String stdate, @Param("en") String endate);

    //    @Query(value = "select PaymentId,finalPrice,date,method,payTime,EmployeeId,BranchId,orderId from Payment where DAYOFWEEK(date)=:d", nativeQuery = true)
//    public List<Payment>findPaymentsByManagerAndAndPayTime(@Param(value = "bid")Long bid, @Param(value = "d")Long day);
    @Query(value = "select PaymentId,finalPrice,method,payTime,EmployeeId,BranchId,orderId from Payment where BranchId = :branchId", nativeQuery = true)
    public List<PaymentSummary> findAllByManager(@Param(value = "branchId") Long branchId);
    /*@Query(value = "", nativeQuery = true)
    public List<PaymentSummary>findPaymentsByManagerAndPayTime(@Param(value="branchId")Long branchId, @Param(value=""));
    //SELECT date_format(payTime,'%Y-%m-%d') AS 'date', sum(finalPrice) From payment group by date_format(payTime,'%y-%m-%d');*/

    @Query(value = "SELECT DAYOFWEEK(payTime)\n" +
            "AS DateRange, count(finalPrice) AS total, sum(finalPrice) AS totalSale FROM Payment WHERE BranchId =:bid GROUP BY DAYOFWEEK(payTime)", nativeQuery = true)
    public List<PaymentDaySummary> findAllByManagerAndPayTimeFROMALLBYDAY(@Param(value = "bid") Long branchId);

    /*SELECT
      CASE DAYOFWEEK(payTime)
        WHEN 1 THEN "Sun"
        WHEN 2 THEN "Mon"
        WHEN 3 THEN "Tue"
        WHEN 4 THEN "Wed"
        WHEN 5 THEN "Thu"
        WHEN 6 THEN "Fri"
        WHEN 7 THEN "Sat"
      END AS DateRange
    ,count(finalPrice) AS Total
    ,sum(finalPrice) AS TotalSale
    FROM payment
    GROUP BY DAYOFWEEK(payTime);
*/
    @Query(value = "SELECT WEEK(payTime) weeks,sum(finalPrice) as totalSale, count(finalPrice) as totalCount," +
            "count(CASE WHEN WEEK(payTime)=WEEK(now()) AND method=\"현금\" THEN finalPrice END) AS CashTotal,\n" +
            "sum(CASE WHEN WEEK(payTime)=WEEK(now()) AND method=\"현금\" THEN finalPrice END) AS CashTotalSale,\n" +
            "count(CASE WHEN WEEK(payTime)=WEEK(now()) AND method=\"카드\" THEN finalPrice END) AS CardTotal,\n" +
            "sum(CASE WHEN WEEK(payTime)=WEEK(now()) AND method=\"카드\"THEN finalPrice END) AS CardTotalSale" +
            " FROM payment where payTime >= :st and payTime <= :end and BranchId =:bid GROUP BY weeks order by weeks", nativeQuery = true)
    public List<PaymentWeekSummary> findAllByManagerAndPayTimeFROMWEEK(@Param(value = "bid") Long branchId, @Param(value = "st") String start, @Param(value = "end") String end);


    @Query(value = "select MONTH(payTime) as months, sum(finalPrice) as totalSale, count(finalPrice) as totalCount," +
            " sum(CASE WHEN method='카드'Then finalPrice END) as cardTotalSale, count(CASE WHEN method='카드'Then finalPrice END) as cardTotal," +
            " sum(CASE WHEN method='현금'Then finalPrice END) as cashTotalSale, count(CASE WHEN method='현금'Then finalPrice END) as cashTotal" +
            " FROM pos.payment where payTime >=:st and payTime <= :end and BranchId =:bid GROUP BY months order by months\n", nativeQuery = true)
    public List<PaymentMonthSummary> findAllByManagerAndPayTimeFromMonth(@Param(value = "bid") Long branchId, @Param(value = "st") String start, @Param(value = "end") String end);

    @Query(value = "SELECT sum(finalPrice) as yearSale, count(finalPrice) as yearCount, \n" +
            "count(CASE WHEN WEEK(payTime) =  WEEK(now()) THEN finalPrice END) AS weekCount,\n" +
            "sum(CASE WHEN WEEK(payTime) =  WEEK(now()) THEN finalPrice END) AS weekSale,\n" +
            "count(CASE WHEN Month(payTime) = Month(now()) THEN finalPrice END) AS monthCount,\n" +
            "sum(CASE WHEN Month(payTime) = Month(now()) THEN finalPrice END) AS monthSale,\n" +
            "count(CASE WHEN date(payTime)=date(now()) THEN finalPrice END) AS todayCount,\n" +
            "sum(CASE WHEN date(payTime)=date(now()) THEN finalPrice END) AS todaySale,\n" +
            "count(CASE WHEN date(payTime)=date(now()) AND method ='현금' THEN finalPrice END) AS todayCashTotal,\n" +
            "sum(CASE WHEN date(payTime)=date(now()) AND method ='현금' THEN finalPrice END) AS todayCashTotalSale,\n" +
            "count(CASE WHEN date(payTime)=date(now()) AND method = '카드' THEN finalPrice END) AS todayCardTotal,\n" +
            "sum(CASE WHEN date(payTime)=date(now()) AND method ='카드' THEN finalPrice END) AS todayCardTotalSale\n" +
            "FROM payment where BranchId = :bid and payTime >=:st and payTime <= :end", nativeQuery = true)
    public PayMentTodaySummary findByManagerToday(@Param(value = "bid") Long branchId, @Param(value = "st") String start, @Param(value = "end") String end);


    @Query(value = "SELECT day(payTime) as date, sum(finalPrice) as totalSale, count(finalPrice) as totalCount FROM pos.payment where BranchId=:bid and week(payTime)=week(now()) group by date order by date", nativeQuery = true)
    public List<PaymentWeeklySummary> findByManagerAAndPayTimeFromWeekly(@Param(value = "bid") Long branchId);

    @Query(value = "select day(payTime) as date, count(finalPrice) as totalCount, sum(finalPrice) as totalSale from pos.payment where BranchId =:bid AND DATE(payTime) between CURDATE()-7 AND CURDATE() group by date order by date", nativeQuery = true)
    public List<PaymentWeeklySummary> findByManagerAndPayTimeFROMRecent7Days(@Param(value = "bid") Long branchId);

    @Query(value = "SELECT DAYOFWEEK(payTime) AS DateRange, count(finalPrice) AS total, sum(finalPrice) AS totalSale FROM pos.payment" +
            " WHERE BranchId =:bid AND payTime >= :st AND payTime <= :end " +
            "GROUP BY DAYOFWEEK(payTime) order by DAYOFWEEK(payTime)", nativeQuery = true)
    public List<PaymentDaySummary> findAllByManagerAndPayTimeSortedByDayOfWeekBetween(@Param(value = "bid") Long branchId, @Param(value = "st") String start, @Param(value = "end") String end);

    @Query(value = "SELECT  Day(payTime) AS DateRange, count(finalPrice) AS total, sum(finalPrice) AS totalSale FROM pos.payment \n" +
            "WHERE BranchId =:bid AND payTime >= :st AND payTime <= :end Group by DateRange order by DateRange", nativeQuery = true)
    public List<PaymentDaySummary> findALlByManagerAndPayTimeSortedByDayBetween(@Param(value = "bid") Long branchId, @Param(value = "st") String start, @Param(value = "end") String end);

    @Query(value = "SELECT  Hour(payTime) AS hour, count(finalPrice) AS total, sum(finalPrice) AS totalSale FROM pos.payment \n" +
            "WHERE BranchId =:bid AND payTime >= :st AND payTime <= :end Group by hour order by hour", nativeQuery = true)
    public List<PaymentHourSummary> findALLByManagerAndPayTimeSortedByHourBetween(@Param(value = "bid") Long branchId, @Param(value = "st") String start, @Param(value = "end") String end);


    @Query(value = "SELECT  Date(payTime) AS DateRange, count(finalPrice) AS total, sum(finalPrice) AS totalSale FROM pos.payment\n" +
            "WHERE BranchId =:bid AND payTime >= :st AND payTime <= :end Group by DateRange order by DateRange", nativeQuery = true)
    public List<PaymentDateSummary> findByManagerAndPayTimeSortedByDateBetweenInput(@Param(value = "bid") Long branchId, @Param(value = "st") String start, @Param(value = "end") String end);

    @Query(value = "SELECT sum(finalPrice) as totalSale, count(finalPrice) as totalCount,\n" +
            "count(CASE WHEN method= '현금' THEN finalPrice END) AS CashTotal,\n" +
            "sum(CASE WHEN method='현금' THEN finalPrice END) AS CashTotalSale,\n" +
            "count(CASE WHEN method='카드' THEN finalPrice END) AS CardTotal,\n" +
            "sum(CASE WHEN method='카드'THEN finalPrice END) AS CardTotalSale\n" +
            "FROM pos.payment where payTime >= :st and payTime <= :end and BranchId =:bid", nativeQuery = true)
    public List<PaymentSumSummary> findByManagerAndPayTimeSumSummaryBetweenInput(@Param(value = "bid") Long branchId, @Param(value = "st") String start, @Param(value = "end") String end);

    @Query(value = "SELECT month(payTime) as month,\n" +
            "count(CASE WHEN orderType='TABLE_ORDER' THEN finalPrice END) as tableTotalCount, sum(CASE WHEN orderType='TABLE_ORDER' THEN finalPrice END) as tableTotalSale,\n" +
            "count(CASE WHEN orderType='TAKEOUT_ORDER' THEN finalPrice END) as takeOutTotalCount, sum(CASE WHEN orderType='TAKEOUT_ORDER' THEN finalPrice END) as takeOutTotalSale \n" +
            "FROM pos.payment as p join pos.menuorder as o ON p.orderId = o.orderId where payTime >= :st AND payTime <= :end AND p.BranchId = :bid group by month order by month ", nativeQuery = true)
    public List<PaymentMonthOrderTypeSummary> findByMangerANDOrderIdANDPayTimeANDOrderTypeMonthSummary(@Param(value = "bid") Long branchId, @Param(value = "st") String start, @Param(value = "end") String end);

    @Query(value = "SELECT Date(payTime) as date,\n" +
            "count(CASE WHEN orderType='TABLE_ORDER' THEN finalPrice END) as tableTotalCount, sum(CASE WHEN orderType='TABLE_ORDER' THEN finalPrice END) as tableTotalSale,\n" +
            "count(CASE WHEN orderType='TAKEOUT_ORDER' THEN finalPrice END) as takeOutTotalCount, sum(CASE WHEN orderType='TAKEOUT_ORDER' THEN finalPrice END) as takeOutTotalSale \n" +
            "FROM pos.payment as p join pos.menuorder as o ON p.orderId = o.orderId where Date(payTime) = Date(now()) AND p.BranchId = :bid group by date", nativeQuery = true)
    public List<PaymentTodayOrderTypeSummary> findByMangerANDOrderIdANDPayTimeANDOrderTypeTodaySummary(@Param(value = "bid") Long branchId);

    @Query(value = "SELECT week(payTime) as date,\n" +
            "count(CASE WHEN orderType='TABLE_ORDER' THEN finalPrice END) as tableTotalCount, sum(CASE WHEN orderType='TABLE_ORDER' THEN finalPrice END) as tableTotalSale,\n" +
            "count(CASE WHEN orderType='TAKEOUT_ORDER' THEN finalPrice END) as takeOutTotalCount, sum(CASE WHEN orderType='TAKEOUT_ORDER' THEN finalPrice END) as takeOutTotalSale \n" +
            "FROM pos.payment as p join pos.menuorder as o ON p.orderId = o.orderId where payTime >= :st AND payTime <= :end AND p.BranchId = :bid group by date order by date", nativeQuery = true)
    public List<PaymentTodayOrderTypeSummary> findByManagerANDOrderIdANDPayTimeANDOrderTypeWeekSummary(@Param(value = "bid") Long branchId, @Param(value = "st") String start, @Param(value = "end") String end);

    @Query(value = "SELECT Day(payTime) as date,\n" +
            "count(CASE WHEN orderType='TABLE_ORDER' THEN finalPrice END) as tableTotalCount, sum(CASE WHEN orderType='TABLE_ORDER' THEN finalPrice END) as tableTotalSale,\n" +
            "count(CASE WHEN orderType='TAKEOUT_ORDER' THEN finalPrice END) as takeOutTotalCount, sum(CASE WHEN orderType='TAKEOUT_ORDER' THEN finalPrice END) as takeOutTotalSale \n" +
            "FROM pos.payment as p join pos.menuorder as o ON p.orderId = o.orderId where payTime >= :st AND payTime <= :end AND p.BranchId = :bid group by date order by date", nativeQuery = true)
    public List<PaymentTodayOrderTypeSummary> findByManagerANDOrderIdANDPayTimeANDOrderTypeDaySummary(@Param(value = "bid") Long branchId, @Param(value = "st") String start, @Param(value = "end") String end);

    @Query(value = "SELECT \n" +
            "count(CASE WHEN orderType='TABLE_ORDER' THEN finalPrice END) as tableTotalCount, sum(CASE WHEN orderType='TABLE_ORDER' THEN finalPrice END) as tableTotalSale,\n" +
            "count(CASE WHEN orderType='TAKEOUT_ORDER' THEN finalPrice END) as takeOutTotalCount, sum(CASE WHEN orderType='TAKEOUT_ORDER' THEN finalPrice END) as takeOutTotalSale \n" +
            "FROM pos.payment as p join pos.menuorder as o ON p.orderId = o.orderId where payTime >= :st AND payTime <= :end AND p.BranchId = :bid", nativeQuery = true)
    public List<PaymentTodayOrderTypeSummary> findByManagerAndOrderIdAndPayTimeAndOrderTypeBetweenInputSumSummary(@Param(value = "bid") Long branchId, @Param(value = "st") String start, @Param(value = "end") String end);


}