package com.surajvanshsv.chatapp.views;

import android.os.Bundle;
import android.view.View;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);

        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);

        // RecyclerView with DataBinding
        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        // Getting the Group Name from the Clicked Item in the GroupsActivity
        String groupName = getIntent().getStringExtra("GROUP_NAME");

        myViewModel.getMessagesLiveData(groupName).observe(this, new Observer<List<ChatMessage>>() {
            @Override
            public void onChanged(List<ChatMessage> chatMessages) {
                messagesList = new ArrayList<>();
                messagesList.addAll(chatMessages);

                myAdapter = new ChatAdapter(messagesList,getApplicationContext());

                recyclerView.setAdapter(myAdapter);
                myAdapter.notifyDataSetChanged();

                // Scroll to the latest message added:
                int latestPosition = myAdapter.getItemCount() -1;
                if (latestPosition > 0) {
                    recyclerView.smoothScrollToPosition(latestPosition);
                }


            }
        });

        binding.setVModel(myViewModel);

        binding.sendBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String msg = binding.edittextChatMessage.getText().toString();
                myViewModel.sendMessage(msg, groupName);

                binding.edittextChatMessage.getText().clear();
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}