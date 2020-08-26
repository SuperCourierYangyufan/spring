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
        1. 阻塞队列
            1. LinkedBlockingQueue<Runnable>() 可用于Web服务瞬时削峰，但需注意长时间持续高峰情况造成的队列阻塞，由链表结构  
            组成的有界阻塞队列,先进先出排序,特定是对消费端和生成端分别采用独立锁,锁细分
            2. SynchronousQueue<Runnable>() 快速处理大量耗时较短的任务，如Netty的NIO接受请求时 并发同步阻塞不存储元素队列,
            每一个put操作必须等待一个take操作，否则不能继续添加元素
            3. ArrayBlockingQueue 由数组结构组成的有界阻塞队列,先进先出排序,默认情况不保证访问者的公平性
            4. PriorityBlockingQueue  支持优先级排序的无界阻塞队列,默认采用元素自然顺序排序,或自定义CompareTo()排序
            5. DelayQueue 使用优先级队列实现的无界阻塞队列,特点是支持延时和获取元素,底层使用PriorityQueue来实现,可用于定时
            任务或缓存的使用
            6. LinkedTransferQueue 由链表结构组成的无界阻塞队列
            7. LinkedBlockingDeque 由链表结构组成的双向阻塞队列
        2. 若任务数量>核心线程池数量,且无空闲任务线程,且>max线程数,则任务将会被拒绝
        3. 队列策略
            1. 直接握手队列它将任务交给线程而不需要保留。这里，如果没有线程立即可用来运行它，那么排队任务的尝试将失败，因  
            此将构建新的线程。
            2. 无界队列当所有corePoolSize线程繁忙时，使用无界队列将导致新任务在队列中等待，从而导致maximumPoolSize的值没  
            有任何作用
            3. 有界队列一个有界的队列和有限的maximumPoolSizes配置有助于防止资源耗尽，但是难以控制。队列大小和maximumPoolSizes  
            需要 相互权衡
            4. 有界和无界最大区别就是队列数量有无界限
    3. 拒绝任务,拒绝任务有两种情况：1. 线程池已经被关闭；2. 任务队列已满且maximumPoolSizes已满  
        1. AbortPolicy：默认测策略，抛出RejectedExecutionException运行时异常
        2. CallerRunsPolicy：这提供了一个简单的反馈控制机制，可以减慢提交新任务的速度
        3. DiscardPolicy：直接丢弃新提交的任务
        4. DiscardOldestPolicy：如果执行器没有关闭，队列头的任务将会被丢弃，然后执行器重新尝试执行任务
3. 线程结束的方式
    1. 正常运行结束
    2. 外部设置标志内部判断跳出
    3. Interrupt方法结束线程  
        1. 如使用了sleep,同步锁的wait,socket中的receiver,accept等方法时，会使线程处于阻塞状态。当调用线程的interrupt() 方法时会抛出InterruptException异常,通过代码捕获该异常，然后break跳出循环状态
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
        1. ReentrantLock 通过方法 lock()与 unlock()来进行加锁与解锁操作，与 synchronized 会被 JVM 自动解锁机制不同，ReentrantLock 加锁后需要手动进行解锁
        2. ReentrantLock 相比 synchronized 的优势是可中断、公平锁、多个锁。这种情况下需要使用 ReentrantLock
        3. 需要再finally中进行解锁操作
        4. ReentrantReadWriteLock.ReadLock|ReentrantReadWriteLock.WriteLock又一个ReentrantReadWriteLock生成,读锁为共享锁,
        且当没有读锁发生时共享,读锁为排他锁
        5. 在ReentrantLock的默认无参构造方法中，创建的是非公平锁,使用的是其中的内部类NonfairSync,继承AbstractQueuedSynchronizer
            * NonfairSync
                - NonfairSync就是以个AQS,上锁本质上就是尝试设置state=1
                - 线程只要执行lock请求,就会立马尝试获取锁,不会管AQS当前管理的等待队列中有没有正在等待的线程,这种操作是不公平的，没有先来后到
                - CAS操作得比较与交换(compareAndSetState)利用unsafe包的cas操作，unsafe包类似一种java留给开发者的后门，  
                可以用来直接操作内存数据，并且保证这个操作的原子性
                - 当第一次获得锁失败,再次判断,若还是不行,判断是否为重入锁情况(获取锁线程是否为当前线程),若是则state+1
                - 当获取失败且部位重入锁情况下,创建一个互斥得节点,放入Node链表尾部
                - 然后让该节点判断,首先判断上个节点是否为头节点,若是得话说明是队列中最大优先级节点,不必挂起,再次尝试获取锁
                - 若不是,检查前置节点值
                    * 前置节点是-1，返回true表示线程可挂起
                    * 前置节点大于0表示前置节点已经取消，那么进行跳过前置节点的操作，做链表的基本删除节点操作
                    * 如果前置节点还是0,表示前置节点Node的waitStatus是初始值，需要设置为-1，然后外层循环重新执行  
                    shouldParkAfterFailedAcquire方法，即可挂起当前线程
                - 最后阻止线程,等待唤醒
            * FairSync公平锁
                - 与非公平锁最大区别在第一次上锁时会判断队列中是否有等待得线程,若没有才会尝试获取锁
4. 线程方法
    1. 线程等待（wait）
    2. 线程睡眠（sleep）
    3. 线程让步（yield）
    4. 线程中断（interrupt）
    5. 等待其他线程终止(Join)在当前线程中调用一个线程的 join() 方法，则当前线程转为阻塞状态，回到另主线程结束，当前线程  
    再由阻塞状态变为就绪状态，等待 cpu 的宠幸
    6. 线程唤醒（notify）
5. 术语
    1. cpu巧妙通过时间片轮换,当轮换时任务的状态保存及再加载, 这段过程就叫做上下文切换
    2. 上下文是指某一时间点CPU 寄存器和程序计数器的内容
    3. 寄存器 是CPU 内部的数量较少但是速度很快的内存（与之对应的是CPU 外部相对较慢的RAM 主内存）
    4. 程序计数器 是一个专用的寄存器，用于表明指令序列中CPU 正在执行的位置，存的值为正在执行的指令的位置或者下一个将要被执行的指令的位置
6. volatile提供了一种稍弱的同步机制(不加锁),用来确保将变量的更新操作通知到其他线程
    1. 变量可见:保证所有线程可见,值一个线程修改了值,那么新的值对于其他线程是可见的,且每次读取必须刷新内存种最新值
    2. 禁止重排序:禁止了JVM的指令重排序
    3. 对于非volatile的变量读写,每个线程会先拷贝到当前CPU缓存,多个CPU拷贝的值可能会不一样
    4. volatile的变量读写,会直接从内存种读取,跳过CPU缓存这一步
    5. 不支持i++,因为先读取值,再+1,再回写
    6. 所以volatile只支持单纯的赋值
7. ThreadLocal线程本地存储,在一个线程的调用过程,进入多个方法种起作用,减少一个线程内多个函数或组件参数的复杂度
8. CAS(比较并交换)
    1. 包含3个参数(V 需要更新的变量,E 旧值,N 新值),且当V ==E 才会V=N
    2. 若不相等,说明其他线程已经更新,就会返回V,也就是真实值
    3. 运许失败后重试,知道成功,比如使用version字段,是乐观锁
    4. ABA问题,某个线程将值从1变为0,然后再次为1.对于其他线程有可能发生在一次操作中，为感知到已变化过,version无此问题
