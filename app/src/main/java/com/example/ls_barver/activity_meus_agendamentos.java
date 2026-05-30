package com.example.ls_barver;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class activity_meus_agendamentos extends AppCompatActivity {

    private ListView listViewAgendamentos;
    private TextView tvSemAgendamentos;
    private Button btnIrParaAvisos;
    private DatabaseHelper dbHelper;
    private DrawerLayout drawerLayout;
    private ImageView btnMenu;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_agendamentos);

        listViewAgendamentos = findViewById(R.id.list_agendamentos);
        tvSemAgendamentos = findViewById(R.id.tv_sem_meus_agendamentos);
        btnIrParaAvisos = findViewById(R.id.btn_ir_para_avisos);
        dbHelper = new DatabaseHelper(this);

        drawerLayout = findViewById(R.id.drawer_layout);
        btnMenu = findViewById(R.id.btn_menu_hamburger);
        navigationView = findViewById(R.id.nav_view);

        btnIrParaAvisos.setOnClickListener(v -> startActivity(new Intent(this, activity_avisos.class)));
        btnMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // Navegação Corrigida
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_conta) {
                Toast.makeText(this, "Em breve: Conta", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_agendamentos) {
                // Já estamos aqui
            } else if (id == R.id.nav_tabela) {
                startActivity(new Intent(this, activity_servicos.class));
            } else if (id == R.id.nav_data_horarios) {
                Intent intent = new Intent(this, activity_agendamento.class);
                intent.putExtra("SERVICO_NOME", "Agendamento pelo Menu");
                startActivity(intent);
            } else if (id == R.id.nav_avisos) {
                startActivity(new Intent(this, activity_avisos.class));
            } else if (id == R.id.nav_sair) {
                finishAffinity(); // Fecha todas as telas e limpa o stack
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarAgendamentos();
    }

    private void carregarAgendamentos() {
        int usuarioId = getSharedPreferences("ls_barber_prefs", MODE_PRIVATE).getInt("usuario_id", -1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DatabaseHelper.TABLE_AGENDAMENTOS, null,
                DatabaseHelper.COL_AG_USER_ID + " = ?", new String[]{String.valueOf(usuarioId)},
                null, null, DatabaseHelper.COL_AG_ID + " DESC");

        ArrayList<String> listaAgendamentos = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String servico = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_AG_SERVICO));
                String data = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_AG_DATA));
                String hora = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_AG_HORA));
                String status = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_AG_STATUS));
                listaAgendamentos.add(servico + " - " + data + " às " + hora + "\nStatus: " + (status == null ? "Aguardando" : status));
            }
            cursor.close();
        }

        if (listaAgendamentos.isEmpty()) {
            tvSemAgendamentos.setVisibility(View.VISIBLE);
            listViewAgendamentos.setVisibility(View.GONE);
        } else {
            tvSemAgendamentos.setVisibility(View.GONE);
            listViewAgendamentos.setVisibility(View.VISIBLE);
            listViewAgendamentos.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaAgendamentos));
        }
        db.close();
    }
}