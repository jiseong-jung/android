package com.example.worldcup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int long_1 = 0;

    ImageView img1, img2;
    TextView tv_round;
    Button btn_start;
    
    int img_count1 =0;       //처음부터 시작
    int img_count2 =4;       //절반부터 시작


    make_8 make;        //8강 만들어주는 클래스
    //make.index;
    ArrayList<Integer> winner = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        make = new make_8();
        img1 = (ImageButton)findViewById(R.id.img1);
        img2 = (ImageButton)findViewById(R.id.img2);
        tv_round = (TextView)findViewById(R.id.round);
        btn_start = (Button)findViewById(R.id.btn_start);

        make.make_init();
        make.shuffle(make.index);
        

        btn_start.setOnClickListener(start_listener);   //게임 시작 버튼

    }

    //버튼 시작
    View.OnClickListener start_listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            img1.setBackgroundResource(make.index.get(img_count1));
            img2.setBackgroundResource(make.index.get(img_count2));

            img1.setVisibility(View.VISIBLE);
            img2.setVisibility(View.VISIBLE);

        }
    };


    //이미지 두개중 한개를 클릭하였을 때
    public void on_Image_Click(View view) {
        if(view.getId() == R.id.img1){

            winner.add(make.index.get(img_count1));
            Log.i("jiseong", "winner 1 lenght"+ winner.size());


            Log.i("jiseong", long_1+"");
            next_game();

        }
        else if(view.getId() == R.id.img2){

            winner.add(make.index.get(img_count2));
            Log.i("jiseong", "winner 2 lenght"+ winner.size());

            Log.i("jiseong", long_1+"");
            next_game();
        }
        else    //예외처리
            Log.i("jiseong","error");
    }
    
    //4강, 결승전 처리
    void next_game(){
        long_1++;
        //4강
        if(long_1 == 4){
            Log.i("jiseong", "4강전 size:"+ winner.size());
            tv_round.setText(4+"강입니다.");
            make.index = (ArrayList<Integer>) winner.clone();
            winner.removeAll(make.index);
            img_count1 =-1;
            img_count2 =1;
        }

        else if(long_1 == 6){
            Log.i("jiseong", "결승전 size:"+ winner.size());
            tv_round.setText("결승입니다.");
            make.index = (ArrayList<Integer>) winner.clone();
            winner.removeAll(make.index);
            img_count1 =-1;
            img_count2 =0;

        }

        else if(long_1 == 7){
            Log.i("jiseong", "final size size:"+ winner.size());
            tv_round.setText("최종승자");
            img1.setBackgroundResource(winner.get(0));
            img2.setBackgroundResource(winner.get(0));
            return;
        }

        else if(long_1 >= 8){
            Log.i("jiseong","Err");

            AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
            dlg.setTitle("월드컵이 끝났습니다.");
            dlg.setMessage("다시 하시겠습니까?");
            dlg.setIcon(winner.get(0));
            dlg.show();


            return;
        }


        img1.setBackgroundResource(make.index.get(++img_count1));
        img2.setBackgroundResource(make.index.get(++img_count2));
        return;
    }
}

