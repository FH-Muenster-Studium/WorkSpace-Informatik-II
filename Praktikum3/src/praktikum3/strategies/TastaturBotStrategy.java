package praktikum3.strategies;

import praktikum3.Strategy;
import robocode.AdvancedRobot;
import robocode.Event;
import robocode.ScannedRobotEvent;

import java.awt.event.KeyEvent;

/**
 * Created by fabianterhorst on 12.05.17.
 */
public class TastaturBotStrategy implements Strategy {

    private AdvancedRobot advancedRobot;

    @Override
    public void identify(AdvancedRobot advancedRobot) {
        this.advancedRobot = advancedRobot;
    }

    @Override
    public void move() {
        advancedRobot.doNothing();
    }

    @Override
    public void reactEvent(Event e) {

    }

    @Override
    public void reactKey(KeyEvent keyEvent) {
        switch (keyEvent.getKeyChar()) {
            case 'w':
                advancedRobot.ahead(75);
                break;
            case 'a':
                advancedRobot.turnLeft(10);
                break;
            case 's':
                advancedRobot.back(75);
                break;
            case 'd':
                advancedRobot.turnRight(10);
                break;
            case 'q':
                advancedRobot.turnGunLeft(10);
                break;
            case 'e':
                advancedRobot.turnGunRight(10);
                break;
            case ' ':
                advancedRobot.fire(0.1);
                break;

        }
    }

    @Override
    public void fire(ScannedRobotEvent e) {

    }
}
