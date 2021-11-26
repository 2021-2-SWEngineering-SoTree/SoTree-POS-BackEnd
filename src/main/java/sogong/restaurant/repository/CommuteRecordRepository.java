package sogong.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sogong.restaurant.domain.CommuteRecord;
import sogong.restaurant.domain.Employee;
import sogong.restaurant.domain.Manager;

import javax.print.attribute.standard.MediaName;
import java.util.Date;
import java.util.List;

@Repository
public interface CommuteRecordRepository extends JpaRepository<CommuteRecord, Long> {

    @Query(value = "select CommuteRecordId,isComing,time,EmployeeId,ManagerId from CommuteRecord where ManagerId= :bid and isComing = :isCome and time between :st and :en", nativeQuery = true)
    List<CommuteRecord> findAllByEmployeeIdBetweenTime(@Param("bid") Long bid, @Param("st") String stdate, @Param("en") String endate, @Param("isCome") Boolean isCome);


    List<CommuteRecord> findAllByEmployeeId(Employee employee);
    @Query(value = "select CommuteRecordId,isComing,time,EmployeeId,ManagerId from CommuteRecord where ManagerId = :bid and time between :st and :en", nativeQuery = true)
    List<CommuteRecord> findAllByManagerAndTimeBetween(@Param(value = "bid")Long bid, @Param("st") String stdate, @Param("en") String endate);

    List<CommuteRecord> findAllByManager(Manager manager);

}
