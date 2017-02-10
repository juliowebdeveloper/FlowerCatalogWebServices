package com.hanselandpetal.flowercatalog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hanselandpetal.flowercatalog.model.Flower;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    public static final String PHOTOS_BASE_URL = "http://services.hanselandpetal.com/photos/";

    TextView textView;
    ProgressBar progressBar;
    List<Flower> flowerList;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textView  = (TextView) findViewById(R.id.textView);
        textView.setMovementMethod(new ScrollingMovementMethod());//Setando scrolling num unico elemento
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_dotask) {
            if(isOnline()) {
                requestData("http://services.hanselandpetal.com/feeds/flowers.json"); //Com Autenticação
            }else{
                Toast.makeText(this, "Your Device is not online", Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    //Utilizando Volley - Trabalha na arquitetura de callbacks - Não precisa de AsyncTask
    protected void requestData(String url) {
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) { //Se chamar a JsonRequest o listener receberá um jsonArray
                            flowerList = FlowerJSONParser.parseFeed(response);
                            updateDisplay();
                    }
                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) { //Callback de error
                Toast.makeText(MainActivity.this, "Error Returning Flowers" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this); //Fila de requests do Volley, adicionando o request criado
        queue.add(request);

    }

    protected  void updateDisplay(){
        if(flowerList!=null ){
           FlowerAdapter adapter = new FlowerAdapter(this, flowerList);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        }

    }

    protected boolean isOnline(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isConnectedOrConnecting()){ //Checando se há conectividade com a internet
            return true;
        }else{
            return false;
        }
    }




}
