package com.melt.orm.session;

import com.melt.orm.config.parser.ModelConfig;
import com.melt.orm.criteria.By;
import com.melt.orm.dialect.DatabaseDialect;
import com.melt.orm.dialect.MySQLDialect;
import com.melt.orm.exceptions.MeltOrmException;
import com.melt.orm.statement.TestFixture;
import org.junit.Before;
import org.junit.Test;
import sample.model.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static com.melt.orm.config.MeltOrmConfigure.register;
import static com.melt.orm.config.MeltOrmConfigure.registerModels;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataSourceSessionFactoryTest {

    private SessionFactory sessionFactory;
    private Session session;

    @Before
    public void setUp() throws SQLException {
        sessionFactory = buildSessionFactory(TestFixture.prepareModelConfigsForOrders());
        session = sessionFactory.createSession();
        session.deleteAll(Customer.class);
        session.deleteAll(Order.class);
        session.deleteAll(Item.class);
        session.deleteAll(Bill.class);
    }

    @Test
    public void should_return_session_from_data_source() throws SQLException {
        DatabaseDialect dialect = mock(DatabaseDialect.class);
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class)  ;
        when(dataSource.getConnection()).thenReturn(connection);
        HashMap<String,ModelConfig> modelConfigs = newHashMap();
        DataSourceSessionFactory sessionFactory = new DataSourceSessionFactory(dialect, modelConfigs, dataSource);
        Session session = sessionFactory.createSession();
        assertThat(session.getConnection(), is(connection));
    }

    @Test(expected = MeltOrmException.class)
    public void should_throw_exception_when_get_connection_from_data_source_has_error() throws SQLException {
        DatabaseDialect dialect = mock(DatabaseDialect.class);
        DataSource dataSource = mock(DataSource.class);
        when(dataSource.getConnection()).thenThrow(SQLException.class);
        HashMap<String,ModelConfig> modelConfigs = newHashMap();
        DataSourceSessionFactory sessionFactory = new DataSourceSessionFactory(dialect, modelConfigs, dataSource);
        sessionFactory.createSession();
    }

    @Test
    public void should_create_tables() {
//        sessionFactory = buildSessionFactory();
        //        Session session = sessionFactory.createSession();
//        List<Order> orders = session.find(Order.class, By.id(1));
//        Order order = orders.get(0);
//        System.out.println(order.toString());
//        assertThat(order.getItems().size(), is(3));
//
//        List<Item> items = session.find(Item.class, By.nil());
//
//        for (Item item : items) {
//            System.out.println(item.toString());
//        }
//        Item item = items.get(0);
//        assertThat(item.getOrder().getId(), is(1));
//        sessionFactory.createTables();
    }

    @Test
    public void should_insert_a_customer() throws SQLException {
        Customer entity = createCustomer();
        session.insert(entity);
    }

    @Test
    public void should_insert_an_order_with_items() throws SQLException {
        Order order = createOrder();
        session.insert(order);
    }

    @Test
    public void should_insert_an_order_with_items_when_customer_is_existed() throws SQLException {
        Customer customer = createCustomer();
        session.insert(customer);
        List<Customer> customers = session.find(Customer.class, By.eq("name", "ZhangYi"));
        Order order = createOrder(customers.get(0));
        session.insert(order);
    }

    @Test
    public void should_insert_an_order_without_item() throws SQLException {
        Order order = createOrderWithoutItem();
        session.insert(order);
    }

    @Test
    public void should_update_customer() throws SQLException {
        Customer customer = createCustomer();
        session.insert(customer);

        customer.setName("He Haiyang");
        customer.setCustomerType(CustomerType.COMMONS);
        session.update(customer);

        List<Customer> customers = session.find(Customer.class, By.eq("name", "He Haiyang"));
        assertThat(customers.size(), is(1));
        assertThat(customers.get(0).getName(), is("He Haiyang"));
        assertThat(customers.get(0).getCustomerType(), is(CustomerType.COMMONS));
    }

    @Test
    public void should_update_order() throws SQLException {
        Order order = createOrder();
        session.insert(order);

        List<Order> orders = session.find(Order.class, By.eq("orderAddress", "address"));
        order = orders.get(0);
        assertThat(order.getBill().getTitle(), is("bill title"));

        order.setOrderAddress("new address");
        Bill bill = order.getBill();
        bill.setTitle("new title");
        List<Item> items = order.getItems();
        items.get(0).setPrice(9999.9f);
        session.update(order);

        List<Order> newOrders = session.find(Order.class, By.eq("orderAddress", "new address"));
        assertThat(newOrders.size(), is(1));
        assertThat(newOrders.get(0).getOrderAddress(), is("new address"));
//        assertThat(newOrders.get(0).getBill().getTitle(), is("new title"));
//        assertThat(newOrders.get(0).getItems().get(0).getPrice(), is(9999.9f));
    }

    @Test
    public void should_update_single_order() throws SQLException {
        Order order = createSingleOrder();
        session.insert(order);

        List<Order> orders = session.find(Order.class, By.eq("orderAddress", "address"));
        order = orders.get(0);

        order.setOrderAddress("new address");
        session.update(order);

        List<Order> newOrders = session.find(Order.class, By.eq("orderAddress", "new address"));
        assertThat(newOrders.size(), is(1));
        assertThat(newOrders.get(0).getOrderAddress(), is("new address"));
//        assertThat(newOrders.get(0).getBill().getTitle(), is("new title"));
//        assertThat(newOrders.get(0).getItems().get(0).getPrice(), is(9999.9f));
    }

    @Test
    public void should_display_create_tables() {
        sessionFactory.showCreateTablesSQL();
    }

    private Order createOrder() {
        Customer customer = createCustomer();

        Bill bill = new Bill();
        bill.setCount(200.5);
        bill.setTitle("bill title");

        Order order = new Order();
        order.setCount(1);
        order.setDiscount(0.7);
        order.setOrderAddress("address");
        order.setHasSent(false);
        order.setCustomer(customer);
        order.setBill(bill);

        ArrayList<Item> items = newArrayList();

        Item item1 = new Item();
        item1.setPrice(20.0f);
        item1.setOrder(order);
        items.add(item1);


        Item item2 = new Item();
        item2.setPrice(30.5f);
        item2.setOrder(order);
        items.add(item2);

        order.setItems(items);
        bill.setOrder(order);
        return order;
    }

    private Order createSingleOrder() {
        Order order = new Order();
        order.setCount(1);
        order.setDiscount(0.7);
        order.setOrderAddress("address");
        order.setHasSent(false);
        return order;
    }

    private Order createOrder(Customer customer) {

        Bill bill = new Bill();
        bill.setCount(200.5);
        bill.setTitle("bill title");

        Order order = new Order();
        order.setCount(1);
        order.setDiscount(0.7);
        order.setOrderAddress("address");
        order.setHasSent(false);
        order.setCustomer(customer);
        order.setBill(bill);

        ArrayList<Item> items = newArrayList();

        Item item1 = new Item();
        item1.setPrice(20.0f);
        item1.setOrder(order);
        items.add(item1);


        Item item2 = new Item();
        item2.setPrice(30.5f);
        item2.setOrder(order);
        items.add(item2);

        order.setItems(items);
        order.setCustomer(customer);
        bill.setOrder(order);
        return order;
    }

    private Customer createCustomer() {
        Customer entity = new Customer();
        entity.setName("ZhangYi");
        entity.setAge(37);
        return entity;
    }

    private Order createOrderWithoutItem() {
        Customer customer = createCustomer();

        Bill bill = new Bill();
        bill.setCount(200.5);
        bill.setTitle("bill title");

        Order order = new Order();
        order.setCount(1);
        order.setDiscount(0.7);
        order.setOrderAddress("address");
        order.setHasSent(false);
        order.setCustomer(customer);
        order.setBill(bill);
        return order;
    }

    private SessionFactory buildSessionFactory() {
        return registerModels("sample.model")
                .withDatabaseConfig("jdbc:mysql://localhost:3306/melt", "com.mysql.jdbc.Driver", "root", "")
                .withDialect(new MySQLDialect())
                .build();
    }

    private SessionFactory buildSessionFactory(Map<String, ModelConfig> modelConfigs) {
        return register()
                .withDatabaseConfig("jdbc:mysql://localhost:3306/melt", "com.mysql.jdbc.Driver", "root", "")
                .withDialect(new MySQLDialect())
                .build(modelConfigs);
    }
}
