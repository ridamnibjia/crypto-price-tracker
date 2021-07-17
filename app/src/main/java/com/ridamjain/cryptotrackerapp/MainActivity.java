package com.ridamjain.cryptotrackerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText searchEd;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    private ArrayList<CurrencyRVModel> currencyRVModelArrayList;
    private CurrencyRVAdapter currencyRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchEd = findViewById(R.id.search);
        recyclerView = findViewById(R.id.list);
        progressBar = findViewById(R.id.progressbar);
        currencyRVModelArrayList = new ArrayList<>();
        currencyRVAdapter = new CurrencyRVAdapter(currencyRVModelArrayList,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(currencyRVAdapter);
        getData();

        searchEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterCurrency(s.toString());
            }
        });
    }
    private void filterCurrency(String currency)
    {
        ArrayList<CurrencyRVModel> filteredList = new ArrayList<>();
        for (CurrencyRVModel item: currencyRVModelArrayList){
            if (item.getName().toLowerCase().contains(currency.toLowerCase())){
                filteredList.add(item);
            }
        }
        if (filteredList.isEmpty()){
            Toast.makeText(getApplicationContext(),"No currency",Toast.LENGTH_LONG).show();
        }
        else{
            currencyRVAdapter.filteredList(filteredList);
        }
    }
    public void getData()
    {
        progressBar.setVisibility(View.VISIBLE);
        String url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.INVISIBLE);
                try {
                    JSONArray dataArray = response.getJSONArray("data");
                    for (int i=0; i<dataArray.length(); i++)
                    {
                        JSONObject dataObj = dataArray.getJSONObject(i);
                        String name = dataObj.getString("name");
                        String symbo = dataObj.getString("symbol");
                        JSONObject quote = dataObj.getJSONObject("quote");
                        JSONObject Usd = quote.getJSONObject("USD");
                        double price = Usd.getDouble("price");
                        currencyRVModelArrayList.add(new CurrencyRVModel(name,symbo,price));
                    }
                    currencyRVAdapter.notifyDataSetChanged();
                }
                catch (JSONException e)
                {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers = new HashMap<>();
                headers.put("X-CMC_PRO_API_KEY","your-api-key");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
}