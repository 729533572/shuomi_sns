
package main;

import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

import main.config.StringResource;

import framework.activity.Intent;
import framework.activity.ListActivity;

public class PostListActivity extends ListActivity implements CommandListener {

    private static final Command CMD_POST =
            new Command(StringResource.BTN_POST_LABEL, Command.ITEM, 1);
    private static final Command CMD_REPLY =
            new Command(StringResource.BTN_REPLY_LABEL, Command.ITEM, 1);
    private static final Command CMD_DELETE =
            new Command(StringResource.BTN_DELETE_LABEL, Command.ITEM, 1);
    private static final Command CMD_REFRESH =
            new Command(StringResource.BTN_REFRESH_LABEL, Command.ITEM, 1);
    private static final Command CMD_EXIT =
            new Command(StringResource.BTN_EXIT_LABEL, Command.EXIT, 1);

    private Vector posts = new Vector();

    public PostListActivity(Display display) {
        super(StringResource.POST_LIST_ACTIVITY_TITLE, List.IMPLICIT, display);
        setCommandListener(this);
        refreshList();
    }

    private void refreshList() {
        synchronized (posts) {
            posts.removeAllElements();

            // TODO: update posts list and UI
        }
    }

    public void commandAction(Command cmd, Displayable displayable) {
        if (cmd == CMD_POST) {
            // TODO: switch to new post UI
        } else if (cmd == CMD_REPLY) {
            // TODO: switch to reply post UI
        } else if (cmd == CMD_DELETE) {
            // TODO: launch delete post task and refresh UI when task succeeds.
        } else if (cmd == CMD_REFRESH) {
            refreshList();
        } else if (cmd == CMD_EXIT) {
            new Intent(Intent.RECEIVER_MAIN_APP, Intent.INTENT_EXIT_APP,
                    Intent.SENDER_NEW_ACCOUNT_ACTIVITY).post();
        }
    }
}
