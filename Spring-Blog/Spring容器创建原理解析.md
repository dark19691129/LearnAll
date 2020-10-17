# 1，Spring容器创建原理解析

一切都需要从`refresh()`开始探究，接下来将一个一个的探究这些方法做了什么工作。

```java
	public void refresh() throws BeansException, IllegalStateException {
		synchronized (this.startupShutdownMonitor) {
            
			//刷新前的预处理工作
            prepareRefresh();

			
            //获取BeanFactory
			ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();

			
            //BeanFactory的预准备工作(BeanFactory的一些设置)
			prepareBeanFactory(beanFactory);

			try {
				
                //BeanFactory准备工作完成之后进行的后置处理
				postProcessBeanFactory(beanFactory);

                
                //===================以上就是BeanFacotry的预准备工作====================
                
				
				//执行BeanFactorypostProcessor
                invokeBeanFactoryPostProcessors(beanFactory);

				
                //注册BeanPostProcessor(Bean的后置处理器)【用来拦截Bean的创建过程】
				registerBeanPostProcessors(beanFactory);

				
                //初始化MessageSource组件，（做国际化功能；消息绑定；消息解析）
				initMessageSource();

				
                //初始化事件派发器
				initApplicationEventMulticaster();

				
                //留给子容器（子类）
				onRefresh();

				
                //给容器中将所有项目的ApplicationListener注册进来，
				registerListeners();

				
                //初始化所有剩下的单实例的bean
				finishBeanFactoryInitialization(beanFactory);

				
                //完成BeanFactory的初始化创建工作：IOC容器就创建完成
				finishRefresh();
			}

			catch (BeansException ex) {
				
                ......
                
			}

			finally {
				
                ......
                
			}
		}
	}
```







# 2，过程

## 2.1，prepareRefresh();

```java
protected void prepareRefresh() {
    this.scanner.clearCache();
    super.prepareRefresh();
}

//点进super.prepareRefresh()
//=============================================
protected void prepareRefresh() {
    //记录时间
    this.startupDate = System.currentTimeMillis();
    //容器是否关闭了
    this.closed.set(false);
    //容器是否激活了
    this.active.set(true);

    if (logger.isInfoEnabled()) {
		//打印容器刷新日志
        //此时控制台会打印：
        /*
        信息: Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@fad74ee: startup date [Thu Aug 20 11:17:59 GMT+08:00 2020]; root of context hierarchy
        */
        logger.info("Refreshing " + this);
    }

    //初始化一些属性设置。这个方法是留给继承了AnnotationConfigApplicationContext的子类重写该方法并进行一些操作。
    initPropertySources();

    //进行上一方法的自定义属性的校验
    getEnvironment().validateRequiredProperties();

    //来保存容器中早期存在的一些事件
    this.earlyApplicationEvents = new LinkedHashSet<ApplicationEvent>();
}
```







## 2.2，obtainFreshBeanFactory()

继续debug运行下去

查看它的源代码

```java
protected ConfigurableListableBeanFactory obtainFreshBeanFactory() {
    //刷新BeanFactory()【实际上也包含了创建BeanFactory】
    //接下来就查看refreshBeanFactory()
    refreshBeanFactory();
    //获取BeanFactory
    ConfigurableListableBeanFactory beanFactory = getBeanFactory();
    if (logger.isDebugEnabled()) {
        logger.debug("Bean factory for " + getDisplayName() + ": " + beanFactory);
    }
    //返回刚才创建的BeanFactory
    return beanFactory;
}
```



> refreshBeanFactory()

```java
protected final void refreshBeanFactory() throws IllegalStateException {
    if (!this.refreshed.compareAndSet(false, true)) {
        throw new IllegalStateException(
            "GenericApplicationContext does not support multiple refresh attempts: just call 'refresh' once");
    }
    //这里是给这个BeanFactory设置了一个序列化ID，实际上在这一步之前，就创建好了一个BeanFactory
    /*
    public GenericApplicationContext() {
		this.beanFactory = new DefaultListableBeanFactory();
	}
    */
    this.beanFactory.setSerializationId(getId());
}
```

