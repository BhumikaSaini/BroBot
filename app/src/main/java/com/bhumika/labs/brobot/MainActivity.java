package com.bhumika.labs.brobot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ConversationService mConversationService = new ConversationService(
                "2018-08-5",
                getString(R.string.username),
                getString(R.string.password));
    }
}
