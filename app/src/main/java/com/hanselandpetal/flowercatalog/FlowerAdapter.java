package com.hanselandpetal.flowercatalog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.hanselandpetal.flowercatalog.model.Flower;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by MAGNA2 on 27/01/2017.
 */

public class FlowerAdapter extends RecyclerView.Adapter<FlowerAdapter.ViewHolder> {

    //Retrofit!!a
    private  Context context;
    private List<Flower> flowerList;
    private RequestQueue requestQueue;

    private OkHttpClient client = new OkHttpClient();

    //Trabalhando com Cache do Android
    private LruCache<Integer, Bitmap> imageCache;

    public FlowerAdapter (Context c, List<Flower> flowerList){
        this.context = c;
        this.flowerList = flowerList;
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory()/ 1024); //Calculo vindo da documentação Android para obter a max memory disponivel
        final int cacheSize = maxMemory / 8; //Usar 1/8 do que tiver disponivel - Picasso por default usa 1/7, o que é bem agradavel para a maioria dos dispositivos.
        imageCache = new LruCache<>(cacheSize);
        requestQueue = Volley.newRequestQueue(context);


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_flower, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.flowerName.setText(flowerList.get(position).getName());
        Bitmap bitmap = imageCache.get(flowerList.get(position).getProductId()); //Olhando no cache
        if(bitmap!= null){
            holder.flowerImage.setImageBitmap(flowerList.get(position).getBitmap());
        }else{
            String imageUrl = MainActivity.PHOTOS_BASE_URL + (flowerList.get(position).getPhoto());
            ImageRequest imageRequest = new ImageRequest(imageUrl,
                    new com.android.volley.Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            holder.flowerImage.setImageBitmap(response); //Setando no adapter o response vindo do volley
                           // imageCache.put(flowerList.get(position).getProductId(), response); //Setando o imageCache
                        }//Max Width, Max Height
                    }, 80, 80, ImageView.ScaleType.FIT_CENTER, //Scale Type
                    Bitmap.Config.ARGB_8888 // Configuração de Memoria
                    , new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Error retrieving the images " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            requestQueue.add(imageRequest);

        }

    }

    @Override
    public int getItemCount() {
        return flowerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView flowerImage;
        TextView flowerName;

        public ViewHolder(View itemView) {
            super(itemView);
            flowerImage = (ImageView) itemView.findViewById(R.id.imageView1);
            flowerName = (TextView) itemView.findViewById(R.id.textView1);
        }


    }




}