9. AQS 抽象的队列同步器,AQS定义了一套多线程访问共享资源的同步框架,如ReentrantLock,Semaphore,CountDownLatch
    1. 由一个先进先出线程队列(多线程阻塞竞争阻塞时放入此队列)和一个state(资源)构成
    2. 如果被请求的共享资源空闲，则将当前请求资源的线程设置为有效的工作线程，并将共享资源设置为锁定状态，如果被请求的共享资源被占用，  
    那么就需要一套线程阻塞等待以及被唤醒时锁分配的机制，这个机制AQS是用CLH队列锁实现的，即将暂时获取不到锁的线程加入到队列中
    3. CLH队列是一个虚拟的双向队列，虚拟的双向队列即不存在队列实例，仅存在节点之间的关联关系。AQS是将每一条请求共享资源的线程封装成  
    一个CLH锁队列的一个结点（Node），来实现锁的分配。
    4. 用大白话来说，AQS就是基于CLH队列，用volatile修饰共享变量state，线程通过CAS去改变状态符，成功则获取锁成功，失败则进入等待队列，等待被唤醒
    5. 流程
        * 调用自定义同步器的tryAcquire()尝试直接去获取资源，如果成功则直接返回；
        * 没成功，则addWaiter()将该线程加入等待队列的尾部，并标记为独占模式；
        * quireQueued()使线程在等待队列中休息，有机会时（轮到自己，会被unpark()）会去尝试获取资源。获取到资源后才返回。  
        如果在整个等待过程中被中断过，则返回true，否则返回false。
        * 如果线程在等待过程中被中断过，它是不响应的。只是获取资源后才再进行自我中断selfInterrupt()，将中断补上
    6. AQS定义两种资源共享方式,如ReentrantLock的独占式,和如Semaphore,CountDownLatch共享式
    7. AQS 只是一个框架，具体资源的获取/释放方式交由自定义同步器去实现
    8. status在不同情况含义不同
        * 在ReentrantLock中，表示AQS的锁是否已经被占用获取，0：没有，>=1：已被获取,当大于1时表示被同一线程多次重入锁
        * 在CountDownLatch中，表示计数还剩的次数，当到达0时，唤醒等待线程。
        * 在Semaphore中，表示AQS还可以被获取锁的次数，获取一次就减1，当到达0时，尝试获取的线程将会阻塞

# 基础
1. 反射
    1. 动态语言指程序运行期间可以改变结构,可以增加新得函数,删除已有函数,从反射角度,java属于半动态语言
    2. Person p=new Student(); 编译时类型为Person，运行时类型为Student
    3. list.getClass();ArrayList.class;Class.forName("类的全路径")
2. clone()方法需要类实现Cloneable并重写clone()

#Mybatis
1. 一级缓存
    1. 一级缓存是指SqlSession级别的缓存，当在同一个SqlSession中进行相同的SQL语句查询时，第二次以后的查询不会从数据库查询，  
    而是直接从缓存中获取，一级缓存最多缓存1024条SQL
    2. 发出一个新得sql查询,查询结果写入SqlSession,结构为map(key:MapperID+offset+limit+Sql+所有的入参,value:信息)
    2. 如果中间出现commit操作（修改、添加、删除），本sqlsession中的一级缓存区域全部清空
2. 二级缓存
    1. 二级缓存的范围是mapper级别（mapper同一个命名空间）
    2. 所有的查询操作，在CacheExecutor中都会先匹配缓存中是否存在，不存在则查询数据库
    3. key：MapperID+offset+limit+Sql+所有的入参

# netty
1. 0拷贝
    1. Netty的接送和发送ByteBuffer使用堆外直接内存进行Socket读写,不要进行字节缓存区的二次拷贝
        * 读取本地文件,通过传统网络发送需要4次内容复制
            1. 从磁盘复制数据到内核态内(DMA操作,无需消耗CPU)
            2. 从内核态内存复制到应用缓冲区中,该步骤完成读取数据(copy动作,需要消耗CPU)
            3. 然后从应用缓冲区中内存复制到网络驱动的内核态内存(copy动作,需要消耗CPU)
            4. 后是从网络驱动的内核态内存复制到网卡中进行传输,该步骤完成发送内容
        * 使用netty则直接取消了应用缓存区,直接将读取到的内核内数据直接复制到写出的内核数据中
    2. 传统模式使用堆内存进行Socket读写,JVM需要将堆内存Buff拷贝到直接内存,再写入Buff
    3. Netty可以组合多个Buff聚合成一个Buff
    4. Netty的文件传输采用了transferTo方法,可以直接将文件缓存区的数据发送到Channel,避免了循环write导致的内存拷贝问题
2. 内存池,特别时堆外内存分配回收十分耗时,Netty采用了基于内存池的缓存区重用机制
3. NIO采用多个并行的串行化,实现了局部无锁化的串行线程比一个队列多个工作线程模型更优
4. Netty RPC
    1. 流程,RPC就是将3-8进行封装。java一般使用动态代理方式实现远程调用
        1. 生产者使用zk注册,消费端可以通过zk获取地址
        2. 消费方以本地调用方式调用服务
        3. Client Stub(客户端存根)用于存放服务端地址消息,将客户端的组装方法,参数打包成网络消息,,也便是一个地址,很长的一段URL
        4. 找到服务地址,再通过网络远程发送给服务方
        4. Server Stub 收到消息后进行解码
        5. Server Stub 根据解码结果调用本地方法
        6. 本地服务执行并将结果返回给 Server Stub
        7. Server Stub 将返回结果打包成消息并发送至消费方
        8. Client Stub 接收到消息，并进行解码
        9. 服务消费方得到最终结果。
    2. 消息结构
        1. 消费端请求结构:接口名称+方法名+参数类型和参数值+超时时间+ requestID(标识唯一请求 id)
        2. 服务端返回结构:返回值+状态code+requestID
    3. 通讯过程
        1. 调用使用netty的channel.writeAndFlush()方法来发送消息二进制串,这个方法从整个调用过程来说是异步的,不知道何时返回  
        结果,所以会向下运行(问题1)
        2. 多线程进行方法调用,一个Client下有一个Socket,此时A线程先进来发送消息,再是B线程,有可能B的结果先返回,怎么保证对于(问题2)
        3. 问题2:client每次通过Socket调用前需要生成唯一的RequestId,返回将其带回,通常使用AtomicLong累计生成
        4. 问题1: 
            1. 将处理结果的回调对象callback存放到全局ConcurrentHashMap里面put(requestID, callback)
            2. 当线程channel.writeAndFlush()发送消息后，紧接着执行 callback 的 get()方法试图获取远程返回的结果
            3. 在 get()内部，则使用 synchronized 获取回调对象 callback 的锁，再先检测是否已经获取到结果，如果没有，然后调用 callback 的 wait()方法，  
            释放callback 上的锁，让当前线程处于等待状态
            4. 服务端处理完成后,将response发给客户端,客户端Socket连接上专门监听消息的线程收到消息
            5. 分析response,获取requestId,再从ConcurrentHashMap通过id获得callback对象
            6. 再通过synchronized获取CallBack的锁,再将返回的结果设置到CallBack对象中
            7. 最后通过callback.notifyAll()唤醒前面处于等待状态的线程
    4. MRI  Java 远程方法调用，即 Java RMI是 Java 编程语言里，一种用于实现远程过程调用的应用程序编程接口
        
# kafka
1. 构成
    1. broker:kafka服务器,负责消息存储和转发
    2. topic:消息类别
    3. partition:topic分区,一个topic可以有多个partition,topic消息保存再各个partition上
    4. offset:消息日志中的位置,可以理解是消息再partition上的偏移量,也代表是消息的唯一序号
    5. producer:生产者
    6. consumer:消费者
    7. consumer group:消费分组
    8. zookeeper:保存broker,topic,partition等数据,还负责故障发现,partition选举,负载均衡
2. partition
    1. partition中每条Message包含三个属性 offset,messageSize,data
    2. partition物理上由多个segment文件组成,每个segment相等,顺序读写
    3. kafka为每个segment中的数据文件建立了索引,采用稀疏存储，每隔一定字节建立索引
    
    
# RocketMq
1. rocketMq分为pull模式和Push模式,push通过长轮询得pull实现
2. RocketMq定义了一个ProcessQueue,来解决监控和控制,比如:如何得知当前消息堆积的数量,如何重复处理某些消息,如何延迟处理某些消息
    * ProcessQueue对象里主要的内容是一个TreeMap和一个读写锁
        - TreeMap里以MessageQueue的Offset作为Key，以消息内容的引用为Value，保存了所有从MessageQueue获取到，但是还未被处理的消息
        - 读写锁控制着多个线程对TreeMap对象的并发访问
    * ProcessQueue还可以辅助实现顺序消费的逻辑
    * 一个Topic会有多个MessageQueueOffset是指某个Topic下的一条消息在某个MessageQueue里的位置，通过Offset的值可以定位到这条消息
        - push模式下不用关心OffsetStore的事，但是如果PullConsumer，我们就要自己处理OffsetStore了,默认用的Push
        - offset对于每个队列都是自己下从0开始定义的,不是总数
