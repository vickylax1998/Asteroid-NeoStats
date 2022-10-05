package com.ranium.asteroid_neostats;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.ranium.adapter.NeoFeedAdapter;
import com.ranium.pojo.NeoFeed;
import com.ranium.util.AppController;
import com.ranium.util.Keys;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
EditText edtStartDate,edtEndDate;
Button  btnSubmit;
DatePickerDialog picker;
ProgressDialog dialog;
ArrayList<String> arrayListDates=new ArrayList<>();
ArrayList<String> arrayListAsteriodCounts=new ArrayList<>();
ArrayList<NeoFeed> arrayListNeoFeed=new ArrayList<>();
LineChart chart;
TextView tvFastestAsteriod,tvClosestAsteriod;
RecyclerView recyclerView;
NeoFeedAdapter neoFeedAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppController.initialize(getApplicationContext());
        edtStartDate = findViewById(R.id.editTextStartDate);
        edtEndDate = findViewById(R.id.editTextEndDate);
        btnSubmit = findViewById(R.id.buttonSubmit);
        tvFastestAsteriod = findViewById(R.id.textViewFastestAsteriod);
        tvClosestAsteriod = findViewById(R.id.textViewClosestAsteriod);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        neoFeedAdapter = new NeoFeedAdapter(arrayListNeoFeed,getApplicationContext());
        recyclerView.setAdapter(neoFeedAdapter);
        chart = findViewById(R.id.chart1);
        initializechart();
        dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage("Please wait Loading...");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        edtStartDate.setInputType(InputType.TYPE_NULL);
        edtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                edtStartDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
        edtEndDate.setInputType(InputType.TYPE_NULL);
        edtEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                edtEndDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              String startdate = edtStartDate.getText().toString().trim();
              String enddate = edtEndDate.getText().toString().trim();
              if (startdate.equals("")){
                  Toast.makeText(getApplicationContext(), "Please select a start date", Toast.LENGTH_SHORT).show();
              }else if (enddate.equals("")){
                  Toast.makeText(getApplicationContext(), "Please select a end date", Toast.LENGTH_SHORT).show();
              }else {
                  submit(startdate,enddate);
              }
            }
        });
    }

    private void initializechart() {
        chart.setViewPortOffsets(0, 0, 0, 0);
        chart.setBackgroundColor(Color.WHITE);

        // no description text
        chart.getDescription().setEnabled(false);

        // enable touch gestures
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(true);

        chart.setDrawGridBackground(false);
        chart.setMaxHighlightDistance(300);

        XAxis x = chart.getXAxis();
        x.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);

        x.setEnabled(true);
        x.setAxisMinValue(0);
        x.setTextColor(Color.BLACK);
        x.setLabelCount(4, true);
        x.setValueFormatter(new ClaimsXAxisValueFormatter(arrayListDates));

        YAxis y = chart.getAxisLeft();
        // y.setTypeface(tfLight);
        y.setLabelCount(6, false);
        y.setTextColor(Color.BLACK);
        y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        y.setDrawGridLines(false);
        y.setAxisLineColor(Color.BLACK);
        y.setValueFormatter(new ClaimsYAxisValueFormatter());

        chart.getAxisRight().setEnabled(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisLeft().setDrawGridLines(false);


    }

    private void submit(String startdate, String enddate) {
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.GET, Keys.URL+"start_date="+startdate+"&end_date="+enddate+"&api_key=DEMO_KEY", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.i("vic", "response=>" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("near_earth_objects")){
                        if (!jsonObject.isNull("near_earth_objects")){
                            JSONObject jsonObject1 = jsonObject.getJSONObject("near_earth_objects");
                            for(int i = 0; i<jsonObject1.names().length(); i++){
                               // Log.v(TAG, "key = " + jobject.names().getString(i) + " value = " + jobject.get(jobject.names().getString(i)));
                                arrayListDates.add(jsonObject1.names().getString(i));
                                JSONArray jsonArray1 = jsonObject1.getJSONArray(jsonObject1.names().getString(i));
                                arrayListAsteriodCounts.add(String.valueOf(jsonArray1.length()));
                                for(int j = 0; j<jsonArray1.length(); j++){
                                    JSONObject jsonObject2 = jsonArray1.getJSONObject(j);
                                    JSONArray jsonArray =jsonObject2.getJSONArray("close_approach_data");
                                    JSONObject jsonObject5 = jsonObject2.getJSONObject("estimated_diameter").getJSONObject("kilometers");
                                    JSONObject jsonObject3=jsonArray.getJSONObject(0).getJSONObject("relative_velocity");
                                    JSONObject jsonObject4=jsonArray.getJSONObject(0).getJSONObject("miss_distance");
                                    String speed = jsonObject3.getString("kilometers_per_hour");
                                    String distance = jsonObject4.getString("kilometers");
                                    String min_size = jsonObject5.getString("estimated_diameter_min");
                                    String max_size = jsonObject5.getString("estimated_diameter_max");
                                    arrayListNeoFeed.add(new NeoFeed(jsonObject2.getString("id"),Float.parseFloat(speed),Float.parseFloat(distance),Float.parseFloat(min_size),Float.parseFloat(max_size)));

                                    Log.i("vic", distance);
                                }

                            }
                            NeoFeed fastestasteriod = Collections.max(arrayListNeoFeed, new compareSpeed());
                            NeoFeed closestasteriod = Collections.max(arrayListNeoFeed, new compareDistance());
                            tvFastestAsteriod.setText("Fastest Asteroid id : "+fastestasteriod.getAsteriod_id()+"\n"+"Fastest Asteroid speed : "+fastestasteriod.getSpeed()+" km/hr");
                            tvClosestAsteriod.setText("Closest Asteroid id : "+closestasteriod.getAsteriod_id()+"\n"+"Closest Asteroid Distance : "+closestasteriod.getDistance()+" km");

                            Log.i("vic", String.valueOf(closestasteriod.getDistance()));
                            setData();
                            chart.getLegend().setEnabled(false);
                            chart.animateXY(500, 500);
                            neoFeedAdapter.notifyDataSetChanged();

                        }else{
                            Toast.makeText(MainActivity.this, "No data", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(MainActivity.this, "No data", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().add(request);
    }
    public class compareSpeed implements Comparator<NeoFeed> {
        public int compare(NeoFeed a, NeoFeed b) {
            if (a.getSpeed() > b.getSpeed())
                return -1; // highest value first
            if (a.getSpeed() == b.getSpeed())
                return 0;
            return 1;
        }
    }
    public class compareDistance implements Comparator<NeoFeed> {
        public int compare(NeoFeed a, NeoFeed b) {
            if (a.getDistance() > b.getDistance())
                return -1; // highest value first
            if (a.getDistance() == b.getDistance())
                return 0;
            return 1;
        }
    }
    public class ClaimsYAxisValueFormatter extends ValueFormatter {

        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            return String.valueOf(value);
        }
    }
    public class ClaimsXAxisValueFormatter extends ValueFormatter {

        List<String> datesList;

        public ClaimsXAxisValueFormatter(List<String> arrayOfDates) {
            this.datesList = arrayOfDates;
        }


        @Override
        public String getAxisLabel(float value, AxisBase axis) {
/*
Depends on the position number on the X axis, we need to display the label, Here, this is the logic to convert the float value to integer so that I can get the value from array based on that integer and can convert it to the required value here, month and date as value. This is required for my data to show properly, you can customize according to your needs.
*/
            Integer position = Math.round(value);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");

            if (value > 1 && value < 2) {
                position = 0;
            } else if (value > 2 && value < 3) {
                position = 1;
            } else if (value > 3 && value < 4) {
                position = 2;
            } else if (value > 4 && value <= 5) {
                position = 3;
            }
            if (position < datesList.size())
                return sdf.format(new Date((getDateInMilliSeconds(datesList.get(position), "yyyy-MM-dd"))));
            return "";
        }
    }
    public static long getDateInMilliSeconds(String givenDateString, String format) {
        String DATE_TIME_FORMAT = format;
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.US);
        long timeInMilliseconds = 1;
        try {
            Date mDate = sdf.parse(givenDateString);
            timeInMilliseconds = mDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeInMilliseconds;
    }
    private void setData() {

        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < arrayListAsteriodCounts.size(); i++) {

            values.add(new Entry(i,Float.parseFloat(arrayListAsteriodCounts.get(i))));
        }

        LineDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "DataSet 1");

           // set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
           // set1.setCubicIntensity(0.2f);
            set1.setDrawFilled(true);
            set1.setDrawCircles(true);
            set1.setLineWidth(1.8f);
            set1.setCircleRadius(4f);
            set1.setCircleColor(Color.rgb(251,202,74));
            set1.setHighLightColor(Color.rgb(244, 117, 117));
            set1.setColor(Color.rgb(153,204,255));
            set1.setFillColor(Color.rgb(153,204,255));
            set1.setFillAlpha(100);
            set1.setDrawHorizontalHighlightIndicator(false);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return chart.getAxisLeft().getAxisMinimum();
                }
            });

            // create a data object with the data sets
            LineData data = new LineData(set1);
            // data.setValueTypeface(Typeface.defaultFromStyle(R.font.mplusrounded_light));
            data.setValueTextSize(9f);
            data.setDrawValues(false);

            // set data
            chart.setData(data);
            chart.invalidate();


        }
    }
}