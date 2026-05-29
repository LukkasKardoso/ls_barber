package com.example.ls_barver;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class activity_esqueceu_senha extends AppCompatActivity {

    // Componentes da tela (O etEmail foi adicionado aqui)
    private EditText etEmail, etNovaSenha, etConfirmarSenha;
    private Button btnRedefinir;
    private TextView tvVoltar;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueceu_senha);

        dbHelper = new DatabaseHelper(this);

        // Vinculação de todos os IDs com o XML atualizado (incluindo o e-mail)
        etEmail = findViewById(R.id.et_email);
        etNovaSenha = findViewById(R.id.et_nova_senha);
        etConfirmarSenha = findViewById(R.id.et_confirmar_senha);
        btnRedefinir = findViewById(R.id.btn_salvar_nova_senha);
        tvVoltar = findViewById(R.id.tv_voltar_login);

        // Configuração dos cliques
        btnRedefinir.setOnClickListener(v -> redefinirSenha());
        tvVoltar.setOnClickListener(v -> finish());
    }

    private void redefinirSenha() {
        // Captura os textos digitados pelo usuário
        String email = etEmail.getText().toString().trim();
        String novaSenha = etNovaSenha.getText().toString().trim();
        String confirmar = etConfirmarSenha.getText().toString().trim();

        // Validação se algum campo foi deixado em branco
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(novaSenha) || TextUtils.isEmpty(confirmar)) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validação se as senhas batem
        if (!novaSenha.equals(confirmar)) {
            Toast.makeText(this, "As senhas não coincidem!", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Verificar se o e-mail informado existe no banco de dados
        Cursor cursor = db.query(DatabaseHelper.TABLE_USUARIOS,
                new String[]{DatabaseHelper.COL_USER_ID},
                DatabaseHelper.COL_USER_EMAIL + "=?",
                new String[]{email}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            cursor.close(); // Fecha o cursor inicial antes de proceder com a alteração

            // Monta os novos dados para atualizar a tabela
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COL_USER_SENHA, novaSenha);

            // Executa o comando UPDATE filtrando pelo e-mail
            db.update(DatabaseHelper.TABLE_USUARIOS, values,
                    DatabaseHelper.COL_USER_EMAIL + "=?", new String[]{email});

            Toast.makeText(this, "Senha redefinida com sucesso!", Toast.LENGTH_SHORT).show();
            finish(); // Fecha a tela e volta para o Login
        } else {
            Toast.makeText(this, "Email não encontrado!", Toast.LENGTH_SHORT).show();
        }

        // Evita vazamentos de memória fechando o cursor de forma segura se ele ainda estiver aberto
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }
}