3. 消息发送同步发送、异步发送,单向发送,还支持延时发送和发送事务
    * 延时发送:Broker收到这类消息后，延迟一段时间再处理，使消息在规定的一段时间后生效,时间为仅支持设值的时间长度
    * 对事务的支持:是指发送消息事件和其他事件需要同时成功或同时失败
        - 发送方向RocketMQ发送“待确认”消息
        - RocketMQ将收到的“待确认”消息持久化成功后，向发送方回复消息已经发送成功，此时第一阶段消息发送完成
        - 发送方开始执行本地事件逻辑
        - 发送方根据本地事件执行结果向RocketMQ发送二次确认（Commit或是Rollback）消息
        - RocketMQ收到Commit状态则将第一阶段消息标记为可投递，订阅方将能够收到该消息；
        - 收到Rollback状态则删除第一阶段的消息，订阅方接收不到该消息。
        - 如果出现异常情况，步骤4）提交的二次确认最终未到达RocketMQ,服务器在经过固定时间段后将对“待确认”消息、发起回查请求
        - 发送方收到消息回查请求通过检查对应消息的本地事件执行结果返回Commit或Roolback状态
        - RocketMQ收到回查请求后,继续按照上述逻辑处理

4. NameServer维护每个机器的角色、IP地址配置信息、状态信息，其他角色都通过NameServer来协同执行
    * NameServer是整个消息队列中的状态服务器，集群的各个组件通过它来了解全局的信息,，各个角色的机器都要定期向NameServer  
    上报自己的状态，超时不上报的话，NameServer会认为某个机器出故障不可用了，其他的组件会把这个机器从可用列表里移 
    * NameServer本身是无状态的，也就是说NameServer中的Broker、Topic等状态信息不会持久存储，都是由各个角色定时上报并存储到内存中的
    * RocketMQ各个模块间的通信，可以通过发送统一格式的自定义消息,采用自己定义了一个通信协议，使得模块间传输的二进制消息和有意义的内容之间互相转换
    * NameServer的功能虽然非常重要，但是被设计得很轻量级，代码量少并且几乎无磁盘存储，所有的功能都通过内存高效完成
    * RocketMQ基于Netty对底层通信做了很好的抽象，使得通信功能逻辑清晰
5. Broker是RocketMQ的核心,包括接受生产者消息,处理消费者请求、消息的持久化存储、消息的HA机制以及服务端过滤功能等
    * 磁盘读写,顺序写速度可以达到600MB/s,而磁盘随机写的速度只有大概lOOKB/s
    * RocketMQ消息的存储是由ConsumeQueue和CommitLog配合完成的
        - ConsumeQueue本质队列里面放的不是数据,而是个指针,指向的是物理存储地址，也就是CommitLog里面对应的数据
        - 在CommitLog中，一个消息的存储长度是不固定的，RocketMQ采取一些机制，尽量向CommitLog中顺序写，但是随机读
        - Rocket分布式中,分为Master(brokerId=0)支持读写,而Slave(brokerId>0)仅支持读
        - 所以消费者连接Slave,而生产者连接Master会大大提高效率
    * 刷盘方式分为同步刷盘和异步刷盘
        * 异步刷盘消息只被写入叶缓存,当消息堆积到一定程度后批量写入
        * 同步刷盘是当返回消息状态为成功时,已经完成磁盘写入
    * Master复制到Slave也分为同步复制与异步复制
6. rocketMq默认不能进行全局顺序消费,若要满足全局顺序消费,需要将Top下的读写队列设置为1,简单来说就是单线程处理
7. 若要满足部分顺序消费,则将一个消息组放入一个消息队列中即可,消费端通过MessageQueueSelector来选择发往那个消息队列,消费端  
通过MessageListenerOrderly类来解决单MessageQueue的消息被并发处理的问题
8. 优化
    * 消息过滤
        - 通过Tag进行过滤
        - 在启动Broker前在配置文件里加上filterServer­Nums = 3这样的配置,消费端实现MessageFilter类
    * 提高Consumer处理能力
        - 增加Consumer实例的数量来提高并行度
        - 设置Consumer的consumeMessageBatchMaxSize这个参数，默认是1，如果设置为N，在消息多的时候每次收到的是个长度为N的消息链表
        - 检测延时情况，跳过非重要消息
    * Consumer负载均衡默认五种,也可自己实现
    
       
# 数据库
1. 存储引擎包含InnoDB,MyIsam,memory,Archive,Federated等等
2. InnoDB(B+树)
    1. 5.6默认为InnoDB
    2. 遵循ACID模式,行级别锁,一致性读
    3. 底层为B+树,每个节点对应InnoDB的一个page,每个page大小是固定的,其中非叶子节点只有键值,叶子节点包含完整数据
3. InnoDB构成
    1. 内存池客户端读取数据时，如果数据存在于缓冲池中，客户端就会直接读取缓冲池中的数据，否则再去磁盘中读取；对于数据库中  
    的修改数据，首先是修改在缓冲池中的数据,然后等到阈值批量提交。维护该查询缓存对应的内存区域。从MySQL 5.7.20开始，不推荐  
    使用查询缓存，并在MySQL 8.0中删除
        * 缓冲区分为两块,第一块为控制页括该页所属的表空间编号、页号、缓存页在Buffer Pool中的地址、链表节点信息、一些锁信息以及LSN信息  
        第二块就是缓冲的数据,控制页和缓存页是一一对应的,其中控制块被存放到 Buffer Pool 的前边，缓存页被存放到 Buffer Pool 后边
        * 在Buffer Pool中被修改的页称为脏页，脏页并不是立即刷新，而是被加入到flush链表中，待之后的某个时刻同步到磁盘上
        * 为了快速定位某个页是否被加载到Buffer Pool，使用表空间号 + 页号作为key，缓存页作为value，建立哈希表
        * 缓冲区淘汰原则采用LRU(最久未使用淘汰),存放一个链表,最新使用的放入表头.整个链表分为两节,一节储存使用率不高的old区  
        一节使用率高的young区,当磁盘上的某个页面在初次加载到BufferPool中的某个缓存页时，该缓存页对应的控制块会被放到old区域的头部,   
        这样解决了一次大量读取会刷掉热数据和数据库预读引起的问题
        * 因为一页有多条数据,全表扫描会读取一页多次,所以依然会把old放入young,所以提出时间概念,第一次访问该页会记录时间,如果后续  
        访问该页时间短,也不会触发移动
        * 只有被访问的缓存页位于young区域的1/4的后边，才会被移动到LRU链表头部
        * 我们可以通过指定innodb_buffer_pool_instances来控制Buffer Pool实例的个数，每个Buffer Pool实例中都有各自独立的链表，互不干扰
    2. 后台线程
        1. Master Thread 主要负责将缓冲池中的数据异步刷新到磁盘中，除此之外还包括插入缓存、undo 页的回收等
        2. IO Thread 是负责读写 IO 的线程
        3. Purge(清除)Thread 主要用于回收事务已经提交了的 undo log
        4. PagerCleanerThread 是新引入的一个用于协助 Master Thread 刷新脏页到磁盘的线程，它可以减轻 Master Thread 的工作压力，减少阻塞
    3. 存储文件:存储数据都是按表空间进行存放的，默认为共享表空间，存储的文件即为共享表空间文件（ibdata1)
