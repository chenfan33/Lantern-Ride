package edu.upenn.cis350.cisproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import edu.upenn.cis350.cisproject.data.User;

public class SignupActivity extends Activity  {

    private EditText email;
    private EditText password;
    private EditText phoneNumber;
    User person;
    Button post;
    private boolean successCreated = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        this.post = (Button) super.findViewById(R.id.signup);

        this.email = (EditText) super.findViewById(R.id.signup_email);
        this.password = (EditText) super.findViewById(R.id.singup_password);
        this.phoneNumber = (EditText) super.findViewById(R.id.phone);
        this.post = (Button) super.findViewById(R.id.signup_button);

        // check if you are connected or not
        if(isConnected()){
            Toast.makeText(getBaseContext(), "Connected!", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getBaseContext(), "Fail", Toast.LENGTH_LONG).show();
        }

        // add click listener to Button "POST"
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validate()){
                    Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_LONG).show();
                    try {
                        String urlName = "http://10.0.2.2:3000/set?type=user&id=" + email.getText().toString() +
                                "&password=" + password.getText().toString() +
                                "&phoneNum=" + phoneNumber.getText().toString();

                        URL url = new URL(urlName);
                        AccessWebTask task = new AccessWebTask();
                        Toast.makeText(getApplicationContext(), urlName, Toast.LENGTH_LONG).show();

                        task.execute(url);
                        String name = task.get();
                        Toast.makeText(getApplicationContext(), name, Toast.LENGTH_LONG).show();
                    }
                    catch (Exception e) { Toast.makeText(getApplicationContext(), "Exception", Toast.LENGTH_LONG).show(); }
                }
            }

        });

    }

    public void logIn(View view) {
        Intent i = new Intent(SignupActivity.this, LoginActivity.class);
        i.putExtra("identity","passenger");
        startActivity(i);
    }



    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }




    private boolean validate() {
        try {
            String urlName = "http://10.0.2.2:3000/get?type=user&id=" + email.getText().toString();

            URL url = new URL(urlName);
            AccessWebTask task = new AccessWebTask();
            Toast.makeText(getApplicationContext(), urlName, Toast.LENGTH_LONG).show();

            task.execute(url);
            String name = task.get();

            if(name.contains("Default")) return true;
            else if(!name.contains("id")){
                Toast.makeText(getApplicationContext(), "Please enter Bryn mawr email!", Toast.LENGTH_LONG).show();
                return false;
            }
            Toast.makeText(getApplicationContext(), name, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Exception", Toast.LENGTH_LONG).show();
        }
        Toast.makeText(getApplicationContext(), "Email exists,please login!", Toast.LENGTH_LONG).show();

        return false;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }
}
