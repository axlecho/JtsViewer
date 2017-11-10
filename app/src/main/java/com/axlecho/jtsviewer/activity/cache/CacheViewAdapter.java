package com.axlecho.jtsviewer.activity.cache;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.axlecho.jtsviewer.R;
import com.axlecho.jtsviewer.action.JtsBaseAction;
import com.axlecho.jtsviewer.action.ui.JtsShowGtpTabAction;
import com.axlecho.jtsviewer.module.CacheModule;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.untils.JtsTextUnitls;
import com.axlecho.jtsviewer.untils.JtsViewerLog;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CacheViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "cache";
    private Context context;
    private List<CacheModule> modules;

    public CacheViewAdapter(Context context, List<CacheModule> modules) {
        this.context = context;
        this.modules = modules;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tab, parent, false);
        return new CacheViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        CacheModule module = modules.get(position);
        ((CacheViewHolder) holder).cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processClickAction(position);
            }
        });

        JtsTabInfoModel tabModel = module.tabInfo;
        if (tabModel != null) {
            ((CacheViewHolder) holder).setData(tabModel);
        } else {
            ((CacheViewHolder) holder).title.setText(JtsTextUnitls.removePostfixFromFileName(module.fileName));
        }
    }

    @Override
    public int getItemCount() {
        return modules.size();
    }


    private void processClickAction(int position) {
        CacheModule module = modules.get(position);

        module.frequency++;
        try {
            module.writeToFile();
        } catch (IOException e) {
            JtsViewerLog.e(TAG, "update tab info failed");
        }

        JtsShowGtpTabAction action = new JtsShowGtpTabAction();
        action.setKey(JtsBaseAction.CONTEXT_KEY, this.context.getApplicationContext());
        action.setKey(JtsShowGtpTabAction.GTP_FILE_PATH, module.path + File.separator + module.fileName);
        action.processAction();
    }

    private class CacheViewHolder extends RecyclerView.ViewHolder {
        public View cardView;
        private TextView title;
        private TextView author;
        private TextView time;
        private TextView reply;
        private TextView watch;
        private ImageView type;
        private TextView uper;
        private ImageView avatar;

        public CacheViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tab_item_title);
            author = (TextView) view.findViewById(R.id.tab_item_author);
            time = (TextView) view.findViewById(R.id.tab_item_time);
            reply = (TextView) view.findViewById(R.id.tab_item_reply);
            watch = (TextView) view.findViewById(R.id.tab_item_watch);
            type = (ImageView) view.findViewById(R.id.tab_item_type);
            uper = (TextView) view.findViewById(R.id.tab_item_uper);
            avatar = (ImageView) view.findViewById(R.id.tab_item_avatar);
            cardView = view.findViewById(R.id.tab_item_view);
        }

        public void setData(JtsTabInfoModel model) {
            title.setText(model.title);
            author.setText(model.author);
            time.setText(model.time);
            reply.setText(context.getResources().getString(R.string.reply) + ":" + model.reply);
            watch.setText(context.getResources().getString(R.string.watch) + ":" + model.watch);
            uper.setText(model.uper);
            this.setType(model.type);

            Picasso.with(context).load(model.avatar).error(R.drawable.ic_launcher).into(avatar);
        }

        private void setType(String tabType) {
            if (tabType.equals("GTP谱")) {
                type.setImageResource(R.mipmap.ic_gtp);
            } else if (tabType.equals("和弦谱")) {
                type.setImageResource(R.mipmap.ic_chord);
            } else if (tabType.equals("图片谱")) {
                type.setImageResource(R.mipmap.ic_img);
            } else if (tabType.equals("PDF谱")) {
                type.setImageResource(R.mipmap.ic_pdf);
            }
        }
    }
}
