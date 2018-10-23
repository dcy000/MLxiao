# MLxiao

##  [CHANGELOG](CHANGELOG.md)

## 团队协作

### 基础版 Git 分支策略

- Robot1.3.0 版本之后 master 仅作为基础版的生产分支， 此分支改动少，较稳定。
- 从 Master 分支 checkout 新分支 dev 作为预发版分支， 作为开发与测试的对接。
- 其他团队成员每人 checkout 一个新分支，作为开发分支。

> - master
>   - dev
>     - somebody1 ( develop branch by somebody1 )
>     - somebody2 ( develop branch by somebody2 )
>     - somebody3 ( develop branch by somebody3 )
