package com.example.deedo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Modify_Friend_Adapter extends RecyclerView.Adapter<Modify_Friend_Adapter.Modify_Friend_ViewHolder> {

    private ArrayList<Modify_Friend_Data> Modify_Friend_Data_list;
    private Context context;
    DBHelper db;
    String userId;

    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    private OnItemClickListener onItemClickListener;


    public Modify_Friend_Adapter(OnItemClickListener onItemClickListener, String _id) {
        this.onItemClickListener = onItemClickListener;
        this.userId = _id;
    }

    public Modify_Friend_Adapter(ArrayList<Modify_Friend_Data> modify_Friend_Data_list, Context context, String _id) {
        this.Modify_Friend_Data_list = modify_Friend_Data_list;
        this.context = context;
        this.userId = _id;
    }

    @NonNull
    @Override
    public Modify_Friend_Adapter.Modify_Friend_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 카드 레이아웃 찾고(view) -> 지정하고(holder = view) -> 지정해준 holder 넘겨줌
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);

        Modify_Friend_ViewHolder holder = new Modify_Friend_ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Modify_Friend_Adapter.Modify_Friend_ViewHolder holder, int position) {

        holder.Modify_Friend_name.setText("이름 = " + Modify_Friend_Data_list.get(position).getModify_Friend_name());
        holder.Modify_Friend_id.setText("아이디 = " + Modify_Friend_Data_list.get(position).getModify_Friend_id());
        
        
        /*
        친구 구경가기 버튼 클릭 시
         */
        holder.item_inquiry_friend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //구경가기 버튼 - 그래프 구현 후에 작성
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            }
        });

         /*
        친구 끊기 버튼 클릭 시
         */
        holder.item_delete_friend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                db = new DBHelper(context);


                Log.v("선택된 번호 =", "" + pos);


                String Friend_Name = Modify_Friend_Data_list.get(pos).getModify_Friend_name();
                String Friend_id = Modify_Friend_Data_list.get(pos).getModify_Friend_id();
                Log.v("선택된 정보 = ", "이름 = " + Friend_Name + " - 아이디 = " + Friend_id);
                db.Delete_Friend(userId, Friend_id);
                Modify_Friend_Data_list.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, Modify_Friend_Data_list.size());


            }
        });


    }

    @Override
    public int getItemCount() {
        //리스트가 null이 아니면 size, null이면 0
        return (Modify_Friend_Data_list != null ? Modify_Friend_Data_list.size() : 0);
    }

    public class Modify_Friend_ViewHolder extends RecyclerView.ViewHolder {

        TextView Modify_Friend_name, Modify_Friend_id;
        Button item_inquiry_friend_btn;
        Button item_delete_friend_btn;

        public Modify_Friend_ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.Modify_Friend_name = itemView.findViewById(R.id.item_textView_modify_friend_name);
            this.Modify_Friend_id = itemView.findViewById(R.id.item_textView_modify_friend_id);

            this.item_inquiry_friend_btn = itemView.findViewById(R.id.item_inquiry_friend_btn);

            this.item_delete_friend_btn = itemView.findViewById(R.id.item_modify_delete_friend_btn);


        }


    }
}



