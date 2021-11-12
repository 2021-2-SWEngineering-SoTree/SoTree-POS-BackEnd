package sogong.restaurant.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import sogong.restaurant.domain.*;
import sogong.restaurant.repository.*;
import sogong.restaurant.service.LoginService;
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

    @RequestMapping("/login")
    public String login(@RequestBody HashMap<String, String>map){

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

        if(!passwordEncoder.matches(pw,user.getPassword())) {
            return null;
        }
            else {
            String token = jwtTokenProvider.createToken(user.getLoginId(), user.getRoles());
            //System.out.println("token = " + token);
            return token;

        }
    }

    @PostMapping("/addManager")
    public Long addManager(@RequestBody Map<String, String> manager){

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

        return managerRepository.save(manager1).getId();
    }

    @RequestMapping("/findBranchName")
    public Long findBranchName(@RequestBody String storeName){
        Long ret = -1l;
        Optional<Manager> manager= managerRepository.findByStoreName(storeName);
        if(manager.isPresent()){
            ret = manager.get().getId();
        }
        return ret;
    }

    @RequestMapping("/addUser")
    public Long addUser(@RequestBody Map<String, String> user){

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
                .password(passwordEncoder.encode(user.get("password")))
                .roles(Collections.singletonList("ROLE_USER")).build();

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
    public boolean validName(@RequestBody String loginId){
        if(userRepository.findByLoginId(loginId).isPresent()) return false;
        else return true;

    }

    @RequestMapping("/getAllPersonName")
    public List<String> findAllPersonName(){
        //요청 파라미터 없음
        List<User> all = userRepository.findAll();
        List<String> ret=new ArrayList<>();
        for(int i=0;i<all.size();i++){
            ret.add(all.get(i).getPersonName());
        }

        return ret;
    }

    @RequestMapping("/requireCreate")
    public boolean requireCreate(@RequestBody Long managerId){
        Optional<Manager> manager = managerRepository.findById(managerId);
        if(!manager.isPresent()) throw new NoSuchElementException();

        List<Menu> menus = menuRepository.findAllById(managerId);
        List<Stock> stocks = stockRepository.findAllById(managerId);

        if(menus.isEmpty() || stocks.isEmpty()) return true;
        else return false;

    }

}