也就是说：`obtainFreshBeanFactory()`就是创建并刷新BeanFactory并进行返回。



## 2.3，prepareBeanFactory(beanFactory)

继续debug运行，查看代码

```java
protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {
    //设置类加载器
    beanFactory.setBeanClassLoader(getClassLoader());
    //设置表达式解析器
    beanFactory.setBeanExpressionResolver(new StandardBeanExpressionResolver(beanFactory.getBeanClassLoader()));
    beanFactory.addPropertyEditorRegistrar(new ResourceEditorRegistrar(this, getEnvironment()));

    //添加一个ApplicationContextAwareProcessor的后置处理器
    beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
    
    //设置忽略的自动装配的接口--设置这些接口的实现类不能通过接口类型进行自动注入
    beanFactory.ignoreDependencyInterface(EnvironmentAware.class);
    beanFactory.ignoreDependencyInterface(EmbeddedValueResolverAware.class);
    beanFactory.ignoreDependencyInterface(ResourceLoaderAware.class);
    beanFactory.ignoreDependencyInterface(ApplicationEventPublisherAware.class);
    beanFactory.ignoreDependencyInterface(MessageSourceAware.class);
    beanFactory.ignoreDependencyInterface(ApplicationContextAware.class);

    //注册可以解析的自动装配，我们可以直接在任何组件中进行自动注入
    //可以在组件中自动注入这些东西
    beanFactory.registerResolvableDependency(BeanFactory.class, beanFactory);
    beanFactory.registerResolvableDependency(ResourceLoader.class, this);
    beanFactory.registerResolvableDependency(ApplicationEventPublisher.class, this);
    beanFactory.registerResolvableDependency(ApplicationContext.class, this);

    //添加ApplicationListenerDetector这个类型的后置处理器
    beanFactory.addBeanPostProcessor(new ApplicationListenerDetector(this));

    //添加编译时的AspectJ支持
    if (beanFactory.containsBean(LOAD_TIME_WEAVER_BEAN_NAME)) {
        beanFactory.addBeanPostProcessor(new LoadTimeWeaverAwareProcessor(beanFactory));
        // Set a temporary ClassLoader for type matching.
        beanFactory.setTempClassLoader(new ContextTypeMatchClassLoader(beanFactory.getBeanClassLoader()));
    }

    //给容器中注册一些组件
    if (!beanFactory.containsLocalBean(ENVIRONMENT_BEAN_NAME)) {
        beanFactory.registerSingleton(ENVIRONMENT_BEAN_NAME, getEnvironment());
    }
    if (!beanFactory.containsLocalBean(SYSTEM_PROPERTIES_BEAN_NAME)) {
        beanFactory.registerSingleton(SYSTEM_PROPERTIES_BEAN_NAME, getEnvironment().getSystemProperties());
    }
    if (!beanFactory.containsLocalBean(SYSTEM_ENVIRONMENT_BEAN_NAME)) {
        beanFactory.registerSingleton(SYSTEM_ENVIRONMENT_BEAN_NAME, getEnvironment().getSystemEnvironment());
    }
}
```

这些就是BeanFactory要做的预准备工作







## 2.4，postProcessBeanFactory(beanFactory)

显而易见，这个就是在BeanFactory准备完成后进行的后置处理工作

```java
//这个实际上是交由继承了AnnotationConfigApplicationContext的子类，重写这个方法，在BeanFactory创建并预准备完成后进行一些相应的操作
protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
}
```





## 2.5，invokeBeanFactoryPostProcessors(beanFactory)

继续debug



