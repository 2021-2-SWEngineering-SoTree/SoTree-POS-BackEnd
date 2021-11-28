package sogong.restaurant.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import sogong.restaurant.domain.*;
import sogong.restaurant.repository.*;
import sogong.restaurant.service.LoginService;
import sogong.restaurant.service.MenuService;
import sogong.restaurant.util.GenerateAlphaNumericString;
import sogong.restaurant.util.JwtTokenProvider;

import java.util.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    private LoginService loginService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final ManagerRepository managerRepository;
    private final EmployeeRepository employeeRepository;
    private final StockRepository stockRepository;
    private final MenuRepository menuRepository;
    private final MenuService menuService;
    //private final UserService userService;

    @RequestMapping("/login")
    public String login(@RequestBody HashMap<String, String> map) {

        /*
        {
            "id":"아이디",
            "pw":"비밀번호"
        }
        형식
         */

        String id = map.get("id");
        String pw = map.get("pw");

        User user = userRepository.findByLoginId(id)
                .orElseThrow(() -> new NoSuchElementException());

        if (!passwordEncoder.matches(pw, user.getPassword())) {
            return "wrong password";
        } else {
            String token = jwtTokenProvider.createToken(user.getLoginId(), user.getRoles());
            //System.out.println("token = " + token);
            return token;

        }
    }

    @PostMapping("/addManager")
    public Long addManager(@RequestBody Map<String, String> manager) {

        /*
        {
    "loginId":"admin",
    "birthDay":"2017-12-21 11:30",
    "userName":"박서진",
    "email":"mina881@naver.com",
    "phoneNumber":"010-1234-1234",
    "password":"1234",
    "storeName":"강식당",
    "branchPhoneNumber":"02-123-1234"
}
         */

        User user1 = User.builder()
                .loginId(manager.get("loginId"))
                .birthDay(manager.get("birthDay")).userName(manager.get("userName"))
                .email(manager.get("email")).phoneNumber(manager.get("phoneNumber"))
                .password(passwordEncoder.encode(manager.get("password")))
                .roles(Collections.singletonList("ROLE_ADMIN")).build();

        userRepository.save(user1);

        Manager manager1 = new Manager();
        manager1.setUser(user1);
        manager1.setStoreName(manager.get("storeName"));
        manager1.setBranchPhoneNumber(manager.get("branchPhoneNumber"));
        manager1.setSeatCnt(16);

        managerRepository.save(manager1);

        // default 메뉴인 기타 메뉴 추가
        Menu menu = new Menu();
        menu.setMenuName("기타");
        menu.setMenuCategory(null);
        menu.setPrice(1);
        menu.setManager(manager1);
        menu.setActive(Boolean.TRUE);
        menuService.saveMenu(menu);

        return manager1.getId();
    }

    @RequestMapping("/findBranchName")
    public Long findBranchName(@RequestBody String storeName) {
        Long ret = -1l;
        System.out.println(storeName);
        Optional<Manager> manager = managerRepository.findByStoreName(storeName);
        if (manager.isPresent()) {
            ret = manager.get().getId();
        }
        return ret;
    }

    @RequestMapping("/addUser")
    public Long addUser(@RequestBody Map<String, String> user) {

        /*
        {
    "loginId":"werad15",
    "birthDay":"2017-12-21 11:30",
    "userName":"박서진",
    "email":"mina881@naver.com",
    "phoneNumber":"010-1234-1234",
    "password":"1234",
    "managerId":"1"
}
         */

        User user1 = User.builder()
                .loginId(user.get("loginId"))
                .birthDay(user.get("birthDay")).userName(user.get("userName"))
                .email(user.get("email")).phoneNumber(user.get("phoneNumber"))
                .password(passwordEncoder.encode(user.get("password"))).build();
        //user는 생성할때 권한을 더이상 가지지 않는다.
        //.roles(Collections.singletonList("ROLE_USER")).build();

        System.out.println("user1.getUsername() = " + user1.getUsername());

        Long retId = userRepository.save(user1).getId();

        Long managerId = Long.parseLong(user.get("managerId"));
        Optional<Manager> manager = managerRepository.findById(managerId);

        Employee employee = new Employee();
        employee.setManager(manager.get());
        employee.setUser(user1);
        employee.setCommuteState(false);

        employeeRepository.save(employee);

        return retId;
    }

    @RequestMapping("/userIdPresent")
    public boolean validName(@RequestBody String loginId) {
        if (userRepository.findByLoginId(loginId).isPresent()) {
            return false;
        } else {
            return true;
        }

    }

    @RequestMapping("/getAllPersonName")
    public List<Map<String,String>> findAllPersonName(@RequestBody String branchId){
        //요청 파라미터 없음
        //List<User> all = userRepository.findAll();
        Optional<Manager> managers = managerRepository.findById(Long.valueOf(branchId));
        if(managers.isEmpty()) throw new NoSuchElementException("존재하지 않는 가게입니다.");

        Manager manager = managers.get();

        List<Employee> employees = employeeRepository.findEmployeesByManager(manager);
        List<Map<String,String>> ret=new ArrayList<>();
            Map<String,String> one = new HashMap<>();
            one.put("ManagerId",String.valueOf(manager.getId()));
            one.put("personName",manager.getUser().getPersonName());
            ret.add(one);


        for(int i=0;i<employees.size();i++){
            Map<String,String> ones = new HashMap<>();
            ones.put("EmployeeId",String.valueOf(employees.get(i).getId()));
            ones.put("personName",employees.get(i).getUser().getPersonName());
            ret.add(ones);
        }

        return ret;
    }

    @RequestMapping("/requireCreate")
    public boolean requireCreate(@RequestBody Long managerId) {
        Optional<Manager> manager = managerRepository.findById(managerId);
        if (!manager.isPresent()) {
            throw new NoSuchElementException();
        }

        List<Menu> menus = menuRepository.findAllById(managerId);
        List<Stock> stocks = stockRepository.findAllById(managerId);

        if (menus.isEmpty() || stocks.isEmpty()) {
            return true;
        } else {
            return false;
        }

    }

    @PutMapping("/updateUser")
    public String updateUser(@RequestBody Map<String, String> param) {

        //Long userId = Long.parseLong(param.get("userId"));
        String birthDay = param.get("birthDay");
        String email = param.get("email");
        String loginId = param.get("loginId");
        //String password = param.get("password");
        String phoneNumber = param.get("phoneNumber");
        String userName = param.get("userName");

        String storeName = param.get("storeName");
        String branchPhoneNumber = param.get("branchPhoneNumber");

        Optional<User> user = userRepository.findByLoginId(loginId);
        if (user.isEmpty()) {
            throw new NoSuchElementException("존재하지 않는 유저입니다.");
        }

        User user1 = user.get();

        user1.setLoginId(loginId);
        user1.setUserName(userName);
        user1.setPhoneNumber(phoneNumber);
        //user1.setPassword(passwordEncoder.encode(password));
        user1.setEmail(email);
        user1.setBirthDay(birthDay);

        userRepository.save(user1);

        Manager manager = managerRepository.findByUser(user1).get();

        manager.setStoreName(storeName);
        manager.setBranchPhoneNumber(branchPhoneNumber);
        manager.setId(manager.getId());
        manager.setUser(user1);
        manager.setSeatCnt(manager.getSeatCnt());

        managerRepository.save(manager);

        return "OK";
    }

    @PutMapping("/updateUserPw")
    public String updateUserPw(@RequestBody Map<String, String> param) {
        String prevPw = param.get("prevPw");
        String changePw = param.get("changePw");
        String loginId = param.get("loginId");

        Optional<User> optionalUser = userRepository.findByLoginId(loginId);
        User user = optionalUser.get();

        if (!passwordEncoder.matches(prevPw, user.getPassword())) {
            throw new IllegalStateException("비밀번호가 틀립니다.");
        }

        user.setPassword(passwordEncoder.encode(changePw));
        userRepository.save(user);
        return "OK";
    }

    @PostMapping("/getUserByLoginId")
    public Manager getUserByLoginId(@RequestBody String loginId){
        Optional<User> optionalUser = userRepository.findByLoginId(loginId);

        if(optionalUser.isEmpty()) throw new NoSuchElementException("존재하지 않는 매니저입니다.");

        return managerRepository.findByUser(optionalUser.get()).get();
    }

    @PostMapping("/findLoginId")
    public String findLoginId(@RequestBody Map<String, String> param) {

        String userName = param.get("userName");
        String birthDay = param.get("birthDay");
        String phoneNumber = param.get("phoneNumber");
        String email = param.get("email");

        Optional<User> user = userRepository.findByUserNameAndBirthDayAndPhoneNumberAndEmail(userName, birthDay, phoneNumber, email);
        if (user.isEmpty()) {
            throw new NoSuchElementException("존재하지 않는 유저입니다.");
        } else {
            return user.get().getLoginId();
        }

    }

    @PostMapping("/findUserPw")
    public String findUserPw(@RequestBody Map<String, String> param) {
        String userName = param.get("userName");
        String loginId = param.get("loginId");
        String phoneNumber = param.get("phoneNumber");
        String email = param.get("email");

        Optional<User> user = userRepository.findByUserNameAndLoginIdAndPhoneNumberAndEmail(userName, loginId, phoneNumber, email);
        if (user.isEmpty()) {
            throw new NoSuchElementException("존재하지 않는 유저입니다.");
        } else {
            User user1 = user.get();
            String newPw = GenerateAlphaNumericString.getRandomString(10);

            user1.setPassword(passwordEncoder.encode(newPw));
            userRepository.save(user1);
            return newPw;
        }
    }

    @PostMapping("/getAllUser")
    public List<Map<String, String>> getAllUser(@RequestBody Long branchId) {
        List<Map<String, String>> ret = new ArrayList<>();

        Optional<Manager> manager = managerRepository.findById(branchId);
        if (manager.isEmpty()) {
            throw new NoSuchElementException("존재하지 않는 가게입니다.");
        }

        Map<String, String> man = new HashMap<>();
        man.put("loginId", manager.get().getUser().getLoginId());
        man.put("userName", manager.get().getUser().getUsername());
        man.put("personName", manager.get().getUser().getPersonName());
        man.put("birthDay", manager.get().getUser().getBirthDay());
        man.put("email", manager.get().getUser().getEmail());
        man.put("phoneNumber", manager.get().getUser().getPhoneNumber());
        man.put("position", "manager");

        ret.add(man);

        for (Employee employee : employeeRepository.findEmployeesByManager(manager.get())) {
            Map<String, String> one = new HashMap<>();

            one.put("loginId", employee.getUser().getLoginId());
            one.put("userName", employee.getUser().getUsername());
            one.put("personName", employee.getUser().getPersonName());
            one.put("birthDay", employee.getUser().getBirthDay());
            one.put("email", employee.getUser().getEmail());
            one.put("phoneNumber", employee.getUser().getPhoneNumber());
            one.put("employeeId", String.valueOf(employee.getId()));

            if (employee.getUser().getAuthorities().isEmpty()) {
                one.put("position", "not_granted_employee");
            } else {
                one.put("position", "employee");
            }

            ret.add(one);
        }

        return ret;

    }

    @PostMapping("/allowEmployee")
    public String allowEmployee(@RequestBody Map<String, String> param) {
        Long branchId = Long.parseLong(param.get("branchId"));
        Long employeeId = Long.parseLong(param.get("employeeId"));
        String workSchedule = param.get("workSchedule");
        Optional<Employee> employeeByIdAndManager = employeeRepository.findEmployeeByIdAndManager(employeeId, branchId);
        if (employeeByIdAndManager.isEmpty()) {
            throw new NoSuchElementException("존재하지 않는 직원 or 가게입니다.");
        }

        Employee employee = employeeByIdAndManager.get();

        User user = employee.getUser();

        User addUser = new User();
        Employee addEmployee = new Employee();

        addUser.setRoles(Collections.singletonList("ROLE_USER"));
        addUser.setUserName(user.getPersonName());
        addUser.setLoginId(user.getLoginId());
        addUser.setPassword(user.getPassword());
        //asdfas
        addUser.setEmail(user.getEmail());
        addUser.setBirthDay(user.getBirthDay());
        addUser.setId(user.getId());
        addUser.setPhoneNumber(user.getPhoneNumber());

        userRepository.save(addUser);

        addEmployee.setUser(addUser);
        addEmployee.setManager(managerRepository.findById(branchId).get());
        addEmployee.setId(employeeId);
        addEmployee.setCommuteState(false);
        addEmployee.setWorkSchedule(workSchedule);

        employeeRepository.save(addEmployee);

        return "OK";
    }

    @PostMapping("/updateSeatCnt")
    String updateSeatCnt(@RequestBody Map<String, String> param) {
        Long branchId = Long.parseLong(param.get("branchId"));
        int newSeatCnt = Integer.parseInt(param.get("seatCnt"));

        Optional<Manager> optionalManager = managerRepository.findById(branchId);
        if (optionalManager.isEmpty()) {
            throw new NoSuchElementException("존재하지 않는 가게입니다.");
        }

        Manager manager = optionalManager.get();

        manager.setSeatCnt(newSeatCnt);
        manager.setUser(manager.getUser());
        manager.setId(manager.getId());
        manager.setBranchPhoneNumber(manager.getBranchPhoneNumber());
        manager.setStoreName(manager.getStoreName());

        managerRepository.save(manager);

        return "OK";
    }

    @PostMapping("/getComingEmployee")
    public List<Map<String,String>> getComingEmployee(@RequestBody String branchId){

        Long bId = Long.parseLong(branchId);

        Optional<Manager> optionalManager = managerRepository.findById(bId);
        if (optionalManager.isEmpty()) {
            throw new NoSuchElementException("존재하지 않는 가게입니다.");
        }

        Manager manager = optionalManager.get();
        List<Employee> all = employeeRepository.findAllByManagerAndCommuteState(manager, true);
        List<Map<String,String>> ret=new ArrayList<>();

        Map<String,String> man = new HashMap<>();

        man.put("personName",manager.getUser().getPersonName());
        man.put("ManagerId",String.valueOf(manager.getId()));
        man.put("level","사장");

        ret.add(man);

        for(Employee e : all){
            Map<String,String> one = new HashMap<>();

            one.put("personName",e.getUser().getPersonName());
            one.put("EmployeeId",String.valueOf(e.getId()));
            one.put("level","직원");

            ret.add(one);
        }
        return ret;
    }

}
