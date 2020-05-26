package com.example.requisicoeshttp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Button botaoRecuperar;
    private TextView textoResultado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        botaoRecuperar = findViewById(R.id.buttonRecuperar);
        textoResultado = findViewById(R.id.textResultado);

        botaoRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                MyTask task = new MyTask();
                String urlApi = "https://blockchain.info/ticker";
                String cep = "93044385";
                //String urlCep = "https://viacep.com.br/ws/01001000/json/"; //-> recuperando cep
                String urlCep = "https://viacep.com.br/ws/" + cep + "/json/"; //-> recuperando cep
                task.execute(urlCep);

            }
        });

    }
    //String que será URL
    class MyTask extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            String stringUrl = strings[0];
            InputStream inputStream = null;
            InputStreamReader inputStreamReader = null;
            StringBuffer buffer = null;

            //setar no AndroidManifest a permissão
            //<uses-permission android:name="android.permission.INTERNET"/>

            try {
                URL url = new URL(stringUrl); //converter para objeto URL para fazer requisição HTTP
                HttpURLConnection conexao = (HttpURLConnection) url.openConnection(); //cria/faz a requisição

                //Recuperar os dados em bytes
                inputStream =  conexao.getInputStream();

                //Le os dados em bytes e decodifica para caracteres
                inputStreamReader = new InputStreamReader(inputStream);

                //leitura dos caracteres do inputStream
                BufferedReader reader = new BufferedReader(inputStreamReader);
                buffer = new StringBuffer();
                String linha = "";

                //Percorre cada linha
                while((linha = reader.readLine()) != null) {
                    buffer.append(linha); //adiciona uma linha após a outra
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);

            String logradouro = null;
            String cep = null;

            try {
                JSONObject jsonObject = new JSONObject(resultado);
                logradouro = jsonObject.getString("logradouro"); //chave logradouro do json
                cep = jsonObject.getString("cep"); //chave cep do json
            } catch (JSONException e) {
                e.printStackTrace();
            }


            textoResultado.setText(resultado);
        }
    }


}
