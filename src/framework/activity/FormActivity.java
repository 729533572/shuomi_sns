
package framework.activity;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Form;

import framework.utils.AlertUtils;

public class FormActivity extends Form {

    protected Display display;

    public FormActivity(String title, Display display) {
        super(title);
        this.display = display;
    }

    public void show() {
        display.setCurrent(this);
    }

    protected void alert(String msg) {
        AlertUtils.showAlert(msg, display, this);
    }
}
