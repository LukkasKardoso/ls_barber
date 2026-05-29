package com.example.ls_barver;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class activity_cadastro extends AppCompatActivity {

    EditText btNome, btEmailCadastro, btSenhaCadastro, btConfirmarSn;
    Button btCadastro;
    TextView btVoltar;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        dbHelper = new DatabaseHelper(this);

        btNome = findViewById(R.id.btNome);
        btEmailCadastro = findViewById(R.id.btEmailCadastro);
        btSenhaCadastro = findViewById(R.id.btSenhaCadastro);
        btConfirmarSn = findViewById(R.id.btConfirmarSn);
        btCadastro = findViewById(R.id.btCadastro);
        btVoltar = findViewById(R.id.btVoltar);

        btCadastro.setOnClickListener(v -> realizarCadastro());

        btVoltar.setOnClickListener(v -> finish());
    }

    private void realizarCadastro() {
        String nome = btNome.getText().toString().trim();
        String email = btEmailCadastro.getText().toString().trim();
        String senha = btSenhaCadastro.getText().toString().trim();
        String confirmar = btConfirmarSn.getText().toString().trim();

        if (TextUtils.isEmpty(nome) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(senha) || TextUtils.isEmpty(confirmar)) {
            Toast.makeText(this, "Preencha todos os campos obrigatórios!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!senha.equals(confirmar)) {
            Toast.makeText(this, "As senhas não coincidem!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (senha.length() < 6) {
            Toast.makeText(this, "A senha deve ter pelo menos 6 caracteres!", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_USER_NOME, nome);
        values.put(DatabaseHelper.COL_USER_EMAIL, email);
        values.put(DatabaseHelper.COL_USER_SENHA, senha);

        long resultado = db.insert(DatabaseHelper.TABLE_USUARIOS, null, values);

        if (resultado != -1) {
            Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Erro: Email já cadastrado!", Toast.LENGTH_SHORT).show();
        }
    }
}
