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
    List<MyTask> tasks;
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
        tasks = new ArrayList<>();
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
                //requestData("http://services.hanselandpetal.com/feeds/flowers.xml");
               // requestData("http://services.hanselandpetal.com/feeds/flowers.json");
                requestData("http://services.hanselandpetal.com/secure/flowers.json"); //Com Autenticação
            }else{
                Toast.makeText(this, "Your Device is not online", Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    protected void requestData(String url) {
        MyTask task = new MyTask();
        task.execute(url);
       //task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "Param1", "Param2", "Param3");//Para processamento em paralelo


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

    private class MyTask extends AsyncTask<String, String, List<Flower>>{

        @Override
        protected void onPreExecute() {
           // updateDisplay("Executing Task");
            if(tasks.size()==0) {
                progressBar.setVisibility(View.VISIBLE);//Só deixa visivel se nao tiver tasks na lista
            }
            tasks.add(this);//Referenciando a propria class
        }

        @Override
        protected void onPostExecute(List<Flower> result) { //Result que vem do doInBackground

            //Fazendo o parsing do xml
            //flowerList = FlowerXMLParser.parseFeed(result);
            updateDisplay();
            tasks.remove(this);
            if(tasks.size()==0) {
                progressBar.setVisibility(View.INVISIBLE);//Só deixa visivel se nao tiver tasks na lista
            }

        }

        @Override
        protected void onProgressUpdate(String... values) {
           // updateDisplay(values[0]);
        }

        @Override
        protected List<Flower> doInBackground(String... strings) {
          //  String result = HttpManager.getData(strings[0]); //Passando o primeiro valor que é a propria url, caso tivessem mais, deve ser apontado
           //Passando com o login para autorização
            String result = HttpManager.getData(strings[0], "feeduser", "feedpassword");
            //Fazendo o parser aqui no doInBackground
            flowerList = FlowerJSONParser.parseFeed(result);

            //chamando as imagens das flores - Transferido para o adapter
      /*      for (Flower f:flowerList) {
                String imageUrl = PHOTOS_BASE_URL + f.getPhoto(); //apontando pra imagem
                try {
                    InputStream in = (InputStream) new URL(imageUrl).getContent(); //retorna todo o content
                    Bitmap bitmap = BitmapFactory.decodeStream(in);
                    f.setBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }*/

            /* for (String s:
                 strings) {
                publishProgress("Working with: "+s);//O data type do parametro desse metodo é determinado pelo parametro do meio da class

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }*/
            return flowerList;
        }
    }


}
