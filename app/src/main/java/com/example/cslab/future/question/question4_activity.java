package com.example.cslab.future.question;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cslab.future.R;
import com.example.cslab.future.util.SysApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CSLab on 2017/7/15.
 */

public class question4_activity extends AppCompatActivity {
    private CheckBox checkBox1,checkBox2,checkBox3,checkBox4,checkBox5,checkBox6,checkBox7,checkBox8;
    private List<CheckBox> checkBoxList = new ArrayList<CheckBox>();
    int word = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_4);
        SysApplication.getInstance().addActivity(this);

        checkBox1 = (CheckBox)findViewById(R.id.ist_1);
        checkBox2 = (CheckBox)findViewById(R.id.ist_2);
        checkBox3 = (CheckBox)findViewById(R.id.ist_3);
        checkBox4 = (CheckBox)findViewById(R.id.ist_4);
        checkBox5 = (CheckBox)findViewById(R.id.ist_5);
        checkBox6 = (CheckBox)findViewById(R.id.ist_6);
        checkBox7 = (CheckBox)findViewById(R.id.ist_7);
        checkBox8 = (CheckBox)findViewById(R.id.ist_8);
        Button button_submit = (Button)findViewById(R.id.submit);
        Button button_back = (Button)findViewById(R.id.back);

        checkBoxList.add(checkBox1);
        checkBoxList.add(checkBox2);
        checkBoxList.add(checkBox3);
        checkBoxList.add(checkBox4);
        checkBoxList.add(checkBox5);
        checkBoxList.add(checkBox6);
        checkBoxList.add(checkBox7);
        checkBoxList.add(checkBox8);
        TextView name = (TextView)findViewById(R.id.textView);
        Intent intent = getIntent();
        final question_transmit p = (question_transmit) intent.getSerializableExtra("person");
        name.setText("那么,你有什么兴趣爱好呢?");

        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (CheckBox checkbox : checkBoxList) {
                    if (checkbox.isChecked()){
                        if(p.getInterest()==null){
                            p.setInterest(checkbox.getText().toString());
                        }
                        else {
                            word = 1;
                            p.setInterest(p.getInterest() + "-" + checkbox.getText().toString());
                        }
                    }
//                    Toast tost =Toast.makeText(question4_activity.this,"252555",Toast.LENGTH_LONG);
//                    tost.show();
                }



                if(word==1) {
                    Bundle data = new Bundle();
                    data.putSerializable("person", p);

                    Intent q2_intent = new Intent(question4_activity.this, question5_activity.class);
                    q2_intent.putExtras(data);
                    startActivity(q2_intent);
                }
            }
        });

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle data = new Bundle();
                data.putSerializable("person",p);

                Intent q2b_intent = new Intent(question4_activity.this,question3_activity.class);
                q2b_intent.putExtras(data);
                startActivity(q2b_intent);
            }
        });
    }

}