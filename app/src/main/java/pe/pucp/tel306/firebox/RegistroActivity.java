package pe.pucp.tel306.firebox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class RegistroActivity extends AppCompatActivity {
    UsuarioDto usuarioDto = new UsuarioDto();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //----------------------------------------------------
        /*
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getValue() != null){
                    UsuarioDto usuarioDto = snapshot.getValue(UsuarioDto.class);
                    Log.d("infoApp","NOMBRE : " + usuarioDto.getNombre() + " | UID : " + usuarioDto.getUid() + " | TIPO : " + usuarioDto.getTipo() + " | CAPACIDAD : " +usuarioDto.getCapacidad());
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
        */

        //----------------------------------------------------

        //----------------------------------------------------------------------------------------------------------------------------------------
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        usuarioDto.setNombre(firebaseUser.getDisplayName());
        usuarioDto.setUid(firebaseUser.getUid());

        String tipoSeleccionado = null;
        String [] lista = {"Free        (1 BYTE)","silver        (10M BYTE)", "Gold        (100G BYTE)","Black        (100T BYTE)"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,lista);
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //tipoSeleccionado = parent.getItemAtPosition(position).toString();
                Log.d("infoApp","SELECCIONASTE ESTO : " + parent.getItemAtPosition(position).toString());

                if(position == 0){
                    usuarioDto.setTipo("Free");
                    usuarioDto.setCapacidad("1 BYTE");
                }else if(position == 1){
                    usuarioDto.setTipo("Silver");
                    usuarioDto.setCapacidad("10M BYTE");
                }else if(position == 2){
                    usuarioDto.setTipo("Gold");
                    usuarioDto.setCapacidad("100G BYTE");
                }else if(position == 3){
                    usuarioDto.setTipo("Black");
                    usuarioDto.setCapacidad("100T BYTE");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //----------------------------------------------------------------------------------------------------------------------------------------
    }

    public void guardarUsuario(View view){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        /*
        databaseReference.child(usuarioDto.getUid()).setValue(usuarioDto)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("infoApp","GUARDADO EXITOSO EN TU DATABASE");
                        Log.d("infoApp","NOMBRE : " + usuarioDto.getNombre());
                        Log.d("infoApp","UID : " + usuarioDto.getUid());
                        Log.d("infoApp","TIPO : " + usuarioDto.getTipo());
                        Log.d("infoApp","CAPACIDAD : " + usuarioDto.getCapacidad());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
        */
        databaseReference.child("users").push().setValue(usuarioDto)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent = new Intent(RegistroActivity.this, ArchivosActivity.class);
                        intent.putExtra("usuario", usuarioDto);
                        startActivity(intent);
                        finish();
                        Log.d("infoApp","GUARDADO EXITOSO EN TU DATABASE");
                        Log.d("infoApp","NOMBRE : " + usuarioDto.getNombre());
                        Log.d("infoApp","UID : " + usuarioDto.getUid());
                        Log.d("infoApp","TIPO : " + usuarioDto.getTipo());
                        Log.d("infoApp","CAPACIDAD : " + usuarioDto.getCapacidad());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });

    }
}