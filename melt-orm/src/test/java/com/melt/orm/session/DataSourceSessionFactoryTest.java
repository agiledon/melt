package com.melt.orm.session;

import com.melt.orm.config.parser.ModelConfig;
import com.melt.orm.criteria.By;
import com.melt.orm.dialect.DatabaseDialect;
import com.melt.orm.dialect.MySQLDialect;
import com.melt.orm.exceptions.MeltOrmException;
import org.junit.Test;
import sample.model.Item;
import sample.model.Order;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import static com.google.common.collect.Maps.newHashMap;
import static com.melt.orm.config.MeltOrmConfigure.registerModels;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataSourceSessionFactoryTest {
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
        SessionFactory sessionFactory = registerModels("sample.model")
                .withDatabaseConfig("jdbc:mysql://localhost:3306/melt", "com.mysql.jdbc.Driver", "root", "")
                .withDialect(new MySQLDialect())
                .build();
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
    public void should_display_create_tables() {
        SessionFactory sessionFactory = registerModels("sample.model")
                .withDatabaseConfig("jdbc:mysql://localhost:3306/melt", "com.mysql.jdbc.Driver", "root", "")
                .withDialect(new MySQLDialect())
                .build();

        sessionFactory.showCreateTablesSQL();
    }
}
