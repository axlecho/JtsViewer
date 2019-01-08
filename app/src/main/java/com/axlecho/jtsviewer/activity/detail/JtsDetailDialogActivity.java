package com.axlecho.jtsviewer.activity.detail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.Toast;

import com.axlecho.jtsviewer.R;
import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.action.tab.JtsGtpTabAction;
import com.axlecho.jtsviewer.action.tab.JtsImgTabAction;
import com.axlecho.jtsviewer.module.JtsTabDetailModule;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.network.JtsServer;
import com.axlecho.jtsviewer.untils.JtsTextUnitls;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class JtsDetailDialogActivity extends AppCompatActivity {

    private List<Disposable> disposables = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_jts_detail_dialog);
        // getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        // getWindow().setGravity(Gravity.CENTER);
        getTabDetail();
    }

    @Override
    protected void onDestroy() {
        for (Disposable disposable:disposables) {
            disposable.dispose();
        }
        super.onDestroy();
    }

    public void getTabDetail() {
        final JtsTabInfoModel info = (JtsTabInfoModel) this.getIntent().getSerializableExtra("tabinfo");
        long tabKey = JtsTextUnitls.getTabKeyFromUrl(info.url);
        Disposable disposable = JtsServer.getSingleton(this).getDetail(tabKey)
                .subscribe(new Consumer<JtsTabDetailModule>() {
                    @Override
                    public void accept(JtsTabDetailModule jtsTabDetailModule) throws Exception {
                        processTabPlay(jtsTabDetailModule, info);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(JtsDetailDialogActivity.this,"some error happen",Toast.LENGTH_LONG).show();
                    }
                });
        disposables.add(disposable);
    }

    public void processTabPlay(JtsTabDetailModule detail,JtsTabInfoModel info) {
        JtsBaseAction action;
        long gid = JtsTextUnitls.getTabKeyFromUrl(info.url);
        if (detail.gtpUrl != null) {
            action = new JtsGtpTabAction(this, gid, detail.gtpUrl,info);

        } else if (detail.imgUrls != null && detail.imgUrls.size() != 0) {
            action = new JtsImgTabAction(this, gid, detail.imgUrls);
        } else {
            action = new JtsBaseAction() {
                @Override
                public void processAction() {
                    Toast.makeText(JtsDetailDialogActivity.this,"no support ",Toast.LENGTH_LONG).show();
                    JtsDetailDialogActivity.this.finish();
                }
            };
        }
        action.execute();
        finish();
    }


}
