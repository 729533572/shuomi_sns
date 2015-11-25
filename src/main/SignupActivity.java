
package main;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Item;

import main.config.StringResource;

import framework.activity.IndicatorActivity;
import framework.data.Account;
import framework.net.task.HttpTaskListener;
import framework.net.task.SignupTask;
import framework.ui.Button;

public class SignupActivity extends IndicatorActivity implements HttpTaskListener {
    private static final Command CMD_TIMEOUT = new Command(StringResource.BTN_TIMEOUT_LABEL, Command.ITEM, 1);

    private Button btnTimeout;

    public SignupActivity(Display display, Account account, Displayable prev) {
        super(StringResource.INFO_TITLE, display, prev);

        btnTimeout = new Button(StringResource.BTN_TIMEOUT_LABEL, CMD_TIMEOUT, this);
        append(btnTimeout);

        SignupTask task = new SignupTask(account.getUsername(), account.getPassword(), account.getEmail(), this);
        task.start();
        onTaskStart();
    }

    public void commandAction(Command cmd, Item item) {
        if (cmd == CMD_TIMEOUT) {
            onTaskStop();
            // TODO: show twitter post list
        } else {
            super.commandAction(cmd, item);
        }
    }

    public void onFailure(String msg) {
        updateInfoAndStop(msg);
    }

    public void onUpdated(String msg) {
        updateInfo(msg);
    }

    public void onSuccess(String msg) {
        updateInfoAndStop(msg);
    }

    public void onSuccess(String msg, Object response, int responseType) {
        updateInfoAndStop(msg);
    }
}
