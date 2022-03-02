package edu.upenn.cis350.cisproject;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//If you want to learn about SEND JSON REQUEST TO WEB SERVER FROM ANDROID:
//https://www.baeldung.com/httpurlconnection-post
public class UploadJsonTask extends AsyncTask<Void, Void, String> {
    String urlString;
    String jsonString;
    Context context;

    public UploadJsonTask(String jsonString, String urlString, Context context) {
        this.jsonString = jsonString;
        this.urlString = urlString;
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String msg = "";
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //Set up request and open connection
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.connect();

            //Transform jsonString to binary data, then write to the output stream
            byte[] input = jsonString.getBytes("utf-8");
            OutputStream os = conn.getOutputStream();
            os.write(input);
            os.flush();
            os.close();

            // Read response from input stream
            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(in, "utf-8"));
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = responseStreamReader.readLine()) != null) {
                response.append(responseLine.trim());
            }
            response.append("\n");
            responseStreamReader.close();
            System.out.println(response.toString());
            if(response.toString() == "200"){
                msg = "success";
            } else {
                msg = "failure_bad_response";
            }
            // Disconnection
            conn.disconnect();
        } catch(MalformedURLException e) {
            e.printStackTrace();
            msg = "failure_invalid_url";
            //Toast.makeText(getContext(), "Upload Image: failure", Toast.LENGTH_SHORT).show();
        } catch(IOException e) {
            e.printStackTrace();
            msg = "failure_IOE_exception";
            //Toast.makeText(getContext(), "Upload Image: failure", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String msg) {
        if (msg == "success"){
            Toast.makeText(this.context, "Content submitted!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this.context, "Submission failed!", Toast.LENGTH_SHORT).show();
        }
    }
}
