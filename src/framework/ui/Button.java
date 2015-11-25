
package framework.ui;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.lcdui.StringItem;

public class Button extends StringItem {

    public Button(String label, Command cmd, ItemCommandListener listener) {
        super(null, label, Item.BUTTON);
        setDefaultCommand(cmd);
        setItemCommandListener(listener);
    }
}
