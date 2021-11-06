package sogong.restaurant.VO;

import lombok.Builder;
import sogong.restaurant.domain.StockDetail;

import java.util.List;

public class StockVO {
    private String stockName;
    private Long branchId;
    private int quantity;

    @Builder
    public StockVO(String stockName, Long branchId, int quantity, List<StockDetail> stockDetailList) {
        this.stockName = stockName;
        this.branchId = branchId;
        this.quantity = quantity;
        this.stockDetailList = stockDetailList;
    }

    List<StockDetail> stockDetailList;

    public String getStockName() {
        return stockName;
    }

    public Long getBranchId() {
        return branchId;
    }

    public int getQuantity() {
        return quantity;
    }

    public List<StockDetail> getStockDetailList() {
        return stockDetailList;
    }
}
