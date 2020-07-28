# 一期

### jvm
1. java代码编译器产生.Class文件(字节码文件)(如jdk命令),字节码文件通过jvm的解释器在不同的平台产生不同的机器码
2. 线程私有数据区域与线程生命周期相同,会根据线程的创建而创建，销毁而销毁;线程共享区则会随虚拟机的创建而创建，销毁而销毁
3. Jvm构成
    1. 程序计数器     |私有| 显示当前线程所执行的字节码的行号
    2. 虚拟机栈       |私有| 为一个栈,用于存储 局部变量，操作数栈，动态链接，方法出口信息
    3. 本地方法区     |私有| 执行native方法服务
    4. 堆             |共享| 创建的对象和数组保存在此
    5. 方法区(永久代) |共享| 被jvm加载的信息,如常量,静态变量,即使编译的代码,运行时常量池也是其中一部分,用于存放编译生成  
        的各种字面量和符号引用
4. 堆从GC角度分为 新生代 老年代
    1. 新生代:老年代 1:2
    2. 新生代分为 Eden:ServivorFrom:ServivorTo 8:1:1
    3. 新生代采用 复制清除算法  老年代采用 标记整理算法    
5. 永久代指内存永久保存区域,主要存放Class和元数据信息,JDK8永久代移除,替代有一个元空间，本质上区别元空间不在虚拟机中而是  
    使用直接内存
6. 确定垃圾采用可达性分析
7. 垃圾回收算法
    1. 标记清除算法
    2. 复制清除算法    
    3. 标记整理算法
    4. 分代收集算法
8. JAVA 四中引用类型
    1. 强 它是不可能被垃圾回收机制回收的
    2. 软 当系统内存足够时它不会被回收，当系统内存空间不足时它会被回收
    3. 弱 只要垃圾回收机制一运行，不管JVM的内存空间是否足够，总会回收该对象占用的内存
    4. 虚 虚引用的主要作用是跟踪对象被垃圾回收的状态
9. 垃圾收集器
    1. Serial             新/老  单线程  复制算法/标记整理
    2. ParNew             新     多线程  复制算法
    3. Parallel Scavenge  新/老  多线程  复制算法/标记整理 它重点关注的是程序达到一个可控制的吞吐量，自适应调节策略也是  
    ParallelScavenge收集器与ParNew收集器的一个重要区别
    4. CMS                老     多线程  标记整理          主要目标是获取最短垃圾回收停顿时间,流程如下
        1. 标记GC Roots能直接关联的对象,仍然需要暂停所有的工作线程
        2. 进行GC Roots跟踪的过程，和用户线程一起工作，不需要暂停工作线程
        3. 修正跟踪期间变化的标记，仍然需要暂停所有的工作线程
        4. 清除GC Roots不可达对象，和用户线程一起工作，不需要暂停工作线程
    5. G1  
        1. 基于标记-整理算法，不产生内存碎片
        2. 可以非常精确控制停顿时间，在不牺牲吞吐量前提下，实现低停顿垃圾回收
        3. G1收集器避免全区域垃圾收集，它把堆内存划分为大小固定的几个独立区域，优先回收垃圾最多的区域
10 JVM 类加载机制,顺序如下 
    1. 加载   加载是类加载过程中的一个阶段,会将这个类加载到内存中
    2. 验证   确保Class文件的字节流中包含的信息是否符合当前虚拟机的要求
    3. 准备   正式为类变量分配内存并设置类变量的初始值阶段,即在方法区分配内存空间
    4. 解析   虚拟机将常量池中的符号引用(字节码的标识 如 CONSTANT_Field_info3)替换为直接引用(地址指向)的过程
    5. 初始化 初始化阶段是执行类构造器<client>方法的过程,如果一个类中没有对静态变量赋值也没有静态语句块，那么编译器可以  
    不为这个类生成<client>()方法
11 类加载器 
    1. 自定义加载器->应用程序类加载器->扩展类加载器->启动类加载器
    2. 当一个类收到了类加载请求，他首先不会尝试自己去加载这个类，而是把这个请求委派给父类去完成,父类不行才交付子类
    3. 好处是不管是哪个加载器加载这个类，最终都是委托给顶层的启动类加载器进行加载，这样就保证了使用不同的类加载器最终得  
    到的都是同样一个Object对象。

# 集合
1.  List
    1. ArrayList,Vector(安全的ArrayList),LinkList
2. Set
    1. HashSet,TreeSet(自己定义的类必须实现Comparable接口，并且覆写相应的compareTo()函数),LinkHashSet(基于LinkHashMap)
3. Map
    1. HashMap 数组+链表+红黑树
        1. 只允许一条记录key=null,允许多条value=null
        2. 扩容=2,负载=0.75
        3. 7->8 从链表长度8个以后为红黑树 时间复杂度从O(n)->O(logN)
    2. ConcurrentHashMap 分段锁,默认分段数为16,JDK8同样引入红黑树
    3. HashTable 
    4. TreeMap 实现SortedMap接口，能够把它保存的记录根据键排序，默认是按键值的升序排序。key必须实现Comparable接口或者在  
    构造TreeMap传入自定义的Comparator
    5. LinkHashMap 

