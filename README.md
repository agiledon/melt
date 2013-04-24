##MELT

Melt is lightweight IoC container and no intrusiveness based on Java platform. It provides common features of IoC including constructor injection, setter injection, autowire injection and factory method injection. It doesn't use stuffy xml configuraiton mechanic or annotation style. It supports POJO object to initialize all java objects automatically.

###Benifit
* No intrusiveness;
* No xml configure files to be managed;
* Pure pojo object support;
* DSL feature let it more make sense;
* Support container scope;

###How to use it
In melt framework, Container and ContainerBuilder are the core classes. The basic operations are register() and resolve() method. 

####Default Constructor
Java class with default construtor:
```java
Container container = builder.register(DefaultCustomerService.class)
                             .build();
DefaultCustomerService container.resolve(DefaultCustomerService.class);
```              

####Constructor Injection
Java class with parameterized constructor:
```java
container = builder.register(DefaultCustomerService.class)
                   .withConstructorParameter(CustomerDao.class)
                   .withConstructorParameter(CustomerFiller.class)
                   .build();

DefaultCustomerService customerService = container.resolve(DefaultCustomerService.class);
assertThat(customerService, not(nullValue()));
```

When constructor contains some primitive types, String type or List:
```java
List<Customer> newCustomers = newArrayList(new Customer(1, "zhangyi"));
container = builder.register(DefaultCustomerService.class)
                  .withConstructorParameter(CustomerDao.class)
                  .withConstructorParameter(5)
                  .withConstructorParameter("hello melt")
                  .withConstructorParameter(50.0)
                  .withConstructorParameter(5000L)
                  .withConstructorParameter(40.0f)
                  .withConstructorParameter(newCustomers)
                .build();

DefaultCustomerService customerService = container.resolve(DefaultCustomerService.class);
assertThat(customerService, not(nullValue()));
```

####Property Injection
Java class with one property:
```java
container = builder.register(DefaultBankService.class)
                  .withProperty(DefaultBankDao.class)
                  .build();
DefaultBankService bankService = container.resolve(DefaultBankService.class);
assertThat(bankService, not(nullValue()));
```

Java class with nested property:
```java
container = builder.register(CustomerDao.class)
                  .withProperty(JdbcTemplate.class)
                  .register(DefaultCustomerService.class)
                  .withProperty(CustomerDao.class)
                  .withProperty(CustomerFiller.class)
                  .build();

DefaultCustomerService customerService = container.resolve(DefaultCustomerService.class);
assertThat(customerService, not(nullValue()));
assertThat(customerService.getCustomerFiller(), instanceOf(CustomerFiller.class));

CustomerDao customerDao = customerService.getCustomerDao();
assertThat(customerDao, instanceOf(CustomerDao.class));
assertThat(customerDao.getJdbcTemplate(), instanceOf(JdbcTemplate.class));
```

When property type is any primitive type or String or List:
```java
List<String> accounts = newArrayList("haha");
container = builder.register(DefaultBankService.class)
                   .withProperty(DefaultBankDao.class)
                   .withProperty("max", 1)
                   .withProperty("tax", 2.3)
                   .withProperty("interest", 2.3f)
                   .withProperty("maxMoney", 12345l)
                   .withProperty("account", "haha")
                   .withProperty("accounts", accounts)
                   .build();

DefaultBankService bankService = container.resolve(BankService.class);
assertThat(bankService.getBankDao(), instanceOf(BankDao.class));
assertThat(bankService.getMax(), is(1));
assertThat(bankService.getTax(), is(2.3));
assertThat(bankService.getInterest(), is(2.3f));
assertThat(bankService.getMaxMoney(), is(12345l));
assertThat(bankService.getAccount(), is("haha"));
assertThat(bankService.getAccounts(), is(accounts));
```

####Register java object with name
Regiser java bean with name:
```java
container = builder.register(DefaultBankService.class)
                .asName("bankService")
                .build();

DefaultBankService bankService = container.resolve("bankService");
```

