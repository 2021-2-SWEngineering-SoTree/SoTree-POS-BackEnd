package sogong.restaurant.service;

import org.springframework.beans.factory.annotation.Autowired;
import sogong.restaurant.domain.Stock;
import sogong.restaurant.domain.StockDetail;
import sogong.restaurant.repository.StockDetailRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class StockDetailService {

    private StockDetailRepository stockDetailRepository;

    @Autowired
    public StockDetailService(StockDetailRepository stockDetailRepository) {
        this.stockDetailRepository = stockDetailRepository;
    }

    @Transactional
    public Long addStockDetail(StockDetail stockDetail){
        return stockDetailRepository.save(stockDetail).getId();
    }

    @Transactional
    public void deleteStockDetail(Long id) {
        stockDetailRepository.deleteById(id);
    }

    public List<StockDetail> getStockDetailByStock(Stock stock){return stockDetailRepository.findStockDetailByStock(stock);}

}
