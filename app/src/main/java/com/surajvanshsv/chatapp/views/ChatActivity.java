package com.surajvanshsv.chatapp.views;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.surajvanshsv.chatapp.R;
import com.surajvanshsv.chatapp.databinding.ActivityChatBinding;
import com.surajvanshsv.chatapp.model.ChatMessage;
import com.surajvanshsv.chatapp.viewmodel.MyViewModel;
import com.surajvanshsv.chatapp.views.adapters.ChatAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding binding;
    private MyViewModel myViewModel;
    private RecyclerView recyclerView;
    private ChatAdapter myAdapter;
    private List<ChatMessage> messagesList;

    // Animation and UI enhancement variables
    private Handler handler = new Handler();
    private Runnable typingRunnable;
    private boolean isTyping = false;
    private AnimationDrawable neonLineAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);

        // Initialize animations and UI enhancements
        initializeAnimations();
        setupRecyclerView();
        setupMessageObserver();
        setupSendButton();
        setupEditTextAnimations();
        binding.edittextChatMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.edittextChatMessage.requestFocus();
                binding.edittextChatMessage.postDelayed(() -> {
                    binding.edittextChatMessage.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.showSoftInput(binding.edittextChatMessage, InputMethodManager.SHOW_IMPLICIT);
                    }
                }, 100);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initializeAnimations() {
        // Start the neon gradient line animation in header
        View neonLine = findViewById(R.id.main).findViewById(R.id.main); // You can add an ID to the neon line view
        // Note: The animated line drawable will start automatically

        // Add subtle entrance animation to the main layout
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(binding.main, "alpha", 0f, 1f);
        fadeIn.setDuration(500);
        fadeIn.setInterpolator(new AccelerateDecelerateInterpolator());
        fadeIn.start();

        // Add slide up animation to input area
        ObjectAnimator slideUp = ObjectAnimator.ofFloat(binding.layoutInput, "translationY", 200f, 0f);
        slideUp.setDuration(400);
        slideUp.setInterpolator(new AccelerateDecelerateInterpolator());
        slideUp.setStartDelay(200);
        slideUp.start();
    }

    private void setupRecyclerView() {
        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        // Add fade in animation to RecyclerView
        ObjectAnimator recyclerFadeIn = ObjectAnimator.ofFloat(recyclerView, "alpha", 0f, 1f);
        recyclerFadeIn.setDuration(600);
        recyclerFadeIn.setStartDelay(300);
        recyclerFadeIn.start();
    }

    private void setupMessageObserver() {
        String groupName = getIntent().getStringExtra("GROUP_NAME");

        myViewModel.getMessagesLiveData(groupName).observe(this, new Observer<List<ChatMessage>>() {
            @Override
            public void onChanged(List<ChatMessage> chatMessages) {
                messagesList = new ArrayList<>();
                messagesList.addAll(chatMessages);

                myAdapter = new ChatAdapter(messagesList, getApplicationContext());
                recyclerView.setAdapter(myAdapter);
                myAdapter.notifyDataSetChanged();

                // Scroll to the latest message with smooth animation
                int latestPosition = myAdapter.getItemCount() - 1;
                if (latestPosition > 0) {
                    recyclerView.smoothScrollToPosition(latestPosition);

                    // Add a subtle bounce effect when new message arrives
                    animateNewMessage();
                }
            }
        });
    }

    private void setupSendButton() {
        binding.setVModel(myViewModel);

        binding.sendBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = binding.edittextChatMessage.getText().toString().trim();

                if (!msg.isEmpty()) {
                    String groupName = getIntent().getStringExtra("GROUP_NAME");

                    // Add send button animation
                    animateSendButton();

                    // Send message
                    myViewModel.sendMessage(msg, groupName);

                    // Clear input with fade animation
                    animateClearInput();

                    // Hide typing indicator
                    hideTypingIndicator();
                }
            }
        });
    }

    private void setupEditTextAnimations() {
        binding.edittextChatMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Show/hide send button based on text
                animateSendButtonVisibility(!s.toString().trim().isEmpty());

                // Simulate typing indicator (you can connect this to real typing events)
                handleTypingIndicator(s.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Add focus change animations
        binding.edittextChatMessage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                animateEditTextFocus(hasFocus);
            }
        });
    }

    private void animateNewMessage() {
        // Subtle bounce animation for new messages
        ValueAnimator bounceAnimator = ValueAnimator.ofFloat(0f, 10f, 0f);
        bounceAnimator.setDuration(300);
        bounceAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                recyclerView.setTranslationY(-value);
            }
        });
        bounceAnimator.start();
    }

    private void animateSendButton() {
        // Scale animation for send button
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(binding.sendBTN, "scaleX", 1f, 1.1f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(binding.sendBTN, "scaleY", 1f, 1.1f, 1f);

        scaleX.setDuration(200);
        scaleY.setDuration(200);

        scaleX.start();
        scaleY.start();

        // Add a subtle glow effect
        ObjectAnimator alpha = ObjectAnimator.ofFloat(binding.sendBTN, "alpha", 1f, 0.8f, 1f);
        alpha.setDuration(200);
        alpha.start();
    }

    private void animateClearInput() {
        // Fade out current text, then clear
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(binding.edittextChatMessage, "alpha", 1f, 0.5f);
        fadeOut.setDuration(150);
        fadeOut.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                binding.edittextChatMessage.getText().clear();
                ObjectAnimator fadeIn = ObjectAnimator.ofFloat(binding.edittextChatMessage, "alpha", 0.5f, 1f);
                fadeIn.setDuration(150);
                fadeIn.start();
            }
        });
        fadeOut.start();
    }

    private void animateSendButtonVisibility(boolean hasText) {
        float targetAlpha = hasText ? 1f : 0.7f;
        float targetScale = hasText ? 1f : 0.9f;

        ObjectAnimator alpha = ObjectAnimator.ofFloat(binding.sendBTN, "alpha", targetAlpha);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(binding.sendBTN, "scaleX", targetScale);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(binding.sendBTN, "scaleY", targetScale);

        alpha.setDuration(200);
        scaleX.setDuration(200);
        scaleY.setDuration(200);

        alpha.start();
        scaleX.start();
        scaleY.start();
    }

    private void animateEditTextFocus(boolean hasFocus) {
        // Subtle elevation change animation
        float targetElevation = hasFocus ? 8f : 4f;
        ObjectAnimator elevation = ObjectAnimator.ofFloat(binding.edittextChatMessage, "elevation", targetElevation);
        elevation.setDuration(200);
        elevation.start();

        // Subtle scale animation
        float targetScale = hasFocus ? 1.02f : 1f;
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(binding.edittextChatMessage, "scaleX", targetScale);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(binding.edittextChatMessage, "scaleY", targetScale);

        scaleX.setDuration(200);
        scaleY.setDuration(200);
        scaleX.start();
        scaleY.start();
    }

    private void handleTypingIndicator(boolean typing) {
        if (typing && !isTyping) {
            // Start typing indicator after a short delay
            handler.removeCallbacks(typingRunnable);
            typingRunnable = new Runnable() {
                @Override
                public void run() {
                    showTypingIndicator();
                }
            };
            handler.postDelayed(typingRunnable, 500);
            isTyping = true;
        } else if (!typing && isTyping) {
            // Hide typing indicator after user stops typing
            handler.removeCallbacks(typingRunnable);
            typingRunnable = new Runnable() {
                @Override
                public void run() {
                    hideTypingIndicator();
                }
            };
            handler.postDelayed(typingRunnable, 1000);
            isTyping = false;
        }
    }

    private void showTypingIndicator() {
        View typingIndicator = findViewById(R.id.typing_indicator);
        if (typingIndicator != null && typingIndicator.getVisibility() != View.VISIBLE) {
            typingIndicator.setVisibility(View.VISIBLE);

            // Fade in animation
            ObjectAnimator fadeIn = ObjectAnimator.ofFloat(typingIndicator, "alpha", 0f, 1f);
            ObjectAnimator slideUp = ObjectAnimator.ofFloat(typingIndicator, "translationY", 50f, 0f);

            fadeIn.setDuration(300);
            slideUp.setDuration(300);

            fadeIn.start();
            slideUp.start();
        }
    }

    private void hideTypingIndicator() {
        View typingIndicator = findViewById(R.id.typing_indicator);
        if (typingIndicator != null && typingIndicator.getVisibility() == View.VISIBLE) {
            // Fade out animation
            ObjectAnimator fadeOut = ObjectAnimator.ofFloat(typingIndicator, "alpha", 1f, 0f);
            ObjectAnimator slideDown = ObjectAnimator.ofFloat(typingIndicator, "translationY", 0f, 30f);

            fadeOut.setDuration(200);
            slideDown.setDuration(200);

            fadeOut.addListener(new android.animation.AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(android.animation.Animator animation) {
                    typingIndicator.setVisibility(View.GONE);
                }
            });

            fadeOut.start();
            slideDown.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up handler callbacks
        if (handler != null && typingRunnable != null) {
            handler.removeCallbacks(typingRunnable);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Hide typing indicator when activity is paused
        hideTypingIndicator();
    }
}