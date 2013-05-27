package com.melt.orm.mapping;

import com.melt.orm.config.parser.ModelConfig;
import com.melt.orm.config.parser.ModelMappingHandler;
import com.melt.orm.session.Session;
import net.sf.cglib.proxy.MethodProxy;
import org.junit.Before;
import org.junit.Test;
import sample.model.Item;
import sample.model.Order;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.melt.orm.criteria.By.eq;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProxyCallbackTest {
    private Session session;
    private Map<String, ModelConfig> modelConfigs;
    private MethodProxy proxy;
    private ProxyCallback callback;
    private ModelConfig orderConfig;

    @Before
    public void setUp() {
        ModelMappingHandler handler = new ModelMappingHandler();
        modelConfigs = handler.mappingModelConfigs("sample.model");
        orderConfig = modelConfigs.get(Order.class.getName());

        session = mock(Session.class);
        when(session.getModelConfigs()).thenReturn(modelConfigs);

        proxy = mock(MethodProxy.class);
        callback = new ProxyCallback(session, Order.class);
    }

    @Test
    public void should_return_list_for_one_to_many_field() throws Throwable {
        List<Object> items = newArrayList();

        when(session.find(Item.class, eq("OrderId", 1))).thenReturn(items);
        Order order = new Order();
        order.setId(1);
        Method getItemsMethod = Order.class.getMethod("getItems", null);
        List<Object> intercept = (List<Object>) callback.intercept(order, getItemsMethod, null, proxy);
        assertThat(intercept, is(items));
    }
}
