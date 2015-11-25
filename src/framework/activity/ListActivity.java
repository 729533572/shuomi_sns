
package framework.activity;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.List;

import framework.utils.AlertUtils;

public class ListActivity extends List {

    protected Display display;

    public ListActivity(String title, int type, Display display) {
        super(title, type);
        this.display = display;
    }

    public void show() {
        display.setCurrent(this);
    }

    protected void alert(String msg) {
        AlertUtils.showAlert(msg, display, this);
    }
}
