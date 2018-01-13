package com.example.cslab.future.question;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cslab.future.R;
import com.example.cslab.future.activity.RegisterActivity;
import com.example.cslab.future.util.SysApplication;

/**
 * Created by CSLab on 2017/7/15.
 */

public class question5_activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_5);
        SysApplication.getInstance().addActivity(this);

        Button button_submit = (Button)findViewById(R.id.submit);
        Button button_back = (Button)findViewById(R.id.back);

        TextView name = (TextView)findViewById(R.id.textView);
        Intent intent = getIntent();
        final question_transmit p = (question_transmit) intent.getSerializableExtra("person");
        name.setText("那你的梦想是什么呢? 注意哦,我会永远记住你现在说下的话的,认真思考后再回答我吧(^-^)");

        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText dream = (EditText)findViewById(R.id.editText);
                if(!dream.getText().toString().equals("")) {
                    p.setDream(dream.getText().toString());
                    Bundle data = new Bundle();
                    data.putSerializable("person", p);

                    Intent q2_intent = new Intent(question5_activity.this, RegisterActivity.class);
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

                Intent q2b_intent = new Intent(question5_activity.this,question4_activity.class);
                q2b_intent.putExtras(data);
                startActivity(q2b_intent);
            }
        });
    }

}
