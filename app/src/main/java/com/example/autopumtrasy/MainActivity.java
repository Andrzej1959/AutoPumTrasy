package com.example.autopumtrasy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    public ListView listview;
    public JSONObject linia;
    public JSONArray tablica;
    public final String RESTURL = "http://lukan.sytes.net:1880/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listview = findViewById(R.id.lista);

        new Thread(new Runnable() {
            @Override
            public void run() {
                tablica = SendJSON.getArray(RESTURL + "listatras");
            }
        }).start();

        while (tablica == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        final ArrayList<String> list = new ArrayList<String>();

        //final ArrayList<JSONObject> jasonList = new ArrayList<JSONObject>((Collection<? extends JSONObject>) tablica);


        for (int i = 0; i < tablica.length(); ++i) {
            try {

                linia = tablica.getJSONObject(i);
                list.add(linia.getString("opis"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);


        listview.setAdapter(adapter);


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                JSONObject pozycja;
                String linia = null;
                String id_trasy = null;
                try {
                    pozycja = tablica.getJSONObject(position);
                    linia = pozycja.getString("id_trasy") + " ";
                    linia += pozycja.getString("opis");
                    id_trasy = pozycja.getString("id_trasy");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final String adres = RESTURL + "mapa?id_trasy=" + id_trasy;

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SendJSON.getJSON(adres);
                    }
                }).start();


                Toast.makeText(getApplicationContext(),
                        "Ładowanie trasy nr: " + linia, Toast.LENGTH_LONG)
                                .show();
            }
        });


    }
}
