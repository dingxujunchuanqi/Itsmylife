package com.sinoautodiagnoseos.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.activity.CarInfoActivity;
import com.sinoautodiagnoseos.activity.MainActivity;
import com.sinoautodiagnoseos.entity.Brand.Brand;
import com.sinoautodiagnoseos.entity.Brand.BrandResult;
import com.sinoautodiagnoseos.entity.CarBrands.CarInfo;
import com.sinoautodiagnoseos.entity.CarBrands.ZKCarBrands;
import com.sinoautodiagnoseos.entity.Data.Data;
import com.sinoautodiagnoseos.entity.Fault.Fault;
import com.sinoautodiagnoseos.entity.Fault.FaultResult;
import com.sinoautodiagnoseos.entity.ThinkTank.ResultInfo;
import com.sinoautodiagnoseos.entity.ThinkTank.ThinkTank;
import com.sinoautodiagnoseos.net.requestSubscribers.HttpSubscriber;
import com.sinoautodiagnoseos.net.requestSubscribers.SubscriberOnListener;
import com.sinoautodiagnoseos.net.requestUtil.NetRequestApi;
import com.sinoautodiagnoseos.openvcall.ui.Search_Dialog;
import com.sinoautodiagnoseos.propeller.ui.SearchView;
import com.sinoautodiagnoseos.ui.UIHelper;
import com.sinoautodiagnoseos.ui.adapter.GzAdapter;
import com.sinoautodiagnoseos.ui.adapter.PpAdapter;
import com.sinoautodiagnoseos.ui.adapter.ZlAdapter;
import com.sinoautodiagnoseos.ui.recyclerView.MyItemDecoration;
import com.sinoautodiagnoseos.ui.recyclerView.MyRecyclerView;
import com.sinoautodiagnoseos.ui.wheelview.OnWheelChangedListener;
import com.sinoautodiagnoseos.ui.wheelview.OnWheelScrollListener;
import com.sinoautodiagnoseos.ui.wheelview.WheelView;
import com.sinoautodiagnoseos.ui.wheelview.adapter.NumericWheelAdapter;
import com.sinoautodiagnoseos.utils.CommUtil;
import com.sinoautodiagnoseos.utils.ToastUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static com.sinoautodiagnoseos.R.id.gz_layout;
import static com.sinoautodiagnoseos.R.id.item_title;

/**
 * 维修方案
 * Created by HQ_Demos on 2017/5/22.
 */
public class ServiceFragment extends Fragment {
    private MyRecyclerView mRecyclerView;
    private SearchView case_search_view;
    private TextView search_or_cancle;
    private int btn_searchorcacle=0;

    private MyAdapter myAdapter;
    Handler h=new Handler();
    private int position;
    private RelativeLayout user;
    private int clickId;
    private LinearLayout rl_main;
    private String keyword="";
    private int seletion;

    private View view;
    private boolean isViewCreate;//view是否创建
    private boolean isViewVisible;//view是否可见
    private String caseType="";
    private String brandId="";
    private String faultId="";
    private String carYear="";
    private Integer caseId=null;
    private int pageIndex=1;//起始页
    private int pageSize=5;//每次搜索的条数
    private int totalCount;//总条数
    private Context context;
    private  MainActivity activity;
    private ImageView cancel_image;
    public void setThis(MainActivity activity){
        this.activity=activity;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view==null){
            view = inflater.inflate(R.layout.fragment_service,container,false);
            initData(keyword);
            initView(view);
            System.out.println("------------我是服务oncreateview-");
        }
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            isViewVisible=true;
            isViewCreate=true;//view已创建
          //  initData(keyword);
            user= (RelativeLayout) activity.findViewById(R.id.user);
            user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("ServiceFragment");
                    showSearchDialog();
                    getAllCar();
                }
            });
        }
    }

    private List<ResultInfo>result_list=new ArrayList<>();
    private List<BrandResult>brand_list=new ArrayList<>();
    private List<BrandResult>brand_list1=new ArrayList<>();
    private List<FaultResult>fault_list=new ArrayList<>();
    private List<Data>data_list=new ArrayList<>();
    private Data data=null;
    private void initData(String keyword) {
        if (isViewVisible&&isViewCreate){
        pageIndex=1;
        NetRequestApi.getInstance().getThinkTank(2,"",keyword,"","","",pageIndex,pageSize,null,new HttpSubscriber<ThinkTank>(new SubscriberOnListener<ThinkTank>() {
            @Override
            public void onSucceed(ThinkTank data) {
                totalCount=data.getTotalCount();
                System.out.println("--totalCount--"+totalCount);
                System.out.println("-------我是服务搜索接口---------");
                result_list=data.getResult();
                if (result_list.size()!=0){
                    caseId=result_list.get(0).getCaseId();
                }else{
                    caseId=null;
                }
                myAdapter=new MyAdapter(result_list);
                mRecyclerView.setAdapter(myAdapter);
                myAdapter.notifyDataSetChanged();
                myAdapter.setOnItemClickListener(new onRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        System.out.println("--1---position------"+position);
                        int caseId=result_list.get(position).getCaseId();
                        System.out.println("--1---caseId------"+caseId);
                        String title=result_list.get(position).getTitle();
                        System.out.println("--1---title------"+title);
                        UIHelper.showServiceDetail(getActivity(),caseId,title);
//                        intentDetail(caseId);
                    }
                });
            }

            @Override
            public void onError(int code, String msg) {

            }
        },getContext()));

     }
    }



