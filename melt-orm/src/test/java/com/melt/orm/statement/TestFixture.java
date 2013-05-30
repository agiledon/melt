package com.melt.orm.statement;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.melt.orm.config.parser.FieldConfig;
import com.melt.orm.config.parser.ModelConfig;
import com.melt.orm.session.Session;
import org.mockito.Mockito;
import sample.model.Bill;
import sample.model.Customer;
import sample.model.Item;
import sample.model.Order;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestFixture {
    public static Session prepareSession() {
        return new Session(null, prepareModelConfigsForCustomer());
    }

    public static Map<String, ModelConfig> prepareModelConfigsForCustomer() {
        Map<String, ModelConfig> modelConfigs = Maps.newHashMap();
        ModelConfig modelConfig = createCustomerModelConfig();
        modelConfigs.put(Customer.class.getName(), modelConfig);
        return modelConfigs;
    }

    public static Map<String, ModelConfig> prepareModelConfigsForOrders() {
        Map<String, ModelConfig> modelConfigs = Maps.newHashMap();
        modelConfigs.put(Customer.class.getName(), createCustomerModelConfig());
        modelConfigs.put(Order.class.getName(), createOrderModelConfig());
        modelConfigs.put(Item.class.getName(), createItemModelConfig());
        modelConfigs.put(Bill.class.getName(), createBillModelConfig());
        return modelConfigs;
    }

    private static ModelConfig createCustomerModelConfig() {
        List<FieldConfig> fieldConfigs = Lists.newArrayList();
        fieldConfigs.add(new FieldConfig("id", Integer.class));
        fieldConfigs.add(new FieldConfig("name", String.class));
        fieldConfigs.add(new FieldConfig("age", int.class));
        FieldConfig orders = new FieldConfig("orders", List.class);
        orders.setOneToMany(true);
        fieldConfigs.add(orders);
        return new ModelConfig(fieldConfigs, Customer.class);
    }

    private static ModelConfig createItemModelConfig() {
        List<FieldConfig> fieldConfigs = Lists.newArrayList();
        fieldConfigs.add(new FieldConfig("id", Integer.class));
        fieldConfigs.add(new FieldConfig("price", float.class));
        FieldConfig order = new FieldConfig("order", Order.class);
        order.setManyToOne(true);
        fieldConfigs.add(order);
        return new ModelConfig(fieldConfigs, Item.class);
    }

    private static ModelConfig createBillModelConfig() {
        List<FieldConfig> fieldConfigs = Lists.newArrayList();
        fieldConfigs.add(new FieldConfig("id", Integer.class));
        fieldConfigs.add(new FieldConfig("count", double.class));
        fieldConfigs.add(new FieldConfig("title", String.class));
        FieldConfig order = new FieldConfig("order", Order.class);
        order.setOneToOne(true);
        fieldConfigs.add(order);
        return new ModelConfig(fieldConfigs, Bill.class);
    }

    private static ModelConfig createOrderModelConfig() {
        List<FieldConfig> fieldConfigs = Lists.newArrayList();
        fieldConfigs.add(new FieldConfig("id", Integer.class));
        fieldConfigs.add(new FieldConfig("count", Integer.class));
        fieldConfigs.add(new FieldConfig("discount", Double.class));
        fieldConfigs.add(new FieldConfig("hasSent", short.class));
        fieldConfigs.add(new FieldConfig("orderAddress", String.class));
        FieldConfig items = new FieldConfig("items", List.class);
        items.setOneToMany(true);
        fieldConfigs.add(items);
        FieldConfig customer = new FieldConfig("customer", Customer.class);
        customer.setManyToOne(true);
        fieldConfigs.add(customer);
        FieldConfig bill = new FieldConfig("bill", Bill.class);
        bill.setOneToOne(true);
        fieldConfigs.add(bill);
        return new ModelConfig(fieldConfigs, Order.class);
    }
}