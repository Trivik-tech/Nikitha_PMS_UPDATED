package com.triviktech.services.auth;

import com.triviktech.payloads.login.Login;

public interface LoginService {

    String verifyLogin(Login login);
}
