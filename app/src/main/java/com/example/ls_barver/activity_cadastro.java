package com.example.ls_barver;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class activity_cadastro extends AppCompatActivity {

    private EditText btNome, btEmailCadastro, btSenhaCadastro, btConfirmarSn;
    private Button btCadastro;
    private TextView btVoltar;
    private DatabaseHelper dbHelper;

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

        // Validação de campos vazios
        if (TextUtils.isEmpty(nome) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(senha) || TextUtils.isEmpty(confirmar)) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validação de senha
        if (!senha.equals(confirmar)) {
            Toast.makeText(this, "As senhas não coincidem!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (senha.length() < 6) {
            Toast.makeText(this, "A senha deve ter pelo menos 6 caracteres!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Inserção no banco
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_USER_NOME, nome);
        values.put(DatabaseHelper.COL_USER_EMAIL, email);
        values.put(DatabaseHelper.COL_USER_SENHA, senha);

        try {
            long resultado = db.insert(DatabaseHelper.TABLE_USUARIOS, null, values);

            if (resultado != -1) {
                Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Erro: Este e-mail já está cadastrado.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao cadastrar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            db.close(); // Fecha o banco sempre
        }
    }
}