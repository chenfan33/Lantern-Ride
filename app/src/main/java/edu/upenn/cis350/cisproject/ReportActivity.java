package edu.upenn.cis350.cisproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import edu.upenn.cis350.cisproject.data.Request;
import edu.upenn.cis350.cisproject.ui.profile.ProfileFragment;

public class ReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
    }

    public void onSendClick(View view){
        //send info to web side
        String id = ProfileFragment.id;
        Intent i = getIntent();
        // Note: !! Ride here should be retrieved from the db
        //Just for demonstration.
        Request rideObj = (Request)i.getSerializableExtra("ride");
        JSONObject rideJsn = new JSONObject();
        try {
            rideJsn.put("driver_name", rideObj.getDriverName());
            rideJsn.put("driver_id", rideObj.getDriverID());
            rideJsn.put("date", rideObj.getDate().toString());
            rideJsn.put("pickup", rideObj.getPickUpLoc());
            rideJsn.put("droppff", rideObj.getDropOffLoc());
            rideJsn.put("num_of_passengers", rideObj.getNumOfPassengers());
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e);
        }
        String title = (((EditText)findViewById(R.id.title)).getText()).toString();
        String content = (((EditText)findViewById(R.id.content)).getText()).toString();

        JSONObject report =  new JSONObject();
        try {
            report.put("reporter", id);
            report.put("report_as", "passenger");
            report.put("title", title);
            report.put("content", content);
            report.put("ride", rideJsn);
        } catch (Exception e){
            e.printStackTrace();
            return;
        }
        String jsonString = report.toString();
        Log.v("DD", jsonString);
        String urlString = "http://10.0.2.2:3000/uploadReport";
        new UploadJsonTask(jsonString, urlString, this).execute();
        finish();
    }

}
