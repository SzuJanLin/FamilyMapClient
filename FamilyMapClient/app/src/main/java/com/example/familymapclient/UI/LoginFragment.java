package com.example.familymapclient.UI;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.familymapclient.DataCache;
import com.example.familymapclient.Proxy;
import com.example.familymapclient.R;
import com.example.shared.requests.LoginRequest;
import com.example.shared.requests.RegisterRequest;
import com.example.shared.responses.EventsResponse;
import com.example.shared.responses.LoginResponse;
import com.example.shared.responses.PersonResponse;
import com.example.shared.responses.PersonsResponse;
import com.example.shared.responses.RegisterResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.net.MalformedURLException;
import java.net.URL;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class LoginFragment extends Fragment {

    private EditText serverHost;
    private EditText serverPort;
    private EditText userName;
    private EditText password;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private RadioGroup radioGroup;
    private Button signInButton;
    private Button registerButton;
    private Toast toast;

    private String hostInput;
    private String portInput;
    private String userNameInput;
    private String passwordInput;
    private String firstNameInput;
    private String lastNameInput;
    private String emailInput;
    private String gender = "m";
    private String authToken;
    public static final String ARG_TITLE = "title";


    private String loginFirstName;
    private String loginLastName;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){


        View view = inflater.inflate(R.layout.fragment_login,container,false);
        serverHost = view.findViewById(R.id.serverHost);
        serverPort = view.findViewById(R.id.serverPort);
        userName = view.findViewById(R.id.userName);
        password = view.findViewById(R.id.password);
        firstName = view.findViewById(R.id.firstName);
        lastName = view.findViewById(R.id.lastName);
        email = view.findViewById(R.id.email);

        radioGroup = (RadioGroup)view.findViewById(R.id.genderRadioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radio_male:
                        gender = "m";
                        break;
                    case R.id.radio_female:
                        gender = "f";
                        break;
                }
            }
        });

        serverHost.addTextChangedListener(enableTextWatcher);
        serverPort.addTextChangedListener(enableTextWatcher);
        userName.addTextChangedListener(enableTextWatcher);
        password.addTextChangedListener(enableTextWatcher);
        firstName.addTextChangedListener(enableTextWatcher);
        lastName.addTextChangedListener(enableTextWatcher);
        email.addTextChangedListener(enableTextWatcher);




        signInButton = view.findViewById(R.id.signIn);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    LoginTask loginTask = new LoginTask();
                   // loginTask.execute(new URL("https://localhost:8080/user/login"));

                    loginTask.execute(new URL("http://"+hostInput+":"+portInput+"/user/login"));

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        registerButton = view.findViewById(R.id.register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    RegisterTask registerTask = new RegisterTask();
                    registerTask.execute(new URL("http://"+hostInput+":"+portInput+"/user/register"));
                }catch (Exception e){

                }
            }
        });


        return view;
    }

    private TextWatcher enableTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            hostInput = serverHost.getText().toString().trim();
            portInput = serverPort.getText().toString().trim();
            userNameInput = userName.getText().toString().trim();
            passwordInput = password.getText().toString().trim();
            firstNameInput = firstName.getText().toString().trim();
            lastNameInput = lastName.getText().toString().trim();
            emailInput = email.getText().toString().trim();


            if(hostInput.equals("") || portInput.equals("") || userNameInput.equals("") || passwordInput.equals("")){
                signInButton.setEnabled(false);

            }else {
                signInButton.setEnabled(true);
            }

            if(hostInput.equals("") || portInput.equals("") || userNameInput.equals("") || passwordInput.equals("") ||firstNameInput.equals("") || lastNameInput.equals("")|| emailInput.equals("")) {
                registerButton.setEnabled(false);
            }else {
                registerButton.setEnabled(true);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    private class LoginTask extends AsyncTask<URL, Integer, LoginResponse>{

        @Override
        protected LoginResponse doInBackground(URL... urls) {
            Gson gson = new Gson();
            Proxy proxy = new Proxy();
            LoginRequest loginRequest = new LoginRequest(userNameInput,passwordInput);
            LoginResponse loginResponse = null;
            String loginInString = gson.toJson(loginRequest);
            try{
                String loginContent = proxy.postConnection(urls[0],loginInString);
                if(loginContent != null){

                    loginResponse = gson.fromJson(loginContent, LoginResponse.class);

                }else {
                    throw new IOException("No contented included");
                }

            }catch (IOException | Proxy.BadResponseCodeException ex){
                return new LoginResponse(ex.getMessage(),false);
            }
            return loginResponse;
        }

        @Override
        protected void onPostExecute(LoginResponse loginResponse) {
            super.onPostExecute(loginResponse);
            toast = null;
            if(loginResponse.isSuccess()){
                try {
                    FetchUserTask fetchUserTask = new FetchUserTask();
                    FetchPersonsTask fetchPersonsTask = new FetchPersonsTask();
                    FetchEventsTask fetchEventsTask = new FetchEventsTask();
                    authToken = loginResponse.getAuthToken();

                    fetchPersonsTask.execute(new URL("http://"+hostInput+":"+portInput+"/person"));
                    fetchEventsTask.execute(new URL("http://"+hostInput+":"+portInput+"/event"));
                    DataCache dataCache = DataCache.getInstance();
                    dataCache.setUserId(loginResponse.getPersonID());
                    fetchUserTask.execute(new URL("http://"+hostInput+":"+portInput+"/person/"+loginResponse.getPersonID()));

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }else {
                toast = Toast.makeText(getContext(),loginResponse.getMessage(), Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    private class RegisterTask extends AsyncTask<URL, Integer, RegisterResponse> {
        @Override
        protected RegisterResponse doInBackground(URL... urls) {
            Gson gson = new Gson();
            Proxy proxy = new Proxy();
            RegisterRequest registerRequest = new RegisterRequest(userNameInput,passwordInput,emailInput,firstNameInput,lastNameInput,gender);
            RegisterResponse registerResponse = null;
            String registerInString = gson.toJson(registerRequest);
            try{
                String registerContent = proxy.postConnection(urls[0],registerInString);
                if(registerContent != null){

                    registerResponse = gson.fromJson(registerContent, RegisterResponse.class);

                }else {
                    throw new IOException("No contented included");
                }

            }catch (IOException | Proxy.BadResponseCodeException ex){
                return new RegisterResponse(ex.getMessage(),false);
            }
            return registerResponse;
        }

        @Override
        protected void onPostExecute(RegisterResponse registerResponse) {
            super.onPostExecute(registerResponse);



            if(registerResponse.isSuccess()){
                try {
                    FetchUserTask fetchUserTask = new FetchUserTask();
                    FetchPersonsTask fetchPersonsTask = new FetchPersonsTask();
                    FetchEventsTask fetchEventsTask = new FetchEventsTask();
                    authToken = registerResponse.getAuthToken();

                    fetchPersonsTask.execute(new URL("http://"+hostInput+":"+portInput+"/person"));
                    fetchEventsTask.execute(new URL("http://"+hostInput+":"+portInput+"/event"));
                    DataCache dataCache = DataCache.getInstance();
                    dataCache.setUserId(registerResponse.getPersonID());
                    fetchUserTask.execute(new URL("http://"+hostInput+":"+portInput+"/person/"+registerResponse.getPersonID()));

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }


            }else {
                toast = Toast.makeText(getContext(),"Register Failed \n "+ registerResponse.getMessage(), Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }


    private class FetchUserTask extends AsyncTask<URL, Integer, PersonResponse>{

        @Override
        protected PersonResponse doInBackground(URL... urls) {
            PersonResponse personResponse = null;
            Gson gson = new Gson();
            Proxy proxy = new Proxy();
            try {
                String personContent = proxy.getConnection(urls[0],authToken);
                personResponse = gson.fromJson(personContent,PersonResponse.class);
                if(personResponse.isSuccess()){
                    loginFirstName = personResponse.getFirstName();
                    loginLastName = personResponse.getLastName();
                }else
                    throw new MalformedParameterizedTypeException();
            }catch (Exception e){
                e.printStackTrace();
            }
            return personResponse;
        }

        @Override
        protected void onPostExecute(PersonResponse personResponse) {

            DataCache dataCache = DataCache.getInstance();
            dataCache.sortFamilyMember(personResponse.getPersonID());

            toast = Toast.makeText(getContext(),"Welcome "+loginFirstName + " " + loginLastName, Toast.LENGTH_SHORT);
            toast.show();

            
            Fragment mapFragment = new MapFragment();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.insideFrameLayout,mapFragment);
            transaction.commit();

        }
    }


    private class FetchPersonsTask extends AsyncTask<URL, Integer, PersonsResponse>{

        @Override
        protected PersonsResponse doInBackground(URL... urls) {
            PersonsResponse personsResponse = null;
            Gson gson = new Gson();
            Proxy proxy = new Proxy();

            try {
                String personsContent = proxy.getConnection(urls[0],authToken);
                personsResponse = gson.fromJson(personsContent,PersonsResponse.class);
                if(personsResponse.isSuccess()){
                    //add into dataCache
                }else
                    throw new MalformedParameterizedTypeException();

            }catch (Exception e){
                e.printStackTrace();
            }
            return personsResponse;
        }

        @Override
        protected void onPostExecute(PersonsResponse personsResponse){
            DataCache dataCache = DataCache.getInstance();
            dataCache.initialPersonsDataInsert(personsResponse);
        }
    }
    private class FetchEventsTask extends AsyncTask<URL, Integer, EventsResponse>{

        @Override
        protected EventsResponse doInBackground(URL... urls) {
            EventsResponse eventsResponse = null;
            Gson gson = new Gson();
            Proxy proxy = new Proxy();

            try {
                String eventsContent = proxy.getConnection(urls[0],authToken);
                eventsResponse = gson.fromJson(eventsContent,EventsResponse.class);
                if(eventsResponse.isSuccess()){
                    //add into dataCache
                }else
                    throw new MalformedParameterizedTypeException();

            }catch (Exception e){
                e.printStackTrace();
            }
            return eventsResponse;
        }

        @Override
        protected void onPostExecute(EventsResponse eventsResponse){
            DataCache dataCache = DataCache.getInstance();
            dataCache.initialEventsDataInsert(eventsResponse);
        }
    }
}