4. InnoDb逻辑存储结构主要包括表空间，段，区，页，行组成
    1. 表空间共有两种，一种是共享表空间，另一种是独占表空间共享表空间为ibdata1。如果设置了参数innodb_file_per_table,  
    则每一张表都有一个独立的物理文件  
        * 我们知道MySQL中的视图其实是虚拟的表，也就是某个查询语句的一个别名而已，所以在存储视图的时候是不需要存储真实的数据的，  
        只需要把它的结构存储起来就行了。和表一样，描述视图结构的文件也会被存储到所属数据库对应的子目录下边，只会存储一个视图名.frm的文件
        * 整个MySQL进程只有一个系统表空间，在系统表空间中会额外记录一些有关整个系统信息的页面
        * 独立表空间中第一个页的头文件中存有最大索引值得下一个应分配值
        * 系统表里会存放很多元数据,如表对应的每一个列的类型是什么,该表有多少索引，每个索引对应哪几个字段，该索引对应的根页面  
        在哪个表空间的哪个页面等等,这些在插入时候,修改等等时候会需要用到
    2. 表空间有段组成，段分为数据段和回滚段
        * 存放叶子节点的区的集合就算是一个段,存放非叶子节点的区的集合也算是一个段
        * 段其实不对应表空间中某一个连续的物理区域，而是一个逻辑上的概念，由若干个零散的页面以及一些完整的区组成
    3. 区是表空间的结构单元,对于16KB的页来说，连续的64个页就是一个区大小为1M。
        * 为了防止不同物理空间上存放太远的页进行随机I/O过于慢,所以引出区
        * 碎片区:在一个碎片区中，并不是所有的页都是为了存储同一个段的数据而存在的，可以是叶子节点的页+非叶子节点的页,当某个  
        段占用了32个碎片区页面之后，就会以完整的区为单位来分配存储空间
    4. 页是MySQL中磁盘和内存交互的基本单位，也是MySQL是管理存储空间的基本单位，默认大小为16KB。一次读写最小也是16kb
        * Mysql规定一个页中至少存放两行记录,如果我们一条记录的某个列中存储的数据占用的字节数非常多时，该列就可能成为溢出列
        * 页结构
            1. FileHeader,文件头部,页的一些通用信息,比方说这个页的编号是多少，它的上一个页、下一个页是谁,页的类型,比如索引页，也就是我们  
            所说的数据页,也就是存放记录的页
            2. PageHeader,页面头部,数据页专有的一些信息,比如本页中已经存储了多少条记录，第一条记录的地址是什么，页目录中存储了多少个槽等等
            3. Infimum + Supremum,最小记录和最大记录,两个虚拟的行记录,也会写入行格式中记录头信息的heap_no中,所以heap_no默认实际数据从2开始
            4. UserRecords,用户记录,实际存储的行记录内容,行格式存储部分
            5. FreeSpace,空闲空间,页中尚未使用的空间,当插入时,申请空间写入后分配到UserRecords,且空闲用完也就说明该页用完,再次插入  
            只能申请新的页
            6. PageDirectory,页面目录,页中的某些记录的相对位置
            7. FileTrailer,文件尾部,校验页是否完整,防止内存同步磁盘时断电等情况	
        * 页目录(PageDirectory)
            1. 将所有正常的记录（包括最大和最小记录，不包括标记为已删除的记录）划分为几个组
            2. 每个组的最后一条记录（也就是组内最大的那条记录）的头信息中的n_owned属性表示该记录拥有多少条记录，也就是该组内共有几条记录。
            3. 将每个组的最后一条记录的地址偏移量单独提取出来按顺序存储到靠近页的尾部的地方,这就叫页目录
            4. 对于最小记录所在的分组只能有 1 条记录，最大记录所在的分组拥有的记录条数只能在 1~8 条之间，剩下的分组中记录的条数范围只能在是 4~8 条之间
            5. 在一个组中的记录数等于8个后再插入一条记录时，会将组中的记录拆分成两个组，一个组中4条记录，另一个5条记录
            6. 在页中根据页目录查询记录步骤
                1. 通过二分法确定该记录所在的槽，并找到该槽所在分组中主键值最小的那条记录
                2. 通过记录的next_record属性遍历该槽所在的组中的各个记录。
    5. InnoDB 存储引擎是面向行的,也就是说数据是按行进行存放的，每个页存放的行记录也是有硬性定义的，最多允许存放7992 行记录
        * 行结构
            1. 额外信息
                1. 变长字段,如varchar(M),字段长度不固定,这里存放变长字端真实长度,按照列的【顺序】逆序存放,且当字符集为非边长字符集  
                如ascii时,char(M)长度才不会放入边长字段中
                2. NULL值列表，每一个允许为空得列对应一个二进制位,二进制位按照顺序的逆序排列
                3. 记录头信息，固定5个字节,也就是40个二进制位,包含许多基本属性
                    * 名字     大小(bit)   描述
                    * 预留位1	    1	没有使用
                    * 预留位2	    1	没有使用
                    * delete_mask	1	标记该记录是否被删除
                    * min_rec_mask	1	B+树的每层非叶子节点中的最小记录都会添加该标记
                    * n_owned	    4	表示当前记录拥有的记录数
                    * heap_no	    13	表示当前记录在记录堆的位置信息
                    * record_type	3	表示当前记录的类型，0表示普通记录，1表示B+树非叶节点记录，2表示最小记录，3表示最大记录
                    * next_record	16	表示下一条记录的相对位置,排序是按照主键由小到大,指向下一行记录真实信息和额外的信息中间,  
                    因为记录信息中null值列表和边长字段都是倒序,从右往左查询刚好为正,二分思想
            2. 真实信息
                1. 隐藏列,如row_id(唯一标识记录,非必须),transaction_id(事务Id),roll_pointer(回滚指针)
                    * trx_id：每次一个事务对某条聚簇索引记录进行改动时(真正修改记录时)，都会把该事务的事务id赋值给trx_id隐藏列
                    * roll_pointer：每次对某条聚簇索引记录进行改动时，都会把旧的版本写入到undo日志中，然后这个隐藏列就相当  
                    于一个指针，可以通过它来找到该记录修改前的信息
                2. 列存储的真实值,当真实值数据过大,会发生行溢出,超过页的承受范围,真实值会分布到其他几页,该真实值位置则放指向页的指针
       * Dynamic是5.7默认行格式,与上述的Compact很相似,最大区别在于行溢出时,Compact在列中存放一部分实际数据然后外加其他页地址,Dynamic则
       * 一条行记录被删除并不会马上被从硬盘上删除,而是会把next_record写为0,放入垃圾链表,之后如果有新记录插入到表中的话，可能把这些被删除  
       的记录占用的存储空间覆盖掉。  
       完全只存放其他页地址,不存真实数据     
