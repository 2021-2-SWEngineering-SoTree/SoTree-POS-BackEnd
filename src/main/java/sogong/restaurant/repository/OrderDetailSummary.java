package sogong.restaurant.repository;

public interface OrderDetailSummary {

    int getQuantity();

    MenuSummary getMenu();

    interface MenuSummary {
        String getMenuName();

        int getPrice();
    }
}