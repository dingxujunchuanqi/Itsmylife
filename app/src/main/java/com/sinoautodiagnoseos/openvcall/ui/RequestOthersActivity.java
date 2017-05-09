package com.sinoautodiagnoseos.openvcall.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.openvcall.model.AllExpertsData;
import com.sinoautodiagnoseos.openvcall.model.ExpertsData;
import com.sinoautodiagnoseos.propeller.ui.SearchView;
import com.sinoautodiagnoseos.utils.Constant;
import com.sinoautodiagnoseos.utils.ToastUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 邀请专家页面
 * Created by HQ_Demos on 2017/2/21.
 */
public class RequestOthersActivity extends Activity implements SearchView.SearchViewListener{
    private TextView cancle_btn,confirm_btn;

    private final static Logger log = LoggerFactory.getLogger(RequestOthersActivity.class);

    /**
     * 显示所有专家的列表view
     */
    private ListView request_listView;

    /**
     * 搜索结果列表view
     */
    private ListView lvResults;

    /**
     * 搜索view
     */
    private SearchView search_view;

    /**
     * 热搜框列表adapter
     */
    private ArrayAdapter<String> hintAdapter;

    /**
     * 自动补全列表adapter
     */
    private ArrayAdapter<String> autoCompleteAdapter;

    /**
     * 搜索结果列表adapter
     */
    private SearchAdapter resultAdapter;

    private List<AllExpertsData> dbData;

    /**
     * 热搜版数据
     */
    private List<String> hintData;

    /**
     * 搜索过程中自动补全数据
     */
    private List<String> autoCompleteData;

    /**
     * 搜索结果的数据
     */
    private List<ExpertsData> resultData;

    /**
     * 默认提示框显示项的个数
     */
    private static int DEFAULT_HINT_SIZE = 4;

    /**
     * 提示框显示项的个数
     */
    private static int hintSize = DEFAULT_HINT_SIZE;

    private Map<Integer,Boolean> isSelected;

    private List beSelectedData = new ArrayList();

    private RequestExpertsAdapter adapter;

    private List<ExpertsData> extersDatas;

    /**
     *  设置提示框显示项的个数
     * @param hintSize 提示框显示个数
     */
    public static void setHintSize(int hintSize) {
        RequestOthersActivity.hintSize = hintSize;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_request);
        initData();
        initView();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        extersDatas = (List<ExpertsData>) getIntent().getSerializableExtra("extersDatas");
        Log.e("extersDatas-----",extersDatas.size()+"");
//        //初始化热搜版数据
//        getHintData();
        //初始化自动补全数据
        getAutoCompleteData(null);
        //初始化搜索结果数据
        getResultData(null);
        if (isSelected!=null)
            isSelected = null;
        isSelected = new HashMap<Integer, Boolean>();
        for (int i=0;i<extersDatas.size();i++){
            isSelected.put(i,false);
        }
        //清除已选项
        if (beSelectedData.size()>0){
            beSelectedData.clear();
        }
    }

