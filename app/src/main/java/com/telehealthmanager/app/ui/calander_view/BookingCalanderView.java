package com.telehealthmanager.app.ui.calander_view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.telehealthmanager.app.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class BookingCalanderView extends LinearLayout {
    Context context;
    RecyclerView recyclerView;
    ArrayList<BookingCalanderModel> list = new ArrayList<>();
    private int mFirstCompleteVisibleItemPosition = -1;
    private int mLastCompleteVisibleItemPosition = -1;

    public interface OnCalendarListener {
        void onDateSelected(Calendar cal, String dateStr);
    }

    public BookingCalanderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public BookingCalanderView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public BookingCalanderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.book_calander_view, null);

        TextView textView = view.findViewById(R.id.text);
        recyclerView = view.findViewById(R.id.re);

        view.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        addView(view);
    }

    public void setUpCalendar(long start, long end, ArrayList<String> dates, OnCalendarListener onCalendarListener) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(start);

        long today = Tools.getTimeInMillis(Tools.getFormattedDateToday());

        long current = start;
        int i = 0;
        int pos = 0;
        while (current < end) {
            Calendar c1 = Calendar.getInstance();
            c1.setTimeInMillis(start);
            c1.add(Calendar.DATE, i);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            if (sdf.format(c1.getTimeInMillis()).equalsIgnoreCase(sdf.format(today))) {
                pos = i;
            }
            BookingCalanderModel model = new BookingCalanderModel(c1.getTimeInMillis());
            if (dates.contains(sdf.format(c1.getTimeInMillis()))) {
                model.setStatus(1);
            }
            list.add(model);
            current = c1.getTimeInMillis();
            i++;
        }

        BookingCalanderAdapter adapter = new BookingCalanderAdapter(list, context, dates);
        adapter.setOnCalendarListener(onCalendarListener);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(pos);

    }
}
