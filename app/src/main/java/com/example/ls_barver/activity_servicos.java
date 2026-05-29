package com.example.ls_barver;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class activity_servicos extends AppCompatActivity {

    private TextView tvBemVindo;
    // Removidos os botões btnAvisos e btnSair
    private Button btnCorte, btnBarba, btnSobrancelha, btnPenteado, btnPezinho;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicos);

        tvBemVindo = findViewById(R.id.tv_bem_vindo);
        btnCorte = findViewById(R.id.btn_corte);
        btnBarba = findViewById(R.id.btn_barba);
        btnSobrancelha = findViewById(R.id.btn_sobrancelha);
        btnPenteado = findViewById(R.id.btn_penteado);
        btnPezinho = findViewById(R.id.btn_pezinho);

        SharedPreferences prefs = getSharedPreferences("ls_barber_prefs", MODE_PRIVATE);
        tvBemVindo.setText("Olá, " + prefs.getString("usuario_nome", "Cliente") + "!");

        btnCorte.setOnClickListener(v -> abrirAgendamento("Corte de Cabelo"));
        btnBarba.setOnClickListener(v -> abrirAgendamento("Barba"));
        btnSobrancelha.setOnClickListener(v -> abrirAgendamento("Sobrancelha"));
        btnPenteado.setOnClickListener(v -> abrirAgendamento("Penteado"));
        btnPezinho.setOnClickListener(v -> abrirAgendamento("Pezinho"));

        // Nenhum código extra para Avisos ou Sair
    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizarPrecos();
    }

    private void atualizarPrecos() {
        SharedPreferences prefs = getSharedPreferences("ls_barber_servicos", MODE_PRIVATE);
        btnCorte.setText("Corte - R$ " + prefs.getString("preco_corte", "35"));
        btnBarba.setText("Barba - R$ " + prefs.getString("preco_barba", "25"));
        btnSobrancelha.setText("Sobrancelhas - R$ " + prefs.getString("preco_sobrancelha", "10"));
        btnPenteado.setText("Penteado - R$ " + prefs.getString("preco_penteado", "15"));
        btnPezinho.setText("Pezinho - R$ " + prefs.getString("preco_pezinho", "5"));
    }

    private void abrirAgendamento(String servico) {
        Intent intent = new Intent(this, activity_agendamento.class);
        intent.putExtra("SERVICO_NOME", servico);
        startActivity(intent);
    }
}