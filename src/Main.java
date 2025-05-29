import entity.Customer;
import entity.Order;
import entity.OrderItem;
import entity.Product;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<Product> products = Arrays.asList(
                new Product(1L, "Laptop", 1200.0),
                new Product(2L, "Smartphone", 800.0),
                new Product(3L, "Tablet", 500.0),
                new Product(4L, "Monitor", 300.0),
                new Product(5L, "Keyboard", 70.0),
                new Product(6L, "Mouse", 50.0),
                new Product(7L, "Headphone", 150.0),
                new Product(8L, "Webcam", 90.0),
                new Product(9L, "Printer", 250.0),
                new Product(10L, "Smartwatch", 200.0)
        );

        List<Customer> customers = new ArrayList<>();
        for (long i = 1; i <= 5; i++) {
            Customer customer = new Customer();
            customer.setId(i);
            customer.setName("Customer " + i);
            customers.add(customer);
        }
        List<Order> allOrders = new ArrayList<>();
        Random rand = new Random();
        for (long i = 1; i <= 20; i++) {
            int itemCount = rand.nextInt(3) + 1;
            List<OrderItem> items = new ArrayList<>();

            for (int j = 0; j < itemCount; j++) {
                Product randomProduct = products.get(rand.nextInt(products.size()));
                int quantity = rand.nextInt(3) + 1;
                OrderItem item = new OrderItem();
                item.setId(i * 10 + j);
                item.setProduct(randomProduct);
                item.setQuantity(quantity);
                items.add(item);
            }

            Order order = new Order();
            order.setId(i);
            order.setOrderItems(items);
            order.setOrderDate(LocalDate.now().minusDays(rand.nextInt(30)));
            allOrders.add(order);

            Customer randomCustomer = customers.get(rand.nextInt(customers.size()));
            if (randomCustomer.getOrders() == null) {
                randomCustomer.setOrders(new ArrayList<>());
            }
            randomCustomer.getOrders().add(order);
        }
        Map<Product,Double> revenueOfPerProduct = allOrders.stream()
                .flatMap(order -> order.getOrderItems().stream())
                .collect(Collectors.groupingBy(OrderItem::getProduct,Collectors.summingDouble(OrderItem::getPrice)));
        List<Product> threeMostRevenueProducts = revenueOfPerProduct.entrySet()
                .stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .map(Map.Entry::getKey)
                .limit(3)
                .toList();
        System.out.println("Three products have most revenue");
        threeMostRevenueProducts.forEach(System.out::println);

        Map<Customer,Double> revenuePerCus = customers.stream().collect(Collectors.toMap(
                customer -> customer,
                Customer::getTotalFee
        ));

        System.out.println("Total revenue per customer");

        for (Map.Entry entry : revenuePerCus.entrySet()){
            System.out.println(entry.getKey() + ": " +entry.getValue());
        }

        System.out.println("Most expensive order per customer");
        Map<Customer,Order> mostExpensiveOrderPerCus = customers.stream().collect(Collectors.toMap(
                customer -> customer,
                customer -> customer.getOrders().stream()
                        .max(Comparator.comparing(Order::getTotalPrice)).orElse(null)
        ));
        for (Map.Entry entry : mostExpensiveOrderPerCus.entrySet()){
            System.out.println(entry.getKey() + ": " +entry.getValue());
        }

        System.out.println("Categorize orders by month");

        Map<YearMonth,List<Order>> ordersPerMonth = allOrders.stream()
                .collect(Collectors.groupingBy(order -> YearMonth.from(order.getOrderDate())));
        for (Map.Entry entry : ordersPerMonth.entrySet()){
            System.out.println(entry.getKey() + ": " +entry.getValue());
        }

        Map<YearMonth, Double> totalRevenuePerMonth = ordersPerMonth.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        orderPerMonth -> orderPerMonth.getValue()
                                .stream().map(Order::getTotalPrice).reduce(0.0,Double::sum)
                ));
        System.out.println("Total revenue per month");
        for (Map.Entry entry : totalRevenuePerMonth.entrySet()){
            System.out.println(entry.getKey() + ": " +entry.getValue());
        }
        LocalDate start = LocalDate.of(2015,12,2);
        LocalDate end = LocalDate.of(2016,12,2);

        System.out.println("The most productive customer in this time");

        System.out.println(customers.stream()
                .max(Comparator.comparing(customer -> customer.getTotalFeeBetweenTime(start,end))));
    }
}