5. 事务
    1. 事务隔离级别
        * 未提交读: 一个事务读取到其他事务未提交的数据，是级别最低的隔离机制；
        * 提交读: 一个事务读取到其他事务提交后的数据
        * 可重复读: 一个事务对同一份数据读取到的相同，不在乎其他事务对数据的修改；
        * 序列化: 事务串行化执行，隔离级别最高，牺牲了系统的并发性
    2. 事务的并发会造成的问题
       * 脏读：事务A读取了事务B更新的数据，然后B回滚操作，那么A读取到的数据是脏数据
       * 不可重复读：事务 A 多次读取同一数据，事务 B 在事务A多次读取的过程中，对数据作了更新并提交，导致事务A多次  
       读取同一数据时，结果 不一致    
       * 幻读：同一事务中对同一范围的数据进行读取，结果却多出了数据或者少了数据，这就叫幻读
    3. ACID
        1. 原子性 操作是一个不可分割的操作
        2. 隔离性 要保证其它的状态转换不会影响到本次状态转换
        3. 一致性 最终结果保存相等
        4. 持久性 当执行一个操作后，这个转换的结果将永久的保留
    4. 事务开启有三种模式
        * READ ONLY：标识当前事务是一个只读事务，也就是属于该事务的数据库操作只能读取数据，而不能修改数据
        * READ WRITE：标识当前事务是一个读写事务，也就是属于该事务的数据库操作既可以读取数据，也可以修改数据(默认)
        * WITH CONSISTENT SNAPSHOT：启动一致性读
        * 在只读事务中不可以对普通的表（其他事务也能访问到的表）进行增、删、改操作，但可以对临时表做增、删、改操作
    5. 保存点;是在事务对应的数据库语句中打几个点，我们在调用ROLLBACK语句时可以指定会滚到哪个点，而不是回到最初的原点
    6. 这个事务id本质上就是一个数字，它的分配策略和我们前边提到的对隐藏列row_id(id值)的分配策略大抵相同
         * 服务器会在内存中维护一个全局变量，分配一个事务id时，就会把该变量的值当作事务id分配给该事务，并且把该变量自增1
         * 每当这个变量的值为256的倍数时，就会将该变量的值刷新到系统表空间的页号为5的页面中一个称之为Max Trx ID的属性处
         * 当系统下一次重新启动时，会将上边提到的Max Trx ID属性加载到内存中，将该值加上256之后赋值给我们前边提到的全局变量  
         ,因为上次分配出去的值不可能大于256
    7. ReadView(MVCC)
        1. 原理:当多个事务对一行数据修改时,roll_pointer会存储所有的更新信息,按照事务id从大到小排序。其他事务中若读取该事务  
        按照判断规则获取对应的一行数据返回
        2. 构成
            1. m_ids：表示在生成ReadView时当前系统中活跃的读写事务的事务id列表。
            2. min_trx_id：表示在生成ReadView时当前系统中活跃的读写事务中最小的事务id，也就是m_ids中的最小值。
            3. max_trx_id：表示生成ReadView时系统中应该分配给下一个事务的id值。
            4. creator_trx_id：表示生成该ReadView的事务的事务id。
        3. 判断
            1. 如果被访问版本的trx_id属性值与ReadView中的creator_trx_id值相同，意味着当前事务在访问它自己修改过的记录，所以该版本可以被当前事务访问
            2. 如果被访问版本的trx_id属性值小于ReadView中的min_trx_id值，表明生成该版本的事务在当前事务生成ReadView前已经提交，所以该版本可以被当前事务访问
            3. 如果被访问版本的trx_id属性值大于或等于ReadView中的max_trx_id值，表明生成该版本的事务在当前事务生成ReadView后才开启，所以该版本不可以被当前事务访问
            4. 如果被访问版本的trx_id属性值在ReadView的min_trx_id和max_trx_id之间，那就需要判断一下trx_id属性值是不是在m_ids列表中  
            如果在，说明创建ReadView时生成该版本的事务还是活跃的，该版本不可以被访问；如果不在，说明创建ReadView时生成该版本的事务已经被提交，该版本可以被访问。
            5. 如果某个版本的数据对当前事务不可见的话，那就顺着版本链找到下一个版本的数据，继续按照上边的步骤判断可见性，依此类推，直到版本链中的最后一个版本。  
            如果最后一个版本也不可见的话，那么就意味着该条记录对该事务完全不可见，查询结果就不包含该记录
    8.事务级别
        * READ UNCOMMITTED,直接读取 记录的最新版本就好
        * READ COMMITTED 在每次查询开始时都会生成一个独立的ReadView。
        * REPEATABLE READ 在第一次读取数据时生成一个ReadView,所以事务中每次读取的ReadView相同,得到的行数据下roll_pointer相同
        * SERIALIZABLE(串行化)在第一个事务更新了某条记录后，就会给这条记录加锁，另一个事务再次更新时就需要等待第一个事务提交了，把锁释放之后才可以继续更新
    
6. 服务器处理客户端请求过程 处理连接->查询缓存->语法解析->查询优化->存储引擎
7. 字符编码解析过程
    1. 客户端使用操作系统的字符集编码发送字符串到mysql
    2. mysql根据配置character_set_client节码,转成character_set_connection(理解为mysql字符集)字符集  
    3. 执行sql一系列操作将返回结果转为character_set_results对应的字符集
    4. 操作系统接受到结果，转为自身的字符集
8. 索引
    1. 聚簇索引
        * 索引数据与主键一致按顺序排列分布在不同的页中,且页数不连续,由指针构成链表
        * 索引由专门的索引页构成，其中设置行的头信息record_type=1,存放页的用户记录中最小的主键值,页号
        * 在加一个索引页存放2设置的索引页,加快找到对应的数据索引页,这样形式上构成了一颗B+树
        * B+树的叶子节点存储的是完整的用户记录。
        * 聚簇索引并不需要我们在MySQL语句中显式的使用INDEX语句去创建.InnoDB存储引擎会自动的为我们创建聚簇索引
    2. 二级索引
        * 根据设置的非主键建立的索引字段创建一个新的B+树
        * 中间一层目录节点与聚簇索引不同,存储为三个部分索引列的值,(主键值),页号,这样就保证主键值+页号唯一性,避免了索引列的值  
        相同情况,新的相同索引列插入时不知道插入那张页的问题
        * B+树的叶子节点存储的并不是完整的用户记录，而只是索引列+主键这两个列的值
        * 所以根据二级索引需要先在该索引中查询出主键,再通过主键查询聚簇索引查询到值,这一步操作也叫回表
    3. 联合索引
        * 多个字段建立的索引为联合索引，本质上也是一个二级索引
        * 排序顺序按字段顺序来,如C1,C2两字段为组合索引,按C1排序,若C1相同则再按C2来
    4. 一个B+树索引的根节点自诞生之日起，便不会再移动
    5. InnoDB和MyISAM会自动为主键或者声明为UNIQUE的列去自动建立B+树索引    
    6.索引原则
        * 联合索引第二点说明了在联合索引中的最左匹配原则
        * 索引排序字符串比较大小是先按一位位字符从前往后比较,所以前缀都是排好序的,所以可以得到like查询时,只支持后面模糊查询走索引
        * 范围查询时,因为索引字段是排序的,所以支持索引的,但是联合时,只有最左有完整的链路,所以只支持最左范围查询。同理,当最左  
        的查询时精确查询(=)时,第二个索引字段可以范围查询
        * order by 若是索引字段则也减去了排序时间,当然若是联合索引也必须按照索引顺序order by
        * 当批量查询数据时,第一次查询二级索引,很快速可以查询出需要的页于其对应的主键集合,这是顺序I/O,但是根据主键查询聚簇索引  
        查询对应行数据时,因为主键地址不是连续的,所以是随机I/O,当集合非常大时,效率很低,这就是回表的代价,这时候不如采用全表查询
        * 如果索引列在比较表达式中不是以单独列的形式出现，而是以某个表达式，或者函数调用形式出现的话，是用不到索引的
        * 对应主键索引来说,非顺序性会进行行链表的断开再插入,顺序只会插入,所以让主键具有AUTO_INCREMENT，让存储引擎自己为表生成主键，而不是我们0手动插入
9. 执行计划
    * 级别,从快到慢
        1. const(常量级别) 根据聚簇索引或者二级索引(唯一)=号查询,直接找到地址
        2. ref根据二级索引=号查询,因为二级索引不具有唯一性,有可能有多个key(二级索引字段)->主键id,所以会查出多条
        3. ref_or_null有时候我们不仅想找出某个二级索引列的值等于某个常数的记录，还想把该列的值为NULL的记录也找出来
        4. range查询符号为=、<=>、IN、NOT IN、IS NULL、IS NOT NULL、>、<、>=、<=、BETWEEN、!=或者LIKE操作符连接起来
        5. index 查询列为联合索引的索引列,且搜索条件也是联合索引的列,可以通过遍历联合索引的列来匹配条件
        6. all 全表
    * 当多个二级索引进行查询时,会根据表的统计数据来判断到底使用哪个条件到对应的二级索引中查询扫描的行数会更少,从而根据该条件查询二级  
    索引,然后回表(根据结果查询聚簇索引) 得到的数据进行后续的判断  
    * Intersection交集索引可以在某个查询使用多个二级索引，将从多个二级索引中查询到的结果取交集(AND)
        1. 二级索引列是等值匹配的情况，对于联合索引来说，在联合索引中的每个列都必须等值匹配，不能出现只匹配部分列的情况
        2. 主键列可以是范围匹配(因为二级索引的用户记录是由索引列 + 主键构成的，二级索引列的值相同的记录可能会有好多条，这些索引列  
        的值相同的记录又是按照主键的值进行排序的)
    * Union并集索引(Intersection和Union本质上都是合并一部分查询结果)
        1. 二级索引列是等值匹配的情况
        2. 主键列可以是范围匹配
        3. 使用Intersection索引合并的搜索条件
    * Sort-Union合并,当两个二级索引为区间查询时且关联条件为or.这种情况会把两个区间条件根据条件查出再按主键排序,然后再合并,比  
    Union多了一步排序
    * 当多表连接时,根据条件选择一张表进行查询,这张表称为驱动表,然后根据驱动表的结果每条记录去多张表生成的笛卡尔积中删选出  
    结果再次进行根据条件查询,访问次数取决于对驱动表执行单表查询后的结果集中的记录条数    
    * 内连接和外连接的根本区别就是在驱动表中的记录不符合ON子句中的连接条件时不会把该记录加入到最后的结果集
    * 基于块的嵌套循环连接。提前划出一块内存（join buffer）存储驱动表结果集中的记录，然后开始扫描被驱动表，每一条被驱动表的  
    记录一次性和这块内存中的多条驱动表记录匹配，可以显著减少被驱动表的I/O操作
