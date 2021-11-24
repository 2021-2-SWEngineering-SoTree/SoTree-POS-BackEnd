package sogong.restaurant.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sogong.restaurant.domain.CommuteRecord;
import sogong.restaurant.domain.Employee;
import sogong.restaurant.domain.Manager;
import sogong.restaurant.repository.CommuteRecordRepository;
import sogong.restaurant.repository.EmployeeRepository;
import sogong.restaurant.repository.ManagerRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class CommuteService {
    @Autowired
    private final EmployeeRepository employeeRepository;
    @Autowired
    private final CommuteRecordRepository commuteRecordRepository;
    @Autowired
    private final ManagerRepository managerRepository;

    public String recordCome(Long employeeId, String comingTime, Long branchId) {

        Optional<Manager> managerOptional = managerRepository.findById(branchId);

        if (managerOptional.isEmpty()) {
            throw new NoSuchElementException("존재하지 않는 가게입니다.");
        }

        Manager manager = managerOptional.get();

        Optional<Employee> byId = employeeRepository.findEmployeeByIdAndManager(employeeId, branchId);

        if (byId.isEmpty()) {
            throw new NoSuchElementException("존재하지 않는 직원입니다.");
        }

        Employee employee = byId.get();

        if (employee.isCommuteState()) {
            throw new IllegalStateException("현재 출근상태입니다.");
        }


        employee.setCommuteState(true);
        CommuteRecord commuteRecord = new CommuteRecord();
        commuteRecord.setEmployee(employee);
        commuteRecord.setIsComing(true);
        commuteRecord.setTime(comingTime);
        commuteRecord.setManager(manager);

        commuteRecordRepository.save(commuteRecord);

        return comingTime;

    }

    public String recordOut(Long employeeId, String comingTime, Long branchId) {

        Optional<Manager> managerOptional = managerRepository.findById(branchId);

        if (managerOptional.isEmpty()) {
            throw new NoSuchElementException("존재하지 않는 가게입니다.");
        }

        Manager manager = managerOptional.get();

        Optional<Employee> byId = employeeRepository.findEmployeeByIdAndManager(employeeId, branchId);

        if (byId.isEmpty()) {
            throw new NoSuchElementException("존재하지 않는 직원입니다.");
        }

        Employee employee = byId.get();

        if (!employee.isCommuteState()) {
            throw new IllegalStateException("현재 퇴근상태입니다.");
        }

        employee.setCommuteState(false);
        CommuteRecord commuteRecord = new CommuteRecord();
        commuteRecord.setEmployee(employee);
        commuteRecord.setIsComing(false);
        commuteRecord.setTime(comingTime);
        commuteRecord.setManager(manager);

        commuteRecordRepository.save(commuteRecord);

        return comingTime;

    }

    public List<CommuteRecord> getOneEmployeeRecords(Long employeeId, Long branchId) {
        Employee employee = employeeRepository.findEmployeeByIdAndManager(employeeId, branchId)
                .orElseThrow(() -> new NoSuchElementException("해당 직원이 존재하지 않습니다."));

        return commuteRecordRepository.findAllByEmployeeId(employee);
    }

}
