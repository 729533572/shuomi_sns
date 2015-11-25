
package main;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

import main.config.StringResource;

import framework.activity.Intent;
import framework.activity.ListActivity;
import framework.data.Account;
import framework.data.AccountManager;

public class SelectAccountActivity extends ListActivity implements CommandListener {

    private static final Command CMD_LOGIN =
            new Command(StringResource.BTN_LOGIN_LABEL, Command.ITEM, 1);
    private static final Command CMD_NEW_ACCOUNT =
            new Command(StringResource.BTN_NEW_ACCOUNT_LABEL, Command.ITEM, 1);
    private static final Command CMD_DEL_ACCOUNT =
            new Command(StringResource.BTN_DEL_ACCOUNT_LABEL, Command.ITEM, 1);
    private static final Command CMD_EXIT =
            new Command(StringResource.BTN_EXIT_LABEL, Command.EXIT, 1);

    public SelectAccountActivity(Display display) {
        super(StringResource.SELECT_ACCOUNT_ACTIVITY_TITLE, Choice.IMPLICIT, display);
        setSelectCommand(CMD_LOGIN);
        addCommand(CMD_NEW_ACCOUNT);
        addCommand(CMD_DEL_ACCOUNT);
        addCommand(CMD_EXIT);
        setCommandListener(this);
        updateList();
    }

    private void updateList() {
        deleteAll();
        for (int i = 0; i < AccountManager.getAccountCount(); i++) {
            Account account = AccountManager.getAccount(i);
            append(account.getUsername(), null);
        }
    }

    public void commandAction(Command cmd, Displayable displayable) {
        if (cmd == CMD_LOGIN || cmd == List.SELECT_COMMAND) {
            int idx = getSelectedIndex();
            SigninActivity activity = new SigninActivity(display, AccountManager.getAccount(idx), this);
            display.setCurrent(activity);
        } else if (cmd == CMD_NEW_ACCOUNT) {
            NewAccountActivity activity = new NewAccountActivity(display);
            display.setCurrent(activity);
        } else if (cmd == CMD_DEL_ACCOUNT) {
            int idx = getSelectedIndex();
            AccountManager.deleteAccount(idx);
            updateList();
        } else if (cmd == CMD_EXIT) {
            new Intent(Intent.RECEIVER_MAIN_APP, Intent.INTENT_EXIT_APP,
                    Intent.SENDER_SELECT_ACCOUNT_ACTIVITY).post();
        }
    }
}
