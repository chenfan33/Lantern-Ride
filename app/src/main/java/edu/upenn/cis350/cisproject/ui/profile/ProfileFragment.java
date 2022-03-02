package edu.upenn.cis350.cisproject.ui.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.URL;

import edu.upenn.cis350.cisproject.AccessWebTask;
import edu.upenn.cis350.cisproject.PassengerActivity;
import edu.upenn.cis350.cisproject.R;
import edu.upenn.cis350.cisproject.UploadImageTask;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment implements View.OnClickListener{
    public static String id;
    String password;
    private TextView phoneNum;
    private TextView pass;

    private static final int RESULT_LOAD_IMAGE = 1;
    private CircularImageView imageToUpLoad;
    private ImageView navImg;
    private TextView sUpLoadImage;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        PassengerActivity activity = (PassengerActivity) getActivity();
        id = activity.id;
        password = activity.pass;

        TextView email = root.findViewById(R.id.signup_email);
        email.setText(id);

        this.pass = root.findViewById(R.id.singup_password);
        pass.setText(getPassword());

        this.phoneNum = root.findViewById(R.id.phone);
        phoneNum.setText(getPhone());

        TextView edit = root.findViewById(R.id.set_phone);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPhoneNum();
            }

        });

        TextView set_pass = root.findViewById(R.id.set_pass);
        set_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPassword();
            }

        });

        imageToUpLoad = (CircularImageView)root.findViewById(R.id.profilePic);
        sUpLoadImage = (TextView) root.findViewById(R.id.sUploadImage);
        imageToUpLoad.setOnClickListener(this);
        sUpLoadImage.setOnClickListener(this);

        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        View headView = navigationView.getHeaderView(0);
        navImg = headView.findViewById(R.id.imageView);
        imageToUpLoad.setImageBitmap(((BitmapDrawable)navImg.getDrawable()).getBitmap()); //set profile image to be the image in nav header

        return root;
    }

    private String getPhone(){
        String phonee = "";
        try {
            String urlName = "http://10.0.2.2:3000/get?type=user&id=" + id;
            URL url = new URL(urlName);
            AccessWebTask task = new AccessWebTask();
            task.execute(url);
            String result = task.get();
            JSONParser parser = new JSONParser();
            JSONObject arrays = (JSONObject) parser.parse(result);
            phonee = (String) arrays.get("phoneNum");
        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Exception", Toast.LENGTH_LONG).show();
        }
        return phonee;
    }

    private void setPhoneNum() {
        final EditText editText = new EditText(getActivity());
        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(getActivity());
        inputDialog.setTitle("Please type in new phone number").setView(editText);
        inputDialog.setPositiveButton("confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(),
                                editText.getText().toString(),
                                Toast.LENGTH_SHORT).show();
                        String newnumber = editText.getText().toString();
                        changePhone(newnumber);
                        phoneNum.setText(newnumber);

                    }
                }).show();

    }
    public void changePhone(String newPhone){
        try {
            String urlName = "http://10.0.2.2:3000/set?type=user&id=" + id +
                    "&id=" + id +
                    "&password=" + getPassword() +
                    "&phoneNum=" + newPhone;
            URL url = new URL(urlName);
            AccessWebTask task = new AccessWebTask();
            task.execute(url);
            String name = task.get();
            Toast.makeText(getContext(), name, Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(getContext(), "Exception", Toast.LENGTH_LONG).show();
        }
    }

    private void setPassword() {
        /*@setView 装入一个EditView
         */
        final EditText editText = new EditText(getActivity());
        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(getActivity());
        inputDialog.setTitle("Please type in new password").setView(editText);
        inputDialog.setPositiveButton("confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(),
                                editText.getText().toString(),
                                Toast.LENGTH_SHORT).show();
                        String newnumber = editText.getText().toString();
                        changPassword(newnumber);
                        pass.setText(newnumber);

                    }
                }).show();

    }
    public void changPassword(String pp){
        try {
            String urlName = "http://10.0.2.2:3000/set?type=user&id=" + id +
                    "&id=" + id +
                    "&password=" + pp +
                    "&phoneNum=" + getPhone();
            URL url = new URL(urlName);
            AccessWebTask task = new AccessWebTask();
            task.execute(url);
            String name = task.get();
            Toast.makeText(getContext(), name, Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(getContext(), "Exception", Toast.LENGTH_LONG).show();
        }
    }

    private String getPassword(){
        String pass = "";
        try {
            String urlName = "http://10.0.2.2:3000/get?type=user&id=" + id;
            URL url = new URL(urlName);
            AccessWebTask task = new AccessWebTask();
            task.execute(url);
            String result = task.get();
            JSONParser parser = new JSONParser();
            JSONObject arrays = (JSONObject) parser.parse(result);
            pass = (String) arrays.get("password");
        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Exception", Toast.LENGTH_LONG).show();
        }
        return pass;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.profilePic:
                //initiate selection activity
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                break;
            case R.id.sUploadImage:
                Bitmap image = ((BitmapDrawable)imageToUpLoad.getDrawable()).getBitmap();
                UploadImageTask task  = new UploadImageTask(image, getContext());
                task.execute();
                String result = "";
                try {
                    result = task.get();
                } catch(Exception e) {
                    System.out.println(e);
                }
                if(result == "Image Uploaded"){
                    navImg.setImageBitmap(image);
                }
                break;
        }
    }

    //called after user select pic from gallery
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK  && data != null){
            //uniform resource indicate: address of the image in phone that is selected
            Uri selectedImage = data.getData();
            imageToUpLoad.setImageURI(selectedImage);
        }
    }
}
