package com.example.ls_barver;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class activity_meus_agendamentos extends AppCompatActivity {

    private ListView listViewAgendamentos;
    private TextView tvSemAgendamentos;
    private Button btnIrParaAvisos;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_agendamentos);

        listViewAgendamentos = findViewById(R.id.list_agendamentos);
        tvSemAgendamentos = findViewById(R.id.tv_sem_meus_agendamentos);
        btnIrParaAvisos = findViewById(R.id.btn_ir_para_avisos);
        dbHelper = new DatabaseHelper(this);

        btnIrParaAvisos.setOnClickListener(v -> startActivity(new Intent(this, activity_avisos.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarAgendamentos(); // Atualiza a tela toda vez que o usuário voltar para ela
    }

    private void carregarAgendamentos() {
        int usuarioId = getSharedPreferences("ls_barber_prefs", MODE_PRIVATE)
                .getInt("usuario_id", -1);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_AGENDAMENTOS,
                null,
                DatabaseHelper.COL_AG_USER_ID + " = ?",
                new String[]{String.valueOf(usuarioId)},
                null, null, DatabaseHelper.COL_AG_ID + " DESC"
        );

        ArrayList<String> listaAgendamentos = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String servico = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_AG_SERVICO));
                String data = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_AG_DATA));
                String hora = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_AG_HORA));
                String status = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_AG_STATUS));

                // Tratamento para evitar o "null"
                String statusExibicao = (status == null || status.isEmpty()) ? "Aguardando" : status;

                listaAgendamentos.add(servico + " - " + data + " às " + hora + "\nStatus: " + statusExibicao);
            }
            cursor.close();
        }

        if (listaAgendamentos.isEmpty()) {
            tvSemAgendamentos.setVisibility(View.VISIBLE);
            listViewAgendamentos.setVisibility(View.GONE);
        } else {
            tvSemAgendamentos.setVisibility(View.GONE);
            listViewAgendamentos.setVisibility(View.VISIBLE);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaAgendamentos);
            listViewAgendamentos.setAdapter(adapter);
        }
        db.close();
    }
}