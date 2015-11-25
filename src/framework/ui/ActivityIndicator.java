
package framework.ui;

import javax.microedition.lcdui.Gauge;

public class ActivityIndicator extends Gauge implements Runnable {

    private boolean done = false;

    public ActivityIndicator() {
        super(null, false, Gauge.INDEFINITE, Gauge.INCREMENTAL_IDLE);
    }

    public void run() {
        while (!done) {
            setValue(Gauge.INCREMENTAL_UPDATING);

            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException err) {
            }
        }
    }

    public void start() {
        new Thread(this).start();
    }

    public void stop() {
        done = true;
    }
}
