package com.example.ls_barver;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

public class activity_servicos extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Button btnCorte, btnBarba, btnSobrancelha, btnPenteado, btnPezinho;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicos);

        // Configuração do Menu Lateral
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        findViewById(R.id.btn_menu_hamburger).setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // Listener de Navegação (Isso faz o menu funcionar!)
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_agendamentos) startActivity(new Intent(this, activity_meus_agendamentos.class));
            else if (id == R.id.nav_tabela) { /* Já estamos aqui */ }
            else if (id == R.id.nav_data_horarios) startActivity(new Intent(this, activity_agendamento.class));
            else if (id == R.id.nav_avisos) startActivity(new Intent(this, activity_avisos.class));
            else if (id == R.id.nav_sair) { finishAffinity(); startActivity(new Intent(this, MainActivity.class)); }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // Inicialização dos Botões de Serviço
        btnCorte = findViewById(R.id.btn_corte);
        btnBarba = findViewById(R.id.btn_barba);
        btnSobrancelha = findViewById(R.id.btn_sobrancelha);
        btnPenteado = findViewById(R.id.btn_penteado);
        btnPezinho = findViewById(R.id.btn_pezinho);

        // Ações de clique
        btnCorte.setOnClickListener(v -> abrirAgendamento("Corte - R$ 35"));
        btnBarba.setOnClickListener(v -> abrirAgendamento("Barba - R$ 25"));
        btnSobrancelha.setOnClickListener(v -> abrirAgendamento("Sobrancelhas - R$ 10"));
        btnPenteado.setOnClickListener(v -> abrirAgendamento("Penteado - R$ 15"));
        btnPezinho.setOnClickListener(v -> abrirAgendamento("Pezinho - R$ 5"));
    }

    private void abrirAgendamento(String servico) {
        Intent intent = new Intent(this, activity_agendamento.class);
        intent.putExtra("SERVICO_NOME", servico);
        startActivity(intent);
    }
}