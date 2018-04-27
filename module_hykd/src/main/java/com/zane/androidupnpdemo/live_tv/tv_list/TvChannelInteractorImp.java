package com.zane.androidupnpdemo.live_tv.tv_list;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.zane.androidupnpdemo.live_tv.LiveBean;

import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

/**
 * Created by gzq on 2018/3/26.
 */

public class TvChannelInteractorImp implements ITvChannelInteractor{
    private OnParseExcelListener parseExcelListener;
    private TvChannelActivity tvChannelActivity;
    private static String TAG="EXCEL";
    @Override
    public void getChannels(OnParseExcelListener parseExcelListener,ITvList tvChannelActivity) {
        this.parseExcelListener=parseExcelListener;
        this.tvChannelActivity= (TvChannelActivity) tvChannelActivity;
        new ExcelTask().execute();
    }

    private class ExcelTask extends AsyncTask<Void,Void,ArrayList<LiveBean>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (parseExcelListener!=null)
                parseExcelListener.onStart();
        }

        @Override
        protected ArrayList<LiveBean> doInBackground(Void... voids) {
            return getXlsData("cqtv.xls",0);
        }

        @Override
        protected void onPostExecute(ArrayList<LiveBean> liveBeans) {
            super.onPostExecute(liveBeans);
            if (liveBeans!=null){
                parseExcelListener.onFinished(liveBeans);
            }else{
                parseExcelListener.onError("没有获取到数据");
            }
        }
    }

    private ArrayList<LiveBean> getXlsData(String xlsName, int index) {
        ArrayList<LiveBean> countryList = new ArrayList<>();
        AssetManager assetManager = tvChannelActivity.getAssets();

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
                countryModel.setTvImgUrl(sheet.getCell(7,i).getContents());
                countryList.add(countryModel);
            }

            workbook.close();

        } catch (Exception e) {
            parseExcelListener.onError("获取数据失败");
        }

        return countryList;
    }
}
