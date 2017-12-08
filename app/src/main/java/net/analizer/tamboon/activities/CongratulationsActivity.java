package net.analizer.tamboon.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.analizer.tamboon.R;

public class CongratulationsActivity extends AppCompatActivity {

    public static void showActivity(AppCompatActivity appCompatActivity) {
        Intent intent = new Intent(appCompatActivity, CongratulationsActivity.class);
        appCompatActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congratulations);
    }
}
