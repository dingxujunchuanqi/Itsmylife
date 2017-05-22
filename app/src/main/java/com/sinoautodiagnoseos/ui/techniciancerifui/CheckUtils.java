package com.sinoautodiagnoseos.ui.techniciancerifui;

import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.app.AppContext;
import com.sinoautodiagnoseos.utils.ToastUtils;

/**
 * Created by dingxujun on 2017/5/19.
 */

public class CheckUtils {
    /**
     *提交之前要进行数据检查
     *@author dingxujun
     *created at 2017/5/19 18:30
     */
    public static boolean checkCommit( String identityBackUrl, String identityFrontUrl,
                                 String fileUrl,String realName,String skill, String station,  String workYears ){
        if (identityBackUrl==null||identityFrontUrl==null||fileUrl==null
                ||realName==null||skill==null||station==null||workYears==null){
            ToastUtils.showShort(AppContext.getInstance(), R.string.the_information_should_be_complete);
            return false;
        }else {
            ToastUtils.showShort(AppContext.getInstance(),R.string.Congratulations_on_your_success);
            return true;
        }
    }

}
