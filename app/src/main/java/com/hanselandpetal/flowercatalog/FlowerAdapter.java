package com.hanselandpetal.flowercatalog;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hanselandpetal.flowercatalog.model.Flower;

import java.util.List;

/**
 * Created by MAGNA2 on 27/01/2017.
 */

public class FlowerAdapter extends RecyclerView.Adapter<FlowerAdapter.ViewHolder> {

    private  Context context;
    private List<Flower> flowerList;

    public FlowerAdapter (Context c, List<Flower> flowerList){
        this.context = c;
        this.flowerList = flowerList;
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
