package com.example.ls_barver;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

public class activity_agendamento extends AppCompatActivity {

    private CalendarView calendarView;
    private Button btnConfirmar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private String servicoSelecionado, dataSelecionada = "", horaSelecionada = "";
    private DatabaseHelper dbHelper;
    private TextView[] todosHorarios;
    private final String[] valoresHorarios = {"08:00", "08:40", "09:20", "10:00", "10:40", "11:20", "12:00", "12:40", "13:20", "14:00", "14:40", "15:20", "16:00", "16:40", "17:20"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendamento);

        dbHelper = new DatabaseHelper(this);
        servicoSelecionado = getIntent().getStringExtra("SERVICO_NOME");

        // UI e Navegação
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        calendarView = findViewById(R.id.calendar_view);
        btnConfirmar = findViewById(R.id.btn_confirmar);

        // Menu Hambúrguer (Certifique-se que o ID existe no XML)
        findViewById(R.id.btn_menu_hamburger).setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // Setup do Menu Lateral
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_agendamentos) startActivity(new Intent(this, activity_meus_agendamentos.class));
            else if (id == R.id.nav_tabela) startActivity(new Intent(this, activity_servicos.class));
            else if (id == R.id.nav_avisos) startActivity(new Intent(this, activity_avisos.class));
            else if (id == R.id.nav_sair) { finishAffinity(); startActivity(new Intent(this, MainActivity.class)); }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // Configuração dos horários
        todosHorarios = new TextView[]{
                findViewById(R.id.tv_h_0800), findViewById(R.id.tv_h_0840), findViewById(R.id.tv_h_0920),
                findViewById(R.id.tv_h_1000), findViewById(R.id.tv_h_1040), findViewById(R.id.tv_h_1120),
                findViewById(R.id.tv_h_1200), findViewById(R.id.tv_h_1240), findViewById(R.id.tv_h_1320),
                findViewById(R.id.tv_h_1400), findViewById(R.id.tv_h_1440), findViewById(R.id.tv_h_1520),
                findViewById(R.id.tv_h_1600), findViewById(R.id.tv_h_1640), findViewById(R.id.tv_h_1720)
        };

        calendarView.setOnDateChangeListener((view, year, month, day) -> {
            dataSelecionada = day + "/" + (month + 1) + "/" + year;
            horaSelecionada = "";
            atualizarCoresHorarios();
        });

        for (int i = 0; i < todosHorarios.length; i++) {
            final String h = valoresHorarios[i];
            todosHorarios[i].setOnClickListener(v -> horaSelecionada = h);
        }

        btnConfirmar.setOnClickListener(v -> confirmarAgendamento());
    }

    private void atualizarCoresHorarios() {
        java.util.List<String> ocupados = dbHelper.getHorariosOcupados(dataSelecionada);
        for (int i = 0; i < todosHorarios.length; i++) {
            boolean isOcupado = ocupados.contains(valoresHorarios[i]);
            todosHorarios[i].setBackgroundColor(isOcupado ? Color.GRAY : Color.WHITE);
            todosHorarios[i].setEnabled(!isOcupado);
        }
    }

    private void confirmarAgendamento() {
        if (dataSelecionada.isEmpty() || horaSelecionada.isEmpty()) {
            Toast.makeText(this, "Selecione data e hora!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (dbHelper.isHorarioOcupado(dataSelecionada, horaSelecionada)) {
            new AlertDialog.Builder(this).setTitle("Indisponível").setMessage("Horário ocupado!").setPositiveButton("OK", null).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_AG_USER_ID, 1);
        values.put(DatabaseHelper.COL_AG_SERVICO, servicoSelecionado != null ? servicoSelecionado : "Corte");
        values.put(DatabaseHelper.COL_AG_DATA, dataSelecionada);
        values.put(DatabaseHelper.COL_AG_HORA, horaSelecionada);
        values.put(DatabaseHelper.COL_AG_STATUS, "Confirmado");

        long resultado = db.insert(DatabaseHelper.TABLE_AGENDAMENTOS, null, values);
        db.close();

        if (resultado != -1) {
            Toast.makeText(this, "Agendamento realizado!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Erro ao salvar.", Toast.LENGTH_SHORT).show();
        }
    }
}