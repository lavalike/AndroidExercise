### Apk安装
1. 将Apk文件复制到**data/app**目录
2. 解析apk信息
3. dexopt操作
4. 更新权限信息
5. 完成安装，发送**Intent.ACTION_PACKAGE_ADDED**广播

![](https://tva1.sinaimg.cn/large/007S8ZIlgy1ghx9hctdh1j30uy0mj0v9.jpg)


### java泛型擦除、类型擦除
上限通配符 <? extends T> 限定为T和T的子类型  
下限通配符 <? super T> 限定为T和T的父类型

Context数量=Activity数量+Service数量+1(Application)

### AndroidStudio点击build

* aapt 资源打包生成R.java和Resource文件
* aidl 处理aidl文件，生成java文件
* javac 编译生成class文件
* dex 转换class为dex文件
* jarsigner 签名
* zipalign 对签名后的apk对齐处理，所有资源文件距离文件起始偏移4字节的整数倍，加快访问速度

### 对象逃逸状态
* 全局级别逃逸：一个对象可能从一个方法或者当前线程中逃逸
* 参数级别逃逸：如果一个对象被作为参数传递给另一方法，但在这个方法之外无法访问或对其他线程不可见，这个对象标记为参数级别逃逸
* 无逃逸状态

###  双亲委派
> 虽然有“父子”关系，但实际上并不是继承关系，而是通过组合方式实现

分层：
1. 根加载器BootStrap ClassLoader（通过getParent获取为null）
2. 扩展类加载器 ExtClassLoader
3. 应用程序加载器 AppClassLoader（如无自定义加载器，这个就是默认加载器，代码一般通过它加载）

过程：  
一个类加载器收到了类加载请求，加载器并不会直接加载，而是委派个父类加载器完成，只有父类加载器无法完成时，才到子类加载器中加载。
1. 先根据findLoadedClass确定是不是已经加载过
2. 如果没有，就委派父类加载器进行加载
3. 如果父类没有加载成功，抛出ClassNotFoundException，并调用本加载器的findClass进行加载

### 加固与混淆壳dex文件包括：
1. 自定义DexClassLoader
2. ProxyApplication
	1. attachBaseContext方法：读取加密dex并解密，使用自定义DexClassLoader加载解密后的dex
	2. onCreate方法：创建正确的Application并设置给ActivityThread

Dex加解密  
![](https://tva1.sinaimg.cn/large/007S8ZIlgy1ghgwvrdoewj30ph0ilacq.jpg)
加固  
( [原dex文件] -> [加密后的dex文件] ) + [壳dex文件] = [新的classes.dex文件] -> 重打包签名 -> [加固后的apk]

启动  
[App启动] -> [解密源程序] -> [初始化自定义类加载器] -> [反射设置LoadedApk中加载器对象为自定义加载器] -> [获取源程序中Application名称] -> [反射产生正确的Application] -> [反射设置ActivityThread中Application信息] -> [源程序正常运行]

### Hook
不影响原有逻辑，以Hook点击事件为例  
1. 获取View#getListenerInfo方法  
2. 获取mListenerInfo的mOnClickListener成员变量  
3. 使用自定义View.OnClickListener包裹

注意：需先设置点击事件再hook才会生效

```java
private fun hook() {
    //拿到mListenerInfo
    val method: Method = View::class.java.getDeclaredMethod("getListenerInfo")
    method.isAccessible = true
    val mListenerInfo = method.invoke(btn_normal)
    //拿到mOnClickListener
    val field = Class.forName("android.view.View\$ListenerInfo").getDeclaredField("mOnClickListener")
    val originalListener: View.OnClickListener = field.get(mListenerInfo) as View.OnClickListener
    field.set(mListenerInfo, HookClickListener(originalListener))
}

class HookClickListener(private val original: View.OnClickListener?) : View.OnClickListener {
    override fun onClick(v: View) {
        Toast.makeText(v.context, "Hooked !", Toast.LENGTH_SHORT).show()
        original?.onClick(v)
    }
}
```

### Bitmap复用

**存储**

*  2.3.3及以前版本，像素点保存在native memory，bitmap对象保存在dalvik heap
*  3.0以后，像素点数据与bitmap数据一起保存在dalvik heap
*  8.0以后，像素点数据保存在native memory

**复用**

在Android 3.0开始引入了inBitmap设置，通过设置这个参数，在图片加载的时候可以使用之前已经创建了的Bitmap，但是需要大小一样,以便节省内存，避免再次创建一个Bitmap。在Android4.4，新增了允许inBitmap设置的图片与需要加载的图片的大小不同的情况，只要inBitmap的图片比当前需要加载的图片大就好了。

复用注意点：  

* Bitmap一定要是可变的，即inmutable设置一定为ture
* Android4.4以下的平台，须要保证inBitmap和即将要得到decode的Bitmap的尺寸规格一致
* Android4.4及其以上的平台，仅仅须要满足inBitmap的尺寸大于要decode得到的Bitmap的尺寸规格就可以


### 启动流程

#### 系统启动流程
1. 启动电源以及系统启动

	当电源键按下时引导芯片代码从预定义的地方（固化在ROM）开始执行。加载引导程序BootLoader到RAM中，然后执行。
	
2. 引导程序BootLoader

	引导程序BootLoader是在Android操作系统开始运行前的一个小程序，它的主要作用是把系统OS拉起来并运行。

3. Linux内核启动
	
	当内核启动时，设置缓存、被保护存储器、计划列表、加载驱动。当内核完成系统设置时，它首先在系统文件中寻找init.rc文件，并启动init进程。
	
4. init进程启动
	
	初始化和启动属性服务，并且启动Zygote进程。
	
5. Zynote进程启动

	创建java虚拟机并为java虚拟机注册JNI方法，创建服务器端Socket，启动SystemServer进程。
	
6. SystemServer进程启动

	启动Binder线程池和SystemServiceManager，并且启动各种系统服务。
	
7. Launcher启动

	被SystemServer进程启动的AMS会启动Launcher，Launcher启动后会将已安装应用的快捷图标显示到界面上。
	

#### 理论
**Launcher**

AndroidManifest.xml **android.intent.category.HOME**

**zygote**

zygote意为“受精卵“。Android是基于Linux系统的，而在Linux中，所有的进程都是由init进程直接或者是间接fork出来的，zygote进程也不例外。在Android系统里面，zygote是一个进程的名字。Android是基于Linux System的，当你的手机开机的时候，Linux的内核加载完成之后就会启动一个叫“init“的进程。在Linux System里面，所有的进程都是由init进程fork出来的，我们的zygote进程也不例外。每个APP都是：

> 一个单独的dalvik虚拟机  
> 一个单独的进程

所以当系统里面的第一个zygote进程运行之后，在这之后再开启App，就相当于开启一个新的进程。而为了实现资源共用和更快的启动速度，Android系统开启新进程的方式，是通过fork第一个zygote进程实现的。所以说，除了第一个zygote进程，其他应用所在的进程都是zygote的子进程。这下你明白为什么这个进程叫“受精卵”了吧？因为就像是一个受精卵一样，它能快速的分裂，并且产生遗传物质一样的细胞！

**SystemServer**

SystemServer也是一个进程，而且是由zygote进程fork出来的。
知道了SystemServer的本质，我们对它就不算太陌生了，这个进程是Android Framework里面两大非常重要的进程之一,另外一个进程就是上面的zygote进程。为什么说SystemServer非常重要呢？因为系统里面重要的服务都是在这个进程里面开启的，比如ActivityManagerService、PackageManagerService、WindowManagerService等等。

**ActivityManagerService**

AMS是Android中最核心的服务之一，主要负责系统中四大组件的启动、切换、调度及应用进程的管理和调度等工作，其职责与操作系统中的进程管理和调度模块相类似，因此它在Android中非常重要，它本身也是一个Binder的实现类。
ActivityManagerService进行初始化的时机很明确，就是在SystemServer进程开启的时候，就会初始化。

**Instrumentation和ActivityThread**

每个Activity都持有Instrumentation对象的一个引用，但是整个进程只会存在一个Instrumentation对象。  
Instrumentation这个类里面的方法大多数和Application和Activity有关，这个类就是完成对Application和Activity初始化和生命周期的工具类。Instrumentation这个类很重要，对Activity生命周期方法的调用根本就离不开他，他可以说是一个大管家。  
ActivityThread，就是UI线程，应用的入口类，通过调用main方法，开启消息循环队列。App和AMS是通过Binder传递信息的，那么ActivityThread就是专门与AMS的外交工作的。

**流程图**
![](https://tva1.sinaimg.cn/large/007S8ZIlgy1ghp0uy77o9j30ic0e30tw.jpg)

1. 点击桌面App图标，Launcher进程采用Binder IPC向system_server进程发起startActivity请求；
2. system_server进程接收到请求后，向zygote进程发送创建进程的请求；
3. Zygote进程fork出新的子进程，即App进程；
4. App进程，通过Binder IPC向sytem_server进程发起attachApplication请求；
5. AMS向ApplicationThreadProxy发送realStartActivityLocked请求。
6. system_server进程在收到请求后，进行一系列准备工作后，再通过binder IPC向App进程发送scheduleLaunchActivity请求；
7. App进程的binder线程（ApplicationThread）在收到请求后，通过handler向主线程发送LAUNCH_ACTIVITY消息；
8. 主线程在收到Message后，通过调用handleLaunchActivity创建目标Activity，并回调Activity.onCreate()等方法。
9. 到此，App便正式启动，开始进入Activity生命周期，执行完onCreate/onStart/onResume方法，UI渲染结束后便可以看到App的主界面。

### LeakCanary原理
![](https://tva1.sinaimg.cn/large/007S8ZIlgy1ghqb8ma6h9j30jp0bgdgi.jpg)

### 电量优化
* **监听手机充电状态**  
得到充电状态信息之后，我们可以有针对性的对部分代码做优化。比如我们可以判断只有当前手机为 AC 充电状态时 才去执行一些非常耗电的操作。可以通过下面的方法判断手机当前的充电状态
![](https://tva1.sinaimg.cn/large/007S8ZIlgy1ghqeev4e2mj30xc07fdhl.jpg)

* **屏幕唤醒**  
当 Android 设备空闲时，屏幕会变暗，然后关闭屏幕，最后会停止 CPU 的运行，这样可以防 止电池电量掉的快。但有些时候我们需要改变 Android 系统默认的这种状态:比如玩游戏时我 们需要保持屏幕常亮，比如一些下载操作不需要屏幕常亮但需要 CPU 一直运行直到任务完成。  
保持屏幕常亮比较好的方式是在 Activity 中使用 FLAG_KEEP_SCREEN_ON 的 Flag，这个方法的好处是不像唤醒锁(wake locks)，需要一些特定的权限(permission)。并且能 正确管理不同 app 之间的切换，不用担心无用资源的释放问题。
![](https://tva1.sinaimg.cn/large/007S8ZIlgy1ghqet88wwij30xc03y3zb.jpg)
另一个方式是在布局文件中使用 android:keepScreenOn 属性:
![](https://tva1.sinaimg.cn/large/007S8ZIlgy1ghqeu7nji8j30xc0bejti.jpg)

* **WakeLock**  
wake_lock 锁主要是相对系统的休眠而言的，意思就是程序给 CPU 加了这个锁那系统就不会 休眠了，这样做的目的是为了全力配合我们程序的运行。  
	1. 添加唤醒锁权限
	![](https://tva1.sinaimg.cn/large/007S8ZIlgy1ghqglof01xj30xc01gjrl.jpg)
	2. 直接使用唤醒锁
	![](https://tva1.sinaimg.cn/large/007S8ZIlgy1ghqgm36xhgj30xc03fq3n.jpg)
	
	> **注意**：在使用该类的时候，必须保证 acquire 和 release 是成对出现的。不然当我们业务已经不需要时， 当 CPU 处于唤醒状态，这个时候就会损耗多余的电量。

* **JobScheduler**  
自 Android 5.0 发布以来，JobScheduler 已成为执行后台工作的很好的方式，其工作方式有 利于用户在适当的时机执行正确的事情。应用可以在安排作业的同时允许系统基于内存、电源 和连接情况进行优化。JobSchedule 的宗旨就是把一些不是特别紧急的任务放到更合适的时机 批量处理。这样做有两个好处:
	* 避免频繁的唤醒硬件模块，造成不必要的电量消耗
	* 避免在不合适的时间(例如低电量情况下、弱网络或者移动网络情况下的)执行过多的
任务消耗电量

* **GPS**  
**选择合适的 Location Provider**  
Android 系统支持多个 Location Provider:  
	* **GPS_PROVIDER**: GPS 定位，利用 GPS 芯片通过卫星获得自己的位置信息。定位精准度高，一般在 10 米左右， 耗电量大;但是在室内，GPS 定位基本没用。
	* **NETWORK_PROVIDER**: 网络定位，利用手机基站和 WIFI 节点的地址来大致定位位置，这种定位方式取决于服务器，
即取决于将基站或 WIF 节点信息翻译成位置信息的服务器的能力。
	* **PASSIVE_PROVIDER**: 被动定位，就是用现成的，当其他应用使用定位更新了定位信息，系统会保存下来，该应用接 收到消息后直接读取就可以了。

* **传感器**  
使用传感器，选择合适的采样率，越高的采样率类型则越费电。
	* **SENSOR_DELAY_NOMAL** (200000 微秒)
	* **SENSOR_DELAY_UI** (60000 微秒)
	* **SENSOR_DELAY_GAME** (20000 微秒)
	* **SENSOR_DELAY_FASTEST** (0 微秒)

### class文件结构
* Class文件是一组以8位字节为基础单位的的二进制流。
* 各数据项目之间没有任何分隔符
* Class文件格式采用的结构只有两种数据结构：无符号数和表。
	* **无符号数**：以u1,u2等表示1，2个字节的无符号数，无符号数可用于描述数字、索引引用、数量值、字符串值。
	* **表**：以多个无符号数或者表组成，通常以_info结尾，整个Class文件就是一张表。

![](https://tva1.sinaimg.cn/large/007S8ZIlgy1ghqjkz7tenj30xc0o243g.jpg)

* **魔数**：Class文件开始四个字节是魔数，用于验证该文件是否能被虚拟机执行
* **版本号**：魔数后四个字节则是Class文件的主版本号和次版本号
* **常量池**：版本号后面紧跟着的则是常量池部分。常量池不同与java中，它是由1开始计数的，常量痴表的数目是常量个数减一。第0项为保留项目，用于指示当前Class文件不需要引用任何一个常量池。常量池中主要包含两大类常量：
	* **字面量**：包括文本字符串，被声明为final的常量值等。
	* **符号引用**：包括类和接口的权限定名，字段的名称和描述符，方法的名称和描述符
* **访问标志**：常量池部分结束后则是访问标志部分，该部分包括信息：是类还是接口，是否定义为public，是否final，是否abstract等信息。
* **类索引，父类索引，接口索引**：该部分的主要信息为：该类的信息的索引，父类的信息的索引，实现接口的个数和对应接口信息的索引。（通过指向一个类型为CONSTANT_Class_info的类描述符常量，从而在对应的常量池找到相关信息）
* **字段表**：接下来的部分则是用于描述类声明的变量信息，包括变量的作用域，是否为static，是否为final等信息。
* **方法表**：方法表则是用于描述类中方法的信息，与字段表类似。
* **属性表**：属性表在字段表和方法表中都会出现，主要用于描述某些场景特有的信息。比如方法是否抛出异常，被final声明的变量的值，内部类列表等信息。

### Dex文件结构
Dex是Android平台上(Dalvik虚拟机，art虚拟机)的可执行文件，每个APK压缩包中都包含一个（或者多个MultiDex）Dex文件，Dex文件中包含了app的所有源码。

* Dex文件是一组以8位字节为基础单位的的二进制流。
* Dex文件的各数据项目之间也没有任何分隔符
* Dex文件由文件头，索引区，数据区三个部分组成

![](https://tva1.sinaimg.cn/large/007S8ZIlgy1ghqm4iengjj30dm0czwf2.jpg)

* **header：dex**文件头部，记录整个dex文件的相关属性
* **string_ids**：字符串数据索引，记录了每个字符串在数据区的偏移量
* **type_ids**：类型数据索引，记录了每个类型的字符串索引
* **proto_ids**：原型数据索引，记录了方法声明的字符串，返回类型字符串，参数列表
* **field_ids**：字段数据索引，记录了所属类，类型以及方法名
* **method_ids**：类方法索引，记录方法所属类名，方法声明以及方法名等信息
* **class_defs**：类定义数据索引，记录指定类各类信息，包括接口，超类，类数据偏移量
* **data**：数据区，保存了各个类的真实数据
* **link_data**：连接数据区


### AOP
**AspectJ**

AspectJ是一个面向切面的框架，它扩展了Java语言。AspectJ定义了AOP语法，它有一个专门的编译器用来生成遵守Java字节编码规范的Class文件。

[gradle_plugin_android_aspectjx](https://github.com/HujiangTechnology/gradle_plugin_android_aspectjx)

* **@Before**：前置通知, 在方法执行之前执行
* **@After**：后置通知, 在方法执行之后执行
* **@AfterRunning**：返回通知, 在方法返回结果之后执行
* **@AfterThrowing**：异常通知, 在方法抛出异常之后
* **@Around**：环绕通知, 围绕着方法执行


**ASM**

ASM是一个java字节码操纵框架，它能被用来动态生成类或者增强既有类的功能。ASM 可以直接产生二进制 class 文件，也可以在类被加载入 Java 虚拟机之前动态改变类行为。Java class 被存储在严格格式定义的 .class文件里，这些类文件拥有足够的元数据来解析类中的所有元素：类名称、方法、属性以及 Java 字节码（指令）。ASM从类文件中读入信息后，能够改变类行为，分析类信息，甚至能够根据用户要求生成新类。asm字节码增强技术主要是用来反射的时候提升性能的，如果单纯用jdk的反射调用，性能是非常低下的，而使用字节码增强技术后反射调用的时间已经基本可以与直接调用相当了.

[asm jar 下载](http://files.cnblogs.com/liuling/asm-3.2.rar)

ASM框架中的核心类有以下几个：

* **ClassReader**: 该类用来解析编译过的class字节码文件。
* **ClassWriter**: 该类用来重新构建编译后的类，比如说修改类名、属性以及方法，甚至可以生成新的类的字节码文件。
* **ClassAdapter**: 该类也实现了ClassVisitor接口，它将对它的方法调用委托给另一个ClassVisitor对象。

### 面向对象设计(OOD)原则
![](https://tva1.sinaimg.cn/large/007S8ZIlgy1ghy7r06dc2j30jc0c4mz7.jpg)

**比设计模式更重要的是设计原则**

面相对象设计目标是希望软件系统能做到以下几点：

* **可扩展**：新特性能够很容易的添加到现有系统中，不会影响原本的东西
* **可修改**：当修改某一部分的代码时，不会影响到其它不相关的部分
* **可替代**：将系统中某部分的代码用其它有相同接口的类替换时，不会影响到现有系统

**五个基本原则(SOLID)**

* S 单一职责原则
	> 一个类有且仅有一个职责，只有一个引起它变化的原因
	
* O 开放关闭原则
	> 一个软件实体如类、模块和函数应该对扩展开放，而对修改关闭
	
* L 里氏替换原则
	> 所有引用基类的地方必须能透明地使用其子类的对象
	
* I 接口隔离原则
	> 不能强迫用户去依赖那些他们不使用的接口
	
* D 依赖倒置原则
	> 高层模块不应该依赖低层模块，两者都应该依赖其抽象；抽象不应该依赖细节；细节应该依赖抽象
	
### Android源码中的设计模式
**建造者模式**

Dialog、Notification

**单例模式**

以ActivityManager等系统服务来说，是通过静态代码块的形式实现单例，在首次加载类文件时，生成单例对象，然后保存在Cache中，之后的使用都是直接从Cache中获取。

**原型模式**

实现Cloneable接口获取一个对象，属性相同但没有关联

**工厂模式**

BitmapFactory.decodeXXX

**策略模式**

以网络模块为例，传入对应泛型，自动将结果解析成相应格式

**责任链模式**

Android的触摸机制

**观察者模式**

ListView的BaseAdapter、EventBus、RxJava

**模板方法模式**

流程确定，具体实现细节由子类完成，如AsyncTask

**代理模式与装饰器模式**

代理模式会持有被代理对象的实例，而这个实例一般是作为成员变量直接存在于代理类中的，即不需要额外的赋值。  
装饰器模式的目的不在于控制访问，而是扩展功能，相比于继承基类来扩展功能，使用装饰器模式更加的灵活。

**外观模式**

对用户隐藏细节，如工具类


### Android 11存储适配

``` kotlin
fun saveFile() {
    if (checkPermission()) {
        //getExternalStoragePublicDirectory被弃用，分区存储开启后就不允许访问了
        val filePath = Environment.getExternalStoragePublicDirectory("").toString() + "/test3.txt"
        val fw = FileWriter(filePath)
        fw.write("hello world")
        fw.close()
        showToast("文件写入成功")
    }
}
```
分情况运行：

1. targetSdkVersion = 28，运行后正常读写。
2. targetSdkVersion = 29，不删除应用，targetSdkVersion 由28修改到29，覆盖安装，运行后正常读写。
3. targetSdkVersion = 29，删除应用，重新运行，读写报错，程序崩溃（open failed: EACCES (Permission denied)）
4. targetSdkVersion = 29，添加android:requestLegacyExternalStorage="true"（不启用分区存储），读写正常不报错
5. targetSdkVersion = 30，不删除应用，targetSdkVersion 由29修改到30，读写报错，程序崩溃（open failed: EACCES (Permission denied)）
6. targetSdkVersion = 30，不删除应用，targetSdkVersion 由29修改到30，增加android:preserveLegacyExternalStorage="true"，读写正常不报错
7. targetSdkVersion = 30，删除应用，重新运行，读写报错，程序崩溃（open failed: EACCES (Permission denied)）

解决方法

1. 应用专属目录
	
	``` kotlin
	//分区存储空间
	val file = File(context.filesDir, filename)
	//应用专属外部存储空间
	val appSpecificExternalDir = File(context.getExternalFilesDir(), filename)
	```
2. 访问公共媒体目录文件

	``` kotlin
	val cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, "${MediaStore.MediaColumns.DATE_ADDED} desc")
	if (cursor != null) {
	    while (cursor.moveToNext()) {
	        val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
	        val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
	        println("image uri is $uri")
	    }
	    cursor.close()
	}
	```

3. SAF(存储访问框架--Storage Access Framework)

	``` kotlin
	val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
	intent.addCategory(Intent.CATEGORY_OPENABLE)
	intent.type = "image/*"
	startActivityForResult(intent, 100)
	
	@RequiresApi(Build.VERSION_CODES.KITKAT)
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
	    super.onActivityResult(requestCode, resultCode, data)
	    if (data == null || resultCode != Activity.RESULT_OK) return
	    if (requestCode == 100) {
	        val uri = data.data
	        println("image uri is $uri")
	    }
	}
	```

### kotlin let,with,run,apply,also函数区别

| 函数名 | 函数体内使用对象 | 返回值 | 是否是扩展函数 | 适用场景 |
| :----: | ---- | ---- | :----: | ---- |
| let | it指代当前对象 | 闭包形式返回 | 是 | 适用于处理不为null场景 |
| with | this指代当前对象 | 闭包形式返回 | 否 | 适用于调用同一个类的多个方法时，可以省去类名重复，直接调用类的方法即可，经常用于Android中RecyclerView中onBinderViewHolder中，数据model的属性映射到UI上 |
| run | this指代当前对象 | 闭包形式返回 | 是 | 适用于let、with函数任何场景 |
| apply | this指代当前对象 | 返回this | 是 | 1、适用于run函数的任何场景，一般用于初始化一个对象实例的时候，操作对象属性，并最终返回这个对象。 2、动态inflate出一个XML的View的时候需要给View绑定数据也会用到. 3、一般可用于多个扩展函数链式调用 4、数据model多层级包裹判空处理的问题 |
| also |

### 主线程的消息循环模型
ActivityThread通过ApplicationThread和AMS进行进程间通信，AMS以进程间通信方式完成ActivityThread的请求后会回调ApplicationThread中的Binder方法，然后ApplicationThread会向H发送消息，H收到消息后会将ApplicationThread中的逻辑切换到ActivityThread中去执行，即切换到主线程中去执行。

ActivityThread实际上并非线程，不像HandlerThread类，ActivityThread并没有真正继承Thread类，既然ActivityThread不是一个线程，那么ActivityThread中Looper绑定的是哪个Thread

**进程** 

每个app运行时前首先创建一个进程，该进程是由Zygote fork出来的，用于承载App上运行的各种Activity/Service等组件。进程对于上层应用来说是完全透明的，这也是google有意为之，让App程序都是运行在Android Runtime。大多数情况一个App就运行在一个进程中，除非在AndroidManifest.xml中配置Android:process属性，或通过native代码fork进程。

**线程**

线程对应用来说非常常见，比如每次new Thread().start都会创建一个新的线程。该线程与App所在进程之间资源共享，从Linux角度来说进程与线程除了是否共享资源外，并没有本质的区别，都是一个task_struct结构体，在CPU看来进程或线程无非就是一段可执行的代码，CPU采用CFS调度算法，保证每个task都尽可能公平的享有CPU时间片。

**其实承载ActivityThread的主线程就是由Zygote fork而创建的进程。**


### OkHttp缓存
一般控制缓存有两种方式：  
> 1、在request里面去设置cacheControl()策略  
> 2、在header里面去添加cache-control

``` java
OkHttpClient client = OKHttpManager.getClient().newBuilder()
        .cache(new Cache(new File(getExternalCacheDir(), "okhttpcache"), 1024 * 1024 * 10))
        .build();
Request request = new Request.Builder()
        .url("http://open.iciba.com/dsapi/")
        .cacheControl(NetworkUtil.isNetworkAvailable(this) ? CacheControl.FORCE_NETWORK : CacheControl.FORCE_CACHE)
        .build();
client.newCall(request).enqueue(...);
```

1. max-age是啥，maxStale是啥，他们的区别是啥？

	**max-age**
	
	未超出max-age返回缓存数据，超出max-age发起新的请求，请求失败返回缓存
	
	**max-stale**
	
	未超出max-stale返回缓存数据，超出max-stale发起新的请求，请求失败返回失败
	> 失败信息：**504 Unsatisfiable Request**

2. 为什么没有网络的情况下，request要cacheControl.FORCE_CACHE
	
	``` kotlin
	@JvmField
val FORCE_CACHE = Builder()
    .onlyIfCached()
    .maxStale(Integer.MAX_VALUE, TimeUnit.SECONDS)
    .build()
	```
	FORCE_CACHE是设置了maxStale的最大时间为interger的最大时间，一直（强制）拿缓存
	
	
### OkHttp 拦截器
**addInterceptor(应用拦截器)**
> 1. 不需要担心中间过程的响应,如重定向和重试.  
> 2. 总是只调用一次,即使HTTP响应是从缓存中获取.  
> 3. 观察应用程序的初衷. 不关心OkHttp注入的头信息如: If-None-Match.  
> 4. 允许短路而不调用 Chain.proceed(),即中止调用.  
> 5. 允许重试,使 Chain.proceed()调用多次.  

**addNetworkInterceptor(网络拦截器)**
> 1. 能够操作中间过程的响应,如重定向和重试.  
> 2. 当网络短路而返回缓存响应时不被调用.  
> 3. 只观察在网络上传输的数据.  
> 4. 携带请求来访问连接.  

### HTTP的缓存规则总结
HTTP的缓存规则是优先考虑强制缓存，然后考虑对比缓存。

1. 首先判断强制缓存中的数据的是否在有效期内。如果在有效期，则直接使用缓存。如果过了有效期，则进入对比缓存。
2. 在对比缓存过程中，判断ETag是否有变动，如果服务端返回没有变动，说明资源未改变，使用缓存。如果有变动，判断Last-Modified。
3. 判断Last-Modified，如果服务端对比资源的上次修改时间没有变化，则使用缓存，否则重新请求服务端的数据，并作缓存工作。

![](https://tva1.sinaimg.cn/large/007S8ZIlgy1gifll2uipzj30fg0erq3y.jpg)

### OkHttp读取缓存流程

请求体缓存

``` 
http://192.168.10.100:8080/wangzhen/plugin/plugin.json
GET
0
HTTP/1.1 200 
9
Accept-Ranges: bytes
ETag: W/"176-1587794129000"
Last-Modified: Sat, 25 Apr 2020 05:55:29 GMT
Content-Type: application/json
Content-Length: 176
Date: Sat, 05 Sep 2020 03:32:09 GMT
Cache-Control: max-age=60
OkHttp-Sent-Millis: 1599276730232
OkHttp-Received-Millis: 1599276730245
```

响应结果缓存

```
{
    "version_name": "0.0.1",
    "version_code": 35,
    "version_description": "功能更新",
    "url": "http://192.168.188.132:8080/wangzhen/plugin/apk/app-release.apk"
}
```

1. 首先获取OkhttpClient的Cache对象
2. 调用Cache的get方法传入Request查找缓存响应数据Response
3. 构造一个缓存策略，传入Request和Response，决策使用网络请求还是缓存响应
4. 策略判定之后，如果使用缓存，则它的cacheResponse不为空，networkRequest为空，如果使用请求，则相反
5. 如果使用请求，但是之前又找到了缓存响应，则要关闭缓存响应资源
6. 如果策略得出缓存响应为空，网络请求也为空，则返回请求不合理的响应（比如强制使用缓存，但找不到缓存的情况）

总结：先查找Cache是否可用，通过Cache查找请求对应的缓存，然后将请求和缓存交给缓存策略判断使用请求还是缓存，如果使用缓存，用缓存构造响应直接返回，如果使用请求，则开始网络请求流程

### 缓存一致性协议（MESI协议）
如果一个变量在多个CPU中都存在缓存（一般在多线程编程时才会出现），那么就可能存在缓存不一致的问题。

通常有2种解决方法(均为硬件层面)

1. **通过在总线加LOCK#锁的方式**
	
	在早期CPU中，是通过在总线上加LOCK#锁的形式来解决缓存不一致问题。只有一个CPU能使用变量的内存，命令执行过程中在总线上发出了LOCK#锁信号，只有命令执行完毕，其他CPU才能从主存读取谜题，效率低下。

2. **通过缓存一致性协议**
	
	最出名的就是Intel的MESI协议，MESI协议保证了每个缓存中使用的共享变量副本是一致的，核心思想：**当CPU写数据时，如果发现操作的变量是共享变量，即在其他CPU中也存在该变量的副本，会发出信号通知其他CPU将该变量的缓存状态置为无效状态，因此当其他CPU需要读取这个变量时，发现自己缓存中的变量是无效状态，会从主存重新读取**。
	![](https://tva1.sinaimg.cn/large/007S8ZIlgy1gihxcabsjrj30l909rjsc.jpg)

### TCP拥塞控制

在某段时间，若 **对网络中某一资源的需求超过了该资源所能提供的可用部分，网络性能就要变坏**，这种情况就叫做 **网络拥塞**。

若 **出现拥塞而不进行控制**，整个网络的 **吞吐量将随输入负荷的增大而下降**。

![](https://tva1.sinaimg.cn/large/0081Kckwgy1gk7mhxsnaqj30hm08rgm9.jpg)

当输入的负载到达一定程度 吞吐量不会增加，即一部分网络资源会丢失掉，网络的吞吐量维持在其所能控制的最大值，转发节点的缓存不够大这造成分组的丢失是拥塞的征兆。

**TCP的四种拥塞控制算法**

1. 慢开始
2. 拥塞控制
3. 快重传
4. 快恢复

**假定**

1. 数据是单方向传送，而另一个方向只传送确认
2. 接收方总是拥有足够大的缓存空间，因而发送方发送窗口的大小由网络拥塞程序来决定
3. 以TCP报文段的个数为讨论问题的单位，而不是以字节为单位

![](https://tva1.sinaimg.cn/large/0081Kckwgy1gk7mgfe9gsj30pf0e90w1.jpg)

![](https://tva1.sinaimg.cn/large/0081Kckwgy1gk7mo4k2ruj30q50ezdhy.jpg)


### 判断两棵树是否相同

``` kotlin
def isSameTree(p, q):
	if p == None and q == None:
		return True
	elif p != None and q != None:
		return p.val == q.val and isSameTree(p.left, q.left) and isSameTree(p.right, q.right)
	else:
		return False
```

### 进程的状态转换图

进程在其生命周期内通常进程有以下三种状态

1. 就绪(Ready)状态：指进程已处回于准备好运行的状态，答及进程已经分配到需要的系统资源，只要在获得CPU就可以执行
2. 执行(Running)状态：指进程获得了CPU正在执行，在单处理机系统中，最多只有一个进程处于该状态
3. 阻塞(Block)状态：指正在执行的进程，在执行过程中发生了某时间（如：I/O请求、申请缓冲区失败等）

![](https://tva1.sinaimg.cn/large/0081Kckwgy1gk7mu9jbe4j30go0843yt.jpg)


### 内核态和用户态的区别，为什么要这样划分

由于需要限制不同的程序之间的访问能力, 防止他们获取别的程序的内存数据, 或者获取外围设备的数据, 并发送到网络, CPU划分出两个权限等级 – 用户态和内核态。

在CPU的所有指令中，有一些指令是非常危险的，如果错用，将导致整个系统崩溃。比如：清内存、设置时钟等。所以，CPU将指令分为特权指令和非特权指令，对于那些危险的指令，只允许操作系统及其相关模块使用，普通的应用程序只能使用那些不会造成灾难的指令。

**用户态切换到内核态的3种方式**

1. 系统调用

	> 这是用户态进程主动要求切换到内核态的一种方式，用户态进程通过系统调用申请使用操作系统提供的服务程序完成工作，比如fork()实际上就是执行了一个创建新进程的系统调用。而系统调用的机制其核心还是使用了操作系统为用户特别开放的一个中断来实现，例如Linux的int 80h中断。

2. 中断

	> 当外围设备完成用户请求的操作后，会向CPU发出相应的中断信号，这时CPU会暂停执行下一条即将要执行的指令转而去执行与中断信号对应的处理程序，如果先前执行的指令是用户态下的程序，那么这个转换的过程自然也就发生了由用户态到内核态的切换。比如硬盘读写操作完成，系统会切换到硬盘读写的中断处理程序中执行后续操作等。

3. 异常

	> 当CPU在执行运行在用户态下的程序时，发生了某些事先不可知的异常，这时会触发由当前运行进程切换到处理此异常的内核相关程序中，也就转到了内核态，比如缺页异常。

### HashMap成环链表

![](https://tva1.sinaimg.cn/large/0081Kckwgy1gk8f5ab0p6j31ke0rsjve.jpg)

问题原因

1. 插入的时候和平时我们追加到尾部的思路是不一致的，是链表的头结点开始循环插入，导致插入的顺序和原来链表的顺序相反的。
1. table 是共享的，table 里面的元素也是共享的，while 循环都直接修改 table 里面的元素的 next 指向，导致指向混乱。

### 操作系统中的缺页中断

malloc()和mmap()等内存分配函数，在分配时只是建立了进程虚拟地址空间，并没有分配虚拟内存对应的物理内存。当进程访问这些没有建立映射关系的虚拟内存时，处理器自动触发一个缺页异常。

缺页本身是一种中断，与一般的中断一样，需要经过4个处理步骤：

1. 保护CPU现场
2. 分析中断原因
3. 转入缺页中断处理程序进行处理
4. 恢复CPU现场，继续执行

但是缺页中断是由于所要访问的页面不存在于内存时，由硬件所产生的一种特殊的中断，因此，与一般的中断存在区别：

1. 在指令执行期间产生和处理缺页中断信号
2. 一条指令在执行期间，可能产生多次缺页中断
3. 缺页中断返回是，执行产生中断的一条指令，而一般的中断返回是，执行下一条指令。


操作系统中的中断

中断是指CPU对系统发生的某个事件做出的一种反应，CPU暂停正在执行的程序，保存现场后自动去执行相应的处理程序，处理完该事件后再返回中断处继续执行原来的程序。中断一般三类，一种是由CPU外部引起的，如I/O中断、时钟中断，一种是来自CPU内部事件或程序执行中引起的中断，例如程序非法操作，地址越界、浮点溢出），最后一种是在程序中使用了系统调用引起的。而中断处理一般分为中断响应和中断处理两个步骤，中断响应由硬件实施，中断处理主要由软件实施。