```java
//执行BeanFactoryPostProcessor
//BeanFactoryPostProcessor是在BeanFactory标准初始化之后执行的
protected void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
    
    //点击查看invokBeanFactoryPostProcessor的代码
    PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(beanFactory, getBeanFactoryPostProcessors());

    // Detect a LoadTimeWeaver and prepare for weaving, if found in the meantime
    // (e.g. through an @Bean method registered by ConfigurationClassPostProcessor)
    if (beanFactory.getTempClassLoader() == null && beanFactory.containsBean(LOAD_TIME_WEAVER_BEAN_NAME)) {
        beanFactory.addBeanPostProcessor(new LoadTimeWeaverAwareProcessor(beanFactory));
        beanFactory.setTempClassLoader(new ContextTypeMatchClassLoader(beanFactory.getBeanClassLoader()));
    }
}
```



> （1）invokeBeanFactoryPostProcessors(beanFactory, getBeanFactoryPostProcessors())

执行BeanFactory的PostProcessor方法

```java
public static void invokeBeanFactoryPostProcessors(
    ConfigurableListableBeanFactory beanFactory, List<BeanFactoryPostProcessor> beanFactoryPostProcessors) {

    
    
    //【1】先执行BeanDefinitionRegistryPostProcessor的方法
    // Invoke BeanDefinitionRegistryPostProcessors first, if any.
    Set<String> processedBeans = new HashSet<String>();

    //先判断beanFactory是不是BeanDefinitionRegistry这个接口的
    if (beanFactory instanceof BeanDefinitionRegistry) {
        
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
        
        //创建用于存放BeanFactoryPostProcessor的LinkedList
        List<BeanFactoryPostProcessor> regularPostProcessors = new LinkedList<BeanFactoryPostProcessor>();
        
        //创建用于存放BeanDefinitionRegistryPostProcessor的LinkedList
        List<BeanDefinitionRegistryPostProcessor> registryProcessors = new LinkedList<BeanDefinitionRegistryPostProcessor>();

        //这里会拿到所有的BeanFactoryPostProcessor并进行遍历
        for (BeanFactoryPostProcessor postProcessor : beanFactoryPostProcessors) {
            
            //如果BeanFactoryPostProcessor是BeanDefinitionRegistryPostProcessor这个接口的话
            if (postProcessor instanceof BeanDefinitionRegistryPostProcessor) {
                BeanDefinitionRegistryPostProcessor registryProcessor =
                    (BeanDefinitionRegistryPostProcessor) postProcessor;
                registryProcessor.postProcessBeanDefinitionRegistry(registry);
                
                //添加到存放BeanDefinitionRegistryPostProcessor的LinkedList中
                registryProcessors.add(registryProcessor);
            }
            else {
                ////添加到存放BeanDefinitionRegistryPostProcessor的LinkedList中
                regularPostProcessors.add(postProcessor);
            }
        }

        //创建存储BeanDefinitionRegistryPostProcessor的List
        List<BeanDefinitionRegistryPostProcessor> currentRegistryProcessors = new ArrayList<BeanDefinitionRegistryPostProcessor>();

        //首先，从BeanFactory中拿到所有BeanDefinitionRetistryPostProcessor
        String[] postProcessorNames =
            beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
        
        //遍历这些BeanDefinitionRegistryPostProcessor
        for (String ppName : postProcessorNames) {
            
            //如果是实现了PriorityOrdered这个顺序接口的话
            //也就是所谓的优先级排序
            if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
                
                //添加到List当中
                currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
                processedBeans.add(ppName);
            }
        }
        
        //排序
        sortPostProcessors(currentRegistryProcessors, beanFactory);
        registryProcessors.addAll(currentRegistryProcessors);
        
        //执行BeanDefinitionRegistry的后置处理方法
        //这一步其实就是：
        //postProcessor.postProcessBeanDefinitionRegistry(registry);
        invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
        currentRegistryProcessors.clear();

        
        
        //接下来拿到BeanDefinitionRegistryPostProcessor
        postProcessorNames = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
        
        //遍历一下
        for (String ppName : postProcessorNames) {
            
            //如果是实现了Ordered接口的话
            if (!processedBeans.contains(ppName) && beanFactory.isTypeMatch(ppName, Ordered.class)) {
                
                //添加
                currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
                processedBeans.add(ppName);
            }
        }
        
        //排序
        sortPostProcessors(currentRegistryProcessors, beanFactory);
        registryProcessors.addAll(currentRegistryProcessors);
        
        //执行执行BeanDefinitionRegistry的后置处理方法
        //postProcessor.postProcessBeanDefinitionRegistry(registry);
        invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
        currentRegistryProcessors.clear();

        //最后，执行没有实现任何接口的BeanDefinitionRegistryPostProcessor
        boolean reiterate = true;
        while (reiterate) {
            reiterate = false;
            
            //拿到BeanDefinitionRegistryPostProcessor
            postProcessorNames = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
            
            //遍历
            for (String ppName : postProcessorNames) {
                
                //没有实现任何优先级接口的
                if (!processedBeans.contains(ppName)) {
                    currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
                    processedBeans.add(ppName);
                    reiterate = true;
                }
            }
            
            //排序
            sortPostProcessors(currentRegistryProcessors, beanFactory);
            registryProcessors.addAll(currentRegistryProcessors);
            
            //执行它们的BeanDefinitionPostProcessor
            //postProcessor.postProcessBeanDefinitionRegistry(registry);
            invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
            currentRegistryProcessors.clear();
        }

        
        
        //如果在这些BeanDefinitionRegistryPostProcessor中，还重写了父类的BeanFactoryPostProcessor方法的话，就在这里执行BeanFactoryPostProcessor的方法
        invokeBeanFactoryPostProcessors(registryProcessors, beanFactory);
        invokeBeanFactoryPostProcessors(regularPostProcessors, beanFactory);
    }

    else {
        //如果判断这个beanFactory不是实现了BeanDefinitionRegistryPostProcessor的话
        //又重写BeanFactoryPostProcessor方法的话就执行
        invokeBeanFactoryPostProcessors(beanFactoryPostProcessors, beanFactory);
    }

    
//===========================华丽的分界线===============================
    
    
    //【2】再执行BeanFactoryPostProcessor的方法
    //拿到所有的beanFactoryPostProcessor
    String[] postProcessorNames =
        beanFactory.getBeanNamesForType(BeanFactoryPostProcessor.class, true, false);

    //大体逻辑和上面的一样
    //这里还是分离那些实现了PriorityOrdered和Ordered或者没有实现任何接口的BeanFactoryPostProcessor
    //创建三个ArrayList，分别存放实现了PriorityOrdered，Ordered，没有实现接口的BeanFactoryPostProcessorNames
    List<BeanFactoryPostProcessor> priorityOrderedPostProcessors = new ArrayList<BeanFactoryPostProcessor>();
    List<String> orderedPostProcessorNames = new ArrayList<String>();
    List<String> nonOrderedPostProcessorNames = new ArrayList<String>();
    
    //遍历BeanFactoryPostProcessor
    for (String ppName : postProcessorNames) {
        if (processedBeans.contains(ppName)) {
            // skip - already processed in first phase above
        }
        //如果是实现了PriorityOrdered接口的话
        else if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
            
            //添加到这个ArrayList中
            priorityOrderedPostProcessors.add(beanFactory.getBean(ppName, BeanFactoryPostProcessor.class));
        }
        //如果是实现了Ordered接口的话
        else if (beanFactory.isTypeMatch(ppName, Ordered.class)) {
            
            //添加到这个ArrayList中
            orderedPostProcessorNames.add(ppName);
        }
        //没有实现任何接口的
        else {
            
            //添加到这个ArrayList中
            nonOrderedPostProcessorNames.add(ppName);
        }
    }

    //首先，执行实现了PriorityOrdered接口的BeanFactoryPostProcessor的方法
    //排序
    sortPostProcessors(priorityOrderedPostProcessors, beanFactory);
    //执行BeanFactoryPostProcessor
    invokeBeanFactoryPostProcessors(priorityOrderedPostProcessors, beanFactory);

    //创建一个存放BeanFactoryPostProcessor的ArrayList
    List<BeanFactoryPostProcessor> orderedPostProcessors = new ArrayList<BeanFactoryPostProcessor>();
    
    //遍历
    for (String postProcessorName : orderedPostProcessorNames) {
        orderedPostProcessors.add(beanFactory.getBean(postProcessorName, BeanFactoryPostProcessor.class));
    }
    
    //排序
    sortPostProcessors(orderedPostProcessors, beanFactory);
    //然后，执行实现了Ordered接口的BeanFactoryPostProcessor的方法
    invokeBeanFactoryPostProcessors(orderedPostProcessors, beanFactory);

    //最后，执行那些没有实现任何接口的BeanFactoryPostProcessor的方法
    List<BeanFactoryPostProcessor> nonOrderedPostProcessors = new ArrayList<BeanFactoryPostProcessor>();
    //遍历
    for (String postProcessorName : nonOrderedPostProcessorNames) {
        //添加
        nonOrderedPostProcessors.add(beanFactory.getBean(postProcessorName, BeanFactoryPostProcessor.class));
    }
    //执行方法
    invokeBeanFactoryPostProcessors(nonOrderedPostProcessors, beanFactory);

    // Clear cached merged bean definitions since the post-processors might have
    // modified the original metadata, e.g. replacing placeholders in values...
    beanFactory.clearMetadataCache();
}
```





