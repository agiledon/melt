package com.melt.sample;

import com.bazaarvoice.dropwizard.assets.ConfiguredAssetsBundle;
import com.melt.config.autowired.AutoWiredBy;
import com.melt.core.Container;
import com.melt.core.InjectionModule;
import com.melt.core.Melt;
import com.melt.orm.config.MeltOrmManager;
import com.melt.sample.config.MeltConfiguration;
import com.melt.sample.dao.CustomerDao;
import com.melt.sample.dao.OrderDao;
import com.melt.sample.model.Order;
import com.melt.sample.resources.customer.*;
import com.melt.sample.resources.IndexResource;
import com.melt.sample.resources.order.AddOrderResource;
import com.melt.sample.resources.order.EditOrderResource;
import com.melt.sample.resources.order.OrderItemsResource;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.views.ViewBundle;

import java.util.List;

public class Main extends Service<MeltConfiguration> {
    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.run(new String[]{"server"});
    }

    @Override
    public void initialize(Bootstrap bootstrap) {
        bootstrap.setName("Melt");
        bootstrap.addBundle(new ViewBundle());
        bootstrap.addBundle(new ConfiguredAssetsBundle("/assets/", "/assets/"));
    }

    @Override
    public void run(MeltConfiguration configuration, Environment environment) throws Exception {
        Container container = initIocContainer();
        environment.addResource(container.resolve(IndexResource.class));
        environment.addResource(container.resolve(CustomerResource.class));
        environment.addResource(container.resolve(EditCustomerResource.class));
        environment.addResource(container.resolve(AddCustomerResource.class));
        environment.addResource(container.resolve(DeleteCustomerResource.class));
        environment.addResource(container.resolve(CustomerOrdersResource.class));
        environment.addResource(container.resolve(EditOrderResource.class));
        environment.addResource(container.resolve(OrderItemsResource.class));
        environment.addResource(container.resolve(AddOrderResource.class));
    }

    private Container initIocContainer() {
        return Melt.createContainer(new InjectionModule(AutoWiredBy.TYPE) {
            @Override
            public void configure() {

                register(MeltOrmManager.class)
                        .withProperty("modelPackage", "com.melt.sample.model")
                        .withProperty("url", "jdbc:mysql://localhost:3306/melt")
                        .withProperty("driver", "com.mysql.jdbc.Driver")
                        .withProperty("username", "root")
                        .withProperty("password", "")
                        .factory("createSessionFactory");
                register(OrderDao.class);
                register(CustomerDao.class);

                register(IndexResource.class);
                register(CustomerResource.class);
                register(EditCustomerResource.class);
                register(AddCustomerResource.class);
                register(DeleteCustomerResource.class);
                register(CustomerOrdersResource.class);
                register(AddOrderResource.class);
                register(EditOrderResource.class);
                register(OrderItemsResource.class);
            }
        });
    }
}
