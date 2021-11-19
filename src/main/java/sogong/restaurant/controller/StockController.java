package sogong.restaurant.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sogong.restaurant.VO.StockVO;
import sogong.restaurant.VO.StockdetailVO;
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
                .orElseThrow(() -> new NoSuchElementException("해당 지점이 존재하지 않습니다."));

        Optional<Stock> stock1 = stockService.getOneStock(manager, stockvo.getStockName());
        if (stock1.isPresent()) {   // 비활성화된 stock이 있는 경우
            // 활성화된 stock인데 추가하는 것이므로 에러
            if (stock1.get().getActive()) {
                throw new IllegalStateException("이미 존재하는 재고입니다.");
            } else {
                stock = stock1.get();
                stock.setActive(Boolean.TRUE);
            }
            stockService.updateStockWithoutStockName(stock);
        } else { // 기존 stock 없는 경우 stock 처음부터 지정
            stock.setStockName(stockvo.getStockName());
            // stock.setQuantity(stockvo.getQuantity());
            stock.setManager(manager);
            stock.setActive(Boolean.TRUE);
            stockService.saveStock(stock);
        }

        // StockDetail로 재고의 양 등 초기 설정
        for (StockdetailVO stockdetailVO : stockvo.getStockDetailList()) {
            System.out.println(stockdetailVO);
            //Employee employee = stockDetail.getEmployee();
            // 직원이 없을 수도 있음
            Employee employee = employeeRepository.findEmployeeByIdAndManager(stockdetailVO.getEmployeeId(), manager.getId())
                    .orElse(null);
            // System.out.println("직원 : " + employee.getId());

            StockDetail stockDetail = new StockDetail();
            stockDetail.setStock(stock);
            stockDetail.setEmployee(employee);
            stockDetail.setQuantityChanged(stockdetailVO.getQuantityChanged());
            // stockDetail.setFinalQuantity(stockdetailVO.getQuantityChanged()); // 처음 재고 설정이므로 변화 이후 양도 동일함
            stockDetailService.addStockDetail(stock, stockDetail);
        }

        return "OK";
    }

    // 재고 수정
    // 재고 양 임의 수정 or StockDetail로 수정
    @PutMapping("/update")
    public String updateStock(@RequestBody StockVO stockvo) {
        Manager manager = managerService.getOneManager(stockvo.getManagerId())
                .orElseThrow(() -> new NoSuchElementException("해당 지점이 존재하지 않습니다."));

        Stock stock = stockService.getOneStock(managerService.getOneManager(stockvo.getManagerId()).orElseThrow(() -> new NoSuchElementException("해당 지점이 없습니다.")),
                stockvo.getStockName())
                .orElseThrow(() -> new NoSuchElementException("해당 재고가 없습니다."));

        // 재고 양만 수정 가능(임의로)
        // stock.setQuantity(stockvo.getQuantity());

        for (StockdetailVO stockdetailVO : stockvo.getStockDetailList()) {
            System.out.println(stockdetailVO);
            //Employee employee = stockDetail.getEmployee();
            // 직원이 없을 수도 있음
            Employee employee = employeeRepository.findEmployeeByIdAndManager(stockdetailVO.getEmployeeId(), manager.getId())
                    .orElse(null);
            // System.out.println("직원 : " + employee.getId());

            StockDetail stockDetail = new StockDetail();
            stockDetail.setStock(stock);
            stockDetail.setEmployee(employee);
            stockDetail.setQuantityChanged(stockdetailVO.getQuantityChanged());
            // stockDetail.setFinalQuantity(stockdetailVO.getQuantityChanged()); // 처음 재고 설정이므로 변화 이후 양도 동일함
            stockDetailService.addStockDetail(stock, stockDetail);
        }

        // 이름 변경 안 됐을 때, 중복검증 없음
//        if (stockvo.getStockName().equals(stock.getStockName())) {
//            stockService.updateStockWithoutStockName(stock);
//        } else {
//            stockService.saveStock(stock);
//        }

        return "redirect:/";
    }

    // 재고 종류 삭제
    // stockdetail은 삭제 안함
    // isActive 속성만 변경
    @PutMapping("/delete")
    public String deleteStock(@RequestBody StockVO stockvo) {
        Manager manager = managerService.getOneManager(stockvo.getManagerId())
                .orElseThrow(() -> new NoSuchElementException("해당 지점이 존재하지 않습니다."));

        // StockDetail 먼저 삭제
//        for (StockDetail stockDetail : stockvo.getStockDetailList()) {
//            stockDetailService.deleteStockDetail(stockDetail.getId());
//        }
        Stock stock = stockService.getOneStock(managerService.getOneManager(stockvo.getManagerId()).orElseThrow(() -> new NoSuchElementException("해당 지점이 없습니다.")),
                stockvo.getStockName())
                .orElseThrow(() -> new NoSuchElementException("해당 재고가 없습니다."));

        // 비활성화 & 양 0으로 초기화
        stock.setActive(Boolean.FALSE);
        stock.setQuantity(0);

        stockService.updateStockWithoutStockName(stock);

        return "redirect:/";
    }

    @PostMapping("/getAll")
    public List<Stock> getAllStock(@RequestBody String managerId) {
        //managerId 숫자만 body에 넣어서 요청하면 된다.

        Manager manager = managerService.getOneManager(Long.parseLong(managerId))
                .orElseThrow(() -> new NoSuchElementException("해당 지점이 없습니다."));
        // active인 재고 목록 get
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

//    // stockVO로 stockDetail 넣어주면 거기에 stockdetail 추가
//    // addStock 과 달리 새로운 stock을 추가하는게 아니라, 기존 stock에 stockdetail만 추가가
//    @PostMapping("/addStockDetail")
//    public String addStockDetail(@RequestBody StockVO stockVO) {
//
//        Stock stock = stockService.getOneStock(managerService.getOneManager(stockVO.getManagerId())
//                .orElseThrow(() -> new NoSuchElementException("해당 지점이 없습니다.")), stockVO.getStockName())
//                .orElseThrow(() -> new NoSuchElementException("해당 재고가 존재하지 않습니다."));
//
//        for (StockdetailVO stockdetailVO : stockVO.getStockDetailList()) {
//
//            Employee employee = employeeRepository.findEmployeeByIdAndManager(stockdetailVO.getEmployeeId(), manager.getId())
//                    .orElseThrow(() -> new NoSuchElementException("해당 직원이 없습니다."));
//            System.out.println("직원 : " + employee.getId());
//
//            int currentQuantity = stock.getQuantity(); // 현재 양
//            int quantityChanged = stockdetailVO.getQuantityChanged(); // 변화량
//
//            int finalQuantity = currentQuantity + quantityChanged;   // 변화 이후
//
//
//            StockDetail stockDetail = new StockDetail();
//            stockDetail.setStock(stock);
//            stockDetail.setEmployee(employee);
//            stockDetail.setQuantityChanged(quantityChanged);
//            stockDetail.setFinalQuantity(); // 처음 재고 설정이므로 변화 이후 양도 동일함
//            stockDetailService.addStockDetail(stockDetail);
//        }
//        return "OK!";
//    }
}
