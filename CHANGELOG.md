# Change Log
## 2019.04.03
- 修复心电年龄不能修改等若干bug

## 2019。01.04
- 博声心电兼容直连模式

## 2019.01.02
- 修复三合一内置蓝牙仪器不能连接的bug

## 2018.12.21
- 将签约医生的预约模块去除
- 修复血氧仪在2G平板上不能正常上传数据的bug

## 工程化
 
### 组件化

> - app-component 可直接运行
> - [cc-settings.gradle](cc-settings.gradle): [CC](https://github.com/luckybilly/CC) 组件化配置文件

 - lib-common-core （ Repository 数据层架构, App 组件生命周期，等等 )
 - lib-common-business （ 公共业务模块， 供需要的业务组件使用，等等 )
 - lib-common-app（ App 或 组件单独调试运行时， 需要使用的模块，等等 )
 - app-component ( [CC](https://github.com/luckybilly/CC) 组件化主 App 组件 demo )
 - component-cc( [CC](https://github.com/luckybilly/CC) 组件化组件 demo，启动 Activity )
 
### 业务组件拆分

- [ ] 后面慢慢抽出的组件， 可添加文档，以记录