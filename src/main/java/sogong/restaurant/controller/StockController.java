package sogong.restaurant.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sogong.restaurant.VO.StockVO;
import sogong.restaurant.domain.Employee;
import sogong.restaurant.domain.Manager;
import sogong.restaurant.domain.Stock;
import sogong.restaurant.domain.StockDetail;
import sogong.restaurant.repository.EmployeeRepository;
import sogong.restaurant.repository.ManagerRepository;
import sogong.restaurant.service.StockDetailService;
import sogong.restaurant.service.StockService;

import java.util.*;

@AllArgsConstructor
@RestController
@Slf4j
@RequestMapping("/stock")
public class StockController {

    @Autowired
    private final StockService stockService;
    @Autowired
    private final StockDetailService stockDetailService;
    @Autowired
    private final ManagerRepository managerRepository;
    @Autowired
    private final EmployeeRepository employeeRepository;


    // 관리자가 재고 종류를 추가하는 경우
    @PostMapping("/add")
    public String addStock(@RequestBody StockVO stockvo) {

        Stock stock = new Stock();

        Manager manager = managerRepository.findById(stockvo.getManagerId())
                .orElseThrow(() -> new NoSuchElementException());

        stock.setStockName(stockvo.getStockName());
        stock.setQuantity(stockvo.getQuantity());
        stock.setManager(manager);
        stockService.saveStock(stock);

        System.out.println(stockvo.getEmployeeId());
        Employee employee = employeeRepository.findById(stockvo.getEmployeeId())
                .orElseThrow(() ->
                        new NoSuchElementException("해당 메뉴가 존재하지 않습니다."));

        for (StockDetail stockDetail : stockvo.getStockDetailList()) {
            stockDetail.setStock(stock);
            stockDetail.setEmployee(employee);
            stockDetailService.addStockDetail(stockDetail);
        }

        return "OK";
    }

    @PutMapping("/{id}")
    public String updateStock(@RequestBody StockVO stockvo) {
        Stock stock = stockService.getOneStock(stockvo.getStockName()).get();

        stock.setStockName(stockvo.getStockName());
        stock.setQuantity(stockvo.getQuantity());

        stockService.saveStock(stock);

        return "redirect:/";
    }

    //
    @DeleteMapping("/{id}")
    public String deleteStock(@RequestBody StockVO stockvo) {

        // StockDetail 먼저 삭제
        for (StockDetail stockDetail : stockvo.getStockDetailList()) {
            stockDetailService.deleteStockDetail(stockDetail.getId());
        }
        Stock stock = stockService.getOneStock(stockvo.getStockName()).get();
        stockService.deleteStock(stock.getId());

        return "redirect:/";
    }

    @PostMapping("/getAll")
    public List<Stock> getAllStock(@RequestBody String managerId) {
        //managerId 숫자만 body에 넣어서 요청하면 된다.

        Optional<Manager> manager = managerRepository.findById(Long.parseLong(managerId));
        return stockService.getAllStock(manager.get());
    }

//    @PostMapping("/isPresent")
//    public boolean validName(@RequestBody Map<String, String> param) {
//
//        String stockName = param.get("stockName");
//        Long managerId = Long.parseLong(param.get("managerId"));
//
//        System.out.println("stockName = " + stockName);
//
//        Optional<Stock> stock = stockService.getOneStock(stockName);
//
//        //이름이 이미 존재하면 false 값을 리턴한다.
//        if (stock.isEmpty()) {
//            return true;
//        } else {
//            System.out.println("stock = " + stock.get().getStockName());
//            return false;
//        }
//    }

    @PostMapping("/getByName")
    public List<Map<String, String>> getByName(@RequestBody String stockName) {

        Optional<Stock> stock = stockService.getOneStock(stockName);

        System.out.println("stock.get().getStockName() = " + stock.get().getStockName());


        List<StockDetail> stockDetails = stockDetailService.getStockDetailByStock(stock.get());

        List<Map<String, String>> r = new ArrayList<>();

        Map<String, String> params = new HashMap<>();
        params.put("stockName", stock.get().getStockName());
        params.put("quantity", String.valueOf(stock.get().getQuantity()));

        r.add(params);

        for (StockDetail stockDetail : stockDetails) {
            Map<String, String> param = new HashMap<>();
            param.put("id", String.valueOf(stockDetail.getId()));
            param.put("quantityChanged", String.valueOf(stockDetail.getQuantityChanged()));
            param.put("time", String.valueOf(stockDetail.getTime()));

            r.add(param);
        }

        return r;
    }
}
