package sogong.restaurant.summary;

public interface OrderDetailSummary {

    int getQuantity();

    MenuSummary getMenu();

    interface MenuSummary {
        String getMenuName();

        int getPrice();
    }
}