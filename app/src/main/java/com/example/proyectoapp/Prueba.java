package com.example.proyectoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.StringTokenizer;

public class Prueba extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba); IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Apunta al codigo qr del eveto");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.setOrientationLocked(false);
        integrator.setCaptureActivity(CaptureActivityPortrait.class);
        integrator.initiateScan();
        //Intent i = new Intent(this.getContext(), InfoEvento.class);

    }

    @Override
    protected void onActivityResult(int requestCode, int resulCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resulCode,data);
        Log.d("key of the message", "00000000000000000000000000000000");
        if(result!=null){
            if(result.getContents()==null){
                Toast.makeText(this,"Lectura cancelada",Toast.LENGTH_LONG).show();
            }else{
                Log.d("key of the message", "--------------------------" + result.getContents());
                String scanContent=result.getContents();

                StringTokenizer t = new StringTokenizer(scanContent,"*");
                Intent i = new Intent(Prueba.this,InfoEvento.class);
                i.putExtra("idEvento",t.nextToken());
                i.putExtra("ENombre",t.nextToken());
                i.putExtra("EFecha",t.nextToken());
                i.putExtra("EHora",t.nextToken());
                i.putExtra("ELugar",t.nextToken());
                i.putExtra("ELatitud",t.nextToken());
                i.putExtra("ELongitud",t.nextToken());
                startActivity(i);

                Toast.makeText(this,result.getContents(),Toast.LENGTH_LONG).show();
            }
        }else{
            super.onActivityResult(requestCode,resulCode,data);
        }
    }
}