#线程
1. 线程实现方法
    1. 继承Thread类,重写run()方法,类.start()启动线程,start()为native方法
    2. 实现Runnable接口，实现run()方法,new Thread(实现类).start()
    3. 实现Callable接口.可以得到有结果返回的Future对象
2. 线程池 Executor
    1. public ThreadPoolExecutor(int corePoolSize, // 核心线程池大小  
                                 int maximumPoolSize,  // 最大线程池大小  
                                 long keepAliveTime,  // 线程最大空闲时间  
                                 TimeUnit unit,  // 时间单位  
                                 BlockingQueue<Runnable> workQueue, // 用于存放提交的任务，队列的实际容量与线程池大小相关联  
                                 ThreadFactory threadFactory,  // 线程工厂，用于创建线程，一般用默认即可    
                                 RejectedExecutionHandler handler )  //当任务太多来不及处理时，如何拒绝任务   
    2. 线程等待队列
        1. new LinkedBlockingQueue<Runnable>() 可用于Web服务瞬时削峰，但需注意长时间持续高峰情况造成的队列阻塞
        2. new SynchronousQueue<Runnable>() 快速处理大量耗时较短的任务，如Netty的NIO接受请求时
        3. 若任务数量>核心线程池数量,且无空闲任务线程,且>max线程数,则任务将会被拒绝
        4. 队列策略
            1. 直接握手队列它将任务交给线程而不需要保留。这里，如果没有线程立即可用来运行它，那么排队任务的尝试将失败，因  
            此将构建新的线程。
            2. 无界队列当所有corePoolSize线程繁忙时，使用无界队列将导致新任务在队列中等待，从而导致maximumPoolSize的值没  
            有任何作用
            3. 有界队列一个有界的队列和有限的maximumPoolSizes配置有助于防止资源耗尽，但是难以控制。队列大小和maximumPoolSizes  
            需要 相互权衡
    3. 拒绝任务,拒绝任务有两种情况：1. 线程池已经被关闭；2. 任务队列已满且maximumPoolSizes已满  
        1. AbortPolicy：默认测策略，抛出RejectedExecutionException运行时异常
        2. CallerRunsPolicy：这提供了一个简单的反馈控制机制，可以减慢提交新任务的速度
        3. DiscardPolicy：直接丢弃新提交的任务
        4. DiscardOldestPolicy：如果执行器没有关闭，队列头的任务将会被丢弃，然后执行器重新尝试执行任务
3. 线程结束的方式
    1. 正常运行结束
    2. 外部设置标志内部判断跳出
    3. Interrupt方法结束线程  
        1. 如使用了sleep,同步锁的wait,socket中的receiver,accept等方法时，会使线程处于阻塞状态。当调用线程的interrupt()  
        方法时会抛出InterruptException异常,通过代码捕获该异常，然后break跳出循环状态
        2. 线程未处于阻塞状态：使用isInterrupted()判断线程的中断标志来退出循环。当使用interrupt()方法时，中断标志就会  
        置true，和使用自定义的标志来控制循环是一样的道理
    4. thread.stop()来强行终止线程,线程不安全
    
# 锁
1. 锁类型
    1. 自旋锁,如果持有锁的线程能在很短的时间内释放资源,那么竞争锁资源的线程就不需要做内核态和用户态的切换,只需要等一等,就是  
         所谓的自旋,等持有锁的线程释放锁后即可立即获取锁，这样就避免用户线程和内核的切换的消耗
    2. 可重入锁,指一个线程进入一个锁方法,该方法调用的方法任然获取锁,synchronized属于可重入锁
    3. 独占锁只能有一个线程获取锁,ReentrantLock 就是以独占方式实现的互斥锁.也是悲观策略的一种
    4. 共享锁则允许多个线程同时获取锁,ReadWriteLock,属于乐观锁
    5. 重量级锁,Synchronized通过一个监视器锁monitor(班长)来实现,其底层则使用操作系统的互斥锁来实现,而操作系统与线程切换会  
        导致用户态转为核心态,状态转化时间长
    6. 轻量级锁介绍前需要普及一点,对于绝大部分轻量锁,在整个运行期间不存在竞争的.轻量锁使用CAS操作避免使用互斥锁,也避免了开销,  
        但是如果存在锁竞争,除了互斥锁开销还需要CAS,因此有竞争情况比重量锁更慢.加锁过程为进入同步块后,校验无锁后,虚拟机首先为当前线程的  
        栈帧建立一个锁记录的空间称为Lock Record,拷贝对象头中的标记对象,再进行CAS操作,将标记对象的指针指向Lock Record,再将Lock Record中  
        的owner指向标记对象(标记对象->Lock Record Lock Record中owner->标记对象)
    7. 偏向锁偏向锁的目的是在某个线程获得锁之后，消除这个线程锁重入（CAS）的开销，看起来让这个线程得到了偏护,也就是该线程释放锁再次获得  
    锁。原因是因为大多数情况下锁不仅不存在多线程竞争，而且总是由同一线程多次获得。
    8. 分段锁,分段锁也并非一种实际的锁，而是一种思想 ConcurrentHashMap 是学习分段锁的最好实践
