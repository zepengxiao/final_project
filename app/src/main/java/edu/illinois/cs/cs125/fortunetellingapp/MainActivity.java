package edu.illinois.cs.cs125.fortunetellingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.nio.charset.Charset;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    //private EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        final Button button = findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                startButton();
            }
        });



    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    void startButton() {
        try {
            EditText input = findViewById(R.id.editText);
            String inputString = input.getText().toString();
            String inputError = "Invalid input!";
            String blank = "";
            final TextView textView2 = findViewById(R.id.textView2);
            final TextView textView7 = findViewById(R.id.textView7);
            if (inputString.length() != 4 ) {
                textView7.setText(inputError.toCharArray(), 0, inputError.length());
                textView2.setText(blank.toCharArray(), 0, blank.length());
                return;
            } else {
                if (inputString.substring(0, 2).equals("01") ||
                        inputString.substring(0, 2).equals("03") ||
                        inputString.substring(0, 2).equals("05") ||
                        inputString.substring(0, 2).equals("07") ||
                        inputString.substring(0, 2).equals("08") ||
                        inputString.substring(0, 2).equals("10") ||
                        inputString.substring(0, 2).equals("12")) {
                    if (Integer.parseInt(inputString.substring(2, 4)) > 31) {
                        textView7.setText(inputError.toCharArray(), 0, inputError.length());
                        textView2.setText(blank.toCharArray(), 0, blank.length());
                        return;
                    }
                } else if (inputString.substring(0, 2).equals("04") ||
                        inputString.substring(0, 2).equals("06") ||
                        inputString.substring(0, 2).equals("09") ||
                        inputString.substring(0, 2).equals("11")) {
                    if (Integer.parseInt(inputString.substring(2, 4)) >30) {
                        textView7.setText(inputError.toCharArray(), 0, inputError.length());
                        textView2.setText(blank.toCharArray(), 0, blank.length());
                        return;
                    }
                } else if (inputString.substring(0, 2).equals("02")) {
                    if (Integer.parseInt(inputString.substring(2, 4)) > 29) {
                        textView7.setText(inputError.toCharArray(), 0, inputError.length());
                        textView2.setText(blank.toCharArray(), 0, blank.length());
                        return;
                    }
                } else {
                    textView7.setText(inputError.toCharArray(), 0, inputError.length());
                    return;
                }
            }
            int fortuneValue = 5381;
            Calendar c = Calendar.getInstance();
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DATE);
            for (int i = 0; i < inputString.toCharArray().length; i++) {
                fortuneValue = fortuneValue * 33 +
                        (int) inputString.toCharArray()[i] * (10 * month +day);
            }
            int fortuneValue1 = fortuneValue % 100;
            final int fortuneValue2 = fortuneValue;
            if (fortuneValue1 < 0) {
                fortuneValue1 += 100;
            }
            String fortuneValueString1 = String.valueOf(fortuneValue1);
            textView2.setText(fortuneValueString1.toCharArray(), 0, fortuneValueString1.length());
            /**
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    Request.Method.GET,
                    "http://fortunecookieapi.herokuapp.com/v1/fortunes?limit=&skip=&page=",
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(final JSONArray response) {
                            try {
                                System.out.println(response + "weird");
                                int i = fortuneValue2 % response.length();
                                if (i < 0) {
                                    i += response.length();
                                }
                                JSONObject messageObject = response.getJSONObject(i);
                                String message = messageObject.getString("message");
                                System.out.println(message + "weird");
                                textView7.setText(message.toCharArray(), 0, message.length());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            /**
                             JSONParser parer = new JSONParser();
                             JsonArray result = parser.parse(json).getAsJsonArray();
                             JsonObject message = result.getJSONObject(0).getAsJsonObject("message");

                            //JSONArray jsonarray = response.getJSONArray("message");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }
            );
            */
            JSONObject json = readJsonFromUrl("https://graph.facebook.com/19292868552");
            System.out.println(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
