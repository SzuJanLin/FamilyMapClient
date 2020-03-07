package pj.jordanlin.familymapclientredo.UI.fragments;

import android.support.v4.app.Fragment;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.lang.reflect.Proxy;

public class LoginFragment extends Fragment {

    public LoginFragment(){};
    private static final String ARG_TITLE = "title";

    private String title;

    private EditText serverHost;

    private  EditText serverPort;

    private  EditText userName;

    private  EditText password;

    private  EditText firstName;

    private  EditText lastName;

    private  EditText email;

    private RadioGroup radioGroupLogin;

    private RadioButton radioButtonMaleLogin;

    private RadioButton getRadioButtonFemaleLogin;

}
