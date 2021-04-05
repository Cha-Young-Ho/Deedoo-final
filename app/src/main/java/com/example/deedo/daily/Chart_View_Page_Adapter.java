package com.example.deedo.daily;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.deedo.DB.DBHelperFirebase;
import com.example.deedo.R;
import com.example.deedo.callback.Get_period_list_Callback;

import java.util.ArrayList;
import java.util.List;

public class Chart_View_Page_Adapter extends PagerAdapter {

    // LayoutInflater 서비스 사용을 위한 Context 참조 저장.
    private Context mContext = null ;
    DBHelperFirebase firebase;
    TextView textView_inquiry_daily_text;
    AnyChartView inquiry_daily_chart_view;
    ImageView right_direction, left_direction;
    TextView left_textView, right_textView;

    String userId;
    // Context를 전달받아 mContext에 저장하는 생성자 추가.
    public Chart_View_Page_Adapter(Context context, String userId) {
        mContext = context ;
        this.firebase = new DBHelperFirebase();
        this.userId = userId;



    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = null ;

            if (mContext != null) {

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(position == 0){
            view = inflater.inflate(R.layout.chart_view_first_page, container, false);
            textView_inquiry_daily_text = (TextView) view.findViewById(R.id.textView_inquiry_daily_first_text);
            ImageView chart_view_first_imageview = view.findViewById(R.id.chart_view_imageview);
            chart_view_first_imageview.setImageResource(R.drawable.chart_view_first_page);
            right_direction = view.findViewById(R.id.first_right_direction_btn);




        }else {
                // LayoutInflater를 통해 "/res/layout/page.xml"을 뷰로 생성.
                view = inflater.inflate(R.layout.chart_view_page, container, false);
                int period = 1;
                textView_inquiry_daily_text = (TextView) view.findViewById(R.id.textView_inquiry_daily_text);
                inquiry_daily_chart_view = view.findViewById(R.id.inquiry_daily_chart_view);
                if (position == 1) {
                    period = 1;
                } else if (position == 6) {
                    period = 365;
                } else if (position == 5) {
                    period = 180;
                } else if (position == 4) {
                    period = 90;
                } else if (position == 3) {
                    period = 30;
                } else if (position == 2) {
                    period = 7;
                }
                firebase.Get_Period_Daily(new Get_period_list_Callback() {
                    @Override
                    public void get_period_list_Callback(ArrayList<daily_data> period_list) {
                        Log.v("period_list 길이 = ", "" + period_list.size());
                        ArrayList<String> chart_d = new ArrayList<>();
                        ArrayList<Integer> earning = new ArrayList<>();
                        for (int i = 0; i < period_list.size(); i++) {
                            if (Integer.parseInt(period_list.get(i).getSecond()) != 0) {
                                chart_d.add(period_list.get(i).getArea_tag());
                                earning.add(Integer.parseInt(period_list.get(i).getSecond()));
                            }
                        }
                        Setup_Pie_Chart(chart_d, earning);

                    }
                }, userId, period);
                textView_inquiry_daily_text.setText(period + "일 동안의 일과");
            right_direction = view.findViewById(R.id.imageView_right_direction);
            left_direction = view.findViewById(R.id.imageView_left_direction);
            left_textView = view.findViewById(R.id.textView_left_direction);
            right_textView = view.findViewById(R.id.textView_right_direction);

            if(position == 1) {
                left_textView.setVisibility(View.GONE);
                right_textView.setText("일주일");

                left_direction.setVisibility(View.GONE);
            }else if(position == 2){
                left_textView.setText("1일");
                right_textView.setText("1개월");


            }else if(position == 3){
                left_textView.setText("일주일");
                right_textView.setText("3개월");

            }else if(position == 4){
                left_textView.setText("1개월");
                right_textView.setText("6개월");

            }else if(position == 5){
                left_textView.setText("3개월");
                right_textView.setText("1년");


            }else if(position == 6){
                left_textView.setText("6개월");
                right_textView.setVisibility(View.GONE);
                right_direction.setVisibility(View.GONE);

            }


            }
        }

        // 뷰페이저에 추가.
        container.addView(view) ;

        return view ;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // 뷰페이저에서 삭제.
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        // 전체 페이지 수는 10개로 고정.
        return 7;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View)object);
    }

    public void Setup_Pie_Chart(List<String> chart_d, List<Integer> earning) {
        Pie pie = AnyChart.pie();
        List<DataEntry> dataEntries = new ArrayList<>();


        for (int i = 0; i < chart_d.size(); i++) {
            dataEntries.add(new ValueDataEntry(chart_d.get(i), earning.get(i)));
        }

        pie.data(dataEntries);


        inquiry_daily_chart_view.setChart(pie);



    }
}
