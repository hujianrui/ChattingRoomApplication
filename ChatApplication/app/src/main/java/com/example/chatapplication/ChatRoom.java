package com.example.chatapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ChatRoom extends AppCompatActivity {

    private List<String> chatHis;
    private ArrayAdapter<String> adapter;
    private ListView chatHisDP;
    private WebSocket ws = null;
    private String user;
    private String room;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        preSet();


        try {
            ws = new WebSocketFactory().createSocket("ws://10.0.2.2:8080");
            ws.addListener(new WebSocketAdapter() {

                @Override
                public void onConnected(WebSocket websocket, Map<String, List<String>> headers) {
                    ws.sendText("join " + room);
                    System.out.println("on web socket");
                }

                @Override
                public void onFrame(WebSocket websocket, WebSocketFrame frame) {
                    addToList(frame.getPayloadText());
                    System.out.println("on frame");
                }
            });
            ws.connectAsynchronously();
            System.out.println("Connecting to Server");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    private void addToList(String message){
        JsonParser jsonPar = new JsonParser();
        JsonElement jsonEle = jsonPar.parse(message);
        JsonObject jsonObj = jsonEle.getAsJsonObject();

        String userName = jsonObj.get("user").getAsString();
        String sendMsg = jsonObj.get("message").getAsString();

        if(!sendMsg.equals("")) {
            chatHis.add(userName + ": " + sendMsg);
            adapter.notifyDataSetChanged();
        }

        chatHisDP.post(
                new Runnable(){
                    @Override
                    public void run(){
                        chatHisDP.setSelection(adapter.getCount() - 1);
                    }
                }
        );
    }


    public void preSet(){
        Intent intent = getIntent();
        user = intent.getStringExtra("User");
        room = intent.getStringExtra("Room");

        TextView nameTextView = findViewById(R.id.nameTextView);
        nameTextView.setText("User: "+user);
        TextView roomTextView = findViewById(R.id.roomTextView);
        roomTextView.setText("Room: "+room);

        chatHisDP = findViewById(R.id.chatHistoryList);
        chatHis = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, chatHis);
        chatHisDP.setAdapter(adapter);
    }


    public void send(View v) {
        EditText message = findViewById(R.id.messageEditText);
        String msg = message.getText().toString();
        ws.sendText(user + " " + msg);
        message.getText().clear();
    }

    public void exit(View v) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

}
