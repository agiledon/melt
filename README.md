##MELT

Melt is lightweight IoC container and no intrusiveness based on Java platform. It provides common features of IoC including constructor injection, setter injection, autowire injection and factory method injection. It doesn't use stuffy xml configuraiton mechanic or annotation style. It supports POJO object to initialize all java objects automatically.

###Benifit
* No intrusiveness;
* No xml configure files to be managed;
* Pure pojo object support;
* DSL feature let it more make sense;
* Support container scope;
* Autowired by type or name;

###How to use it
In melt framework, Container and ContainerBuilder are the core classes. The basic operations are register() and resolve() method. 

#### Step 1: Define Module
Define the class derived from InjectionModule provided by Melt, then register the bean what you want to resolve. For example:
```java
public class MyInjectionModule extends InjectionModule {
    @Override
    public void configure() {
        register(DefaultBankService.class)
       		.withConstructorParameter(CustomerFiller.class);
        register(DefaultBankDao.class)
            .withProperty("message", "melt");
    }
}
```

#### Step 2: Create Container
You can invoke the factory method of Melt to create container with your module:
```java
Container container = Melt.createContainer(new MyInjectionModule);

DefaultBankService bankService = container.resolve(BankService.class);
assertThat(bankService.getBankDao(), instanceOf(BankDao.class));
```

#### Step 3(Optional): Parent Container
You can assign the parent container when you create container, so that the child container know the object in the parent container. 

```java
public class ParentModule extends InjectionModule {
    @Override
    public void configure() {
        register(DefaultBankDao.class).asName("bankDao");
    }
}
public class ChildModule extends InjectionModule {
    @Override
    public void configure() {
        register(DefaultBankService.class).autoWiredBy(AutoWiredBy.NAME);
    }
}
        
parentContainer = Melt.createContainer(new ParentModule);
Container childContainer = Melt.createContainer(new ChildModule(), parentContainer);

assertThat(subContainer.resolve(BankDao.class), instanceOf(BankDao.class));
DefaultBankService bankService = subContainer.resolve(BankService.class);
assertThat(bankService, instanceOf(BankService.class));
assertThat(bankService.getBankDao(), instanceOf(BankDao.class));
```

###Todo
* Bean scope; currently, just support singleton scope;
* Plan to support JDK 1.8, and introduce Lambda expression feature;
