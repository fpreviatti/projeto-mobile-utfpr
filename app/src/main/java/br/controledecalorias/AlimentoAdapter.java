package br.controledecalorias;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import br.controledecalorias.entity.Alimento;

public class AlimentoAdapter extends ArrayAdapter<Alimento> {
    private ArrayList<Alimento> alimentos;

    public AlimentoAdapter(@NonNull Context context, int resource, ArrayList<Alimento> alimentos){
        super(context,resource,alimentos);
        this.alimentos = alimentos;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        int phraseIndex = position;
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_xml,
                    parent,false);
        }

        ImageView alimentoImage = convertView.findViewById(R.id.alimento_imageview);

        TextView tipoAlimentoTextView = convertView.findViewById(R.id.refeicaoTextview);

        TextView alimentoTextView = convertView.findViewById(R.id.textViewHorario);

        TextView quantidadeItensTextView = convertView.findViewById(R.id.textViewQuantidadeItensId);

        TextView caloriasTotal = convertView.findViewById(R.id.textViewTotalCaloriasRefeicao);

        TextView metricaParaCadaAlimento = convertView.findViewById(R.id.textViewMetricaQuantidade);

        Button botaoAdicionar = convertView.findViewById(R.id.botaoAdicionarId);
        Button botaoRemover = convertView.findViewById(R.id.botaoRemoverId);

        if(alimentos.get(position).getNomeAlimento().equals(convertView.getContext().getString(R.string.arroz))){
            metricaParaCadaAlimento.setText(R.string.colheres_de_sopa);
        }

        if(alimentos.get(position).getNomeAlimento().equals(convertView.getContext().getString(R.string.cerveja))){
            metricaParaCadaAlimento.setText(R.string.lata_350ml);
        }

        if(alimentos.get(position).getNomeAlimento().equals(convertView.getContext().getString(R.string.azeite))){
            metricaParaCadaAlimento.setText(R.string.colheres_de_sopa);
        }

        if(alimentos.get(position).getNomeAlimento().equals(convertView.getContext().getString(R.string.pao))){
            metricaParaCadaAlimento.setText(R.string.unidade);
        }

        if(alimentos.get(position).getNomeAlimento().equals(convertView.getContext().getString(R.string.feijao))){
            metricaParaCadaAlimento.setText(R.string.conchas);
        }

        if(alimentos.get(position).getNomeAlimento().equals(convertView.getContext().getString(R.string.cenoura))){
            metricaParaCadaAlimento.setText(R.string.unidade);
        }

        if(alimentos.get(position).getNomeAlimento().equals(convertView.getContext().getString(R.string.refrigerante))){
            metricaParaCadaAlimento.setText(R.string.lata_350ml);
        }

        if(alimentos.get(position).getNomeAlimento().equals(convertView.getContext().getString(R.string.chocolate))){
            metricaParaCadaAlimento.setText(R.string.barra_90g);
        }

        if(alimentos.get(position).getNomeAlimento().equals(convertView.getContext().getString(R.string.tomate))){
            metricaParaCadaAlimento.setText(R.string.unidade);
        }

        if(alimentos.get(position).getNomeAlimento().equals(convertView.getContext().getString(R.string.tilapia))){
            metricaParaCadaAlimento.setText(R.string.file_115g_cada);
        }

        botaoAdicionar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // do something
                alimentos.get(position).setQuantidade(alimentos.get(position).getQuantidade()+1);
                quantidadeItensTextView.setText(alimentos.get(position).getQuantidade().toString());


            }
        });

        botaoRemover.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // do something
                if(alimentos.get(position).getQuantidade()==0){
                    alimentos.get(position).setQuantidade(0);
                }
                else{
                    alimentos.get(position).setQuantidade(alimentos.get(position).getQuantidade()-1);
                }
                quantidadeItensTextView.setText(alimentos.get(position).getQuantidade().toString());
            }
        });



        alimentoImage.setImageResource(alimentos.get(position).getIdImagemAlimento());
        alimentoTextView.setText(alimentos.get(position).getNomeAlimento());
        tipoAlimentoTextView.setText(alimentos.get(position).getTipoDeAlimento());
        caloriasTotal.setText(alimentos.get(position).getCalorias().toString());
        quantidadeItensTextView.setText(alimentos.get(position).getQuantidade().toString());

        return convertView;

    }

}

