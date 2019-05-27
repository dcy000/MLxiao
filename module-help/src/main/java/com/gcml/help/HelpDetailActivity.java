package com.gcml.help;

import android.os.Bundle;
import android.widget.TextView;

import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.sjtu.yifei.annotation.Route;

@Route(path = "help/help/activity")
public class HelpDetailActivity extends ToolbarBaseActivity {

    private TextView tvHelp;
    private TextView tvHelpDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_detail);

        tvHelp = ((TextView) findViewById(R.id.tvHelp));
        tvHelpDetail = ((TextView) findViewById(R.id.tvHelpDetail));

        String help = getIntent().getStringExtra("help");
        String helpDetail = getIntent().getStringExtra("helpDetail");

        if (help != null) {
            tvHelp.setText(help);
        }

        if (helpDetail != null) {
            tvHelpDetail.setText(helpDetail);
        }
    }
}
