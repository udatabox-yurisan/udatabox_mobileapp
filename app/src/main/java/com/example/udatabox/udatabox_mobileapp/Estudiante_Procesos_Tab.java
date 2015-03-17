package com.example.udatabox.udatabox_mobileapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
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
import java.util.HashMap;
import java.util.List;

import static android.widget.TabHost.*;


public class Estudiante_Procesos_Tab extends ActionBarActivity {
    private List<Procesos> array_procesosTodos = new ArrayList<Procesos>();
    private List<Procesos> array_procesosAsignados = new ArrayList<Procesos>();
    private List<Procesos> array_procesosPendiente = new ArrayList<Procesos>();
    private SimpleAdapter adapter_procesosTodo;
    private SimpleAdapter adapter_procesosAsignados;
    private SimpleAdapter adapter_procesosPendiente;
    private List<HashMap<String, String>> aList_procesosTodo = new ArrayList<HashMap<String, String>>();
    private List<HashMap<String, String>> aList_procesosAsignados = new ArrayList<HashMap<String, String>>();
    private List<HashMap<String, String>> aList_procesosPendiente = new ArrayList<HashMap<String, String>>();
    private String[] from;
    private int[] to;
    private TabHost tabHost;
    private String iduser;
    private  ListView listaTodos, listaAsignados,listaPendiente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estudiante__procesos__tab);

        tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup();
        Intent intent = getIntent();
        String opc = intent.getStringExtra("opc");
        iduser = intent.getStringExtra("iduser");

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("Todos");
        tabSpec.setContent(R.id.TabTodos);
        tabSpec.setIndicator("Todos");
        tabHost.addTab(tabSpec);
        tabSpec = tabHost.newTabSpec("Asignados");
        tabSpec.setContent(R.id.TabAsignados);
        tabSpec.setIndicator("Asignados");
        tabHost.addTab(tabSpec);
        tabSpec = tabHost.newTabSpec("Pendiente");
        tabSpec.setContent(R.id.TabPendiente);
        tabSpec.setIndicator("Pendiente");
        tabHost.addTab(tabSpec);
        tabHost.setCurrentTab(Integer.parseInt(opc) - 1);

        //LISTA
        from = new String[]{"numero", "fecha", "nombre"};
        to = new int[]{R.id.tw_proc_numero, R.id.tw_proc_fecha, R.id.tw_proc_nombre};

        //TODOS
        listaTodos = (ListView) findViewById(R.id.ListTodos);
        new ConexionProcesosTodos(this).execute();
        listaTodos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String s = (String) ((TextView) view.findViewById(R.id.tw_proc_nombre)).getText();
                Intent i = new Intent(getApplicationContext(), Estudiante_Procesos_Detalles.class);
                i.putExtra("procesos", array_procesosTodos.get(position));
                i.putExtra("iduser",iduser);
                startActivity(i);
            }
        });

        //Asignados
        listaAsignados = (ListView) findViewById(R.id.ListAsignados);
          new ConexionProcesosAsignados(this).execute();
        listaAsignados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String s = (String) ((TextView) view.findViewById(R.id.tw_proc_nombre)).getText();
                Intent i = new Intent(getApplicationContext(), Estudiante_Procesos_Detalles.class);
                i.putExtra("procesos", array_procesosAsignados.get(position));
                i.putExtra("iduser",iduser);
                startActivity(i);
            }
        });

        //Pendiente
        listaPendiente = (ListView) findViewById(R.id.ListPendiente);
        new ConexionProcesosPendiente(this).execute();
        listaPendiente.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String s = (String) ((TextView) view.findViewById(R.id.tw_proc_nombre)).getText();
                Intent i = new Intent(getApplicationContext(), Estudiante_Procesos_Detalles.class);
                i.putExtra("procesos", array_procesosPendiente.get(position));
                i.putExtra("iduser",iduser);
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

    public class ConexionProcesosTodos extends AsyncTask<String, Void, String> {
        private Context context;
        HttpPost httppost;
        StringBuffer buffer;
        HttpResponse response;
        HttpClient httpclient;
        List<NameValuePair> nameValuePairs;
        InputStream is = null;
        String link = "https://www.udatabox.com/uniservices/servicios/estudiantes.php";

        public ConexionProcesosTodos(Context context) {
            this.context = context;
        }

        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... arg0) {
            String result = "";
            try {
                nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("actividad", "dashboard_procesos_t"));
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
                    array_procesosTodos.add(new Procesos(json_data.getString("nombre_usuario"), json_data.getString("fecha"), json_data.getString("radicado")));
                }
            } catch (JSONException e) {
                Log.e("log_tag", "Error pasar datos " + e.toString());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            for (int i = 0; i < array_procesosTodos.size(); i++) {
                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("numero", array_procesosTodos.get(i).getNumeroExp());
                hm.put("fecha", array_procesosTodos.get(i).getFecha());
                hm.put("nombre", array_procesosTodos.get(i).getNombre());
                aList_procesosTodo.add(hm);
            }
            adapter_procesosTodo = new SimpleAdapter(getBaseContext(), aList_procesosTodo, R.layout.fila_listado_procesos, from, to);
            listaTodos.setAdapter(adapter_procesosTodo);
        }
    }

    public class ConexionProcesosAsignados extends AsyncTask<String, Void, String> {
        private Context context;
        HttpPost httppost;
        StringBuffer buffer;
        HttpResponse response;
        HttpClient httpclient;
        List<NameValuePair> nameValuePairs;
        InputStream is = null;
        String link = "http://10.0.2.2/conexion/conexion.php";

        public ConexionProcesosAsignados(Context context) {
            this.context = context;
        }

        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... arg0) {
            String resultAsignado = "";
            try {
                nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("actividad", "proceso_detalle_todos"));
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
                resultAsignado = sb.toString();
                Log.i("log_tag", "Realizo conversion de resultados " + resultAsignado);
            } catch (Exception e) {
                Log.e("log_tag", "Error convertir resultados " + e.toString());
            }

            try {
                JSONArray jArray = new JSONArray(resultAsignado);
                for (int i = 1; i < jArray.length(); i++) {
                    JSONObject json_dataAsignado = jArray.getJSONObject(i);
                    array_procesosAsignados.add(new Procesos(json_dataAsignado.getString("nombre_usuario"), json_dataAsignado.getString("fecha"), json_dataAsignado.getString("radicado")));
                }
            } catch (JSONException e) {
                Log.e("log_tag", "Error pasar datos " + e.toString());
            }
            return resultAsignado;
        }

        @Override
        protected void onPostExecute(String result) {
            for (int i = 0; i < array_procesosAsignados.size(); i++) {
                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("numero", array_procesosAsignados.get(i).getNumeroExp());
                hm.put("fecha", array_procesosAsignados.get(i).getFecha());
                hm.put("nombre", array_procesosAsignados.get(i).getNombre());
                aList_procesosAsignados.add(hm);
            }
            adapter_procesosAsignados = new SimpleAdapter(getBaseContext(), aList_procesosAsignados, R.layout.fila_listado_procesos, from, to);
            listaAsignados.setAdapter(adapter_procesosAsignados);
        }
    }

    public class ConexionProcesosPendiente extends AsyncTask<String, Void, String> {
        private Context context;
        HttpPost httppost;
        StringBuffer buffer;
        HttpResponse response;
        HttpClient httpclient;
        List<NameValuePair> nameValuePairs;
        InputStream is = null;
        String link = "http://10.0.2.2/conexion/conexion.php";

        public ConexionProcesosPendiente(Context context) {
            this.context = context;
        }

        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... arg0) {
            String resultPendiente = "";
            try {
                nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("actividad", "proceso_detalle_todos"));
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
                resultPendiente = sb.toString();
                Log.i("log_tag", "Realizo conversion de resultados " + resultPendiente);
            } catch (Exception e) {
                Log.e("log_tag", "Error convertir resultados " + e.toString());
            }

            try {
                JSONArray jArray = new JSONArray(resultPendiente);
                for (int i = 1; i < jArray.length(); i++) {
                    JSONObject json_dataPendiente = jArray.getJSONObject(i);
                    array_procesosPendiente.add(new Procesos(json_dataPendiente.getString("nombre_usuario"), json_dataPendiente.getString("fecha"), json_dataPendiente.getString("radicado")));
                }
            } catch (JSONException e) {
                Log.e("log_tag", "Error pasar datos " + e.toString());
            }
            return resultPendiente;
        }
        @Override
        protected void onPostExecute(String result) {
            for (int i = 0; i < array_procesosPendiente.size(); i++) {
                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("numero", array_procesosPendiente.get(i).getNumeroExp());
                hm.put("fecha", array_procesosPendiente.get(i).getFecha());
                hm.put("nombre", array_procesosPendiente.get(i).getNombre());
                aList_procesosPendiente.add(hm);
            }
            adapter_procesosPendiente = new SimpleAdapter(getBaseContext(), aList_procesosPendiente, R.layout.fila_listado_procesos, from, to);
            listaPendiente.setAdapter(adapter_procesosPendiente);
        }
    }


}
