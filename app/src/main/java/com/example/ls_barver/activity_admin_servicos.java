package com.example.ls_barver;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class activity_admin_servicos extends AppCompatActivity {

    // Componentes da UI
    private EditText etCorte, etBarba, etSobrancelha, etPenteado, etPezinho;
    private Button btnSalvar;

    // Nome do arquivo onde os preços serão salvos
    private static final String PREFS_NAME = "ls_barber_servicos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_servicos);

        // Inicialização dos campos
        etCorte = findViewById(R.id.et_preco_corte);
        etBarba = findViewById(R.id.et_preco_barba);
        etSobrancelha = findViewById(R.id.et_preco_sobrancelha);
        etPenteado = findViewById(R.id.et_preco_penteado);
        etPezinho = findViewById(R.id.et_preco_pezinho);
        btnSalvar = findViewById(R.id.btn_salvar_servicos);

        // Carrega os dados salvos anteriormente
        carregarPrecos();

        // Ação do botão
        btnSalvar.setOnClickListener(v -> salvarPrecos());
    }

    private void carregarPrecos() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Define os valores padrão caso seja a primeira vez abrindo
        etCorte.setText(prefs.getString("preco_corte", "35"));
        etBarba.setText(prefs.getString("preco_barba", "25"));
        etSobrancelha.setText(prefs.getString("preco_sobrancelha", "10"));
        etPenteado.setText(prefs.getString("preco_penteado", "15"));
        etPezinho.setText(prefs.getString("preco_pezinho", "5"));
    }

    private void salvarPrecos() {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();

        editor.putString("preco_corte", etCorte.getText().toString());
        editor.putString("preco_barba", etBarba.getText().toString());
        editor.putString("preco_sobrancelha", etSobrancelha.getText().toString());
        editor.putString("preco_penteado", etPenteado.getText().toString());
        editor.putString("preco_pezinho", etPezinho.getText().toString());

        // Salva os dados de forma assíncrona
        editor.apply();

        Toast.makeText(this, "Preços atualizados com sucesso!", Toast.LENGTH_SHORT).show();
        finish(); // Fecha a tela e retorna ao painel
    }
}