package com.example.ls_barver;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class activity_agendamento extends AppCompatActivity {

    private TextView tvHorario10, tvHorario11, tvHorario12;
    private CalendarView calendarView;
    private Button btnConfirmar;
    private String servicoSelecionado;
    private String dataSelecionada = "";
    private String horaSelecionada = "";
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendamento);

        dbHelper = new DatabaseHelper(this);

        // CORREÇÃO: Recebendo a chave correta "SERVICO_NOME"
        servicoSelecionado = getIntent().getStringExtra("SERVICO_NOME");
        if (servicoSelecionado == null) servicoSelecionado = "Serviço";

        calendarView = findViewById(R.id.calendar_view);
        btnConfirmar = findViewById(R.id.btn_confirmar);
        tvHorario10 = findViewById(R.id.tv_horario_10);
        tvHorario11 = findViewById(R.id.tv_horario_11);
        tvHorario12 = findViewById(R.id.tv_horario_12);

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            dataSelecionada = dayOfMonth + "/" + (month + 1) + "/" + year;
        });

        tvHorario10.setOnClickListener(v -> horaSelecionada = "10:00");
        tvHorario11.setOnClickListener(v -> horaSelecionada = "11:00");
        tvHorario12.setOnClickListener(v -> horaSelecionada = "12:00");

        btnConfirmar.setOnClickListener(v -> confirmarAgendamento());
    }

    private void confirmarAgendamento() {
        if (dataSelecionada.isEmpty() || horaSelecionada.isEmpty()) {
            Toast.makeText(this, "Selecione data e horário!", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences prefs = getSharedPreferences("ls_barber_prefs", MODE_PRIVATE);
        int usuarioId = prefs.getInt("usuario_id", -1);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_AG_USER_ID, usuarioId);
        values.put(DatabaseHelper.COL_AG_SERVICO, servicoSelecionado); // Agora gravado corretamente
        values.put(DatabaseHelper.COL_AG_DATA, dataSelecionada);
        values.put(DatabaseHelper.COL_AG_HORA, horaSelecionada);
        values.put(DatabaseHelper.COL_AG_STATUS, "Confirmado"); // Status padrão

        long resultado = db.insert(DatabaseHelper.TABLE_AGENDAMENTOS, null, values);

        if (resultado != -1) {
            Toast.makeText(this, "Agendamento confirmado!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, activity_meus_agendamentos.class));
            finish();
        } else {
            Toast.makeText(this, "Erro ao agendar.", Toast.LENGTH_SHORT).show();
        }
    }
}