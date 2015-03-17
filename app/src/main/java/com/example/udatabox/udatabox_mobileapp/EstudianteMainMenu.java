package com.example.udatabox.udatabox_mobileapp;

import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.nativecss.NativeCSS;

import java.io.InputStream;


public class EstudianteMainMenu extends ActionBarActivity {
    String contenidoCss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estudiante_main_menu);

        AssetManager assetmanager=getAssets();
        InputStream input;
        try {
            input=assetmanager.open("styles.css");
            int size=input.available();
            byte[] buffer=new byte[size];
            input.read(buffer);
            input.close();
            contenidoCss= new String(buffer);
        }catch (Exception e){
            e.printStackTrace();
        }
        NativeCSS.setDebugLogging(true);
        NativeCSS.styleWithCSS(contenidoCss);

        Button b_todos=  (Button)findViewById(R.id.b_procesos_todos);
        b_todos.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),
                        Estudiante_Procesos_Tab.class);
                i.putExtra("opc","0");
                startActivity(i);
            }
        });
        Button b_nuevos=  (Button)findViewById(R.id.b_procesos_nuevos);
        b_nuevos.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),
                        Estudiante_Procesos_Tab.class);
                i.putExtra("opc","1");
                startActivity(i);
            }
        });
        Button b_sr=  (Button)findViewById(R.id.b_procesos_SR);
        b_sr.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),
                        Estudiante_Procesos_Tab.class);
                i.putExtra("opc","2");
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
}
