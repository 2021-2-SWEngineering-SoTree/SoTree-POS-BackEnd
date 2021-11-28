package sogong.restaurant.summary;

public interface StockDetailSummary {

    String getTime();
    int getQuantityChanged();
    int getFinalQuantity();
    String getMemo();

    EmployeeSummary getEmployee();

    interface EmployeeSummary {
        UserSummary getUser();
        interface UserSummary{
            String getPersonName();
        }
    }
}