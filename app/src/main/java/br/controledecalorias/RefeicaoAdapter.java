package br.controledecalorias;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.controledecalorias.entity.Refeicao;

public class RefeicaoAdapter extends ArrayAdapter<Refeicao> {
    private List<Refeicao> refeicoes;
    private TextView textViewHorarioRefeicao;
    private TextView textViewCaloriasRefeicao;
    private TextView apelidoRefeicao;

    public RefeicaoAdapter(@NonNull Context context, int resource, List<Refeicao> refeicoes){
        super(context,resource,refeicoes);
        this.refeicoes = refeicoes;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        int phraseIndex = position;
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_refeicoes_xml,
                    parent,false);
        }

        String horarioRefeicao = new SimpleDateFormat("HH:mm").format(refeicoes.get(position).getDataRefeicao());

        textViewHorarioRefeicao = convertView.findViewById(R.id.textViewRorarioRefeicao);
        textViewCaloriasRefeicao = convertView.findViewById(R.id.textViewTotalCaloriasRefeicao);
        apelidoRefeicao = convertView.findViewById(R.id.refeicaoTextview);

        textViewHorarioRefeicao.setText(horarioRefeicao);
        textViewCaloriasRefeicao.setText(refeicoes.get(position).getCaloriasRefeicao().toString());
        apelidoRefeicao.setText(refeicoes.get(position).getApelidoRefeicao().toString());

        return convertView;

    }

}

