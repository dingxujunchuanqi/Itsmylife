package com.sinoautodiagnoseos.propeller.ui.specialProgressBar;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.app.AppContext;

import static com.sinoautodiagnoseos.R.id.et_content;

/**
 * Created by 惊吓了时光 on 2017/5/20.
 */

public class PetDiaLog extends AlertDialog implements View.OnClickListener {
int type;
    private OnEditInputFinishedListener mListener; //接口
    private EditText etContent;

    public interface OnEditInputFinishedListener{
        void editInputFinished(String str);
    }

    public PetDiaLog(Context context, OnEditInputFinishedListener mListener, int style, int type) {

        super(context, style);
        this.type=type;
        this.mListener = mListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.pesonrinfo_alert_dialog);
        setView(LayoutInflater.from(AppContext.getInstance())
                .inflate(R.layout.pesonrinfo_alert_dialog, null));
        getWindow().setContentView(R.layout.pesonrinfo_alert_dialog);//设置dialog的位置

        Button btnCommit = (Button) findViewById(R.id.commit_but);
        Button btnNegative = (Button)findViewById(R.id.btn_cancel);
        etContent = (EditText) findViewById(et_content);
        btnCommit.setOnClickListener(this);
        btnNegative.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()) {
            case R.id.commit_but:
                String str = etContent.getText().toString();
                if (isNullEmptyBlank(str)) {
                    etContent.setError("输入框内容不能为空");
            }else {
                    mListener.editInputFinished(str);
                   if(type==1) {
                       Toast.makeText(AppContext.getInstance(), "修改成功", Toast.LENGTH_LONG).show();
                       dismiss();
                   }else
                    Toast.makeText(AppContext.getInstance(), "设置成功", Toast.LENGTH_LONG).show();
                    dismiss();
                }
                break;
            case R.id.btn_cancel:
               dismiss();
                break;
            default:
                break;
        }
    }
    /**
     *输入框内容不能为空的方法
     *@author dingxujun
     *created at 2017/5/10 16:24
     */
    public static boolean isNullEmptyBlank(String str) {
        if (str == null || "".equals(str) || "".equals(str.trim()))
            return true;
        return false;
    }
}

