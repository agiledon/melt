##MELT

Melt is lightweight IoC container and no intrusiveness based on Java platform. It provides common features of IoC including constructor injection, setter injection, autowire injection and factory method injection. It doesn't use stuffy xml configuraiton mechanic or annotation style. It supports POJO object to initialize all java objects automatically.

###Benifit
no intrusiveness;
no xml configure files to be managed;
pure pojo object support;
DSL feature let it more make sense;

###How to use it
In melt framework, Container and ContainerBuilder are the core classes. The basic operations are register() and resolve() method. 

####Manage POJO object
Java class with default construtor:
{% codeblock lang:java %}
Container container = builder.register(DefaultCustomerService.class)
                .build();
DefaultCustomerService customerService = container.resolve(DefaultCustomerService.class);
{% endcodeblock %}                