//    private void intentDetail(int caseId) {
//        NetRequestApi.getInstance().findcasedetail(caseId,new HttpSubscriber<CaseDetail>(new SubscriberOnListener<CaseDetail>() {
//            @Override
//            public void onSucceed(CaseDetail data) {
//
//            }
//
//            @Override
//            public void onError(int code, String msg) {
//
//            }
//        },getContext()));
//    }

    //搜索完成 隐藏软键盘
    private void hideInputMethod(){
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void initView(View view) {
//        user.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println("-----click "+seletion);
//                if (seletion==0){
//                    showSearchDialog();
//                }
//            }
//        });

        case_search_view= (SearchView) view.findViewById(R.id.case_search_view);
        search_or_cancle= (TextView) view.findViewById(R.id.search_or_clear);
        cancel_image = (ImageView) view.findViewById(R.id.cancel_image);
        case_search_view.setSearchViewListener(new SearchView.SearchViewListener() {
            @Override
            public void onRefreshAutoComplete(String text) {
                System.out.println("---11111----"+text);
                keyword=text;
                WindowManager wm = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                int w = display.getWidth();
                RelativeLayout.LayoutParams layoutParams= (RelativeLayout.LayoutParams) case_search_view.getLayoutParams();
                layoutParams.width=w-140;
                case_search_view.setLayoutParams(layoutParams);
                search_or_cancle.setVisibility(View.VISIBLE);
                cancel_image.setVisibility(View.GONE);
                search_or_cancle.setText("取消");

            }

            @Override
            public void onSearch(final String text) {
                if (TextUtils.isEmpty(text)) {
                    ToastUtils.showShort(getActivity(),"请输入关键词");
                } else {
                    System.out.println("---onClick----");
                    System.out.println("------我是搜索被点击了服务-----");
                    //调用键盘搜索键逻辑 业务处理在此
                    search_or_cancle.setText("清除");
                    search_or_cancle.setVisibility(View.VISIBLE);
                    WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
                    Display display = wm.getDefaultDisplay();
                    int w = display.getWidth();
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) case_search_view.getLayoutParams();
                    layoutParams.width = w + 140;
                    case_search_view.setLayoutParams(layoutParams);
                    cancel_image.setVisibility(View.VISIBLE);
                    search_or_cancle.setVisibility(View.GONE);
                    //搜索完成 隐藏软键盘
                    hideInputMethod();
                    initData(text);
                    btn_searchorcacle = 1;
                }
            }
        });

        search_or_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//case_search_view.etInput.clearFocus();
             //   case_search_view.etInput.setFocusable(false);
                        System.out.println("11111我点击了----"+search_or_cancle.getText());
                        search_or_cancle.setText("取消");
                //点击取消 隐藏软键盘
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                        WindowManager wm = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
                        Display display = wm.getDefaultDisplay();
                        int w = display.getWidth();
                        RelativeLayout.LayoutParams layoutParams= (RelativeLayout.LayoutParams) case_search_view.getLayoutParams();
                        layoutParams.width=w;
                        case_search_view.setLayoutParams(layoutParams);
                        search_or_cancle.setVisibility(View.GONE);
                        case_search_view.etInput.setText("");
//                        break;
//                    case 1:
//                        System.out.println("222222我点击了----"+search_or_cancle.getText());
//                        WindowManager wm1 = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
//                        Display display1 = wm1.getDefaultDisplay();
//                        int w1 = display1.getWidth();
//                        LinearLayout.LayoutParams layoutParams1= (LinearLayout.LayoutParams) case_search_view.getLayoutParams();
//                        layoutParams1.width=w1;
//                        case_search_view.setLayoutParams(layoutParams1);
//                        search_or_cancle.setVisibility(View.GONE);
//                        case_search_view.etInput.setText("");//点击清除，清空输入框
////                        search_or_cancle.setText("取消");
//                        keyword="";
//                        initData(keyword);
//                        break;
//                }
            }
        });
        cancel_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("222222我点击了----"+search_or_cancle.getText());
                WindowManager wm1 = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
                Display display1 = wm1.getDefaultDisplay();
                int w1 = display1.getWidth();
                RelativeLayout.LayoutParams layoutParams1= (RelativeLayout.LayoutParams) case_search_view.getLayoutParams();
                layoutParams1.width=w1;
                case_search_view.setLayoutParams(layoutParams1);
                search_or_cancle.setVisibility(View.GONE);
                cancel_image.setVisibility(View.GONE);
                case_search_view.etInput.setText("");//点击清除，清空输入框
