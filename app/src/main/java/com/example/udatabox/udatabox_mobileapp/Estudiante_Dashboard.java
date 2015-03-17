package com.example.udatabox.udatabox_mobileapp;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.nativecss.NativeCSS;

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


public class Estudiante_Dashboard extends Activity {
     String iduser;
    String procesos_a,procesos_t,procesos_p;

    ArrayList<ListaDashboard_model> models = new ArrayList<ListaDashboard_model>();
    ListaDashboard_adaptador adapterProcesos;
    ListView listViewProcesos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estudiante__dashboard);
        //--- Intent
        Intent intent = getIntent();
        iduser = intent.getStringExtra("iduser");
        //---
        new Conexion(this).execute();
        listViewProcesos = (ListView) findViewById(R.id.ListEstDashProcesos);

        listViewProcesos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {;
                String Position=Integer.toString(position);
                if(Position=="1" || Position=="2"|| Position=="3"){
                    Intent i = new Intent(getApplicationContext(), Estudiante_Procesos_Tab.class);
                    i.putExtra("opc",Position);
                    i.putExtra("iduser",iduser);
                    startActivity(i);
                }

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
        String link = "https://www.udatabox.com/uniservices/servicios/estudiantes.php";
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
                nameValuePairs.add(new BasicNameValuePair("actividad", "dashboard_estudiante"));
                nameValuePairs.add(new BasicNameValuePair("iduser", iduser));
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
            try {
                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    if(json_data.has("procesos_t"))
                        procesos_t=json_data.getString("procesos_t");
                    if(json_data.has("procesos_a"))
                        procesos_a=json_data.getString("procesos_a");
                    if(json_data.has("procesos_p"))
                        procesos_p=json_data.getString("procesos_p");
                }
            } catch (JSONException e) {
                Log.e("log_tag", "Error pasar datos " + e.toString());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            models.add(new ListaDashboard_model("Procesos"));
            models.add(new ListaDashboard_model("Todos",procesos_t));
            models.add(new ListaDashboard_model("Asignados",procesos_a));
            models.add(new ListaDashboard_model("Pendiente",procesos_p));
            models.add(new ListaDashboard_model("Asesorias"));
            models.add(new ListaDashboard_model("Todos","23"));
            models.add(new ListaDashboard_model("Nuevas","21"));
            models.add(new ListaDashboard_model("Sin Responder","1"));
            models.add(new ListaDashboard_model("Diligencias"));
            models.add(new ListaDashboard_model("Todos","22"));
            models.add(new ListaDashboard_model("Nuevas","42"));
            models.add(new ListaDashboard_model("Sin Responder","12"));
            adapterProcesos = new ListaDashboard_adaptador(context, models);
            listViewProcesos.setAdapter(adapterProcesos);
        }
    }


}
