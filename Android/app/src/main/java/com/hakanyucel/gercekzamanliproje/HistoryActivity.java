package com.hakanyucel.gercekzamanliproje;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);


        MyDbHelper myDbHelper = new MyDbHelper(this);
        Cursor result = myDbHelper.getAllData_Sensor();
        LinearLayout sv = (LinearLayout) findViewById(R.id.historyScrollView);
        TextView oxygen, temperature, humidity, gas, room, newLine;

        while(result.moveToNext())
        {
            oxygen = new TextView(this);
            oxygen.setText("Oksijen : %"+result.getString(1) + " - " + result.getString(2) + " dk");
            oxygen.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            oxygen.setTextColor(Color.parseColor("#B3DCFF"));

            temperature = new TextView(this);
            temperature.setText("Isı : " + result.getString(3));
            temperature.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            temperature.setTextColor(Color.parseColor("#B3DCFF"));

            humidity = new TextView(this);
            humidity.setText("Nem : "+ result.getString(4));
            humidity.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            humidity.setTextColor(Color.parseColor("#B3DCFF"));

            gas = new TextView(this);
            gas.setText("Gaz : " + result.getString(5));
            gas.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            gas.setTextColor(Color.parseColor("#B3DCFF"));

            room = new TextView(this);
            room.setText("Oda Boyutu : " + result.getString(6));
            room.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            room.setTextColor(Color.parseColor("#B3DCFF"));

            newLine = new TextView(this);
            newLine.setText("");

            sv.addView(oxygen);
            sv.addView(temperature);
            sv.addView(humidity);
            sv.addView(gas);
            sv.addView(room);
            sv.addView(newLine);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.calculator:
                Intent intent = new Intent(HistoryActivity.this, DashboardActivity.class);
                startActivity(intent);
                return true;
            case R.id.history:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
