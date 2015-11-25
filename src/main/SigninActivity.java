
package main;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Item;

import main.config.StringResource;

import framework.activity.IndicatorActivity;
import framework.data.Account;
import framework.json.JSONException;
import framework.json.JSONObject;
import framework.net.task.HttpTaskListener;
import framework.net.task.SigninTask;
import framework.ui.Button;
import framework.utils.TextUtils;

public class SigninActivity extends IndicatorActivity implements HttpTaskListener {
    private static final Command CMD_TIMEOUT = new Command(StringResource.BTN_TIMEOUT_LABEL, Command.ITEM, 1);

    private Button btnTimeout;
    private Account account;

    public SigninActivity(Display display, Account account, Displayable prev) {
        super(StringResource.INFO_TITLE, display, prev);

        this.account = account;

        btnTimeout = new Button(StringResource.BTN_TIMEOUT_LABEL, CMD_TIMEOUT, this);
        append(btnTimeout);

        SigninTask task = new SigninTask(account.getUsername(), account.getPassword(), this);
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

    public void onSuccess(String msg) {
        updateInfoAndStop(msg);
    }

    public void onSuccess(String msg, Object response, int responseType) {
        updateInfoAndStop(msg);

        String jsonStr = "";
        if (responseType == HttpTaskListener.TEXT) {
            jsonStr = (String)response;
        }

        if (TextUtils.isEmpty(jsonStr)) {
            return;
        }

        try {
            JSONObject jsonObj = new JSONObject((String)jsonStr);
            account.setToken(jsonObj.getString("token"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onUpdated(String msg) {
        updateInfo(msg);
    }
}
