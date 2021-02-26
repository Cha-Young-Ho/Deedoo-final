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

public class AreaAdapter extends RecyclerView.Adapter<AreaAdapter.AreaViewHolder> {

    private ArrayList<Area_Data> Area_List;
    private Context context;
    DBHelper db;

    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    private OnItemClickListener onItemClickListener;


    public AreaAdapter(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public AreaAdapter(ArrayList<Area_Data> area_List, Context context) {
        Area_List = area_List;
        this.context = context;
    }

    @NonNull
    @Override
    public AreaAdapter.AreaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 카드 레이아웃 찾고(view) -> 지정하고(holder = view) -> 지정해준 holder 넘겨줌
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lotate_create, parent, false);

        AreaViewHolder holder = new AreaViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AreaAdapter.AreaViewHolder holder, int position) {

        holder.AreaName.setText(Area_List.get(position).getTextView_name());
        holder.AreaLatitude.setText(Area_List.get(position).getTextView_latitude());
        holder.AreaLongitude.setText(Area_List.get(position).getTextView_longitude());

        holder.item_delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                db = new DBHelper(context);

                Log.v("선택된 번호 =", "" + pos);
                Log.v("선택된 곳의 정보", "" + Area_List.get(pos).getTextView_name() + " - " + Area_List.get(pos).getTextView_latitude() + " - " + Area_List.get(pos).getTextView_longitude());
                String AreaName = Area_List.get(pos).getTextView_name();
                String AreaLatitude = Area_List.get(pos).getTextView_latitude();
                String AreaLongitude = Area_List.get(pos).getTextView_longitude();

                db.Delete_Area(AreaName, AreaLatitude, AreaLongitude);
                Area_List.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, Area_List.size());


            }
        });


    }

    @Override
    public int getItemCount() {
        //리스트가 null이 아니면 size, null이면 0
        return (Area_List != null ? Area_List.size() : 0);
    }

    public class AreaViewHolder extends RecyclerView.ViewHolder {

        TextView AreaName, AreaLatitude, AreaLongitude;
        Button item_delete_btn;

        public AreaViewHolder(@NonNull View itemView) {
            super(itemView);
            this.AreaName = itemView.findViewById(R.id.item_textView_name);
            this.AreaLatitude = itemView.findViewById(R.id.item_textView_latitude);
            this.AreaLongitude = itemView.findViewById(R.id.item_textView_longitude);
            this.item_delete_btn = itemView.findViewById(R.id.item_delete_btn);


        }
    }
}



