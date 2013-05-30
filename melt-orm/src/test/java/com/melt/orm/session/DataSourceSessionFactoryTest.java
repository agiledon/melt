package com.melt.orm.session;

import com.melt.orm.config.parser.ModelConfig;
import com.melt.orm.dialect.DatabaseDialect;
import com.melt.orm.dialect.MySQLDialect;
import com.melt.orm.exceptions.MeltOrmException;
import com.melt.orm.statement.TestFixture;
import org.junit.Test;
import sample.model.Bill;
import sample.model.Customer;
import sample.model.Item;
import sample.model.Order;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static com.melt.orm.config.MeltOrmConfigure.register;
import static com.melt.orm.config.MeltOrmConfigure.registerModels;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.internal.util.collections.Sets.newSet;

public class DataSourceSessionFactoryTest {

    private SessionFactory sessionFactory;

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
        sessionFactory = buildSessionFactory();
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
    public void should_insert_a_customer() {
        SessionFactory sessionFactory = buildSessionFactory(TestFixture.prepareModelConfigsForCustomer());
        Session session = sessionFactory.createSession();
        Customer entity = new Customer();
        entity.setName("ZhangYi");
        entity.setAge(37);
        session.insert(entity);
    }

    @Test
    public void should_insert_an_order() {
        SessionFactory sessionFactory = buildSessionFactory(TestFixture.prepareModelConfigsForOrders());
        Session session = sessionFactory.createSession();

        Customer customer = new Customer();
        customer.setName("ZhangYi");
        customer.setAge(37);
        Set<Order> orders = newSet();

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


        Item item2 = new Item();
        item2.setPrice(30.5f);
        item2.setOrder(order);

        order.setItems(items);
        bill.setOrder(order);

        orders.add(order);

        customer.setOrders(orders);

        session.insert(order);
    }

    @Test
    public void should_display_create_tables() {
        sessionFactory = buildSessionFactory();
        sessionFactory.showCreateTablesSQL();
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
