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

public class Search_Friend_Adapter extends RecyclerView.Adapter<Search_Friend_Adapter.Search_Somebody_ViewHolder> {

    private ArrayList<Search_Friend_Data> Search_Friend_Data_list;
    private Context context;
    DBHelper db;

    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    private OnItemClickListener onItemClickListener;


    public Search_Friend_Adapter(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public Search_Friend_Adapter(ArrayList<Search_Friend_Data> Search_Friend_Data_list, Context context) {
        this.Search_Friend_Data_list = Search_Friend_Data_list;
        this.context = context;
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

        holder.Search_Friend_name.setText(Search_Friend_Data_list.get(position).getSearch_Friend_name());
        holder.Search_Friend_id.setText(Search_Friend_Data_list.get(position).getSearch_Friend_id());


        holder.item_search_request_friend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                db = new DBHelper(context);

                /*
                Log.v("선택된 번호 =",""+pos);
                Log.v("선택된 곳의 정보",""+ Search_Friend_Data_list.get(pos).getTextView_name()+ " - "+Search_Friend_Data_list.get(pos).getTextView_latitude()+" - " + Search_Friend_Data_list.get(pos).getTextView_longitude());
                String AreaName = Search_Friend_Data_list.get(pos).getTextView_name();
                String AreaLatitude = Search_Friend_Data_list.get(pos).getTextView_latitude();
                String AreaLongitude = Search_Friend_Data_list.get(pos).getTextView_longitude();



                db.Delete_Area(AreaName, AreaLatitude, AreaLongitude);
                Search_Friend_Data_list.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, Search_Friend_Data_list.size());
                */


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
            this.Search_Friend_name = itemView.findViewById(R.id.item_textView_search_friend_name);
            this.Search_Friend_id = itemView.findViewById(R.id.item_textView_search_friend_id);

            this.item_search_request_friend_btn = itemView.findViewById(R.id.item_search_request_friend_btn);


        }


    }
}



