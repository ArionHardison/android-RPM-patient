package com.telehealthmanager.app.ui.calander_view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.telehealthmanager.app.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

class BookingCalanderAdapter extends RecyclerView.Adapter<BookingCalanderAdapter.MyViewHolder> {
    private ArrayList<BookingCalanderModel> list;
    private Context mCtx;
    private BookingCalanderView.OnCalendarListener onCalendarListener;
    private int selectedPosition = 0;
    private ArrayList<String> currentDate = new ArrayList<>();

    public void setOnCalendarListener(BookingCalanderView.OnCalendarListener onCalendarListener) {
        this.onCalendarListener = onCalendarListener;
    }

    public BookingCalanderAdapter(ArrayList<BookingCalanderModel> list, Context mCtx, ArrayList<String> dates) {
        this.list = list;
        this.mCtx = mCtx;
        this.currentDate = dates;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView date;
        TextView month;
        TextView day;
        LinearLayout parent;

        public MyViewHolder(View view) {
            super(view);
            date = view.findViewById(R.id.date);
            month = view.findViewById(R.id.month);
            parent = view.findViewById(R.id.parent);
            day = view.findViewById(R.id.day);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_calander_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final BookingCalanderModel model = list.get(position);
        Display display = ((Activity) mCtx).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        holder.parent.setMinimumWidth(Math.round(width / 7));

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM EEE");
        final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        holder.date.setText(sdf.format(model.getTimeinmilli()).split(" ")[0]);
        holder.month.setText(sdf.format(model.getTimeinmilli()).split(" ")[1]);
        holder.day.setText(sdf.format(model.getTimeinmilli()).split(" ")[2]);

        if (currentDate.contains(sdf.format(model.getTimeinmilli()))) {
            holder.date.setText("Today");
        }

        if (model.getStatus() == 0) {
            setUpdatedViewSelect(holder);
        } else {
            setUpdatedViewUnSelect(holder);
            selectedPosition = position;
        }

        holder.parent.setOnClickListener(view -> {
            list.get(selectedPosition).setStatus(0);
            list.get(position).setStatus(1);
            setUpdatedViewUnSelect(holder);
            notifyDataSetChanged();

            Calendar cal = Calendar.getInstance();
            cal.set(Integer.parseInt(sdf1.format(model.getTimeinmilli()).split("-")[0]),
                    Integer.parseInt(sdf1.format(model.getTimeinmilli()).split("-")[1]),
                    Integer.parseInt(sdf1.format(model.getTimeinmilli()).split("-")[2]));
            onCalendarListener.onDateSelected(cal, sdf1.format(model.getTimeinmilli()));
        });
    }

    private void setUpdatedViewSelect(MyViewHolder holder) {
        holder.date.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorButton));
        holder.month.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorButton));
        holder.day.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorButton));
        holder.parent.setBackgroundResource(R.drawable.calander_unselecte);
    }

    private void setUpdatedViewUnSelect(MyViewHolder holder) {
        holder.date.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorWhite));
        holder.month.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorWhite));
        holder.day.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorWhite));
        holder.parent.setBackgroundResource(R.drawable.calander_select);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}