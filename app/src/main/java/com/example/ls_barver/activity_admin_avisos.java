package com.example.ls_barver;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class activity_admin_avisos extends AppCompatActivity {

    private EditText etAvisoFeriado, etAvisoPromocao, etAvisoHorarios;
    private Button btnPublicarAvisos;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_avisos);

        dbHelper = new DatabaseHelper(this);

        etAvisoFeriado = findViewById(R.id.et_aviso_feriado);
        etAvisoPromocao = findViewById(R.id.et_aviso_promocao);
        etAvisoHorarios = findViewById(R.id.et_aviso_horarios);
        btnPublicarAvisos = findViewById(R.id.btn_publicar_avisos);

        carregarAvisosAtuais();

        btnPublicarAvisos.setOnClickListener(v -> publicarNovosAvisos());
    }

    private void carregarAvisosAtuais() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        etAvisoFeriado.setText(buscarTextoAviso(db, "chave_feriado"));
        etAvisoPromocao.setText(buscarTextoAviso(db, "chave_promocao"));
        etAvisoHorarios.setText(buscarTextoAviso(db, "chave_horarios"));
        db.close();
    }

    private String buscarTextoAviso(SQLiteDatabase db, String chave) {
        String texto = "";
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_AVISOS,
                new String[]{DatabaseHelper.COL_AV_TEXTO},
                DatabaseHelper.COL_AV_CHAVE + "=?",
                new String[]{chave},
                null, null, null
        );

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                texto = cursor.getString(0);
            }
            cursor.close();
        }
        return texto;
    }

    private void publicarNovosAvisos() {
        String textoFeriado = etAvisoFeriado.getText().toString().trim();
        String textoPromocao = etAvisoPromocao.getText().toString().trim();
        String textoHorarios = etAvisoHorarios.getText().toString().trim();

        if (TextUtils.isEmpty(textoFeriado) || TextUtils.isEmpty(textoPromocao) || TextUtils.isEmpty(textoHorarios)) {
            Toast.makeText(this, "Nenhum campo pode estar vazio!", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        atualizarAvisoNoBanco(db, "chave_feriado", textoFeriado);
        atualizarAvisoNoBanco(db, "chave_promocao", textoPromocao);
        atualizarAvisoNoBanco(db, "chave_horarios", textoHorarios);
        db.close();

        Toast.makeText(this, "Avisos atualizados com sucesso!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void atualizarAvisoNoBanco(SQLiteDatabase db, String chave, String novoTexto) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_AV_TEXTO, novoTexto);

        db.update(
                DatabaseHelper.TABLE_AVISOS,
                values,
                DatabaseHelper.COL_AV_CHAVE + "=?",
                new String[]{chave}
        );
    }
}