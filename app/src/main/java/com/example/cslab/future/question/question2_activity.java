package com.example.cslab.future.question;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cslab.future.R;
import com.example.cslab.future.util.SysApplication;

import static android.R.attr.data;

/**
 * Created by CSLab on 2017/7/14.
 */

public class question2_activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_2);
        SysApplication.getInstance().addActivity(this);

        Button button_submit = (Button)findViewById(R.id.submit);
        Button button_back = (Button)findViewById(R.id.back);

        TextView name = (TextView)findViewById(R.id.textView);
        Intent intent = getIntent();
        final question_transmit p = (question_transmit) intent.getSerializableExtra("person");
        name.setText(p.getName() + "你好，我现在在世界树的顶端，你在哪？");

        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText city = (EditText) findViewById(R.id.editText);
                if(!city.getText().toString().equals("")) {
                    p.setCity(city.getText().toString());
                    Bundle data = new Bundle();
                    data.putSerializable("person", p);

                    Intent q2_intent = new Intent(question2_activity.this,question3_activity.class);
                    q2_intent.putExtras(data);
                    startActivity(q2_intent);
                }
            }
        });

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText city = (EditText)findViewById(R.id.editText);
                p.setCity(city.getText().toString());
                Bundle data = new Bundle();
                data.putSerializable("person",p);

                Intent q2b_intent = new Intent(question2_activity.this,question1_activity.class);
                q2b_intent.putExtras(data);
                startActivity(q2b_intent);
            }
        });
    }

}
