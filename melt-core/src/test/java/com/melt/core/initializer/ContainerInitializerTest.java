package com.melt.core.initializer;

import com.melt.core.BeansContainer;
import com.melt.sample.customer.service.DefaultCustomerService;
import org.junit.Test;

public class ContainerInitializerTest {
    @Test
    public void should_initial_bean_with_default_constructor() {
        ContainerInitializer containerInitializer = new ContainerInitializer();
        BeansContainer beansContainer = new BeansContainer();
        beansContainer.addBean("customerService", new DefaultCustomerService(null));
        InitializedContainer container = containerInitializer.initialize(beansContainer);
    }
}
