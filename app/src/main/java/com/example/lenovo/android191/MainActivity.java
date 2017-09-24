package com.example.lenovo.android191;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    TextView textView1, textView2, textView3, textView4, textView5, textView6, textView7;
    private String TAG = MainActivity.class.getSimpleName();
    String location_name;
    String[] weatherStr;
    String coordStr;
    int temp_min;
    int temp_max;
    int humidity;
    long sunrise, sunset;
    ArrayList<HashMap<String, String>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView1 = (TextView) findViewById(R.id.tv_location);
        textView2 = (TextView) findViewById(R.id.tv_cordinate);
        textView3 = (TextView) findViewById(R.id.tv_weather);
        textView4 = (TextView) findViewById(R.id.tv_temperature);
        textView5 = (TextView) findViewById(R.id.tv_humidity);
        textView6 = (TextView) findViewById(R.id.tv_sunrise);
        textView7 = (TextView) findViewById(R.id.tv_sunset);

        contactList = new ArrayList<>();
        new GetWeather().execute();
    }
    private class GetWeather extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "Json Data is downloading", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            String url = "http://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=b1b15e88fa797225412429c1c50c122a1";
            HttpHandler httpHandler = new HttpHandler();
            String jsonStr = httpHandler.makeServiceCall(url);
            weatherStr = new String[2];
            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONObject jsonObject_coord = jsonObject.getJSONObject("coord");

                    location_name = jsonObject.getString("name");

                    String lon = jsonObject_coord.getString("lon");
                    String lat = jsonObject_coord.getString("lat");

                    coordStr = "Lon : " + lon + ", " + "Lat : " + lat;

                    JSONArray weathers = jsonObject.getJSONArray("weather");
                    int temp;
                    String weath_description = "";

                    for (int i = 0; i < weathers.length(); i++) {
                        JSONObject weath = weathers.getJSONObject(i);

                        weath_description = weath.getString("description");
                    }

                    weatherStr[0] = weath_description;
                    JSONObject main = jsonObject.getJSONObject("main");
                    temp = main.getInt("temp");
                    weatherStr[1] = "Avg.Temp : " + Integer.toString(temp) + "F";

                    JSONObject jsonObject_temp = jsonObject.getJSONObject("main");
                    temp_min = jsonObject_temp.getInt("temp_min");
                    temp_max = jsonObject_temp.getInt("temp_max");
                    humidity = jsonObject_temp.getInt("humidity");

                    JSONObject jsonObject_sun = jsonObject.getJSONObject("sys");
                    sunrise = jsonObject_sun.getLong("sunrise");
                    sunset = jsonObject_sun.getLong("sunset");
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            textView1.setText(location_name.toUpperCase());
            textView2.setText(coordStr);
            textView3.setText(weatherStr[0] + "\n" + weatherStr[1]);
            textView4.setText("MIN " + Integer.toString(temp_min) + " F , MAX " + Integer.toString(temp_max) + " F");
            textView5.setText(Integer.toString(humidity));
            textView6.setText(String.format("%d Hr,%d min, %d sec",
                    (TimeUnit.MILLISECONDS.toHours(sunrise) / 24 > 12) ? TimeUnit.MILLISECONDS.toHours(sunrise) / 24 - 12 : TimeUnit.MILLISECONDS.toHours(sunrise) / 24,
                    TimeUnit.MILLISECONDS.toMinutes(sunrise) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(sunrise)),
                    TimeUnit.MILLISECONDS.toSeconds(sunrise) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(sunrise))));
            textView7.setText(String.format("%d Hr,%d min, %d sec",
                    (TimeUnit.MILLISECONDS.toHours(sunset) / 24 > 12) ? TimeUnit.MILLISECONDS.toHours(sunset) / 24 - 12 : TimeUnit.MILLISECONDS.toHours(sunset) / 24,
                    TimeUnit.MILLISECONDS.toMinutes(sunset) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(sunset)),
                    TimeUnit.MILLISECONDS.toSeconds(sunset) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(sunset))));

        }
    }
}
