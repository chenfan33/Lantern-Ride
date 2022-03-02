package edu.upenn.cis350.cisproject;

import android.content.Context;
import android.graphics.Bitmap;
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

/**
 *  Upload selected image to web server
 *  image is stored in disk on the server machine, directory: /public/image.png
 *  subsequent uploading will override previous image.png,
 *  since writing method takes the same name param in the server side
 */

public class UploadImageTask extends AsyncTask<Void, Void, String> {
    String stringUrl = "http://10.0.2.2:3000/uploadProfileImage";
    Bitmap image;
    Context context;

    /*
     * image: get from photo gallery of the mobile phone
     * context: where to show the message of "Image Uploaded"
     */
    public UploadImageTask(Bitmap image, Context context){
        this.image = image;
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String resultMsg = "";
        try {
            URL url = new URL(stringUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            //conn.setDoInput(true);

            //set up request
            conn.setRequestMethod("POST");
            //conn.setRequestProperty("Connection", "Kepp-Alive");
            //conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("Content-Type", "multipart/form-data");

            //use compress method to write binary image data to the output stream
            OutputStream os = conn.getOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();


            //Read response from input stream
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

//                InputStream in = new BufferedInputStream(conn.getInputStream());
//                BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(in));
//                String line = "";
//                StringBuilder stringBuilder = new StringBuilder();
//                while (true) {
//                    try {
//                        if (!((line = responseStreamReader.readLine()) != null)) break;
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                stringBuilder.append(line).append("\n");
//                responseStreamReader.close();
//                String response = stringBuilder.toString();
//                System.out.println(response);

            // Disconnection
            conn.disconnect();
        } catch(MalformedURLException e) {
            e.printStackTrace();
            resultMsg = "Failed to upload image, check wifi connection";
        } catch(IOException e) {
            e.printStackTrace();
            resultMsg = "Failed to upload image, check wifi connection";
        }
        resultMsg = "Image Uploaded";
        return resultMsg;
    }

    @Override
    protected void onPostExecute(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
