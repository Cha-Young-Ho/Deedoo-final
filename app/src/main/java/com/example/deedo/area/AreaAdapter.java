package com.example.deedo.area;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deedo.DB.DBHelperFirebase;
import com.example.deedo.R;
import com.example.deedo.callback.Delete_Area_Callback;

import java.util.ArrayList;

public class AreaAdapter extends RecyclerView.Adapter<AreaAdapter.AreaViewHolder> {

    private ArrayList<Area_Data> Area_List;
    private Context context;
    DBHelperFirebase firebase = new DBHelperFirebase();
    String userId;

    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    private OnItemClickListener onItemClickListener;


    public AreaAdapter(OnItemClickListener onItemClickListener, String _id) {
        this.onItemClickListener = onItemClickListener;
        this.userId = _id;
    }

    public AreaAdapter(ArrayList<Area_Data> area_List, Context context, String _id) {
        Area_List = area_List;
        this.context = context;
        this.userId = _id;
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

        holder.AreaName.setText(Area_List.get(position).getTextView_name() + " (" + Area_List.get(position).getArea_tag() + ")");
        holder.AreaLatitude.setText(Area_List.get(position).getTextView_latitude());
        holder.AreaLongitude.setText(Area_List.get(position).getTextView_longitude());

        holder.item_delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();


                Log.v("선택된 번호 =", "" + pos);
                Log.v("선택된 곳의 정보", "" + Area_List.get(pos).getTextView_name() + " - " + Area_List.get(pos).getTextView_latitude() + " - " + Area_List.get(pos).getTextView_longitude());
                String AreaName = Area_List.get(pos).getTextView_name();
                String AreaLatitude = Area_List.get(pos).getTextView_latitude();
                String AreaLongitude = Area_List.get(pos).getTextView_longitude();

                firebase.Delete_Area(new Delete_Area_Callback() {
                    @Override
                    public void delete_Area_Callback(String area_name, String latitude, String longitude) {
                        Area_List.remove(pos);
                        notifyItemRemoved(pos);
                        notifyItemRangeChanged(pos, Area_List.size());

                    }
                },AreaName, AreaLatitude, AreaLongitude, userId);


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