//                        search_or_cancle.setText("取消");
                keyword="";
                initData(keyword);
            }
        });

        mRecyclerView= (MyRecyclerView) view.findViewById(R.id.list);
        //设置LayoutManager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new MyItemDecoration());

        mRecyclerView.setMyRecyclerViewListener(new MyRecyclerView.MyRecyclerViewListener() {
            @Override
            public void onRefresh() {
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int pageIndex=1;
                        NetRequestApi.getInstance().getThinkTank(2,"",keyword,"","","",pageIndex,pageSize,caseId,new HttpSubscriber<ThinkTank>(new SubscriberOnListener<ThinkTank>() {
                            @Override
                            public void onSucceed(ThinkTank data) {
                                List<ResultInfo>result_list1=new ArrayList<ResultInfo>();
                                ResultInfo resultInfo;
                                result_list1=data.getResult();
                                if (data.getTotalCount()==0){
                                    ToastUtils.showShort(getActivity(),"暂无最新数据");
                                    mRecyclerView.setRefreshComplete();
                                }else {
                                    caseId=result_list1.get(0).getCaseId();
                                    for (int i=0;i<result_list1.size();i++){
                                        resultInfo=new ResultInfo();
                                        resultInfo.setCaseId(result_list1.get(i).getCaseId());
                                        resultInfo.setTitle(result_list1.get(i).getTitle());
                                        resultInfo.setPictureUrl(result_list1.get(i).getPictureUrl());
                                        resultInfo.setCreateTime(result_list1.get(i).getCreateTime());
                                        myAdapter.data.add(0,resultInfo);
                                        }
                                    myAdapter.notifyDataSetChanged();
                                    mRecyclerView.setRefreshComplete();
                                }
                            }

                            @Override
                            public void onError(int code, String msg) {

                            }
                        },getContext()));
                    }
                },1500);
            }

            @Override
            public void onLoadMore() {
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageIndex=pageIndex+1;
                        NetRequestApi.getInstance().getThinkTank(2,"",keyword,"","","",pageIndex,pageSize,null,new HttpSubscriber<ThinkTank>(new SubscriberOnListener<ThinkTank>() {
                            @Override
                            public void onSucceed(ThinkTank data) {
                                List<ResultInfo>result_list1=new ArrayList<ResultInfo>();
                                ResultInfo resultInfo;
                                result_list1=data.getResult();
                                if (result_list1.size()==0){
                                    ToastUtils.showShort(getActivity(),"数据已加载完了");
                                }else {
                                    for (int i = 0; i < result_list1.size(); i++) {
                                        resultInfo = new ResultInfo();
                                        resultInfo.setCaseId(result_list1.get(i).getCaseId());
                                        resultInfo.setTitle(result_list1.get(i).getTitle());
                                        resultInfo.setPictureUrl(result_list1.get(i).getPictureUrl());
                                        resultInfo.setCreateTime(result_list1.get(i).getCreateTime());
                                        myAdapter.data.add(resultInfo);
                                    }
                                    mRecyclerView.setLoadMoreComplete();
                                    myAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onError(int code, String msg) {

                            }
                        },getContext()));
                    }
                },1000);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        case_search_view.etInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) {
// 此处为得到焦点时的处理内容
                    WindowManager wm = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
                    Display display = wm.getDefaultDisplay();
                    int w = display.getWidth();
                    RelativeLayout.LayoutParams layoutParams= (RelativeLayout.LayoutParams) case_search_view.getLayoutParams();
                    layoutParams.width=w-140;
                    case_search_view.setLayoutParams(layoutParams);
                    search_or_cancle.setVisibility(View.VISIBLE);
                    cancel_image.setVisibility(View.GONE);
                    search_or_cancle.setText("取消");
                    System.out.println("--------我有焦点--------");
                } else {
// 此处为失去焦点时的处理内容
                    System.out.println("--------我没有有焦点--------");
                }
            }

        });
    }

    //    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        user= (RelativeLayout) getActivity().findViewById(R.id.user);
