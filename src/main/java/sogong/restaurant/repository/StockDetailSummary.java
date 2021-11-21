package sogong.restaurant.repository;

public interface StockDetailSummary {

    String getTime();
    int getQuantityChanged();
    int getFinalQuantity();

    EmployeeSummary getEmployee();

    interface EmployeeSummary {
        UserSummary getUser();
        interface UserSummary{
            String getPersonName();
        }
    }
}