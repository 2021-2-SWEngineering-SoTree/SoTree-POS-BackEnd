package sogong.restaurant.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import sogong.restaurant.domain.Stock;
import sogong.restaurant.domain.StockDetail;
import sogong.restaurant.service.StockDetailService;
import sogong.restaurant.service.StockService;
import sogong.restaurant.VO.StockVO;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/stock")
public class StockController {

    private final StockService stockService;
    private StockDetailService stockDetailService;

    @Autowired
    public StockController(StockService stockService, StockDetailService stockDetailService){
        this.stockService = stockService;
        this.stockDetailService = stockDetailService;
    }


    @PostMapping("/add")
    public String addStock(@RequestBody StockVO stockvo){

        Stock stock = new Stock();

        stock.setStockName(stockvo.getStockName());
        stock.setQuantity(stockvo.getQuantity());
        stock.setBranchId(stockvo.getBranchId());
        stockService.addStock(stock);


        for(StockDetail stockDetail:stockvo.getStockDetailList()){
            stockDetail.setStock(stock);
            stockDetailService.addStockDetail(stockDetail);
        }

        return "OK";
    }


    @PutMapping("/{id}")
    public String updateStock(@RequestBody StockVO stockvo){
        Stock stock = stockService.getOneStock(stockvo.getStockName()).get();

        stock.setStockName(stockvo.getStockName());
        stock.setQuantity(stockvo.getQuantity());
        stock.setBranchId(stockvo.getBranchId());

        stockService.saveStock(stock);

        return "redirect:/";
    }

    @DeleteMapping("/{id}")
    public String delete(@RequestBody StockVO stockvo){

        // MenuIngredient 먼저 삭제
        for(StockDetail stockDetail:stockvo.getStockDetailList()){
            stockDetailService.deleteStockDetail(stockDetail.getId());
        }
        Stock stock = stockService.getOneStock(stockvo.getStockName()).get();
        stockService.deleteStock(stock.getId());

        return "redirect:/";
    }

    @PostMapping("/getAll")
    public List<Stock> getAllStock(){
        return stockService.getAllStock();
    }

}
