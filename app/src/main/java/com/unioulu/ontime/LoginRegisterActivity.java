package com.unioulu.ontime;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.unioulu.ontime.database_classes.DataHolder;
import com.unioulu.ontime.fragment.ForgetPasswordFragment;
import com.unioulu.ontime.fragment.LoginFragment;
import com.unioulu.ontime.fragment.RegisterFragment;

public class LoginRegisterActivity extends AppCompatActivity
    implements LoginFragment.OnFragmentInteractionListener,
    RegisterFragment.OnFragmentInteractionListener {

    private final String TAG_LOGIN_FRAGMENT = "fragment_login";
    private final String TAG_REGISTER_FRAGMENT = "fragment_register";
    private final String TAG_FORGET_PASSWORD_FRAGMENT = "fragment_forget_password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        LoginFragment loginPasswordFragment = LoginFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.login_content, loginPasswordFragment)
                .commit();
    }

    @Override
    public void registerAccountClicked() {
        RegisterFragment registerFragment = RegisterFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim
                .enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.login_content, registerFragment, TAG_REGISTER_FRAGMENT);
        transaction.addToBackStack(TAG_REGISTER_FRAGMENT);
        transaction.commit();
    }

    @Override
    public void forgetPasswordClicked() {
        ForgetPasswordFragment passwordFragment = ForgetPasswordFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim
                .enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.login_content, passwordFragment, TAG_FORGET_PASSWORD_FRAGMENT);
        transaction.addToBackStack(TAG_FORGET_PASSWORD_FRAGMENT);
        transaction.commit();
    }

    @Override
    public void proceedLogin(String username, String password) {
        String welcomeText = "Hello " + username + "!";
        Toast.makeText(this, welcomeText, Toast.LENGTH_SHORT).show();

        Intent adminUser = new Intent(LoginRegisterActivity.this, AdminMainActivity.class);
        adminUser.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(adminUser);
        finish();
    }

    @Override
    public void accountCreated() {
        LoginFragment loginFragment = LoginFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim
                .enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.login_content, loginFragment, TAG_LOGIN_FRAGMENT);
        transaction.addToBackStack(TAG_LOGIN_FRAGMENT);
        transaction.commit();
    }
}
