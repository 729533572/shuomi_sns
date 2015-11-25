
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
import framework.ui.Button;
import framework.ui.Editor;
import framework.utils.TextUtils;

public class SignupAccountActivity extends FormActivity implements ItemCommandListener {

    private static final Command CMD_COMMIT =
            new Command(StringResource.BTN_COMMIT_LABEL, Command.OK, 1);
    private static final Command CMD_EXIT =
            new Command(StringResource.BTN_EXIT_LABEL, Command.EXIT, 1);

    private Editor editorUsername;
    private Editor editorPassword;
    private Editor editorPassword2;
    private Editor editorEmail;
    private Button btnCommit;
    private Button btnExit;

    public SignupAccountActivity(Display display) {
        super(StringResource.NEW_ACCOUNT_ACTIVITY_TITLE, display);

        editorUsername =
                new Editor(StringResource.USERNAME_LABEL, null, Config.USERNAME_MAX_LEN, Editor.ANY);
        editorPassword =
                new Editor(StringResource.PASSWORD_LABEL, null, Config.PASSWORD_MAX_LEN,
                        Editor.PASSWORD);
        editorPassword2 =
                new Editor(StringResource.PASSWORD2_LABEL, null, Config.PASSWORD_MAX_LEN,
                        Editor.PASSWORD);
        editorEmail =
                new Editor(StringResource.EMAIL_LABEL, null, Config.EMAIL_MAX_LEN, Editor.EMAILADDR);
        btnCommit = new Button(StringResource.BTN_COMMIT_LABEL, CMD_COMMIT, this);
        btnExit = new Button(StringResource.BTN_EXIT_LABEL, CMD_EXIT, this);

        append(editorUsername);
        append(editorPassword);
        append(editorPassword2);
        append(editorEmail);
        append(btnCommit);
        append(btnExit);
    }

    public void commandAction(Command cmd, Item item) {
        if (cmd == CMD_COMMIT) {
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

            String password2 = editorPassword2.getString();
            if (TextUtils.isEmpty(password)) {
                alert(StringResource.INFO_INPUT_PASSWORD2);
                return;
            }

            if (!password.equals(password2)) {
                alert(StringResource.INFO_PASSWORDS_DONT_EQUAL);
                return;
            }

            String email = editorEmail.getString();

            Account account = new Account(Account.NOT_SAVED, username, password, email);
            SignupActivity activity = new SignupActivity(display, account, this);
            display.setCurrent(activity);
        } else if (cmd == CMD_EXIT) {
            new Intent(Intent.RECEIVER_MAIN_APP, Intent.INTENT_EXIT_APP,
                    Intent.SENDER_NEW_ACCOUNT_ACTIVITY).post();
        }
    }
}
