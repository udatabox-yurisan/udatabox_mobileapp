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
import android.widget.TableLayout;
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


public class Estudiante_Procesos_DatosCliente extends ActionBarActivity {
    String iduser, radicado_id,codigo_usuario, conyuge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estudiante__procesos__datos_cliente);
        Intent i = getIntent();
        iduser = i.getStringExtra("iduser");
        radicado_id = i.getStringExtra("radicado_id");
        codigo_usuario = i.getStringExtra("codigo_usuario");

        new Conexion(this).execute();
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
                nameValuePairs.add(new BasicNameValuePair("actividad", "datos_cliente"));
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
                    if (json_data.has("nombre")) {
                        textView = (TextView) findViewById(R.id.tw_P_ClienteNombre);
                        textView.setText(json_data.getString("nombre"));
                    }
                    if (json_data.has("cc")) {
                        textView = (TextView) findViewById(R.id.tw_P_ClienteCC);
                        textView.setText(json_data.getString("cc"));
                    }
                    if (json_data.has("edad")) {
                        textView = (TextView) findViewById(R.id.tw_P_ClienteEdad);
                        textView.setText(json_data.getString("edad"));
                    }
                    if (json_data.has("direccion")) {
                        textView = (TextView) findViewById(R.id.tw_P_ClienteDireccion);
                        textView.setText(json_data.getString("direccion"));
                    }
                    if (json_data.has("barrio")) {
                        textView = (TextView) findViewById(R.id.tw_P_ClienteBarrio);
                        textView.setText(json_data.getString("barrio"));
                    }
                    if (json_data.has("nivelescolar")) {
                        textView = (TextView) findViewById(R.id.tw_P_ClienteEscolaridad);
                        textView.setText(json_data.getString("nivelescolar"));
                    }
                    if (json_data.has("telefono")) {
                        textView = (TextView) findViewById(R.id.tw_P_ClienteTelefono);
                        textView.setText(json_data.getString("telefono"));
                    }
                    if (json_data.has("sisben")) {
                        textView = (TextView) findViewById(R.id.tw_P_ClienteSisben);
                        textView.setText(json_data.getString("sisben"));
                    }
                    if (json_data.has("ocupacion")) {
                        textView = (TextView) findViewById(R.id.tw_P_ClienteOcupacion);
                        textView.setText(json_data.getString("ocupacion"));
                    }
                    if (json_data.has("estadocivil")) {
                        textView = (TextView) findViewById(R.id.tw_P_ClienteEstadoCivil);
                        textView.setText(json_data.getString("estadocivil"));
                    }
                    if (json_data.has("area")) {
                        textView = (TextView) findViewById(R.id.tw_P_ClienteArea);
                        textView.setText(json_data.getString("area"));
                    }
                    if (json_data.has("conyugeNombre")) {
                        textView = (TextView) findViewById(R.id.tw_P_ConyugeNombre);
                        textView.setText(json_data.getString("conyugeNombre"));
                    }
                    if (json_data.has("conyugeHijos")) {
                        textView = (TextView) findViewById(R.id.tw_P_ClienteConyugeHijos);
                        textView.setText(json_data.getString("conyugeHijos"));
                    }
                    if (json_data.has("conyugeOcupacion")) {
                        textView = (TextView) findViewById(R.id.tw_P_ClienteConyugeOcupacion);
                        textView.setText(json_data.getString("conyugeOcupacion"));
                    }
                    if (json_data.has("conyuge")) {
                     conyuge=json_data.getString("conyuge");
                    }
                }
                if(conyuge.equals("no")){
                    TableLayout tableconyuge=(TableLayout) findViewById(R.id.tablaConyuge);
                    tableconyuge.setVisibility(View.GONE);
                    TextView twconyuge=(TextView)findViewById(R.id.tw_titulo_conyuge);
                    twconyuge.setVisibility(View.GONE);
                }

            } catch (JSONException e) {
                Log.e("log_tag", "Error pasar datos " + e.toString());
            }

        }
    }
}
