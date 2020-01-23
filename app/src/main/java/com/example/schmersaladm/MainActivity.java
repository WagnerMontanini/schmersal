package com.example.schmersaladm;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseFirestore db;
    private ListView listDados;
    private List<Map<String, Object>> listaDadosFirebase = new ArrayList<Map<String, Object>>();
    private ArrayAdapter<Map<String, Object>> arrayAdapterDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        inicializarFirebase();
        listDados = (ListView)findViewById(R.id.listDados);
        recuperarDados();
        recuperarDadosCloudFirestore();

    }

    private void inicializarFirebase() {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(MainActivity.this);
        database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        databaseReference = database.getReference();

        //Inicializar o Cloud Firestore
        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_desconectar:
                mAuth.signOut();
                voltarTelaLogin();
            default:
                return  super.onOptionsItemSelected(item);
        }
    }

    public void voltarTelaLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
        Toast.makeText(this, "Usuário Desconectado", Toast.LENGTH_SHORT).show();
    }

    public void recuperarDados(){

        // Read from the database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //listaDadosFirebase.clear();
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                //listaDadosFirebase.add(map);
                //arrayAdapterDados = new ArrayAdapter<Map<String, Object>>(MainActivity.this,
                       // android.R.layout.simple_list_item_1,listaDadosFirebase);
                //listDados.setAdapter(arrayAdapterDados);
                for (String key : map.keySet()) {
                    Object mapString = map.get(key);
                    if(mapString != null) {
                        //Capturamos o valor a partir da chave
                        Map<String, Object> tipoProdutoSuporte = (HashMap<String, Object>) mapString;
                        for (String keytipo : tipoProdutoSuporte.keySet()) {
                            Object tipoProdutoSuporteString = tipoProdutoSuporte.get(keytipo);
                            if(tipoProdutoSuporteString != null) {
                                Map<String, Object> tipoProdutoSuporte2 = (HashMap<String, Object>) tipoProdutoSuporteString;
                                for (String keytipo2 : tipoProdutoSuporte2.keySet()) {
                                    Object tipoProdutoSuporte2String = tipoProdutoSuporte2.get(keytipo2);
                                    if (tipoProdutoSuporte2String != null && tipoProdutoSuporte2String != "") {
                                        Map<String, Object> tipoProdutoSuporte3 = (HashMap<String, Object>) tipoProdutoSuporte2String;
                                        for (String keytipo3 : tipoProdutoSuporte3.keySet()) {
                                            Object tipoProdutoSuporte3String = tipoProdutoSuporte3.get(keytipo3);
                                            if (tipoProdutoSuporte3String != null  ) {
                                                //listaDadosFirebase.add(tipoProdutoSuporte3);
                                                //arrayAdapterDados = new ArrayAdapter<Map<String, Object>>(MainActivity.this,
                                                //    android.R.layout.simple_list_item_1,listaDadosFirebase);
                                                //listDados.setAdapter(arrayAdapterDados);
                                                System.out.println("dados" + key + " = " + keytipo + " = " + keytipo2 + " = " + keytipo3 + " = " + tipoProdutoSuporte3String  );
                                            }
                                        }
                                    }
                                   //System.out.println(key + " = " + keytipo + " = " + keytipo2 + " = " + tipoProdutoSuporte2String);
                                }
                            }
                        }
                    }
                }
                //Log.w("TAG", "O valor é:"+ map.keySet() );

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

    }

    public void recuperarDadosCloudFirestore(){
        //FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Confiance 222s")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
