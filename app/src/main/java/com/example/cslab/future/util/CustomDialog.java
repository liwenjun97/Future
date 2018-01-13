package com.example.cslab.future.util;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cslab.future.R;


/**
 * Created by CSLab on 2017/7/21.
 */

public class CustomDialog extends Dialog {
    Activity context;
    private Button submit;
    public EditText mark;
    public TextView textcount;
    private View.OnClickListener mClickListener;

    public CustomDialog(Activity context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.plan_mark);
        mark = (EditText) findViewById(R.id.mark_plan);
        textcount=(TextView)findViewById(R.id.textcount);
        mark.addTextChangedListener(mTextWatcher);
        textcount.setText(String.valueOf(mark.getText().toString().length()));
        submit = (Button) findViewById(R.id.submit_plan);
        Window dialogWindow = this.getWindow();
        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.8
        dialogWindow.setAttributes(p);
        this.setCancelable(true);
    }

    public void setOnClickListener(View.OnClickListener clickListener){
        submit.setOnClickListener(clickListener);
    }
    public String getText(){
        return mark.getText().toString();
    }
    TextWatcher mTextWatcher=new TextWatcher() {
        private CharSequence temp;
        private int editstart;
        private int editend;
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            temp=charSequence;
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            editstart=mark.getSelectionStart();
            editend=mark.getSelectionEnd();
            textcount.setText(String.valueOf(mark.getText().toString().length()));
        }
    };
}
