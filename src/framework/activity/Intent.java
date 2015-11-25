
package framework.activity;

public class Intent {

    private static final int INTENT_BASE = 100;
    public static final int INTENT_EXIT_APP = INTENT_BASE;

    private static final int SENDER_BASE = 1000;
    public static final int SENDER_LOGIN_ACTIVITY = SENDER_BASE;
    public static final int SENDER_NEW_ACCOUNT_ACTIVITY = SENDER_BASE + 1;
    public static final int SENDER_SELECT_ACCOUNT_ACTIVITY = SENDER_BASE + 2;
    public static final int SENDER_REGISTER_ACCOUNT_ACTIVITY = SENDER_BASE + 3;

    private static final int RECEIVER_BASE = 3000;
    public static final int RECEIVER_MAIN_APP = RECEIVER_BASE;

    private int receiverId;
    private int intentId;
    private int senderId;

    public Intent(int receiverId, int intentId, int senderId) {
        this.receiverId = receiverId;
        this.intentId = intentId;
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public int getIntentId() {
        return intentId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void post() {
        IntentScheduler.pushIntent(this);
    }
}
