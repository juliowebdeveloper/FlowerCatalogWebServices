package com.hanselandpetal.flowercatalog;

import android.util.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Teste2 on 20/01/2017.
 */

public class HttpManager {

    public static String getData(String uri){
        BufferedReader reader = null;

        try {
            URL url = new URL(uri);
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();

            StringBuilder stringBuilder = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(response.body().byteStream()));
            String line;
            while((line = reader.readLine()) !=null){
                 stringBuilder.append(line +"\n");
            }

            return stringBuilder.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally {
            if(reader!=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    //Overload do metodo para tratar de requisições com passwords
    public static String getData(String uri, String username, String password){
        BufferedReader reader = null;
        HttpURLConnection connection = null;

        byte[] loginBytes = (username + ":" + password).getBytes(); //ByteArray representando as crendenciais
        StringBuilder loginBuilder = new StringBuilder().append("Basic ")
                .append(Base64.encodeToString(loginBytes, Base64.DEFAULT));//Passando o loginBytes em Base64 para o String builder



        try {
            URL url = new URL(uri);
            connection = (HttpURLConnection) url.openConnection();
            //Header Value passando a autorização
            connection.addRequestProperty("Authorization",loginBuilder.toString());

            StringBuilder stringBuilder = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while((line = reader.readLine()) !=null){
                stringBuilder.append(line +"\n");
            }

            return stringBuilder.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;


        } catch (IOException e) {
            e.printStackTrace();
            return null;

        }catch (Exception e){
            e.printStackTrace();
            try {
                int resultCode = connection.getResponseCode();
                //Pegando o código de resposta caso 401 - Unaunthorized
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        }finally {
            if(reader!=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
