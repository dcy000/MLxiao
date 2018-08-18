package com.example.han.referralproject.bean;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Space;
import android.widget.TextView;

/**
 * Created by necer on 2017/5/23.
 */

public class NDialog1 {

    private Context mContext;
    private EditText mEditText;
    private BaseAdapter adapter;
    private View inputLine;
    private View customView;

    public static final int CONFIRM = 100;//确认提示框
    public static final int INPUT = 200;//输入框
    public static final int CHOICE = 300;//选择

    private String positiveButtonText = "确定";
    private String negativeButtonText = "取消";

    private int positiveTextColor = Color.parseColor("#333333");
    private int negativeTextColor = Color.parseColor("#333333");
    private int buttonSize = 14;//button字体大小
    private boolean isButtonCenter;//button居中

    private String message;
    private int messageSize = 15;
    private int messageColor = Color.BLACK;
    private boolean isMessageCenter;//信息居中

    private String title;
    private int titleSize = 16;
    private int titleColor = Color.BLACK;
    private boolean isTitleCenter;//标题是否居中


    private String inputText;
    private int inputTextSize = 15;
    private int inputTextColor = Color.parseColor("#333333");
    private String inputHintText;
    private int inputHintTextColor = Color.parseColor("#c1c1c1");
    private int inputType;
    private int inputLineColor = Color.parseColor("#003333");

    private boolean hasDivider = true;//选择时，是否有分割线
    private int dividerHeigh = 1;
    private int dividerColor = Color.parseColor("#c1c1c1");

    private String[] items;
    private int itemColor = Color.parseColor("#333333");
    private int itemSize = 14;
    private int itemGravity = Gravity.LEFT;
    private int itemHeigh = 50;
    private boolean cancleable = true;


    public NDialog1(Context context) {
        this.mContext = context;
    }

    private OnConfirmListener onConfirmListener;
    private OnInputListener onInputListener;

    private OnChoiceListener onChoiceListener;

    AlertDialog alertDialog;

