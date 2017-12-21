package com.hakanyucel.gercekzamanliproje;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class DashboardActivity extends AppCompatActivity {

    private Button btnCalculateOxygen, btnAddOxygen;
    private EditText txtRoomWidth, txtRoomHeight, txtRoomLenght;
    private TextView txtCalculationOxygenResult;

    String url = "https://api.thingspeak.com/channels/388469/feeds.json?api_key=PMWAIGAUPGLI6WD7&results=2";

    private Feed feed;
    private List<Feed> feeds;
    private OkHttpClient client;
    private Request request;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        feeds = new ArrayList<>();
        client = new OkHttpClient();

        txtRoomWidth = (EditText) findViewById(R.id.txtRoomWidth);
        txtRoomHeight = (EditText) findViewById(R.id.txtRoomHeight);
        txtRoomLenght = (EditText) findViewById(R.id.txtRoomLenght);

        txtCalculationOxygenResult = (TextView) findViewById(R.id.txtCalculationOxygenResult);

        btnAddOxygen = (Button) findViewById(R.id.btnAddOxygen);
        btnAddOxygen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random rnd = new Random();
                MyDbHelper myDbHelper = new MyDbHelper(DashboardActivity.this);
                String gelenOxygen = String.valueOf(rnd.nextInt());
                String gelenTemperature = String.valueOf(rnd.nextInt());
                String gelenHumidity = String.valueOf(rnd.nextInt());
                String gelenGas = String.valueOf(rnd.nextInt());
                int sure = rnd.nextInt();
                boolean result = myDbHelper.insertData_Sensor(gelenOxygen,
                        String.valueOf(sure),
                        gelenTemperature,
                        gelenHumidity,
                        gelenGas,
                        txtRoomWidth.getText().toString() + "x" + txtRoomHeight.getText().toString() +
                                "x" + txtRoomLenght.getText().toString());
                if(result)
                    Toast.makeText(DashboardActivity.this, "Eklendi", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(DashboardActivity.this, "Eklenmedi", Toast.LENGTH_SHORT).show();
            }
        });

        btnCalculateOxygen = (Button) findViewById(R.id.btnCalculateOxygen);
        btnCalculateOxygen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(txtRoomWidth.getText().equals("") || txtRoomHeight.getText().equals("") ||
                            txtRoomLenght.getText().equals(""))
                    {
                        Toast.makeText(getApplicationContext(), "Lütfen Oda Boyutlarını Boş Bırakmayınız", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        request = new Request.Builder()
                                .url(url)
                                .build();

                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                call.cancel();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {

                                final String myResponse = response.body().string();

                                DashboardActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try{

                                            JSONObject reader = new JSONObject(myResponse);
                                            JSONArray sysa  = reader.getJSONArray("feeds");
                                            for(int i = 0 ; i < sysa.length() ; i++)
                                            {
                                                JSONObject sys = sysa.getJSONObject(i);
                                                //feed = new Feed(sys.getString("entry_id"),sys.getString("field3"),sys.getString("created_at"));
                                                //feeds.add(feed);

                                                MyDbHelper myDbHelper = new MyDbHelper(DashboardActivity.this);
                                                double oxygen = Double.parseDouble(sys.getString("field1"));
                                                double en = Double.parseDouble(txtRoomWidth.getText().toString());
                                                double boy = Double.parseDouble(txtRoomHeight.getText().toString());
                                                double yukseklik = Double.parseDouble(txtRoomLenght.getText().toString());
                                                double v = en * boy * yukseklik;
                                                double o2 = (v * (oxygen / 100)) * 1000;
                                                double min = o2 / 1.39;
                                                int sure = Integer.parseInt(String.valueOf(min));

                                                String gelenOxygen = sys.getString("field1");
                                                String gelenTemperature = sys.getString("field2");
                                                String gelenHumidity = sys.getString("field3");
                                                String gelenGas = sys.getString("field4");

                                                boolean result = myDbHelper.insertData_Sensor(gelenOxygen,
                                                        String.valueOf(sure),
                                                        gelenTemperature,
                                                        gelenHumidity,
                                                        gelenGas,
                                                        txtRoomWidth.getText().toString() + "x" + txtRoomHeight.getText().toString() +
                                                                "x" + txtRoomLenght.getText().toString());
                                                if(result)
                                                {
                                                    txtCalculationOxygenResult.setText("Odada bulunan oksijen miktarı %" + gelenOxygen + "\n" +
                                                            "Bir insan odada yaklaşık " + String.valueOf(sure) + " dakika yaşıyabilir" + "\n" +
                                                            "Sıcaklık değeri : " + gelenTemperature + "\n" +
                                                            "Nem Değeri : " + gelenHumidity + "\n" +
                                                            "Gaz Değeri : " + gelenGas);
                                                }
                                            }



                                        }catch (Exception ex){
                                            Log.e("Parse exception" , ex.getMessage());
                                        }

                                    }
                                });

                            }
                        });
                    }
                }
                catch (Exception ex){
                    Log.e("Hata", ex.getMessage());
                }

            }
        });
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
                return true;
            case R.id.history:
                Intent intent = new Intent(DashboardActivity.this, HistoryActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
