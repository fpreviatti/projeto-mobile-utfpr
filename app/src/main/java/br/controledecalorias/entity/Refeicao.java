package br.controledecalorias.entity;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Relation;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import br.controledecalorias.ListagemDeRefeicoesActivity;
import br.controledecalorias.persistencia.DateConverter;

@Entity
public class Refeicao implements Serializable{

    @PrimaryKey(autoGenerate = true)
    private int id;

    @Ignore
    private List<Alimento> alimentos;

    @TypeConverters(DateConverter.class)
    private Date dataRefeicao;

    private Double caloriasRefeicao;
    private String tipoRefeicao;
    private String statusDieta;
    private String apelidoRefeicao;
    private Boolean primeiraRefeicaoDoDia;

    public Refeicao(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getPrimeiraRefeicaoDoDia() {
        return primeiraRefeicaoDoDia;
    }

    public void setPrimeiraRefeicaoDoDia(Boolean primeiraRefeicaoDoDia) {
        this.primeiraRefeicaoDoDia = primeiraRefeicaoDoDia;
    }

    public String getApelidoRefeicao() {
        return apelidoRefeicao;
    }

    public void setApelidoRefeicao(String apelidoRefeicao) {
        this.apelidoRefeicao = apelidoRefeicao;
    }

    public Double getCaloriasRefeicao() {
        return caloriasRefeicao;
    }

    public void setCaloriasRefeicao(Double caloriasRefeicao) {
        this.caloriasRefeicao = caloriasRefeicao;
    }

    public String getStatusDieta() {
        return statusDieta;
    }

    public void setStatusDieta(String statusDieta) {
        this.statusDieta = statusDieta;
    }

    public String getTipoRefeicao() {
        return tipoRefeicao;
    }

    public void setTipoRefeicao(String tipoRefeicao) {
        this.tipoRefeicao = tipoRefeicao;
    }

    public List<Alimento> getAlimentos() {
        return alimentos;
    }

    public void setAlimentos(List<Alimento> alimentos) {
        this.alimentos = alimentos;
    }

    public Date getDataRefeicao() {
        return dataRefeicao;
    }

    public void setDataRefeicao(Date dataRefeicao) {
        this.dataRefeicao = dataRefeicao;
    }
}