//    List<AllExpertsData> info=new ArrayList<>();

    /**
     * 初始化控件
     */
    ExpertsData expertsInfo = null;
    private void initView() {
        cancle_btn= (TextView) findViewById(R.id.cancle_btn);
        confirm_btn= (TextView) findViewById(R.id.confirm_btn);

        lvResults = (ListView) findViewById(R.id.request_listview);
        search_view = (SearchView) findViewById(R.id.search_view);

        request_listView= (ListView) findViewById(R.id.request_listview);
        adapter=new RequestExpertsAdapter(this, extersDatas,isSelected,beSelectedData);
        request_listView.setAdapter(adapter);
        request_listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        adapter.notifyDataSetChanged();
        request_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Log.e("map",extersDatas.get(i).getName());
            }
        });

        //设置监听
        search_view.setSearchViewListener(this);
        //设置adapter
        search_view.setTipsHintAdapter(hintAdapter);
        search_view.setAutoCompleteAdapter(autoCompleteAdapter);

        lvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(RequestOthersActivity.this, i + "", Toast.LENGTH_SHORT).show();
            }
        });

        cancle_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancle_btn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                finish();
            }
        });

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    confirm_btn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                if (isSelected.size()!=0){
                    for (int i=0;i<extersDatas.size();i++){
                        boolean flag = isSelected.get(i).equals(true);//用来判断选择的是哪一个专家
                        if (flag==true){
                            expertsInfo=new ExpertsData();
                            String name=extersDatas.get(i).getName().toString();
                            String expertId=extersDatas.get(i).getId();
                            String memberId=extersDatas.get(i).getMemberId();
                            String callHistoryId=extersDatas.get(i).getCallHistoryId();
                            String avatar =extersDatas.get(i).getAvatorUrl();
                            String mobile = extersDatas.get(i).getMobile();
                            expertsInfo.setName(name);
                            expertsInfo.setAvatorUrl(avatar);
                            expertsInfo.setId(expertId);
                            expertsInfo.setMemberId(memberId);
                            expertsInfo.setMobile(mobile);
                            expertsInfo.setCallHistoryId(callHistoryId);

                            System.out.println("邀请其他专家-----房间号----"+ Constant.ROOMID);
                            directCallProfessor(expertsInfo.getId(), Constant.ROOMID);

                        }
                    }
//                    PushUtil.InvitePush(RequestOthersActivity.this,expertsInfo.getMobile(), Constant.USERNAME,"技师:"+Constant.USERNAME,Constant.ROOMID);

//                    notice_to();
                    ToastUtils.showShort(RequestOthersActivity.this,getString(R.string.str_request_success));
//                    Intent intent = new Intent(RequestOthersActivity.this,ChatActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("expertsInfo",expertsInfo);
//                    intent.putExtras(bundle);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    setResult(1000,intent);
                    finish();
                }else {
                    ToastUtils.showShort(RequestOthersActivity.this,"请选择一个专家");
                }
            }
        });

    }


    public void directCallProfessor(final String expertsId, final String roomId) {
//        HttpRequestApi.getInstance().getDirectexpert(expertsId,roomId, new HttpSubscriber<SearchExpertsData>(new SubscriberOnListener<SearchExpertsData>() {
//            @Override
//            public void onSucceed(SearchExpertsData directexpert) {
//                if (directexpert.getData().getRoomId().equals("2")||directexpert.getData().getRoomId().equals("3")){
//                    Toast.makeText(RequestOthersActivity.this,"该专家不在线", Toast.LENGTH_SHORT).show();
//                }else{
//                    finish();
//                }
//            }
//
//            @Override
//            public void onError(int code, String msg) {
//            }
//        }, RequestOthersActivity.this));
    }


//    /**
//     * 获取热搜版data 和adapter
//     */
//    private void getHintData() {
//        hintData = new ArrayList<>(hintSize);
//        for (int i = 1; i <= hintSize; i++) {
//            hintData.add(extersDatas.get(i).getName() );
//        }
//        hintAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, hintData);
//    }

    /**
     * 获取自动补全data 和adapter
     */
    private void getAutoCompleteData(String text) {
        if (autoCompleteData == null) {
            //初始化
            autoCompleteData = new ArrayList<>(hintSize);
        } else {
            // 根据text 获取auto data
            autoCompleteData.clear();
            for (int i = 0, count = 0; i < extersDatas.size()
                    && count < hintSize; i++) {
                if (extersDatas.get(i).getName().contains(text.trim())) {
                    autoCompleteData.add(extersDatas.get(i).getName());
                    count++;
                }
            }
        }
        if (autoCompleteAdapter == null) {
            autoCompleteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, autoCompleteData);
        } else {
            autoCompleteAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取搜索结果data和adapter
     */
    private void getResultData(String text) {
        if (resultData == null) {
            // 初始化
            resultData = new ArrayList<>();
        } else {
            resultData.clear();
            for (int i = 0; i < extersDatas.size(); i++) {
                if (extersDatas.get(i).getName().contains(text.trim())) {
                    resultData.add(extersDatas.get(i));
                }
            }
        }
        if (resultAdapter == null) {
            resultAdapter = new SearchAdapter(this, resultData, R.layout.item_experts_list);
        } else {
            resultAdapter.notifyDataSetChanged();
        }
    }



    public void onBtnCancleClicked(View view){
        log.info("onBtnCancleClicked"+view);
        cancle_btn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        confirm_btn.setTextColor(getResources().getColor(R.color.gray));
        finish();
    }

    public void onBtnSureClicked(View view){
        log.info("onBtnSureClicked"+view);
        confirm_btn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        cancle_btn.setTextColor(getResources().getColor(R.color.gray));
    }

    /**
     * 当搜索框 文本改变时 触发的回调 ,更新自动补全数据
     * @param text
     */
    @Override
    public void onRefreshAutoComplete(String text) {
        //更新数据
        getAutoCompleteData(text);
    }

    /**
     * 点击搜索键时edit text触发的回调
     *
     * @param text
     */
    @Override
    public void onSearch(String text) {
        //更新result数据
        getResultData(text);
        lvResults.setVisibility(View.VISIBLE);
        //第一次获取结果 还未配置适配器
        if (lvResults.getAdapter() == null) {
            //获取搜索数据 设置适配器
            lvResults.setAdapter(resultAdapter);
        } else {
            //更新搜索数据
            resultAdapter.notifyDataSetChanged();
        }
        Toast.makeText(this, getString(R.string.search_finish), Toast.LENGTH_SHORT).show();
    }
}
