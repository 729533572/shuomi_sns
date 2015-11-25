
package main;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;

import main.config.Config;
import main.config.StringResource;

import framework.activity.FormActivity;
import framework.activity.Intent;
import framework.data.Account;
import framework.data.AccountManager;
import framework.ui.Button;
import framework.ui.Editor;
import framework.utils.TextUtils;

public class NewAccountActivity extends FormActivity implements ItemCommandListener {

    private static final Command CMD_LOGIN =
            new Command(StringResource.BTN_LOGIN_LABEL, Command.ITEM, 1);
    private static final Command CMD_REGISTER =
            new Command(StringResource.BTN_REGISTER_LABEL, Command.ITEM, 1);
    private static final Command CMD_EXIT =
            new Command(StringResource.BTN_EXIT_LABEL, Command.EXIT, 1);

    private Editor editorUsername;
    private Editor editorPassword;
    private Button btnLogin;
    private Button btnRegister;
    private Button btnExit;

    public NewAccountActivity(Display display) {
        super(StringResource.NEW_ACCOUNT_ACTIVITY_TITLE, display);

        editorUsername =
                new Editor(StringResource.USERNAME_LABEL, null, Config.USERNAME_MAX_LEN, Editor.ANY);
        editorPassword =
                new Editor(StringResource.PASSWORD_LABEL, null, Config.PASSWORD_MAX_LEN,
                        Editor.PASSWORD);
        btnLogin = new Button(StringResource.BTN_LOGIN_LABEL, CMD_LOGIN, this);
        btnRegister = new Button(StringResource.BTN_REGISTER_LABEL, CMD_REGISTER, this);
        btnExit = new Button(StringResource.BTN_EXIT_LABEL, CMD_EXIT, this);

        append(editorUsername);
        append(editorPassword);
        append(btnLogin);
        append(btnRegister);
        append(btnExit);
    }

    public void commandAction(Command cmd, Item item) {
        if (cmd == CMD_LOGIN) {
            String username = editorUsername.getString();
            if (TextUtils.isEmpty(username)) {
                alert(StringResource.INFO_INPUT_USERNAME);
                return;
            }

            String password = editorPassword.getString();
            if (TextUtils.isEmpty(password)) {
                alert(StringResource.INFO_INPUT_PASSWORD);
                return;
            }

            Account account = new Account(Account.NOT_SAVED, username, password);

            AccountManager.addAccount(account);

            SigninActivity activity = new SigninActivity(display, account, this);
            display.setCurrent(activity);
        } else if (cmd == CMD_REGISTER) {
            SignupAccountActivity activity = new SignupAccountActivity(display);
            display.setCurrent(activity);
        } else if (cmd == CMD_EXIT) {
            new Intent(Intent.RECEIVER_MAIN_APP, Intent.INTENT_EXIT_APP,
                    Intent.SENDER_NEW_ACCOUNT_ACTIVITY).post();
        }
    }
}
