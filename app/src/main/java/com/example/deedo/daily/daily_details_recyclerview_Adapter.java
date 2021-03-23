package com.example.deedo.daily;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deedo.DB.DBHelperFirebase;
import com.example.deedo.R;

import java.util.ArrayList;

public class daily_details_recyclerview_Adapter extends RecyclerView.Adapter<daily_details_recyclerview_Adapter.Daily_Details_ViewHolder> {

    private ArrayList<daily_data> daily_details_data_List;
    private Context context;
    String userId;
    String[] DATE;
    DBHelperFirebase firebase = new DBHelperFirebase();

    public interface OnItemClickListener {
        void onItemClick(View v, int pos);

    }

    private OnItemClickListener onItemClickListener;


    public daily_details_recyclerview_Adapter(ArrayList<daily_data> Daily_details_data_List, Context context, String userId, String[] DATE) {
        this.daily_details_data_List = Daily_details_data_List;
        this.context = context;
        this.userId = userId;
        this.DATE = DATE;
    }
    @NonNull
    @Override
    public daily_details_recyclerview_Adapter.Daily_Details_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 카드 레이아웃 찾고(view) -> 지정하고(holder = view) -> 지정해준 holder 넘겨줌
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_daily_details_dialog, parent, false);

        Daily_Details_ViewHolder holder = new Daily_Details_ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull daily_details_recyclerview_Adapter.Daily_Details_ViewHolder holder, int position) {
        //////////////////////////////daily NAME, 수정 다이얼로그에서 DB작업 구현해야함

        //받아온 daily 이름을 리사이클뷰에 배치
        holder.daily_tag.setText(daily_details_data_List.get(position).getArea_tag());

        int minute;
        int hour;
        int second;

        hour = ((Integer.parseInt(daily_details_data_List.get(position).getSecond())/60)/60);
        minute = ((Integer.parseInt(daily_details_data_List.get(position).getSecond())/60));
        second = ((Integer.parseInt(daily_details_data_List.get(position).getSecond()))%60);

        holder.executing_time_hour.setText(hour + "시 " + minute + "분");
        holder.executing_time_minute.setText(second + "초");
        
        //받아온 daily 할당 시간을 리사이클 뷰에 배치
        

        /*
        받아온 daily name에 따라 이미지 배치
         */


        if ((daily_details_data_List.get(position).getArea_tag()).contains("공부")){
            holder.item_imageview_daily_details.setImageResource(R.drawable.study);

        }else if(daily_details_data_List.get(position).getArea_tag().contains("운동")) {
            holder.item_imageview_daily_details.setImageResource(R.drawable.exercise);

        }else if(daily_details_data_List.get(position).getArea_tag().contains("학교")) {
            holder.item_imageview_daily_details.setImageResource(R.drawable.school2);

        }else if(daily_details_data_List.get(position).getArea_tag().contains("근무")) {
            holder.item_imageview_daily_details.setImageResource(R.drawable.work);

        }else{
            holder.item_imageview_daily_details.setImageResource(R.drawable.default_);

        }



    }

    @Override
    public int getItemCount() {
        //리스트가 null이 아니면 size, null이면 0
        return (daily_details_data_List != null ? daily_details_data_List.size() : 0);
    }

    public class Daily_Details_ViewHolder extends RecyclerView.ViewHolder {


        TextView daily_tag, executing_time_hour, executing_time_minute;

        ImageView item_imageview_daily_details;




        public Daily_Details_ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.daily_tag = itemView.findViewById(R.id.item_textview_daily_details_name);
            this.executing_time_hour = itemView.findViewById(R.id.item_textview_daily_details_time_hour);
            this.executing_time_minute = itemView.findViewById(R.id.item_textview_daily_details_time_minute);
            this.item_imageview_daily_details = itemView.findViewById(R.id.item_imageview_daily_details);

        }
    }
}



