package com.sinoautodiagnoseos.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.entity.ExceptionBean.ErrMessage;
import com.sinoautodiagnoseos.entity.Upload.TechnicianAuth;
import com.sinoautodiagnoseos.entity.Upload.Upload;
import com.sinoautodiagnoseos.entity.User.Skill;
import com.sinoautodiagnoseos.entity.User.UserBaseData;
import com.sinoautodiagnoseos.net.requestApi.HttpRequestApi;
import com.sinoautodiagnoseos.net.requestSubscribers.HttpSubscriber;
import com.sinoautodiagnoseos.net.requestSubscribers.SubscriberOnListener;
import com.sinoautodiagnoseos.propeller.ui.specialProgressBar.PetDiaLog;
import com.sinoautodiagnoseos.ui.UIHelper;
import com.sinoautodiagnoseos.ui.adapter.TypeWorkAdpter;
import com.sinoautodiagnoseos.ui.loginui.SwipeBackActivity;
import com.sinoautodiagnoseos.ui.personinfoui.ClipImageActivity;
import com.sinoautodiagnoseos.ui.personinfoui.HeadPortraitUtils;
import com.sinoautodiagnoseos.ui.techniciancerifui.CheckUtils;
import com.sinoautodiagnoseos.utils.Constant;
import com.sinoautodiagnoseos.utils.OnMultiClickListener;
import com.sinoautodiagnoseos.utils.SharedPreferences;
import com.sinoautodiagnoseos.utils.ToastUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.io.File;
import java.util.Calendar;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import static android.R.attr.type;
import static com.sinoautodiagnoseos.R.id.shop_place_rl;
import static com.sinoautodiagnoseos.utils.Constant.READ_EXTERNAL_STORAGE_REQUEST_CODE;
import static com.sinoautodiagnoseos.utils.Constant.REQUEST_CAPTURE;
import static com.sinoautodiagnoseos.utils.LogUtils.deBug;

/**    技师认证界面
 * Created by dingxujun on 2017/5/15.
 */

public class TechnicianCertifActivity extends SwipeBackActivity implements View.OnClickListener {

