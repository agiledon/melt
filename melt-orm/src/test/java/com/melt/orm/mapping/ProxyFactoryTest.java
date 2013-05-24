package com.melt.orm.mapping;

import com.google.common.collect.ImmutableList;
import com.melt.orm.config.parser.ModelConfig;
import com.melt.orm.config.parser.ModelMappingHandler;
import com.melt.orm.session.Session;
import org.junit.Before;
import org.junit.Test;
import sample.model.Item;
import sample.model.Order;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.ImmutableList.of;
import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProxyFactoryTest {

    private Map<String,ModelConfig> modelConfigs;
    private ModelConfig orderModelConfig;
    private Session session;

    @Before
    public void setUp(){
        ModelMappingHandler handler = new ModelMappingHandler();
        modelConfigs = handler.mappingModelConfigs("sample.model");
        orderModelConfig = modelConfigs.get(Order.class.getName());
        session = mock(Session.class);
    }

    @Test
    public void should_return_proxy_object(){
//        Order order = ProxyFactory.getProxy(orderModelConfig, session);
//        order.setCount(1);
//        order.setId(1);
//        assertThat(order.getCount(), is(1));
//
//        List<Object> items = newArrayList();
//        Item item = new Item();
//        items.add(item);
//        when(session.find()).thenReturn(items);
//        assertThat(order.getItems().get(0), is(item));
    }
}
