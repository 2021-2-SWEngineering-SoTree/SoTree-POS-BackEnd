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
import java.text.SimpleDateFormat;
import java.util.*;

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

    public List<Map<String, String>> getAllCommuteDay(Long branchId, String month) throws Exception {
        List<Map<String, String>> ret = new ArrayList<>();

        Optional<Manager> optionalManager = managerRepository.findById(branchId);
        if (optionalManager.isEmpty()) {
            throw new NoSuchElementException("존재하지 않는 가게입니다.");
        }
        Manager manager = optionalManager.get();
        String startTime = month + "-01";
        String endTime = month + "-32";

        Date st = new SimpleDateFormat("yyyy-MM-dd").parse(startTime);
        Date en = new SimpleDateFormat("yyyy-MM-dd").parse(endTime);

        System.out.println("st = " + st);

        List<CommuteRecord> all = commuteRecordRepository.findAllByManagerAndTimeBetween(branchId, startTime, endTime);

        Map<Long, String> startMap = new HashMap<>();
        Map<Long, String> endMap = new HashMap<>();


        for (CommuteRecord commuteRecord : all) {
            if (startMap.containsKey(commuteRecord.getEmployee().getId()) && commuteRecord.getIsComing().booleanValue() == false) {
                String dateStr = startMap.get(commuteRecord.getEmployee().getId());
                Date comingTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(startMap.get(commuteRecord.getEmployee().getId()));
                Date goingTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(commuteRecord.getTime());

                Map<String, String> one = new HashMap<>();

                one.put("employeeName", commuteRecord.getEmployee().getUser().getPersonName());
                one.put("date", commuteRecord.getTime());

                Date morning = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateStr.substring(0, 10) + " 09:49");
                Date morning2 = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateStr.substring(0, 10) + " 10:11");
                Date mid = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateStr.substring(0, 10) + " 15:49");
                Date mid2 = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateStr.substring(0, 10) + " 16:11");
                Date night = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateStr.substring(0, 10) + " 21:49");
                Date night2 = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateStr.substring(0, 10) + " 22:11");

                if (morning2.after(comingTime) && morning.before(comingTime) && mid2.after(goingTime) && mid.before(goingTime)) {
                    one.put("working", "L");
                } else if (mid2.after(comingTime) && mid.before(comingTime) && night2.after(goingTime) && night.before(goingTime)) {
                    one.put("working", "D");
                } else if (morning2.after(comingTime) && morning.before(comingTime) && night2.after(goingTime) && night.before(goingTime)) {
                    one.put("working", "F");
                } else {
                    one.put("working", "N");
                }


                ret.add(one);
            } else if (commuteRecord.getIsComing().booleanValue()) {
                startMap.put(commuteRecord.getEmployee().getId(), commuteRecord.getTime());
            }
        }

        return ret;
    }

    public Map<String, String> findCommuteByManager(Long branchId) {
        Map<String, String> ret = new HashMap<>();
        Optional<Manager> optionalManager = managerRepository.findById(branchId);
        if (optionalManager.isEmpty()) {
            throw new NoSuchElementException("존재하지 않는 가게입니다.");
        }
        Manager manager = optionalManager.get();

        List<Employee> all = employeeRepository.findEmployeesByManagerAndIsActive(manager, true);

        for (Employee employee : all) {
            if (employee.getWorkSchedule() != null) {
                ret.put(employee.getUser().getPersonName(), employee.getWorkSchedule());
            }
        }

        return ret;
    }

}
