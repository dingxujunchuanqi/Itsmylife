package com.sinoautodiagnoseos.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.app.AppContext;
import com.sinoautodiagnoseos.entity.CarBrands.CarBrands;
import com.sinoautodiagnoseos.entity.User.Skill;
import com.sinoautodiagnoseos.openvcall.ui.ExpertsActivity;
import com.sinoautodiagnoseos.propeller.ui.specialProgressBar.PetDiaLog;
import com.sinoautodiagnoseos.ui.UIHelper;
import com.sinoautodiagnoseos.ui.loginui.SwipeBackActivity;
import com.sinoautodiagnoseos.ui.personinfoui.ClipImageActivity;
import com.sinoautodiagnoseos.ui.personinfoui.HeadPortraitUtils;
import com.sinoautodiagnoseos.utils.Constant;
import com.sinoautodiagnoseos.utils.OnMultiClickListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.R.attr.type;
import static com.sinoautodiagnoseos.R.id.shop_place_rl;
import static com.sinoautodiagnoseos.ui.personinfoui.HeadPortraitUtils.gotoClipActivity;
import static com.sinoautodiagnoseos.ui.personinfoui.HeadPortraitUtils.tempFile;
import static com.sinoautodiagnoseos.utils.Constant.READ_EXTERNAL_STORAGE_REQUEST_CODE;
import static com.sinoautodiagnoseos.utils.Constant.REQUEST_CAPTURE;
import static com.sinoautodiagnoseos.utils.LogUtils.deBug;

/**
 * 专家认证界面
 * Created by dingxujun on 2017/5/21.
 */

public class ExpertsCertificationActivity extends SwipeBackActivity implements View.OnClickListener {

