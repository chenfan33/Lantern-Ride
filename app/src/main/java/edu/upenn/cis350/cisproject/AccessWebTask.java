package edu.upenn.cis350.cisproject;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Scanner;

public class AccessWebTask extends AsyncTask<URL, String, String> {
    /*This method is called in background when this object's "execute" method is invoked.
    The arguments passed to "execute" are passed to this method.*/


    protected String doInBackground(URL... urls) {
        try {
            // get the first URL from the array
            URL url = urls[0];
            // create connection and send HTTP request
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            // read first line of data that is returned
            Scanner in = new Scanner(url.openStream());
            String msg = in.nextLine();
            System.out.println("                         "+msg);
            // use Android JSON library to parse JSON
            if (!isNumeric(msg)) {


             JSONObject jo = new JSONObject(msg);
             // assumes that JSON object contains a "name" field
             String result = jo.toString();

             // this will be passed to onPostExecute method
             conn.disconnect();
             return result;
            }
            return msg;
        }catch (Exception e) {
            return e.toString();

        }

    }
 public static boolean isNumeric(String str) {
  NumberFormat formatter = NumberFormat.getInstance();
  ParsePosition pos = new ParsePosition(0);
  formatter.parse(str, pos);
  return str.length() == pos.getIndex();
 }

        /*This method is called in foreground after doInBackground finishes.
          It can access and update Views in user interface.*/
        protected void onPostExecute(String msg) {
        }
}






/**package edu.upenn.cis350.cisproject;

 import android.app.Activity;
 import android.content.Intent;
 import android.os.AsyncTask;
 import android.os.Bundle;
 import android.util.Log;
 import android.view.View;
 import android.view.View.OnClickListener;
 import android.widget.Button;
 import android.widget.EditText;

 import org.json.JSONException;
 import org.json.JSONObject;

 import java.io.BufferedReader;
 import java.io.BufferedWriter;
 import java.io.IOException;
 import java.io.InputStream;
 import java.io.InputStreamReader;
 import java.io.OutputStreamWriter;
 import java.io.Writer;
 import java.net.HttpURLConnection;
 import java.net.URL;

 import edu.upenn.cis350.cisproject.data.User;

 import static androidx.constraintlayout.widget.Constraints.TAG;

 public class SignupActivity extends Activity implements OnClickListener {

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


 }

 public void logIn(View view) {
 Intent i = new Intent(SignupActivity.this, LoginActivity.class);
 i.putExtra("identity", "passenger");
 startActivity(i);
 }




 public void senddatatoserver(View v) {
 //function in the activity that corresponds to the layout button
 String emailid = email.getText().toString();
 String passwordNum = password.getText().toString();
 String phone = phoneNumber.getText().toString();
 JSONObject post_dict = new JSONObject();

 try {
 post_dict.put("email", emailid);
 post_dict.put("password", passwordNum);
 post_dict.put("phone", phone);

 } catch (JSONException e) {
 e.printStackTrace();
 }
 if (post_dict.length() > 0) {
 new SendJsonDataToServer().execute(String.valueOf(post_dict));
 }
 //add background inline class here


 }


 @Override
 public void onPointerCaptureChanged(boolean hasCapture) {

 }

 @Override
 public void onClick(View v) {

 }
 }


 class SendJsonDataToServer extends AsyncTask<String, String, String> {

 @Override
 protected String doInBackground(String... params) {
 String JsonResponse = null;
 String JsonDATA = params[0];
 HttpURLConnection urlConnection = null;
 BufferedReader reader = null;
 try {
 URL url = new URL("https://reqres.in/api/users/pages");
 urlConnection = (HttpURLConnection) url.openConnection();
 urlConnection.setDoOutput(true);
 // is output buffer writter
 urlConnection.setRequestMethod("POST");
 urlConnection.setRequestProperty("Content-Type", "application/json");
 urlConnection.setRequestProperty("Accept", "application/json");
 //set headers and method
 Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
 writer.write(JsonDATA);
 // json data
 writer.close();
 InputStream inputStream = urlConnection.getInputStream();
 //input stream
 StringBuffer buffer = new StringBuffer();
 if (inputStream == null) {
 // Nothing to do.
 return null;
 }
 reader = new BufferedReader(new InputStreamReader(inputStream));

 String inputLine;
 while ((inputLine = reader.readLine()) != null)
 buffer.append(inputLine + "\n");
 if (buffer.length() == 0) {
 // Stream was empty. No point in parsing.
 return null;
 }
 JsonResponse = buffer.toString();
 //response data
 Log.i(TAG, JsonResponse);
 //send to post execute
 return JsonResponse;


 } catch (IOException e) {
 e.printStackTrace();
 } finally {
 if (urlConnection != null) {
 urlConnection.disconnect();
 }
 if (reader != null) {
 try {
 reader.close();
 } catch (final IOException e) {
 Log.e(TAG, "Error closing stream", e);
 }
 }
 }
 return null;
 }

 @Override
 protected void onPostExecute(String s) {
 }
 }

 */