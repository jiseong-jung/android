package com.example.study_1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class member_join extends AppCompatActivity {

    TextView tv_name, tv_phone, tv_id, tv_pw;
    TextView tv_log;
    Button btn_submit;

    Customer customer = new Customer();     //customer object create
    Handler handler = new Handler();        //handler


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_join);
        setTitle("Sing up");

        tv_name = (TextView) findViewById(R.id.et_name);
        tv_phone = (TextView) findViewById(R.id.et_phone);
        tv_id = (TextView) findViewById(R.id.et_id);
        tv_pw = (TextView) findViewById(R.id.et_pw);
        btn_submit = (Button)findViewById(R.id.submit);

        tv_log = (TextView) findViewById(R.id.tv_log);

        btn_submit.setOnClickListener(listener_btn);

    }

    View.OnClickListener listener_btn = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            customer.setName(tv_name.getText().toString());
            customer.setPhone(Integer.parseInt(tv_phone.getText().toString()));
            customer.setName(tv_id.getText().toString());
            customer.setName(tv_pw.getText().toString());




        }
    };

    public void send(String data){


        try {
            int portNumber = 8080;
            //String ip_address = "127.0.0.1";
            String ip_address = "localhost";

            Socket sock = new Socket(ip_address, portNumber);
            printClientLog("socket connection");

            //DataOutputStream outputStream = new DataOutputStream(sock.getInputStream());





        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void printClientLog(final String data) { 
        Log.d("MainActivity", data);


        handler.post(new Runnable() { 
            @Override
            public void run() {
                tv_log.append(data + "\n");
            }
        });

    }


}