## 2.6，registerBeanPostProcessors(beanFactory);

注册Bean的后置处理器

这些后置处理器是用来拦截Bean的创建过程

注意：有不同类型的BeanPostProcessor，它们在Bean创建前后的执行时机是不一样的。

```java
protected void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
    PostProcessorRegistrationDelegate.registerBeanPostProcessors(beanFactory, this);
}

//继续点进去查看代码
public static void registerBeanPostProcessors(
    ConfigurableListableBeanFactory beanFactory, AbstractApplicationContext applicationContext) {

    //先拿到所有的BeanPostProcessor
    String[] postProcessorNames = beanFactory.getBeanNamesForType(BeanPostProcessor.class, true, false);

    
    int beanProcessorTargetCount = beanFactory.getBeanPostProcessorCount() + 1 + postProcessorNames.length;
    beanFactory.addBeanPostProcessor(new BeanPostProcessorChecker(beanFactory, beanProcessorTargetCount));

    
    
    //获取所有的BeanPostProcessor，并进行分离--》也就是实现了PriorityOrdered，Ordered和没有实现接口的
    //创建ArrayList
    List<BeanPostProcessor> priorityOrderedPostProcessors = new ArrayList<BeanPostProcessor>();
    List<BeanPostProcessor> internalPostProcessors = new ArrayList<BeanPostProcessor>();
    List<String> orderedPostProcessorNames = new ArrayList<String>();
    List<String> nonOrderedPostProcessorNames = new ArrayList<String>();
    
    //遍历
    for (String ppName : postProcessorNames) {
        //如果是实现了PriorityOrdered接口的
        if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
            BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
            priorityOrderedPostProcessors.add(pp);
            //如果是实现了MergedBeanDefinitionPostProcessor接口的
            if (pp instanceof MergedBeanDefinitionPostProcessor) {
                internalPostProcessors.add(pp);
            }
        }
        //如果是实现了Ordered接口的
        else if (beanFactory.isTypeMatch(ppName, Ordered.class)) {
            orderedPostProcessorNames.add(ppName);
        }
        //没有实现任何接口的
        else {
            nonOrderedPostProcessorNames.add(ppName);
        }
    }

    //首先，注册那些实现了PriorityOrdered接口的
    sortPostProcessors(priorityOrderedPostProcessors, beanFactory);
    registerBeanPostProcessors(beanFactory, priorityOrderedPostProcessors);

    
    List<BeanPostProcessor> orderedPostProcessors = new ArrayList<BeanPostProcessor>();
    //遍历
    for (String ppName : orderedPostProcessorNames) {
        BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
        orderedPostProcessors.add(pp);
        //如果是实现了MergedBeanDefinitionPostProcessor接口的话
        if (pp instanceof MergedBeanDefinitionPostProcessor) {
            internalPostProcessors.add(pp);
        }
    }
    //然后，注册实现了Ordered接口的
    sortPostProcessors(orderedPostProcessors, beanFactory);
    registerBeanPostProcessors(beanFactory, orderedPostProcessors);

    
    List<BeanPostProcessor> nonOrderedPostProcessors = new ArrayList<BeanPostProcessor>();
    //遍历
    for (String ppName : nonOrderedPostProcessorNames) {
        
        BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
        nonOrderedPostProcessors.add(pp);
        //如果是实现了MergedBeanDefinitionPostProcessor接口的话
        if (pp instanceof MergedBeanDefinitionPostProcessor) {
            internalPostProcessors.add(pp);
        }
    }
    //注册那些没有实现任何接口的BeanPostProcessor
    registerBeanPostProcessors(beanFactory, nonOrderedPostProcessors);

    //最后注册那些MergedBeanDefinitionPostProcessor
    sortPostProcessors(internalPostProcessors, beanFactory);
    registerBeanPostProcessors(beanFactory, internalPostProcessors);

    //注册一个ApplicationListenerDetector，在Bean创建前后检查是否是ApplicationListener，如果是，就保存到容器中，
    beanFactory.addBeanPostProcessor(new ApplicationListenerDetector(applicationContext));
}
```

