```java

//refresh方法
finishBeanFactoryInitialization(beanFactory);

//里面有这么一个方法
beanFactory.preInstantiateSingletons();

//点进去看，这个是实例化单实例Bean之前要做的操作
public void preInstantiateSingletons() throws BeansException {
    if (this.logger.isDebugEnabled()) {
        this.logger.debug("Pre-instantiating singletons in " + this);
    }

    // Iterate over a copy to allow for init methods which in turn register new bean definitions.
    // While this may not be part of the regular factory bootstrap, it does otherwise work fine.
	//拿到容器中的bean的定义信息
    List<String> beanNames = new ArrayList<String>(this.beanDefinitionNames);

    // Trigger initialization of all non-lazy singleton beans...
	//遍历所有Bean的定义信息，依次用getBean(beanName)创建对象
    for (String beanName : beanNames) {
        RootBeanDefinition bd = getMergedLocalBeanDefinition(beanName);
        if (!bd.isAbstract() && bd.isSingleton() && !bd.isLazyInit()) {
            if (isFactoryBean(beanName)) {
				
                ......
                
                if (isEagerInit) {
                    getBean(beanName);
                }
            }
            else {
                getBean(beanName);
            }
        }
    }


    ...
}

//===================================================================================

//getBean()->doGetBean()->getSingleton()
//那么就来看看doGetBean()
protected <T> T doGetBean(
    final String name, final Class<T> requiredType, final Object[] args, boolean typeCheckOnly)
    throws BeansException {

    final String beanName = transformedBeanName(name);
    Object bean;

    // Eagerly check singleton cache for manually registered singletons.
    //会试图去缓存中获取当前要创建的bean，但最初创建的时候，容器是没有这个bean的，所以就会去创建
    Object sharedInstance = getSingleton(beanName);
    if (sharedInstance != null && args == null) {
        if (logger.isDebugEnabled()) {
            if (isSingletonCurrentlyInCreation(beanName)) {
                logger.debug("Returning eagerly cached instance of singleton bean '" + beanName +
                             "' that is not fully initialized yet - a consequence of a circular reference");
            }
            else {
                logger.debug("Returning cached instance of singleton bean '" + beanName + "'");
            }
        }
        bean = getObjectForBeanInstance(sharedInstance, name, beanName, null);
    }

    else {
		
        ......

        try {

            ......

            // Create bean instance.
            //创建bean的实例
            if (mbd.isSingleton()) {
                sharedInstance = getSingleton(beanName, new ObjectFactory<Object>() {
                    @Override
                    public Object getObject() throws BeansException {
                        try {
                            return createBean(beanName, mbd, args);
                        }
                        catch (BeansException ex) {
                            // Explicitly remove instance from singleton cache: It might have been put there
                            // eagerly by the creation process, to allow for circular reference resolution.
                            // Also remove any beans that received a temporary reference to the bean.
                            destroySingleton(beanName);
                            throw ex;
                        }
                    }
                });
                bean = getObjectForBeanInstance(sharedInstance, name, beanName, mbd);
            }

           	...
                
                
        }
        catch (BeansException ex) {
            cleanupAfterBeanCreationFailure(beanName);
            throw ex;
        }
    }
	
    ......
        
        
}


//===================================================================================

//接下来研究一下creatBean(),创建bean
protected Object createBean(String beanName, RootBeanDefinition mbd, Object[] args) throws BeanCreationException {
    if (logger.isDebugEnabled()) {
        logger.debug("Creating instance of bean '" + beanName + "'");
    }
    
    //拿到bean的定义信息
    RootBeanDefinition mbdToUse = mbd;


    ......
        

    try {
        // Give BeanPostProcessors a chance to return a proxy instead of the target bean instance.
        //这一步，希望后置处理器能在此返回一个代理对象！！！
        //如果能返回代理对象，就返回回去，如果不能就调用下面的doCreateBean(beanName, mbdToUse, args);
        //所以会先调用resolveBeforeInstantiation试着返回一个代理对象回来
        Object bean = resolveBeforeInstantiation(beanName, mbdToUse);
        if (bean != null) {
            return bean;
        }
    }
    catch (Throwable ex) {
        throw new BeanCreationException(mbdToUse.getResourceDescription(), beanName,
                                        "BeanPostProcessor before instantiation of bean failed", ex);
    }
	
    //如果不能返回代理对象，就创建bean
    //这一步才是真正的去创建bean的实例
    Object beanInstance = doCreateBean(beanName, mbdToUse, args);
    if (logger.isDebugEnabled()) {
        logger.debug("Finished creating instance of bean '" + beanName + "'");
    }
    return beanInstance;
}

//===================================================================================

//因为debug运行到这里之后，会进行解析，尝试返回一个代理对象
//由于doCreatBean()在此之前已经介绍过了，所以不加赘述
//下面继续研究resolveBeforeInstantiation(beanName, mbdToUse);
//看看他是怎么返回一个代理对象的
protected Object resolveBeforeInstantiation(String beanName, RootBeanDefinition mbd) {
    Object bean = null;
    
    //查看是否被解析过了
    if (!Boolean.FALSE.equals(mbd.beforeInstantiationResolved)) {
        // Make sure bean class is actually resolved at this point.
        if (!mbd.isSynthetic() && hasInstantiationAwareBeanPostProcessors()) {
            Class<?> targetType = determineTargetType(beanName, mbd);
       
            
            //主要关注这里......
            if (targetType != null) {
                bean = applyBeanPostProcessorsBeforeInstantiation(targetType, beanName);
                if (bean != null) {
                    bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
                }
            }
            
            
            
        } 
        mbd.beforeInstantiationResolved = (bean != null);
    }
    return bean;
}


//我们点进去applyBeanPostProcessorsBeforeInstantiation，
//它会去查看是否是InstantiationAwareBeanPostProcessor接口的，是的话就执行postProcessBeforeInstantiation()
protected Object applyBeanPostProcessorsBeforeInstantiation(Class<?> beanClass, String beanName) {
    
    //拿到所有的后置处理器！！！
    //当拿到实现了InstantiationAwareBeanPostProcessor接口的后置处理器，就会进行方法的调用
    for (BeanPostProcessor bp : getBeanPostProcessors()) {
        
        //判断是否是InstantiationAwareBeanPostProcessor接口的
        if (bp instanceof InstantiationAwareBeanPostProcessor) {
            InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor) bp;
            
            //是的话，就去执行InstantiationAwareBeanPostProcessor的postProcessBeforeInstantiation()
            Object result = ibp.postProcessBeforeInstantiation(beanClass, beanName);
            if (result != null) {
                return result;
            }
        }
    }
    return null;
}

//===================================================================================

//这里值得注意的是，InstantiationAwareBeanPostProcessor与BeanPostProcessor是不同的后置处理器
//我们可以查看源码
//InstantiationAwareBeanPostProcessor的源代码
public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {

	Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException;

	boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException;

    ...

}

//BeanPostProcessor的源代码
public interface BeanPostProcessor {

	Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException;

	Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException;

}

//也就是说，这里涉及了两个不同的后置处理器，
//【InstantiationAwareBeanPostProcessor】是在创建bean实例之前，尝试用后置处理器返回对象的
//【BeanPostProcessor】是在Bean对象创建完成初始化前后调用的
//而上一章讲过的AnnotationAwareAspectJAutoProxyCreator是InstantiationAwareBeanPostProcessor类型的后置处理器，所以它会在任何bean创建之前进行拦截，会调用postProcessBeforeInstantiation()！！！



//===================================================================================
//接下来继续研究postProcessBeforeInstantiation()

//注意这里需要放行几次，当beanClass中的信息是有关MathCaculator(也就是我们的业务逻辑类)
public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
    
    //先拿到MathCaculator的信息
    Object cacheKey = getCacheKey(beanClass, beanName);

    if (beanName == null || !this.targetSourcedBeans.contains(beanName)) {

        //会先判断MathCaculator是否在advisedBeans(需要增强的bean)中。
        if (this.advisedBeans.containsKey(cacheKey)) {
            return null;
        }
        
        //【isInfrastructureClass(beanClass)】--》判断当前Bean是不是基础类型（也就是实现了Advice，Pointcut，Advisor，AopInfrastructureBean接口），或者是不是用切面注解（@Aspect）标注的
        //这里研究一下shouldSkip，
        if (isInfrastructureClass(beanClass) || shouldSkip(beanClass, beanName)) {
            this.advisedBeans.put(cacheKey, Boolean.FALSE);
            return null;
        }
    }


    ......
        

    return null;
}

//===================================================================================
protected boolean shouldSkip(Class<?> beanClass, String beanName) {
    // TODO: Consider optimization by caching the list of the aspect names
    //拿到所有候选的增强器(切面里面的通知方法)，只不过把这些增强器包装一下，详见图2
    //每一个增强器的类型是【InstantiationModelAwarePointcutAdvisor】
    List<Advisor> candidateAdvisors = findCandidateAdvisors();
    
    //拿出每个增强器
    for (Advisor advisor : candidateAdvisors) {
        
        //判断如果是AspectJPointcutAdvisor类型的，就会返回true
        if (advisor instanceof AspectJPointcutAdvisor) {
            if (((AbstractAspectJAdvice) advisor.getAdvice()).getAspectName().equals(beanName)) {
                return true;
            }
        }
    }
    
    //否则就会返回false
    return super.shouldSkip(beanClass, beanName);
}


//===================================================================================
//shouldSkip返回false之后，postProcessBeforeInstantiation()就会返回一个null
//继续debug运行之后，它会到MainConfigOfAOP（自己写的配置类），开始创建MathCaculator对象
@Bean
public MathCaculator mathCaculator(){
    return new MathCaculator();
}


//===================================================================================
//创建完之后会调用
@Override
public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    if (bean != null) {
        Object cacheKey = getCacheKey(bean.getClass(), beanName);
        if (!this.earlyProxyReferences.contains(cacheKey)) {
            
            //如果需要的情况下会进行包装
            //那什么时候需要包装呢？研究一下
            return wrapIfNecessary(bean, beanName, cacheKey);
        }
    }
    return bean;
}


//===================================================================================
protected Object wrapIfNecessary(Object bean, String beanName, Object cacheKey) {
    if (beanName != null && this.targetSourcedBeans.contains(beanName)) {
        return bean;
    }
    if (Boolean.FALSE.equals(this.advisedBeans.get(cacheKey))) {
        return bean;
    }
    if (isInfrastructureClass(bean.getClass()) || shouldSkip(bean.getClass(), beanName)) {
        this.advisedBeans.put(cacheKey, Boolean.FALSE);
        return bean;
    }

    // Create proxy if we have advice.
    //获取当前bean的所有增强器（通知方法）
    //而getAdvicesAndAdvisorsForBean(bean.getClass(), beanName, null);做了什么呢？
    //1，它会找到所有候选的增强器（找到哪些通知方法是需要切入到当前bean方法中的）
    //2，获取到能在bean使用的增强器
    //3，给增强器排序
    Object[] specificInterceptors = getAdvicesAndAdvisorsForBean(bean.getClass(), beanName, null);
    
    //如果这个bean是需要增强的，那么specificInterceptors就不为空，就能执行里面的操作
    if (specificInterceptors != DO_NOT_PROXY) {
        
        //保存当前bean到advisedBeans中，表示当前bean已经被增强处理了
        this.advisedBeans.put(cacheKey, Boolean.TRUE);
        
        //如果bean需要增强，就创建代理对象
        Object proxy = createProxy(
            bean.getClass(), beanName, specificInterceptors, new SingletonTargetSource(bean));
        this.proxyTypes.put(cacheKey, proxy.getClass());
        return proxy;
    }

    this.advisedBeans.put(cacheKey, Boolean.FALSE);
    return bean;
}



//===================================================================================

//接下来研究一下createProxy()它是如何创建代理对象的？
protected Object createProxy(
    Class<?> beanClass, String beanName, Object[] specificInterceptors, TargetSource targetSource) {

    if (this.beanFactory instanceof ConfigurableListableBeanFactory) {
        AutoProxyUtils.exposeTargetClass((ConfigurableListableBeanFactory) this.beanFactory, beanName, beanClass);
    }

    //创建代理工厂
    ProxyFactory proxyFactory = new ProxyFactory();
    proxyFactory.copyFrom(this);

    
	......

    //把增强器保存在代理工厂之中    
    Advisor[] advisors = buildAdvisors(beanName, specificInterceptors);
    proxyFactory.addAdvisors(advisors);
    proxyFactory.setTargetSource(targetSource);
    customizeProxyFactory(proxyFactory);

	
    ...
    

    //用代理工厂帮我们创建对象 
    //有两种代理对象： 至于创建哪个是由Spring决定的
    //JdkDynamicAopProxy(config)jdk的动态代理
    //ObjensisCglibAopProxy(config)cglib的动态代理
    //这里给容器中返回当前组件使用了cglib增强之后的代理对象
    //所以以后容器中获取到的就是这个组件的代理对象！！！执行目标方法的时候，代理对象就会执行通知方法的流程。    
	return proxyFactory.getProxy(getProxyClassLoader());
}
  


//===================================================================================
//继续debug运行到测试类中的
public class IOCTestAOP {

    @Test
    public void test01(){
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(MainConfigOfAop.class);

        //1,不要自己创建对象
        //但是这样写，实际上是没有效果的，因为这是我们自己写的，而不是由容器执行的
        //只有容器生成的那些bean对象执行方法才能够有aop的效果
        //所以我们不能自己创建，应该要从容器中拿到那些bean
        //        MathCaculator mathCaculator = new MathCaculator();
        //        mathCaculator.div(1,1);
        MathCaculator mathCaculator = annotationConfigApplicationContext.getBean(MathCaculator.class);
		//这里查看一下这个mathCaculator这个bean，详见图3
        //容器中保存了组件的代理对象（这里调用的其实是cglib增强后的对象），对象保存了详细信息
        //
        mathCaculator.div(1,1);

        annotationConfigApplicationContext.close();
    }
}


//===================================================================================

//接下来就是研究一下div这个方法是怎么运行的
//step into进去，会到达CglibAopProxy.intercept()
public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
    Object oldProxy = null;
    boolean setProxyContext = false;
    Class<?> targetClass = null;
    Object target = null;
    try {

        ......
        
        //这一步很重要
        //根据proxyFactory对象获取将要执行的目标方法的拦截器链
        List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);
        Object retVal;
        

        //如果没有拦截器链，直接执行目标方法
        if (chain.isEmpty() && Modifier.isPublic(method.getModifiers())) {
            Object[] argsToUse = AopProxyUtils.adaptArgumentsIfNecessary(method, args);
            retVal = methodProxy.invoke(target, argsToUse);
        }
        //如果有拦截器链
        else {
            //把需要执行的目标对象，方法，参数，拦截器链等传进来，创建一个CglibMethodInvocation对象
            //并调用它的proceed()
            retVal = new CglibMethodInvocation(proxy, target, method, args, targetClass, chain, methodProxy).proceed();
        }
        //对proceed的返回值进行操作
        retVal = processReturnType(proxy, target, method, retVal);
        return retVal;
    }
    finally {
		......
    }
}


//===================================================================================

//接下来的重点就是怎么获取的拦截器链？
//debug进去getInterceptorsAndDynamicInterceptionAdvice()

public List<Object> getInterceptorsAndDynamicInterceptionAdvice(Method method, Class<?> targetClass) {
    MethodCacheKey cacheKey = new MethodCacheKey(method);
    List<Object> cached = this.methodCache.get(cacheKey);
    if (cached == null) {
        
        //这里会使用advisorChainFactory获得拦截器链
        cached = this.advisorChainFactory.getInterceptorsAndDynamicInterceptionAdvice(
            this, method, targetClass);
        this.methodCache.put(cacheKey, cached);
    }
    return cached;
}

//继续debug进去getInterceptorsAndDynamicInterceptionAdvice
public List<Object> getInterceptorsAndDynamicInterceptionAdvice(
    Advised config, Method method, Class<?> targetClass) {


    //先创建一个list保存所有的拦截器，创建的时候已经根据config中的增强器的长度规定好list的长度
    //这里config中的增强器包括：一个默认的ExposeInvocationInterceptor和四个增强器：logException，logReturn，logEnd，logStart
    List<Object> interceptorList = new ArrayList<Object>(config.getAdvisors().length);
    Class<?> actualClass = (targetClass != null ? targetClass : method.getDeclaringClass());
    boolean hasIntroductions = hasMatchingIntroductions(config, actualClass);
    AdvisorAdapterRegistry registry = GlobalAdvisorAdapterRegistry.getInstance();

    //拿到所有的增强器进行遍历
    for (Advisor advisor : config.getAdvisors()) {
        
        //如果PointcutAdvisor
        if (advisor instanceof PointcutAdvisor) {
            // Add it conditionally.
            PointcutAdvisor pointcutAdvisor = (PointcutAdvisor) advisor;
            if (config.isPreFiltered() || pointcutAdvisor.getPointcut().getClassFilter().matches(actualClass)) {
                
                //这里是把增强器传过来，包装成一个interceptors
                //这里的包装的原理：
                //1，如果这个增强器是【MethodInterceptor】这个类型的话，就直接加进interceptor并返回
                //2，如果不是的话，会通过AdvisorAdapter转成interceptor，再返回来
                MethodInterceptor[] interceptors = registry.getInterceptors(advisor);
                MethodMatcher mm = pointcutAdvisor.getPointcut().getMethodMatcher();
                if (MethodMatchers.matches(mm, method, actualClass, hasIntroductions)) {
                    if (mm.isRuntime()) {
                        for (MethodInterceptor interceptor : interceptors) {
                            
                            //添加到interceptorList
                            interceptorList.add(new InterceptorAndDynamicMethodMatcher(interceptor, mm));
                        }
                    }
                    else {
                        
                        interceptorList.addAll(Arrays.asList(interceptors));
                    }
                }
            }
        }
        //如果是IntroductionAdvisor
        else if (advisor instanceof IntroductionAdvisor) {
            IntroductionAdvisor ia = (IntroductionAdvisor) advisor;
            if (config.isPreFiltered() || ia.getClassFilter().matches(actualClass)) {
                
                //包装
                Interceptor[] interceptors = registry.getInterceptors(advisor);
                //添加到interceptorList
                interceptorList.addAll(Arrays.asList(interceptors));
            }
        }
        //又或者
        else {
            //包装
            Interceptor[] interceptors = registry.getInterceptors(advisor);
            //添加到interceptorList
            interceptorList.addAll(Arrays.asList(interceptors));
        }
    }

    //上述的逻辑简而言之就是遍历所有的增强器，并转为interceptor
    //返回interceptorList
    //拦截器链：每一个通知方法又被包装为方法拦截器，利用MethodInterceptor机制
    return interceptorList;
}


//===================================================================================
//接下来就是了解这个拦截器链怎么触发了
//这就需要研究CglibMethodInvocation(proxy, target, method, args, targetClass, chain, methodProxy).proceed()这个方法了
//debug进去，可以看到proceed()的源代码
public Object proceed() throws Throwable {

    
    //currentInterceptorIndex初始值为-1
    //这个判断的用处是：
    //如果这个拦截器链为空，那么就直接执行目标方法
    //还有种情况是，如果执行到最后一个拦截器，最后一个拦截器索引是4，而拦截器链的长度是5，所以也就是拦截器都执行完了，可以开始执行目标方法了
    if (this.currentInterceptorIndex == this.interceptorsAndDynamicMethodMatchers.size() - 1) {
        //这里会利用到反射执行目标方法
        return invokeJoinpoint();
    }

    //拿到拦截器，比如刚开始的话就拿到拦截器链中索引为0的拦截器
    Object interceptorOrInterceptionAdvice =
        this.interceptorsAndDynamicMethodMatchers.get(++this.currentInterceptorIndex);
    if (interceptorOrInterceptionAdvice instanceof InterceptorAndDynamicMethodMatcher) {
        // Evaluate dynamic method matcher here: static part will already have
        // been evaluated and found to match.
        InterceptorAndDynamicMethodMatcher dm =
            (InterceptorAndDynamicMethodMatcher) interceptorOrInterceptionAdvice;
        if (dm.methodMatcher.matches(this.method, this.targetClass, this.arguments)) {
            return dm.interceptor.invoke(this);
        }
        else {
            // Dynamic matching failed.
            // Skip this interceptor and invoke the next in the chain.
            return proceed();
        }
    }
    else {

        //如果不符合上面的操作，就会在这里执行索引为0的拦截器的invoke方法
        //这里的this是CglibMethodInvocation这个对象，记住这一点！
        //下面就需要研究一下各个拦截器的invoke方法，这个至关重要！！！
        return ((MethodInterceptor) interceptorOrInterceptionAdvice).invoke(this);
    }
}


//===================================================================================

//下面的逻辑简而言之就是：链式获取每一个拦截器，拦截器执行invoke方法，每一个拦截器等待下一个拦截器执行完成返回以后再来执行，通过这种拦截器链的机制，保证通知方法与目标方法的执行顺序


//拦截器链中的各个拦截器由头至尾分别是
//ExposeInvocationInterceptor
//AspectJAfterThrowingAdvice
//AfterReturningAdviceInterceptor
//AspectJAfterAdvice
//MethodBeforeAdviceInterceptor


//ExposeInvocationInterceptor
public Object invoke(MethodInvocation mi) throws Throwable {
    //这部分其实是把CglibMethodInvocation设置成线程共享
    //了解下即可
    MethodInvocation oldInvocation = invocation.get();
    invocation.set(mi);
    
    try {
        //在这里重新调用CglibMethodInvocation的proceed方法
        //又回到上面那一段代码去，但是此时的索引不再是-1，而是0，也就是说，此时即将调用的是下一个拦截器的invoke方法，代码详见下一点
        return mi.proceed();
    }
    finally {
        invocation.set(oldInvocation);
    }
}




//AspectJAfterThrowingAdvice
public Object invoke(MethodInvocation mi) throws Throwable {
    try {
        //在这里重新调用CglibMethodInvocation的proceed方法
        //又回到上上面那一段代码去，但是此时的索引不再是0，而是1，也就是说，此时即将调用的是下一个拦截器的invoke方法，代码详见下一点
        return mi.proceed();
    }
    //这里是用来捕获异常的，记住这个点！
    catch (Throwable ex) {
        if (shouldInvokeOnThrowing(ex)) {
            invokeAdviceMethod(getJoinPointMatch(), null, ex);
        }
        throw ex;
    }
}



//AfterReturningAdviceInterceptor
public Object invoke(MethodInvocation mi) throws Throwable {
    //在这里重新调用CglibMethodInvocation的proceed方法
    //又回到上上面那一段代码去，但是此时的索引不再是1，而是2，也就是说，此时即将调用的是下一个拦截器的invoke方法，代码详见下一点
    //但是要注意一点，这里是方法执行后的后置通知，也就是说，如果方法正常执行，那将什么事都没有
    //但是！如果方法运行出错了，这个方法又没有处理异常的逻辑，只能向上抛，而他的上层正是AfterThrowing，
    //由AfterThrowing处理异常
    //这也解释了，为什么只有方法正常运行时才能执行AfterReturning，出现异常就执行不了！
    Object retVal = mi.proceed();
    this.advice.afterReturning(retVal, mi.getMethod(), mi.getArguments(), mi.getThis());
    return retVal;
}


//AspectJAfterAdvice
public Object invoke(MethodInvocation mi) throws Throwable {
    try {
        //在这里重新调用CglibMethodInvocation的proceed方法
        //又回到上上面那一段代码去，但是此时的索引不再是2，而是3，也就是说，此时即将调用的是下一个拦截器的invoke方法，代码详见下一点        
        return mi.proceed();
    }
    finally {
        invokeAdviceMethod(getJoinPointMatch(), null, null);
    }
}


//MethodBeforeAdviceInterceptor
public Object invoke(MethodInvocation mi) throws Throwable {
    
    //这一步调用前置通知！！！
    //此时控制台会打印【 div除法运行。。。@Before参数列表是{[1, 1]} 】
    this.advice.before(mi.getMethod(), mi.getArguments(), mi.getThis() );
	//注意这里又回到了CglibMethodInvocation，但是此时索引为4，会直接调用目标方法！
    return mi.proceed();
}

//打印的结果：
//div除法运行。。。@Before参数列表是{[1, 1]}
//这个方法被调用了MathCaculatoy...div...
//div除法结束。。。@After参数列表是{}
//div除法正常返回。。。@AfterReturning参数列表是{1}

```