10. 规则优化
    1. 条件简化,如移除不必要的括号,常量传递,等值传递,移除没用的条件,表达式计算,HAVING子句和WHERE子句的合并
    2. 外连接消除:在被驱动表的WHERE子句符合空值拒绝的条件后，外连接和内连接可以相互转换,这种转换带来的好处就是查询优化器  
    可以通过评估表的不同连接顺序的成本，选出成本最低的那种连接顺序来执行查询
    3. 对于子查询,若子查询和父亲无关连,先执行只查询再跟据结果集查询父亲,若父子有连,先查询父亲中的结果,循环出结果集每一条数据  
    将关联字段带入子查询,若满足则加入最终结果集
    4. 对于In查询,当里面特别多时不直接将不相关子查询的结果集当作外层查询的参数，而是将该结果集写入一个临时表里
11. 日志
    * redo日志
        1. 因为缓存区不会立即将数据提交,当系统断电,放入缓存的地方是内存,所以断电会不见,为了持久化,所以引出redo日志
        2. redo日志会把事务在执行过程中对数据库所做的所有修改都记录下来，在之后系统崩溃重启后可以把事务所做的任何修改都恢复出来
    * undo日志
        1. 回滚的本质就是把回滚时所需的东西都给记下来,称为撤销日志undo log
            * 你插入一条记录时，至少要把这条记录的主键值记下来，之后回滚的时候只需要把这个主键值对应的记录删掉就好了
            * 你删除了一条记录，至少要把这条记录中的内容都记下来，这样之后回滚时再把由这些内容组成的记录插入到表中就好了
            * 你修改了一条记录，至少要把修改这条记录前的旧值都记录下来，这样之后回滚时再把这条记录更新为旧值就好了
        2. insert undo在事务提交之后就可以被释放掉了，而update undo由于还需要支持MVCC，不能立即删除掉
        3. 为了支持MVCC，对于delete mark操作来说，仅仅是在记录上打一个删除标记，并没有真正将它删除掉
12. 锁
    1. S(共享锁,行级别),IS(意向共享锁,极一条数据要获取行锁时现在表上加一条带我我要获取X锁的表锁),X,IX(排他锁,意向排他锁)
    2. 对表结果修改时,会导致语句阻塞,同理,事务执行时会对表修改阻塞.DDL语句执行时会隐式的提交当前会话中的事务
    3. 为了解决幻读,提出gap锁,当给聚簇索引某行加上他后,他与前面一行不允许进行插入的,可以在尾节点record_type=3的节点加入GapLock,  
    保证该页不会有数据在数据节点后再插入,同样在内存会有存储意向插入GapLock

# Spring源码部分       
1. @Import 可以传入四种类型：普通类、配置类、ImportSelector 的实现类,ImportBeanDefinitionRegistrar的实现类
2. @EnableAutoConfiguration(@AutoConfigurationPackage,@Import(AutoConfigurationImportSelector.class))
    * @AutoConfigurationPackage,表示包含该注解的类所在的包应该在 AutoConfigurationPackages 中注册
    * AutoConfigurationImportSelector:读取META-INF/spring.factories(spring-boot-autoconfigure)下所有的自动配置类装配到IOC容器中，之后自动配置类就会  
    通过 ImportSelector 和 @Import 的机制被创建出来，之后就生效了
3. SPI是一种动态替换发现的机制,在META-INF/services里面声明接口的全类名,通过ServiceLoader加载出实现它的子类
4. @WebMvcAutoConfiguration为springMvc的配置类(spring-boot-autoconfigure下META-INF/spring.factories里包含)
    * 代码
         ``` 
                    @Configuration
                    //当前环境必须是WebMvc（Servlet）环境
                    @ConditionalOnWebApplication(type = Type.SERVLET)
                    //当前运行环境的classpath中必须有Servlet类，DispatcherServlet类，WebMvcConfigurer类
                    @ConditionalOnClass({ Servlet.class, DispatcherServlet.class, WebMvcConfigurer.class })
                    //如果没有自定义WebMvc的配置类，则使用本自动配置
                    @ConditionalOnMissingBean(WebMvcConfigurationSupport.class)
                    @AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE + 10)
         *           // @AutoConfigureAfter表示该类需要在DispatcherServletAutoConfiguration实例化后执行
                    @AutoConfigureAfter({ DispatcherServletAutoConfiguration.class,
                    		TaskExecutionAutoConfiguration.class, ValidationAutoConfiguration.class })
                    public class WebMvcAutoConfiguration {
         ```
    * WebMvcAutoConfiguration->DispatcherServletAutoConfiguration->ServletWebServerFactoryAutoConfiguration
        1. ServletWebServerFactoryAutoConfiguration下通过@Import导入ServletWebServerFactoryConfiguration.EmbeddedTomcat.class
            ``` 
                @Configuration
                //@ConditionalOnClass表示当前classPath下必须有Tomcat 这个类，该配置类才会生效
           	*     @ConditionalOnClass({ Servlet.class, Tomcat.class, UpgradeProtocol.class })
           	    @ConditionalOnMissingBean(value = ServletWebServerFactory.class, search = SearchStrategy.CURRENT)
           	    public static class EmbeddedTomcat {
            
           	    	@Bean
           	    	public TomcatServletWebServerFactory tomcatServletWebServerFactory() {
           	    	//初始化Tomcat
           	    		return new TomcatServletWebServerFactory();
           	    	}
            
           	    }
            ```
        2. DispatcherServletAutoConfiguration分别注册 DispatcherServlet和DispatcherServletRegistrationBean
        3. WebMvcAutoConfiguration注册了国际化组件,视图解析器,静态资源映射,主页的设置,应用图标的设置等等
        4. WebMvcAutoConfiguration注册了国际化组件注册了SpringWebMvc 中最核心的两个组件：处理器适配器、处理器映射器。
        5. 里面大部分使用了内部静态类，为了不向外暴露这个内部类而已；毕竟只是在外部类配置场景需要用到
