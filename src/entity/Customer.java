package entity;

import java.time.LocalDate;
import java.util.List;

public class Customer {
    private Long id;
    private String name;
    private List<Order> orders;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Double getTotalFee(){
        return orders.stream().map(Order::getTotalPrice).reduce(0.0,Double::sum);
    }

    public Double getTotalFeeBetweenTime(LocalDate start, LocalDate end){
        return orders.stream()
                .filter(order ->
                        ( !order.getOrderDate().isBefore(start) && !order.getOrderDate().isAfter(end) ))
                .map(Order::getTotalPrice)
                .reduce(0.0, Double::sum);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
