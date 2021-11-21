package sogong.restaurant.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sogong.restaurant.domain.Manager;
import sogong.restaurant.domain.Stock;
import sogong.restaurant.repository.StockRepository;
import sogong.restaurant.repository.StockSummary;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
@AllArgsConstructor
public class StockService {

    @Autowired
    private final StockRepository stockRepository;

    @Transactional
    public Long saveStock(Stock stock) {
        validateDuplicateStock(stock);
        return stockRepository.save(stock).getId();
    }

    @Transactional
    public Long updateStockWithoutStockName(Stock stock) {
        return stockRepository.save(stock).getId();
    }

    // 같은 지점에서 재고 이름 중복 방지
    private void validateDuplicateStock(Stock stock) {
        stockRepository.findStockByManagerAndStockName(stock.getManager(), stock.getStockName())
                .ifPresent(s -> {
                    throw new IllegalStateException("이미 존재하는 재고입니다.");
                });
    }

    public List<Stock> getAllStock(Manager manager) {
        return stockRepository.findAllByManagerAndActive(manager, Boolean.TRUE);
    }

    public Optional<Stock> getOneStock(Manager manager, String stockName) {
        return stockRepository.findStockByManagerAndStockName(manager, stockName);
    }

    public List<StockSummary> getAllStockWithOutActiveCondition(Manager manager){
        return stockRepository.findAllByManager(manager);
    }

    @Transactional
    public void deleteStock(Long id) {
        stockRepository.deleteById(id);
    }
}
