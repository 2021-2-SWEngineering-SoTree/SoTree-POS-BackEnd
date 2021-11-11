package sogong.restaurant.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sogong.restaurant.domain.*;

@SpringBootTest
class TableOrderRepositoryTest {
    @Autowired private UserRepository userRepository;
    @Autowired private ManagerRepository managerRepository;
    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private TableOrderRepository tableOrderRepository;
    @Autowired private MenuRepository menuRepository;
    @Autowired private MenuIngredientRepository menuIngredientRepository;
    @Autowired private OrderDetailRepository orderDetailRepository;

    @BeforeEach
    public void makeManagerAndEmployeeAndMenu(){

        User user = new User();
        user.setUserName("박서진");
        user.setEmail("mina881@naver.com");
        user.setBirthDay("1998-01-03 13:30");
        user.setPassword("1234");
        user.setLoginId("testAdmin");
        user.setPhoneNumber("010-9283-9712");
        userRepository.save(user);
        Manager manager = new Manager();
        manager.setUser(user);
        manager.setStoreName("테스트가게");
        manager.setBranchPhoneNumber("02-123-1234");
        managerRepository.save(manager);

        User user1 = new User();
        user1.setUserName("직원임");
        user1.setEmail("aa@naver.com");
        user1.setBirthDay("1998-01-02 13:12");
        user1.setPassword("1234");
        user1.setLoginId("testEmployee");
        user1.setPhoneNumber("010-1234-1234");
        userRepository.save(user1);

        Employee employee = new Employee();
        employee.setManager(manager);
        employee.setCommuteState(false);
        employee.setUser(user1);

        employeeRepository.save(employee);

        Menu menu= new Menu();

        menu.setMenuName("된장찌개");
        menu.setPrice(12000);
        menu.setMenuCategory("식사");
        menu.setManager(managerRepository.findByStoreName("테스트가게").get());


        menuRepository.save(menu);

        Menu insertMenu = menuRepository.findMenuByMenuName("된장찌개").get();

        MenuIngredient menuIngredient = new MenuIngredient();
        menuIngredient.setMenu(insertMenu);
        menuIngredient.setIngredientName("된장");
        menuIngredient.setCount(12);

        menuIngredientRepository.save(menuIngredient);

    }

    @Test
    void testMakingTableOrder(){

        Manager manager = managerRepository.findByStoreName("테스트가게").get();
        User user = userRepository.findByLoginId("testEmployee").get();
        Employee employee = employeeRepository.findEmployeeByUser(user).get();

        MenuOrder order = new MenuOrder();
        order.setManager(manager);
        order.setEmployee(employee);
        order.setOrderDate("2021-11-11");
        order.setStartTime("2021-11-11 12:20");
        order.setEndTime("2021-11-11 14:20");
        order.setTotalPrice(24000);
        orderRepository.save(order);

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setMenuOrder(order);
        orderDetail.setQuantity(2);
        orderDetail.setMenu(menuRepository.findMenuByMenuName("된장찌개").get());

        orderDetailRepository.save(orderDetail);

        TableOrder tableOrder = new TableOrder();
        tableOrder.setSeatNumber(1);
        tableOrder.setMenuOrder(order);
        tableOrderRepository.save(tableOrder);

    }


}