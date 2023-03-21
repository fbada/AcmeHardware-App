package com.hardwarestore.acme;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    private TextView textView;
    private TextView textView2;
    private ImageView imageView;
    private Button button;
    private TextView textView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        imageView = findViewById(R.id.imageView);
        button = findViewById(R.id.button);
        textView3 = findViewById(R.id.textView3);

        // Set initial positions of views
        textView.setTranslationY(500f);
        textView2.setTranslationY(500f);
        imageView.setTranslationY(500f);
        button.setAlpha(0f);
        textView3.setAlpha(0f);

        // Set initial positions of views
        textView.setTranslationY(500f);
        textView2.setTranslationY(500f);
        imageView.setTranslationY(500f);
        button.setAlpha(0f);
        textView3.setAlpha(0f);

        // Animate views
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(textView, "translationY", 500f, 0f);
        animator1.setDuration(800);
        animator1.setInterpolator(new DecelerateInterpolator());
        animator1.start();

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(textView2, "translationY", 500f, 0f);
        animator2.setDuration(800);
        animator2.setInterpolator(new DecelerateInterpolator());
        animator2.setStartDelay(100);
        animator2.start();

        ObjectAnimator animator3 = ObjectAnimator.ofFloat(imageView, "translationY", 500f, 0f);
        animator3.setDuration(800);
        animator3.setInterpolator(new DecelerateInterpolator());
        animator3.setStartDelay(200);
        animator3.start();

        ObjectAnimator animator4 = ObjectAnimator.ofFloat(button, "alpha", 0f, 1f);
        animator4.setDuration(500);
        animator4.setStartDelay(500);
        animator4.start();

        ObjectAnimator animator5 = ObjectAnimator.ofFloat(textView3, "alpha", 0f, 1f);
        animator5.setDuration(500);
        animator5.setStartDelay(500);
        animator5.start();
    }


    /**
     * This method is called when the user taps the Login button
     *@param view
     */

    public void goToLogin(View view) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * This method is called when the user taps the Sign Up button
     * @param view
     */

    public void goToSignUp(View view) {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    /**
     * This method is called when the user taps the Sign Up button
     * @param view
     */
    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
            finish();
        }
    }
}
