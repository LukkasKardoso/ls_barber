package com.example.ls_barver;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

public class activity_avisos extends AppCompatActivity {

    private TextView tvAvisoFeriados, tvAvisoPromocao, tvAvisoHorarios;
    private DatabaseHelper dbHelper;
    private DrawerLayout drawerLayout;
    private ImageView btnMenu;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avisos);

        dbHelper = new DatabaseHelper(this);

        tvAvisoFeriados = findViewById(R.id.tv_aviso_feriados);
        tvAvisoPromocao = findViewById(R.id.tv_aviso_promocao);
        tvAvisoHorarios = findViewById(R.id.tv_aviso_horarios);
        drawerLayout = findViewById(R.id.drawer_layout);
        btnMenu = findViewById(R.id.btn_menu_hamburger);
        navigationView = findViewById(R.id.nav_view);

        btnMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            Log.d("DEBUG_MENU", "ID selecionado: " + id);

            if (id == R.id.nav_conta) {
                Toast.makeText(this, "Em breve: Conta", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_agendamentos) {
                startActivity(new Intent(this, activity_meus_agendamentos.class));
            } else if (id == R.id.nav_tabela) {
                startActivity(new Intent(this, activity_servicos.class));
            } else if (id == R.id.nav_data_horarios) {
                Intent intent = new Intent(this, activity_agendamento.class);
                intent.putExtra("SERVICO_NOME", "Agendamento pelo Menu");
                startActivity(intent);
            } else if (id == R.id.nav_avisos) {
                // Já estamos aqui
            } else if (id == R.id.nav_sair) {
                finishAffinity();
                startActivity(new Intent(this, MainActivity.class));
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        carregarAvisosDoBanco();
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarAvisosDoBanco();
    }

    private void carregarAvisosDoBanco() {
        String feriado = dbHelper.getAviso("chave_feriado");
        String promocao = dbHelper.getAviso("chave_promocao");
        String horarios = dbHelper.getAviso("chave_horarios");

        if (feriado != null && !feriado.isEmpty()) tvAvisoFeriados.setText(feriado);
        if (promocao != null && !promocao.isEmpty()) tvAvisoPromocao.setText(promocao);
        if (horarios != null && !horarios.isEmpty()) tvAvisoHorarios.setText(horarios);
    }
}