
package framework.net.task;

/**
 * A listener for HTTP communication, providing a way to handle the
 * communication result.
 *
 * @author Oscar Cai
 */

public interface HttpTaskListener {

    /**
     * Indicate the content type of response from HTTP server is unknown.
     */
    public static final int UNKNOWN = 0;

    /**
     * Indicate the content type of response from HTTP server is plain text, including JSON, HTML,
     * etc.
     */
    public static final int TEXT = 1;

    /**
     * Indicate the content type of response from HTTP server is an image, including PNG, JPG, GIF,
     * etc.
     */
    public static final int IMAGE = 2;

    /**
     * Notify the task progress.
     *
     * @param msg task status description
     */
    public void onUpdated(String msg);

    /**
     * Notify the task has finished and succeeded.
     * @param msg task success message
     */
    public void onSuccess(String msg);

    /**
     * Notify the task has finished and succeeded.
     * @param msg task success message
     * @param response the response content from HTTP server.
     * @param responseType the content type of response, one of {@link #TEXT}, {@link #IMAGE} or {@link #UNKNOWN}.
     */
    public void onSuccess(String msg, Object response, int responseType);

    /**
     * Notify the task has finished and been failed.
     * @param msg task failure message
     */
    public void onFailure(String msg);
}
