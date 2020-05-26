# spring
1. ## 循环依赖
	1. obj对象-》BeanDefinition -> bean对象
	2. spring会先去扫描要注入得类得配置，如id名字等 生成一个beanDefinition，将所有得beanDefinition何为一个map<beanId,beanDefinition>,  
	并且生成一个List<beanId>。spring若其扩展，一般则是实现BeanFactoryPostProcessor(算是增加一个后置处理器)，会传入这个map,若没有扩展，则会通过map,PreinstantiateSingletons生成bean
	3. spring默认支持单列得循环依赖，因为在spring的bean生命周期中有个地方判断是否进行循环依赖，有 allowCircularReferences的默认属性值为true
	4. 概述版
		1. ClassA 依赖 ClassB ClassB依赖ClassA 
		2. 先通过beanDefinition-A获得类属性，得到类，反射构造方法 得到A类,判断是否支持循环依赖，若支持，放入一个缓存中，缓存为map key:beanName  
		value:A类，注意是类，不是bean
		3. 往下走 填充A属性，需要ClassB，通过singletonObjects(通常认为的bean的单例池)获取，获取不到，判断是否b是创建过程中,不是，返回空，重复1
		4. 通过重复1，生成了一个ClassB，放入缓存，填充B属性，继续通过singletonObjects获取A,判断A是否为创建过程，是的，判断是否能从缓存中拿去A(为类)  
		，可以，填充A,删除缓冲中的A,继续往下走，生成beanB，回到A的生命周期，填充beanB
	5. 三重缓存（当到3时 方法为org.springframework.beans.factory.support.DefaultSingletonBeanRegistry#getSingleton(java.lang.String, boolean)）
		1. 一重 单例池
		2. 二重 反射构造方法生成的类 生成一个类的工厂的Map
		3. 三重 put一个从二级缓存中生产出来的一个对象 
		4. 代码流程为先判断缓存1是否有bean 没有且该bean在创建过程中，拿取缓存2，创建bean,放入缓存3，删除缓存2对应的bean,返回
		5. 三重缓存意义 是因为二重缓存为工厂，创建bean有可能十分复杂，防止资源浪费
	6. aop某个对象，则改变了这个对象，如classA bean的类为xxx.xxx.classA  aop后 为xxx.xxx.ClassA$$SpringCgibXXXXXXXX 
	7. new对象-》注入属性-》生命周期方法-》aop->创建对象
	8. 二重缓冲的意义：ClassA 依赖 ClassB 且均进行AOP 可通过6得知，aop过后就不是ClassB了，但是的确时B.因为在bean进行init时，经过后置处理器（如其中的aop）  
	后得到ClassB$$SpringCgibXXXXXXXX ，但是这只是类，而工厂中依旧可以生产原生的bean
	9. 循环依赖也不是完美的。假设B的初始化方法调用A中的某些方法，而A因为提前暴露，尚未出初始化，一旦调用了A中一些需要初始化以后才实例化的对象,  
	那么就会触发空指针异常。（如果没有提前暴露，则不会有问题）
	10. jdk 和 cg lib的动态代理，在spring中，接口用jdk,类使用cglib
	11. META-INFO下配置javax.servlet.ServletContainerInitializer文件，容器(tomcat..)会在启动时调用其中的类，servlet3.0提供得新特性
	    1. springWeb其中放的为SpringServletContainerInitializer 实现 ServletContainerInitializer
	    2. @HandlesTypes({WebApplicationInitializer.class})为Servlet3.0提供注解.里面为接口，该接口所有实现类会作为入参给onStartup方法
	    3. onStartup方法为ServletContainerInitializer定义的方法，在容器启动时调用
	12. dispatcherServlet启动时也会执行refresh()方法   
2. ## IOC
    1. Spring容器是Spring提供的个个组件，如bean工厂，后置处理器等等，这些组件合作为spring运行
    2. 如何产生一个对象
        1. new
        2. 反射  spring采用该手动产生对象
        3. clone
        4. 序列化
    3. <bean id="A",class="xxxxxx" depends-on="B"/> 则beanB则会再beanA前生成  
    4. 单例里，依赖多例B,B依旧是单例,因为A只初始化一次，想B为多例，再get上加@Lookup
    5. BeanFactory是spring中的顶级类，是bean的工厂，能过得bean。FactoryBean是一个接口，如果类实现了他，spring容器会有两个对象  
    一个是自身(名字是&+当前类对象的名字)，一个是getObject返回的对象(名字是当前类指定的名字)，最重要可以让我们自定义Bean的创建过程,
    许多框架的属性注入都运用到他
    6. 扫描思路:通过包路径获得项目下的所有文件，拿到class文件,反射通过文件名字得到对象，然后判断注解等等
    7. 生成bean流程 beanDefinition->准备实例化-beanPostProcessor->代理对象->init初始化方法->填充属性
