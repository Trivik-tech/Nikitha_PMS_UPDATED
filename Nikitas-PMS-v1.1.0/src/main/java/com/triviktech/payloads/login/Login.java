package com.triviktech.payloads.login;

/**
 * Payload class used for login requests.
 * <p>
 * Carries the username and password provided by the user
 * when attempting to authenticate.
 */

public class Login {

    private String username;

    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
