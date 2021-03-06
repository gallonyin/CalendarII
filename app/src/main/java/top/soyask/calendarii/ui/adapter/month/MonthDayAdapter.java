package top.soyask.calendarii.ui.adapter.month;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.util.List;

import top.soyask.calendarii.R;
import top.soyask.calendarii.domain.Day;
import top.soyask.calendarii.global.Setting;

import static top.soyask.calendarii.global.Global.VIEW_DAY;
import static top.soyask.calendarii.global.Global.VIEW_TODAY;
import static top.soyask.calendarii.global.Global.VIEW_WEEK;

public class MonthDayAdapter extends BaseAdapter {

    private List<Day> mDays;
    private int mDateStartPos;
    private int mEndPosition;
    private static final String[] WEEK_ARRAY = {"日", "一", "二", "三", "四", "五", "六",};


    public MonthDayAdapter(List<Day> days) {
        this.mDays = days;
        updateCount();
    }


    @Override
    public int getCount() {
        return 49;
    }

    @Override
    public String getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        switch (type) {
            case VIEW_WEEK:
                int index = (position + Setting.date_offset) % WEEK_ARRAY.length;
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_widget_week, parent, false);
                ((TextView) convertView).setText(WEEK_ARRAY[index]);
                break;
            case VIEW_TODAY:
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_widget_today, parent, false);
                break;
            case VIEW_DAY:
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_widget_day, parent, false);
                break;
        }
        if (position >= mDateStartPos && position < mEndPosition && position - mDateStartPos < mDays.size()) {
            Day day = mDays.get(position - mDateStartPos);
            boolean hasBirthday = day.hasBirthday();
            ((TextView) convertView.findViewById(R.id.tv_lunar)).setText(hasBirthday ? "生日" : day.getLunar().getSimpleLunar());
            ((TextView) convertView.findViewById(R.id.tv_greg)).setText(String.valueOf(day.getDayOfMonth()));
            convertView.findViewById(R.id.iv_birth).setVisibility(hasBirthday ? View.VISIBLE : View.INVISIBLE);
            convertView.findViewById(R.id.fl_event).setVisibility(day.hasEvent() ? View.VISIBLE : View.INVISIBLE);
        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        int type = VIEW_DAY;
        if (position >= mDateStartPos && position < mEndPosition && position - mDateStartPos < mDays.size()) {
            Day day = mDays.get(position - mDateStartPos);
            if (day.isToday()) {
                return VIEW_TODAY;
            }
        }
        type = position < 7 ? VIEW_WEEK : type;
        return type;
    }

    private void updateCount() {
        if (mDays.size() > 0) {
            this.mDateStartPos = (mDays.get(0).getDayOfWeek() + 6 - Setting.date_offset) % 7 + 7;
        } else {
            this.mDateStartPos = 6;
        }
        this.mEndPosition = mDateStartPos + mDays.size();
    }
}
