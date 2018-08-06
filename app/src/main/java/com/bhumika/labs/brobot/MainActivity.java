package com.bhumika.labs.brobot;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;

public class MainActivity extends AppCompatActivity {

    private ImageButton sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initLayout();

        // Added String Resource Values for username and password. Remove if you want.
        final ConversationService mConversationService = new ConversationService(
                "2018-08-5",
                getString(R.string.username),
                getString(R.string.password));
    }

    private void initLayout() {
        sendButton = findViewById(R.id.send_button);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Send Button Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
