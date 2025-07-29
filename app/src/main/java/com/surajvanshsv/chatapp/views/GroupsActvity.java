package com.surajvanshsv.chatapp.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import com.surajvanshsv.chatapp.databinding.ActivityGroupsActvityBinding;
import com.surajvanshsv.chatapp.model.ChatGroup;
import com.surajvanshsv.chatapp.viewmodel.MyViewModel;
import com.surajvanshsv.chatapp.views.adapters.GroupAdapter;

import java.util.ArrayList;
import java.util.List;

public class GroupsActvity extends AppCompatActivity {
    private ArrayList<ChatGroup> chatGroupArrayList;
    private RecyclerView recyclerView;
    private GroupAdapter groupAdapter;
    private ActivityGroupsActvityBinding binding;
    private MyViewModel myViewModel;
    private Dialog chatGroupDialog;
    private boolean isFirstLoad = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Apply neon theme to status bar
        getWindow().setStatusBarColor(getResources().getColor(R.color.neon_bg_dark, null));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.neon_bg_dark, null));

        setContentView(R.layout.activity_groups_actvity);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_groups_actvity);

        // Define the view model
        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);

        // Initialize RecyclerView with enhanced animations
        setupRecyclerView();

        // Setup FAB with enhanced animations
        setupFloatingActionButton();

        // Setup observer for data changes
        setupDataObserver();

        // Apply window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Animate header on first load
        animateHeaderEntrance();
    }

    private void setupRecyclerView() {
        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Add subtle scroll animations
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // Hide/show FAB based on scroll direction
                if (dy > 0 && binding.fab.getVisibility() == View.VISIBLE) {
                    hideFab();
                } else if (dy < 0 && binding.fab.getVisibility() != View.VISIBLE) {
                    showFab();
                }
            }
        });
    }

    private void setupFloatingActionButton() {
        // Initial FAB animation entrance
        binding.fab.setScaleX(0f);
        binding.fab.setScaleY(0f);

        // Animate FAB entrance after a delay
        new Handler().postDelayed(() -> {
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(binding.fab, "scaleX", 0f, 1f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(binding.fab, "scaleY", 0f, 1f);

            scaleX.setDuration(300);
            scaleY.setDuration(300);
            scaleX.setInterpolator(new DecelerateInterpolator());
            scaleY.setInterpolator(new DecelerateInterpolator());

            scaleX.start();
            scaleY.start();
        }, 500);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add click animation
                animateFabClick();
                showDialog();
            }
        });
    }

    private void setupDataObserver() {
        myViewModel.getGroupList().observe(this, new Observer<List<ChatGroup>>() {
            @Override
            public void onChanged(List<ChatGroup> chatGroups) {
                // The updated data is received as "chatGroups" parameter in onChanged()
                chatGroupArrayList = new ArrayList<>();
                chatGroupArrayList.addAll(chatGroups);

                groupAdapter = new GroupAdapter(chatGroupArrayList);
                recyclerView.setAdapter(groupAdapter);

                // Apply layout animation for smooth item appearances
                if (isFirstLoad && !chatGroups.isEmpty()) {
                    recyclerView.setLayoutAnimation(
                            AnimationUtils.loadLayoutAnimation(GroupsActvity.this, R.anim.neon_layout_animation)
                    );
                    isFirstLoad = false;
                }

                groupAdapter.notifyDataSetChanged();

                // Animate RecyclerView entrance if it has items
                if (!chatGroups.isEmpty()) {
                    animateRecyclerViewEntrance();
                }
            }
        });
    }

    private void animateHeaderEntrance() {
        View header = binding.imageView2;
        header.setTranslationY(-200f);
        header.setAlpha(0f);

        header.animate()
                .translationY(0f)
                .alpha(1f)
                .setDuration(600)
                .setInterpolator(new DecelerateInterpolator())
                .start();
    }

    private void animateRecyclerViewEntrance() {
        recyclerView.setAlpha(0f);
        recyclerView.setTranslationY(50f);

        recyclerView.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(400)
                .setStartDelay(200)
                .setInterpolator(new DecelerateInterpolator())
                .start();
    }

    private void animateFabClick() {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(binding.fab, "scaleX", 1f, 0.9f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(binding.fab, "scaleY", 1f, 0.9f, 1f);

        scaleX.setDuration(150);
        scaleY.setDuration(150);

        scaleX.start();
        scaleY.start();
    }

    private void hideFab() {
        binding.fab.animate()
                .scaleX(0f)
                .scaleY(0f)
                .setDuration(200)
                .setInterpolator(new DecelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        binding.fab.setVisibility(View.GONE);
                    }
                });
    }

    private void showFab() {
        binding.fab.setVisibility(View.VISIBLE);
        binding.fab.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(200)
                .setInterpolator(new DecelerateInterpolator())
                .setListener(null);
    }

    public void showDialog() {
        chatGroupDialog = new Dialog(this);
        chatGroupDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Apply neon theme to dialog
        chatGroupDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_layout, null);
        chatGroupDialog.setContentView(view);

        // Animate dialog entrance
        view.setScaleX(0.8f);
        view.setScaleY(0.8f);
        view.setAlpha(0f);

        chatGroupDialog.show();

        view.animate()
                .scaleX(1f)
                .scaleY(1f)
                .alpha(1f)
                .setDuration(250)
                .setInterpolator(new DecelerateInterpolator())
                .start();

        Button submit = view.findViewById(R.id.submit_btn);
        EditText edt = view.findViewById(R.id.chat_group_edt);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String groupName = edt.getText().toString().trim();

                if (!groupName.isEmpty()) {
                    // Animate button press
                    animateButtonPress(submit);

                    // Show styled toast
                    Toast.makeText(GroupsActvity.this,
                            "✨ Chat group '" + groupName + "' created!",
                            Toast.LENGTH_SHORT).show();

                    myViewModel.createNewGroup(groupName);

                    // Animate dialog exit
                    animateDialogExit();
                } else {
                    // Shake animation for empty input
                    animateInputError(edt);
                }
            }
        });
    }

    private void animateButtonPress(Button button) {
        button.animate()
                .scaleX(0.95f)
                .scaleY(0.95f)
                .setDuration(100)
                .withEndAction(() -> {
                    button.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(100)
                            .start();
                })
                .start();
    }

    private void animateInputError(EditText editText) {
        ObjectAnimator shake = ObjectAnimator.ofFloat(editText, "translationX", 0, 25, -25, 25, -25, 15, -15, 6, -6, 0);
        shake.setDuration(600);
        shake.start();

        // Show error styling briefly
        editText.setBackgroundTintList(getResources().getColorStateList(android.R.color.holo_red_light, null));
        new Handler().postDelayed(() -> {
            editText.setBackgroundTintList(getResources().getColorStateList(R.color.neon_cyan, null));
        }, 1000);
    }

    private void animateDialogExit() {
        View dialogView = chatGroupDialog.findViewById(android.R.id.content);

        dialogView.animate()
                .scaleX(0.8f)
                .scaleY(0.8f)
                .alpha(0f)
                .setDuration(200)
                .setInterpolator(new DecelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        chatGroupDialog.dismiss();
                    }
                })
                .start();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Ensure FAB is visible when returning to activity
        if (binding.fab.getVisibility() != View.VISIBLE) {
            showFab();
        }
    }
}