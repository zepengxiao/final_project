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

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button = findViewById(R.id.button3);
        mQueue = Volley.newRequestQueue(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                startButton();
            }
        });



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
            int messageValue = 1;
            for (int i = 0; i < inputString.toCharArray().length; i++) {
                messageValue = messageValue * Integer.parseInt(inputString) +
                        (int) inputString.toCharArray()[i] * (month + 10 * day);
            }
            int fortuneValue1 = fortuneValue % 100;
            final int messageValue1 = messageValue;
            if (fortuneValue1 < 0) {
                fortuneValue1 += 100;
            }
            String fortuneValueString1 = String.valueOf(fortuneValue1);
            textView2.setText(fortuneValueString1.toCharArray(), 0, fortuneValueString1.length());

            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    Request.Method.GET,
                    "http://fortunecookieapi.herokuapp.com/v1/fortunes?limit=&skip=&page=",
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(final JSONArray response) {
                            try {
                                int i = messageValue1 % response.length();
                                if (i < 0) {
                                    i += response.length();
                                }
                                JSONObject messageObject = response.getJSONObject(i);
                                String message = messageObject.getString("message");
                                textView7.setText(message.toCharArray(), 0, message.length());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            mQueue.add(jsonArrayRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
