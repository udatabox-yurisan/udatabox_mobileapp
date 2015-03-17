package com.example.udatabox.udatabox_mobileapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
import java.util.List;


public class Estudiante_Procesos_DatosDesplazados extends ActionBarActivity {
    String[] listaprdetalle;
    String iduser, radicado_id,codigo_usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estudiante__procesos__datos_desplazados);

        Intent i = getIntent();
        iduser = i.getStringExtra("iduser");
        radicado_id = i.getStringExtra("radicado_id");
        codigo_usuario = i.getStringExtra("codigo_usuario");

        new Conexion(this).execute();

        ListView listaProcesosDetalles = ( ListView ) findViewById(R.id.listaProcesosDetalleDesplazado);
        listaprdetalle=getResources().getStringArray(R.array.listadoProcesosDatosDesplazado);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.fila_procesos_detalle, R.id.tw_proceso_detalle, listaprdetalle);

        listaProcesosDetalles.setAdapter(adapter);
        listaProcesosDetalles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent i=null;
                if(position==0){
                    i = new Intent(getApplicationContext(), Estudiante_Procesos_DatosDesplazadosMotivo.class);
                 }
                if(position==1){
                    i = new Intent(getApplicationContext(), Estudiante_Procesos_DatosDesplazadosBienes.class);
                }
                i.putExtra("iduser",iduser);
                i.putExtra("radicado_id",radicado_id);
                i.putExtra("codigo_usuario",codigo_usuario);
                startActivity(i);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            String result = "";
            try {
                nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("actividad", "datos_desplazado"));
                nameValuePairs.add(new BasicNameValuePair("codigo_usuario ",codigo_usuario));
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
                result = sb.toString();
                Log.i("log_tag", "Realizo conversion de resultados " + result);
            } catch (Exception e) {
                Log.e("log_tag", "Error convertir resultados " + e.toString());
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    TextView textView;
                    if (json_data.has("lugar")) {
                        textView = (TextView) findViewById(R.id.tw_P_DezplazamintoLugar);
                        textView.setText(json_data.getString("lugar"));
                    }
                    if (json_data.has("fecha")) {
                        textView = (TextView) findViewById(R.id.tw_P_DezplazamintoFecha);
                        textView.setText(json_data.getString("fecha"));
                    }
                    if (json_data.has("rupd")) {
                        textView = (TextView) findViewById(R.id.tw_P_DezplazamintoRupd);
                        textView.setText(json_data.getString("rupd"));
                    }
                }
            } catch (JSONException e) {
                Log.e("log_tag", "Error pasar datos " + e.toString());
            }

        }
    }
}
