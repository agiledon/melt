package com.melt.orm.config;

import com.melt.orm.dialect.DatabaseDialect;
import com.melt.orm.exceptions.MeltOrmException;
import com.melt.orm.session.Session;
import com.melt.orm.session.SessionFactory;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;

import static com.melt.orm.config.MeltOrmConfigure.registerModels;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MeltOrmConfigureTest {
    private DatabaseDialect dialect;
    private DataSource dataSource;
    private Connection connection;

    @Before
    public void setUp() {
        dialect = mock(DatabaseDialect.class);
        dataSource = mock(DataSource.class);
        connection = mock(Connection.class);
    }

    @Test
    public void should_return_data_source_session_factory() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        SessionFactory sessionFactory = registerModels("sample.model")
                .withDialect(dialect)
                .withDataSource(dataSource)
                .build();
        Session session = sessionFactory.createSession();
        assertThat(session.getConnection(), is(connection));
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_when_register_models_with_null_package() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        registerModels(null)
                .withDialect(dialect)
                .withDataSource(dataSource)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_when_with_null_data_source() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        registerModels("sample.model")
                .withDialect(dialect)
                .withDataSource(null)
                .build();
    }

    @Test(expected = MeltOrmException.class)
    public void should_throw_exception_when_set_data_source_after_set_data_base_config() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        registerModels("sample.model")
                .withDialect(dialect)
                .withDatabaseConfig("url", "driver", "username", "password")
                .withDataSource(null)
                .build();
    }

    @Test(expected = MeltOrmException.class)
    public void should_throw_exception_when_set_data_base_config_after_set_data_source() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        registerModels("sample.model")
                .withDialect(dialect)
                .withDataSource(dataSource)
                .withDatabaseConfig("url", "driver", "username", "password")
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_when_with_null_data_base_dialect() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        registerModels("sample.model")
                .withDialect(null)
                .withDataSource(dataSource)
                .build();
    }


}
