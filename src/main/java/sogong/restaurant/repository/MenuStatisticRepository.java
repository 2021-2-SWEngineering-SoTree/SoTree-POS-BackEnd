package sogong.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sogong.restaurant.domain.Menu;
import sogong.restaurant.domain.MenuOrder;
import sogong.restaurant.domain.MenuStatistic;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuStatisticRepository extends JpaRepository<MenuStatistic,Long> {

    @Query(value = "select MenuStatisticId,date,quantity,BranchId,MenuId from MenuStatistic where BranchId = :bid and MenuId= :mid and date = :st", nativeQuery = true)
    public Optional<MenuStatistic>findByMenuIdAndBranchIdAndDate(@Param(value = "bid")Long bid,@Param(value="mid")Long mid, @Param("st") String stdate);
    @Query(value = "select MenuStatisticId,date,quantity,BranchId,MenuId from MenuStatistic where BranchId = :bid and date between :st and :en", nativeQuery = true)
    public List<MenuStatistic>findByMenuIdAndBranchIdAndDateBetween(@Param(value = "bid")Long bid, @Param("st") String stdate,@Param("en") String enDate);

    @Query(value = "select ms.MenuStatisticId,ms.date,ms.quantity,ms.BranchId,ms.MenuId from MenuStatistic ms, Menu m where ms.BranchId = :bid and m.MenuId=ms.MenuId and m.menuCategory=:mc and ms.date between :st and :en", nativeQuery = true)
    public List<MenuStatistic>findByMenuIdAndBranchIdAndMenuCategoryAndDateBetween(@Param(value = "bid")Long bid, @Param("st") String stdate,@Param("en") String enDate,@Param("mc") String menuCategory);

    @Query(value = "select MenuStatisticId,date,quantity,BranchId,MenuId from MenuStatistic where BranchId = :bid and DAYOFWEEK(date)=:d", nativeQuery = true)
    public List<MenuStatistic>findByBranchIdAndDay(@Param(value = "bid")Long bid,@Param(value = "d")Long day);

    @Query(value = "select ms.MenuStatisticId,ms.date,ms.quantity,ms.BranchId,ms.MenuId from MenuStatistic ms, Menu m where ms.BranchId = :bid and m.MenuId=ms.MenuId and m.menuCategory=:cat and DAYOFWEEK(ms.date)=:d", nativeQuery = true)
    public List<MenuStatistic>findByBranchIdAndCategoryAndDay(@Param(value = "bid")Long bid,@Param(value = "d")Long day,@Param("cat") String category);

    @Query(value = "select ms.MenuId,sum(ms.quantity) as quantity, count(ms.quantity) as totalCount ,m.MenuName as MenuName from pos.MenuStatistic ms, pos.Menu m" +
            " where ms.BranchId =:bid and m.MenuId=ms.MenuId and DATE(ms.date) between CURDATE()-7 AND CURDATE()  group by MenuId  order by quantity desc limit 5", nativeQuery = true)
    public List<MenuStatisticWeeklySummary>findByBranchAndMenuONRecentWeekly(@Param(value="bid")Long ranchId);

}
