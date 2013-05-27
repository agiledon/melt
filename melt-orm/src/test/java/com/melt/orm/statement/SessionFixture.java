package com.melt.orm.statement;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.melt.orm.config.parser.FieldConfig;
import com.melt.orm.config.parser.ModelConfig;
import com.melt.orm.session.Session;
import org.mockito.Mockito;
import sample.model.Customer;

import java.util.List;
import java.util.Map;

public class SessionFixture {
    static Session prepareSession() {
        Session session = Mockito.mock(Session.class);
        Map<String, ModelConfig> modelConfigs = Maps.newHashMap();
        List<FieldConfig> fieldConfigs = Lists.newArrayList();
        fieldConfigs.add(new FieldConfig("id", Integer.class));
        fieldConfigs.add(new FieldConfig("name", String.class));
        fieldConfigs.add(new FieldConfig("age", int.class));
        FieldConfig orders = new FieldConfig("orders", List.class);
        orders.setOneToMany(true);
        fieldConfigs.add(orders);
        modelConfigs.put(Customer.class.getName(), new ModelConfig(fieldConfigs, Customer.class));
        Mockito.when(session.getModelConfigs()).thenReturn(modelConfigs);
        return session;
    }
}