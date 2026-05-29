package com.example.ls_barver;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText btLogin, SenhaLogin;
    Button btEntrar;
    TextView CriarConta, tvEsqueceuSenha; // Variável corrigida
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        btLogin = findViewById(R.id.btLogin);
        SenhaLogin = findViewById(R.id.SenhaLogin);
        btEntrar = findViewById(R.id.btEntrar);
        CriarConta = findViewById(R.id.CriarConta);

        // CORREÇÃO: Usando o ID exato que está no seu XML (btEsqueceuSenha)
        tvEsqueceuSenha = findViewById(R.id.btEsqueceuSenha);

        btEntrar.setOnClickListener(v -> realizarLogin());

        CriarConta.setOnClickListener(v -> startActivity(new Intent(this, activity_cadastro.class)));

        // Clique para a tela de recuperação de senha
        tvEsqueceuSenha.setOnClickListener(v -> {
            startActivity(new Intent(this, activity_esqueceu_senha.class));
        });
    }

    private void realizarLogin() {
        String email = btLogin.getText().toString().trim();
        String senha = SenhaLogin.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(senha)) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_USUARIOS,
                new String[]{DatabaseHelper.COL_USER_ID, DatabaseHelper.COL_USER_TIPO},
                DatabaseHelper.COL_USER_EMAIL + "=? AND " + DatabaseHelper.COL_USER_SENHA + "=?",
                new String[]{email, senha}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int usuarioId = cursor.getInt(0);
            String tipo = cursor.getString(1);
            cursor.close();

            getSharedPreferences("ls_barber_prefs", MODE_PRIVATE)
                    .edit()
                    .putInt("usuario_id", usuarioId)
                    .apply();

            if ("admin".equalsIgnoreCase(tipo)) {
                startActivity(new Intent(this, activity_admin_painel.class));
            } else {
                startActivity(new Intent(this, activity_servicos.class));
            }
            finish();
        } else {
            Toast.makeText(this, "Email ou senha incorretos!", Toast.LENGTH_SHORT).show();
        }
        if (cursor != null) cursor.close();
    }
}