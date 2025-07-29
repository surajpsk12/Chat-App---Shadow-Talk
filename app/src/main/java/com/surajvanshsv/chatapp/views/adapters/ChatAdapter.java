package com.surajvanshsv.chatapp.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.surajvanshsv.chatapp.BR;
import com.surajvanshsv.chatapp.R;
import com.surajvanshsv.chatapp.databinding.RowChatBinding;
import com.surajvanshsv.chatapp.model.ChatMessage;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {


    private List<ChatMessage> chatMessageList;
    private Context context;

    public ChatAdapter(List<ChatMessage> chatMessageList, Context context) {
        this.chatMessageList = chatMessageList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.row_chat,parent,false);

        RowChatBinding binding = DataBindingUtil.bind(view);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.getBinding().setVariable(BR.chatMessage, chatMessageList.get(position));
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return chatMessageList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        private RowChatBinding binding;

        public MyViewHolder( RowChatBinding binding) {
            super(binding.getRoot());
            setBinding(binding);
        }


        public RowChatBinding getBinding() {
            return binding;
        }

        public void setBinding(RowChatBinding binding) {
            this.binding = binding;
        }
    }
}