    public AlertDialog create(final int type) {
        if (type == CONFIRM) {
        } else {
            throw new RuntimeException("目前只支持CONFIRM，INPUT和CHOICE三种弹窗！");
        }
        if (alertDialog == null) {
            alertDialog = new AlertDialog.Builder(mContext)
                    .setMessage(message)
                    .setTitle(title)
                    .setView(customView)
                    .setCancelable(cancleable)
                    .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setButtonClick(type, 1);
                        }
                    })
                    .setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setButtonClick(type, 0);
                        }
                    }).create();
        }


        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                setDetails(alertDialog, type);
            }
        });

        return alertDialog;
    }

    private void setButtonClick(int type, int which) {
        if (type == INPUT && onInputListener != null) {
            String inputString = mEditText.getText().toString();
            onInputListener.onClick(inputString, which);
        }

        if (type == CONFIRM && onConfirmListener != null) {
            onConfirmListener.onClick(which);
        }
    }


    private void setDetails(AlertDialog alertDialog, int type) {
        switch (type) {
            case CONFIRM:
                setButtonStyle(alertDialog);
                setTitleStyle(alertDialog);
                setMessageStyle(alertDialog);
                break;
            case INPUT:
                setButtonStyle(alertDialog);
                setTitleStyle(alertDialog);
                setInputStyle();
                popupSoftInput(mContext, mEditText);
                break;
            case CHOICE:
                setListViewStyle(alertDialog);
                break;
        }

    }

    private void setListViewStyle(final AlertDialog alertDialog) {

        ListView listView = customView.findViewById(com.megvii.faceppidcardui.R.id.listview);
        listView.setAdapter(adapter == null ? new AAdapter() : adapter);
        if (hasDivider) {
            listView.setDivider(new ColorDrawable(dividerColor));
            listView.setDividerHeight(dividerHeigh);
        } else {
            listView.setDividerHeight(0);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (onChoiceListener != null) {
                    onChoiceListener.onClick(items == null ? null : items[position], position);
                    alertDialog.dismiss();
                }
            }
        });
    }

    private void setInputStyle() {

        mEditText = customView.findViewById(com.megvii.faceppidcardui.R.id.input);
        inputLine = customView.findViewById(com.megvii.faceppidcardui.R.id.input_line);

        mEditText.setText(inputText);
        mEditText.setTextColor(inputTextColor);
        mEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP, inputTextSize);
        mEditText.setHint(inputHintText);
        mEditText.setHintTextColor(inputHintTextColor);
        if (inputType != 0) {
            mEditText.setInputType(inputType);
        }
        mEditText.setSelection(inputText == null ? 0 : inputText.length());
        inputLine.setBackgroundColor(inputLineColor);

    }

    private void setButtonStyle(AlertDialog alertDialog) {
        Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negativeButton.setTextColor(negativeTextColor);
        positiveButton.setTextColor(positiveTextColor);

        negativeButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, buttonSize);
        positiveButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, buttonSize);

        if (isButtonCenter) {
            Window window = alertDialog.getWindow();
            Button button3 = window.findViewById(android.R.id.button3);
            Space space = window.findViewById(com.megvii.faceppidcardui.R.id.spacer);

            button3.setVisibility(View.GONE);
            space.setVisibility(View.GONE);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
            negativeButton.setLayoutParams(lp);
            positiveButton.setLayoutParams(lp);
        }
    }

    private void setTitleStyle(AlertDialog alertDialog) {
        Window window = alertDialog.getWindow();
        TextView titleView = window.findViewById(com.megvii.faceppidcardui.R.id.alertTitle);

        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, titleSize);
        titleView.setTextColor(titleColor);

        if (isTitleCenter) {
            ImageView imageView = window.findViewById(android.R.id.icon);
            imageView.setVisibility(View.GONE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                titleView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }
        }
    }

    private void setMessageStyle(AlertDialog alertDialog) {
        Window window = alertDialog.getWindow();
        TextView messageView = window.findViewById(android.R.id.message);

        messageView.setTextSize(TypedValue.COMPLEX_UNIT_SP, messageSize);
        messageView.setTextColor(messageColor);

        if (isMessageCenter) {
            messageView.setGravity(Gravity.CENTER);
        }
    }

    private void popupSoftInput(final Context context, final EditText editText) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, 0);
            }
        }, 10);
    }


    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public interface OnConfirmListener {
        void onClick(int which);//whichButton:0,1

    }

    public interface OnInputListener {
        void onClick(String inputText, int which);
    }

    public interface OnChoiceListener {
        void onClick(String item, int which);
    }


    class AAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return items == null ? 0 : items.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(mContext).inflate(com.megvii.faceppidcardui.R.layout.item_layout, null);
            LinearLayout linearLayout = convertView.findViewById(com.megvii.faceppidcardui.R.id.ll);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dip2px(mContext, itemHeigh));
            linearLayout.setLayoutParams(layoutParams);
            TextView itemView = convertView.findViewById(com.megvii.faceppidcardui.R.id.text1);
            itemView.setText(items[position]);
            itemView.setTextColor(itemColor);
            itemView.setGravity(itemGravity | Gravity.CENTER_VERTICAL);
            itemView.setTextSize(TypedValue.COMPLEX_UNIT_SP, itemSize);
            return convertView;
        }
    }


    /****************
     * 参数statr
     *****************/


    public NDialog1 setCancleable(boolean cancleable) {
        this.cancleable = cancleable;
        return this;
    }


    public NDialog1 setButtonCenter(boolean buttonCenter) {
        isButtonCenter = buttonCenter;
        return this;
    }


    public NDialog1 setOnConfirmListener(OnConfirmListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
        return this;
    }

    public NDialog1 setOnInputListener(OnInputListener onInputListener) {
        this.onInputListener = onInputListener;
        return this;
    }

    public NDialog1 setOnChoiceListener(OnChoiceListener onChoiceListener) {
        this.onChoiceListener = onChoiceListener;
        return this;
    }

    public NDialog1 setInputLineColor(int inputLineColor) {
        this.inputLineColor = inputLineColor;
        return this;
    }

    public NDialog1 setInputHintText(String inputHintText) {
        this.inputHintText = inputHintText;
        return this;
    }

    public NDialog1 setInputHintTextColor(int inputHintTextColor) {
        this.inputHintTextColor = inputHintTextColor;
        return this;
    }

    public NDialog1 setInputType(int inputType) {
        this.inputType = inputType;
        return this;
    }

    public NDialog1 setMessageCenter(boolean messageCenter) {
        isMessageCenter = messageCenter;
        return this;
    }

    public NDialog1 setItems(String[] items) {
        this.items = items;
        return this;
    }

    public NDialog1 setItemColor(int itemColor) {
        this.itemColor = itemColor;
        return this;
    }

    public NDialog1 setItemSize(int itemSize) {
        this.itemSize = itemSize;
        return this;
    }

    public NDialog1 setItemGravity(int itemGravity) {
        this.itemGravity = itemGravity;
        return this;
    }

    public NDialog1 setPositiveButtonText(String positiveButtonText) {
        this.positiveButtonText = positiveButtonText;
        return this;
    }

    public NDialog1 setNegativeButtonText(String negativeButtonText) {
        this.negativeButtonText = negativeButtonText;
        return this;
    }

    public NDialog1 setPositiveTextColor(int positiveTextColor) {
        this.positiveTextColor = positiveTextColor;
        return this;
    }

    public NDialog1 setNegativeTextColor(int negativeTextColor) {
        this.negativeTextColor = negativeTextColor;
        return this;
    }

    public NDialog1 setMessage(String message) {
        this.message = message;
        return this;
    }

    public NDialog1 setMessageSize(int sp) {
        this.messageSize = sp;
        return this;
    }

    public NDialog1 setMessageColor(int messageColor) {
        this.messageColor = messageColor;
        return this;
    }

    public NDialog1 setTitle(String title) {
        this.title = title;
        return this;
    }

    public NDialog1 setTitleSize(int sp) {
        this.titleSize = sp;
        return this;
    }

    public NDialog1 setTitleColor(int titleColor) {
        this.titleColor = titleColor;
        return this;
    }

    public NDialog1 setTitleCenter(boolean titleCenter) {
        isTitleCenter = titleCenter;
        return this;
    }

    public NDialog1 setButtonSize(int buttonSize) {
        this.buttonSize = buttonSize;
        return this;
    }

    public NDialog1 setInputText(String inputText) {
        this.inputText = inputText;
        return this;
    }

    public NDialog1 setInputTextColor(int inputTextColor) {
        this.inputTextColor = inputTextColor;
        return this;
    }

    public NDialog1 setInputTextSize(int sp) {
        this.inputTextSize = sp;
        return this;
    }

    public NDialog1 setHasDivider(boolean hasDivider) {
        this.hasDivider = hasDivider;
        return this;
    }

    public NDialog1 setDividerHeigh(int px) {
        this.dividerHeigh = px;
        return this;
    }

    public NDialog1 setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        return this;
    }

    public NDialog1 setItemHeigh(int dp) {
        this.itemHeigh = dp;
        return this;
    }

    public NDialog1 setAdapter(BaseAdapter adapter) {
        this.adapter = adapter;
        return this;
    }

    /****************参数end*****************/


}
