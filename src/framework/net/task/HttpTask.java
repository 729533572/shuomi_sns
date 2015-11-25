
package framework.net.task;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.Image;

import main.config.StringResource;

import framework.utils.TextUtils;

/**
 * HttpTask wraps an asynchronous way to communicate with HTTP server.
 *
 * @author Oscar Cai
 */

public abstract class HttpTask implements Runnable {

    /**
     * Indicate the communication does not start yet.
     */
    private static final int NOT_START = 1;

    /**
     * Indicate the communication is undergoing.
     */
    private static final int RUNNING = 2;

    /**
     * Indicate the communication has finished.
     */
    private static final int FINISHED = 3;

    /**
     * The timeout duration of communication, in milliseconds.
     */
    private static final long TIME_OUT = 60000;

    /**
     * The method for the URL request, one of:
     * <ul>
     * <li>{@link HttpConnection.GET}</li>
     * <li>{@link HttpConnection.POST}</li>
     * <li>{@link HttpConnection.HEAD}</li>
     * </ul>
     * are legal, subject to protocol restrictions. The default value is {@link HttpConnection.GET}.
     */
    private String method;

    /**
     * The requested URL
     */
    private String url;

    /**
     * The listener to handle communication result
     */
    protected HttpTaskListener listener;

    /**
     * The post content string for Post method.
     */
    private String postContent;

    /**
     * The running state of the communication, one of:
     * <ul>
     * <li>{@link #NOT_START}</li>
     * <li>{@link #RUNNING}</li>
     * <li>{@link #FINISHED}</li>
     * </ul>
     * are legal, The default value is {@link #NOT_START}.
     */
    private int runningState;

    /**
     * The HTTP connection instance.
     */
    private HttpConnection connection;

    /**
     * The output communication channel
     */
    private OutputStream outputStream;

    /**
     * The input communication channel
     */
    private InputStream inputStream;

    /**
     * The starting time of communication, in milliseconds.
     */
    private long startTime;

    /**
     * Create an HttpTask without specifying method
     */
    public HttpTask(HttpTaskListener listener) {
        setListener(listener);
        runningState = NOT_START;
    }

    /**
     * Create an HttpTask using Get method.
     *
     * @param url the URL for the connection.
     * @param params the query parameters for Get method
     * @param listener the listener to handle communication result
     */
    public HttpTask(String url, Hashtable params, HttpTaskListener listener) {
        this(listener);
        setGetParams(url, params);
    }

    /**
     * Create an HttpTask using Post method.
     *
     * @param url the URL for the connection.
     * @param content the post content for Post method
     * @param listener the listener to handle communication result
     */
    public HttpTask(String url, String content, HttpTaskListener listener) {
        this(listener);
        setPostParams(url, content);
    }

    private void setListener(HttpTaskListener listener) {
        this.listener = listener;
    }

    protected void setGetParams(String url, Hashtable params) {
        method = HttpConnection.GET;
        this.url = encodeUrl(url);
        if (params != null) {
            StringBuffer sb = new StringBuffer(this.url);
            if (this.url.indexOf("=") < 0 && !this.url.endsWith("?")) {
                sb.append("?");
            }
            if (this.url.indexOf("=") > 0 && !this.url.endsWith("&")) {
                sb.append("&");
            }

            Enumeration e = params.keys();
            do {
                String key = (String) e.nextElement();
                sb.append(key);
                sb.append("=");
                sb.append(params.get(key));
                sb.append("&");
            } while (e.hasMoreElements());
            sb.deleteCharAt(sb.length() - 1);

            this.url = sb.toString();
        }
    }

    protected void setPostParams(String url, String content) {
        method = HttpConnection.POST;
        this.url = encodeUrl(url);
        postContent = TextUtils.isEmpty(content) ? "" : content;
    }

