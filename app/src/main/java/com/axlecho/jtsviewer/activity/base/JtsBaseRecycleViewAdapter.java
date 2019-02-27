package com.axlecho.jtsviewer.activity.base;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public abstract class JtsBaseRecycleViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public enum Mode {NORMAL, SELECT}

    private Mode mode = Mode.NORMAL;

    protected List<T> data;
    protected Context context;

    protected List<OnItemClickListener<T>> clickListenerList;
    protected List<OnItemLongClickListener<T>> longClickListenerList;

    protected JtsBaseRecycleViewAdapter(Context context, List<T> data) {
        if (data == null) {
            this.data = new ArrayList<>();
        } else {
            this.data = data;
        }

        this.context = context;
        this.clickListenerList = new ArrayList<>();
        this.longClickListenerList = new ArrayList<>();
    }

    public void addData(List<T> content) {
        this.data.addAll(content);
    }

    public void clearData() {
        this.data.clear();
    }

    public List<T> getData() {
        return data;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public void addOnItemLongClickListener(OnItemLongClickListener<T> listener) {
        this.longClickListenerList.add(listener);
    }

    public void addOnItemClickListener(OnItemClickListener<T> listener) {
        this.clickListenerList.add(listener);
    }


    public interface OnItemClickListener<T> {
        void onItemClick(T module, View shareView);

        void onItemAvatarClick(T module, View shareView);
    }

    public interface OnItemLongClickListener<T> {
        void onItemLongClick(T module);
    }

    public interface OnItemSelectedListener<T> {
        void onItemSelected(T module);
    }


    public void enableSelectMode() {
        mode = Mode.SELECT;
    }

    protected class BaseItemClickListener implements View.OnClickListener {

        private T data;
        private View shareView;

        public BaseItemClickListener(T data, View shareView) {
            this.data = data;
            this.shareView = shareView;
        }

        @Override
        public void onClick(View v) {
            for (OnItemClickListener<T> listener : clickListenerList) {
                listener.onItemClick(data, shareView);
            }
        }
    }

    protected class BaseItemLongClickListener implements View.OnLongClickListener {
        private T data;

        public BaseItemLongClickListener(T data) {
            this.data = data;
        }

        @Override
        public boolean onLongClick(View v) {
            enableSelectMode();

            for (OnItemLongClickListener<T> listener : longClickListenerList) {
                listener.onItemLongClick(data);
            }
            return false;
        }
    }
}