注意：这里只是在进行BeanPostProcessor的注册，而非方法的执行！





## 2.7，initMessageSource();

初始化MessageSource组件（主要是用来做国际化功能，消息绑定，消息解析的）

debug进去，查看代码

```java
protected void initMessageSource() {
    //获取BeanFactory
    ConfigurableListableBeanFactory beanFactory = getBeanFactory();
    
    //看容器中是否有ID为messageSource，类型为MeassageSource的组件
    if (beanFactory.containsLocalBean(MESSAGE_SOURCE_BEAN_NAME)) {
        
        //有的话，就进行获取
        this.messageSource = beanFactory.getBean(MESSAGE_SOURCE_BEAN_NAME, MessageSource.class);
        
        
        ......
        
    }
    //没有的话
    else {
        //就在这里new一个默认的出来
        DelegatingMessageSource dms = new DelegatingMessageSource();
        dms.setParentMessageSource(getInternalParentMessageSource());
        this.messageSource = dms;
        
        //将new出来的消息服务组件【DelegatingMessageSource】注册到容器当中
        beanFactory.registerSingleton(MESSAGE_SOURCE_BEAN_NAME, this.messageSource);
        if (logger.isDebugEnabled()) {
            logger.debug("Unable to locate MessageSource with name '" + MESSAGE_SOURCE_BEAN_NAME +
                         "': using default [" + this.messageSource + "]");
        }
    }
}
```

