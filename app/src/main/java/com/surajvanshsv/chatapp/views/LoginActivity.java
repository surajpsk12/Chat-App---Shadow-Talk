package com.surajvanshsv.chatapp.views;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.surajvanshsv.chatapp.R;
import com.surajvanshsv.chatapp.databinding.ActivityLoginBinding;
import com.surajvanshsv.chatapp.viewmodel.MyViewModel;

public class LoginActivity extends AppCompatActivity {

    MyViewModel viewModel;
    private ActivityLoginBinding binding;

    // Views for animation
    private LinearLayout brandingSection, loginContent, loginButtonContainer, loadingIndicator;
    private ImageView loginLogo;
    private View accentLine, bgGlowTop, bgGlowBottom;
    private Button loginButton;
    private View loadingDot1, loadingDot2, loadingDot3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        viewModel = new ViewModelProvider(this).get(MyViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setVModel(viewModel);

        // Initialize views
        initViews();

        // Setup entrance animations
        setupEntranceAnimations();

        // Start animations
        startEntranceAnimations();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Setup loading state observer (if your ViewModel has loading state)
        setupLoadingStateObserver();
    }

    private void initViews() {
        brandingSection = findViewById(R.id.branding_section);
        loginContent = findViewById(R.id.login_content);
        loginButtonContainer = findViewById(R.id.login_button_container);
        loadingIndicator = findViewById(R.id.loading_indicator);
        loginLogo = findViewById(R.id.login_logo);
        accentLine = findViewById(R.id.accent_line);
        bgGlowTop = findViewById(R.id.bg_glow_top);
        bgGlowBottom = findViewById(R.id.bg_glow_bottom);
        loginButton = findViewById(R.id.button_auth);
        loadingDot1 = findViewById(R.id.loading_dot1);
        loadingDot2 = findViewById(R.id.loading_dot2);
        loadingDot3 = findViewById(R.id.loading_dot3);
    }

    private void setupEntranceAnimations() {
        // Initially hide elements for entrance animation
        brandingSection.setAlpha(0f);
        brandingSection.setTranslationY(-50f);

        loginContent.setAlpha(0f);
        loginContent.setTranslationY(30f);

        loginButtonContainer.setAlpha(0f);
        loginButtonContainer.setTranslationY(50f);

        accentLine.setScaleX(0f);
    }

    private void startEntranceAnimations() {
        // Background glow rotation
        startBackgroundAnimations();

        // Sequential entrance animations
        animateBrandingSection();

        new Handler(Looper.getMainLooper()).postDelayed(this::animateLoginContent, 300);
        new Handler(Looper.getMainLooper()).postDelayed(this::animateAccentLine, 600);
        new Handler(Looper.getMainLooper()).postDelayed(this::animateLoginButton, 900);
        new Handler(Looper.getMainLooper()).postDelayed(this::startLogoGlow, 1200);
    }

    private void startBackgroundAnimations() {
        // Continuous slow rotation for background glows
        bgGlowTop.animate()
                .rotationBy(360f)
                .setDuration(20000)
                .withEndAction(() -> startBackgroundAnimations())
                .start();

        bgGlowBottom.animate()
                .rotationBy(-360f)
                .setDuration(25000)
                .withEndAction(() -> {
                    bgGlowBottom.animate()
                            .rotationBy(-360f)
                            .setDuration(25000)
                            .withEndAction(() -> startBackgroundAnimations())
                            .start();
                })
                .start();
    }

    private void animateBrandingSection() {
        brandingSection.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(600)
                .setInterpolator(new android.view.animation.DecelerateInterpolator())
                .start();
    }

    private void animateLoginContent() {
        loginContent.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(500)
                .setInterpolator(new android.view.animation.DecelerateInterpolator())
                .start();
    }

    private void animateAccentLine() {
        accentLine.animate()
                .scaleX(1f)
                .setDuration(400)
                .setInterpolator(new android.view.animation.OvershootInterpolator())
                .start();
    }

    private void animateLoginButton() {
        loginButtonContainer.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(500)
                .setInterpolator(new android.view.animation.DecelerateInterpolator())
                .withEndAction(() -> {
                    // Add subtle button hover effect
                    loginButton.setOnTouchListener((v, event) -> {
                        switch (event.getAction()) {
                            case android.view.MotionEvent.ACTION_DOWN:
                                v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).start();
                                break;
                            case android.view.MotionEvent.ACTION_UP:
                            case android.view.MotionEvent.ACTION_CANCEL:
                                v.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                                break;
                        }
                        return false;
                    });
                })
                .start();
    }

    private void startLogoGlow() {
        // Subtle pulsing glow effect for logo
        loginLogo.animate()
                .scaleX(1.1f)
                .scaleY(1.1f)
                .alpha(0.8f)
                .setDuration(2000)
                .withEndAction(() -> {
                    loginLogo.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .alpha(1f)
                            .setDuration(2000)
                            .withEndAction(this::startLogoGlow)
                            .start();
                })
                .start();
    }

    private void setupLoadingStateObserver() {
        // Override the button click to show loading state
        loginButton.setOnClickListener(v -> {
            showLoadingState();
            // Call your ViewModel method
            viewModel.signUpAnonymousUser();
        });
    }

    private void showLoadingState() {
        // Hide main content and show loading
        brandingSection.animate().alpha(0.3f).setDuration(300).start();
        loginContent.animate().alpha(0.3f).setDuration(300).start();
        loginButtonContainer.animate().alpha(0f).setDuration(300).start();

        loadingIndicator.setVisibility(View.VISIBLE);
        loadingIndicator.setAlpha(0f);
        loadingIndicator.animate().alpha(1f).setDuration(300).start();

        // Start loading dots animation
        startLoadingDotsAnimation();
    }

    private void startLoadingDotsAnimation() {
        animateLoadingDot(loadingDot1, 0);
        animateLoadingDot(loadingDot2, 200);
        animateLoadingDot(loadingDot3, 400);
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
                                    // Continue animation if still loading
                                    if (loadingIndicator.getVisibility() == View.VISIBLE) {
                                        animateLoadingDot(dot, 600);
                                    }
                                })
                                .start();
                    })
                    .start();
        }, delay);
    }

    public void hideLoadingState() {
        // Call this method when login is complete
        loadingIndicator.animate().alpha(0f).setDuration(300).withEndAction(() -> {
            loadingIndicator.setVisibility(View.GONE);
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clear animations to prevent memory leaks
        if (bgGlowTop != null) bgGlowTop.clearAnimation();
        if (bgGlowBottom != null) bgGlowBottom.clearAnimation();
        if (loginLogo != null) loginLogo.clearAnimation();
    }
}