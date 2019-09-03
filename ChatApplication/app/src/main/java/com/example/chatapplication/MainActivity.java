package com.example.chatapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void join(View v){
        Intent intent = new Intent(getApplicationContext(),ChatRoom.class);
        EditText userName= findViewById(R.id.nameTextEdit);
        EditText roomName= findViewById(R.id.roomTextEdit);
        String user = userName.getText().toString();
        String room = roomName.getText().toString();
        intent.putExtra("User", user);
        intent.putExtra("Room", room);
        startActivity(intent);
        }

}




