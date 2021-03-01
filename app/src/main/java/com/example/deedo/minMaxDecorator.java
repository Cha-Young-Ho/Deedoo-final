package com.example.deedo;

import android.graphics.Color;
import android.text.style.ForegroundColorSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Calendar;

public class minMaxDecorator implements DayViewDecorator {

    private final CalendarDay calendar_now;
    private final CalendarDay calendar_max;
    public minMaxDecorator(CalendarDay min, CalendarDay max) {

        this.calendar_now = min;
        this.calendar_max = max;

    }
    @Override
    public boolean shouldDecorate(CalendarDay day) {

       // day.copyTo();
       // int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
       // return weekDay == Calendar.SUNDAY;

        return false;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new ForegroundColorSpan(Color.RED));
    }
}
/*
class MinMaxDecorator(min:CalendarDay, max:CalendarDay):DayViewDecorator {
    val maxDay = max
    val minDay = min
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return (day?.month == maxDay.month && day.day > maxDay.day)
                || (day?.month == minDay.month && day.day < minDay.day)
    }
    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(object:ForegroundColorSpan(Color.parseColor("#d2d2d2")){})
        view?.setDaysDisabled(true)
    }
}
 */
