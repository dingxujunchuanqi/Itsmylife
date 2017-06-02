package com.sinoautodiagnoseos.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static com.sinoautodiagnoseos.R.id.cancel_image;
import static com.sinoautodiagnoseos.R.id.item_title;

/**
 * 汽车资料
 * Created by HQ_Demos on 2017/5/22.
 */
public class CarInfoFragment extends Fragment {
    private MyRecyclerView mRecyclerView;
    private MyAdapter myAdapter;
    Handler h=new Handler();
    private String keyword="";
    private SearchView case_search_view;
    private TextView search_or_cancle;
    private int btn_searchorcacle=0;

    private RelativeLayout user;
    private int clickId;
    private int seletion;

    private LinearLayout rl_main;

    private View view;
    private boolean isViewCreate;//view是否创建
    private boolean isViewVisible;//view是否可见

    private String caseType="";
    private String brandId="";
    private String faultId="";
    private String carYear="";
    private ImageView cancel_image;
    private MainActivity activity;
    public void setThis(MainActivity activity){
        this.activity=activity;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view==null){
            view = inflater.inflate(R.layout.fragment_service,container,false);
            initData(keyword);
            initView(view);
        }
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            isViewVisible=true;
            isViewCreate=true;//view已创建
            initData(keyword);
            user= (RelativeLayout) activity.findViewById(R.id.user);
            user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("CarInfoFragment");
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
        if (isViewCreate && isViewVisible) {
            NetRequestApi.getInstance().getThinkTank(3,"",keyword,"","","",1,10, new HttpSubscriber<ThinkTank>(new SubscriberOnListener<ThinkTank>() {
                @Override
                public void onSucceed(ThinkTank data) {
                    System.out.println("-------我是汽车资料搜索接口---------");
                    result_list = data.getResult();
                    myAdapter = new MyAdapter(result_list);
                    mRecyclerView.setAdapter(myAdapter);
                    myAdapter.notifyDataSetChanged();
                    myAdapter.setOnItemClickListener(new onRecyclerViewItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            int caseId = result_list.get(position).getCaseId();
                            String title = result_list.get(position).getTitle();
                            UIHelper.showServiceDetail(getActivity(), caseId, title);
                        }
                    });
                }

                @Override
                public void onError(int code, String msg) {
                    System.out.println("-------我是汽车资料搜索失败接口---------");
                }
            }, getContext()));

        }
    }

    private void initView(View view) {
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
            public void onSearch(String text) {
                System.out.println("------我是搜索被点击了汽车资料-----");
                //调用键盘搜索键逻辑 业务处理在此
                // search_or_cancle.setText("清除");
                WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                int w = display.getWidth();
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) case_search_view.getLayoutParams();
                layoutParams.width = w + 140;
                case_search_view.setLayoutParams(layoutParams);
                cancel_image.setVisibility(View.VISIBLE);
                search_or_cancle.setVisibility(View.GONE);
                //搜索完成 隐藏软键盘
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                initData(text);
                btn_searchorcacle=1;
            }
        });

        search_or_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                switch (btn_searchorcacle){
//                    case 0:
                        System.out.println("11111我点击了----"+search_or_cancle.getText());
                        search_or_cancle.setText("取消");
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
                        mRecyclerView.setRefreshComplete();;
                    }
                },1500);
            }

            @Override
            public void onLoadMore() {
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        for(int i=0;i<5;i++){
//                            myAdapter.data.add(i+"ok");
//                        }
                        myAdapter.notifyDataSetChanged();
                        mRecyclerView.setLoadMoreComplete();
                    }
                },1000);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isViewCreate=false;
        isViewVisible=false;
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        user= (RelativeLayout) getActivity().findViewById(R.id.user);
//        user.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println("CarInfoFragment");
//                showSearchDialog();
//                getAllCar();
//            }
//        });
//    }

    String[] array={"A","B","C","D","E","F","G"
            ,"H","I","J","K","L","M","N"
            ,"O","P","Q","R","S","T"
            ,"U","V","W","X","Y","Z"};
    private Map<String,List<ZKCarBrands.Brand>> carbrands_list;
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
    private PpAdapter ppAdapter;
    private GzAdapter gzAdapter;
    private ZlAdapter zlAdapter;
    private LinearLayout clear_btn;
    private TextView pp,gz,zl;
    private RelativeLayout p_layout,g_layout,z_layout;
    private boolean is_clear=false;
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

        pp=(TextView)view.findViewById(R.id.choise_pp_txt);
        gz=(TextView)view.findViewById(R.id.choise_gz_txt);
        zl=(TextView)view.findViewById(R.id.choise_z_txt);

        p_layout=(RelativeLayout)view.findViewById(R.id.pp_layout);
        g_layout=(RelativeLayout)view.findViewById(R.id.gz_layout);
        z_layout=(RelativeLayout)view.findViewById(R.id.z_layout);

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
                            p_layout.setVisibility(View.GONE);
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
                            p_layout.setVisibility(View.GONE);
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
                System.out.println("入参----"+caseType+"-"+brandId+"-"+faultId+"-"+carYear);
                NetRequestApi.getInstance().getThinkTank(3,caseType,keyword,brandId,faultId,selectDate,1,10,new HttpSubscriber<ThinkTank>(new SubscriberOnListener<ThinkTank>() {

                    @Override
                    public void onSucceed(ThinkTank data) {
                        result_list=data.getResult();
                        myAdapter=new MyAdapter(result_list);
                        mRecyclerView.setAdapter(myAdapter);
                        myAdapter.notifyDataSetChanged();
                        caseType="";keyword="";brandId="";faultId="";selectDate="";
                        data_list=new ArrayList<Data>();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000 && resultCode == 1001)
        {
            p_layout.setVisibility(View.VISIBLE);
            brandId=data.getStringExtra("carValue");
            pp.setText(data.getStringExtra("carName"));
            System.out.println("brandId------"+brandId+pp.getText());
        }
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
        public List<ResultInfo> data=new ArrayList<>();
        private onRecyclerViewItemClickListener itemClickListener = null;

        public MyAdapter(List<ResultInfo> result_list) {
            data=result_list;
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {

            View rootView= LayoutInflater.from(parent.getContext()).inflate(R.layout.my_item,parent,false);
            return new MyAdapter.MyViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(MyAdapter.MyViewHolder holder, final int position) {

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
                        System.out.println("------"+position);
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
                String currentText = (String) mWheelAdapter.getItemText(wl_start_year.getCurrentItem());
                setTextViewSize(currentText, mWheelAdapter);
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
}
