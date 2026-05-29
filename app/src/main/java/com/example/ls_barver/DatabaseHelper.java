package com.example.ls_barver;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ls_barber.db";
    private static final int DATABASE_VERSION = 3;

    public static final String TABLE_USUARIOS = "usuarios";
    public static final String COL_USER_ID = "id";
    public static final String COL_USER_NOME = "nome";
    public static final String COL_USER_EMAIL = "email";
    public static final String COL_USER_TELEFONE = "telefone";
    public static final String COL_USER_SENHA = "senha";
    public static final String COL_USER_TIPO = "tipo_usuario";

    public static final String TABLE_AGENDAMENTOS = "agendamentos";
    public static final String COL_AG_ID = "id";
    public static final String COL_AG_USER_ID = "usuario_id";
    public static final String COL_AG_SERVICO = "servico";
    public static final String COL_AG_DATA = "data";
    public static final String COL_AG_HORA = "hora";
    public static final String COL_AG_STATUS = "status";

    public static final String TABLE_AVISOS = "avisos";
    public static final String COL_AV_ID = "id";
    public static final String COL_AV_CHAVE = "chave";
    public static final String COL_AV_TEXTO = "texto";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USUARIOS + " (" + COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_USER_NOME + " TEXT, " + COL_USER_EMAIL + " TEXT UNIQUE, " + COL_USER_TELEFONE + " TEXT, " + COL_USER_SENHA + " TEXT, " + COL_USER_TIPO + " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_AGENDAMENTOS + " (" + COL_AG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_AG_USER_ID + " INTEGER, " + COL_AG_SERVICO + " TEXT, " + COL_AG_DATA + " TEXT, " + COL_AG_HORA + " TEXT, " + COL_AG_STATUS + " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_AVISOS + " (" + COL_AV_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_AV_CHAVE + " TEXT UNIQUE, " + COL_AV_TEXTO + " TEXT)");

        // Inserir Admin inicial
        ContentValues admin = new ContentValues();
        admin.put(COL_USER_NOME, "Admin");
        admin.put(COL_USER_EMAIL, "admin@ls.com");
        admin.put(COL_USER_TELEFONE, "00000000000");
        admin.put(COL_USER_SENHA, "123");
        admin.put(COL_USER_TIPO, "admin");
        db.insert(TABLE_USUARIOS, null, admin);

        // Inserir avisos padrão
        inserirAviso(db, "chave_feriado", "Feriado: Aberto");
        inserirAviso(db, "chave_promocao", "Promoção: 10%");
        inserirAviso(db, "chave_horarios", "Horário: 09h às 18h");
    }

    private void inserirAviso(SQLiteDatabase db, String chave, String texto) {
        ContentValues values = new ContentValues();
        values.put(COL_AV_CHAVE, chave);
        values.put(COL_AV_TEXTO, texto);
        db.insert(TABLE_AVISOS, null, values);
    }

    // NOVO MÉTODO: Essencial para a activity_avisos ler o banco
    public String getAviso(String chave) {
        SQLiteDatabase db = this.getReadableDatabase();
        String texto = "";
        Cursor cursor = db.query(TABLE_AVISOS, new String[]{COL_AV_TEXTO},
                COL_AV_CHAVE + "=?", new String[]{chave}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            texto = cursor.getString(0);
            cursor.close();
        }
        db.close();
        return texto;
    }

    public void atualizarStatusAgendamento(int id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_AG_STATUS, status);
        db.update(TABLE_AGENDAMENTOS, values, COL_AG_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void atualizarAviso(String chave, String novoTexto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_AV_TEXTO, novoTexto);
        db.update(TABLE_AVISOS, values, COL_AV_CHAVE + "=?", new String[]{chave});
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AVISOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AGENDAMENTOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS);
        onCreate(db);
    }
}