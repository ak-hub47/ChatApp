package com.example.loginwithfirebase.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginwithfirebase.MessageActivity;
import com.example.loginwithfirebase.R;
import com.example.loginwithfirebase.model.Chat;
import com.example.loginwithfirebase.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.net.URISyntaxException;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT= 0;
    public static final int MSG_TYPE_RIGHT= 1;
    private Context context;
    private List<Chat> chatList;
    FirebaseUser firebaseUser;
    int replyposiition=0;
    boolean replyclicked= false;


    public MessageAdapter(Context context, List<Chat> chatList) {
        this.context = context;
        this.chatList = chatList;

    }

    public void itemclicked(int position){
        replyclicked= true;
        this.replyposiition=position;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView showMessage,txt_seen,messageswipe,usernameswipe;
        ImageView image,swipeImage;
        RelativeLayout item,swipe1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            showMessage=itemView.findViewById(R.id.show_message);
            txt_seen=itemView.findViewById(R.id.seen);
            image=itemView.findViewById(R.id.image);
            messageswipe=itemView.findViewById(R.id.messageswipe);
            usernameswipe=itemView.findViewById(R.id.usernameswipe);
            swipeImage=itemView.findViewById(R.id.imaheswipe);
            item=itemView.findViewById(R.id.item);
            swipe1=itemView.findViewById(R.id.swipe1);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            return new ViewHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Chat chat=chatList.get(position);

        if(chat.getMessage().equals("default")){

                holder.showMessage.setVisibility(View.GONE);
                holder.image.setVisibility(View.VISIBLE);
                holder.usernameswipe.setVisibility(View.GONE);
                holder.messageswipe.setVisibility(View.GONE);
                Picasso.with(context).load(chat.getClickImage()).into(holder.image);

        }else {
            if(replyclicked && replyposiition==position){
                holder.item.setBackground(context.getResources().getDrawable(R.drawable.background_left));
                holder.swipe1.setBackgroundColor(context.getResources().getColor(R.color.colorGray));
            }
            holder.image.setVisibility(View.GONE);
            holder.showMessage.setVisibility(View.VISIBLE);
            holder.showMessage.setText(chat.getMessage());
            holder.swipeImage.setVisibility(View.GONE);
        }


        if(position == chatList.size()-1){
            if(chat.isIsseen()){
                holder.txt_seen.setText("Seen");
            }else {
                holder.txt_seen.setText("Delivered");
            }
        }else {
            holder.txt_seen.setVisibility(View.GONE);
        }

        if(chat.getReply().equals("yes")){
            holder.usernameswipe.setVisibility(View.VISIBLE);
            holder.usernameswipe.setText(chat.getUsername());
            if(chat.getChatpositionimage().equals("")){
                holder.messageswipe.setVisibility(View.VISIBLE);
                holder.messageswipe.setText(chat.getChatposition());
                holder.swipeImage.setVisibility(View.GONE);
            }
            else {
                holder.messageswipe.setVisibility(View.GONE);
                holder.swipeImage.setVisibility(View.VISIBLE);
                Picasso.with(context).load(chat.getChatpositionimage()).into(holder.swipeImage);
            }
        }else {
            holder.usernameswipe.setVisibility(View.GONE);
            holder.messageswipe.setVisibility(View.GONE);

        }

    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        if(chatList.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }
    }
}

