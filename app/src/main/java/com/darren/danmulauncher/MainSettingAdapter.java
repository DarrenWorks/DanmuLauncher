package com.darren.danmulauncher;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

// TODO: 2018/5/18 加入fab作为添加弹幕按钮， 在无障碍服务中获取弹幕内容及时间 
public class MainSettingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_SEND_SWITCH = 0;
    private static final int VIEW_TYPE_SEND_INTERVALS = 1;
    private static final int VIEW_TYPE_SEND_CONTENTS = 2;

    private List<String> mSendContenstList;

    public MainSettingAdapter(Set<String> sendContentSet) {
        super();
        mSendContenstList = new ArrayList<>(sendContentSet);
    }

    public Set<String> getSendContents () {
        return new LinkedHashSet<>(mSendContenstList);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        switch (viewType) {
            case VIEW_TYPE_SEND_SWITCH: {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_send_switch_item, parent, false);
                viewHolder = new SendSwitchHolder(v);
                break;
            }
            case VIEW_TYPE_SEND_INTERVALS: {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_send_intervals_item, parent, false);
                viewHolder = new SendIntervalsHolder(v);
                break;
            }
            case VIEW_TYPE_SEND_CONTENTS: {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_send_contents_item, parent, false);
                viewHolder = new SendContentHolder(v);
                break;
            }
            default: {//same as VIEW_TYPE_SEND_CONTENTS
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_send_contents_item, parent, false);
                viewHolder = new SendContentHolder(v);
                break;
            }
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SendSwitchHolder) {
            SendSwitchHolder viewHolder = (SendSwitchHolder)holder;
        } else if (holder instanceof SendIntervalsHolder) {
            SendIntervalsHolder viewHolder = (SendIntervalsHolder)holder;
        } else if (holder instanceof SendContentHolder) {
            SendContentHolder viewHolder = (SendContentHolder)holder;
            viewHolder.tvSendContent.setText(mSendContenstList.get(position - 2));
            viewHolder.tvSendContent.addTextChangedListener(new TextWatcher() {
                private String tempContent;
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    tempContent = s.toString();
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    int index = mSendContenstList.indexOf(tempContent);
                    mSendContenstList.remove(index);
                    mSendContenstList.add(index, s.toString());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mSendContenstList == null ? 2 : mSendContenstList.size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position <2 ? position : VIEW_TYPE_SEND_CONTENTS;
    }

    static class SendSwitchHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        Switch swSend;

        public SendSwitchHolder(View view) {
            super(view);

            tvTitle = view.findViewById(R.id.tvTitle);
            swSend = view.findViewById(R.id.swSend);
        }
    }

    static class SendIntervalsHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        EditText etIntervals;

        public SendIntervalsHolder(View view) {
            super(view);

            tvTitle = view.findViewById(R.id.tvTitle);
            etIntervals = view.findViewById(R.id.etIntervals);
        }
    }

    static class SendContentHolder extends RecyclerView.ViewHolder {
        TextView tvSendContent;
        CheckBox cbisSent;

        public SendContentHolder(View view) {
            super(view);

            tvSendContent = view.findViewById(R.id.etSendContent);
            cbisSent = view.findViewById(R.id.cbIsSent);
        }
    }
}
