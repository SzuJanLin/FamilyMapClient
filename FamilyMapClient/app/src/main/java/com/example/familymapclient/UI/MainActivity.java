package com.example.familymapclient.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.location.Address;
import android.os.Bundle;

import com.example.familymapclient.R;

public class MainActivity extends AppCompatActivity {

    private  LoginFragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FragmentManager fm = this.getSupportFragmentManager();
        loginFragment = (LoginFragment) fm.findFragmentById(R.id.loginFrameLayout);
        if (loginFragment == null){
            loginFragment = createLoginFragment("LOGIN");
            fm.beginTransaction().add(R.id.loginFrameLayout,loginFragment).commit();
        }

    }



    private LoginFragment createLoginFragment(String title){
        LoginFragment fragment = new LoginFragment();

        Bundle args = new Bundle();
        args.putString(LoginFragment.ARG_TITLE,title);
        fragment.setArguments(args);

        return fragment;
    }
}
