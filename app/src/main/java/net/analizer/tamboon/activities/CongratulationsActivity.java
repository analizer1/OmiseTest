package net.analizer.tamboon.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.analizer.tamboon.R;
import net.analizer.tamboon.databinding.ActivityCongratulationsBinding;

public class CongratulationsActivity extends BaseActivity {

    public static Intent getIntent(AppCompatActivity appCompatActivity) {
        return new Intent(appCompatActivity, CongratulationsActivity.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_congratulations;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCongratulationsBinding viewBinding =
                getViewBinding(ActivityCongratulationsBinding.class);

        viewBinding.endDonateButton.setOnClickListener(view -> {
            Intent intent = CharityActivity.getIntent(CongratulationsActivity.this);
            startActivity(intent);
            finish();
        });
    }
}
