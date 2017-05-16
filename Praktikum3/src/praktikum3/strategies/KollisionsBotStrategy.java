package praktikum3.strategies;

import praktikum3.Strategy;
import robocode.AdvancedRobot;
import robocode.Event;
import robocode.HitRobotEvent;
import robocode.ScannedRobotEvent;

import java.awt.event.KeyEvent;

/**
 * Created by fabianterhorst on 12.05.17.
 */
public class KollisionsBotStrategy implements Strategy {

    private AdvancedRobot advancedRobot;

    private int turnDirection = 1;

    @Override
    public void identify(AdvancedRobot advancedRobot) {
        this.advancedRobot = advancedRobot;
    }

    @Override
    public void move() {
        advancedRobot.turnRight(5 * turnDirection);
    }

    @Override
    public void reactEvent(Event event) {
        if (event instanceof HitRobotEvent) {
            HitRobotEvent hitRobotEvent = (HitRobotEvent) event;
            if (hitRobotEvent.getBearing() >= 0) {
                turnDirection = 1;
            } else {
                turnDirection = -1;
            }
            advancedRobot.turnRight(hitRobotEvent.getBearing());
            advancedRobot.fire(1);
            advancedRobot.ahead(40);
        }
    }

    @Override
    public void reactKey(KeyEvent e) {

    }

    @Override
    public void fire(ScannedRobotEvent scannedRobotEvent) {
        if (scannedRobotEvent.getBearing() >= 0) {
            turnDirection = 1;
        } else {
            turnDirection = -1;
        }
        advancedRobot.turnRight(scannedRobotEvent.getBearing());
        advancedRobot.ahead(scannedRobotEvent.getDistance() + 5);
        advancedRobot.scan();
    }
}
