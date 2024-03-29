package com.efjerryyang.defendthefrontline.game;

import static com.efjerryyang.defendthefrontline.aircraft.HeroAircraft.BOSS_APPEAR_SCORE;
import static com.efjerryyang.defendthefrontline.application.OnlineActivity.gameClient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.efjerryyang.defendthefrontline.R;
import com.efjerryyang.defendthefrontline.aircraft.AbstractEnemy;
import com.efjerryyang.defendthefrontline.aircraft.BossEnemy;
import com.efjerryyang.defendthefrontline.aircraft.EliteEnemy;
import com.efjerryyang.defendthefrontline.aircraft.HeroAircraft;
import com.efjerryyang.defendthefrontline.aircraft.MobEnemy;
import com.efjerryyang.defendthefrontline.application.Config;
import com.efjerryyang.defendthefrontline.application.FinishActivity;
import com.efjerryyang.defendthefrontline.application.GameActivity;
import com.efjerryyang.defendthefrontline.application.ImageManager;
import com.efjerryyang.defendthefrontline.application.MainActivity;
import com.efjerryyang.defendthefrontline.application.OnlineActivity;
import com.efjerryyang.defendthefrontline.application.OnlineGameActivity;
import com.efjerryyang.defendthefrontline.application.ShootContext;
import com.efjerryyang.defendthefrontline.application.StartActivity;
import com.efjerryyang.defendthefrontline.basic.AbstractFlyingObject;
import com.efjerryyang.defendthefrontline.bullet.BaseBullet;
import com.efjerryyang.defendthefrontline.factory.BloodPropFactory;
import com.efjerryyang.defendthefrontline.factory.BombPropFactory;
import com.efjerryyang.defendthefrontline.factory.BossFactory;
import com.efjerryyang.defendthefrontline.factory.BulletPropFactory;
import com.efjerryyang.defendthefrontline.factory.EliteFactory;
import com.efjerryyang.defendthefrontline.factory.MobFactory;
import com.efjerryyang.defendthefrontline.prop.AbstractProp;
import com.efjerryyang.defendthefrontline.prop.BloodProp;
import com.efjerryyang.defendthefrontline.prop.BombProp;
import com.efjerryyang.defendthefrontline.prop.BulletProp;
import com.efjerryyang.defendthefrontline.record.Record;
import com.efjerryyang.defendthefrontline.strategy.StraightShoot;
import com.google.gson.Gson;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractGame extends SurfaceView implements
        SurfaceHolder.Callback, Runnable {

    private static final String TAG = "GameActivity";
    protected final HeroAircraft heroAircraft;
    protected final List<AbstractEnemy> enemyAircrafts;
    protected final List<BaseBullet> heroBullets;
    protected final List<BaseBullet> enemyBullets;
    protected final List<AbstractProp> props;
    protected final BloodPropFactory bloodPropFactory;
    protected final BulletPropFactory bulletPropFactory;
    protected final BombPropFactory bombPropFactory;
    protected final MobFactory mobFactory;
    protected final EliteFactory eliteFactory;
    protected final BossFactory bossFactory;
    //    protected final RecordDAOImpl recordDAOImpl;
    public double bombPropGeneration;
    public double bloodPropGeneration;
    public double bulletPropGeneration;
    public Bitmap backgroundImage;
    protected MediaPlayer bulletHitThread = null;
    protected MediaPlayer bulletPropThread = null;
    protected MediaPlayer bombExplodeThread = null;
    protected MediaPlayer bloodPropThread = null;
    protected MediaPlayer crashWithShieldThread = null;
    protected MediaPlayer bgmThread = null;
    protected MediaPlayer bgmBossThread = null;
    protected MediaPlayer gameOverThread = null;
    protected ShootContext heroShootContext;
    protected ShootContext enemyShootContext;
    protected boolean enableAudio;
    protected int baseLevel;
    protected double level;
    protected double levelScalar;
    protected int backGroundTop;
    protected int bulletValidTimeCnt;
    protected int bloodValidTimeCnt;
    protected int enemyMaxNumber;
    protected int enemyMaxNumberUpperBound;
    protected int score;
    protected int fscore;
    protected int cycleDuration;
    protected int cycleTime;
    protected int mobCnt;
    protected int mobCntMax;
    protected int time;
    protected int timeInterval;


    protected boolean gameOverFlag;
    protected int[] enemyAircraftGenerationCycle;
    protected int[] enemyShootCycle;
    protected int[] heroShootCycle;
    /**
     * boss机生成控制
     * 当scoreCnt == 0，并且score > 500时，产生boss敌机，bossFlag = true
     * 当boos机存在时，bossFlag = true，初始化scoreCnt = 500
     * 当boss机失效后，bossFlag = false, scoreCnt -= increment
     * 其中，increment是每次score变化的增量，通用控制语句如下
     * scoreCnt -= bossFlag ? 0 : increment;
     */
    protected int scoreCnt;
    protected int bossCnt;
    protected boolean bossFlag;
    protected boolean bombFlag;
    protected boolean bloodFlag;
    protected boolean bulletFlag;
    protected boolean bulletCrash;
    protected boolean crashFlag;
    protected Canvas canvas;
    protected Paint imagePaint;
    protected Paint shapePaint;
    protected Paint textPaint;
    protected SurfaceHolder mSurfaceHolder;
    public int screenHeight;
    public int screenWidth;
    public static float prevFingerX = 0;
    public static float prevFingerY = 0;
    public static float curFingerX = 0;
    public static float curFingerY = 0;
    private Thread connectionThread = null;
    private Context context;
    private MediaPlayer bgm;
//    public Socket socket = null;
//    public OutputStream outputStream = null;
//    public InputStream inputStream = null;

    protected AbstractGame(Context context, int gameLevel, boolean enableAudio) {
        super(context);
        this.context = context;
        this.baseLevel = gameLevel;
        this.level = gameLevel;
        Config.setPropValidMaxTime(level);
        this.enableAudio = enableAudio;
        enemyMaxNumber = 1;
        this.enemyMaxNumber = gameLevel + 1;
        enemyMaxNumberUpperBound = 10;
        this.enemyMaxNumberUpperBound = gameLevel * 2;
        this.screenHeight = Config.screenHeight;
        this.screenWidth = Config.screenWidth;
        heroAircraft = HeroAircraft.getHeroAircraft();
        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<BaseBullet>();
        enemyBullets = new LinkedList<BaseBullet>();
        props = new LinkedList<>();
        bloodPropFactory = new BloodPropFactory();
        bulletPropFactory = new BulletPropFactory();
        bombPropFactory = new BombPropFactory();
        mobFactory = new MobFactory();
        eliteFactory = new EliteFactory();
        bossFactory = new BossFactory();
        heroShootContext = new ShootContext(new StraightShoot());
        enemyShootContext = new ShootContext(new StraightShoot());
//        recordDAOImpl = new RecordDAOImpl(gameLevel);
        imagePaint = new Paint();
        shapePaint = new Paint();
        textPaint = new Paint();
        mSurfaceHolder = this.getHolder();
        mSurfaceHolder.addCallback(this);
        this.setFocusable(true);
        levelScalar = 1.0;
        backGroundTop = 0;
        bloodValidTimeCnt = 0;
        bulletValidTimeCnt = 0;
        score = 0;
        cycleDuration = 500;
        cycleTime = 0;
        mobCnt = 0;
        mobCntMax = 5;
        time = 0;
        timeInterval = 40;
        enemyAircraftGenerationCycle = new int[]{0, 400};
        enemyShootCycle = new int[]{0, 300};
        heroShootCycle = new int[]{0, 300};
        gameOverFlag = false;
        scoreCnt = 0;
        bossCnt = 0;
        bossFlag = false;
        bombFlag = false;
        bloodFlag = false;
        bulletFlag = false;
        bulletCrash = false;
        crashFlag = false;
        bgm = MediaPlayer.create(context, R.raw.bgm);
        bgm.start();
    }

    public final void action() {
        Log.d(TAG, "shootNum:" + heroAircraft.getShootNum() + "\t TimeCnt:" + bulletValidTimeCnt + "\t Stage" + heroAircraft.getBulletPropStage());
        // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定
//        Runnable gameTask = () -> {
        // 周期性执行（控制频率）
        if (timeCountAndNewCycleJudge(enemyAircraftGenerationCycle)) {
            generateAllEnemy();
        }
        if (timeCountAndNewCycleJudge(enemyShootCycle)) {
            enemyShootAction();
        }
        if (timeCountAndNewCycleJudge(heroShootCycle)) {
            heroShootAction();
        }
        // playBGM
//            playBGM();
        // 子弹移动
        bulletsMoveAction();
        // 飞机移动
        aircraftsMoveAction(1);
        // 道具移动
        propMoveAction();
        // 撞击检测
        crashCheckAction();
        // 后处理
        postProcessAction();
        //每个时刻重绘界面
//            repaint();
        // 游戏结束检查
        gameOverCheck();
        time += timeInterval;
        // 难度控制
        level = Math.min(bossCnt + 0.9999 + baseLevel, baseLevel * ((double) time / 1e5 + levelScalar));
        Config.setPropValidMaxTime(level);
        bulletPropStageCount();
        bloodPropStageCount();
//        };
        Runnable connectTask = () -> {
            try {
                String sendMessage = "@{\"score\": " + score + "}";
                System.out.println("Send to server: " + sendMessage);
                gameClient.sendRequest(sendMessage);
                String[] receiveArray = gameClient.receive().split("@");
                String receive = receiveArray[receiveArray.length - 1];
                Log.d(TAG, "Received from server: " + receive);
                Map serverMessage = new Gson().fromJson(receive, Map.class);
                Log.d(TAG, "serverMessage: " + serverMessage);
//                int ipHash = serverMessage.get("ip").hashCode();
//                int fipHash = serverMessage.get("fip").hashCode();
//                Log.d(TAG,"fhash: " + Integer.toHexString(fipHash).toUpperCase());
                Set keySet = serverMessage.keySet();
                Double fscoreDouble = (Double) serverMessage.get("fscore");
                System.out.println("keySet: " + keySet);
                System.out.println("fscoreDouble: " + fscoreDouble);
                fscore = (int) Double.parseDouble(String.valueOf(fscoreDouble));
                System.out.println("fscore:" + fscore);

                // {"ip": "85.203.23.169", "fip": "183.8.83.237", "score": 6420, "fscore": 9870, "matched": true}
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        if (Config.online) {
            connectionThread = new Thread(connectTask);
            connectionThread.start();
        }
    }

    public void bloodPropStageCount() {
        if (bloodValidTimeCnt <= 0) {
            this.bloodValidTimeCnt = 0;
        } else {
            heroAircraft.setHp(heroAircraft.getMaxHp());
            this.bloodValidTimeCnt -= 1;
        }
    }

    public void bulletPropStageCount() {
        if (heroAircraft.getBulletPropStage() > 0) {
            bulletValidTimeCnt -= 1;
            bulletValidTimeCnt = Math.max(bulletValidTimeCnt, 0);
        }
        if (bulletValidTimeCnt <= 0) {
            bulletValidTimeCnt = 0;
            heroAircraft.setBulletPropStage(heroAircraft.getBulletPropStage() - 1);
            if (heroAircraft.getBulletPropStage() > 0) {
                bulletValidTimeCnt = (int) (2000 / (5 + level));
            }
        }
        switch (heroAircraft.getBulletPropStage()) {
            case 0: {
                heroAircraft.setShootNum(1);
                heroAircraft.setBulletSpeedUp(false);
                break;
            }
            case 1:
            case 2: {
                heroAircraft.setBulletSpeedUp(false);
                heroAircraft.setShootNum(heroAircraft.getBulletPropStage() * 2);
                break;
            }
            case 3: {
                heroAircraft.setShootNum(5);
                break;
            }
            default: {
                heroAircraft.setBulletPropStage(3);
                break;
            }
        }
    }

    public boolean timeCountAndNewCycleJudge() {
        cycleTime += timeInterval;
        if (cycleTime >= cycleDuration && cycleTime - timeInterval < cycleTime) {
            // 跨越到新的周期
            cycleTime %= cycleDuration;
            return true;
        } else {
            return false;
        }
    }

    public boolean timeCountAndNewCycleJudge(int[] cycle) {
        cycle[0] += timeInterval;
        if (cycle[0] >= cycle[1] && cycle[1] - timeInterval < cycle[0]) {
            // 跨越到新的周期
            cycle[0] %= cycle[1];
            return true;
        } else {
            return false;
        }
    }

    public void bulletsMoveAction() {
        // 子弹移动
        for (BaseBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    public void aircraftsMoveAction(double factor) {
        heroAircraft.forward();
        for (AbstractEnemy enemyAircraft : enemyAircrafts) {
            boolean outOfBound = enemyAircraft.notValid();
            enemyAircraft.forward();
            if (enemyAircraft.notValid() != outOfBound) {
                double subNum = enemyAircraft.getHp();
                Class type = enemyAircraft.getClass();
                subNum *= MobEnemy.class.equals(type) ? 1.5 / 2.0 * factor : EliteEnemy.class.equals(type) ? 1.75 / 2.0 * factor : BossEnemy.class.equals(type) ? 1.0 * factor : 0;
                score -= subNum;
                scoreCnt += bossFlag ? 0 : subNum;
                score = Math.max(score, 0);
            }
        }
    }

    public void propMoveAction() {
        // 道具移动
        for (AbstractProp prop : props) {
            prop.forward();
        }
    }

    public void gameOverCheck() {
        if (heroAircraft.getHp() <= 0) {
            // 游戏结束
            bgm.stop();
            gameOverFlag = true;
            Record record = null;
//            gameOverThread = new MusicThread("src/audios/game_over.wav");
//            gameOverThread.start();
            System.out.println("Game Over!");
            Config.setScore(score);
            Config.setGameOver(true);
//            if (bgmThread != null && bgmThread.isAlive()) bgmThread.setInterrupt(true);
//            if (bgmBossThread != null && bgmBossThread.isAlive()) bgmBossThread.setInterrupt(true);
//            executorService.shutdownNow();
        }
    }

    /**
     * 后处理：
     * 1. 删除无效的子弹
     * 2. 删除无效的敌机
     * 3. 检查英雄机生存
     * <p>
     * 无效的原因可能是撞击或者飞出边界
     */
    public void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        props.removeIf(AbstractFlyingObject::notValid);
    }

    public void generateEnemyAircraft() {
        if (enemyAircrafts.size() <= enemyMaxNumber && enemyMaxNumber <= enemyMaxNumberUpperBound) {
            // 随机数控制产生精英敌机
            boolean createElite = Math.random() < 0.5;
            if (mobCnt < mobCntMax && !createElite) {
                enemyAircrafts.add(mobFactory.createEnemy(this.level));
                mobCnt++;
            } else {
                enemyAircrafts.add(eliteFactory.createEnemy(this.level));
                mobCnt = 0;
            }
        }

    }

    public void generateBoss() {
        // System.out.println("score: " + score + " scoreCnt: " + scoreCnt + " bossFlag: " + bossFlag);
        if (score > (BOSS_APPEAR_SCORE * level) && scoreCnt <= 0) {
            enemyAircrafts.add(bossFactory.createEnemy(this.level)); // 不改变boss血量
            scoreCnt = (int) (BOSS_APPEAR_SCORE * level);
            bossFlag = true;
            if (enemyMaxNumber < enemyMaxNumberUpperBound) {
                enemyMaxNumber++;
            }
            MediaPlayer bossBgm = MediaPlayer.create(this.context, R.raw.bgm_boss);
            bossBgm.start();
        }
    }

    public void generateAllEnemy() {
        System.out.printf("Time: %7d    Level:%7.4f    MobSpeed:%4d    EliteHp:%4d    PropValidMaxTime:%4d\n", time, level, Math.min((int) (5 * Math.sqrt(level)), 15), (int) (60 * Math.sqrt(this.level)), (int) (2000 / (5 + level)));
        // 新敌机产生
        generateEnemyAircraft();
        // 控制生成boss敌机
        generateBoss();
    }

    public void generateProp(AbstractEnemy enemyAircraft) {
        double randNum = Math.random();
        double threshHoldBlood = bloodPropGeneration;
        double threshHoldBomb = threshHoldBlood + bombPropGeneration;
        double threshHoldBullet = threshHoldBomb + bulletPropGeneration;
        if (randNum < threshHoldBlood) {
            props.add(bloodPropFactory.createProp(enemyAircraft.getLocationX(), enemyAircraft.getLocationY()));
        } else if (randNum < threshHoldBomb) {
            props.add(bombPropFactory.createProp(enemyAircraft.getLocationX(), enemyAircraft.getLocationY()));
        } else if (randNum < threshHoldBullet) {
            props.add(bulletPropFactory.createProp(enemyAircraft.getLocationX(), enemyAircraft.getLocationY()));
        } // else do nothing
    }

    public void generateBossProp(BossEnemy enemyAircraft) {
        props.add(bloodPropFactory.createProp(
                (int) (Math.random() * enemyAircraft.getLocationX() + ImageManager.BOSS_ENEMY_IMAGE.getWidth() / 2),
                (int) (Math.random() * enemyAircraft.getLocationY() + ImageManager.BOSS_ENEMY_IMAGE.getHeight() / 2)
        ));
        props.add(bombPropFactory.createProp(
                (int) (Math.random() * enemyAircraft.getLocationX() + ImageManager.BOSS_ENEMY_IMAGE.getWidth() / 2),
                (int) (Math.random() * enemyAircraft.getLocationY() + ImageManager.BOSS_ENEMY_IMAGE.getHeight() / 2)
        ));
        props.add(bulletPropFactory.createProp(
                (int) (Math.random() * enemyAircraft.getLocationX() + ImageManager.BOSS_ENEMY_IMAGE.getWidth() / 2),
                (int) (Math.random() * enemyAircraft.getLocationY() + ImageManager.BOSS_ENEMY_IMAGE.getHeight() / 2)
        ));
    }

    public void enemyShootAction() {
        // [DONE] 敌机射击
        for (AbstractEnemy enemyAircraft : enemyAircrafts) {
            enemyBullets.addAll(enemyShootContext.executeShootStrategy(enemyAircraft));
        }
    }

    public void heroShootAction() {
        // 英雄射击
        heroBullets.addAll(heroShootContext.executeShootStrategy(heroAircraft));
    }


    public void enemyShootHero() {
        for (BaseBullet bullet : enemyBullets) {
            if (bullet.notValid()) {
                continue;
            }
            if (heroAircraft.crash(bullet)) {
                heroAircraft.decreaseHp(bullet.getPower());
                bullet.vanish();
                MediaPlayer bulletHitBgm = MediaPlayer.create(this.context, R.raw.bullet_hit);
                bulletHitBgm.start();
            }
        }
    }

    public void aircraftCrashJudge(AbstractEnemy enemyAircraft) {
        if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
            // 护盾道具开启时
//            crashWithShieldThread = new MusicThread("src/audios/crash.wav");
//            crashWithShieldThread.start();
            if (bloodValidTimeCnt > 0) {
                if (EliteEnemy.class.equals(enemyAircraft.getClass())) {
                    bloodValidTimeCnt = Math.max(bloodValidTimeCnt - enemyAircraft.getHp() / 2, 3);
                    enemyAircraft.decreaseHp(bloodValidTimeCnt * 2);
                    if (enemyAircraft.notValid()) {
                        int increment = enemyAircraft.getScore();
                        score += increment;
                        scoreCnt -= bossFlag ? 0 : increment;
                        generateProp(enemyAircraft);
                    }
                } else if (BossEnemy.class.equals(enemyAircraft.getClass())) {
                    bloodValidTimeCnt = Math.max(bloodValidTimeCnt - enemyAircraft.getHp() / 2, 3);
                    enemyAircraft.decreaseHp(bloodValidTimeCnt * 2);
                    // 撞毁boss不加分
                } else {
                    bloodValidTimeCnt = Math.max(bloodValidTimeCnt - enemyAircraft.getHp() / 2, 3);
                    enemyAircraft.decreaseHp(bloodValidTimeCnt * 2);
                    if (enemyAircraft.notValid()) {
                        int increment = enemyAircraft.getScore();
                        score += increment;
                        scoreCnt -= bossFlag ? 0 : increment;
                    }
                }
            } else {
                heroAircraft.decreaseHp(heroAircraft.getMaxHp());
                enemyAircraft.vanish();
                // my Todo: 添加爆炸特效
            }
        }

    }

    public void heroShootEnemy() {
        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }
            for (AbstractEnemy enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    // 如果击毁的敌机是boss机，代表boss机在当前窗口消失
                    if (BossEnemy.class.equals(enemyAircraft.getClass())) {
                        bossFlag = false;
                    }
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircraft.crash(bullet)) {
                    MediaPlayer bulletHitBgm = MediaPlayer.create(this.context, R.raw.bullet_hit);
                    bulletHitBgm.start();
                    bulletCrash = true;
//                    bulletHitThread = new MusicThread("src/audios/bullet_hit.wav");
//                    bulletHitThread.start();
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
                    if (enemyAircraft.notValid()) {
//                        crashWithShieldThread = new MusicThread("src/audios/crash.wav");
//                        crashWithShieldThread.start();
                        if (BossEnemy.class.equals(enemyAircraft.getClass())) {
                            generateBossProp((BossEnemy) enemyAircraft);
                            bossFlag = false;
                            bossCnt += 1;
                            levelScalar += 1;
                        }
                        crashFlag = true;
                        int increment = enemyAircraft.getClass().equals(BossEnemy.class) ? 100 : enemyAircraft.getScore();
                        score += increment;
                        scoreCnt -= bossFlag ? 0 : increment;
                        // [DONE] 获得分数，产生道具补给
                        if (EliteEnemy.class.equals(enemyAircraft.getClass())) {
                            generateProp(enemyAircraft);
                        }
                    }
                }
                // 英雄机 与 敌机 相撞，均损毁
                // System.out.println("bloodValidTimeCntCrash: " + this.bloodValidTimeCnt);
                aircraftCrashJudge(enemyAircraft);
            }
        }
    }

    public void heroActivateProp() {
        for (AbstractProp prop : props) {
            if (prop.notValid()) {
                continue;
            }
            if (prop.crash(heroAircraft)) {
                prop.activate(heroAircraft, enemyAircrafts, heroBullets, enemyBullets, time);
                if (prop.getClass().equals(BombProp.class)) {
                    ((BombProp) prop).notifyAllSubscribers();
                    bombFlag = true;
//                    bombExplodeThread = new MusicThread("src/audios/bomb_explosion.wav");
//                    bombExplodeThread.start();
                    MediaPlayer bombBgm = MediaPlayer.create(this.context, R.raw.bomb_explosion);
                    bombBgm.start();
                } else if (prop.getClass().equals(BulletProp.class)) {
                    bulletFlag = true;
//                    bulletPropThread = new MusicThread("src/audios/bullet.wav");
//                    bulletPropThread.start();
                    heroAircraft.setBulletPropStage(heroAircraft.getBulletPropStage() + 1);
                    bulletValidTimeCnt = (int) (2000 / (5 + level));
                    MediaPlayer supplyBgm = MediaPlayer.create(this.context, R.raw.get_supply);
                    supplyBgm.start();
                } else if (prop.getClass().equals(BloodProp.class)) {
                    bloodFlag = true;
//                    bloodPropThread = new MusicThread("src/audios/get_supply.wav");
//                    bloodPropThread.start();
                    if (heroAircraft.getHp() == heroAircraft.getMaxHp()) {
                        bloodValidTimeCnt = (int) (2000 / (5 + level));
                    }
                    MediaPlayer supplyBgm = MediaPlayer.create(this.context, R.raw.get_supply);
                    supplyBgm.start();
                }
                int increment = prop.getScore();
                score += increment;
                scoreCnt -= bossFlag ? 0 : increment;
                prop.vanish();
            }
        }
    }

    /**
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    public void crashCheckAction() {
        // [DONE] 敌机子弹攻击英雄
        enemyShootHero();
        // 英雄子弹攻击敌机
        heroShootEnemy();
        // (DONE) 我方获得道具，道具生效
        heroActivateProp();
    }

    public void paintScoreAndLife() {
        int x = (int) (Config.screenWidth * 0.03);
        int y = (int) (Config.screenHeight * 0.87);
        textPaint.setTextSize(40);
//        textPaint.setStrokeWidth(3); // 加粗没效果
        if (Config.online) {
            textPaint.setColor(Color.CYAN);
            textPaint.setTypeface(Typeface.DEFAULT_BOLD);
            canvas.drawText("YSCORE:" + this.score, x, y, textPaint);
            textPaint.setColor(Color.YELLOW);
            canvas.drawText("FSCORE:" + this.fscore, Config.screenWidth * 0.60f - x, y, textPaint);
        } else {
            textPaint.setColor(Color.CYAN);
            textPaint.setTypeface(Typeface.DEFAULT_BOLD);
            canvas.drawText("SCORE:" + this.score, x, y, textPaint);

        }
    }

    public void paintImageWithPositionRevised(List<? extends AbstractFlyingObject> objects) {
        if (objects.size() == 0) {
            return;
        }
        for (AbstractFlyingObject object : objects) {
            Bitmap image = object.getImage();
            assert image != null : objects.getClass().getName() + " has no image! ";
            canvas.drawBitmap(image, object.getLocationX() - image.getWidth() / 2f, object.getLocationY() - image.getHeight() / 2f, imagePaint);
        }
    }

    public void paintBloodBar(float x, float y, float length, float height,
                              @ColorInt int bottom, @ColorInt int surface,
                              float curHp, float maxHp, boolean text) {
        float pointAX = x + 0, pointAY = y + 0;
        float pointBX = x + length, pointBY = y + 0;
        float pointCX = x + 0, pointCY = y + height;
        float pointDX = x + length, pointDY = y + height;
        /*
           pta: (x, y) ----------------------------- ptb: (x + bloodBarLength, y)
            |                                         |
            |                                         |
           ptc: (x, y + bloodBarHeight) ------------ ptd: (x + bloodBarLength, y + bloodBarHeight)
        */
        float[] pts = new float[]{
                pointAX, pointAY, pointBX, pointBY,
                pointCX, pointCY, pointDX, pointDY,
                pointAX, pointAY, pointCX, pointCY,
                pointBX, pointBY, pointDX, pointDY
        };
        // 绘制底层灰色
        shapePaint.setColor(bottom);
        canvas.drawRect(pointAX, pointAY, pointDX, pointDY, shapePaint);
        // 绘制血条红色
        shapePaint.setColor(surface);
        canvas.drawRect(pointAX, pointAY, pointAX + length * (curHp / maxHp), pointDY, shapePaint);
        // 绘制边框黑色
        shapePaint.setColor(Color.BLACK);
        canvas.drawLines(pts, shapePaint);
        textPaint.setColor(surface);
        textPaint.setTextSize(24);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        if (text) {
            @SuppressLint("DefaultLocale")
            String strDisplay = String.format(" %d / %d ", (int) curHp, (int) maxHp);
            canvas.drawText(strDisplay, pointDX - length * 0.3f, pointDY + height * 1.7f, textPaint);
        }
    }

    public void paintEnemyLife() {
        float bloodBarLength = ImageManager.HERO_IMAGE.getWidth() * 0.9f;
        float bloodBarOffsetX = ImageManager.HERO_IMAGE.getWidth() / 2f * 0.9f;
        float bloodBarOffsetY = ImageManager.HERO_IMAGE.getHeight() / 2f * 1.3f;
        float bloodBarHeight = ImageManager.HERO_IMAGE.getHeight() / 2f * 0.15f;
        float barMargin = ImageManager.HERO_IMAGE.getHeight() / 2f * 0.1f;
        for (AbstractEnemy enemy : enemyAircrafts) {
            if (!enemy.getClass().equals(BossEnemy.class)) {
                float x = enemy.getLocationX() - bloodBarOffsetX;
                float y = enemy.getLocationY() - bloodBarOffsetY;
                paintBloodBar(x, y, bloodBarLength, bloodBarHeight, Color.GRAY, Color.RED,
                        enemy.getHp(), enemy.getMaxHp(), true);
            } else {
                float x = Config.screenWidth * 0.1f;
                float y = Config.screenHeight * 0.01f;
                paintBloodBar(x, y, Config.screenWidth * 0.8f, bloodBarHeight * 2, Color.GRAY, Color.RED,
                        enemy.getHp(), enemy.getMaxHp(), true);
            }
        }

    }

    public void paintHeroAttributes() {
        float bloodBarLength = ImageManager.HERO_IMAGE.getWidth() * 0.9f;
        float bloodBarOffsetX = ImageManager.HERO_IMAGE.getWidth() / 2f * 0.9f;
        float bloodBarOffsetY = ImageManager.HERO_IMAGE.getHeight() / 2f * 1.3f;
        float bloodBarHeight = ImageManager.HERO_IMAGE.getHeight() / 2f * 0.15f;
        float barMargin = ImageManager.HERO_IMAGE.getHeight() / 2f * 0.1f;
        float x = heroAircraft.getLocationX() - bloodBarOffsetX;
        float y = heroAircraft.getLocationY() - bloodBarOffsetY;
        int currentPropValidMaxTime = (int) (2000 / (5 + level));

        // hp and blood prop
        int currentBloodPropStage = heroAircraft.getBloodPropStage();
        int currentBloodValidTime = bloodValidTimeCnt;
        Log.d(TAG, "paintHeroAttributes: " + x);
        if (currentBloodValidTime <= 0) {
            paintBloodBar(x, y, bloodBarLength, bloodBarHeight, Color.GRAY, Color.RED,
                    heroAircraft.getHp(), heroAircraft.getMaxHp(), true);
        } else {
            currentPropValidMaxTime = (int) (2000 / (5 + level));
            paintBloodBar(x, y, bloodBarLength, bloodBarHeight, Color.RED, Color.YELLOW,
                    currentBloodValidTime, currentPropValidMaxTime, true);
        }

        // draw bullet prop
        y -= bloodBarHeight + barMargin;
        int currentBulletPropStage = heroAircraft.getBulletPropStage();
        int currentBulletValidTime = bulletValidTimeCnt;
        currentPropValidMaxTime = (int) (2000 / (5 + level));
        // Todo: should add the constant max to Config value
        @ColorInt int bottom, surface;
        if (currentBulletPropStage == 0 || currentBulletPropStage == 1) {
            bottom = Color.GRAY;
            surface = Color.BLUE;
        } else if (currentBulletPropStage == 2) {
            bottom = Color.BLUE;
            surface = Color.CYAN;
        } else if (currentBulletPropStage == 3) {
            bottom = Color.CYAN;
            surface = Color.MAGENTA;
        } else {
            bottom = Color.GRAY;
            surface = Color.BLUE;
        }
        paintBloodBar(x, y, bloodBarLength, bloodBarHeight, bottom, surface,
                currentBulletValidTime, currentPropValidMaxTime, false);
    }

    public void paintBackground() {
        // 绘制背景,图片滚动
        canvas.drawBitmap(backgroundImage, 0, this.backGroundTop - backgroundImage.getHeight(), imagePaint);
        canvas.drawBitmap(backgroundImage, 0, this.backGroundTop, imagePaint);
        this.backGroundTop += 1;
        if (this.backGroundTop == backgroundImage.getHeight()) {
            this.backGroundTop -= backgroundImage.getHeight();
        }
    }

    public void draw() {
        canvas = mSurfaceHolder.lockCanvas();
        if (mSurfaceHolder == null || canvas == null) {
            return;
        }
        imagePaint.setAntiAlias(true);
        shapePaint.setAntiAlias(true);
        textPaint.setAntiAlias(true);

        // 绘制背景,图片滚动
        paintBackground();

        // 先绘制子弹，后绘制飞机
        paintImageWithPositionRevised(enemyBullets);
        paintImageWithPositionRevised(heroBullets);

        paintImageWithPositionRevised(enemyAircrafts);
        paintImageWithPositionRevised(props);
        canvas.drawBitmap(ImageManager.HERO_IMAGE, heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2f, heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2f, imagePaint);

        // 绘制得分和生命值
        paintScoreAndLife();
        // 绘制道具时间条
        paintHeroAttributes();
        // 绘制敌机生命条
        paintEnemyLife();

        mSurfaceHolder.unlockCanvasAndPost(canvas);
    }

    public boolean getGameOverFlag() {
        return gameOverFlag;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Notice: 该部分代码主要参考来自于tkj，实现不接触英雄机的相对位置移动，避免视角的遮挡
        float x = event.getX(), y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = x - curFingerX, dy = y - curFingerY;
                heroAircraft.setLocationX((int) (heroAircraft.getLocationX() + dx));
                heroAircraft.setLocationY((int) (heroAircraft.getLocationY() + dy));
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                this.heroAircraft.setLocation(heroAircraft.getLocationX(), heroAircraft.getLocationY());
                break;
        }
        curFingerX = x;
        curFingerY = y;
        return true;
    }


    @Override
    public void run() {
        while (!gameOverFlag) {
            Log.d("GameActivity", "run: Width:" + this.backgroundImage.getWidth());
            synchronized (mSurfaceHolder) {
                action();
                Log.d(TAG, "run: action");
                draw();
            }
            try {
                Thread.sleep(40);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        Intent intent = new Intent(GameActivity.class, FinishActivity.class);
//        notifyAll();
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        this.screenWidth = width;
        this.screenHeight = height;
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

        gameOverFlag = true;

    }
    //    abstract public void playBGM();
}