3. ## AOP
    1. Aspectj 是一个面向切面得框架,它实现了AOP，Spring只是用了Aspectj得语法，底层还是用的spring自己得实现
    2. execution(切面表达式)可以定义到方法，精确度高，within()只能定义到类 
    3. jdk动态代理只能用接口,因为代理对象已经继承Proxy对象了      
    4. Aspectj是静态织入，编译期间织入，springAOP是动态得，运行期间织入
    5. 代理
        1. 静态代理，分为继承(可能产生代理类过多,聚合也会产生过多代理类，相对比聚合较多)和聚合(目标对象和代理对象通实现一个接口，代理对象中包含目标对象)
        2. 动态代理的一种思路:通过class反射进行字符串编写一个java文件,然后I/O生成,通过代码工具类(如:javaCompiler)编译，然后通过  
        类加载器，UrlClassLoader加载磁盘上的类，从而生成代理类,若 用的是聚合，生成类应该用构造方法，并传入参数
        3. JDK动态代理不生产文件，通过字节码等信息，向记录类信息的类中插入目标对象的信息，再通过其生产目标对象
    6. 
4. ## spring启动流程
    ``` 
    //实例化一个工厂DefaultListableBeanFactory
    org.springframework.context.support.GenericApplicationContext->GenericApplicationContext()
      	1、实力化一个AnnotatedBeanDefinitionReader
    	2、ClassPathBeanDefinitionScanner，能够扫描我们bd,能够扫描一个类，并且转换成bd
    	org.springframework.context.annotation.AnnotationConfigApplicationContext#AnnotationConfigApplicationContext()
    		委托AnnotationConfigUtils
    		org.springframework.context.annotation.AnnotatedBeanDefinitionReader#AnnotatedBeanDefinitionReader()
    			
    			org.springframework.context.annotation.AnnotationConfigUtils#registerAnnotationConfigProcessors()
    			
    				1、添加AnnotationAwareOrderComparator类的对象，主要去排序
    				2、ContextAnnotationAutowireCandidateResolver
    				3、往BeanDefinitionMap注册一个ConfigurationClassPostProcessor?  org.springframework.context.annotation.internalConfigurationAnnotationProcessor
    					why?因为需要在invokeBeanFactoryPostProcessors
    					invokeBeanFactoryPostProcessors主要是在spring的beanFactory初始化的过程中去做一些事情，怎么来做这些事情呢？
    					委托了多个实现了BeanDefinitionRegistryPostProcessor或者BeanFactoryProcessor接口的类来做这些事情,有自定义的也有spring内部的
    					其中ConfigurationClassPostProcessor就是一个spring内部的BeanDefinitionRegistryPostProcessor
    					因为如果你不添加这里就没有办法委托ConfigurationClassPostProcessor做一些功能
    					到底哪些功能？参考下面的注释
    				4、RequiredAnnotationBeanPostProcessor
    				.......
    				org.springframework.context.annotation.AnnotationConfigUtils#registerAnnotationConfigProcessors()
    					//往BeanDefinitionMap注册
    					org.springframework.context.annotation.AnnotationConfigUtils#registerPostProcessor
    						//准备好bean工厂，实例化对象
    						org.springframework.context.support.AbstractApplicationContext#refresh
    						//准备工作包括设置启动时间，是否激活标识位， 初始化属性源(property source)配置
    							org.springframework.context.support.AbstractApplicationContext#prepareRefresh
    								//得到beanFactory?因为需要对beanFactory进行设置
    								org.springframework.context.support.AbstractApplicationContext#obtainFreshBeanFactory
    									//准备bean工厂
    									1、添加一个类加载器
    									2、添加bean表达式解释器，为了能够让我们的beanFactory去解析bean表达式
    									3、添加一个后置处理器ApplicationContextAwareProcessor
    									4、添加了自动注入别忽略的列表
    									5、。。。。。。
    									6、添加了一个ApplicationListenerDetector后置处理器（自行百度）
    									org.springframework.context.support.AbstractApplicationContext#prepareBeanFactory
    										目前没有任何实现
    										org.springframework.context.support.AbstractApplicationContext#postProcessBeanFactory
    											1、getBeanFactoryPostProcessors()得到自己定义的（就是程序员自己写的，并且没有交给spring管理，就是没有加上@Component）
    											2、得到spring内部自己维护的BeanDefinitionRegistryPostProcessor
    											org.springframework.context.support.AbstractApplicationContext#invokeBeanFactoryPostProcessors
    												//调用这个方法
    												//循环所有的BeanDefinitionRegistryPostProcessor
    												//该方法内部postProcessor.postProcessBeanDefinitionRegistry
    												org.springframework.context.support.PostProcessorRegistrationDelegate#invokeBeanDefinitionRegistryPostProcessors
    													//调用扩展方法postProcessBeanDefinitionRegistry
    													org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor#postProcessBeanDefinitionRegistry
    														//拿出的所有bd，然后判断bd时候包含了@Configuration、@Import，@Compent。。。注解
    														org.springframework.context.annotation.ConfigurationClassPostProcessor#processConfigBeanDefinitions
    															1、的到bd当中描述的类的元数据（类的信息）
    															2、判断是不是加了@Configuration   metadata.isAnnotated(Configuration.class.getName())
    															3、如果加了@Configuration，添加到一个set当中,把这个set传给下面的方法去解析
    															org.springframework.context.annotation.ConfigurationClassUtils#checkConfigurationClassCandidate
    															//扫描包
    															
    															org.springframework.context.annotation.ConfigurationClassParser#parse(java.util.Set<org.springframework.beans.factory.config.BeanDefinitionHolder>)
    																
    																org.springframework.context.annotation.ConfigurationClassParser#parse(org.springframework.core.type.AnnotationMetadata, java.lang.String)
    																	//就行了一个类型封装
    																	org.springframework.context.annotation.ConfigurationClassParser#processConfigurationClass
    																	1、处理内部类 一般不会写内部类
    																	org.springframework.context.annotation.ConfigurationClassParser#doProcessConfigurationClass
    																		//解析扫描的一些基本信息，比如是否过滤，比如是否加入新的包。。。。。
    																		org.springframework.context.annotation.ComponentScanAnnotationParser#parse
    																			org.springframework.context.annotation.ClassPathBeanDefinitionScanner#doScan
    																			org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider#findCandidateComponents
    																				org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider#scanCandidateComponents
    ```     
