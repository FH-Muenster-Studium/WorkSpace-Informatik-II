package praktikum3;

import java.awt.event.KeyEvent;

import robocode.AdvancedRobot;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.ScannedRobotEvent;
import praktikum3.strategies.KollisionsBotStrategy;
import praktikum3.strategies.TastaturBotStrategy;
import praktikum3.strategies.UmrandungsBotStrategy;

public class ChangeBot extends AdvancedRobot {

    private Strategy strategy;
    private Strategy[] strategies = new Strategy[3];
    private int strategyPos = 0;

    @SuppressWarnings("InfiniteLoopStatement")
    public void run() {
        strategies[0] = new KollisionsBotStrategy();
        strategies[0].identify(this);
        strategies[1] = new TastaturBotStrategy();
        strategies[1].identify(this);
        strategies[2] = new UmrandungsBotStrategy();
        strategies[2].identify(this);

        strategy = strategies[0];
        while (true) {
            strategy.move();
        }
    }

    @Override
    public void onKeyPressed(KeyEvent keyEvent) {
        out.println("taste" + keyEvent.getKeyChar());
        if (keyEvent.getKeyChar() == 'c') {
            toggleStrategies();
            out.println("change!");
        } else {
            strategy.reactKey(keyEvent);
        }
    }

    public void onHitByBullet(HitByBulletEvent hitByBulletEvent) {
        strategy.reactEvent(hitByBulletEvent);
    }

    public void onHitRobot(HitRobotEvent hitRobotEvent) {
        strategy.reactEvent(hitRobotEvent);
    }

    public void onHitWall(HitWallEvent hitWallEvent) {
        strategy.reactEvent(hitWallEvent);
    }

    public void onScannedRobot(ScannedRobotEvent scannedRobotEvent) {
        strategy.fire(scannedRobotEvent);
    }

    private synchronized void toggleStrategies() {
        strategyPos += 1;
        if (strategyPos >= strategies.length) {
            strategyPos = 0;
        }
        out.println("Strategiewechsel auf " + strategyPos);
        strategy = strategies[strategyPos];
    }
}