​		



​	

## 2.8，initApplicationEventMulticaster();

继续debug

这个方法是用来初始化容器事件派发器的

```java
protected void initApplicationEventMulticaster() {
    
    //获取BeanFactory
    ConfigurableListableBeanFactory beanFactory = getBeanFactory();
    
    //先从容器中获取ApplicationListenerEventMulticaster
    //如果没有配置
    if (beanFactory.containsLocalBean(APPLICATION_EVENT_MULTICASTER_BEAN_NAME)) {
        this.applicationEventMulticaster =
            beanFactory.getBean(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, ApplicationEventMulticaster.class);
        
        ......
        
    }
    
    //没有配置的话，就会在这里自己创建一个
    else {
        this.applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
        
        //并进行注册
        beanFactory.registerSingleton(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, this.applicationEventMulticaster);
        
        ......
        
    }
}
```



​			

## 2.9，onRefresh();

继续debug

```java
//这是留给子类的，让子类可以在容器刷新的时候，自己进行一些操作
protected void onRefresh() throws BeansException {
    // For subclasses: do nothing by default.
}
```

​			

## 2.10，registerListeners();

将项目里面所有的ApplicationListener注册进来

```java
protected void registerListeners() {
    
    //拿到所有的ApplicationLisener
    for (ApplicationListener<?> listener : getApplicationListeners()) {
        //并添加到事件派发器当中
        getApplicationEventMulticaster().addApplicationListener(listener);
    }

    //拿到所有的ApplicationListener
    String[] listenerBeanNames = getBeanNamesForType(ApplicationListener.class, true, false);
    //遍历
    for (String listenerBeanName : listenerBeanNames) {
        //添加到事件派发器当中，也就是注册这些Listener
        getApplicationEventMulticaster().addApplicationListenerBean(listenerBeanName);
    }

    //派发之前步骤所产生的事件
    Set<ApplicationEvent> earlyEventsToProcess = this.earlyApplicationEvents;
    this.earlyApplicationEvents = null;
    if (earlyEventsToProcess != null) {
        for (ApplicationEvent earlyEvent : earlyEventsToProcess) {
            //进行派发
            getApplicationEventMulticaster().multicastEvent(earlyEvent);
        }
    }
}
```



