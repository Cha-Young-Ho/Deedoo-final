package com.example.deedo.daily;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
        holder.daily_name.setText(daily_details_data_List.get(position).getDaily_name());
        holder.executing_time_hour.setText(daily_details_data_List.get(position).getSecond());
        
        //받아온 daily 할당 시간을 리사이클 뷰에 배치
        

        /*
        받아온 daily name에 따라 이미지 배치
         */

        if ((daily_details_data_List.get(position).getDaily_name()).contains("공부") ||
                daily_details_data_List.get(position).getDaily_name().contains("학습") ||
                daily_details_data_List.get(position).getDaily_name().contains("수강") ||
                daily_details_data_List.get(position).getDaily_name().contains("스터디") ||
                daily_details_data_List.get(position).getDaily_name().contains("study") ||
                daily_details_data_List.get(position).getDaily_name().contains("숙제") ||
                daily_details_data_List.get(position).getDaily_name().contains("homework")){
            holder.item_imageview_daily_details.setImageResource(R.drawable.study);

        }else if(daily_details_data_List.get(position).getDaily_name().contains("운동")||
                daily_details_data_List.get(position).getDaily_name().contains("축구")||
                daily_details_data_List.get(position).getDaily_name().contains("헬스") ||
                daily_details_data_List.get(position).getDaily_name().contains("등산") ||
                daily_details_data_List.get(position).getDaily_name().contains("자전거")||
                daily_details_data_List.get(position).getDaily_name().contains("exercise")) {
            holder.item_imageview_daily_details.setImageResource(R.drawable.exercise);

        }else if(daily_details_data_List.get(position).getDaily_name().contains("학교")||
                daily_details_data_List.get(position).getDaily_name().contains("학원")||
                daily_details_data_List.get(position).getDaily_name().contains("강의") ||
                daily_details_data_List.get(position).getDaily_name().contains("수업") ||
                daily_details_data_List.get(position).getDaily_name().contains("school")) {
            holder.item_imageview_daily_details.setImageResource(R.drawable.school2);

        }else if(daily_details_data_List.get(position).getDaily_name().contains("일")||
                daily_details_data_List.get(position).getDaily_name().contains("회사")||
                daily_details_data_List.get(position).getDaily_name().contains("프레젠테이션") ||
                daily_details_data_List.get(position).getDaily_name().contains("work")) {
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

        Button item_daily_details_modify_btn, item_daily_details_delete_btn;

        TextView daily_name, executing_time_hour, executing_time_minute;

        ImageView item_imageview_daily_details;




        public Daily_Details_ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.daily_name = itemView.findViewById(R.id.item_textview_daily_details_name);
            this.executing_time_hour = itemView.findViewById(R.id.item_textview_daily_details_time_hour);
            this.executing_time_minute = itemView.findViewById(R.id.item_textview_daily_details_time_minute);
            this.item_imageview_daily_details = itemView.findViewById(R.id.item_imageview_daily_details);


        }
    }
}



