package com.example.cslab.future.question;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.cslab.future.R;
import com.example.cslab.future.util.SysApplication;

/**
 * Created by CSLab on 2017/7/14.
 */

public class question3_activity extends AppCompatActivity {
    private Spinner spin_y;
    private Spinner spin_m;
    private Spinner spin_s;
    private ArrayAdapter adapter;
    private  String[] year = new String[]{"1985","1986","1987","1988",
            "1989","1990","1991","1992","1993","1994","1995","1996",
            "1997","1998","1999","2000","2001","2002","2003","2004",};
    private String[] month = new String[]{"01","02","03","04","05","06","07","08","09","10","11","12",};
    private String[] sex = new String[]{"男生","女生","其他"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_3);
        SysApplication.getInstance().addActivity(this);

        spin_y = (Spinner)findViewById(R.id.year);
        spin_m = (Spinner)findViewById(R.id.month);
        spin_s = (Spinner)findViewById(R.id.sex);
        Button button_submit = (Button)findViewById(R.id.submit);
        Button button_back = (Button)findViewById(R.id.back);

        TextView name = (TextView)findViewById(R.id.textView);
        Intent intent = getIntent();
        final question_transmit p = (question_transmit) intent.getSerializableExtra("person");
        name.setText("在" + p.getCity() + "的" + p.getName() + " ， 你是一个阳光帅气的男子汉，还是一个招人喜欢的萌妹子。");

        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                p.setBirth(spin_y.getSelectedItem().toString() + "-" + spin_m.getSelectedItem().toString() + "-01");
                p.setSex(spin_s.getSelectedItem().toString());
                Bundle data = new Bundle();
                data.putSerializable("person",p);

                Intent q2_intent = new Intent(question3_activity.this,question4_activity.class);
                q2_intent.putExtras(data);
                startActivity(q2_intent);
            }
        });

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                p.setBirth(spin_y.getSelectedItem().toString() + "-" + spin_m.getSelectedItem().toString());
                p.setSex(spin_s.getSelectedItem().toString());
                Bundle data = new Bundle();
                data.putSerializable("person",p);

                Intent q2b_intent = new Intent(question3_activity.this,question2_activity.class);
                q2b_intent.putExtras(data);
                startActivity(q2b_intent);
            }
        });
        adapter = new ArrayAdapter(this,R.layout.spinner_item_1,year);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spin_y.setAdapter(adapter);

        adapter = new ArrayAdapter(this,R.layout.spinner_item_1,month);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spin_m.setAdapter(adapter);

        adapter = new ArrayAdapter(this,R.layout.spinner_item_1,sex);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spin_s.setAdapter(adapter);
    }
}
