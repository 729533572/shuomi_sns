
package main;

import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import framework.activity.Intent;
import framework.activity.IntentHandler;
import framework.activity.IntentScheduler;
import framework.data.AccountManager;

public class MainApp extends MIDlet implements IntentHandler {

    private Display mainDisplay;
    private NewAccountActivity newAccountActivity;
    private SelectAccountActivity selectAccountActivity;
    private SigninActivity loginActivity;

    protected void destroyApp(boolean unconditional) {
        IntentScheduler.stop();
    }

    protected void pauseApp() {
        // TODO Auto-generated method stub
    }

    protected void startApp() throws MIDletStateChangeException {
        IntentScheduler.registerHandler(Intent.RECEIVER_MAIN_APP, this);
        IntentScheduler.start();

        AccountManager.loadAccounts();
        int accountCount = AccountManager.getAccountCount();

        mainDisplay = Display.getDisplay(this);

        if (accountCount > 1) {
            selectAccountActivity = new SelectAccountActivity(mainDisplay);
            selectAccountActivity.show();
        } else if (accountCount == 1) {
            selectAccountActivity = new SelectAccountActivity(mainDisplay);

            loginActivity = new SigninActivity(mainDisplay, AccountManager.getAccount(0), selectAccountActivity);
            mainDisplay.setCurrent(loginActivity);
        } else {
            newAccountActivity = new NewAccountActivity(mainDisplay);
            newAccountActivity.show();
        }
    }

    public void onIntent(int intentId, int senderId) {
        if (intentId == Intent.INTENT_EXIT_APP) {
            destroyApp(false);
            notifyDestroyed();
        }
    }

}