    private LinearLayout technology_info;
    private String TAG = this.getClass().getName();
    private FrameLayout identity_fl;
    private String cropImagePath1, cropImagePath2, cropImagePath3,
            imageNme1, imageNme2, imageNme3;
    private ImageView identityimage;
    private RelativeLayout shop_place;
    private ImageView idcontryimg;
    private RelativeLayout yearsworking;
    private static final int DATE_DIALOG = 1;
    private int mYear, mMonth, mDay;
    private TextView yearsworktv;
    private FrameLayout credentialfl;
    private RelativeLayout typeWorkRl;
    private GridView gridView;
    private Dialog typewkdialog;
    private TextView techcertifitv;
    private FrameLayout back_click;
    private ImageView setImage;
    private TextView setname;
    private RelativeLayout set_namerl;
    private Skill skill;
    private Button commitCheckbt;
    private String typeWorkId;
    private String str;
    private String yearw;
    private ImageView identitycredentiaimg;
    private TextView storetv;
    private TextView type_workTV1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technician_certification);
        initView();
        initListenerOclick();
        initDialog();
      HeadPortraitUtils.createCameraTempFile(savedInstanceState);
    }

    /*
   * 初始化控件
   * */
    private void initView() {
        technology_info = (LinearLayout) findViewById(R.id.technology_info);
        technology_info.setVisibility(View.VISIBLE);
        identity_fl = (FrameLayout) findViewById(R.id.identity_fl);
        identityimage = (ImageView) findViewById(R.id.identity_frontage_img);
        shop_place = (RelativeLayout) findViewById(shop_place_rl);
        idcontryimg = (ImageView) findViewById(R.id.identity_contrary_img);
        yearsworking = (RelativeLayout) findViewById(R.id.years_of_working);
        yearsworktv = (TextView) findViewById(R.id.yeardata);//工作年限
        credentialfl = (FrameLayout) findViewById(R.id.credential_fl);
        typeWorkRl = (RelativeLayout) findViewById(R.id.type_work_rl);
        techcertifitv = (TextView) findViewById(R.id.person_tv);
        techcertifitv.setText(R.string.Microsoft_Office_User_Specialist);
        back_click = (FrameLayout) findViewById(R.id.back_click);
        setImage = (ImageView) findViewById(R.id.setting_image);
        setImage.setVisibility(View.GONE);
        setname = (TextView) findViewById(R.id.setname);
        set_namerl = (RelativeLayout) findViewById(R.id.set_namerl);
        commitCheckbt = (Button) findViewById(R.id.commit_check_button);
        identitycredentiaimg = (ImageView) findViewById(R.id.identity_credentia_img);
        storetv = (TextView) findViewById(R.id.store_tv);
        type_workTV1 = (TextView) findViewById(R.id.type_workTV1);
    }
    /*
    * 点击监听事件
    *
    * */
    private int w;

    private void initListenerOclick() {
        set_namerl.setOnClickListener(this);
        back_click.setOnClickListener(this);
        yearsworking.setOnClickListener(this);
        shop_place.setOnClickListener(this);
        technology_info.setOnClickListener(this);
        identity_fl.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                w = 1;
                HeadPortraitUtils.uploadHeadImage(ExpertsCertificationActivity.this);
            }
        });
        idcontryimg.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                w = 2;
                HeadPortraitUtils.uploadHeadImage(ExpertsCertificationActivity.this);
            }
        });
        credentialfl.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                w = 3;
                HeadPortraitUtils.uploadHeadImage(ExpertsCertificationActivity.this);
            }
        });

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("tempFile", tempFile);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.set_namerl:
                showSubmitAlertDialog();
                break;
            case R.id.years_of_working:
                showDialog(DATE_DIALOG);
                break;
            case R.id.shop_place_rl:
                UIHelper.showSelectStore(ExpertsCertificationActivity.this);
                break;
            case R.id.back_click:
                finish();
                break;
            case R.id.technology_info:
                UIHelper.showSkillManagement(this);
            default:
                break;
        }
    }

    /**
     * 初始化dialog常量
     *
     * @author dingxujun
     * created at 2017/5/11 14:07
     */
    private void initDialog() {
        final Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
        deBug(TAG, "\\\\\\\\\\\\\\\\\\\\\\\\" + mYear);
        deBug(TAG, "\\\\\\\\\\\\\\\\\\\\\\\\" + mMonth);
        deBug(TAG, "\\\\\\\\\\\\\\\\\\\\\\\\" + mDay);
    }

    /*
    * 重写Activity的创建dialog的方法
    * */
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, mdateListener, mYear, mMonth, mDay);
        }
        return null;
    }

    /*
 *
 * 日期dialog的接口监听
 *
 * */
    private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            display();
        }
    };

    /**
     * 设置日期 利用StringBuffer追加
     *
     * @author dingxujun
     * created at 2017/5/11 14:11
     */
    public void display() {
        yearsworktv.setText(new StringBuffer().append(mYear).append("-").append(mMonth + 1).append("-").append(mDay).append(" "));
        yearw = yearsworktv.getText().toString().trim();
        System.out.println("============================" + yearw);
    }

    /**
     * 设置姓名的方法 dialog
     * @author dingxujun
     * created at 2017/5/10 16:19
     */
    private void showSubmitAlertDialog() {
        final PetDiaLog petDiaLog = new PetDiaLog(this, new PetDiaLog.OnEditInputFinishedListener() {
            @Override
            public void editInputFinished(String str) {
                ExpertsCertificationActivity.this.str = str;
                setname.setText(str);
            }
        }, R.style.DialogTheme, 2);
        petDiaLog.setView(new EditText(this));
        petDiaLog.show();
    }

    /**
     * 上传图片的请求封装
     * @param imageUri
     * @return
     */
    public MultipartBody upImage(String imageUri) {
        System.out.println("11111====" + imageUri);
        File identityBackfFile = new File(String.valueOf(imageUri));
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), identityBackfFile);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.addFormDataPart("image", identityBackfFile.getName(), requestFile);
        builder.addFormDataPart("title", "image");
        builder.addFormDataPart("alt", "image");
        builder.setType(MultipartBody.FORM);
        MultipartBody multipartBody = builder.build();
        return multipartBody;
    }

    /**
     * 获取新的Activity返回过来的数据
     * @author dingxujun
     * created at 2017/5/9 16:54
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case Constant.REQUEST_CAPTURE: //调用系统相机返回
                if (resultCode == RESULT_OK) {
                    HeadPortraitUtils.gotoClipActivity(Uri.fromFile(tempFile),ExpertsCertificationActivity.this);
                }
                break;
            case Constant.REQUEST_PICK:  //调用系统相册返回
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    HeadPortraitUtils.gotoClipActivity(uri,ExpertsCertificationActivity.this);
                }
                break;
            case Constant.REQUEST_CROP_PHOTO:  //剪切图片返回
                if (resultCode == RESULT_OK) {
                    final Uri uri = intent.getData();
                    if (uri == null) {
                        return;
                    }
                    switch (w) {//根据标记取判断是谁选择的图片
                        case 0:
                            break;
                        case 1:
                            //正面
                            cropImagePath1 = HeadPortraitUtils.getRealFilePathFromUri(ExpertsCertificationActivity.this, uri);//图片的路径
                            imageNme1 = cropImagePath1.substring(cropImagePath1.lastIndexOf("/") + 1);
                            Bitmap bitMap1 = BitmapFactory.decodeFile(cropImagePath1);//将bitmap文件转换为bitp对象
                            identityimage.setImageBitmap(bitMap1);
                            break;
                        case 2:
                            //反面
                            cropImagePath2 = HeadPortraitUtils.getRealFilePathFromUri(getApplicationContext(), uri);//图片的路径
                            imageNme2 = cropImagePath2.substring(cropImagePath2.lastIndexOf("/") + 1);
                            Bitmap bitMap2 = BitmapFactory.decodeFile(cropImagePath2);//将bitmap文件转换为bitp对象
                            idcontryimg.setImageBitmap(bitMap2);
                            break;
                        case 3:
                            //证书
                            cropImagePath3 = HeadPortraitUtils.getRealFilePathFromUri(getApplicationContext(), uri);//图片的路径
                            imageNme3 = cropImagePath3.substring(cropImagePath3.lastIndexOf("/") + 1);
                            Bitmap bitMap3 = BitmapFactory.decodeFile(cropImagePath3);//将bitmap文件转换为bitp对象
                            identitycredentiaimg.setImageBitmap(bitMap3);
                            break;
                    }

                    //将bitmap文件转换为bitmap对象
                    //此处后面可以将bitMap转为二进制上传后台网络

                    //  byte[] mbitmapByte = UpBitmapUtils.getBitmapByte(bitMap);
                    //uploadBitmap();
                    //   updateUserBaseData(1, "", cropImagePath);
                }
                break;
            case Constant.REQUEST_CODE_106://那边传过来的门店条目数据
                if (intent!=null) {
                    String storeType = intent.getStringExtra(SelectStoresActivity.TYPE_WORK);
                    //数据回显
                    storetv.setText(storeType);
                }
                break;
            default:
                break;
        }
    }
    private String storesId;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receive(String storesId) {
        this.storesId = storesId;
        System.out.println("===========我是UserId" + storesId);
        //   PicassoUtils.loadImageView(this, s, image_user);
    }
    /**
     * 检查文件是否存在
     *
     * @author dingxujun
     * created at 2017/5/9 15:07
     */
    private String checkDirPath(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return "";
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dirPath;
    }
}