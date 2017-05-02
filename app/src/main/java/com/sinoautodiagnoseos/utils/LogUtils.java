package com.sinoautodiagnoseos.utils;

import android.util.Log;

/**
 *
 * 	log帮助程序员分析代码,解决bug.

 1. 多人开发,如何屏蔽其他人的log

 2. 上线之后,如何屏蔽
 */
public class LogUtils {
    //开关
    public static boolean isDebug = true; // 开发阶段true,可以看到log ,上线阶段false

    public static void info(String tag ,String msg){
        if(isDebug){
            Log.i(tag, "----------------"+msg);
        }
    }
    public static void deBug(String tag ,String msg){
        if(isDebug){
            Log.d(tag, "++++++++++++++++++"+msg);
        }
    }

}
