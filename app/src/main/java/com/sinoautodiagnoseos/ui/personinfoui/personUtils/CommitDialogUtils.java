package com.sinoautodiagnoseos.ui.personinfoui.personUtils;

import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.activity.PersonalInfoActivity;
import com.sinoautodiagnoseos.app.AppContext;
import com.sinoautodiagnoseos.utils.SharedPreferences;

/**
 * Created by dingxujun on 2017/5/19.
 */

public class CommitDialogUtils {
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
