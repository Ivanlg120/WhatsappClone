package com.ivanlg.whatsappclone.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.ivanlg.whatsappclone.R;
import com.ivanlg.whatsappclone.helper.Preferencias;

import java.util.HashMap;
import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    private EditText codpais;
    private EditText codarea;
    private EditText numero;
    private EditText nome;
    private Button botaocadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        codpais = findViewById(R.id.codpaisid);
        codarea = findViewById(R.id.codareaid);
        nome = findViewById(R.id.nomeid);
        numero = findViewById(R.id.telefoneid);
        botaocadastrar = findViewById(R.id.botcadastrarid);

        SimpleMaskFormatter simpleMaskcodpais = new SimpleMaskFormatter("+NN");
        SimpleMaskFormatter simpleMaskcodarea = new SimpleMaskFormatter("NN");
        SimpleMaskFormatter simpleMasknumero = new SimpleMaskFormatter("NNNN-NNNN");

        MaskTextWatcher maskcodpais = new MaskTextWatcher(codpais,simpleMaskcodpais);
        MaskTextWatcher maskcodarea = new MaskTextWatcher(codarea,simpleMaskcodarea);
        MaskTextWatcher masknumero = new MaskTextWatcher(numero,simpleMasknumero);

        codpais.addTextChangedListener(maskcodpais);
        codarea.addTextChangedListener(maskcodarea);
        numero.addTextChangedListener(masknumero);

        botaocadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomeUsusario  = nome.getText().toString();
                String numeroComplesto = codpais.getText().toString()+codarea.getText().toString()+numero.getText().toString();
                String numeroSemForma = numeroComplesto.replace("-","");

                //Random randomico = new Random();
                //int numerorandon = randomico.nextInt(8999)+1000;
                //String token = String.valueOf(numerorandon);

                Preferencias preferencias = new Preferencias(getApplicationContext());
                preferencias.salvarUsuarioPreferencias(nomeUsusario,numeroSemForma);

                //String mensagemenvio="Codigo de confirmação: "+token;
                //boolean enviadosms = enviaSMS("+"+numeroSemForma,mensagemenvio);
                //HashMap<String,String> usuario = preferencias.getDadosUsuario();

                startActivity(new Intent(LoginActivity.this,ValidadorActivity.class));

            }
        });



    }
/*
    private boolean enviaSMS(String numero,String mensagem){
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(numero,null,mensagem,null,null);
            return true;


        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

 */
}
