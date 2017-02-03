package com.hanselandpetal.flowercatalog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hanselandpetal.flowercatalog.model.Flower;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by MAGNA2 on 27/01/2017.
 */

public class FlowerAdapter extends RecyclerView.Adapter<FlowerAdapter.ViewHolder> {

    private  Context context;
    private List<Flower> flowerList;

    //Trabalhando com Cache do Android
    private LruCache<Integer, Bitmap> imageCache;

    public FlowerAdapter (Context c, List<Flower> flowerList){
        this.context = c;
        this.flowerList = flowerList;
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory()/ 1024); //Calculo vindo da documentação Android para obter a max memory disponivel
        final int cacheSize = maxMemory / 8; //Usar 1/8 do que tiver disponivel - Picasso por default usa 1/7, o que é bem agradavel para a maioria dos dispositivos.
        imageCache = new LruCache<>(cacheSize);


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_flower, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
            holder.flowerName.setText(flowerList.get(position).getName());
           // holder.flowerImage.setImageBitmap(flowerList.get(position).getBitmap());
        //Usando picasso
       // Picasso.with(context).load(MainActivity.PHOTOS_BASE_URL + flowerList.get(position).getPhoto()).into(holder.flowerImage);
        Bitmap bitmap = imageCache.get(flowerList.get(position).getProductId()); //Olhando no cache

        if(bitmap!= null){
            holder.flowerImage.setImageBitmap(flowerList.get(position).getBitmap());
        }else{
            FlowerAndView container = new FlowerAndView();
            container.flower=flowerList.get(position);
            container.holder= holder;
            ImageLoader loader = new ImageLoader();
            loader.execute(container);
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





    //Criando uma nova AsyncTask apenas para carregar as imagens (TOTALMENTE DESNECESSARIO SE USAR O PICASSO - Porém a idéia se mantem para carregar outros tipos de dados)

    //Classe para ser passada para o async task
    class FlowerAndView{
        public Flower flower;
        public ViewHolder holder;
        public Bitmap bitmap;
    }

    //Mesma ideia de AsyncTask
    private class ImageLoader extends AsyncTask<FlowerAndView, Void, FlowerAndView>{


        @Override
        protected FlowerAndView doInBackground(FlowerAndView... params) {

            FlowerAndView container = params[0];
            Flower flower = container.flower;
            //Carregando a imagem
            try {
                String imageUrl = MainActivity.PHOTOS_BASE_URL + flower.getPhoto();
                InputStream in = (InputStream) new URL(imageUrl).getContent(); //retorna todo o content
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                flower.setBitmap(bitmap);
                in.close();
                container.bitmap = bitmap;
                return container;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return  null;
        }

        @Override
        protected void onPostExecute(FlowerAndView result) {
            //Setando o atributo do holder e colocando o bitmap para usos futuros
            result.holder.flowerImage.setImageBitmap(result.bitmap);
            //result.flower.setBitmap(result.bitmap);//ao inves de salvar no flower object, iremos salvar no LruCache
            imageCache.put(result.flower.getProductId(), result.bitmap); //Setando no cache
        }
    }



}
