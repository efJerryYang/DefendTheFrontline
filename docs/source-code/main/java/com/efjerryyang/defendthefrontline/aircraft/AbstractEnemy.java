package com.efjerryyang.defendthefrontline.aircraft;

import com.efjerryyang.defendthefrontline.application.Config;
import com.efjerryyang.defendthefrontline.application.MainActivity;
import com.efjerryyang.defendthefrontline.subscriber.BombSubscriber;

import java.util.LinkedList;
import java.util.List;

/**
 * 抽象敌人类
 * <p>
 * score用于记录击毁敌人所获得的分数
 * type用于记录敌人种类
 *
 * @author JerryYang
 */
public abstract class AbstractEnemy extends AbstractAircraft implements BombSubscriber {
    private int score;

    public AbstractEnemy(int locationX, int locationY, int speedX, int speedY, int hp, int score) {
        super(locationX, locationY, speedX, speedY, hp);
        this.score = score + hp / 5;
    }

    @Override
    public void forward() {
        super.forward();
        if (locationX <= 0 || locationX >= Config.screenWidth) {
            // 横向超出边界后反向
            locationX = locationX <= 0 ? 1 : Config.screenWidth - 1;
            speedX = -speedX;
        }
        // 判定 y 轴向下飞行出界
        if (locationY >= Config.screenHeight) {
            vanish();
        }
    }

//    @Override
//    public List<BaseBullet> shoot() {
//        return new LinkedList<>();
//    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void explode() { }
}
