package br.controledecalorias.persistencia;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import java.util.Date;
import java.util.List;

import br.controledecalorias.entity.Alimento;
import br.controledecalorias.entity.Refeicao;

@Dao
public interface RefeicaoDao {

    @Insert
    long insert(Refeicao refeicao);

    @Insert
    void insert(List<Alimento> alimentos);

    @Delete
    void delete(Refeicao refeicao);

    @Update
    void update(Refeicao refeicao);

    @Update
    void update(List<Alimento> alimentos);

    @Query("SELECT * FROM refeicao WHERE id = :id")
    Refeicao queryForId(long id);

    @Query("SELECT * FROM Refeicao where strftime('%Y %m %d', dataRefeicao / 1000, 'unixepoch','localtime','start of day') = strftime('%Y %m %d', date('now','localtime')) order by dataRefeicao DESC;")
    List<Refeicao> queryAll();

    @Query("SELECT * FROM Alimento where refeicaoId = :id")
    List<Alimento> queryAllRefeicoesComAlimentos(long id);

    @Query("SELECT date('now','localtime')")
    String dataAtual();

}