5. ## 后置处理器
    1. @Configuration加上后 为全配置类,会标识为full，否则为lite,而走到后置处理器实现动态代理时，只有full才会进行cglib的动态代理
    2. 为什么实例化配置类时需要进行动态代理？因为假如在beanA初始化过程中需要走BeanB得构造方法得到B,永远是新得new B，代理则可以把工厂中存在得bean直接给与
    3. 实例化配置类时需要两次代理，第一次代理如上，第二次是因为实现乐FactoryBean 在实现了getObject()方法，返回对象时依旧不能new   
    4. BeanPostProcessor,插手bean的示例过程,在没有bean容器管理之前做事,经典aop
    5. BeanFactoryPostProcessor，在spring中任何一个bean在new之前执行,针对beanFactory，经典为对配置类下@bean操作时进行cglib
    6. BeanDefinitionRegistryPostProcessor,是BeanFactoryPostProcessor的子类,但是他(子类)先执行，源码时这样的流程 
    7. importSelector 通过selectImports返回一个类的全名，把他变成bd.    
6. ## MVC
    1. 在META-INFO有文件名javax.servlet.ServletContainerInitializer的配置,在里面配置全类名为org.springframework.web.SpringServletContainerInitializer的类，tomcat会在启动时候调用他,然后他上面有个注解@HandlesTypes(WebApplicationInitializer.class)，他会拿到实现他接口的全部类，然后在SpringServletContainerInitializer中循环调用拿到WebApplicationInitializer的onStartup方法
    2. ServletContainerInitializer.onStartup()获得ServletContext，往里面注入自己的Servlet方法(继承HttpServlet)重写doGet/doPost方法,这样请求就可以在里面完成  
    3. springBoot默认使用servlet3来实现上传,实现类为StandardMultipartHttpServletRequest 
    4. tomcat.addServlet(Context ctx,String servletName,Servlet servlet).setLoadOnStartUp(1) 会在tomcat启动时调用DispatcherServlet的父类HttpServletBean的init方法
    5. handlerMappings两个一个是用于implements Controller 里面 路径映射对象一个是用于@Controller 里面 路径映射方法
    6. 一个Http请求进来，进入DispatcherServlet的doService()
    7. 得到不同的Handle进行不通的处理->适配器模式
     