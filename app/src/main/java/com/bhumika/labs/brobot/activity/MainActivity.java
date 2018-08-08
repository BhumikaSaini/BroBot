package com.bhumika.labs.brobot.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.bhumika.labs.brobot.R;
import com.bhumika.labs.brobot.adapter.MessageListAdapter;
import com.bhumika.labs.brobot.model.Message;
import com.ibm.watson.developer_cloud.assistant.v1.Assistant;
import com.ibm.watson.developer_cloud.assistant.v1.model.InputData;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageOptions;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.http.ServiceCallback;
import com.ibm.watson.developer_cloud.service.security.IamOptions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ImageButton sendButton;
    private EditText userInput;
    //    private TextView conversationHistory;
    private IamOptions options;
    private Assistant service;
    private RecyclerView messagesRecyclerView;
    private MessageListAdapter messagesAdapter;
    private List<Message> messageList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initLayout();

        options = new IamOptions.Builder()
                .apiKey(getString(R.string.api_key))
                .build();
        service = new Assistant("2018-08-05", options);
        service.setEndPoint("https://gateway-wdc.watsonplatform.net/assistant/api");
    }

    private void initLayout() {
        sendButton = findViewById(R.id.send_button);
        sendButton.setOnClickListener(this);
//        conversationHistory = findViewById(R.id.conversationHistory);
        userInput = findViewById(R.id.user_input);
        initRecyclerView();
    }

    private void initRecyclerView() {
        messageList = new ArrayList<>();
        messagesRecyclerView = findViewById(R.id.reyclerview_message_list);
        messagesAdapter = new MessageListAdapter(this, messageList);
        messagesRecyclerView.setAdapter(messagesAdapter);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send_button:
                String userMsg = userInput.getText().toString();
//                conversationHistory.append(
//                        Html.fromHtml("<p><b>Me:</b> " + userMsg + "</p>")
//                );
                messagesAdapter.addItem(new Message(userMsg, "User", System.currentTimeMillis()));

                userInput.setText("");
                sendRequest(userMsg);
                break;
        }
    }

    private void sendRequest(final String inputText) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String workspaceId = getString(R.string.workspace_id);
                InputData input = new InputData.Builder(inputText).build();
                final MessageOptions options = new MessageOptions.Builder(workspaceId)
                        .input(input)
                        .build();
                final String[] botResponse = {""};

                service.message(options)
                        .enqueue(new ServiceCallback<MessageResponse>() {
                            @Override
                            public void onResponse(MessageResponse response) {
                                botResponse[0] = response.getOutput().get("text")
                                        .toString().replace('[', ' ').replace(']', ' ');

                                Log.d(TAG, "onResponse: Here");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        messagesAdapter.addItem(new Message(botResponse[0], getString(R.string.app_name), System.currentTimeMillis()));
                                    }
                                });
                                Log.d(TAG, "onResponse: There");
                                Log.d("Bot response ---> ", botResponse[0]);
//                                conversationHistory.append(
//                                        Html.fromHtml("<p><b>BroBot:</b> " +
//                                                botResponse[0] + "</br>" + "</p>") );
                            }

                            @Override
                            public void onFailure(Exception e) {
                                e.printStackTrace();
                            }
                        });
            }
        };

        Thread networkThread = new Thread(runnable);
        networkThread.start();
    }
}
