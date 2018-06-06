package com.darren.danmulauncher;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

    private List<String> mSendContentsList;
    private List<String> mSelectedSendContentsList;
    private int mSendIntervals;

    public MainSettingAdapter(Set<String> sendContentSet, Set<String> selectedList, int sendIntervals) {
        super();
        mSendContentsList = new ArrayList<>(sendContentSet);
        mSelectedSendContentsList = new ArrayList<>(selectedList);
        mSendIntervals = sendIntervals;
    }

    public Set<String> getSendContents () {
        return new LinkedHashSet<>(mSendContentsList);
    }

    public int getSendIntervals() {
        return mSendIntervals;
    }

    public Set<String> getSelectedList() {
        return new LinkedHashSet<>(mSelectedSendContentsList);
    }

    public void addSendContent() {
        mSendContentsList.add(SharedPreferencesUtil.mDefSendContent);
        this.notifyItemInserted(mSendContentsList.size()+2);
    }

    public void refreshSendContents (Set<String> sendContentsSet) {
        mSendContentsList = new ArrayList<>(sendContentsSet);
        this.notifyDataSetChanged();
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof SendSwitchHolder) {
            SendSwitchHolder viewHolder = (SendSwitchHolder)holder;
        } else if (holder instanceof SendIntervalsHolder) {
            SendIntervalsHolder viewHolder = (SendIntervalsHolder)holder;
            viewHolder.tvTitle.setText("弹幕间隔(s)");
            viewHolder.etIntervals.setText(String.valueOf(mSendIntervals));
            viewHolder.etIntervals.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String string  = s.toString();
                    mSendIntervals = string.isEmpty() ? 0 : Integer.parseInt(s.toString());
                }
            });
        } else if (holder instanceof SendContentHolder) {
            final SendContentHolder viewHolder = (SendContentHolder)holder;
            viewHolder.tvSendContent.setText(mSendContentsList.get(position - 2));
            if (mSelectedSendContentsList.contains(mSendContentsList.get(position-2))) {
                viewHolder.cbisSent.setChecked(true);
            }
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
                    int index = mSendContentsList.indexOf(tempContent);
                    mSendContentsList.remove(index);
                    mSendContentsList.add(index, s.toString());
                    if (viewHolder.cbisSent.isChecked()) {
                        index = mSelectedSendContentsList.indexOf(tempContent);
                        mSelectedSendContentsList.remove(index);
                        mSelectedSendContentsList.add(index, s.toString());
                    }
                }
            });
                viewHolder.cbisSent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            mSelectedSendContentsList.add(mSendContentsList.get(position-2));
                        } else {
                            mSelectedSendContentsList.remove(mSendContentsList.get(position-2));
                        }
                    }
                });
        }
    }

    @Override
    public int getItemCount() {
        return mSendContentsList == null ? 2 : mSendContentsList.size() + 2;
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
