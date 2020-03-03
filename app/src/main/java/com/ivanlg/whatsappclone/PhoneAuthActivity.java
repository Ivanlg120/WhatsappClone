package com.ivanlg.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.ivanlg.whatsappclone.helper.Preferencias;

import java.util.concurrent.TimeUnit;

public class PhoneAuthActivity extends AppCompatActivity {

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private boolean mVerificationInProgress = false;

    private FirebaseAuth mAuth;

    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private String numero = "+5519981652740";

    private EditText codigo;
    private Button botao;

    private Button sair;
    private Button enviar;
    private Button reenviar;

    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);
        status = findViewById(R.id.statusid);
        status.setText("entando");


        codigo = findViewById(R.id.codigoid2);
        botao = findViewById(R.id.botaoid);
        sair = findViewById(R.id.sairid);
        enviar = findViewById(R.id.enviari);
        reenviar = findViewById(R.id.reenviar);

        mAuth = FirebaseAuth.getInstance();

        Preferencias preferencias = new Preferencias(getApplicationContext());
        if(preferencias.getCredencial()!=null){
            signInToken(preferencias.getCredencial());
        }else {
            status.setText("deslogado");

            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    Log.i("testeee", "onVerificationCompleted:" + phoneAuthCredential);

                    signInWithPhoneAuthCredential(phoneAuthCredential);

                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    // This callback is invoked in an invalid request for verification is made,
                    // for instance if the the phone number format is not valid.
                    Log.i("testeee", "onVerificationFailed", e);

                    if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        // Invalid request
                        // ...
                    } else if (e instanceof FirebaseTooManyRequestsException) {
                        // The SMS quota for the project has been exceeded
                        // ...
                    }

                    // Show a message and update the UI
                    // ...

                }

                @Override
                public void onCodeSent(@NonNull String verificationId,
                                       @NonNull PhoneAuthProvider.ForceResendingToken token) {
                    // The SMS verification code has been sent to the provided phone number, we
                    // now need to ask the user to enter the code and then construct a credential
                    // by combining the code with a verification ID.
                    Log.i("testeee", "onCodeSent:" + verificationId);

                    // Save verification ID and resending token so we can use them later
                    mVerificationId = verificationId;
                    mResendToken = token;

                    // ...
                }
            };

            botao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String code = codigo.getText().toString();
                    if (TextUtils.isEmpty(code)) {
                        codigo.setError("Cannot be empty.");
                        Log.i("testeee", "nao foi");
                        return;
                    }

                    verifyPhoneNumberWithCode(mVerificationId, code);
                }
            });

            sair.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mAuth.signOut();
                    Log.i("testeee", "saiu");
                }
            });

            enviar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("testeee", "manar");
                    startPhoneNumberVerification(numero);
                }
            });

            reenviar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("testeee", "remanar");
                    resendVerificationCode(numero, mResendToken);
                }
            });
        }
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks

        status.setText("sms reenviado");
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        status.setText("sms enviado");

        mVerificationInProgress = true;
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.i("testeee", "signInWithCredential:success");

                            status.setText("logado");

                            FirebaseUser user = task.getResult().getUser();
                            Preferencias preferencias = new Preferencias(getApplicationContext());
                            preferencias.salvarcredencial(user.getIdToken(false).getResult().getToken());


                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.i("testeee", "signInWithCredential:failure", task.getException());

                            status.setText("nao logou");
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Log.i("testeee", "invalio");

                                status.setText("cod errado");
                            }
                        }
                    }
                });
    }

    private void signInToken(String token){
        mAuth.signInWithCustomToken(token)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.i("testeee", "signIntoken:success");

                            status.setText("logado");

                            FirebaseUser user = task.getResult().getUser();

                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.i("testeee", "signIntoken:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Log.i("testeee", "token invalio");

                                status.setText("nao logou token");
                            }
                        }
                    }
                });
    }
}
