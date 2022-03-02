package edu.upenn.cis350.cisproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AccessImageTask extends AsyncTask<URL, Bitmap, Bitmap> {
    /*
This method is called in background when this object's "execute"
method is invoked.
The arguments passed to "execute" are passed to this method.
 */
    protected Bitmap doInBackground(URL... urls){
        Bitmap myBitmap = null;
        try {
            // get the first URL from the array
            URL url = urls[0];

            // create connection and send HTTP request
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            // read data from connection
            InputStream input = conn.getInputStream();

            // convert data into image
            myBitmap = BitmapFactory.decodeStream(input);

            // disconnect
            conn.disconnect();
        } catch(Exception e){
            e.printStackTrace();
        }
        // this will be passed to onPostExecute method
        return myBitmap;
    }

    /*
    This method is called in foreground after doInBackground finishes.
    It can access and update Views in user interface.
     */
    protected void onPostExecute(String msg) {
        // not implemented but you can use this if you’d like
    }
}
