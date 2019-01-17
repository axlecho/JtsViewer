package com.axlecho.jtsviewer.activity.my;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.axlecho.jtsviewer.R;
import com.axlecho.jtsviewer.module.CacheModule;
import com.axlecho.jtsviewer.module.JtsTabInfoModel;
import com.axlecho.jtsviewer.untils.JtsTextUnitls;
import com.axlecho.jtsviewer.untils.JtsViewerLog;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class CacheViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "cache";
    private Context context;
    private List<CacheModule> modules;

    private List<OnItemClickListener> clickListenerList;
    private List<OnItemLongClickListener> longClickListenerList;

    public CacheViewAdapter(Context context, List<CacheModule> modules) {
        this.context = context;
        this.modules = modules;
        clickListenerList = new ArrayList<>();
        longClickListenerList = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tab, parent, false);
        return new CacheViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (!(holder instanceof CacheViewHolder)) {
            JtsViewerLog.w(TAG, "holder is not a cache view holder");
            return;
        }

        final CacheModule module = modules.get(position);
        final CacheViewHolder viewHolder = (CacheViewHolder) holder;
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (OnItemClickListener listener : clickListenerList) {
                    listener.onItemClick(module);
                }
            }
        });

        viewHolder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                for (CacheViewAdapter.OnItemLongClickListener listener : longClickListenerList) {
                    listener.onItemLongClick(module);
                }
                return true;
            }
        });

        viewHolder.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (OnItemClickListener listener : clickListenerList) {
                    listener.onItemAvatarClick(module.tabInfo, viewHolder.avatar);
                }
            }
        });

        JtsTabInfoModel tabModel = module.tabInfo;
        if (tabModel != null) {
            ((CacheViewHolder) holder).setData(tabModel);
        } else {
            ((CacheViewHolder) holder).title.setText(JtsTextUnitls.getTitleFromPath(module.path));
        }
    }

    @Override
    public int getItemCount() {
        return modules.size();
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

            TextDrawable defaultDrawable = TextDrawable.builder()
                    .beginConfig().height(48).width(48).bold().endConfig()
                    .buildRect(model.title.substring(0, 1), context.getResources().getColor(R.color.colorPrimary));
            Glide.with(context).load(model.avatar).error(defaultDrawable).into(avatar);
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

    public void addOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListenerList.add(listener);
    }

    public void addOnItemClickListener(OnItemClickListener listener) {
        this.clickListenerList.add(listener);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(CacheModule module);
    }

    public interface OnItemClickListener {
        void onItemClick(CacheModule module);

        void onItemAvatarClick(JtsTabInfoModel module, View shareView);
    }
}
