package com.example.dechingv1;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import android.view.MenuItem;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private EditText editTextMessage;
    private Button buttonSend;
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
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        // Charger des messages fictifs pour l'exemple
        loadDummyMessages();
        // Activer la flèche de retour dans la barre d'action
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    // Gérer le clic sur la flèche de retour
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                // Gérer le clic sur la flèche de retour
//                onBackPressed();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
    private void sendMessage() {
        String messageText = editTextMessage.getText().toString();

        // Envoi du message au backend (adapter en conséquence)
        Message newMessage = new Message("Utilisateur actuel", messageText);
        messageAdapter.addMessage(newMessage);

        // Effacer le champ de saisie
        editTextMessage.setText("");
    }
    private void loadDummyMessages() {
        List<Message> dummyMessages = new ArrayList<>();
        dummyMessages.add(new Message("Utilisateur 1", "Bonjour !"));
        dummyMessages.add(new Message("Utilisateur 2", "Salut, comment ça va ?"));
        dummyMessages.add(new Message("Utilisateur 1", "Ça va bien, merci !"));

        messageAdapter.addMessages(dummyMessages);
    }
}