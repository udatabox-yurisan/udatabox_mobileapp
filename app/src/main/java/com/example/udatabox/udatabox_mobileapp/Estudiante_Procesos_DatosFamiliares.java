package com.example.udatabox.udatabox_mobileapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Estudiante_Procesos_DatosFamiliares extends ActionBarActivity {
    private List<ListaFamiliares_model> array_familiares=new ArrayList<ListaFamiliares_model>();
    String iduser, radicado_id,codigo_usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estudiante__procesos__datos_familiares);

        Intent i = getIntent();
        iduser = i.getStringExtra("iduser");
        radicado_id = i.getStringExtra("radicado_id");
        codigo_usuario = i.getStringExtra("codigo_usuario");

        new Conexion(this).execute();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_estudiante__procesos__datos_familiares, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public class Conexion extends AsyncTask<String, Void, String> {
        private Context context;
        HttpPost httppost;
        StringBuffer buffer;
        HttpResponse response;
        HttpClient httpclient;
        List<NameValuePair> nameValuePairs;
        InputStream is = null;
        String link = "http://10.0.2.2/conexion/conexion.php";

        public Conexion(Context context) {
            this.context = context;
        }

        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... arg0) {
            String resultPendiente = "";
            try {
                nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("actividad", "datos_familiares"));
                nameValuePairs.add(new BasicNameValuePair("codigo_usuario", codigo_usuario));
                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(link);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                Log.i("log_tag", "Realizo conexion ");
            } catch (Exception e) {
                Log.e("log_tag", "Error en conexion http " + e.toString());
            }
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                resultPendiente = sb.toString();
                Log.i("log_tag", "Realizo conversion de resultados " + resultPendiente);
            } catch (Exception e) {
                Log.e("log_tag", "Error convertir resultados " + e.toString());
            }

            try {
                JSONArray jArray = new JSONArray(resultPendiente);
                for (int i = 1; i < jArray.length(); i++) {
                    JSONObject json_dataPendiente = jArray.getJSONObject(i);
                    array_familiares.add(new ListaFamiliares_model(json_dataPendiente.getString("parentesco"), json_dataPendiente.getString("nombre"), json_dataPendiente.getString("edad")));
                }
            } catch (JSONException e) {
                Log.e("log_tag", "Error pasar datos " + e.toString());
            }
            return resultPendiente;
        }
        @Override
        protected void onPostExecute(String result) {
            List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
            for(int i=0;i<array_familiares.size();i++){
                HashMap<String, String> hm = new HashMap<String,String>();
                hm.put("relacion",array_familiares.get(i).getRelacion());
                hm.put("nombre",array_familiares.get(i).getNombre());
                hm.put("edad",array_familiares.get(i).getEdad());
                aList.add(hm);
            }
            String[] from = {"relacion","nombre","edad" };
            int[] to = {R.id.tw_familiar_relacion,R.id.tw_familiar_nombre,R.id.tw_familiar_edad};
            SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.fila_listado_familiares, from, to);
            ListView listView = ( ListView ) findViewById(R.id.listaFamiliares);
            listView.setAdapter(adapter);
        }
    }

}