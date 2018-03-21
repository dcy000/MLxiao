package com.gcml.gzq.live_media;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.okgo.OkGo;

import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

/**
 * Created by gzq on 2018/3/20.
 */

public class TVListActivity extends AppCompatActivity {

    private RecyclerView mTvList;
    private List<LiveBean> mData;
    private static String TAG="表格";
    private BaseQuickAdapter<LiveBean, BaseViewHolder> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_list);
        OkGo.getInstance().init(this.getApplication());
        initView();
       new ExcelTask().execute();
    }
    private class ExcelTask extends AsyncTask<Void,Void,ArrayList<LiveBean>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG,"开始读取表格");
        }

        @Override
        protected ArrayList<LiveBean> doInBackground(Void... voids) {
            return getXlsData("cqtv.xls",0);
        }

        @Override
        protected void onPostExecute(ArrayList<LiveBean> liveBeans) {
            super.onPostExecute(liveBeans);
            if (liveBeans!=null){
                mData.addAll(liveBeans);
                adapter.notifyDataSetChanged();
            }
        }
    }
    private ArrayList<LiveBean> getXlsData(String xlsName, int index) {
        ArrayList<LiveBean> countryList = new ArrayList<>();
        AssetManager assetManager = getAssets();

        try {
            WorkbookSettings settings=new WorkbookSettings();
            settings.setEncoding("GBK");
            Workbook workbook = Workbook.getWorkbook(assetManager.open(xlsName),settings);
            Sheet sheet = workbook.getSheet(index);

            int sheetNum = workbook.getNumberOfSheets();
            int sheetRows = sheet.getRows();
            int sheetColumns = sheet.getColumns();

            Log.d(TAG, "the num of sheets is " + sheetNum);
            Log.d(TAG, "the name of sheet is  " + sheet.getName());
            Log.d(TAG, "total rows is 行=" + sheetRows);
            Log.d(TAG, "total cols is 列=" + sheetColumns);

            for (int i = 1; i < sheetRows; i++) {
                LiveBean countryModel = new LiveBean();
                countryModel.setTvName(sheet.getCell(0, i).getContents());
                countryModel.setTvUrl(sheet.getCell(6, i).getContents());
                countryList.add(countryModel);
            }

            workbook.close();

        } catch (Exception e) {
            Log.e(TAG, "read error=" + e, e);
        }

        return countryList;
    }
    private void initView() {
        mTvList = findViewById(R.id.tv_list);
        mData=new ArrayList<>();
        mTvList.setLayoutManager(new GridLayoutManager(this,2));
        mTvList.addItemDecoration(new GridViewDividerItemDecoration(10,10));
        mTvList.setAdapter(adapter= new BaseQuickAdapter<LiveBean, BaseViewHolder>(R.layout.tv_item, mData) {
            @Override
            protected void convert(BaseViewHolder helper, LiveBean item) {
                helper.setText(R.id.tv_name,item.getTvName());
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(TVListActivity.this,TVDisplayActivity.class)
                        .putExtra("url",mData.get(position).getTvUrl()));
            }
        });
    }
}
