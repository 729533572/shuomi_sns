
package framework.activity;

import java.util.Hashtable;
import java.util.Vector;

public class IntentScheduler implements Runnable {

    private static Hashtable handlers = new Hashtable();
    private static Vector intents = new Vector();
    private static IntentScheduler  sInstance = null;
    private boolean keepRunning;

    public static void start() {
        if (sInstance == null) {
            sInstance = new IntentScheduler();
            sInstance.keepRunning = true;
            new Thread(sInstance).start();
        }
    }

    public static void stop() {
        if (sInstance != null) {
            synchronized (sInstance) {
                sInstance.keepRunning = false;
            }
        }
        notifyWorkingThread();
    }

    public static void pushIntent(Intent intent) {
        intents.addElement(intent);
        notifyWorkingThread();
    }

    public static void registerHandler(int receiverId, IntentHandler handler) {
        handlers.put(new Integer(receiverId), handler);
        notifyWorkingThread();
    }

    private static void notifyWorkingThread() {
        if (sInstance != null) {
            synchronized (sInstance) {
                sInstance.notify();
            }
        }
    }

    public void run() {
        while (sInstance.keepRunning) {
            synchronized (sInstance) {
                while (intents.isEmpty() || handlers.isEmpty()) {
                    try {
                        sInstance.wait();
                    } catch (InterruptedException ie) {
                        // ignore
                    }
                }

                for (int i = 0; i < intents.size(); i++) {
                    Intent intent = (Intent) intents.elementAt(i);
                    Integer key = new Integer(intent.getReceiverId());
                    if (handlers.containsKey(key)) {
                        ((IntentHandler)handlers.get(key)).onIntent(intent.getIntentId(), intent.getSenderId());
                        intents.removeElementAt(i--);
                    }
                }
            }
        }
    }
}
