# MLxiao项目开发规范

## 最小边界原则

1.**在模块内初始化**。有些模块需要在Application中初始化，必须通过AutoService在本模块内进行；

2.**本模块的业务只在本模块内进行**。除了极个别的异步业务需要去B模块拿参数，实际业务在A模块中发生；绝大部分业务都必须在在宿主模块内进行，通过**ARetrofit**的服务功能可达到此目的。例如B模块内的函数，只在B模块中调用，而不能传递到A模块中执行。



## 路由解耦原则

**ARetrofit**使用极其简单，完全有能力替换原生写法，并且比原生写法更加解耦。

建议在后续开发中逐渐替换项目中的原生写法为路由跳转。即使是同模块中的页面跳转也推荐路由跳转，因为在后续开发中，可能存在模块重新划分和整合的情况，这时的原生写法，将会耦合严重，不利于快速拆分和整合。

另外，虽然Aretrofit支持像Retrofit那样任意的接口表，但是还是建议统一写在AppRouter中，因为统一书写，不仅能减少工作量，而且不会因为不同的路径字符串命名而导致混乱或者出错；更为重要的原因是方便后期根据代码进行业务的追踪和维护。

最后一个使用该路由的原因是，在进行Intent的传参的时候，不再跟踪发起跳转的具体代码，而只需跟踪路由注册表中的字段即可，不易出错，也更容易查看同伴书写的代码传递的具体参数是什么。





# 模块说明

## module-control

1.机器人对话

2.设置和产品简介等简单但是重要的页面

3.app更新功能

4.音量控制功能

5.WiFi控制功能

6.全局唤醒功能（该功能放在这里完全是因为使用到的很多资源文件都和机器人对话功能的一致）



## module-hypertension-manager

1.高血压管理

2.中医体质



## module-mall

1.超哥做的新版商城

2.old包下的老版商城

3.支付（微信和支付宝）

4.服务包（套餐）



## 其他模块

**其他模块功能比较专一，比较容易理解，请自行查看代码。**



# 注意事项和建议

1.使用的全局变量尽量用UserSpHelper或者用提供的全局方式动态获取：

```java
 Routerfit.register(AppRouter.class)
          .getUserProvider()
          .getUserEntity()
          .subscribeOn(Schedulers.io())
     	  .observeOn(AndroidSchedulers.mainThread())
          .as(RxUtils.autoDisposeConverter(this))
          .subscribe(new DefaultObserver<UserEntity>() {
            	@Override
                public void onNext(UserEntity userEntity) {
                                
                }
           });
```

2.有两个和两个以上模块功能用的资源文件或者实体bean建议下沉到lib-common-business中去，减少代码冗余的情况。

3.在壳模块中的引用统一使用addComponent，减少壳模块对业务模块资源引用导致的耦合

4.模块单独开发和调试方式任然和CC框架1.x使用方式一致，具体参考CC文档