//        user.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println("ServiceFragment");
//                showSearchDialog();
//                getAllCar();
//            }
//        });
//    }
    String[] array={"A","B","C","D","E","F","G"
            ,"H","I","J","K","L","M","N"
            ,"O","P","Q","R","S","T"
            ,"U","V","W","X","Y","Z"};
    private Map<String,List<ZKCarBrands.Brand>>carbrands_list;
    private List<CarInfo>carInfoList=new ArrayList<>();
    private CarInfo carInfo=null;
    private void getAllCar(){
        NetRequestApi.getInstance().getZKCarBrands(new HttpSubscriber<ZKCarBrands>(new SubscriberOnListener<ZKCarBrands>() {

            @Override
            public void onSucceed(ZKCarBrands data) {
                if (data.getData().size()==0){
                    carbrands_list=new Hashtable<String, List<ZKCarBrands.Brand>>();
                }else {
                    carbrands_list=data.getData();
                    for (int i=0;i<array.length;i++){
                        List<ZKCarBrands.Brand> carList=carbrands_list.get(array[i]);
                        if (carList!=null&&carList.size()>0){
                            for (ZKCarBrands.Brand b :carList){
                                carInfo=new CarInfo(b.getText(),array[i],b.getValue());
                                carInfoList.add(carInfo);
                                System.out.println(array[i]+"-----"+b.getText());
                            }
                        }
                    }
                }
            }

            @Override
            public void onError(int code, String msg) {

            }
        },getContext()));
    }

    BrandResult brandResult=null;
    Search_Dialog dialog;
    private GridView search_pinpai,search_guzhang,search_ziliao;
    private Button btn_year;
    private TextView btn_ok,btn_cancle;
    private LinearLayout zl_layout,clear_btn;
    private TextView pp,gz,zl;
    private RelativeLayout p_layout,g_layout,z_layout;
    private PpAdapter ppAdapter;//品牌Adapter
    private GzAdapter gzAdapter;//故障Adapter
    private ZlAdapter zlAdapter;//资料的Adapter
    private boolean is_clear=false;
    Button brand_btn;
    //显示搜索dialog
    private void showSearchDialog() {
        dialog=new Search_Dialog(getContext(),R.style.DialogTheme);
        View view=View.inflate(getContext(),R.layout.search_dialog,null);
        rl_main = (LinearLayout) view.findViewById(R.id.rl_main);
        search_pinpai= (GridView) view.findViewById(R.id.search_pinpai);
        search_guzhang= (GridView) view.findViewById(R.id.search_guzhang);
        search_ziliao= (GridView) view.findViewById(R.id.search_ziliao);
        btn_year= (Button) view.findViewById(R.id.btn_year);
        btn_ok= (TextView) view.findViewById(R.id.search_btn_ok);
        btn_cancle= (TextView) view.findViewById(R.id.search_btn_cancle);
        zl_layout= (LinearLayout) view.findViewById(R.id.zl_layout);

        pp=(TextView)view.findViewById(R.id.choise_pp_txt);
        gz=(TextView)view.findViewById(R.id.choise_gz_txt);
        zl=(TextView)view.findViewById(R.id.choise_z_txt);

        p_layout=(RelativeLayout)view.findViewById(R.id.pp_layout);
        g_layout=(RelativeLayout)view.findViewById(gz_layout);
        z_layout=(RelativeLayout)view.findViewById(R.id.z_layout);

        zl_layout.setVisibility(View.GONE);

        clear_btn= (LinearLayout) view.findViewById(R.id.clear_btn);
        clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_clear=true;
                p_layout.setVisibility(View.GONE);
                g_layout.setVisibility(View.GONE);
                z_layout.setVisibility(View.GONE);
                pp.setText("");
                gz.setText("");
                zl.setText("");
                brandId="";
                faultId="";
                caseType="";
                btn_year.setText("不限");
                p_layout.setVisibility(View.GONE);
                g_layout.setVisibility(View.GONE);
                z_layout.setVisibility(View.GONE);
                ppAdapter.setSeclection(-1);//刷新adapter的样式预留方法 让选择框失去焦点
                ppAdapter.notifyDataSetChanged();
                gzAdapter.setSeclection(-1);
                gzAdapter.notifyDataSetChanged();
//                brand_btn.setBackgroundResource(R.drawable.pinpai_shape);
//                brand_btn.setTextColor(getContext().getResources().getColor(R.color.text_apha40));
//                ppAdapter.notifyDataSetChanged();
            }
        });

        dialog.setView(view);
        dialog.setProperty();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        //获取品牌数据
        NetRequestApi.getInstance().findbrandpage(1,15,new HttpSubscriber<Brand>(new SubscriberOnListener<Brand>() {
            @Override
            public void onSucceed(Brand data) {
                brandResult=new BrandResult();
                brand_list=data.getResult();
                if (brand_list.size()>8){//>8即为两列 超过两行隐藏
                    brand_list1=new ArrayList<BrandResult>();
                    for (int i=0;i<7;i++){
                        brand_list1.add(brand_list.get(i));
                    }
                    brandResult.setBrandName("更多");
                    brand_list1.add(brandResult);
                    ppAdapter=new PpAdapter(getContext(),brand_list1);
                    search_pinpai.setAdapter(ppAdapter);
                   CommUtil.setViewHeightBasedOnChildren(search_pinpai);
                    ppAdapter.notifyDataSetChanged();
                }else{
                    //品牌adapter
                    ppAdapter=new PpAdapter(getContext(),brand_list);
                    search_pinpai.setAdapter(ppAdapter);
                 CommUtil.setViewHeightBasedOnChildren(search_pinpai);
                    ppAdapter.notifyDataSetChanged();
                }

                search_pinpai.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        brand_btn= (Button) view.findViewById(R.id.search_brand);
                        ppAdapter.setSeclection(position);
                        ppAdapter.notifyDataSetChanged();
                        brandId=String.valueOf(brand_list1.get(position).getBrandId());
                        String p_txt=brand_list1.get(position).getBrandName();
                        pp.setText(p_txt);
                        p_layout.setVisibility(View.VISIBLE);
                        if (brand_list1.get(position).getBrandName().contains("更多")
                                ||brand_list1.get(position).getBrandName().contains("全部"))
                        {
                            p_layout.setVisibility(View.GONE);
                        }
                        System.out.println("---brand_list1--"+brandId);
                        if (brand_list1.get(position).getBrandName().contains("更多"))
                        {
                            brandResult=new BrandResult();
                            brand_list1.clear();
                            brand_list1.addAll(brand_list);
                            brandResult.setBrandName("全部");
                            brand_list1.add(brandResult);
                            ppAdapter=new PpAdapter(getContext(),brand_list1);
                            search_pinpai.setAdapter(ppAdapter);
                           CommUtil.setViewHeightBasedOnChildren(search_pinpai);
                            ppAdapter.notifyDataSetChanged();
                        }else if (brand_list1.get(position).getBrandName().contains("全部"))
                        {
                            //用来显示所有数据
//                            UIHelper.showCarInfoActivity(getActivity(),carInfoList);
                            Intent intent =new Intent(getContext(), CarInfoActivity.class);
                            intent.putExtra("carInfoList", (Serializable) carInfoList);
                            startActivityForResult(intent, 1000);
                        }else {
                            ppAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }

            @Override
            public void onError(int code, String msg) {

            }
        },getContext()));

        //获取故障数据
        NetRequestApi.getInstance().findallfault(new HttpSubscriber<Fault>(new SubscriberOnListener<Fault>() {
            @Override
            public void onSucceed(Fault data) {
                fault_list=data.getResult();
                //故障adapter
                Log.e("--故障adapter--",fault_list.size()+"");
                gzAdapter=new GzAdapter(getContext(),fault_list);
                search_guzhang.setAdapter(gzAdapter);
               CommUtil.setViewHeightBasedOnChildren(search_guzhang);
                gzAdapter.notifyDataSetChanged();
                search_guzhang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        gzAdapter.setSeclection(position);
                        gzAdapter.notifyDataSetChanged();
                        faultId=fault_list.get(position).getValue().toString();
                        String g_txt=fault_list.get(position).getText();
                        gz.setText(g_txt);
                        g_layout.setVisibility(View.VISIBLE);
                        System.out.println("---faultId--"+faultId);
                    }
                });
            }

            @Override
            public void onError(int code, String msg) {

            }
        },getContext()));

        //时间选择器
        btn_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPop();
                makeWindowDark();
            }
        });
        //资料数据
        data=new Data("线路图","1");
        data_list.add(data);
        data=new Data("维修手册","2");
        data_list.add(data);
        data=new Data("车辆使用手册","3");
        data_list.add(data);
        zlAdapter=new ZlAdapter(getContext(),data_list);
        search_ziliao.setAdapter(zlAdapter);
       CommUtil.setViewHeightBasedOnChildren(search_ziliao);
        zlAdapter.notifyDataSetChanged();
        search_ziliao.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                zlAdapter.setSeclection(position);
                zlAdapter.notifyDataSetChanged();
                caseType=data_list.get(position).getValue().toString();
                String z_txt=data_list.get(position).getText();
                zl.setText(z_txt);
                z_layout.setVisibility(View.VISIBLE);
                System.out.println("---caseType--"+caseType);
            }
        });


        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                data_list=new ArrayList<Data>();
                pp.setText("");
                gz.setText("");
                zl.setText("");
                p_layout.setVisibility(View.GONE);
                g_layout.setVisibility(View.GONE);
                z_layout.setVisibility(View.GONE);
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (carYear.equals("不限")||carYear=="不限")
                {
                    carYear="";
                }
                System.out.println("入参----"+caseType+"-"+brandId+"-"+faultId+"-"+carYear);
                NetRequestApi.getInstance().getThinkTank(2,caseType,keyword,brandId,faultId,selectDate,1,10,null,new HttpSubscriber<ThinkTank>(new SubscriberOnListener<ThinkTank>() {

                    @Override
                    public void onSucceed(ThinkTank data) {
                        result_list=data.getResult();
                        myAdapter=new MyAdapter(result_list);
                        mRecyclerView.setAdapter(myAdapter);
                        myAdapter.notifyDataSetChanged();
                        data_list=new ArrayList<Data>();
                        caseType="";keyword="";brandId="";faultId="";selectDate="";
                        pp.setText("");
                        gz.setText("");
                        zl.setText("");
                        p_layout.setVisibility(View.GONE);
                        g_layout.setVisibility(View.GONE);
                        z_layout.setVisibility(View.GONE);
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(int code, String msg) {

                    }
                },getContext()));
            }
        });

    }

    @Override
    public void onDestroyView() {
        System.out.println("---Service---onDestroyView");
        super.onDestroyView();
        case_search_view.etInput.setText("");
        isViewCreate=false;
        isViewVisible=false;
    }

    @Override
    public void onStop() {
        System.out.println("---Service---onStop");
        super.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000 && resultCode == 1001)
        {
            System.out.println("-----onActivityResult-------");
            p_layout.setVisibility(View.VISIBLE);
            brandId=data.getStringExtra("carValue");
            pp.setText(data.getStringExtra("carName"));
            System.out.println("brandId------"+brandId+pp.getText());
        }
    }

    private int mScreenWidth;
    private PopupWindow popupWindow;
    private String selectDate;//选择时间
    //年份pop
    private void showPop() {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenWidth = metrics.widthPixels;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View popupWindowView = inflater.inflate(R.layout.pop_double_time_select, null);
        Button btn_cancel = (Button) popupWindowView.findViewById(R.id.btn_cancel);
        Button btn_ok = (Button) popupWindowView.findViewById(R.id.btn_ok);
        popupWindow = new PopupWindow(popupWindowView, 4 * mScreenWidth / 5, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOutsideTouchable(true);
        initWheelView(popupWindowView);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                makeWindowLight();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate = wl_start_year.getCurrentItem() + 1998 + "";//年
                popupWindow.dismiss();
                btn_year.setText(selectDate);
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(rl_main, Gravity.CENTER, 0, 0);
    }
    private WheelView wl_start_year;//年中的 WheelView
    private NumericWheelAdapter mWheelAdapter;
    private void initWheelView(View view) {
        Calendar c = Calendar.getInstance();
        int curYear = c.get(Calendar.YEAR);
        int curMonth = c.get(Calendar.MONTH) + 1;//通过Calendar算出的月数要+1
        /*****************开始时间***********************/
        wl_start_year = (WheelView) view.findViewById(R.id.wl_start_year);

        mWheelAdapter = new NumericWheelAdapter(getContext(), curYear-19, curYear);
        mWheelAdapter.setLabel(" ");
        wl_start_year.setViewAdapter(mWheelAdapter);
        mWheelAdapter.setTextColor(R.color.black);
        mWheelAdapter.setTextSize(20);
        wl_start_year.setCyclic(true);//是否可循环滑动
        wl_start_year.addScrollingListener(startScrollListener);
        wl_start_year.setCurrentItem(curYear - 1998);
        wl_start_year.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                carYear = (String) mWheelAdapter.getItemText(wl_start_year.getCurrentItem());
                setTextViewSize(carYear, mWheelAdapter);
            }
        });
        mWheelAdapter.setPosition(wl_start_year.getCurrentItem());
    }

    OnWheelScrollListener startScrollListener = new OnWheelScrollListener(){

        @Override
        public void onScrollingStarted(WheelView wheel) {

        }

        @Override
        public void onScrollingFinished(WheelView wheel) {

        }
    };

    private void setTextViewSize(String curriteItemText, NumericWheelAdapter adapter) {
        ArrayList<View> arrayList = adapter.getTestViews();
        int size = arrayList.size();
        String currentText;
        for (int i = 0; i < size; i++) {
            TextView textvew = (TextView) arrayList.get(i);
            currentText = textvew.getText().toString().trim();
            if (curriteItemText.equals(currentText)) {
                textvew.setTextSize(20);
                textvew.setTextColor(Color.BLACK);
            } else {
                textvew.setTextSize(18);
                textvew.setTextColor(Color.parseColor("#FF585858"));
            }
        }
    }

    /**
     * 让屏幕变暗
     */
    private void makeWindowDark() {
        Window window = getActivity().getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.5f;
        window.setAttributes(lp);
    }

    /**
     * 让屏幕变亮
     */
    private void makeWindowLight() {
        Window window = getActivity().getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 1f;
        window.setAttributes(lp);
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
        public List<ResultInfo> data=new ArrayList<>();
        private onRecyclerViewItemClickListener itemClickListener = null;

        public MyAdapter(List<ResultInfo> result_list) {
            data=result_list;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {

            View rootView= LayoutInflater.from(parent.getContext()).inflate(R.layout.my_item,parent,false);
            return new MyViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            holder.title.setText(data.get(position).getTitle());
            holder.time.setText(data.get(position).getCreateTime());
            String img_url=data.get(position).getPictureUrl();
            Glide.with(getContext())
                    .load(img_url)
                    .error(R.drawable.default_error)
                    .crossFade()
                    .into(holder.img);
            holder.lin_layout.setOnClickListener(new View.OnClickListener() {//为每一个item绑定监听 
                @Override
                public void onClick(View v) {
                    if (itemClickListener!=null){
                        itemClickListener.onItemClick(position);
                    }
                }
            });
        }

        public void setOnItemClickListener(onRecyclerViewItemClickListener listener){
            this.itemClickListener=listener;
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{
            ImageView img;
            TextView title;
            TextView time;
            LinearLayout lin_layout;
            public MyViewHolder(View itemView) {
                super(itemView);
                img= (ImageView) itemView.findViewById(R.id.item_img);
                title= (TextView) itemView.findViewById(item_title);
                time= (TextView) itemView.findViewById(R.id.item_time);
                lin_layout= (LinearLayout) itemView.findViewById(R.id.lin_layout);
            }
        }
    }

    public interface onRecyclerViewItemClickListener {
        void onItemClick(int position);
    }
}
