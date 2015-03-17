package com.example.udatabox.udatabox_mobileapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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


public class Estudiante_Procesos_Detalles extends ActionBarActivity {
    String[] listaprdetalle;
    final Context context = this;
    private String iduser, codigo_usuario, tipocaso, estado, respuestaservidor, error;
    private Boolean opcionBoton;
    private Procesos procesos;
    private TextView tw_estadoProceso;
    private Button b_acceptar;
    private ListView listaProcesosDetalles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estudiante__procesos__detalles);

        Intent i = getIntent();
        procesos = (Procesos) i.getSerializableExtra("procesos");
        iduser = i.getStringExtra("iduser");
        getSupportActionBar().setTitle(procesos.getNumeroExp());

        tw_estadoProceso = (TextView) findViewById(R.id.tw_estadoProceso);
        b_acceptar = (Button) findViewById(R.id.b_proc_aceptar);
        listaProcesosDetalles = (ListView) findViewById(R.id.listaProcesosDetalle);

        new Conexion(this).execute();

       // Lista Evento
        listaProcesosDetalles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ListaOpcion(position);
            }
        });

        //Button aceptar
        b_acceptar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("¿Quiere Aceptar el Proceso?");
                alertDialogBuilder
                        .setCancelable(true)
                        .setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                opcionBoton=true;
                                new ConexionAceptarExpendiente(getApplicationContext()).execute();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                opcionBoton=false;
                                new ConexionAceptarExpendiente(getApplicationContext()).execute();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

    }
    public void ListaOpcion(int position){
        Intent i=null;
        if (position == 0) {
            i = new Intent(getApplicationContext(), Estudiante_Procesos_DatosCliente.class);
        }
        if (position == 1) {
            i = new Intent(getApplicationContext(), Estudiante_Procesos_DatosDesplazados.class);
        }
        if (position == 2) {
            i = new Intent(getApplicationContext(), Estudiante_Procesos_DatosHechos.class);
        }
        if (position == 3) {
            i = new Intent(getApplicationContext(), Estudiante_Procesos_DatosFamiliares.class);
        }
        if (position == 4) {
            if(tipocaso.equals("caso"))
                i = new Intent(getApplicationContext(), Estudiante_Procesos_DatosCasos.class);
            else
                i = new Intent(getApplicationContext(), Estudiante_Procesos_DatosAsesorias.class);
        }
        if (position == 5) {

        }
        i.putExtra("iduser",iduser);
        i.putExtra("radicado_id",procesos.getNumeroExp());
        i.putExtra("codigo_usuario",codigo_usuario);

        startActivity(i);
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
                nameValuePairs.add(new BasicNameValuePair("actividad", "proceso_detalle"));
                nameValuePairs.add(new BasicNameValuePair("radicado_id ", procesos.getNumeroExp()));
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
            TextView textView = null;
            textView = (TextView) findViewById(R.id.tw_proc_numero);
            textView.setText(procesos.getNumeroExp());
            try {
                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    if (json_data.has("fecha_inicio")) {
                        textView = (TextView) findViewById(R.id.tw_proc_fecha);
                        textView.setText(json_data.getString("fecha_inicio"));
                    }
                    if (json_data.has("fecha_fin")) {
                        textView = (TextView) findViewById(R.id.tw_proc_fechaFin);
                        textView.setText(json_data.getString("fecha_fin"));
                    }
                    if (json_data.has("nombre_usuario")) {
                        textView = (TextView) findViewById(R.id.tw_proc_nombre);
                        textView.setText(json_data.getString("nombre_usuario"));
                    }
                    if (json_data.has("nombre_recepcionado")) {
                        textView = (TextView) findViewById(R.id.tw_proc_recepcionadoNombre);
                        textView.setText(json_data.getString("nombre_recepcionado"));
                    }
                    if (json_data.has("codigo_usuario")) {
                        codigo_usuario = json_data.getString("codigo_usuario");
                    }
                    if (json_data.has("tipocaso")) {
                        tipocaso = json_data.getString("tipocaso");
                    }
                    if (json_data.has("estado")) {
                        estado = json_data.getString("estado");
                    }
                }
            } catch (JSONException e) {
                Log.e("log_tag", "Error pasar datos " + e.toString());
            }
            //Boton Aceptar o Estado Label
            if (!estado.equals("pendiente")) {
                b_acceptar.setVisibility(View.INVISIBLE);
                tw_estadoProceso.setText(estado);
            }
            //Lista de Caso o Asesoria
            if (tipocaso.equals("caso")) {
                listaprdetalle = getResources().getStringArray(R.array.listadoProcesosDetalleCasos);
            } else
                listaprdetalle = getResources().getStringArray(R.array.listadoProcesosDetalleAsesoria);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                    R.layout.fila_procesos_detalle, R.id.tw_proceso_detalle, listaprdetalle);
            listaProcesosDetalles.setAdapter(adapter);


        }
    }

    public class ConexionAceptarExpendiente extends AsyncTask<String, Void, String> {
        private Context context;

        HttpPost httppost;
        StringBuffer buffer;
        HttpResponse response;
        HttpClient httpclient;
        List<NameValuePair> nameValuePairs;
        InputStream is = null;
        String link = "http://10.0.2.2/conexion/conexion.php";
        public ConexionAceptarExpendiente(Context context) {
            this.context = context;
        }
        protected void onPreExecute() {
        }
        @Override
        protected String doInBackground(String... arg0) {
            String result = "";
            try {
                nameValuePairs = new ArrayList<NameValuePair>(3);
                nameValuePairs.add(new BasicNameValuePair("actividad", "aceptar_proceso"));
                nameValuePairs.add(new BasicNameValuePair("radicado_id ", procesos.getNumeroExp()));
                nameValuePairs.add(new BasicNameValuePair("respuesta ",opcionBoton.toString()));
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
                    if(json_data.has("resp"))
                        respuestaservidor=json_data.getString("resp");
                    if(json_data.has("error"))
                        error=json_data.getString("error");
                }
            } catch (JSONException e) {
                Log.e("log_tag", "Error pasar datos " + e.toString());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
                if(respuestaservidor.equals("OK")){
                    b_acceptar.setVisibility(View.INVISIBLE);
                    if(opcionBoton)
                        tw_estadoProceso.setText("Aceptado");
                    else
                        tw_estadoProceso.setText("Rechazado");
                }
        }
    }
}
