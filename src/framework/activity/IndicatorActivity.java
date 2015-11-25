
package framework.activity;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.lcdui.StringItem;

import main.config.StringResource;

import framework.ui.ActivityIndicator;
import framework.ui.Button;

public class IndicatorActivity extends FormActivity implements ItemCommandListener {

    private static final Command CMD_CANCEL = new Command(StringResource.BTN_CANCEL_LABEL, Command.ITEM, 1);

    private Displayable prev;
    private Button btnCancel;
    private ActivityIndicator indicator;
    private StringItem info;

    public IndicatorActivity(String title, Display display, Displayable prev) {
        super(title, display);

        this.prev = prev;
        btnCancel = new Button(StringResource.BTN_CANCEL_LABEL, CMD_CANCEL, this);
        indicator = new ActivityIndicator();
        info = new StringItem(null, null);

        append(indicator);
        append(info);
        append(btnCancel);
    }

    protected void updateInfo(String msg) {
        info.setText(msg);
    }

    protected void updateInfoAndStop(String msg) {
        updateInfo(msg);
        onTaskStop();
    }

    protected void onTaskStart() {
        indicator.start();
    }

    protected void onTaskStop() {
        indicator.stop();
    }

    public void commandAction(Command cmd, Item item) {
        if (cmd == CMD_CANCEL) {
            onTaskStop();
            display.setCurrent(prev);
        }
    }
}
