package br.controledecalorias.persistencia;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import br.controledecalorias.entity.Alimento;
import br.controledecalorias.entity.Refeicao;

@Database(entities = {Refeicao.class, Alimento.class}, version = 1, exportSchema = false)
public abstract class RefeicaoDatabase extends RoomDatabase {

    public abstract RefeicaoDao refeicaoDao();

    private static RefeicaoDatabase instance;

    public static RefeicaoDatabase getDatabase(final Context context) {

        if (instance == null) {

            synchronized (RefeicaoDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context,
                            RefeicaoDatabase.class,
                            "refeicoes.db").allowMainThreadQueries().build();
                }
            }
        }
        return instance;
    }
}
