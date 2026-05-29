package com.example.ls_barver;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class activity_admin_agendamentos extends AppCompatActivity {

    private LinearLayout containerAgendamentos;
    private TextView tvSemAgendamentos;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_agendamentos);

        dbHelper = new DatabaseHelper(this);
        containerAgendamentos = findViewById(R.id.container_agendamentos);
        tvSemAgendamentos = findViewById(R.id.tv_sem_agendamentos);

        listarAgendamentos();
    }

    private void listarAgendamentos() {
        containerAgendamentos.removeAllViews();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT a." + DatabaseHelper.COL_AG_ID + ", a." + DatabaseHelper.COL_AG_SERVICO + ", " +
                "a." + DatabaseHelper.COL_AG_DATA + ", a." + DatabaseHelper.COL_AG_HORA + ", a." + DatabaseHelper.COL_AG_STATUS + ", " +
                "u." + DatabaseHelper.COL_USER_NOME + ", u." + DatabaseHelper.COL_USER_TELEFONE +
                " FROM " + DatabaseHelper.TABLE_AGENDAMENTOS + " a " +
                " INNER JOIN " + DatabaseHelper.TABLE_USUARIOS + " u ON a." + DatabaseHelper.COL_AG_USER_ID + " = u." + DatabaseHelper.COL_USER_ID +
                " ORDER BY a." + DatabaseHelper.COL_AG_ID + " DESC";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.getCount() > 0) {
            tvSemAgendamentos.setVisibility(View.GONE);
            while (cursor.moveToNext()) {
                final int id = cursor.getInt(0);
                String servico = cursor.getString(1);
                String data = cursor.getString(2);
                String hora = cursor.getString(3);
                String status = cursor.getString(4);
                String clienteNome = cursor.getString(5);

                LinearLayout cardLayout = new LinearLayout(this);
                cardLayout.setOrientation(LinearLayout.VERTICAL);
                cardLayout.setPadding(32, 32, 32, 32);
                cardLayout.setBackgroundColor(Color.parseColor("#11142A"));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
                params.setMargins(0, 0, 0, 24);
                cardLayout.setLayoutParams(params);

                TextView tvInfo = new TextView(this);
                tvInfo.setText("Cliente: " + clienteNome + "\nServiço: " + servico + "\nData: " + data + " às " + hora);
                tvInfo.setTextColor(Color.WHITE);
                cardLayout.addView(tvInfo);

                TextView tvStatus = new TextView(this);
                tvStatus.setText("Status: " + status);
                tvStatus.setTypeface(null, Typeface.BOLD);
                tvStatus.setTextColor(status.equalsIgnoreCase("Cancelado") ? Color.RED : Color.GREEN);
                cardLayout.addView(tvStatus);

                if (!status.equalsIgnoreCase("Cancelado")) {
                    Button btnCancelar = new Button(this);
                    btnCancelar.setText("Cancelar Agendamento");
                    btnCancelar.setBackgroundColor(Color.RED);
                    btnCancelar.setOnClickListener(v -> {
                        dbHelper.atualizarStatusAgendamento(id, "Cancelado");
                        Toast.makeText(this, "Agendamento cancelado!", Toast.LENGTH_SHORT).show();
                        listarAgendamentos();
                    });
                    cardLayout.addView(btnCancelar);
                }
                containerAgendamentos.addView(cardLayout);
            }
            cursor.close();
        } else {
            tvSemAgendamentos.setVisibility(View.VISIBLE);
        }
        db.close();
    }
}