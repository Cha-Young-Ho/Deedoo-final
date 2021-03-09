package com.example.deedo.Friend;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deedo.DB.DBHelper;
import com.example.deedo.DB.DBHelperFirebase;
import com.example.deedo.R;

import java.util.ArrayList;

public class Search_Friend_Adapter extends RecyclerView.Adapter<Search_Friend_Adapter.Search_Somebody_ViewHolder> {

    private ArrayList<Search_Friend_Data> Search_Friend_Data_list;
    private Context context;
    DBHelper db;
    String userId;
    DBHelperFirebase firebase;
    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    private OnItemClickListener onItemClickListener;


    public Search_Friend_Adapter(OnItemClickListener onItemClickListener, String _id) {
        this.onItemClickListener = onItemClickListener;
        this.userId = _id;
        this.firebase = new DBHelperFirebase();
    }

    public Search_Friend_Adapter(ArrayList<Search_Friend_Data> Search_Friend_Data_list, Context context, String _id) {
        this.Search_Friend_Data_list = Search_Friend_Data_list;
        this.context = context;
        this.userId = _id;
    }

    @NonNull
    @Override
    public Search_Friend_Adapter.Search_Somebody_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 카드 레이아웃 찾고(view) -> 지정하고(holder = view) -> 지정해준 holder 넘겨줌
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_somebody, parent, false);

        Search_Somebody_ViewHolder holder = new Search_Somebody_ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Search_Friend_Adapter.Search_Somebody_ViewHolder holder, int position) {

        holder.Search_Friend_name.setText("이름 = " + Search_Friend_Data_list.get(position).getSearch_Friend_name());
        holder.Search_Friend_id.setText("아이디 = " + Search_Friend_Data_list.get(position).getSearch_Friend_id());


        holder.item_search_request_friend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                db = new DBHelper(context);


                Log.v("선택된 번호 =",""+pos);



                String Friend_Name = Search_Friend_Data_list.get(pos).getSearch_Friend_name();
                String Friend_id = Search_Friend_Data_list.get(pos).getSearch_Friend_id();
                Log.v("선택된 정보 = ", "이름 = "+Friend_Name+ " - 아이디 = " + Friend_id);

                firebase.Create_Friend(userId, Friend_id, Friend_Name);
                Search_Friend_Data_list.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, Search_Friend_Data_list.size());



            }
        });


    }

    @Override
    public int getItemCount() {
        //리스트가 null이 아니면 size, null이면 0
        return (Search_Friend_Data_list != null ? Search_Friend_Data_list.size() : 0);
    }

    public class Search_Somebody_ViewHolder extends RecyclerView.ViewHolder {

        TextView Search_Friend_name, Search_Friend_id;
        Button item_search_request_friend_btn;

        public Search_Somebody_ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.Search_Friend_name = itemView.findViewById(R.id.item_textview_plan_details_name);
            this.Search_Friend_id = itemView.findViewById(R.id.item_textview_plan_details_time);

            this.item_search_request_friend_btn = itemView.findViewById(R.id.item_plan_details_delete_btn);



        }


    }
}