​				

## 2.11，finishBeanFactoryInitialization(beanFactory)

==注意：这一步非常重要--》初始化所有剩下的单实例Bean==

```java
protected void finishBeanFactoryInitialization(ConfigurableListableBeanFactory beanFactory) {
    
    
    ......
    

    //初始化所有剩下的单实例bean
    beanFactory.preInstantiateSingletons();
}
```

step into进去查看代码

```java
public void preInstantiateSingletons() throws BeansException {
    if (this.logger.isDebugEnabled()) {
        this.logger.debug("Pre-instantiating singletons in " + this);
    }

    //拿到容器中所有的bean的定义信息
    List<String> beanNames = new ArrayList<String>(this.beanDefinitionNames);

    //遍历
    for (String beanName : beanNames) {
        //拿到bean的定义信息
        RootBeanDefinition bd = getMergedLocalBeanDefinition(beanName);
        //查看bean是否是非抽象的，单实例的，非懒加载的
        if (!bd.isAbstract() && bd.isSingleton() && !bd.isLazyInit()) {
            //这里会判断这个bean是否是实现了factoryBean接口的
            //如果是，就会用工厂方式创建bean
            if (isFactoryBean(beanName)) {
                final FactoryBean<?> factory = (FactoryBean<?>) getBean(FACTORY_BEAN_PREFIX + beanName);
                boolean isEagerInit;
                if (System.getSecurityManager() != null && factory instanceof SmartFactoryBean) {
                    
                    .......
                    
                }
                else {
                    
                    
                    ......
                    
                    
                }
                if (isEagerInit) {
                    getBean(beanName);
                }
            }
            //如果不是，就会在这里创建bean
            else {
                getBean(beanName);
            }
        }
    }

    //在所有的bean都创建完之后
    for (String beanName : beanNames) {
        //拿到所有的单实例的bean
        Object singletonInstance = getSingleton(beanName);
        //如果是实现了SmartInitializingSingleton接口的话
        if (singletonInstance instanceof SmartInitializingSingleton) {
            //如果是的话
            final SmartInitializingSingleton smartSingleton = (SmartInitializingSingleton) singletonInstance;
            
            if (System.getSecurityManager() != null) {
                AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    @Override
                    public Object run() {
                        //如果是实现SmartInitializingsingleton接口的话，就执行afterSingletonsInstantiated方法
                        smartSingleton.afterSingletonsInstantiated();
                        return null;
                    }
                }, getAccessControlContext());
            }
            else {
                //如果是实现SmartInitializingsingleton接口的话，就执行afterSingletonsInstantiated方法
                smartSingleton.afterSingletonsInstantiated();
            }
        }
    }
}
```



