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
import com.melt.sample.resources.customer.AddCustomerResource;
import com.melt.sample.resources.customer.CustomerResource;
import com.melt.sample.resources.customer.DeleteCustomerResource;
import com.melt.sample.resources.customer.EditCustomerResource;
import com.melt.sample.resources.IndexResource;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.views.ViewBundle;

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
        Container container = Melt.createContainer(new InjectionModule(AutoWiredBy.TYPE) {
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
            }
        });

        OrderDao orderDao = container.resolve(OrderDao.class);
        Order order = orderDao.findById(1);
        System.out.println(order.getCount());
        environment.addResource(container.resolve(IndexResource.class));
        environment.addResource(container.resolve(CustomerResource.class));
        environment.addResource(container.resolve(EditCustomerResource.class));
        environment.addResource(container.resolve(AddCustomerResource.class));
        environment.addResource(container.resolve(DeleteCustomerResource.class));

    }
}
