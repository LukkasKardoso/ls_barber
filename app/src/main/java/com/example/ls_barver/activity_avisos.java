package com.example.ls_barver;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class activity_avisos extends AppCompatActivity {

    private TextView tvAvisoFeriados, tvAvisoPromocao, tvAvisoHorarios;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avisos);

        dbHelper = new DatabaseHelper(this);

        // Inicializa os TextViews
        tvAvisoFeriados = findViewById(R.id.tv_aviso_feriados);
        tvAvisoPromocao = findViewById(R.id.tv_aviso_promocao);
        tvAvisoHorarios = findViewById(R.id.tv_aviso_horarios);

        // Carrega os avisos ao iniciar
        carregarAvisosDoBanco();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recarrega os avisos toda vez que o usuário entrar nesta tela
        carregarAvisosDoBanco();
    }

    private void carregarAvisosDoBanco() {
        // Busca os textos usando os métodos do seu DatabaseHelper
        // Nota: Certifique-se de que seu DatabaseHelper tenha o método getAviso
        String feriado = dbHelper.getAviso("chave_feriado");
        String promocao = dbHelper.getAviso("chave_promocao");
        String horarios = dbHelper.getAviso("chave_horarios");

        // Aplica os textos se eles não forem nulos
        if (feriado != null && !feriado.isEmpty()) {
            tvAvisoFeriados.setText(feriado);
        }

        if (promocao != null && !promocao.isEmpty()) {
            tvAvisoPromocao.setText(promocao);
        }

        if (horarios != null && !horarios.isEmpty()) {
            tvAvisoHorarios.setText(horarios);
        }
    }
}