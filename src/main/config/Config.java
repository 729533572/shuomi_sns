
package main.config;

public interface Config {

    int USERNAME_MAX_LEN = 30;
    int PASSWORD_MAX_LEN = 16;
    int EMAIL_MAX_LEN = 100;

    String SERVER = "http://localhost";
    String SIGNUP_URL = SERVER + "/account/signup";
    String SIGNIN_URL = SERVER + "/account/signin";
}
