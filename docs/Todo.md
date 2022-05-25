## Todo

- [x] 部分类代码重构：

  - [x] java package naming routine

    > https://stackoverflow.com/questions/29053659/how-to-name-a-java-package-as-a-student

  - [x] 模板模式相关代码重构

    - [x] `AbstractGame.java`
      - [x] `timeCountAndNewCycleJudge()`
      - [x] `paint()`
    - [x] `DifficultGame.java`
    - [x] `MediumGame.java`
    - [x] `SimpleGame.java`

  - [x] `refactor: crashCheckAction()`

  - [x] 策略模式相关代码重构

    - [x] `AbstractAircraft.java`
    - [x] `HeroAircraft.java`
    - [x] `AbstractGame.java`
    - [x] `Context.java` 
      
      > may be removed later
      
    - [x] `BulletProp.java`
    - [x] `strategy.*`

- [x] 原始功能移植

  - [x] 开始界面
  - [x] 游戏主体
    - [x] 背景滚动
    - [x] 游戏逻辑运行（action）
    - [x] 血条添加（draw）
    - [ ] 音乐播放
  - [ ] 排行榜界面
  - [ ] `refactor: System.out.print to Log.m/i/d/r/w/e`

- [ ] 联网功能

## Bug

- [x] 绘制敌机和子弹的部分出错


## Game Settings

- [x] 子弹速度太慢了 
- [x] 道具下落速度太慢了
- [x] 血条的字再小一点点就好
- [x] 血量格式化显得很奇怪
- [x] 得分应当和敌机初始血量挂钩
- [ ] 出现了一次分数高于三万的时候吃道具会没有作用，难度为`difficult`
- [ ] 偶尔会出现血条超出显示范围的情况
- [ ] 背景移动太慢了
