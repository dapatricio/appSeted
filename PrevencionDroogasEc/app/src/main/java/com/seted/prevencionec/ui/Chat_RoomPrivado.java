/*
 * Created by Daniel Peñarreta.
 * Copyright (c) 2016.  All rights reserved.
 */

package com.seted.prevencionec.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.seted.prevencionec.R;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by dapat on 14/1/2017.
 */

public class Chat_RoomPrivado extends AppCompatActivity {

    private String user_name, room_name, usuario;
    private String chat_msg, chat_user_name;
    private DatabaseReference root;
    private String temp_key;

    private TextView textoEscrito;
    private ImageButton btnEnvio;
    private EditText txtChat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_roomprivado);

        Bundle b = getIntent().getExtras();
        user_name = b.getString("nombreChat");
        usuario = b.getString("nombrePer");

        textoEscrito = (TextView)findViewById(R.id.textoEscri);
        btnEnvio = (ImageButton)findViewById(R.id.btnsend);
        txtChat =(EditText)findViewById(R.id.txtChat);

        room_name = user_name;

        root = FirebaseDatabase.getInstance().getReference().child(room_name);

        Map<String,Object> map = new HashMap<String, Object>();
        temp_key = root.push().getKey();
        root.updateChildren(map);

        DatabaseReference message_root = root.child(temp_key);
        Map<String,Object> map2 = new HashMap<String, Object>();
        map2.put("name", ":");
        map2.put("msg", "\nHola "+usuario+".\nBienvenido a nuestro Chat en Linea.\nRecuerda que te atenderemos en horario Lunes a Viernes de 08:30am a 17:30pm.");

        message_root.updateChildren(map2);

        btnEnvio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String,Object> map = new HashMap<String, Object>();
                temp_key = root.push().getKey();
                root.updateChildren(map);

                DatabaseReference message_root = root.child(temp_key);
                Map<String,Object> map2 = new HashMap<String, Object>();
                map2.put("name", user_name);
                map2.put("msg", txtChat.getText().toString());

                message_root.updateChildren(map2);
                txtChat.setText("");
            }
        });

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                append_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                append_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
            });

        ImageButton btn = (ImageButton)findViewById(R.id.btnHome);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
    private void append_chat_conversation(DataSnapshot dataSnapshot) {

        Iterator i = dataSnapshot.getChildren().iterator();

        while (i.hasNext()){

            chat_msg = (String) ((DataSnapshot)i.next()).getValue();
            chat_user_name = (String) ((DataSnapshot)i.next()).getValue();
            textoEscrito.append(chat_user_name+ " : "+ chat_msg+" \n");
            if (chat_user_name.equals("seted"+"@gmail.com")){
                AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
                dialogo.setMessage("entro en el mensaje");
                dialogo.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialogChat = dialogo.create();
                dialogChat.show();
            };
        }
    }
}
