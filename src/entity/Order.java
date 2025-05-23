package entity;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public class Order implements Comparator<Order> {
    private Long id;
    private List<OrderItem> orderItems;
    private LocalDate orderDate;
    private Customer customer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public Double getTotalPrice() {
        return orderItems.stream()
                .map(OrderItem::getPrice)
                .reduce(0.0, Double::sum);
    }

    @Override
    public int compare(Order o1, Order o2) {
        return o1.getTotalPrice().compareTo(o2.getTotalPrice());
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderItems=" + orderItems +
                ", orderDate=" + orderDate +
                ", totalFee=" + this.getTotalPrice() +
                '}';
    }
}
