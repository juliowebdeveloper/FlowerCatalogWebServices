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
import com.hanselandpetal.flowercatalog.model.FlowersAPI;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {


    public static final String PHOTOS_BASE_URL = "http://services.hanselandpetal.com/photos/";
    public static final String ENDPOINT ="http://services.hanselandpetal.com/"; //Essa é a Base URL, o resto das urls serão definidas nas interfaces

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
                requestData("http://services.hanselandpetal.com/feeds/flowers.json");
            }else{
                Toast.makeText(this, "Your Device is not online", Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    protected void requestData(String url) {
        //No Retrofit 1.x  - RestAdapter
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ENDPOINT).addConverterFactory(GsonConverterFactory.create()).build(); //Usando o GSON converter para converter json em POJOS
        FlowersAPI api = retrofit.create(FlowersAPI.class);//Dizendo que essa class que definirá o webservices e o que iremos chamar
        Call<List<Flower>> call = api.getFlowers();
            //Para executar assim é necessario estar em uma asyncTask, Retrofit 2  não trata threads sozinho exceto pelo enqueue
            // flowerList = call.execute().body();
           // updateDisplay();
           call.enqueue(new Callback<List<Flower>>() {
                @Override
                public void onResponse(Call<List<Flower>> call, retrofit2.Response<List<Flower>> response) {
                    flowerList = response.body();
                    updateDisplay();
                }
                @Override
                public void onFailure(Call<List<Flower>> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Error Retrieving Data with Retrofit", Toast.LENGTH_SHORT).show();
                }
            });
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
