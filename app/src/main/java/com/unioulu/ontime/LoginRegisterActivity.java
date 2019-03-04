package com.unioulu.ontime;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.unioulu.ontime.fragment.LoginFragment;

public class LoginRegisterActivity extends AppCompatActivity {

    private final String TAG_LOGIN_FRAGMENT = "fragment_login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        LoginFragment loginPasswordFragment = LoginFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.login_content, loginPasswordFragment)
                .commit();
    }
}
