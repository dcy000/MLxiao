### 主要流程
1. 注册 
3. 注册讯飞
4. 补充信息
5. 登录 
6. 获取用户信息  

#### 登录
1. 讯飞登录 login/xunfei
2. 身份证登录 login/user_sfz_login
3. 账号密码登录 login/applogin
4. 通过 refreshToken 获取新 token login/refresh/

#### 注册
1. 普通注册 br/appadd
2. 查询手机号 login/tel_isClod
3. 查询身份证 /api/user/info/idCard/{idCard}/
    
#### 查询用户信息
1. 获取用户基本信息  br/selOneUserEverything

#### 讯飞
1. 查询用户在机器上的讯飞信息 /api/user/xunfei/{userId}/
2. 查询机器讯飞组信息 /api/user/xunfei/equipment/{userId}/
3. 修改用户讯飞信息 /api/user/xunfei/{userId}/ PUT
4. 清除用户讯飞信息 /api/user/xunfei/{userId}/ DELETE

#### 修改用户
1. 患者信息修改 /api/user/info/{userId}/

#### 查询多个用户（切换账号使用本地缓存，建议废除此接口）
2. 患者信息查询 /api/user/info/users/

