package com.example.dechingv1;
import android.os.Bundle;
import android.os.UserManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private EditText editTextMessage;
    private Button buttonSend;
    private MessageDatabase messageDatabase;
    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = findViewById(R.id.recyclerViewMessages);
        messageAdapter = new MessageAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);

        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);

        messageDatabase = MessageDatabase.getInstance(this);
        userManager = new UserManager(messageDatabase);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        // Activer la flèche de retour dans la barre d'action
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Charger les messages de l'utilisateur actuel pour l'exemple
        loadUserMessages();
    }

    private void sendMessage() {
        String messageText = editTextMessage.getText().toString();
        User currentUser = userManager.getCurrentUser();
        Message newMessage = new Message(currentUser.getId(), messageText);
        messageDatabase.messageDao().insertMessage(newMessage);
        messageAdapter.addMessage(newMessage);
        editTextMessage.setText("");
    }

    private void loadUserMessages() {
        User currentUser = userManager.getCurrentUser();
        List<Message> userMessages = messageDatabase.messageDao().getMessagesForUser(currentUser.getId());
        messageAdapter.addMessages(userMessages);
    }
}
