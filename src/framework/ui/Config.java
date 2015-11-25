
package framework.ui;

import javax.microedition.lcdui.Font;

public interface Config {

    Font BUTTON_FONT = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
    int BUTTON_FONT_COLOR_DISABLE = 0xFF8A8A8A;
    int BUTTON_FONT_COLOR_NORMAL = 0xFF000000;
    int BUTTON_BORDER_COLOR = 0xFF000000;
    int BUTTON_BGCOLOR_DISABLE = 0xFFD0D0D0;
    int BUTTON_BGCOLOR_NORMAL = 0xFFB1B1B1;
    int BUTTON_BGCOLOR_PRESSED = 0xFFFFD0FF;
    int BUTTON_MIN_WIDTH = 70;
    int BUTTON_MIN_HEIGHT = BUTTON_FONT.getHeight() * 2;
    int BUTTON_HORIZONTAL_PADDING = 4;

    int KEY_SELECT = -5;
}
