package com.example.deedo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Friend_recyclerview_Adapter extends RecyclerView.Adapter<Friend_recyclerview_Adapter.FriendViewHolder> {

    private ArrayList<Friend_Data> Friend_List;
    private Context context;
    DBHelper db;
    String userId;
    Modify_Friend Modify_Friend;

    public interface OnItemClickListener {
        void onItemClick(View v, int pos);

    }

    private OnItemClickListener onItemClickListener;


    public Friend_recyclerview_Adapter(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;

        this.Modify_Friend = new Modify_Friend();
        this.userId = Modify_Friend.userId;

    }

    public Friend_recyclerview_Adapter(ArrayList<Friend_Data> friend_List, Context context) {
        Friend_List = friend_List;
        this.context = context;
    }

    @NonNull
    @Override
    public Friend_recyclerview_Adapter.FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 카드 레이아웃 찾고(view) -> 지정하고(holder = view) -> 지정해준 holder 넘겨줌
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);

        FriendViewHolder holder = new FriendViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Friend_recyclerview_Adapter.FriendViewHolder holder, int position) {

        holder.Friend_name.setText(Friend_List.get(position).getFriend_id());
        holder.Friend_id.setText(Friend_List.get(position).getFriend_name());


        holder.item_delete_friend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                db = new DBHelper(context);

                // Log.v("선택된 번호 =",""+pos);
                //Log.v("선택된 곳의 정보",""+ Friend_List.get(pos).getFriend_id()+ " - "+Friend_List.get(pos).getFriend_name());
                String FriendName = Friend_List.get(pos).getFriend_name();
                String FriendId = Friend_List.get(pos).getFriend_id();

                db.Delete_Friend(userId, FriendId);
                Friend_List.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, Friend_List.size());


            }
        });

        holder.item_inquiry_friend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                db = new DBHelper(context);


            }
        });


    }

    @Override
    public int getItemCount() {
        //리스트가 null이 아니면 size, null이면 0
        return (Friend_List != null ? Friend_List.size() : 0);
    }

    public class FriendViewHolder extends RecyclerView.ViewHolder {

        TextView Friend_name, Friend_id;
        Button item_inquiry_friend_btn, item_delete_friend_btn;


        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);

            this.Friend_id = itemView.findViewById(R.id.item_textview_plan_details_time);
            this.Friend_name = itemView.findViewById(R.id.item_textview_plan_details_name);

            this.item_inquiry_friend_btn = itemView.findViewById(R.id.item_inquiry_friend_btn);
            this.item_delete_friend_btn = itemView.findViewById(R.id.item_plan_details_delete_btn);


        }
    }
}



