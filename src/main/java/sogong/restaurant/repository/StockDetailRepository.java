package sogong.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sogong.restaurant.domain.Stock;
import sogong.restaurant.domain.StockDetail;
import sogong.restaurant.summary.StockDetailSummary;

import java.util.Date;
import java.util.List;

public interface StockDetailRepository extends JpaRepository<StockDetail, Long> {
    @Override
    List<StockDetail> findAll();

    List<StockDetail> findStockDetailByStock(Stock stock);

    //List<StockDetail> findStockDetailsByStock(Stock stock);
    @Query(value = "select StockDetailId, quantityChanged, time,EmployeeId,StockId,finalQuantity, memo from stockdetail where time between :st and :en and stockId IN (select stockId from stock where branchId=:bid)", nativeQuery = true)
    List<StockDetail> findAllByDateAndBranchId(@Param(value = "bid") Long bid, @Param("st") Date stdate, @Param("en") Date endate);

    List<StockDetailSummary> findAllByStock(Stock stock);
}