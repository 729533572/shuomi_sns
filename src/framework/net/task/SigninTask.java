
package framework.net.task;

import javax.microedition.io.HttpConnection;

import main.config.Config;
import main.config.StringResource;
import framework.json.JSONException;
import framework.json.JSONWriter;
import framework.utils.TextUtils;

public class SigninTask extends HttpTask {

    private String username;

    public SigninTask(String username, String password, HttpTaskListener listener) {
        super(listener);
        String content = "";
        try {
            JSONWriter writer = new JSONWriter().object()
                    .key("userId").value(username)
                    .key("password").value(password)
                    .endObject();
            content = writer.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setPostParams(Config.SIGNIN_URL, content);

        this.username = username;
    }

    protected void onStarted() {
        String msg = TextUtils.get(StringResource.INFO_SIGNNING_IN, new String[] {
                username
            });

        if (listener != null) {
            listener.onUpdated(msg);
        }
    }

    public void onHttpResult(boolean success, Object response, int responseCode, int responseType,
            String errMsg) {
        String msg;
        if (responseCode == HttpConnection.HTTP_OK) {
            msg = TextUtils.get(StringResource.INFO_SIGNIN_OK, new String[] {
                    username
                });
            if (listener != null) {
                listener.onSuccess(msg, response, responseType);
            }
        } else {
            if (responseCode == HttpConnection.HTTP_UNAUTHORIZED) {
                msg = TextUtils.get(StringResource.INFO_SIGNIN_UNAUTHORIZED, new String[] {
                        username
                    });
            } else {
                msg = TextUtils.get(StringResource.INFO_SIGNIN_FAIL, new String[] {
                        username
                    });
            }
            if (listener != null) {
                listener.onFailure(msg);
            }
        }
    }
}
