
package framework.utils;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;

import main.config.StringResource;

public class AlertUtils {

    public static void showAlert(String text, Display display, Displayable previous) {
        Alert alert = new Alert(StringResource.ALERT_TITLE, text, null, AlertType.ALARM);
        display.setCurrent(alert, previous);
    }
}