    private String encodeUrl(String url) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < url.length(); i++) {
            char c = url.charAt(i);
            if (c == ' ') {
                sb.append("%20");
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Tell whether the communication has finished.
     *
     * @return true if the communication has finished, false otherwise.
     */
    public boolean isFinished() {
        return runningState == FINISHED;
    }

    public boolean isTimeout() {
        long curTime = System.currentTimeMillis();
        return runningState == RUNNING && curTime - startTime > TIME_OUT;
    }

    /**
     * Start communication
     */
    public void start() {
        runningState = RUNNING;
        new Thread(this).start();
        onStarted();
    }

    /**
     * Stop and close the communication.
     */
    public void stop() {
        try {
            if (inputStream != null) {
                inputStream.close();
                inputStream = null;
            }

            if (outputStream != null) {
                outputStream.close();
                outputStream = null;
            }
            if (connection != null) {
                connection.close();
                connection = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            runningState = FINISHED;
        }
    }

    /**
     * Notify the listener that the communication finish.
     *
     * @param success whether communicate successfully, true for success, false for failure.
     * @param response the response content from HTTP server.
     * @param responseCode the response code
     * @param responseType the content type of response, one of {@link HttpTaskListener#TEXT},
     * {@link HttpTaskListener#IMAGE} or {@link HttpTaskListener#UNKNOWN}.
     * @param errMsg failure description if success is false, null if success is true.
     */
    protected abstract void onHttpResult(boolean success, Object response, int responseCode,
            int responseType, String errMsg);

    /**
     * Notify the listener that the communication starts.
     */
    protected abstract void onStarted();

    /**
     * Notify the listener that the communication is successful.
     * <p>
     * See {@link HttpTaskListener#onHttpResult(boolean, Object, int, int, String)}.
     */
    private void notifySuccess(Object response, int responseCode, int responseType) {
        onHttpResult(true, response, responseCode, responseType, null);
    }

    /**
     * Notify the listener that the communication is failed.
     * <p>
     * See {@link HttpTaskListener#onHttpResult(boolean, Object, int, int, String)}.
     */
    private void notifyFailure(String errMsg) {
        notifyFailure(HttpConnection.HTTP_BAD_REQUEST, errMsg);
    }

    /**
     * Notify the listener that the communication is failed.
     * <p>
     * See {@link HttpTaskListener#onHttpResult(boolean, Object, int, int, String)}.
     */
    private void notifyFailure(int responseCode, String errMsg) {
        onHttpResult(false, null, responseCode, HttpTaskListener.UNKNOWN, errMsg);
    }

    /**
     * Notify the listener that the communication times out.
     * <p>
     * See {@link #notifyFailure(String)}.
     */
    public void notifyTimeout() {
        notifyFailure(HttpConnection.HTTP_CLIENT_TIMEOUT, StringResource.INFO_CONNECTING_TIMEOUT);
    }

    /**
     * Read all data from specified InputStream.
     *
     * @param is the InputStream instance to read
     * @return byte array of all read data.
     * @throws IOException if an error occurred reading data
     */
    private byte[] readAllData(InputStream is) throws IOException {
        byte[] buf = new byte[256];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int actualRead;
        while ((actualRead = is.read(buf)) != -1) {
            baos.write(buf, 0, actualRead);
        }
        return baos.toByteArray();
    }

    /**
     * @see java.lang.Runnable#run()
     */
    public void run() {
        // record starting time
        startTime = System.currentTimeMillis();

        // check method
        if (TextUtils.isEmpty(method)) {
            method = HttpConnection.GET;
        }

        try {
            // create connection instance
            connection = (HttpConnection) Connector.open(url);
            connection.setRequestMethod(method);

            // set request property
            connection
                    .setRequestProperty(
                            "accept",
                            "application/vnd.wap.wmlscriptc, text/vnd.wap.wml, application/vnd.wap.xhtml+xml, application/xhtml+xml, text/html, multipart/mixed, */*, text/x-vcard, text/x-vcalendar, image/*");
            connection.setRequestProperty("accept-encoding", "gzip, deflate");
            connection.setRequestProperty("accept-charset",
                    "ISO-8859-1, US-ASCII, UTF-8; Q=0.8, ISO-10646-UCS-2; Q=0.6");
            connection.setRequestProperty("accept-language", "zh-CN");

            if (method.equals(HttpConnection.POST)) {
                connection.setRequestProperty("content-language", "zh-CN");
                connection.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("content-length", String
                        .valueOf(postContent.length()));

                outputStream = connection.openOutputStream();
                outputStream.write(postContent.getBytes());
                outputStream.close();
            }

            // send/post request and get the response code
            if (connection.getResponseCode() >= HttpConnection.HTTP_BAD_REQUEST) {
                notifyFailure(connection.getResponseCode(), connection.getResponseMessage());
                return;
            }

            // read response
            inputStream = connection.openInputStream();
            byte[] buf = readAllData(inputStream);
            inputStream.close();

            // parse response content type.
            String contentType = connection.getHeaderField("content-type");
            int typeId = HttpTaskListener.UNKNOWN;
            Object response = buf;
            if (!TextUtils.isEmpty(contentType)) {
                contentType = contentType.toLowerCase();
                if (contentType.startsWith("text")) {
                    typeId = HttpTaskListener.TEXT;
                    int csIndex = contentType.indexOf("charset=");
                    String jsonStr;
                    if (csIndex != -1) {
                        jsonStr = new String(buf, contentType.substring(csIndex + 8));
                    } else {
                        jsonStr = new String(buf);
                    }

                    // remove UTF-8 ROM
                    if (jsonStr.charAt(0) == 0x0FEFF) {
                        jsonStr = jsonStr.substring(1);
                    }

                    response = jsonStr;
                } else if (contentType.startsWith("image")) {
                    typeId = HttpTaskListener.IMAGE;
                    response = Image.createImage(buf, 0, buf.length);
                }
            }

            notifySuccess(response, connection.getResponseCode(), typeId);
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
            notifyFailure(e.getMessage());
        } catch (SecurityException e) {
            e.printStackTrace();
            notifyFailure(e.getMessage());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            notifyFailure(e.getMessage());
        } finally {
            stop();
        }
    }
}
