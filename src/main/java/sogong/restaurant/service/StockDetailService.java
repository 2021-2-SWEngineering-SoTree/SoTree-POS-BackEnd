package sogong.restaurant.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sogong.restaurant.domain.Stock;
import sogong.restaurant.domain.StockDetail;
import sogong.restaurant.repository.StockDetailRepository;
import sogong.restaurant.repository.StockRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
@AllArgsConstructor
public class StockDetailService {

    @Autowired
    private final StockDetailRepository stockDetailRepository;
    @Autowired
    private final StockRepository stockRepository;


    @Transactional
    public Long addStockDetail(Stock stock, StockDetail stockDetail) {
        int currentQuantity = stock.getQuantity(); // 현재 양
        int quantityChanged = stockDetail.getQuantityChanged(); // 변화량
        int finalQuantity = currentQuantity + quantityChanged;   // 변화 이후

        if (finalQuantity < 0) {
            throw new IllegalStateException("잔여 재고 양이 0보다 적습니다.");
        }

        stockDetail.setFinalQuantity(finalQuantity); // 처음 재고 설정이므로 변화 이후 양도 동일함
        stockDetail.setMemo("기본");
        stock.setQuantity(finalQuantity);

        stockRepository.save(stock);
        return stockDetailRepository.save(stockDetail).getId();
    }

    @Transactional
    public void deleteStockDetail(Long id) {
        stockDetailRepository.deleteById(id);
    }

    public List<StockDetail> getStockDetailByStock(Stock stock) {
        return stockDetailRepository.findStockDetailByStock(stock);
    }

}
