package pe.pucp.tel306.firebox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        agregarLoginFragment();
    }

    public void agregarLoginFragment(){
        LoginFragment loginFragment = LoginFragment.newInstance();
        FragmentManager supportFragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.Fragments,loginFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void login(View view){

        List<AuthUI.IdpConfig> prooviders = Arrays.asList(
                new  AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        AuthUI instance = AuthUI.getInstance();
        Intent intent = instance.createSignInIntentBuilder().setAvailableProviders(prooviders).build();

        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==1 ){
            validarUsuario();
        }
    }

    public void  validarUsuario(){
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //firebaseUser.getUid()
        //Log.d("infoApp",)

        if (firebaseUser != null){
            firebaseUser.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(firebaseUser.isEmailVerified()){
                        //startActivity(new Intent(MainActivity.this, ArchivosActivity.class));

                        //-------------------------------------------------------------------------------------------------------------------------------------------------------------
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        databaseReference.child("users").addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                if(snapshot.getValue() != null){
                                    UsuarioDto usuarioDto = snapshot.getValue(UsuarioDto.class);
                                    if(usuarioDto.getUid().equalsIgnoreCase(firebaseUser.getUid())){
                                        Log.d("infoApp","YA EXISTE ESTE USUARIO");
                                        startActivity(new Intent(MainActivity.this, ArchivosActivity.class));
                                    }
                                    //Log.d("infoApp","NOMBRE : " + usuarioDto.getNombre() + " | UID : " + usuarioDto.getUid() + " | TIPO : " + usuarioDto.getTipo() + " | CAPACIDAD : " +usuarioDto.getCapacidad());
                                }
                            }
                            @Override
                            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                if(snapshot.getValue() != null){
                                    UsuarioDto usuarioDto = snapshot.getValue(UsuarioDto.class);
                                    Log.d("infoApp","NOMBRE : " + usuarioDto.getNombre() + " | UID : " + usuarioDto.getUid() + " | TIPO : " + usuarioDto.getTipo() + " | CAPACIDAD : " +usuarioDto.getCapacidad());
                                }
                            }
                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                            }
                            @Override
                            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                        //-------------------------------------------------------------------------------------------------------------------------------------------------------------

                        startActivity(new Intent(MainActivity.this, RegistroActivity.class));
                    }else {
                        Toast.makeText(MainActivity.this, "Se le ha enviado un correo para verificar su cuenta", Toast.LENGTH_SHORT).show();
                        firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("emailVer", "Correo enviado");
                            }
                        });
                    }
                }
            });
        }

    }
}