2. synchronized 它可以把任意一个非NULL的对象当作锁。他属于独占式的悲观锁，同时属于可重入锁
    1. 作用范围
        1. 作用于方法时，锁住的是对象的实例(this)
        2. 当作用于静态方法时，锁住的是Class实例
        3. 作用于一个对象实例时，锁住的是所有以该对象为锁的代码块
    2. 核心组件
        1. Wait Set:调用Wait方法,线程阻塞的对象放置于此
        2. Contention List:所有请求该锁的请求首先放置于该队列中
        3. Entry List: Contention中有资格成为候选者放入该队列
        4. OnDeck:任意时刻,只有一个线程正在竞争锁,该线程就是OnDeck
        5. Owner:获得锁权限的线程
        5. !Owner:当前释放锁的线程
    3. 实现
        1. 一个线程线先会尝试自旋获取锁,如果获取不到会进入Contention,这能体现出synchronized是非公平的
        2. 因为并发情况下Contention会进行大量CAS访问,为了降低对尾部元素竞争,jvm会将一部分Contention放入Entry
        3. Owner解锁时,会将 ContentionList 中的部分线程迁移到 EntryList 中，并指定EntryList中的某个线程为OnDeck线（一般  
          是最先进去的那个线程）
        4. Owner 线程并不直接把锁传递给OnDeck线程,而是把锁竞争的权利交给OnDeck,OnDeck需要重新竞争锁,这也是非公平的
        5. OnDeck线程获取到锁资源后会变为Owner,而没有得到锁资源的仍然停留在 EntryList中。
        6. 如果 Owner 线程被 wait 方法阻塞，则转移到 WaitSet 队列中直到某个时刻通过 notify或者 notifyAll 唤醒，会重新进去 EntryList 中
    4. 注意
        1. 每个对象都有个monitor(班长)对象加锁就是在竞争monitor对象，代码块加锁是在前后分别加,上monito和 monitorexit 指令来实现的，  
        方法加锁是通过一个标记位来判断的
        2. synchronized 是一个重量级操作，需要调用操作系统相关接口，性能是低效的，有可能给线程加锁消耗的时间比有用操作消耗的时间更多。
        3. JDK6引入适应自旋、锁消除、锁粗化、轻量级锁及偏向锁等，效率有了本质上的提高
        4. JDK7引入了偏向锁和轻量级锁。都是在对象头中有标记位，不需要经过操作系统加锁。
        5. 锁可以从偏向锁升级到轻量级锁，再升级到重量级锁。这种升级过程叫做锁膨胀
    5. 与ReentrantLock区别
        1. ReentrantLock是Api级别的,synchronized是JVM级别的
        2. synchronized是同步阻塞,悲观。lock是同步非阻塞,乐观
        3. lock会自动释放锁,而Lock需要手动
        4. Lock可以实现读写锁
3. ReentrantLock继承Lock并实现接口中定义的方法,是一种可重入锁,除了可完成synchronized的工作外,还可相应中断锁,轮询锁请求,定时锁
        1. ReentrantLock 通过方法 lock()与 unlock()来进行加锁与解锁操作，与 synchronized 会被 JVM 自动解锁机制不同，  
        ReentrantLock 加锁后需要手动进行解锁
        2. ReentrantLock 相比 synchronized 的优势是可中断、公平锁、多个锁。这种情况下需要使用 ReentrantLock
        3. 需要再finally中进行解锁操作
4. 线程方法
    1. 线程等待（wait）
    2. 线程睡眠（sleep）
    3. 线程让步（yield）
    4. 线程中断（interrupt）
    5. 等待其他线程终止(Join)在当前线程中调用一个线程的 join() 方法，则当前线程转为阻塞状态，回到另主线程结束，当前线程  
    再由阻塞状态变为就绪状态，等待 cpu 的宠幸
    6. 线程唤醒（notify）
5. CAS(比较并交换)
    1. 包含3个参数(V 需要更新的变量,E 旧值,N 新值),且当V ==E 才会V=N
    2. 若不相等,说明其他线程已经更新,就会返回V,也就是真实值
    3. 运许失败后重试,知道成功,比如使用version字段,是乐观锁
    4. ABA问题,某个线程将值从1变为0,然后再次为1.对于其他线程有可能发生在一次操作中，为感知到已变化过,version无此问题
6. AQS 抽象的队列同步器,AQS定义了一套多线程访问共享资源的同步框架,如ReentrantLock,Semaphore,CountDownLatch
    1. 由一个先进先出线程队列(多线程阻塞竞争阻塞时放入此队列)和一个state(资源)构成
    2. AQS定义两种资源共享方式,如ReentrantLock的独占式,和如Semaphore,CountDownLatch共享式
    3. AQS 只是一个框架，具体资源的获取/释放方式交由自定义同步器去实现
    4. AQS 也支持自定义同步器同时实现独占和共享两种方式，如 ReentrantReadWriteLock。
    