package com.twtstudio.retrox.schedule.view;

import android.databinding.ObservableField;

import com.kelin.mvvmlight.base.ViewModel;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.twtstudio.retrox.schedule.TimeHelper;
import com.twtstudio.retrox.schedule.model.ClassTable;

/**
 * Created by zhangyulong on 7/9/17.
 */

public class CourseIsEmptyViewModel implements ViewModel {
    public final ObservableField<String> text = new ObservableField<>();
    private String day;

    CourseIsEmptyViewModel(CalendarDay calendarDay, ClassTable classTable) {
        long startUnix = Long.parseLong(classTable.data.term_start);
        int presentWeek = TimeHelper.getWeekInt(startUnix, calendarDay.getCalendar());
        if (presentWeek > 20)
            text.set("这个学期已经过去了");
        else {
            if (calendarDay.equals(CalendarDay.today()))
                day = "今日";
            else day = "该日";
            text.set(day + "无课，做点有意思的事吧~");
        }
    }
}
