package edu.upenn.cis350.cisproject;
import edu.upenn.cis350.cisproject.data.User;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import androidx.appcompat.app.AppCompatActivity;


import java.io.IOException;
import java.net.URL;

import edu.upenn.cis350.cisproject.dataSource.DataSource;


public class LoginActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    private CheckBox choose1;
    private CheckBox choose2;
    private EditText email;
    private EditText password = null;
    DataSource user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.choose1 = (CheckBox) super.findViewById(R.id.choosePassenger);
        this.choose2 = (CheckBox) super.findViewById(R.id.chooseDriver);
        choose1.setOnCheckedChangeListener(this);
        choose2.setOnCheckedChangeListener(this);
        this.email = (EditText) super.findViewById(R.id.login_email);
        this.password = (EditText) super.findViewById(R.id.login_password);
    }

    public void signUp(View view) {
        Intent i = new Intent(LoginActivity.this, SignupActivity.class);
        i.putExtra("identity","passenger");
        startActivity(i);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked) {
            if (choose1.getText().toString().equals(buttonView.getText().toString())) {
                choose1.setChecked(true);
                choose2.setChecked(false);
            } else {
                choose1.setChecked(false);
                choose2.setChecked(true);
            }

        }
    }

    public void enterMainPage(View view) throws IOException {
//        if (choose1.isChecked() & isValidUser()) {
            Intent i = new Intent(LoginActivity.this, PassengerActivity.class);
            String id = email.getText().toString();
            String pass = password.getText().toString();
            //Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_LONG).show();

            i.putExtra("id", id);
            i.putExtra("pass", pass);
            startActivity(i);
//        } else if(!choose1.isChecked()){
//            Toast.makeText(getApplicationContext(),"Check box", Toast.LENGTH_LONG).show();
//
//        }

    }

    public boolean isValidUser(){
        if(!email.getText().toString().equals("") && !password.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_LONG).show();
            try {
                String urlName = "http://10.0.2.2:3000/get?type=user&id=" + email.getText().toString();
                URL url = new URL(urlName);
                AccessWebTask task = new AccessWebTask();
                task.execute(url);
                String result = task.get();
                JSONParser parser = new JSONParser();
                JSONObject arrays = (JSONObject) parser.parse(result);

                    String pass = (String) arrays.get("password");

                    if (pass!= null && pass.equals(password.getText().toString()) && !pass.equals("Default")) {
                        return true;
                    } else if(pass == null){
                        Toast.makeText(getApplicationContext(), "No such user", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Wrong password", Toast.LENGTH_LONG).show();
                    }

            }
            catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Exception", Toast.LENGTH_LONG).show();
            }
        }
        return false;
    }
}



/**
 package edu.upenn.cis350.cisproject;

 import android.os.*;
 import android.util.Log;
 import android.view.View;
 import android.widget.CheckBox;
 import android.widget.CompoundButton;
 import android.widget.EditText;

 import org.apache.http.params.*;
 import androidx.appcompat.app.AppCompatActivity;
 import org.json.*;

 import java.io.BufferedInputStream;
 import java.io.DataOutputStream;
 import java.io.IOException;
 import java.io.InputStream;
 import java.net.HttpURLConnection;
 import java.net.URL;
 import java.net.URLEncoder;
 import java.util.Map;

 public class SignupActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
 private CheckBox choose1;
 private CheckBox choose2;
 private EditText email;
 private EditText password;
 private EditText phoneNumber;

 @Override
 protected void onCreate(Bundle savedInstanceState) {
 super.onCreate(savedInstanceState);
 setContentView(R.layout.activity_signup);
 this.choose1 = (CheckBox) super.findViewById(R.id.choosePassenger);
 this.choose2 = (CheckBox) super.findViewById(R.id.chooseDriver);
 choose1.setOnCheckedChangeListener(this);
 choose2.setOnCheckedChangeListener(this);
 this.email = (EditText) super.findViewById(R.id.signup_email);
 this.password = (EditText) super.findViewById(R.id.singup_password);
 this.phoneNumber = (EditText) super.findViewById(R.id.phone);
 }

 @Override
 public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
 if (isChecked) {
 if (choose1.getText().toString().equals(buttonView.getText().toString())) {
 choose1.setChecked(true);
 choose2.setChecked(false);

 } else {
 choose1.setChecked(false);
 choose2.setChecked(true);

 }

 }
 }

 public void sendInfo(View v) {
 //connect();
 }

 public void senddatatoserver(View v) {
 //function in the activity that corresponds to the layout button
 String emailAdress = email.getText().toString();
 String storedPassword = password.getText().toString();
 String phone = phoneNumber.getText().toString();
 JSONObject post_dict = new JSONObject();


 }


 }

 */