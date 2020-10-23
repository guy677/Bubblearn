package com.example.androidproject01;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;

import android.os.Build;
import android.os.Bundle;
import android.util.ArraySet;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;


import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;

import androidx.lifecycle.ViewModelProvider;


import com.example.androidproject01.models.AuthViewModel;
import com.example.androidproject01.models.DB_model;

import com.example.androidproject01.models.UserStats;
import com.example.androidproject01.models.questionToDB;
import com.example.androidproject01.models.retrieveDataRealTime;
import com.example.androidproject01.utils.OnSwipeTouchListener;
import com.example.androidproject01.utils.baseView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import com.github.mikephil.charting.formatter.ValueFormatter;

import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class StatsActivity extends baseView {

    private AuthViewModel  authViewModel;
    private UserStats userStats;
    private String pieChartView;
    private Map<String,Object> userQuizzes;
    private ImageView imageView ;
    private int pagefliper = 0;

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        initAuthViewModel();
        setSwipeListers();
        imageView = findViewById(R.id.imageView4);
        this.HideUI();
        int[] imageResources = new int[]{
                R.drawable.main,
                R.drawable.logout,
        };
        HashMap<Integer,String[]> menuStrings = new HashMap<>();
        String[] MenuHeadString = new String[]{
                getText(R.string.backToMenu).toString(),
                getText(R.string.LogOut).toString()
        };
        String[] MenuSubString = new String[]{
                getText(R.string.backToMenuSUB).toString(),
                getText(R.string.LogOutSub).toString()
        };
        List<Intent> menuIntent = new LinkedList<>();
        menuIntent.add(new Intent(this, SelectCategoryActivity.class));
        menuIntent.add(new Intent(this, LogIn.class));
        String userID = authViewModel.getUser().getUid();
        DB_model db_model = new ViewModelProvider(this).get(DB_model.class);
        db_model.setRealTimeRepository("Members");
        Switch s = findViewById(R.id.switch1);
        s.setTranslationZ(90);
        db_model.getUserQuizzes(userID, new retrieveDataRealTime() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(Object data) {
                userQuizzes  = (Map<String, Object>) data;
                LineChart lineChart = findViewById(R.id.lineChart);
                lineChart.setNoDataText(getResources().getString(R.string.noData));
                lineChart.setNoDataTextColor(Color.BLUE);
                if(userQuizzes != null) {
                    lineChart.setData(getLineData());
                    lineChart.animateY(2000);
                    lineChart.getDescription().setEnabled(false);
                    lineChart.invalidate(); // refresh
                }
            }
            @Override
            public void onFailed(DatabaseError databaseError) { }
        });
        db_model.getUsersStats(userID, new retrieveDataRealTime() {
            @Override
            public void onStart() { }
            @Override
            public void onSuccess(Object data) {

                userStats = (UserStats) data;
                PieChart pieChart = findViewById(R.id.pieChart);
                if(userStats ==null){
                    pieChart.setNoDataText(getResources().getString(R.string.noData));
                    pieChart.setNoDataTextColor(Color.BLUE);
                }
                else {
                    pieChartView = "Correct";
                    pieChart.setData(getPieData());
                    pieChart.getDescription().setEnabled(false);
                    pieChart.animate();
                    pieChart.invalidate(); // refresh
                }

            }

            @Override
            public void onFailed(DatabaseError databaseError) {

            }
        });
        this.SetMenu(MenuHeadString, menuStrings, MenuSubString, imageResources, menuIntent);
        s.setTranslationZ(90);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                PieChart pieChart = findViewById(R.id.pieChart);
                if(userStats ==null){
                    pieChart.setNoDataText(getResources().getString(R.string.noData));
                    pieChart.setNoDataTextColor(Color.BLUE);
                }
                else {
                    if (b) {
                        pieChartView = "Wrong";
                        s.setText(R.string.btn_value_wrong);
                    } else {
                        pieChartView = "Correct";
                        s.setText(R.string.btn_value_correct);
                    }
                    pieChart.getDescription().setEnabled(false);
                    pieChart.setData(getPieData());
                    pieChart.animate();
                    pieChart.invalidate(); // refresh
                }
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setSwipeListers() {
        CardView cardViewLine = findViewById(R.id.cardLine);
        CardView cardViewPie = findViewById(R.id.cardPie);
        findViewById(R.id.tableMain).setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeLeft() {
                cardViewPie.setVisibility(View.INVISIBLE);
                cardViewLine.setVisibility(View.VISIBLE);
                imageView.setImageResource(R.drawable.pagefliper2);
            }
            public void onSwipeRight() {
                cardViewPie.setVisibility(View.VISIBLE);
                cardViewLine.setVisibility(View.INVISIBLE);
                imageView.setImageResource(R.drawable.pagefliper1);
            }
        });
    }



    private LineData getLineData () {
        LineChart lineChart = findViewById(R.id.lineChart);
        XAxis xAxis = lineChart.getXAxis();
        YAxis yAxis = lineChart.getAxisRight();
        yAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "";
            }
        });
        lineChart.getAxisLeft().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return (int)value + " %";
            }
        });
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        List<Entry> userSuccesses  = new ArrayList<Entry>();
        ArrayList ar = new ArrayList<>();

        Set<String> set =  userQuizzes.keySet();
        Iterator<String> i = set.iterator();
        int x = 1;
        while(i.hasNext())
        {
            ar.add(x);
            String res = i.next();
            HashMap<String,Object> value=  (HashMap<String,Object>) userQuizzes.get(res);
            float correct = Math.toIntExact((Long) value.get("Correct"));
            List<questionToDB> wrongList = (List<questionToDB>) value.get("Wrong Answers");
            float wrong = 0;
            if(wrongList!= null) {
                wrong = wrongList.size();
            }
            userSuccesses.add(new Entry( x ,(correct/(correct+wrong)*100)));
            x++;

        }
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return "";
            }
        });
        if(!userSuccesses.isEmpty())
        {
            userSuccesses = userSuccesses.subList(Math.max(0,userSuccesses.size()-30),userSuccesses.size());
        }
        LineDataSet setComp1 = new LineDataSet(userSuccesses, "Success rate");
        setComp1.setDrawFilled(true);
        setComp1.setFillColor(R.drawable.gradient);
        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
        setComp1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        setComp1.setCubicIntensity(0.2f);
        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(setComp1);
        LineData data = new LineData(dataSets);
        data.setValueTextSize(12f);
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if(value < 10) return "";
                return (int)value + " %";
            }
        });
        return data;

    }

    private PieData getPieData() {
        PieChart p = findViewById(R.id.pieChart);
        p.setEntryLabelColor(R.color.colorPrimaryDark);
        List<PieEntry> entries = new ArrayList<>();
        Set<String> set = new ArraySet<>();
        try {
            set =  userStats.getData().keySet();
        }catch (Exception e){}
        Iterator<String> i = set.iterator();
        while(i.hasNext())
        {
            String res = i.next();
            entries.add(new PieEntry(userStats.getData().get(res).get(pieChartView), res));

        }
        if(!entries.isEmpty())
        {
            entries = entries.subList(Math.max(0,entries.size()-10),entries.size());
        }
        PieDataSet sets = new PieDataSet(entries, "Correct Answers By Categories");
        sets.setColors(ColorTemplate.JOYFUL_COLORS);
        sets.setSliceSpace(4f);
        sets.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        //sets.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        PieData data = new PieData(sets);
        data.setValueTextSize(15f);
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return ((int)value)+"";
            }
        });
        return data;
    }
    private void initAuthViewModel() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

    public void changeChart(View view) {
        pagefliper++;
        CardView cardViewLine = findViewById(R.id.cardLine);
        CardView cardViewPie = findViewById(R.id.cardPie);
        ImageView imageView = (ImageView) view;
        if(pagefliper%2==1){
            imageView.setImageResource(R.drawable.pagefliper2);
            cardViewPie.setVisibility(View.INVISIBLE);
            cardViewLine.setVisibility(View.VISIBLE);
        }
        else{
            pagefliper=0;
            imageView.setImageResource(R.drawable.pagefliper1);
            cardViewLine.setVisibility(View.INVISIBLE);
            cardViewPie.setVisibility(View.VISIBLE);
        }

    }
}