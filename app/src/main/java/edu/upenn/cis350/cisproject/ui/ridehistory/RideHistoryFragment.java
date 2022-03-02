package edu.upenn.cis350.cisproject.ui.ridehistory;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

import edu.upenn.cis350.cisproject.R;
import edu.upenn.cis350.cisproject.ReportActivity;
import edu.upenn.cis350.cisproject.data.Request;

public class RideHistoryFragment extends Fragment {

    private RideHistoryViewModel rideHistoryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        rideHistoryViewModel =
//                ViewModelProviders.of(this).get(RideHistoryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_ridehistory, container, false);

        ListView listView = root.findViewById(R.id.list);
        ArrayList<Request> rideList = new ArrayList<>();
        Request ride1 = new Request("Erdman", "Mermont Plaze", new Date(), "Sia", "12345", 3);
        Request ride2 = new Request("Park", "New Dorm", new Date(), "John", "12345", 3);
        Request ride3 = new Request("Taylor", "Canaday", new Date(), "Michael", "12345", 1);
        rideList.add(ride1);
        rideList.add(ride2);
        rideList.add(ride3);
        rideList.add(ride1);
        rideList.add(ride2);
        rideList.add(ride3);
        rideList.add(ride1);
        rideList.add(ride2);
        rideList.add(ride3);
        rideList.add(ride1);
        rideList.add(ride2);
        rideList.add(ride3);
        rideList.add(ride1);
        rideList.add(ride2);
        rideList.add(ride3);

        //center layout param: customize textview in listView
        RideListAdapter adapter = new RideListAdapter(getContext(), R.layout.list_unit_view, rideList);
        listView.setAdapter(adapter);

        //set onClick activity on item of listView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Request ride = (Request) parent.getAdapter().getItem(position);
                Intent intent = new Intent(getContext(), ReportActivity.class);
                intent.putExtra("ride", ride);
                startActivity(intent);
            }
        });
        return root;
    }
}