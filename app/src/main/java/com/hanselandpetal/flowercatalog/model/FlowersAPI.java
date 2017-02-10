package com.hanselandpetal.flowercatalog.model;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;

/**
 * Created by MAGNA2 on 10/02/2017.
 */

public interface FlowersAPI {
    //@GET("/feeds/flowers.json") //Definir a Url, mas não a base
   //Assim seria em Retrofit 1.0 public void getFeed(Callback <List<Flower>> response); //Pssar a instancia da Callback class, retrofit sabe pegar um formato Json e converte-lo para POJOS

    @GET("/feeds/flowers.json")
    Call<List<Flower>> getFlowers();
}