> getBean()

在这里会深入的探究一下getBean()做了什么

```java
protected <T> T doGetBean(
    final String name, final Class<T> requiredType, final Object[] args, boolean typeCheckOnly)
    throws BeansException {

    final String beanName = transformedBeanName(name);
    Object bean;

    //这里是拿到所有被缓存起来的单实例的bean
    Object sharedInstance = getSingleton(beanName);
    if (sharedInstance != null && args == null) {
        
        
        ......
        
        
    }

    //第一次拿的时候，单实例缓存中是空的，所以会到这一步 
    else {
        
        if (isPrototypeCurrentlyInCreation(beanName)) {
            throw new BeanCurrentlyInCreationException(beanName);
        }

        //这里是查看有无父工厂
        BeanFactory parentBeanFactory = getParentBeanFactory();
        if (parentBeanFactory != null && !containsBeanDefinition(beanName)) {
            
            
            ......
            
            
        }

        if (!typeCheckOnly) {
            //先来标记当前bean已经被创建
            //【防止多线程的时候，创建出来bean不是单实例的】
            markBeanAsCreated(beanName);
        }

        try {
            //拿到bean的定义信息
            final RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
            checkMergedBeanDefinition(mbd, beanName, args);

            
            
            //拿到当前bean所依赖的其他bean！
            //xml配置文件配置bean的时候--》depends-on属性 在这里有所体现
            String[] dependsOn = mbd.getDependsOn();
            //如果有依赖的bean，就在这里先创建出来
            if (dependsOn != null) {
                for (String dep : dependsOn) {
                    
                    ......
                    
                    registerDependentBean(dep, beanName);
                    getBean(dep);
                }
            }

            
            
            //如果是单实例的
            if (mbd.isSingleton()) {
                
                //这里会使用Factory下的getObject来进行Bean的创建
                sharedInstance = getSingleton(beanName, new ObjectFactory<Object>() {
                    @Override
                    public Object getObject() throws BeansException {
                        try {
                            //这一步会涉及到多个后置处理器的方法执行
                            //就不进行深入研究了
                            return createBean(beanName, mbd, args);
                        }
                        catch (BeansException ex) {
                            
                            
                            ......
                            
                        }
                    }
                });
                bean = getObjectForBeanInstance(sharedInstance, name, beanName, mbd);
            }

            
            ......
            
            
    return (T) bean;
}
```

至此，`finishBeanFactoryInitialization(beanFactory)`就执行完毕了





## 2.12，finishRefresh();

完成BeanFactory的初始化创建工作；IOC容器就创建完成了

```java
protected void finishRefresh() {
    //初始化和生命周期有关的处理器	
    //这里允许我们实现一个LifeCycleProcessor的实现类，可以在BeanFactory执行到相应的生命周期时，进行相应的操作
    initLifecycleProcessor();

    //拿到前面定义的生命周期的处理器，回调onRefresh()
    getLifecycleProcessor().onRefresh();

    //发布容器刷新完成的事件
    publishEvent(new ContextRefreshedEvent(this));

    //可以不关心
    LiveBeansView.registerApplicationContext(this);
}
```







## 2.13，总结

> （1）Spring容器在启动的时候，先会保存所有注册进来的Bean的定义信息
> 	（1.1）xml注册Bean
> 	（1.2）注解注册Bean：@Service,@Component,@Bean
> （2）Spring容器会在合适的时机创建这些Bean
> 	创建Ban的时机：
> 	（2.1）用到这个bean的时候，利用getBean()创建bean，创建好之后就保存在容器之中
> 	（2.2）统一创建剩下的所有的bean的时候：finishBeanFactoryInitialization()
> （3）后置处理器的思想非常重要
> 	（3.1）每一个bean创建完成都会使用各种后置处理器进行处理，来增强bean的功能
> 		例如：AutowiredAnnotationBeanPostProcessor:专门用来处理自动注入的
> 		……



























