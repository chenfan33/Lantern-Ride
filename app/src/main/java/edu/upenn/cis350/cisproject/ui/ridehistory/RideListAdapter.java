package edu.upenn.cis350.cisproject.ui.ridehistory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.baidu.mapapi.map.MapViewLayoutParams;

import java.util.ArrayList;
import java.util.Date;

import edu.upenn.cis350.cisproject.R;
import edu.upenn.cis350.cisproject.data.Request;

// ListView uses Adapter to fill its rows with Views
// by calling getView() on each row
// so need to override getView() to customize list view
public class RideListAdapter extends ArrayAdapter<Request> {
    private static final String TAG = "edu.upenn.cis350.cisproject.ui.ridehistory.RideListAdapter";
    private Context context;
    private int resource;

    // deals with large data loading
    static class ViewHolder {
        TextView tvDate;
        TextView tvDriverName;
        TextView tvLocs;
    }

    public RideListAdapter(Context context, int resource, ArrayList<Request> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    // convertView: Adapter uses it to recycle old View objects. No need to recreate.
    // Version2: added view holder and if-else condition to make scroll smooth
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the ride info
        Request ride = getItem(position);
        Date date = ride.getDate();
        String driverName = ride.getDriverName();
        String pickUpLoc = ride.getPickUpLoc();
        String dropOffLoc = ride.getDropOffLoc();

        ViewHolder holder;
        if (convertView == null) {
            //inflate/populate each row
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource, parent, false);

            //view holder to solve potential problem when large amount of data needed to be display
            // keep main thread from heavy processing
            holder = new ViewHolder();
            holder.tvDate = (TextView) convertView.findViewById(R.id.date);
            holder.tvDriverName = (TextView) convertView.findViewById(R.id.driverName);
            holder.tvLocs = (TextView) convertView.findViewById(R.id.locs);
            convertView.setTag(holder); //store View in memory
        } else {
            holder = (ViewHolder) convertView.getTag(); //View referenced from memory
        }

        //set text
        holder.tvDate.setText(date.toString());
        holder.tvDriverName.setText(driverName);
        String str = pickUpLoc + " -> " + dropOffLoc;
        holder.tvLocs.setText(str);

        return convertView;
    }
}




//    @Override
//    public View getView(int position, View convertView, ViewGroup parent){
//        //inflate/populate each row
//        LayoutInflater inflater = LayoutInflater.from(context);
//        convertView = inflater.inflate(resource, parent, false); //convertView: Adapter uses it to recycle old View objects. No need to recreate.
//
//        //get the ride info
//        Request ride = getItem(position);
//        Date date = ride.getDate();
//        String driverName = ride.getDriverName();
//        String pickUpLoc = ride.getPickUpLoc();
//        String dropOffLoc = ride.getDropOffLoc();
//        //set text
//        TextView tvDate = (TextView) convertView.findViewById(R.id.date);
//        TextView tvDriverName = (TextView) convertView.findViewById(R.id.driverName);
//        TextView tvLocs = (TextView) convertView.findViewById(R.id.locs);
//        tvDate.setText(date.toString());
//        tvDriverName.setText(driverName);
//        tvLocs.setText(pickUpLoc + "->" + dropOffLoc);
//
//        return convertView;
//    }

