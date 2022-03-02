package edu.upenn.cis350.cisproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void signUp(View view) {
        Intent i = new Intent(MainActivity.this, SignupActivity.class);
        i.putExtra("identity","passenger");
        startActivity(i);
    }

    public void logIn(View view) {
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        i.putExtra("identity","passenger");
        startActivity(i);
    }
}
