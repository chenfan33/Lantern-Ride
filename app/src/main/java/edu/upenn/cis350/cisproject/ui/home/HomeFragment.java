package edu.upenn.cis350.cisproject.ui.home;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MapView;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationData;

import com.baidu.mapapi.model.LatLng;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import edu.upenn.cis350.cisproject.AccessWebTask;
import edu.upenn.cis350.cisproject.MapActivity;
import edu.upenn.cis350.cisproject.PassengerActivity;
import edu.upenn.cis350.cisproject.R;



public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private edu.upenn.cis350.cis350project.ui.home.HomeViewModel homeViewModel;
    public String id;
    private String pickupLocation;
    private String dropoffLocation;
    private boolean requestMade = false;

    /******************************************************************/
    private static final String LTAG = MapActivity.class.getSimpleName();
    private MapView mMapView;
    private BaiduMap mBaiduMap;


    private boolean isFirstLoc = true;
    public LocationClient mLocationClient;
    private BDLocation mlocation=null;
    private TextView setTime;
    private Spinner pickup;
    private Spinner dropoff;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在fragment用getContext().getApplicationContext()获取整个应用的上下文
        mLocationClient = new LocationClient(Objects.requireNonNull(getContext()).getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(new HomeFragment.MyLocationListener());
        //注册监听函数
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(edu.upenn.cis350.cis350project.ui.home.HomeViewModel.class);

        SDKInitializer.initialize(getActivity().getApplicationContext());

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        initialize(root);
        setTime = (TextView) root.findViewById(R.id.totalTime);
        pickup = root.findViewById(R.id.pickup);
        dropoff = root.findViewById(R.id.dropoff);
                /******************************************************************/
        mMapView = root.findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();

        LatLng cenpt = new LatLng(40.028197,-75.313814);
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(100)
                .build();

        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mMapView = new MapView(getActivity(), new BaiduMapOptions());
        mMapView.getMap().setMapStatus(mMapStatusUpdate);
        mMapView.removeViewAt(1);//删除百度地图LoGo


        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }//获取手机状态
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }//获取位置信息
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }//获取位置信息
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }//读写SD卡
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }//读写SD卡
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);

            requestPermissions(permissions, 1);
        } else {
            requestLocation();
        }

        //getActivity().setContentView(R.layout.fragment_home);
        /******************************************************************/
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(getActivity(), "发生权限问题", Toast.LENGTH_SHORT).show();
                            //                            getActivity().finish();
                            //                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(getActivity(), "发生未知错误，换个新手机试试？", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
                break;
            default:
        }
    }
    private void requestLocation() {

        initLocation();
        //mLocationClient为第二步初始化过的LocationClient对象
        //调用LocationClient的start()方法，便可发起定位请求
    }

    private void navigateTo(BDLocation location){
        if(location==null){
            return;
        } else if(isFirstLoc) {
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            mBaiduMap.animateMapStatus(update);
            //zoom设置缩放等级，值越大，地点越详细
            MapStatus mMapSta0tus = new MapStatus.Builder()
                    .target(ll)
                    .zoom(19)
                    .build();
            //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapSta0tus);
            //改变地图状态
            mBaiduMap.animateMapStatus(mMapStatusUpdate);
            isFirstLoc = false;
        }
        MyLocationData locationData=new MyLocationData.Builder().accuracy(20)//locData.accuracy = location.getRadius();//获取默认误差半径
                //accuracy设置精度圈大小
                //设置开发者获取到的方向信息，顺时针旋转0-360度
                .direction(100).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
        mBaiduMap.setMyLocationData(locationData);
    }


    //实现BDAbstractLocationListener接口
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
            double latitude = location.getLatitude();    //获取纬度信息
            double longitude = location.getLongitude();    //获取经度信息
            float radius = location.getRadius();    //获取定位精度，默认值为0.0f
            String coorType = location.getCoorType();
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
            int errorCode = location.getLocType();
            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
            if (errorCode== BDLocation.TypeGpsLocation
                    || errorCode == BDLocation.TypeNetWorkLocation) {
                navigateTo(location);
                mlocation=location;
            }
        }
    }

    //配置定位SDK参数
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        MyLocationListener myLocationListener = new MyLocationListener();
        //注册监听函数
        mLocationClient.registerLocationListener(myLocationListener);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，设置定位模式，默认高精度
        //LocationMode.Hight_Accuracy：高精度；
        //LocationMode. Battery_Saving：低功耗；
        //LocationMode. Device_Sensors：仅使用设备；

        option.setCoorType("bd09ll");
        //可选，设置返回经纬度坐标类型，默认GCJ02
        //GCJ02：国测局坐标；
        //BD09ll：百度经纬度坐标；
        //BD09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标

        int span=1000;
        option.setScanSpan(span);
        //可选，设置发起定位请求的间隔，int类型，单位ms
        //如果设置为0，则代表单次定位，即仅定位一次，默认为0
        //如果设置非0，需设置1000ms以上才有效

        option.setOpenGps(true);
        //可选，设置是否使用gps，默认false
        //使用高精度和仅用设备两种定位模式的，参数必须设置为true

        option.setLocationNotify(true);
        //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        option.setIgnoreKillProcess(false);
        //可选，定位SDK内部是一个service，并放到了独立进程。
        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(false);
        //可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setWifiCacheTimeOut(5*60*1000);
        //可选，V7.2版本新增能力
        //如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位

        option.setEnableSimulateGps(false);
        //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false
        option.setIsNeedAltitude(false);
        //设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者，该模式下开发者无需再关心定位间隔是多少，定位SDK本身发现位置变化就会及时回调给开发者
        option.setOpenAutoNotifyMode();
        //设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者

        option.setOpenAutoNotifyMode(3000,1, LocationClientOption.LOC_SENSITIVITY_HIGHT);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mLocationClient.setLocOption(option);
        //mLocationClient为第二步初始化过的LocationClient对象
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        //开始定位
        mLocationClient.start();
        //        option.setIsNeedAddress(true);
        //        option.setIsNeedLocationPoiList(true);
    }

    @Override
    public void setMenuVisibility(boolean menuVisibile) {
        super.setMenuVisibility(menuVisibile);
        if (this.getView() != null) {
            this.getView().setVisibility(menuVisibile ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // activity 暂停时同时暂停地图控件
        mMapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            mBaiduMap.setMyLocationEnabled(false);
			mMapView.onDestroy();
        } catch (Exception e) {

        }

    }
    /******************************************************************/











    public void initialize(View root){
        PassengerActivity activity = (PassengerActivity) getActivity();
        id = activity.id;

        Spinner pickup = root.findViewById(R.id.pickup);
        Spinner dropoff = root.findViewById(R.id.dropoff);

        ArrayAdapter<CharSequence> locationAdapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.locations, android.R.layout.simple_spinner_item);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        pickup.setAdapter(locationAdapter);
        pickup.setOnItemSelectedListener(this);

        dropoff.setAdapter(locationAdapter);
        dropoff.setOnItemSelectedListener(this);

        Button request = root.findViewById(R.id.request);
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRequestButtonClick(v);
            }
        });
        Button cancel = root.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelButtonClick(v);
            }
        });
    }

    public void onRequestButtonClick(View v){
        if(requestMade){
            Toast.makeText(getActivity(), "You have an ongoing ride", Toast.LENGTH_LONG).show();
        }
        else {
            if (!pickupLocation.equals("") && !dropoffLocation.equals("") && !pickupLocation.equals(dropoffLocation)) {
                Toast.makeText(getActivity(), "OK", Toast.LENGTH_LONG).show();
                try {
                    String urlName = "http://10.0.2.2:3000/set?id=" + id +
                            "&pickup=" + pickupLocation +
                            "&dropoff=" + dropoffLocation;
                    URL url = new URL(urlName);
                    AccessWebTask task = new AccessWebTask();
                    task.execute(url);
                    String name = task.get();
                    Toast.makeText(getContext(), "Request sent", Toast.LENGTH_LONG).show();
                    requestMade = true;
                    getWaitingInfo(v);
                } catch (Exception e) {
                    e.printStackTrace();

                    Toast.makeText(getContext(), "Exception", Toast.LENGTH_LONG).show();
                }
            } else Toast.makeText(getContext(), "Please enter pickup/dropoff location", Toast.LENGTH_LONG).show();
        }

    }

    public void getWaitingInfo(View v) throws MalformedURLException, ExecutionException, InterruptedException {
        String urlName = "http://10.0.2.2:3000/getNumber";
        URL url = new URL(urlName);
        AccessWebTask task = new AccessWebTask();
        task.execute(url);
        String number = task.get();
        Toast.makeText(getContext(), "There are " + number+" rides before you.", Toast.LENGTH_LONG).show();

        String total = "http://10.0.2.2:3000/totalTime";
        URL time = new URL(total);
        AccessWebTask task2 = new AccessWebTask();
        task2.execute(time);
        String total_wait_time = task2.get();
        setTime.setText("Estimate arriving time: "+total_wait_time);
    }

    public void onCancelButtonClick(View v){
        if(requestMade) {
            try {
                String urlName = "http://10.0.2.2:3000/remove?id=" + id;
                URL url = new URL(urlName);
                AccessWebTask task = new AccessWebTask();
                task.execute(url);
                String name = task.get();
                Toast.makeText(getContext(), "Delete Request", Toast.LENGTH_LONG).show();
                requestMade = false;
                pickupLocation = "";
                dropoffLocation = "";
                pickup.setSelection(0);
                dropoff.setSelection(0);
                setTime.setText("");

            }
            catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Exception", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spin = (Spinner)parent;
        if(spin.getId() == R.id.pickup)
            pickupLocation = parent.getItemAtPosition(position).toString();
        else if(spin.getId() == R.id.dropoff)
            dropoffLocation = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }

}