package com.find.luxelaundary;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.find.luxelaundary.Adapter.ChatAdapter;
import com.find.luxelaundary.Model.ChatMessage;
import com.find.luxelaundary.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Chat_user extends AppCompatActivity {

    private EditText msg;
    private ImageView sent, btn_back;
    private RecyclerView rvChat;

    private ChatAdapter adapter;
    private ArrayList<ChatMessage> messageList = new ArrayList<>();

    private DatabaseReference chatRef;
    String name ;
     String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_user);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.white)); // Use your color here
        }

        SharedPreferences sp = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        currentUserId = sp.getString("id", "");
        name = sp.getString("name", "");

        msg = findViewById(R.id.msg);
        sent = findViewById(R.id.sent);
        btn_back = findViewById(R.id.btn_back);
        rvChat = findViewById(R.id.rvChat);

        adapter = new ChatAdapter(messageList, currentUserId);
        rvChat.setLayoutManager(new LinearLayoutManager(this));
        rvChat.setAdapter(adapter);

        chatRef = FirebaseDatabase.getInstance().getReference("chats");

        sent.setOnClickListener(v -> {
            String message = msg.getText().toString().trim();
            if (!message.isEmpty()) {
                long timestamp = System.currentTimeMillis();
                ChatMessage chatMessage = new ChatMessage(name,currentUserId, message, timestamp);
                chatRef.push().setValue(chatMessage);
                msg.setText("");
            }
        });

        loadMessages();

        btn_back.setOnClickListener(v -> finish());
    }

    private void loadMessages() {
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    ChatMessage message = snap.getValue(ChatMessage.class);
                    if (message != null) {
                        messageList.add(message);
                    }
                }
                adapter.notifyDataSetChanged();
                rvChat.scrollToPosition(messageList.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // handle error
            }
        });
    }
}
