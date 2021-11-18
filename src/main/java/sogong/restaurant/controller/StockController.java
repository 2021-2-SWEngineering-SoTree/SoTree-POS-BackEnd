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
import sogong.restaurant.service.ManagerService;
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
    private final ManagerService managerService;
    @Autowired
    private final EmployeeRepository employeeRepository;


    // 관리자가 재고 종류를 추가하는 경우
    @PostMapping("/add")
    public String addStock(@RequestBody StockVO stockvo) {

        Stock stock = new Stock();

        Manager manager = managerService.getOneManager(stockvo.getManagerId())
                .orElseThrow(() -> new NoSuchElementException("해당 재고가 존재하지 않습니다."));

        stock.setStockName(stockvo.getStockName());
        stock.setQuantity(stockvo.getQuantity());
        stock.setManager(manager);
        stockService.saveStock(stock);


        for (StockDetail stockDetail : stockvo.getStockDetailList()) {
            System.out.println(stockDetail);
            //Employee employee = stockDetail.getEmployee();
            Employee employee = employeeRepository.findById(stockvo.getEmployeeId())
                    .orElseThrow(() -> new NoSuchElementException("해당 직원이 없습니다."));
            System.out.println("직원 : "+ employee.getId());
            stockDetail.setStock(stock);
            stockDetail.setEmployee(employee);
            stockDetailService.addStockDetail(stockDetail);
        }

        return "OK";
    }

    @PutMapping("/{id}")
    public String updateStock(@RequestBody StockVO stockvo) {
        Stock stock = stockService.getOneStock(managerService.getOneManager(stockvo.getManagerId()).orElseThrow(() -> new NoSuchElementException("해당 지점이 없습니다.")),
                        stockvo.getStockName())
                .orElseThrow(() -> new NoSuchElementException("해당 재고가 없습니다."));

        stock.setStockName(stockvo.getStockName());
        stock.setQuantity(stockvo.getQuantity());

        // 이름 변경 안 됐을 때, 중복검증 없음
        if (stockvo.getStockName().equals(stock.getStockName())) {
            stockService.updateStockWithoutStockName(stock);
        } else {
            stockService.saveStock(stock);
        }

        return "redirect:/";
    }

    //
    @DeleteMapping("/{id}")
    public String deleteStock(@RequestBody StockVO stockvo) {

        // StockDetail 먼저 삭제
        for (StockDetail stockDetail : stockvo.getStockDetailList()) {
            stockDetailService.deleteStockDetail(stockDetail.getId());
        }
        Stock stock = stockService.getOneStock(managerService.getOneManager(stockvo.getManagerId()).orElseThrow(() -> new NoSuchElementException("해당 지점이 없습니다.")),
                        stockvo.getStockName())
                .orElseThrow(() -> new NoSuchElementException("해당 재고가 없습니다."));
        stockService.deleteStock(stock.getId());

        return "redirect:/";
    }

    @PostMapping("/getAll")
    public List<Stock> getAllStock(@RequestBody String managerId) {
        //managerId 숫자만 body에 넣어서 요청하면 된다.

        Manager manager = managerService.getOneManager(Long.parseLong(managerId))
                .orElseThrow(() -> new NoSuchElementException("해당 지점이 없습니다."));
        return stockService.getAllStock(manager);
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

    // manager 필요해서 파라미터 stockVO로 수정함
    @PostMapping("/getByName")
    public List<Map<String, String>> getByName(@RequestBody StockVO stockVO) {

        Stock stock = stockService.getOneStock(managerService.getOneManager(stockVO.getManagerId()).orElseThrow(() -> new NoSuchElementException("해당 지점이 없습니다.")),
                        stockVO.getStockName())
                .orElseThrow(() -> new NoSuchElementException("해당 재고가 없습니다."));

        System.out.println("stock.get().getStockName() = " + stock.getStockName());


        List<StockDetail> stockDetails = stockDetailService.getStockDetailByStock(stock);

        List<Map<String, String>> r = new ArrayList<>();

        Map<String, String> params = new HashMap<>();
        params.put("stockName", stock.getStockName());
        params.put("quantity", String.valueOf(stock.getQuantity()));

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

    // stockVO로 stock & stockDetail 넣어주면 거기에 stockdetail 추가
    // addStock 과 달리 새로운 stock을 추가하는게 아니라, 기존 stock에 stockdetail만 추가가
    @PostMapping("/addStockDetail")
    public String addStockDetail(@RequestBody StockVO stockVO) {

        Stock stock = stockService.getOneStock(managerService.getOneManager(stockVO.getManagerId())
                        .orElseThrow(() -> new NoSuchElementException("해당 지점이 없습니다.")), stockVO.getStockName())
                .orElseThrow(() -> new NoSuchElementException("해당 재고가 존재하지 않습니다."));

        for (StockDetail stockDetail : stockVO.getStockDetailList()) {
            Employee employee = stockDetail.getEmployee();
            stockDetail.setStock(stock);
            stockDetail.setEmployee(employee);
            stockDetailService.addStockDetail(stockDetail);
        }
        return "OK!";
    }
}
