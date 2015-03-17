package com.example.udatabox.udatabox_mobileapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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

public class MainActivity extends ActionBarActivity {
    private EditText user, pass;
    private TextView error;
    String contenidoCss = null;
    String s_login, s_pass;
    String r_res,r_iduser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = (EditText) findViewById(R.id.et_usuario);
        pass = (EditText) findViewById(R.id.et_contrasena);
        error = (TextView) findViewById(R.id.tw_errorlogin);
        Button b_login = (Button) findViewById(R.id.b_login);

        AssetManager assetmanager = getAssets();
        InputStream input;
        try {
            input = assetmanager.open("mobile_udatabox.css");
            int size = input.available();
            byte[] buffer = new byte[size];
            input.read(buffer);
            input.close();
            contenidoCss = new String(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NativeCSS.styleWithCSS(contenidoCss);

        b_login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(),
                        Estudiante_Dashboard.class);
                in.putExtra("iduser","lacm");
                startActivity(in);
                error.setText("");
                s_login = user.getText().toString();
                s_pass = pass.getText().toString();
                if (s_login.length() > 0 && s_pass.length() > 0) {
                    new Conexion(getApplicationContext()).execute();

                } else {
                    error.setText(getString(R.string.empty_field));
                }

            }
        });
        TextView recuperarContrasena = (TextView) findViewById(R.id.tw_RecuperarContraseña);
        recuperarContrasena.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),
                        Main_Recuperar_Contrasena.class);
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
       String link = "https://www.udatabox.com/uniservices/servicios/login.php";
        //String link = "http://10.0.2.2/conexion/conexion.php";

        public Conexion(Context context) {
            this.context = context;
        }

        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... arg0) {
            String result = "";
            try {
                nameValuePairs = new ArrayList<NameValuePair>(3);
                nameValuePairs.add(new BasicNameValuePair("actividad", "login"));
                nameValuePairs.add(new BasicNameValuePair("usuario", s_login));
                nameValuePairs.add(new BasicNameValuePair("clave", s_pass));
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
            r_res = "-1";

            //parse json data
            try {
                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    int rr_res = json_data.getInt("res");
                    switch (rr_res){
                        case 0:
                            r_iduser=json_data.getString("iduser");
                            user.setText("");
                            pass.setText("");

                            Intent in = new Intent(getApplicationContext(),
                                    Estudiante_Dashboard.class);
                            in.putExtra("iduser",r_iduser);
                            startActivity(in);
                           break;
                        case 1:
                            error.setText("Usuario o Contraseña invalido");
                            break;
                        case 2:
                            error.setText("Usuario desactivado");
                            break;
                    }


                }
            } catch (JSONException e) {
                Log.e("log_tag", "Error pasar datos " + e.toString());
            }

        }

    }

}