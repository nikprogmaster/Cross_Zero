package com.example.cross_zero;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FrameLayout mainLayout = findViewById(R.id.mainlayout);
        final ObjectAnimator objectAnimator = new ObjectAnimator();
        objectAnimator.setDuration(400).setPropertyName("y");
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.setFloatValues(1800f, 1200f);
        final int height = getResources().getDisplayMetrics().heightPixels;
        Log.i("туру", String.valueOf(height));
        CrossZeroView crossZeroView = new CrossZeroView(MainActivity.this,
                new CrossZeroView.ShowDialog() {
                    @Override
                    public void onShowDialog(DialogFragment dialogFragment) {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .setCustomAnimations((height >= 1928 && height<=2128)?  R.animator.slide_up : 0, (height >= 1928 && height<=2128)?  R.animator.slide_up : 0)
                                .replace(R.id.mainlayout, dialogFragment)
                                .commit();
                    }
                },
                new CrossZeroView.EndGame() {
                    @Override
                    public void endingOfGame() {
                        finish();
                    }
                });

        mainLayout.addView(crossZeroView);
    }


}
