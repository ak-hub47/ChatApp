package com.example.loginwithfirebase.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginwithfirebase.MessageActivity;
import com.example.loginwithfirebase.R;
import com.example.loginwithfirebase.model.UserModel;
import com.google.firebase.firestore.auth.User;

import java.security.PrivateKey;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private List<UserModel> userModelList;
    private boolean isChat;

    public UserAdapter(Context context, List<UserModel> userModelList, boolean isChat) {
        this.context = context;
        this.userModelList = userModelList;
        this.isChat = isChat;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView username;
        RelativeLayout rlmain;
        ImageView isOn,isOff;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username=itemView.findViewById(R.id.usernameUsers);
            rlmain=itemView.findViewById(R.id.rlmain);
            isOff=itemView.findViewById(R.id.isOff);
            isOn=itemView.findViewById(R.id.isOn);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.list_users,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final UserModel user=userModelList.get(position);

        if(user.getUsername().equals("C4U") || user.getUsername().equals("TWR") || user.getUsername().equals("entire")){
            holder.rlmain.setVisibility(View.VISIBLE);
        }else{
            holder.rlmain.setVisibility(View.GONE);
        }
            holder.username.setText(user.getUsername());

        if(isChat){
            if(user.getStatus().equals("online")){
                holder.isOn.setVisibility(View.VISIBLE);
                holder.isOff.setVisibility(View.GONE);
            }
            else {
                holder.isOff.setVisibility(View.VISIBLE);
                holder.isOn.setVisibility(View.GONE);
            }
        }else {
            holder.isOn.setVisibility(View.GONE);
            holder.isOff.setVisibility(View.GONE);
        }



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, MessageActivity.class);
                intent.putExtra("userId",user.getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }
}
