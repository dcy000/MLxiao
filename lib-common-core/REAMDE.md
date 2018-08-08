# Android 工程化

## TODO

- [x] 数据层搭建（见 lib-common-core 模块 repository 。用到的开源项目：Dagger2， RxJava， Retrofit， RxCache， Room， Gson ，timber，Stetho）
- [x] 图片加载二次封装
- [x] 生命周期分发( 见 AppDelegate ）
- [x] 生命周期与内存泄漏(AutoDispose, RxLife）
- [x] 监控异常 （Bugly）
- [ ] 全局异常处理 
- [x] 监控内存泄露 ( LeakCanary )
- [x] 监控主线程耗时操作 ( BlockCanary )
- [x] 通信（ CC (组件总线)，ARoutor (路由)）
- [x] 通信（RxBus， EventBus）
- [ ] 表现层 （ MVVM ）， 业务层 
- [ ] 业务拆分与组件封装
- [ ] 依赖管理 CI CD

## Why RxJava

- 当出现异常未捕获时，一般程序会崩溃。 Rxjava 的处理是将异常通过 onError 向上传递（可以利用这个来做全局异常处理）
- 当我们用 MVX 去架构应用时， 会遇上内存泄露的问题。 RxJava 可以优雅处理。
- 响应式编程带给我们的是， 面对复杂的业务逻辑时， 代码依然可以干净，简洁。

## 致谢

> 感谢开源社区及开源作者

- [CC](https://github.com/luckybilly/CC)
- [Dagger2](https://github.com/google/dagger)
- [RxJava](https://github.com/ReactiveX/RxJava)
- [RxAndroid](https://github.com/ReactiveX/RxAndroid)
- [Retrofit](https://github.com/square/retrofit)
- [OkHttp](https://github.com/square/okhttp)
- [Gson](https://github.com/google/gson)
- [RxCache](https://github.com/VictorAlbertos/RxCache)
- [Room](https://github.com/googlesamples/android-architecture-components)
- [timber](https://github.com/JakeWharton/timber)
- [Stetho](https://github.com/facebook/stetho)
- [ArmsComponent](https://github.com/JessYanCoding/ArmsComponent)
- [MVVMArms](https://github.com/xiaobailong24/MVVMArms)

