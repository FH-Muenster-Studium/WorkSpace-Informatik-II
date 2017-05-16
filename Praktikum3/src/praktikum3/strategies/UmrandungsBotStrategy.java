package praktikum3.strategies;

import praktikum3.Strategy;
import robocode.*;

import java.awt.event.KeyEvent;

/**
 * Created by fabianterhorst on 12.05.17.
 */
public class UmrandungsBotStrategy implements Strategy {

    private AdvancedRobot advancedRobot;

    private boolean right = false;

    @Override
    public void identify(AdvancedRobot advancedRobot) {
        this.advancedRobot = advancedRobot;
        this.advancedRobot.turnLeft(advancedRobot.getHeading());
    }

    @Override
    public void move() {
        advancedRobot.ahead(30);
        if (!right) {
            advancedRobot.turnGunLeft(90);
            advancedRobot.turnGunRight(90);
        } else {
            advancedRobot.turnGunRight(90);
            advancedRobot.turnGunLeft(90);
        }
    }

    @Override
    public void reactEvent(Event event) {
        if (event instanceof HitWallEvent) {
            if (!right) {
                advancedRobot.turnLeft(90);
            } else {
                advancedRobot.turnRight(90);
            }
        } else if (event instanceof HitByBulletEvent || event instanceof HitRobotEvent) {
            advancedRobot.turnLeft(advancedRobot.getHeading() % 90);
            if (!right) {
                advancedRobot.turnLeft(180);
                right = true;
            } else {
                advancedRobot.turnRight(180);
                right = false;
            }
        }
    }

    @Override
    public void reactKey(KeyEvent e) {

    }

    @Override
    public void fire(ScannedRobotEvent scannedRobotEvent) {
        advancedRobot.fire(1);
    }
}
