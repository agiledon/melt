package com.melt.orm.mapping;

import com.melt.orm.config.parser.ModelConfig;
import com.melt.orm.config.parser.ModelMappingHandler;
import com.melt.orm.session.Session;
import org.junit.Before;
import org.junit.Test;
import sample.model.OnlyOneField;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MapperTest {
    private Session session;
    private Map<String,ModelConfig> modelConfigs;
    private ResultSet resultSet;

    @Before
    public void setUp() {
        session = mock(Session.class);
        resultSet = mock(ResultSet.class);
        ModelMappingHandler handler = new ModelMappingHandler();

        modelConfigs = handler.mappingModelConfigs("sample.model");
        when(session.getModelConfigs()).thenReturn(modelConfigs);
    }
    @Test
    public void should_return_list_of_result() throws SQLException {
        Mapper mapper = new Mapper(session);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getObject("ID")).thenReturn(1);
        List<OnlyOneField> list = mapper.mapResult(OnlyOneField.class, resultSet);
        assertThat(list.get(0).getId(), is(1));
    }
}
