package br.controledecalorias.entity;

import static android.arch.persistence.room.ForeignKey.CASCADE;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(foreignKeys = {@ForeignKey(entity = Refeicao.class,
        parentColumns = "id",
        childColumns = "refeicaoId",onDelete = CASCADE)})
public class Alimento implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String nomeAlimento;
    private Double calorias;
    private Double gramas;
    private String tipoDeAlimento;
    private int idImagemAlimento;
    private Integer quantidade;

    @ColumnInfo(index = true)
    private int refeicaoId;

    public Alimento(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRefeicaoId() {
        return refeicaoId;
    }

    public void setRefeicaoId(int refeicaoId) {
        this.refeicaoId = refeicaoId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Alimento(Integer id, String nomeAlimento, Double calorias, String tipoDeAlimento, int idImagemAlimento, Double gramas) {
        this.id = id;
        this.nomeAlimento = nomeAlimento;
        this.calorias = calorias;
        this.tipoDeAlimento = tipoDeAlimento;
        this.idImagemAlimento = idImagemAlimento;
        this.gramas = gramas;
    }

    public Double getGramas() {
        return gramas;
    }

    public void setGramas(Double gramas) {
        this.gramas = gramas;
    }

    public int getIdImagemAlimento() {
        return idImagemAlimento;
    }

    public void setIdImagemAlimento(int idImagemAlimento) {
        this.idImagemAlimento = idImagemAlimento;
    }


    public String getNomeAlimento() {
        return nomeAlimento;
    }

    public void setNomeAlimento(String nomeAlimento) {
        this.nomeAlimento = nomeAlimento;
    }

    public Double getCalorias() {
        return calorias;
    }

    public void setCalorias(Double calorias) {
        this.calorias = calorias;
    }

    public String getTipoDeAlimento() {
        return tipoDeAlimento;
    }

    public void setTipoDeAlimento(String tipoDeAlimento) {
        this.tipoDeAlimento = tipoDeAlimento;
    }
}
