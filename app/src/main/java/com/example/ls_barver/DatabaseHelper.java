package com.example.ls_barver;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ls_barber.db";
    private static final int DATABASE_VERSION = 17; // Versão atualizada

    // Tabelas Agendamentos
    public static final String TABLE_AGENDAMENTOS = "agendamentos";
    public static final String COL_AG_ID = "id";
    public static final String COL_AG_USER_ID = "usuario_id";
    public static final String COL_AG_SERVICO = "servico";
    public static final String COL_AG_DATA = "data";
    public static final String COL_AG_HORA = "hora";
    public static final String COL_AG_STATUS = "status";

    // Tabelas Usuários
    public static final String TABLE_USUARIOS = "usuarios";
    public static final String COL_USER_ID = "id";
    public static final String COL_USER_NOME = "nome";
    public static final String COL_USER_EMAIL = "email";
    public static final String COL_USER_SENHA = "senha";
    public static final String COL_USER_TIPO = "tipo";

    // Tabelas Avisos
    public static final String TABLE_AVISOS = "avisos";
    public static final String COL_AV_CHAVE = "chave";
    public static final String COL_AV_TEXTO = "texto";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_AGENDAMENTOS + "(" +
                COL_AG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_AG_USER_ID + " INTEGER, " +
                COL_AG_SERVICO + " TEXT, " +
                COL_AG_DATA + " TEXT, " +
                COL_AG_HORA + " TEXT, " +
                COL_AG_STATUS + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_USUARIOS + "(" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER_NOME + " TEXT, " +
                COL_USER_EMAIL + " TEXT UNIQUE, " +
                COL_USER_SENHA + " TEXT, " +
                COL_USER_TIPO + " TEXT DEFAULT 'cliente')");

        db.execSQL("CREATE TABLE " + TABLE_AVISOS + "(" +
                COL_AV_CHAVE + " TEXT PRIMARY KEY, " +
                COL_AV_TEXTO + " TEXT)");
    }
    public void verificarAdmin() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Verifica se o admin já existe
        Cursor cursor = db.query(TABLE_USUARIOS, null, "email=?",
                new String[]{"admin@ls.com"}, null, null, null);



        if (cursor.getCount() == 0) {
            // Se não existir, insere apenas uma vez
            ContentValues values = new ContentValues();
            values.put(COL_USER_NOME, "Administrador");
            values.put(COL_USER_EMAIL, "admin@ls.com");
            values.put(COL_USER_SENHA, "1910");
            values.put(COL_USER_TIPO, "admin");
            db.insert(TABLE_USUARIOS, null, values);
        }
        cursor.close();
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AGENDAMENTOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AVISOS);
        onCreate(db);
    }

    // --- MÉTODOS DE AGENDAMENTO ---
    public List<String> getHorariosOcupados(String data) {
        List<String> horarios = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_AGENDAMENTOS, new String[]{COL_AG_HORA},
                COL_AG_DATA + "=? AND " + COL_AG_STATUS + "=?",
                new String[]{data, "Confirmado"}, null, null, null);

        if (cursor.moveToFirst()) {
            do { horarios.add(cursor.getString(0)); } while (cursor.moveToNext());
        }
        cursor.close();
        return horarios;
    }

    public boolean isHorarioOcupado(String data, String hora) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_AGENDAMENTOS, new String[]{COL_AG_ID},
                COL_AG_DATA + "=? AND " + COL_AG_HORA + "=? AND " + COL_AG_STATUS + "=?",
                new String[]{data, hora, "Confirmado"}, null, null, null);
        boolean existe = (cursor.getCount() > 0);
        cursor.close();
        return existe;
    }

    // --- MÉTODO PARA ADMIN ---
    public void atualizarStatusAgendamento(int id, String novoStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_AG_STATUS, novoStatus);
        db.update(TABLE_AGENDAMENTOS, values, COL_AG_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    // --- MÉTODO PARA AVISOS ---
    public String getAviso(String chave) {
        SQLiteDatabase db = this.getReadableDatabase();
        String texto = "";
        Cursor cursor = db.query(TABLE_AVISOS, new String[]{COL_AV_TEXTO},
                COL_AV_CHAVE + "=?", new String[]{chave}, null, null, null);

        if (cursor.moveToFirst()) {
            texto = cursor.getString(0);
        }
        cursor.close();
        return texto;
    }
}