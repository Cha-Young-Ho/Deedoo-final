package com.example.deedo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Plan_details_recyclerview_Adapter extends RecyclerView.Adapter<Plan_details_recyclerview_Adapter.Plan_Details_ViewHolder> {

    private ArrayList<Plan_details_Data> Plan_details_data_List;
    private Context context;
    DBHelper db;
    String userId;
    String[] DATE;


    public interface OnItemClickListener {
        void onItemClick(View v, int pos);

    }

    private OnItemClickListener onItemClickListener;


    public Plan_details_recyclerview_Adapter(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;




    }

    public Plan_details_recyclerview_Adapter(ArrayList<Plan_details_Data> Plan_details_data_List, Context context, String userId, String[] DATE) {
        this.Plan_details_data_List = Plan_details_data_List;
        this.context = context;
        this.userId = userId;
        this.DATE = DATE;
    }

    @NonNull
    @Override
    public Plan_details_recyclerview_Adapter.Plan_Details_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 카드 레이아웃 찾고(view) -> 지정하고(holder = view) -> 지정해준 holder 넘겨줌
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plan_details_dialog, parent, false);

        Plan_Details_ViewHolder holder = new Plan_Details_ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Plan_details_recyclerview_Adapter.Plan_Details_ViewHolder holder, int position) {
        //////////////////////////////PLAN NAME, 수정 다이얼로그에서 DB작업 구현해야함
        
        
        //받아온 plan 이름을 리사이클뷰에 배치
        holder.plan_name.setText(Plan_details_data_List.get(position).getPlan_name());
        
        //받아온 plan 할당 시간을 리사이클 뷰에 배치
        ////////////////////////holder.executing_time.setText(Plan_details_data_List.get(position).getExecuting_time());
        
        
        /*
        받아온 plan name에 따라 이미지 배치
         */
        if ((Plan_details_data_List.get(position).getPlan_name()).contains("공부") ||
                Plan_details_data_List.get(position).getPlan_name().contains("학습") ||
                Plan_details_data_List.get(position).getPlan_name().contains("수강") ||
                Plan_details_data_List.get(position).getPlan_name().contains("스터디") ||
                Plan_details_data_List.get(position).getPlan_name().contains("study") ||
                Plan_details_data_List.get(position).getPlan_name().contains("숙제") ||
                Plan_details_data_List.get(position).getPlan_name().contains("homework")){
            holder.item_imageview_plan_details.setImageResource(R.drawable.study);

        }else if(Plan_details_data_List.get(position).getPlan_name().contains("운동")||
                 Plan_details_data_List.get(position).getPlan_name().contains("축구")||
                Plan_details_data_List.get(position).getPlan_name().contains("헬스") ||
                Plan_details_data_List.get(position).getPlan_name().contains("등산") ||
                Plan_details_data_List.get(position).getPlan_name().contains("자전거")) {
            holder.item_imageview_plan_details.setImageResource(R.drawable.exercise);

        }else if(Plan_details_data_List.get(position).getPlan_name().contains("학교")||
                Plan_details_data_List.get(position).getPlan_name().contains("학원")||
                Plan_details_data_List.get(position).getPlan_name().contains("강의") ||
                Plan_details_data_List.get(position).getPlan_name().contains("수업") ||
                 Plan_details_data_List.get(position).getPlan_name().contains("school")) {
            holder.item_imageview_plan_details.setImageResource(R.drawable.school2);

        }else if(Plan_details_data_List.get(position).getPlan_name().contains("일")||
                Plan_details_data_List.get(position).getPlan_name().contains("회사")||
                Plan_details_data_List.get(position).getPlan_name().contains("프레젠테이션") ||
                Plan_details_data_List.get(position).getPlan_name().contains("work")) {
            holder.item_imageview_plan_details.setImageResource(R.drawable.work);

        }else{
            holder.item_imageview_plan_details.setImageResource(R.drawable.default_);

        }

        /*
        plan 삭제 버튼 클릭 이벤트
         */
        holder.item_plan_details_delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                db = new DBHelper(context);

                String Plan_name = Plan_details_data_List.get(pos).getPlan_name();
                int executing_hour = Plan_details_data_List.get(pos).getExecuting_hour();
                int executing_minute = Plan_details_data_List.get(pos).getExecuting_minute();

                db.Delete_Plan_Details(userId, Plan_name);
                Plan_details_data_List.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, Plan_details_data_List.size());


            }
        });

        // plan 수정 버튼 클릭 이벤트 -> dialog 생성
        holder.item_plan_details_modify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                db = new DBHelper(context);
                String before_plan_name = Plan_details_data_List.get(pos).getPlan_name();
                int before_plan_executing_hour = Plan_details_data_List.get(pos).getExecuting_hour();
                int before_plan_executing_minute = Plan_details_data_List.get(pos).getExecuting_minute();
                
                /*
                Plan detail modify 다이얼로그 생성
                 */
            Dialog_Plan_details_modify dialog_plan_details_modify =
                    new Dialog_Plan_details_modify(
                            Plan_details_recyclerview_Adapter.this.context, userId, DATE, before_plan_name, before_plan_executing_hour, before_plan_executing_minute);




            }
        });


    }

    @Override
    public int getItemCount() {
        //리스트가 null이 아니면 size, null이면 0
        return (Plan_details_data_List != null ? Plan_details_data_List.size() : 0);
    }

    public class Plan_Details_ViewHolder extends RecyclerView.ViewHolder {

        Button item_plan_details_modify_btn, item_plan_details_delete_btn;

        TextView plan_name, executing_time;

        ImageView item_imageview_plan_details;




        public Plan_Details_ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.plan_name = itemView.findViewById(R.id.item_textview_plan_details_time);
            this.executing_time = itemView.findViewById(R.id.item_textview_plan_details_name);

            this.item_plan_details_modify_btn = itemView.findViewById(R.id.item_imageview_plan_details_modify_btn);
            this.item_plan_details_delete_btn = itemView.findViewById(R.id.item_plan_details_delete_btn);

            this.item_imageview_plan_details = itemView.findViewById(R.id.item_imageview_plan_details);


        }
    }
}



