
package framework.utils;

public class TextUtils {

    private static final char SEPARATOR = '%';
    private static StringBuffer buf = new StringBuffer(512);

    public static boolean isEmpty(String text) {
        return text == null || text.length() <= 0;
    }

    /**
     * Get the string by replacing the placeholders %1 to %9 with the params given
     * in the String array.
     * <p>
     *
     * @param formatter
     * @param params String[] array of the params to be filled in or <code>null</code>
     * @return the specified String or <code>null</code> if not found.
     */
    public static String get(String formatter, String[] params) {
        if (isEmpty(formatter) || params == null)
            return formatter;

        synchronized (buf) {
            buf.setLength(0);

            int prevPos = 0;
            int end = formatter.length() - 1;
            int pos = -1;

            for (;;) {
                // indexOf must eventually return -1 and end the loop

                pos = formatter.indexOf(SEPARATOR, prevPos);

                if (pos == -1 || pos == end) {
                    buf.append(formatter.substring(prevPos));
                    break;
                } else {
                    buf.append(formatter.substring(prevPos, pos));

                    prevPos = pos + 2;

                    int arg = formatter.charAt(pos + 1) - '1'; // %1 = params[0]
                    if (arg >= 0 && arg < 9) {
                        if (arg < params.length) {
                            buf.append(params[arg]);
                        } else {
                            buf.append("???"); // too few arguments specified
                        }
                    } else {
                        buf.append('%');
                    }
                }
            }

            return buf.toString();
        }
    }
}
