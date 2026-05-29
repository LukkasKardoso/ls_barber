package com.example.ls_barver;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class activity_admin_painel extends AppCompatActivity {

    private Button btnGerenciarAgendamentos, btnEditarAvisos, btnEditarServicos, btnSairPainel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_painel);

        // Mapeamento dos componentes
        btnGerenciarAgendamentos = findViewById(R.id.btn_gerenciar_agendamentos);
        btnEditarAvisos = findViewById(R.id.btn_editar_avisos);
        btnSairPainel = findViewById(R.id.btn_sair_painel);
        btnEditarServicos = findViewById(R.id.btn_editar_servicos);

// Configure o clique:
        btnEditarServicos.setOnClickListener(v -> {
            startActivity(new Intent(activity_admin_painel.this, activity_admin_servicos.class));
        });

        // Configuração dos cliques
        btnGerenciarAgendamentos.setOnClickListener(v -> {
            startActivity(new Intent(activity_admin_painel.this, activity_admin_agendamentos.class));
        });

        btnEditarAvisos.setOnClickListener(v -> {
            startActivity(new Intent(activity_admin_painel.this, activity_admin_avisos.class));
        });

        btnSairPainel.setOnClickListener(v -> realizarLogout());
    }

    private void realizarLogout() {
        // Limpar dados da sessão
        SharedPreferences prefs = getSharedPreferences("ls_barber_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        // Redirecionar para o Login
        startActivity(new Intent(activity_admin_painel.this, MainActivity.class));
        finish();
    }
}