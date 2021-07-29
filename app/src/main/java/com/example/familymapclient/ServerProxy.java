package com.example.familymapclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import Requests.LoginRequest;
import Requests.RegisterRequest;
import Responses.EventsListResponse;
import Responses.LoginResponse;
import Responses.PersonListResponse;
import Responses.RegisterResponse;
import serializer.JsonSerializer;


public class ServerProxy extends RequestHandler {

    public RegisterResponse register(RegisterRequest registerRequests) {
        RegisterResponse response = new RegisterResponse();
        try {
            URL url = new URL("http://10.0.2.2:8080/user/register");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(5000);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.connect();

            try(OutputStream requestBody = connection.getOutputStream()) {
                String serialized = JsonSerializer.serialize(registerRequests);
                writeString(serialized, requestBody);
            }
            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String json = readString(connection.getInputStream());
                response = JsonSerializer.deserialize(json, RegisterResponse.class);
            }
            else{
                response.setSuccess(false);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    public LoginResponse login(LoginRequest loginRequest){
        LoginResponse response = new LoginResponse();
        try {
            URL url = new URL("http://10.0.2.2:8080/user/login");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setReadTimeout(5000);

            connection.connect();

            try(OutputStream requestBody = connection.getOutputStream()) {
                String serialized = JsonSerializer.serialize(loginRequest);
                writeString(serialized, requestBody);
            }

            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String json = readString(connection.getInputStream());
                response = JsonSerializer.deserialize(json, LoginResponse.class);
            }
            else{
                response.setSuccess(false);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }


    public PersonListResponse getPeople(String authToken){
        PersonListResponse personListResponse = new PersonListResponse();
        try{
            URL url = new URL("http://10.0.2.2:8080/person");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setDoOutput(false);


            connection.addRequestProperty("Authorization", authToken);
            connection.connect();

            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream is = connection.getInputStream();
                String json = readString(is);
                personListResponse = JsonSerializer.deserialize(json, PersonListResponse.class);
            }
            else{
                personListResponse.setSuccess(false);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return personListResponse;
    }

    public EventsListResponse getEvents(String authToken){
        EventsListResponse eventsListResponse = new EventsListResponse();
        try{
            URL url = new URL("http://10.0.2.2:8080/event");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            connection.addRequestProperty("Authorization", authToken);

            connection.connect();
            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = connection.getInputStream();
                String json = readString(is);
                eventsListResponse = JsonSerializer.deserialize(json, EventsListResponse.class);
            }
            else{
                eventsListResponse.setSuccess(false);
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return eventsListResponse;
    }
}

class RequestHandler {

    public String readString(InputStream is) throws IOException{
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    public void writeString(String s, OutputStream os) throws IOException {
        OutputStreamWriter osw = new OutputStreamWriter(os);
        osw.write(s);
        osw.flush();
    }
}