5. 启动流程
    1. 创建SpringApplication
        ``` 
            @SuppressWarnings({ "unchecked", "rawtypes" })
            public SpringApplication(ResourceLoader resourceLoader, Class<?>... primarySources) {
                // resourceLoader为null
                this.resourceLoader = resourceLoader;
                Assert.notNull(primarySources, "PrimarySources must not be null");
                // 将传入的DemoApplication启动类放入primarySources中，这样应用就知道主启动类在哪里，叫什么了
                // SpringBoot一般称呼这种主启动类叫primarySource（主配置资源来源）
                this.primarySources = new LinkedHashSet<>(Arrays.asList(primarySources));
                // 3.1 判断当前应用环境
                this.webApplicationType = WebApplicationType.deduceFromClasspath();
                // 3.2 设置初始化器
                setInitializers((Collection) getSpringFactoriesInstances(ApplicationContextInitializer.class));
                // 3.3 设置监听器
                setListeners((Collection) getSpringFactoriesInstances(ApplicationListener.class));
                // 3.4 确定主配置类
                this.mainApplicationClass = deduceMainApplicationClass();
            }
        ```
        * (3.1)WebApplicationType.deduceFromClasspath()来决定是什么环境，本质上扫描是否有Reactiv的包,是否  
          有Servlet相关的类,若都没有就是个普通应用
        * (3.2)Initializers用于在刷新容器之前初始化Spring ConfigurableApplicationContext 的回调接口
        * (3.2)三种实现方式:实现ApplicationContextInitializer,application.properties中配置,spring.factories中配置   
        * (3.3)ApplicationListener由应用程序事件监听器实现的接口。基于观察者模式的标准 java.util.EventListener 接口。
        * (3.4)从deduceMainApplicationClass 方法开始往上爬，哪一层调用栈上有main方法，方法对应的类就是主配置类，就返回这个类
    2. run方法
        ``` 
        public ConfigurableApplicationContext run(String... args) {
            // 4.1 创建StopWatch对象
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            // 4.2 创建空的IOC容器，和一组异常报告器
            ConfigurableApplicationContext context = null;
            Collection<SpringBootExceptionReporter> exceptionReporters = new ArrayList<>();
            // 4.3 即使没有检测到显示器,也允许其启动.对于服务器来说,是不需要显示器的,所以要这样设置
            configureHeadlessProperty();
            // 4.4 获取SpringApplicationRunListeners，并调用starting方法（回调机制）
            SpringApplicationRunListeners listeners = getRunListeners(args);
            // 【回调】首次启动run方法时立即调用。可用于非常早期的初始化（准备运行时环境之前）。
            listeners.starting();
            try {
                // 将main方法的args参数封装到一个对象中
                ApplicationArguments applicationArguments = new DefaultApplicationArguments(args);
                // 4.5 准备运行时环境
                ConfigurableEnvironment environment = prepareEnvironment(listeners, applicationArguments);
               // 4.6 如果有配置 spring.beaninfo.ignore，则将该配置设置进系统参数
               configureIgnoreBeanInfo(environment);
               // 4.7 打印SpringBoot的banner
               Banner printedBanner = printBanner(environment);
               // 4.8 创建ApplicationContext
               context = createApplicationContext();
               // 初始化异常报告器
               exceptionReporters = getSpringFactoriesInstances(SpringBootExceptionReporter.class,
                       new Class[] { ConfigurableApplicationContext.class }, context);
               // 4.9 初始化IOC容器
               prepareContext(context, environment, listeners, applicationArguments, printedBanner);
               // 4.10 bean的生命流程,刷新容器,最重要
               refreshContext(context);
              // 4.11 刷新后的处理
               afterRefresh(context, applicationArguments);
               stopWatch.stop();
               if (this.logStartupInfo) {
                   new StartupInfoLogger(this.mainApplicationClass).logStarted(getApplicationLog(), stopWatch);
               }
               // 4.12 发布started事件
               listeners.started(context);
               // 4.13 运行器回调
               callRunners(context, applicationArguments);
            }
        ```
        * (4.1)new 一个StopWatch用于统计run启动过程花了多少时间
        * (4.4)SpringApplicationRunListeners是监听SpringApplication运行方法。 
        * (4.5)Environment它是IOC容器的运行环境，它包括Profile和Properties两大部分，它可由一个到几个激活的Profile共同配置，  
        它的配置可在应用级Bean中获取 
        * (4.5)意义为准备环境变量，包括系统变量，环境变量，命令行参数，默认变量，servlet相关配置变量，随机值JNDI属性值，  
        以及配置文件（比如application.properties）等，注意这些环境变量是有优先级的 
        * (4.8)里面根据运行环境创建ApplicationContext,在实例化时先初始化父类构造方法创建(this.beanFactory = new DefaultListableBeanFactory();)
        * (4.9)IOC容器初始化工作
            - 将创建好的应用环境设置到IOC容器中
            - 将beanName生成器,资源加载器,类加载器,类转化器放入IOC
            - 会获取到所有 Initializer，调用initialize方法
            - 生成一个bean加载器(就是注解驱动的Bean定义解析器,Xml定义的Bean定义解析器类路径下的Bean定义扫描器的整合)
            - 进入load()方法,循环所有的sources(仅主启动类一个),根据类型判断(判断为类),再判断是否含有注解Component,最后annotatedReader.register
            - 到这一步,就将主启动类生成为BeanDefinition 放入了IOC容器中
        * (4.10)刷新容器
            ``` 
                public void refresh() throws BeansException, IllegalStateException {
                    synchronized (this.startupShutdownMonitor) {
                        // Prepare this context for refreshing.
                        // 1. 初始化前的预处理
                        prepareRefresh();
                
                        // Tell the subclass to refresh the internal bean factory.
                        // 2. 获取BeanFactory，加载所有bean的定义信息（未实例化）
                        ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();
                
                        // Prepare the bean factory for use in this context.
                        // 3. BeanFactory的预处理配置
                        prepareBeanFactory(beanFactory);
                
                        try {
                            // Allows post-processing of the bean factory in context subclasses.
                            // 4. 准备BeanFactory完成后进行的后置处理
                            postProcessBeanFactory(beanFactory);
                
                            // Invoke factory processors registered as beans in the context.
                            // 5. 执行BeanFactory创建后的后置处理器
                            invokeBeanFactoryPostProcessors(beanFactory);
                
                            // Register bean processors that intercept bean creation.
                            // 6. 注册Bean的后置处理器
                            registerBeanPostProcessors(beanFactory);
                
                            // Initialize message source for this context.
                            // 7. 初始化MessageSource（SpringMVC）
                            initMessageSource();
                
                            // Initialize event multicaster for this context.
                            // 8. 初始化事件派发器
                            initApplicationEventMulticaster();
                
                            // Initialize other special beans in specific context subclasses.
                            // 9. 子类的多态onRefresh
                            //SpringBoot 扩展的IOC容器中对这个方法进行了真正地实现
                            onRefresh();
                
                            // Check for listener beans and register them.
                            // 10. 注册监听器
                            registerListeners();
                          
                            //到此为止，BeanFactory已创建完成
                
                            // Instantiate all remaining (non-lazy-init) singletons.
                            // 11. 初始化所有剩下的单例Bean
                            finishBeanFactoryInitialization(beanFactory);
                
                            // Last step: publish corresponding event.
                            // 12. 完成容器的创建工作
                            finishRefresh();
                        }
                
                        catch (BeansException ex) {
                            if (logger.isWarnEnabled()) {
                                logger.warn("Exception encountered during context initialization - " +
                                        "cancelling refresh attempt: " + ex);
                            }
                
                            // Destroy already created singletons to avoid dangling resources.
                            destroyBeans();
                
                            // Reset 'active' flag.
                            cancelRefresh(ex);
                
                            // Propagate exception to caller.
                            throw ex;
                        }
                
                        finally {
                            // Reset common introspection caches in Spring's core, since we
                            // might not ever need metadata for singleton beans anymore...
                            // 13. 清除缓存
                            resetCommonCaches();
                        }
                    }
                }
            ``` 
            * (1)其中initPropertySources()虽然为空方法,但是在servlet环境下有实现,会把web.xml等信息放入IOC
            * (3)BeanPostProcessor它通常被称为 “Bean的后置处理器“它可以在对象实例化但初始化之前，以及初始化之后进行一些后置处理
            * (4)org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext#postProcessBeanFactory为实现类
            * (4)把ServletContext,ServletConfig注入到组件中,且将Web的几种作用域注册到 BeanFactory 中。
            * (5)执行beanFactory的后置处理器,里面进行了包扫描等等操作
            * (5)注意执行的org.springframework.context.annotation.ConfigurationClassPostProcessor.postProcessBeanDefinitionRegistry,会加载出所有类信息
            * (9)实现类在SpringBoot org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext#onRefresh
            * (9)在这里创建了Tomcat
            * (12)ServletWebServerApplicationContext 还重写了 finishRefresh 方法,调用了 WebServer 的start方法真正启动嵌入式Web容器
            