    private String TAG = this.getClass().getName();
    private FrameLayout identity_fl;
    private String cropImagePath1, cropImagePath2, cropImagePath3,
            imageNme1, imageNme2, imageNme3;
    private File tempFile;
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
        getUserIfoData();
        initDialog();
        createCameraTempFile(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    /*
    * 获取那边PersonalInfoActivity传过来技师工种的数据
    *
    * */
    private void getUserIfoData() {
        techcertifitv.setText(R.string.A_technician_certification);
        setImage.setVisibility(View.GONE);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        skill = (Skill) bundle.getSerializable("skill");
        String text = skill.getData().get(1).getText();
        System.out.println("======我是工种====" + text);
    }

    /**
     * 初始化控件
     *
     * @author dingxujun
     * created at 2017/5/15 16:15
     */
    private void initView() {
        identity_fl = (FrameLayout) findViewById(R.id.identity_fl);
        identityimage = (ImageView) findViewById(R.id.identity_frontage_img);
        shop_place = (RelativeLayout) findViewById(shop_place_rl);
        idcontryimg = (ImageView) findViewById(R.id.identity_contrary_img);
        yearsworking = (RelativeLayout) findViewById(R.id.years_of_working);
        yearsworktv = (TextView) findViewById(R.id.yeardata);//工作年限
        credentialfl = (FrameLayout) findViewById(R.id.credential_fl);
        typeWorkRl = (RelativeLayout) findViewById(R.id.type_work_rl);
        typeWorkRl.setVisibility(View.VISIBLE);
        techcertifitv = (TextView) findViewById(R.id.person_tv);
        back_click = (FrameLayout) findViewById(R.id.back_click);
        setImage = (ImageView) findViewById(R.id.setting_image);
        setname = (TextView) findViewById(R.id.setname);
        set_namerl = (RelativeLayout) findViewById(R.id.set_namerl);
        commitCheckbt = (Button) findViewById(R.id.commit_check_button);
        identitycredentiaimg = (ImageView) findViewById(R.id.identity_credentia_img);
        storetv = (TextView) findViewById(R.id.store_tv);
        type_workTV1 = (TextView) findViewById(R.id.type_workTV1);



    }

    /*
    *
    * 技师工种请求接口
    * */
    private void typeWorkRequest() {
        Constant.TOKEN = SharedPreferences.getInstance().getString("checktoken", "");
        Constant.REGISTRATION = SharedPreferences.getInstance().getString("RegistrationId", "");
        HttpRequestApi.getInstance().getSkill(new HttpSubscriber<Skill>(new SubscriberOnListener<Skill>() {
            @Override
            public void onSucceed(final Skill data) {
                final List<Skill.DataBean> arraylist = data.getData();
                TypeWorkAdpter adpter = new TypeWorkAdpter(TechnicianCertifActivity.this, arraylist);
                gridView.setAdapter(adpter);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        String text = arraylist.get(position).getText();
                        ToastUtils.showShort(TechnicianCertifActivity.this, "你点击了" + text);
                        typewkdialog.dismiss();
                    }
                });
                System.out.println("成功" + Constant.RESPONSECODE + Constant.ERRMESSAGE);
            }

            @Override
            public void onError(int code, String msg) {
                System.out.println("失败" + Constant.RESPONSECODE + Constant.ERRMESSAGE);

            }
        }, TechnicianCertifActivity.this));
    }

    /**
     * 设置姓名的方法 dialog
     *
     * @author dingxujun
     * created at 2017/5/10 16:19
     */
    private void showSubmitAlertDialog() {
        final PetDiaLog petDiaLog = new PetDiaLog(this, new PetDiaLog.OnEditInputFinishedListener() {
            @Override
            public void editInputFinished(String str) {
                TechnicianCertifActivity.this.str=str;
                setname.setText(str);
            }
        }, R.style.DialogTheme,2);
        petDiaLog.setView(new EditText(this));
        petDiaLog.show();
    }
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
     * 点击事件
     *
     * @author dingxujun
     * created at 2017/5/15 16:17
     */
    int w = 0;//加标记

    private void initListenerOclick() {
        shop_place.setOnClickListener(this);
        yearsworking.setOnClickListener(this);
        typeWorkRl.setOnClickListener(this);
        set_namerl.setOnClickListener(this);
        back_click.setOnClickListener(this);
        identity_fl.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                w = 1;
                uploadHeadImage();
            }
        });
        idcontryimg.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                w = 2;
                uploadHeadImage();
            }
        });
        credentialfl.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                w = 3;
                uploadHeadImage();
            }
        });
        commitCheckbt.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                System.out.println("---------我是上传技师参数-----" + cropImagePath1 + "\n" + cropImagePath2 +
                        "\n" + cropImagePath3 + "\n" + str + "\n" + typeWorkId + "\n" + storesId + "\n" + yearw);
                if (CheckUtils.checkCommit(cropImagePath1, cropImagePath2, cropImagePath3, str, typeWorkId, storesId, yearw)) {
                    technicianAuth(cropImagePath1, cropImagePath2, cropImagePath3, str, typeWorkId, storesId, yearw);
                }
            }
        });
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

    /**
     * 上传头像的方法
     *
     * @author dingxujun
     * created at 2017/5/9 14:21
     */
    private void uploadHeadImage() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_popupwindow, null);
        TextView btnCarema = (TextView) view.findViewById(R.id.btn_camera);
        TextView btnPhoto = (TextView) view.findViewById(R.id.btn_photo);
        TextView btnCancel = (TextView) view.findViewById(R.id.btn_cancel);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
        popupWindow.setOutsideTouchable(true);
        View parent = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 20, 20);
        //popupWindow在弹窗的时候背景半透明
        final WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.5f;
        getWindow().setAttributes(params);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params.alpha = 1.0f;
                getWindow().setAttributes(params);
            }
        });

        btnCarema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //权限判断
                if (ContextCompat.checkSelfPermission(TechnicianCertifActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(TechnicianCertifActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_CAPTURE);
                } else {
                    //跳转到调用系统相机
                    gotoCarema();
                }
                popupWindow.dismiss();
            }
        });
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //权限判断
                if (ContextCompat.checkSelfPermission(TechnicianCertifActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请READ_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(TechnicianCertifActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            READ_EXTERNAL_STORAGE_REQUEST_CODE);
                } else {
                    //跳转到调用系统图库
                    HeadPortraitUtils.gotoPhoto(TechnicianCertifActivity.this);
                }
                popupWindow.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });


    }

    /**
     * 跳转到照相机页面
     *
     * @author dingxujun
     * created at 2017/5/9 14:53
     */
    private void gotoCarema() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        startActivityForResult(intent, Constant.REQUEST_CAPTURE);
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

    /**
     * 创建调用系统照相机待存储的临时文件
     *
     * @author dingxujun
     * created at 2017/5/9 15:08
     */
    private void createCameraTempFile(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey("tempFile")) {
            tempFile = (File) savedInstanceState.getSerializable("tempFile");
        } else {
            tempFile = new File(checkDirPath(Environment.getExternalStorageDirectory().getPath() + "/image/"),
                    System.currentTimeMillis() + ".jpg");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("tempFile", tempFile);
    }

    /**
     * 技师认证
     *
     * @param identityBackUrl  身份证背面path
     * @param identityFrontUrl 身份证正面path
     * @param fileUrl          资质证明path
     * @param realName         真实姓名
     * @param skill            工种Id
     * @param station          门店Id
     * @param workYears        工作年限
     */
    public void technicianAuth(final String identityBackUrl, final String identityFrontUrl,
                               final String fileUrl, final String realName, final String skill, final String station, final String workYears) {
        Constant.TOKEN = SharedPreferences.getInstance().getString("checktoken", "");
        Constant.REGISTRATION = SharedPreferences.getInstance().getString("RegistrationId", "");
        HttpRequestApi.getInstance().uploadFile(upImage(identityBackUrl), new HttpSubscriber<Upload>(new SubscriberOnListener<Upload>() {
            @Override
            public void onSucceed(final Upload identityBackfFile) {
                System.out.println("反面成功");
                HttpRequestApi.getInstance().uploadFile(upImage(identityFrontUrl), new HttpSubscriber<Upload>(new SubscriberOnListener<Upload>() {
                    @Override
                    public void onSucceed(final Upload identityFrontfFile) {
                        System.out.println("正面成功");
                        if (!fileUrl.equals("")) {
                            HttpRequestApi.getInstance().uploadFile(upImage(fileUrl), new HttpSubscriber<Upload>(new SubscriberOnListener<Upload>() {
                                @Override
                                public void onSucceed(Upload file) {
                                    System.out.println("证书成功");
                                    TechnicianAuth technicianAuth = new TechnicianAuth();
                                    technicianAuth.setContentType(file.getData().getExtension());
                                    technicianAuth.setCredentialType("21C8BF22-7E2C-4CF7-BAF9-938522A589A4");
                                    technicianAuth.setCredentialTypeName("人社部证书");
                                    technicianAuth.setFileId(file.getData().getId());
                                    technicianAuth.setFileName(file.getData().filename);
                                    technicianAuth.setFileSize(0);
                                    technicianAuth.setFileUrl(file.getData().getDownloadUrl());
                                    technicianAuth.setIdentityBackUrl(identityBackfFile.getData().getDownloadUrl());
                                    technicianAuth.setIdentityFrontUrl(identityFrontfFile.getData().getDownloadUrl());
                                    technicianAuth.setMemberId(Constant.MEMBERID);
                                    technicianAuth.setMobile(SharedPreferences.getInstance().getString("username", ""));
                                    technicianAuth.setName(realName);
                                    technicianAuth.setSkillId(skill);
                                    technicianAuth.setStationId(station);
                                    technicianAuth.setWorkYears(workYears);
                                    technicianAuth.setIdentityBackId(identityBackfFile.getData().getId());
                                    technicianAuth.setIdentityFrontId(identityFrontfFile.getData().getId());
                                    Gson gson = new Gson();
                                    String verifyAuthCodeJson = gson.toJson(technicianAuth);
                                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), verifyAuthCodeJson);
                                    HttpRequestApi.getInstance().technicianAuth(requestBody, new HttpSubscriber<ErrMessage>(new SubscriberOnListener<ErrMessage>() {
                                        @Override
                                        public void onSucceed(ErrMessage demo) {
                                            Toast.makeText(TechnicianCertifActivity.this, "提交成功！", Toast.LENGTH_SHORT).show();
                                            System.out.println("--------审核提交成功----------------");
                                        }
                                        @Override
                                        public void onError(int code, String msg) {
                                            System.out.println("响应吗====" + Constant.RESPONSECODE);
                                        }

                                    }, TechnicianCertifActivity.this));
                                }
                                @Override
                                public void onError(int code, String msg) {

                                    Toast.makeText(TechnicianCertifActivity.this, "证书上传失败，请重新尝试！", Toast.LENGTH_SHORT).show();
                                }
                            }, TechnicianCertifActivity.this));
                        } else {
                            TechnicianAuth technicianAuth = new TechnicianAuth();
                            technicianAuth.setContentType("");
                            technicianAuth.setCredentialType("21C8BF22-7E2C-4CF7-BAF9-938522A589A4");
                            technicianAuth.setCredentialTypeName("人社部证书");
                            technicianAuth.setFileId("");
                            technicianAuth.setFileName("");
                            technicianAuth.setFileSize(0);
                            technicianAuth.setFileUrl("");
                            technicianAuth.setIdentityBackUrl(identityBackfFile.getData().getDownloadUrl());
                            technicianAuth.setIdentityFrontUrl(identityFrontfFile.getData().getDownloadUrl());
                            technicianAuth.setMemberId(Constant.MEMBERID);
                            technicianAuth.setMobile(SharedPreferences.getInstance().getString("username", ""));
                            technicianAuth.setName(realName);
                            technicianAuth.setSkillId(skill);
                            technicianAuth.setStationId(station);
                            technicianAuth.setWorkYears(workYears);
                            technicianAuth.setIdentityBackId(identityBackfFile.getData().getId());
                            technicianAuth.setIdentityFrontId(identityFrontfFile.getData().getId());
                            Gson gson = new Gson();
                            String verifyAuthCodeJson = gson.toJson(technicianAuth);
                            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), verifyAuthCodeJson);
                            HttpRequestApi.getInstance().technicianAuth(requestBody, new HttpSubscriber<ErrMessage>(new SubscriberOnListener<ErrMessage>() {
                                @Override
                                public void onSucceed(ErrMessage demo) {
                                    Toast.makeText(TechnicianCertifActivity.this, "提交成功！", Toast.LENGTH_SHORT).show();
                                }
                                @Override
                                public void onError(int code, String msg) {

                                }
                            }, TechnicianCertifActivity.this));
                        }
                    }

                    @Override
                    public void onError(int code, String msg) {
                        System.out.println("正面失败");
                        Toast.makeText(TechnicianCertifActivity.this, "身份证正面上传失败，请重新尝试！", Toast.LENGTH_SHORT).show();
                    }
                }, TechnicianCertifActivity.this));
            }

            @Override
            public void onError(int code, String msg) {

                Toast.makeText(TechnicianCertifActivity.this, "身份证反面上传失败，请重新尝试！", Toast.LENGTH_SHORT).show();

            }
        }, TechnicianCertifActivity.this));


    }


    /**
     * 上传图片的请求封装
     *
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
     *
     * @author dingxujun
     * created at 2017/5/9 16:54
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case Constant.REQUEST_CAPTURE: //调用系统相机返回
                if (resultCode == RESULT_OK) {
                    gotoClipActivity(Uri.fromFile(tempFile));
                }
                break;
            case Constant.REQUEST_PICK:  //调用系统相册返回
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    gotoClipActivity(uri);
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
                            cropImagePath1 = getRealFilePathFromUri(getApplicationContext(), uri);//图片的路径
                            imageNme1 = cropImagePath1.substring(cropImagePath1.lastIndexOf("/") + 1);
                            Bitmap bitMap1 = BitmapFactory.decodeFile(cropImagePath1);//将bitmap文件转换为bitp对象
                            identityimage.setImageBitmap(bitMap1);
                            break;
                        case 2:
                            //反面
                            cropImagePath2 = getRealFilePathFromUri(getApplicationContext(), uri);//图片的路径
                            imageNme2 = cropImagePath2.substring(cropImagePath2.lastIndexOf("/") + 1);
                            Bitmap bitMap2 = BitmapFactory.decodeFile(cropImagePath2);//将bitmap文件转换为bitp对象
                            idcontryimg.setImageBitmap(bitMap2);
                            break;
                        case 3:
                            //证书
                            cropImagePath3 = getRealFilePathFromUri(getApplicationContext(), uri);//图片的路径
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
     * 修改用户信息的接口
     *
     * @author dingxujun
     * created at 2017/5/11 10:59
     */
    private void uPDateUsermod(final UserBaseData userBaseData, int code, final String id, final String value) {
        Gson gson = new Gson();
        String userJson = gson.toJson(userBaseData);
        Log.e("TAG", userJson);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), userJson);
        Log.e("TAG", requestBody.toString() + "44444444444444444444444444444444444444444444");
        HttpRequestApi.getInstance().updateUserbaseData(requestBody, new HttpSubscriber<Skill>(new SubscriberOnListener<Skill>() {
            @Override
            public void onSucceed(Skill data) {
                ToastUtils.makeShortText("设置成功", TechnicianCertifActivity.this);
                System.out.println("--------------------请求成功我是用户信息-------------------");
            }

            @Override
            public void onError(int code, String msg) {
                String name = userBaseData.getName();

                System.out.println("--------------------请求失败我是用户信息-------------------" + name + "\n"
                        + code + "----" + msg);
            }
        }, TechnicianCertifActivity.this));
    }

    /**
     * 打开截图界面
     *
     * @param uri
     */
    public void gotoClipActivity(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, ClipImageActivity.class);
        intent.putExtra("type", type);//添加标记
        intent.setData(uri);
        //打开新的Activity，等新的Activity关闭后，将把新的Activity的数据返回给自己
        startActivityForResult(intent, Constant.REQUEST_CROP_PHOTO);
    }


    /**
     * 根据Uri返回文件绝对路径
     * 兼容了file:///开头的 和 content://开头的情况
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePathFromUri(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.shop_place_rl:
                UIHelper.showSelectStore(TechnicianCertifActivity.this);
                break;
            case R.id.years_of_working:
                showDialog(DATE_DIALOG);
                break;
            case R.id.type_work_rl:
                showGridViewDialog();
                break;
            case R.id.back_click:
                finish();
                break;
            case R.id.set_namerl:
                showSubmitAlertDialog();
                break;
            default:
                break;

        }
    }

    /**
     * 工种信息的dialog
     *
     * @author dingxujun
     * created at 2017/5/18 16:18
     */
    private void showGridViewDialog() {
        // typeWorkRequest();
        System.out.println("--------我是dialog-------");
        View view = LayoutInflater.from(this).inflate(R.layout.type_work_dialog, null);
        // 设置style 控制默认dialog带来的边距问题
        typewkdialog = new Dialog(this, R.style.DialogTheme);
        typewkdialog.setContentView(view);
        typewkdialog.show();
        gridView = (GridView) view.findViewById(R.id.type_work_gridview);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));//去除条目选中的颜色，xml里面设置不知道为何不起作用
        TypeWorkAdpter adpter = new TypeWorkAdpter(TechnicianCertifActivity.this, skill.getData());
        gridView.setAdapter(adpter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String text = skill.getData().get(position).getText();
                ToastUtils.showShort(TechnicianCertifActivity.this, text);
                typeWorkId = skill.getData().get(position).getValue();
                type_workTV1.setText(text);//工种
                typewkdialog.dismiss();
            }
        });
        Window window = typewkdialog.getWindow();
        window.getDecorView().setPadding(50, 50, 50, 50);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
