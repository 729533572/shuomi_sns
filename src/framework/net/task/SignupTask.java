
package framework.net.task;

import javax.microedition.io.HttpConnection;

import main.config.Config;
import main.config.StringResource;
import framework.json.JSONException;
import framework.json.JSONWriter;
import framework.utils.TextUtils;

public class SignupTask extends HttpTask {

    private String username;
    private String email;

    public SignupTask(String username, String password, String email, HttpTaskListener listener) {
        super(listener);
        String content = "";
        try {
            JSONWriter writer = new JSONWriter().object()
                    .key("username").value(username)
                    .key("password").value(password)
                    .key("email").value(email)
                    .endObject();
            content = writer.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setPostParams(Config.SIGNUP_URL, content);

        this.username = username;
        this.email = email;
    }

    protected void onStarted() {
        String msg = TextUtils.get(StringResource.INFO_SIGNNING_UP, new String[] {
                username
            });

        if (listener != null) {
            listener.onUpdated(msg);
        }
    }

    public void onHttpResult(boolean success, Object response, int responseCode, int responseType,
            String errMsg) {
        String msg;
        if (responseCode == HttpConnection.HTTP_CREATED) {
            msg = TextUtils.get(StringResource.INFO_SIGNUP_OK, new String[] {
                    username
                });
            if (listener != null) {
                listener.onSuccess(msg);
            }
        } else {
            if (responseCode == HttpConnection.HTTP_CONFLICT) {
                msg = TextUtils.get(StringResource.INFO_SIGNUP_CONFLICT, new String[] {
                        username, email
                    });
            } else {
                msg = TextUtils.get(StringResource.INFO_SIGNUP_FAIL, new String[] {
                        username
                    });
            }
            if (listener != null) {
                listener.onFailure(msg);
            }
        }
    }
}
