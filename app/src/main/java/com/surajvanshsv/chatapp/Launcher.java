package com.surajvanshsv.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.surajvanshsv.chatapp.views.LoginActivity;

public class Launcher extends AppCompatActivity {

    private LinearLayout logoContainer, loadingContainer;
    private ImageView appLogo;
    private TextView titleText, subtitleText, loadingText;
    private View neonUnderline, dot1, dot2, dot3;
    private View bgCircle1, bgCircle2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_launcher);

        // Initialize views
        initViews();

        // Start animations
        startAnimations();

        // Navigate to login after 3 seconds (increased for animation viewing)
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Fade out animation before transition
            fadeOutAndNavigate();
        }, 3000);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initViews() {
        logoContainer = findViewById(R.id.logo_container);
        loadingContainer = findViewById(R.id.loading_container);
        appLogo = findViewById(R.id.app_logo);
        titleText = findViewById(R.id.textView3);
        subtitleText = findViewById(R.id.subtitle_text);
        loadingText = findViewById(R.id.loading_text);
        neonUnderline = findViewById(R.id.neon_underline);
        dot1 = findViewById(R.id.dot1);
        dot2 = findViewById(R.id.dot2);
        dot3 = findViewById(R.id.dot3);
        bgCircle1 = findViewById(R.id.bg_circle_1);
        bgCircle2 = findViewById(R.id.bg_circle_2);

        // Initially hide elements for animation
        logoContainer.setAlpha(0f);
        loadingContainer.setAlpha(0f);
        neonUnderline.setScaleX(0f);
    }

    private void startAnimations() {
        // Background circles rotation
        startBackgroundAnimations();

        // Logo container fade in and scale
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            animateLogoContainer();
        }, 300);

        // Loading container fade in
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            animateLoadingContainer();
        }, 800);

        // Neon underline expand
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            animateNeonUnderline();
        }, 1200);

        // Loading dots animation
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startLoadingDotsAnimation();
        }, 1500);
    }

    private void startBackgroundAnimations() {
        // Continuous rotation for background circles
        Animation rotateAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_continuous);
        bgCircle1.startAnimation(rotateAnim);

        // Counter rotation for second circle
        Animation rotateCounterAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_continuous);
        rotateCounterAnim.setDuration(12000); // Slower rotation
        bgCircle2.startAnimation(rotateCounterAnim);
    }

    private void animateLogoContainer() {
        logoContainer.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(800)
                .setStartDelay(0)
                .start();

        // Add pulse effect to logo
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Animation pulseAnim = AnimationUtils.loadAnimation(this, R.anim.pulse_glow);
            appLogo.startAnimation(pulseAnim);
        }, 1000);
    }

    private void animateLoadingContainer() {
        loadingContainer.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(600)
                .start();
    }

    private void animateNeonUnderline() {
        neonUnderline.animate()
                .scaleX(1f)
                .setDuration(500)
                .start();
    }

    private void startLoadingDotsAnimation() {
        // Staggered bounce animation for dots
        animateLoadingDot(dot1, 0);
        animateLoadingDot(dot2, 200);
        animateLoadingDot(dot3, 400);
    }

    private void animateLoadingDot(View dot, int delay) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            dot.animate()
                    .scaleX(1.5f)
                    .scaleY(1.5f)
                    .alpha(0.3f)
                    .setDuration(300)
                    .withEndAction(() -> {
                        dot.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .alpha(1f)
                                .setDuration(300)
                                .withEndAction(() -> {
                                    // Repeat animation
                                    animateLoadingDot(dot, 600);
                                })
                                .start();
                    })
                    .start();
        }, delay);
    }

    private void fadeOutAndNavigate() {
        // Update loading text
        loadingText.setText("Launching...");

        // Fade out animation
        View mainLayout = findViewById(R.id.main);
        mainLayout.animate()
                .alpha(0f)
                .setDuration(500)
                .withEndAction(() -> {
                    startActivity(new Intent(Launcher.this, LoginActivity.class));
                    finish();
                    // Custom transition
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                })
                .start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Clear animations to prevent memory leaks
        if (bgCircle1 != null) bgCircle1.clearAnimation();
        if (bgCircle2 != null) bgCircle2.clearAnimation();
        if (appLogo != null) appLogo.clearAnimation();
    }
}