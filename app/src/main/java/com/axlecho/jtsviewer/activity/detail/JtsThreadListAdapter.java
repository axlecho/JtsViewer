package com.axlecho.jtsviewer.activity.detail;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.axlecho.jtsviewer.R;
import com.axlecho.jtsviewer.module.JtsThreadCommentModule;
import com.axlecho.jtsviewer.module.JtsThreadModule;
import com.axlecho.jtsviewer.untils.JtsImageGetter;
import com.axlecho.jtsviewer.untils.JtsTagHandler;
import com.bumptech.glide.Glide;
import com.pixplicity.htmlcompat.HtmlCompat;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import static com.pixplicity.htmlcompat.HtmlCompat.FROM_HTML_MODE_LEGACY;

/**
 * Created by Administrator on 2017/11/7.
 */

public class JtsThreadListAdapter extends RecyclerView.Adapter<JtsThreadListAdapter.ThreadViewHolder> {

    private List<JtsThreadModule> threads = new ArrayList<>();
    private Context context;

    public JtsThreadListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ThreadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_thread, parent, false);
        return new JtsThreadListAdapter.ThreadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ThreadViewHolder holder, int position) {
        holder.bindData(threads.get(position));
    }

    @Override
    public int getItemCount() {
        return threads.size();
    }

    public void addData(List<JtsThreadModule> threads) {
        this.threads = threads;
    }

    public void clear() {
        this.threads.clear();
        notifyDataSetChanged();
    }

    public class ThreadViewHolder extends ViewHolder {
        public ImageView avatar;
        public TextView auth;
        public TextView time;
        public TextView message;
        public TextView floor;
        public LinearLayout comments;

        public ThreadViewHolder(View itemView) {
            super(itemView);

            avatar = (ImageView) itemView.findViewById(R.id.thread_item_avatar);
            auth = (TextView) itemView.findViewById(R.id.thread_item_auth);
            time = (TextView) itemView.findViewById(R.id.thread_item_time);
            message = (TextView) itemView.findViewById(R.id.thread_item_message);
            floor = (TextView) itemView.findViewById(R.id.thread_item_floor);
            comments = (LinearLayout) itemView.findViewById(R.id.thread_item_comment_layout);
        }

        public void bindData(JtsThreadModule model) {
            if (model.avatar == null) {
                model.avatar = context.getResources().getString(R.string.unknown_author);
            }

            TextDrawable defaultDrawable = TextDrawable.builder()
                    .beginConfig().height(48).width(48).bold().endConfig()
                    .buildRect(model.authi.substring(0, 1), context.getResources().getColor(R.color.colorPrimary));
            Glide.with(context).load(model.avatar).error(defaultDrawable).into(avatar);
            auth.setText(model.authi);
            time.setText(model.time);
            message.setText(HtmlCompat.fromHtml(context, model.message,
                    FROM_HTML_MODE_LEGACY,
                    new JtsImageGetter(message),
                    new JtsTagHandler(context, message, model.message)));
            message.setMovementMethod(LinkMovementMethod.getInstance());

            floor.setText(model.floor);
            if (model.comments.size() > 0) {
                comments.removeAllViews();
                for (JtsThreadCommentModule commentModel : model.comments) {
                    bindComment(comments, commentModel);
                }
                comments.setVisibility(View.VISIBLE);
            }
        }

        public void bindComment(LinearLayout layout, JtsThreadCommentModule model) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_thread, layout, false);
            layout.addView(view);
            ImageView avatar = (ImageView) view.findViewById(R.id.thread_item_avatar);
            TextView auth = (TextView) view.findViewById(R.id.thread_item_auth);
            TextView time = (TextView) view.findViewById(R.id.thread_item_time);
            TextView message = (TextView) view.findViewById(R.id.thread_item_message);
            view.findViewById(R.id.thread_item_floor).setVisibility(View.GONE);

            TextDrawable defaultDrawable = TextDrawable.builder()
                    .beginConfig().height(48).width(48).bold().endConfig()
                    .buildRect(model.authi.substring(0, 1), context.getResources().getColor(R.color.colorPrimary));
            Glide.with(context).load(model.avatar).error(defaultDrawable).into(avatar);
            auth.setText(model.authi);
            time.setText(model.time);
            message.setText(model.message);
        }
    }
}
