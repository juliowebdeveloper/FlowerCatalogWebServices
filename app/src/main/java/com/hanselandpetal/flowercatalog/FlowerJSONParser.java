package com.hanselandpetal.flowercatalog;

import com.hanselandpetal.flowercatalog.model.Flower;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Teste2 on 20/01/2017.
 */

public class FlowerJSONParser {

    public static List<Flower> parseFeed(String content) {
        try {
            JSONArray array  = new JSONArray(content); //Só isso ja transforma o conteudo em array de objetos
            List<Flower> flowers = new ArrayList<>();
            for(int i =0; i<array.length(); i++){
                JSONObject object = array.getJSONObject(i);
                Flower f = new Flower();
                f.setProductId(object.getInt("productId"));
                f.setName(object.getString("name"));
                f.setPhoto(object.getString("photo"));
                f.setCategory(object.getString("category"));
                f.setInstructions(object.getString("instructions"));
                f.setPrice(object.getDouble("price"));
                flowers.add(f);
            }
            return flowers;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

}