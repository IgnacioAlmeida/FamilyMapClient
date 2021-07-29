package com.example.familymapclient;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Model.Person;
import Requests.LoginRequest;
import Requests.RegisterRequest;
import Responses.LoginResponse;
import Responses.RegisterResponse;
import cache.DataCache;

public class LoginFragment extends Fragment {
    private static final String USER_FULL_NAME = "UserFullName";
    private static final String RESPONSE_STATUS = "ResponseStatus";
    private static final String AUTH_TOKEN = "AuthorizationToken";
    EditText etServerHost, etServerPort, etUsername, etPassword, etFirstName, etLastName, etEmail;
    Button registerBtn, signInBtn;
    RadioButton radiobtnMale;
    RadioGroup radiogrpGender;
    String gender;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.login_fragment, container,false);

        etServerHost = view.findViewById(R.id.serverHost);
        etServerPort = view.findViewById(R.id.serverPort);
        etUsername = view.findViewById(R.id.username);
        etPassword = view.findViewById(R.id.password);
        etFirstName = view.findViewById(R.id.firstName);
        etLastName = view.findViewById(R.id.lastName);
        etEmail = view.findViewById(R.id.email);
        registerBtn = view.findViewById(R.id.RegisterButton);
        signInBtn = view.findViewById(R.id.SignInButton);
        radiobtnMale = view.findViewById(R.id.gMale);
        radiogrpGender = view.findViewById(R.id.genderGroup);

        //Default disabled buttons
        signInBtn.setEnabled(false);
        registerBtn.setEnabled(false);
        //Enable Sign in button when required field is blank
        etServerHost.addTextChangedListener(textWatcher);
        etServerPort.addTextChangedListener(textWatcher);
        etUsername.addTextChangedListener(textWatcher);
        etPassword.addTextChangedListener(textWatcher);
        //Enable Register button when required field is blank
        etFirstName.addTextChangedListener(textWatcher);
        etLastName.addTextChangedListener(textWatcher);
        etEmail.addTextChangedListener(textWatcher);


        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{

                    android.os.Handler dataTaskHandler = new android.os.Handler(){
                        @Override
                        public void handleMessage(Message message){
                            Bundle bundle = message.getData();
                            String fullname = bundle.getString(USER_FULL_NAME);

                            Toast.makeText(getActivity(), getString(R.string.login_transfer_success, fullname), Toast.LENGTH_LONG).show();
                        }
                    };
                    android.os.Handler uiThreadMessageHandler = new android.os.Handler() {
                        @Override
                        public void handleMessage(Message message) {
                            Bundle bundle = message.getData();
                            if(DataCache.getInstance().isStatus() == true) {
                                String authToken = bundle.getString(AUTH_TOKEN);
                                DataCache.getInstance().setAuthToken(authToken);

                                GetDataTask getDataTask = new GetDataTask(dataTaskHandler);
                                ExecutorService executor = Executors.newSingleThreadExecutor();
                                executor.submit(getDataTask);
                            }
                            else{
                                Toast.makeText(getActivity(),R.string.login_error,Toast.LENGTH_LONG).show();
                            }
                        }
                    };

                    LoginTask loginTask = new LoginTask(uiThreadMessageHandler);

                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    executor.submit(loginTask);

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{

                    android.os.Handler dataTaskHandler = new android.os.Handler(){
                        @Override
                        public void handleMessage(Message message){
                            Bundle bundle = message.getData();
                            String fullname = bundle.getString(USER_FULL_NAME);
                            Toast.makeText(getActivity(),getString(R.string.register_transfer_success, fullname), Toast.LENGTH_LONG).show();
                        }
                    };
                    android.os.Handler uiThreadMessageHandler = new android.os.Handler() {
                        @Override
                        public void handleMessage(Message message) {
                            Bundle bundle = message.getData();
                            if(DataCache.getInstance().isStatus() == true) {
                                String authToken = bundle.getString(AUTH_TOKEN);
                                DataCache.getInstance().setAuthToken(authToken);

                                GetDataTask getDataTask = new GetDataTask(dataTaskHandler);
                                ExecutorService executor = Executors.newSingleThreadExecutor();
                                executor.submit(getDataTask);
                            }
                            else{
                                Toast.makeText(getActivity(),R.string.register_error,Toast.LENGTH_LONG).show();
                            }
                        }
                    };
                    RegisterTask registerTask = new RegisterTask(uiThreadMessageHandler);

                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    executor.submit(registerTask);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        //Radio buttons set up
        radiogrpGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.gMale:
                        gender = "m";
                        break;
                    case R.id.gFemale:
                        gender = "f";
                        break;
                }
            }
        });
        radiobtnMale.setChecked(true);

        return view;
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String serverHost = etServerHost.getText().toString();
            String serverPort = etServerPort.getText().toString();
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            String firstName = etFirstName.getText().toString();
            String lastName = etLastName.getText().toString();
            String email = etEmail.getText().toString();

            signInBtn.setEnabled(!(serverHost.isEmpty()) && !(serverPort.isEmpty()) && !(username.isEmpty())
                    && !(password.isEmpty()));

            registerBtn.setEnabled(!serverHost.isEmpty() && !serverPort.isEmpty() && !username.isEmpty()
                    && !password.isEmpty() && !firstName.isEmpty() && !lastName.isEmpty() && !email.isEmpty());
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    class RegisterTask implements Runnable {
        private final android.os.Handler messageHandler;

        RegisterTask(Handler handler) {
            this.messageHandler = handler;
        }

        @Override
        public void run() {
            ServerProxy proxy = new ServerProxy();
            RegisterRequest request = new RegisterRequest();
            request.setUserName(etUsername.getText().toString());
            request.setPassword(etPassword.getText().toString());
            request.setEmail(etEmail.getText().toString());
            request.setFirstName(etFirstName.getText().toString());
            request.setLastName(etLastName.getText().toString());
            request.setGender(gender);

            RegisterResponse registerResponse = proxy.register(request);
            String authToken = registerResponse.getAuthToken();
            boolean status = registerResponse.isSuccess();
            DataCache.getInstance().setStatus(status);

            sendMessage(authToken);

        }

        private void sendMessage(String authToken){
            Message message = Message.obtain();
            Bundle messageBundle = new Bundle();
            messageBundle.putString(AUTH_TOKEN, authToken);
            message.setData((messageBundle));
            messageHandler.sendMessage(message);
        }
    }

    class LoginTask implements Runnable{
        private final android.os.Handler messageHandler;

        LoginTask(Handler handler){
            this.messageHandler = handler;
        }

        @Override
        public void run() {
            ServerProxy proxy = new ServerProxy();
            LoginRequest request = new LoginRequest();
            request.setUserName(etUsername.getText().toString());
            request.setPassword(etPassword.getText().toString());

            LoginResponse loginResponse = proxy.login(request);
            String authToken = loginResponse.getAuthtoken();
            boolean status = loginResponse.isSuccess();
            DataCache.getInstance().setStatus(status);

            sendMessage(authToken);

        }

        private void sendMessage(String authToken){
            Message message = Message.obtain();
            Bundle messageBundle = new Bundle();
            messageBundle.putString(AUTH_TOKEN, authToken);
            message.setData((messageBundle));
            messageHandler.sendMessage(message);
        }

    }

    class GetDataTask implements Runnable{

        private final android.os.Handler messageHandler;

        GetDataTask( Handler handler){
            this.messageHandler = handler;
        }
        @Override
        public void run() {
            ServerProxy proxy = new ServerProxy();
            String authToken = DataCache.getInstance().getAuthToken();
            DataCache.getInstance().setPeople(proxy.getPeople(authToken).getData());
            DataCache.getInstance().setEvents(proxy.getEvents(authToken).getEvents());

            Person[] persons = DataCache.getInstance().getPeople();

            Person user = persons[0];
            sendMessage(user.getFirstName() + " " + user.getLastName());
        }

        private void sendMessage(String fullName){
            Message message = Message.obtain();
            Bundle messageBundle = new Bundle();
            messageBundle.putString(USER_FULL_NAME,fullName);
            message.setData((messageBundle));
            messageHandler.sendMessage(message);
        }
    }

}