####Support to resolve java object with interface type
Assuming DefaultBankService implements BankService interface:
```java
container = builder.register(DefaultBankService.class)
                  .withProperty(DefaultBankDao.class)
                  .build();

DefaultBankService bankService = container.resolve(BankService.class);
assertThat(bankService, instanceOf(BankService.class));
assertThat(bankService.getBankDao(), instanceOf(BankDao.class));
```        

Assuming CustomerDao class implements both CustomerDaoInterface and AnotherCustomerDaoInterface:
```java
container = builder.register(CustomerDao.class)
                  .asName("customerDao")
                  .register(DefaultCustomerService.class)
                  .withRefProperty("customerDao")
                  .build();

DefaultCustomerService customerService = container.resolve(DefaultCustomerService.class);
assertThat(customerService, instanceOf(DefaultCustomerService.class));
assertThat(customerService.getCustomerDao(), instanceOf(CustomerDao.class));
assertThat(customerService.getCustomerDao(), instanceOf(CustomerDaoInterface.class));
assertThat(customerService.getCustomerDao(), instanceOf(AnotherCustomerDaoInterface.class));
```

####Autwired
Autowired by type on container level:
```java
builder = new ContainerBuilder(AutoWiredBy.TYPE);
container = builder.register(DefaultBankService.class)
                   .register(DefaultBankDao.class)
                   .build();

DefaultBankService bankService = container.resolve(BankService.class);
assertThat(bankService.getBankDao(), instanceOf(BankDao.class));     
```

Autowired by type for each java class:
```java
container = builder.register(DefaultBankService.class)
                   .autoWiredBy(AutoWiredBy.TYPE)
                   .register(DefaultBankDao.class)
                   .build();

DefaultBankService bankService = container.resolve(BankService.class);
assertThat(bankService.getBankDao(), instanceOf(BankDao.class));
```

####Factory Method Injection
Provide factory method:
```java
container = builder.register(DefaultBankService.class)
                   .factory("init")
                   .build();

DefaultBankService bankService = container.resolve(DefaultBankService.class);
assertThat(bankService, instanceOf(DefaultBankService.class));
```

####Container Scope
Child container know the object in the parent container.

Inject from parent container by name:
```java
container = builder.register(DefaultBankDao.class)
                   .asName("bankDao")
                  .build();

ContainerBuilder subBuilder = new ContainerBuilder();
Container subContainer = subBuilder.parent(container)
                                  .register(DefaultBankService.class)
                                  .autoWiredBy(AutoWiredBy.NAME)
                                  .build();

assertThat(subContainer.resolve(BankDao.class), instanceOf(BankDao.class));
DefaultBankService bankService = subContainer.resolve(BankService.class);
assertThat(bankService, instanceOf(BankService.class));
assertThat(bankService.getBankDao(), instanceOf(BankDao.class));
```

Constructor injection with ref bean in the parent container:
```java
container = builder.register(CustomerDao.class)                                 
                   .asName("customerDao")                                                  
                   .register(CustomerFiller.class)                                         
                   .asName("customerFiller")                                               
                   .build();                                                               
                                                                                
ContainerBuilder childBuilder = new ContainerBuilder();                         
Container childContainer = childBuilder.register(DefaultCustomerService.class)  
                                       .withRefConstructorParameter("customerDao")                             
                                       .withRefConstructorParameter("customerFiller")                          
                                       .parent(container)                                                      
                                       .build();                                                               
````

Autowired by type from parent container:
```java
container = builder.register(DefaultBankDao.class)                          
                   .build();                                                           
ContainerBuilder subBuilder = new ContainerBuilder();                       
Container subContainer = subBuilder.parent(container)                       
                                   .register(DefaultBankService.class)                                 
                                   .autoWiredBy(AutoWiredBy.TYPE)                                      
                                   .build();                                                           

assertThat(subContainer.resolve(BankDao.class), instanceOf(BankDao.class)); 
DefaultBankService bankService = subContainer.resolve(BankService.class);   
assertThat(bankService, instanceOf(BankService.class));                     
assertThat(bankService.getBankDao(), instanceOf(BankDao.class));            
```