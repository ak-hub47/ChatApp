package com.example.loginwithfirebase.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.loginwithfirebase.R;
import com.example.loginwithfirebase.adapter.UserAdapter;
import com.example.loginwithfirebase.model.Chat;
import com.example.loginwithfirebase.model.ChatList;
import com.example.loginwithfirebase.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    RecyclerView chatRecyclerView;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    private UserAdapter userAdapter;
    private List<UserModel> userModelList;
    private List<ChatList> userList;


    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_chat, container, false);
        chatRecyclerView=view.findViewById(R.id.chatRecyclerView);
        chatRecyclerView.setHasFixedSize(true);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        userList=new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference("ChatList").child(firebaseUser.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ChatList chatList=snapshot.getValue(ChatList.class);
                    userList.add(chatList);
                }

                chatList();

            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






        return view;
    }

    private void chatList() {

        userModelList=new ArrayList<>();
        databaseReference=FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                userModelList.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    UserModel user=snapshot.getValue(UserModel.class);
                    for(ChatList chatList:userList){
                        if(user.getId().equals(chatList.getId())){
                            userModelList.add(user);
                        }
                    }
                }

                userAdapter=new UserAdapter(getActivity(),userModelList,true);
                chatRecyclerView.setAdapter(userAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
