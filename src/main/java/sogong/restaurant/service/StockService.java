package sogong.restaurant.service;

import org.springframework.beans.factory.annotation.Autowired;
import sogong.restaurant.domain.Menu;
import sogong.restaurant.domain.Stock;
import sogong.restaurant.repository.StockRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
public class StockService {

    private StockRepository stockRepository;

    @Autowired
    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Transactional
    public Long saveStock(Stock stock) {       return stockRepository.save(stock).getId();}

    @Transactional
    public Long addStock(Stock stock){
        validateDuplicateStock(stock);
        stockRepository.save(stock);
        return stock.getId();
    }
    
    // 이름 중복되는 재고 방지
    private void validateDuplicateStock(Stock stock){
        stockRepository.findStockByStockName(stock.getStockName())
                .ifPresent(s -> {
            throw new IllegalStateException("이미 존재하는 재고입니다.");
        });
    }

    public List<Stock> getAllStock(){
        return stockRepository.findAll();
    }

    public Optional<Stock> getOneStock(String stockName) {
        return stockRepository.findStockByStockName(stockName);
    }

    @Transactional
    public void deleteStock(Long id) {
        stockRepository.deleteById(id);
    }
}