6. IOC小结
    * 执行顺序 构造方法->BeanPostProcessor的before方法->@PostConstruct/init-method->InitializingBean的afterPropertiesSet方法->BeanPostProcessor的after方法 
    * BeanFactoryPostProcessor是在所有的 BeanDefinition 已经被加载，但没有Bean被实例化,可以对 BeanFactory 进行后置处理。BeanDefinitionRegistryPostProcessor  
    它的执行时机是所有Bean的定义信息即将被加载但未实例化时，也就是先于 BeanFactoryPostProcessor;总结就是【规律】BeanPostProcessor 是对Bean的后置处理，BeanFactoryPostProcessor   
    是对 BeanFactory 的后置处理
    * spring会把classPath下所有class文件解析出来,然后使用类加载器，把传入的根包以Resource的形式加载出来,然后通过一个MetadataReader来解析.class文件，  
    它就可以读取这个class的类定义信息、注解标注信息。之后要用 MetadataReader 来判断这个class是否为一个 Component,判定为 Component 后，  
    会将这个class封装为 BeanDefinition，最后返回
    * org.springframework.context.annotation.AnnotationBeanNameGenerator为注解的bean名称生成器,规则为看这些模式注解上是否有  
    显式的声明 value 属性，如果没有则进入下面的 buildDefaultBeanName 方法，它会取类名的全称，之后调 Introspector.decapitalize 方法将首字母转为小写
    * SpringBoot 会根据classpath下存在的类，决定当前应用的类型，以此来创建合适的IOC容器。默认WebMvc环境下，创建的IOC容器是 AnnotationConfigServletWebServerApplicationContext
    * SpringBoot 使用 SpringFactoriesLoader.loadFactoryNames 机制来从 META-INF/spring.factories 文件中读取指定 类/注解 映射的组件全限定类名，  
    以此来反射创建组件。Spring设计的SPI比Java原生的SPI要更灵活，因为它的key可以任意定义类/注解，不再局限于“接口-实现类”的形式
    * SpringApplicationRunListener 可以监听 SpringApplication 的运行方法。通过注册 SpringApplicationRunListener ，  
    可以自定义的在 SpringBoot 应用启动过程、运行、销毁时监听对应的事件，来执行自定义逻辑
    * Spring应用的IOC容器需要依赖 Environment - 运行环境，它用来表示整个Spring应用运行时的环境，它分为 profiles 和 properties 两个部分。  
    通过配置不同的 profile ，可以支持配置的灵活切换，并且可以同时配置一到多个 profile 来共同配置 Environment
    * 后置处理器
        - BeanPostProcessor：Bean实例化后，初始化的前后触发
        - BeanDefinitionRegistryPostProcessor：所有Bean的定义信息即将被加载但未实例化时触发
        - BeanFactoryPostProcessor：所有的 BeanDefinition 已经被加载，但没有Bean被实例化时触发
        - InstantiationAwareBeanPostProcessor：Bean的实例化对象的前后过程、以及实例的属性设置（AOP）
        - InitDestroyAnnotationBeanPostProcessor：触发执行Bean中标注 @PostConstruct 、@PreDestroy 注解的方法
        - ConfigurationClassPostProcessor：解析加了 @Configuration 的配置类，解析 @ComponentScan 注解扫描的包，以及解析 @Import 、@ImportResource 等注解
        - AutowiredAnnotationBeanPostProcessor：负责处理 @Autowired 、@Value 等注解
7. 循环依赖
    * 重要集合
        - singletonObjects：一级缓存，存放完全初始化好的Bean的集合，从这个集合中取出来的Bean可以立马返回
        - earlySingletonObjects：二级缓存，存放创建好但没有初始化属性的Bean的集合，它用来解决循环依赖
        - singletonFactories：三级缓存，存放单实例Bean工厂的集合
        - singletonsCurrentlyInCreation：存放正在被创建的Bean的集合                
    * 思路
        - 初始化 Bean 之前，将这个 bean 的 name 放入三级缓存
        - 创建 Bean 将准备创建的 Bean 放入 singletonsCurrentlyInCreation （正在创建的 Bean ）
        - createNewInstance 方法执行完后执行 addSingletonFactory，将这个实例化但没有属性赋值的 Bean 放入三级缓存，
        并从二级缓存中移除,一般情况下初次创建的 bean 不会存在于二级缓存，故该步骤可以简单理解为仅仅是放入了三级缓存而已
        - 属性赋值&自动注入时，引发关联创建
        - 关联创建时判断
            1. 检查“正在被创建的 Bean ”中是否有即将注入的 Bean
            2. 如果有，检查二级缓存中是否有当前创建好但没有赋值初始化的 Bean
            3. 如果没有，检查三级缓存中是否有正在创建中的 Bean
            4. 至此一般会有，将这个 Bean 放入二级缓存，并从三级缓存中移除
        - 之后 Bean 被成功注入，最后执行 addSingleton，将这个完全创建好的Bean放入一级缓存，从二级缓存和三级缓存移除，
        并记录已经创建了的单实例Bean    
8. AOP
    * 通过@EnableAspectJAutoProxy中@Import(AspectJAutoProxyRegistrar.class)创建出AnnotationAwareAspectJAutoProxyCreator
    * AnnotationAwareAspectJAutoProxyCreator扩展了InstantiationAwareBeanPostProcessor接口,而其它用于组件的创建前后做后置处理
    * createBean第一次执行后置处理器AbstractAutoProxyCreator(AspectJAwareAdvisorAutoProxyCreator)
    * 后置处理器里面本质上是创建动态代理的配置类,默认单例返回为空.里面就仅仅根据@aspect生成代理增强器放入IOC
    * 进入doCreateBean->initializeBean ->applyBeanPostProcessorsAfterInitialization 又回到AbstractAutoProxyCreator里进行正真代理
    * 后置处理器中先获取所有增加器,然后筛选出可用的,在加个ExposeInvocationInterceptor 类型的增强器
    * 创建代理工厂,将增加的代理器组合成一个增加器，放入工厂中
    * 然后判断如果目标对象有接口，用jdk动态代理；没有接口，用cglib动态代理。
    * jdk通过Proxy.newProxyInstance实现动态代理
9. JDK目标方法执行JdkDynamicAopProxy
    * jdk动态代理借助接口实现，并且在创建代理对象之前还注入了额外的接口
    * 核心思想都是获取增强器调用链，然后链式执行增强器（拦截器）
    * 执行拦截器链时，为保证拦截器链能有序执行，会引入下标索引机制
10. 事务
    * 执行流程
        - @EnableTransactionManagementz组件@Import最后默认创建AutoProxyRegistrar,ProxyTransactionManagementConfiguration
        - AutoProxyRegistrar在调用beanFactoryPostProcessors时通过后置处理器(postProcessBeanDefinitionRegistry)读取出来
        - AutoProxyRegistrar里面生成创建InfrastructureAdvisorAutoProxyCreator
        - InfrastructureAdvisorAutoProxyCreator得父类也是AbstractAdvisorAutoProxyCreator(AOP得父类),所以实现也一样
        - ProxyTransactionManagementConfiguration注册了多个组件，用来生成事务增强器、事务切入点解析器、事务配置源、事务拦截器等组件  
        - 本质上还是经过动态代理得到JdkDynamicAopProxy,然后invoke,里面通过try{时间执行}catch{回滚}finally{清除缓存}提交
    * 事务传播行为原理
        1. 7种类型
            - PROPAGATION_REQUIRED:【默认值：必需】当前方法必须在事务中运行，如果当前线程中没有事务，则开启一个新的事务；
            如果当前线程中已经存在事务，则方法将会在该事务中运行。
            - PROPAGATION_SUPPORTS:【支持】当前方法单独运行时不需要事务，但如果当前线程中存在事务时，方法会在事务中运行
            - PROPAGATION_MANDATORY:【强制】当前方法必须在事务中运行，如果当前线程中不存在事务，则抛出异常
            - PROPAGATION_REQUIRES_NEW:【新事务】当前方法必须在独立的事务中运行，如果当前线程中已经存在事务，则将该事务挂起，
            重新开启一个事务，直到方法运行结束再释放之前的事务
            - PROPAGATION_NOT_SUPPORTED:【不支持】当前方法不会在事务中运行，如果当前线程中存在事务，则将事务挂起，直到方法运行结束
            - PROPAGATION_NEVER:【不允许】当前方法不允许在事务中运行，如果当前线程中存在事务，则抛出异常
            - PROPAGATION_NESTED:【嵌套】当前方法必须在事务中运行，如果当前线程中存在事务，则将该事务标注保存点，形成嵌套事务。
            嵌套事务中的子事务出现异常不会影响到父事务保存点之前的操作。