package sogong.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sogong.restaurant.domain.Employee;
import sogong.restaurant.domain.Payment;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {
    @Query(value = "select PaymentId,finalPrice,method,payTime,EmployeeId,BranchId,orderId from Payment where PaymentId = :id and BranchId = :branchId", nativeQuery = true)
    public Optional<Payment>findByIdAndManager(@Param(value = "id")Long id, @Param(value = "branchId") Long branchId);
    @Query(value = "select PaymentId,finalPrice,method,payTime,EmployeeId,BranchId,orderId from Payment where BranchId = :bid and payTime between :st and :en", nativeQuery = true)
    public List<Payment> findByManagerAndDateBetween(@Param(value = "bid")Long bid, @Param("st") String stdate, @Param("en") String endate);

    //    @Query(value = "select PaymentId,finalPrice,date,method,payTime,EmployeeId,BranchId,orderId from Payment where DAYOFWEEK(date)=:d", nativeQuery = true)
//    public List<Payment>findPaymentsByManagerAndAndPayTime(@Param(value = "bid")Long bid, @Param(value = "d")Long day);
    @Query(value = "select PaymentId,finalPrice,method,payTime,EmployeeId,BranchId,orderId from Payment where BranchId = :branchId", nativeQuery = true)
    public List<PaymentSummary>findAllByManager(@Param(value = "branchId") Long branchId);
    /*@Query(value = "", nativeQuery = true)
    public List<PaymentSummary>findPaymentsByManagerAndPayTime(@Param(value="branchId")Long branchId, @Param(value=""));
    //SELECT date_format(payTime,'%Y-%m-%d') AS 'date', sum(finalPrice) From payment group by date_format(payTime,'%y-%m-%d');*/

    @Query(value = "SELECT DAYOFWEEK(payTime)\n" +
            "AS DateRange, count(finalPrice) AS total, sum(finalPrice) AS totalSale FROM Payment WHERE BranchId =:bid GROUP BY DAYOFWEEK(payTime)", nativeQuery = true)
    public List<PaymentDaySummary> findAllByManagerAndPayTimeFROMALLBYDAY(@Param(value = "bid")Long branchId);
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
    @Query(value = "SELECT WEEK(payTime) weeks,sum(finalPrice) as totalPrice, count(finalPrice) as totalCount FROM payment where payTime >= :st and payTime <= :end and BranchId =:bid GROUP BY weeks", nativeQuery = true)
    public List<PaymentWeekSummary> findAllByManagerAndPayTimeFROMWEEK(@Param(value="bid")Long branchId, @Param(value="st")String start, @Param(value="end")String end);


    @Query(value = "select MONTH(payTime) as months, sum(finalPrice) as totalPrice, count(finalPrice) as totalCount," +
            " sum(CASE WHEN method='카드'Then finalPrice END) as cardTotalPrice, count(CASE WHEN method='카드'Then finalPrice END) as cardTotal," +
            " sum(CASE WHEN method='현금'Then finalPrice END) as cashTotalPrice, count(CASE WHEN method='현금'Then finalPrice END) as cashTotal" +
            " FROM pos.payment where payTime >=:st and payTime <= :end and BranchId =:bid GROUP BY months\n", nativeQuery = true)
    public List<PaymentMonthSummary> findAllByManagerAndPayTimeFromMonth(@Param(value="bid")Long branchId, @Param(value="st")String start, @Param(value="end")String end);

    @Query(value = "SELECT sum(finalPrice) as yearPrice, count(finalPrice) as yearCount, \n" +
            "count(CASE WHEN WEEK(payTime) =  WEEK(now()) THEN finalPrice END) AS weekCount,\n" +
            "sum(CASE WHEN WEEK(payTime) =  WEEK(now()) THEN finalPrice END) AS weekPrice,\n" +
            "count(CASE WHEN Month(payTime) = Month(now()) THEN finalPrice END) AS monthCount,\n" +
            "sum(CASE WHEN Month(payTime) = Month(now()) THEN finalPrice END) AS monthPrice,\n" +
            "count(CASE WHEN date(payTime)=date(now()) THEN finalPrice END) AS todayCount,\n" +
            "sum(CASE WHEN date(payTime)=date(now()) THEN finalPrice END) AS todayPrice\n" +
            "FROM pos.payment where BranchId = :bid and payTime >=:st and payTime <= :end", nativeQuery = true)
    public PayMentTodaySummary findByManagerToday(@Param(value="bid")Long branchId, @Param(value="st")String start, @Param(value="end")String end);


    @Query(value ="SELECT day(payTime) as date, sum(finalPrice) as totalPrice, count(finalPrice) as totalCount FROM pos.payment where BranchId=:bid and week(payTime)=week(now()) group by date order by date",  nativeQuery = true)
    public List<PaymentWeeklySummary> findByManagerAAndPayTimeFromWeekly(@Param(value="bid")Long branchId);

}
