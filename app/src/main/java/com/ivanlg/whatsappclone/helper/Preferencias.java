package com.ivanlg.whatsappclone.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.PhoneAuthCredential;

import java.util.HashMap;

public class Preferencias {

    private Context contexto;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    //private PhoneAuthCredential credential;
    private final String NOME_ARQUIVOS = "whatsapp_preferencias";
    private final String CHAVE_NOME = "nome";
    private final String CHAVE_numero = "numero";
    private final String CHAVE_Credencial = "credencial";
    //private final String CHAVE_token = "token";
    private final int MODE=0;

    public  Preferencias(Context contextParametros){
        contexto = contextParametros;
        preferences = contexto.getSharedPreferences(NOME_ARQUIVOS,MODE);
        editor = preferences.edit();
    }

    public void salvarUsuarioPreferencias(String nome, String numero){
        editor.putString(CHAVE_NOME,nome);
        editor.putString(CHAVE_numero,numero);
        //editor.putString(CHAVE_token,token);
        editor.commit();
    }

    public HashMap<String,String> getDadosUsuario(){
        HashMap<String,String> dadosUsuario = new HashMap<>();
        dadosUsuario.put(CHAVE_NOME,preferences.getString(CHAVE_NOME,null));
        dadosUsuario.put(CHAVE_numero,preferences.getString(CHAVE_numero,null));
        //dadosUsuario.put(CHAVE_token,preferences.getString(CHAVE_token,null));
        return dadosUsuario;
    }

    public String getnumero(){
        return preferences.getString(CHAVE_numero,null);
    }

    public void salvarcredencial (String c){
        editor.putString(CHAVE_Credencial,c);

        editor.commit();
    }

    public String getCredencial(){
        String credencial = preferences.getString(CHAVE_Credencial,null);
        return  credencial;
    }



}
