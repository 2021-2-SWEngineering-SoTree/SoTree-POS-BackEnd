package sogong.restaurant.VO;

import lombok.Builder;
import sogong.restaurant.domain.StockDetail;

import java.util.List;

public class StockVO {
    private String stockName;
    private int quantity;
    private Long managerId;

    @Builder
    public StockVO(String stockName, int quantity, List<StockDetail> stockDetailList, Long managerId) {
        this.stockName = stockName;
        this.quantity = quantity;
        this.stockDetailList = stockDetailList;
        this.managerId = managerId;
    }

    List<StockDetail> stockDetailList;

    public String getStockName() {
        return stockName;
    }

    public int getQuantity() {
        return quantity;
    }

    public Long getManagerId(){return managerId;}

    public List<StockDetail> getStockDetailList() {
        return stockDetailList;
    }
}
