package br.controledecalorias;

import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.controledecalorias.entity.Alimento;
import br.controledecalorias.entity.Refeicao;
import br.controledecalorias.persistencia.RefeicaoDatabase;
import br.controledecalorias.utils.UtilsGUI;

public class ListagemDeRefeicoesActivity extends AppCompatActivity {

    private ListView listView;
    private List<Refeicao>listaRefeicoes;

    private Refeicao refeicao;
    private Integer controleLista=0;
    private TextView textViewCaloriasDiariasTotal;
    public RefeicaoAdapter adapter;
    private TextView apelidoRefeicao;
    private TextView dataDeHoje;
    public static final String MODO    = "MODO";
    public static final String RESULT    = "RESULT";
    public static final int    NOVO    = 1;
    public static final int    ALTERAR = 2;
    private int    modo;
    public static String REFEICAO = "REFEICAO";
    public static String ALIMENTOS = "ALIMENTOS";
    private View viewSelecionada;
    private ActionMode actionMode;
    private int        posicaoSelecionada = -1;
    private static final String ARQUIVO =
            "br.controledecalorias.sharedpreferences.PREFERENCIAS_TEMA";
    private int opcao = AppCompatDelegate.MODE_NIGHT_NO;
    int corPadrao;
    private static final String TEMA = "TEMA";
    private Boolean primeiraVezCarregado=false;
    Double contadorCaloriasDiarias=0d;
    List<Alimento> alimentosRefeicao;
    String dataAtual;

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {

            MenuInflater inflate = mode.getMenuInflater();
            inflate.inflate(R.menu.listagem_refeicao_selecionada, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            switch(item.getItemId()){
                case R.id.menuItemEditar:
                    alterarRefeicao();
                    mode.finish();
                    return true;

                case R.id.menuItemExcluir:
                    excluirRefeicao(posicaoSelecionada);
                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

            if (viewSelecionada != null){
                viewSelecionada.setBackgroundColor(Color.TRANSPARENT);
            }
            actionMode         = null;
            viewSelecionada    = null;
            listView.setEnabled(true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_refeicoes);

        refeicao = new Refeicao();

        listView = findViewById(R.id.listViewRefeicoes);

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent,
                                            View view,
                                            int position,
                                            long id) {

                        posicaoSelecionada = position;

                    }
                });

        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listView.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {

                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent,
                                                   View view,
                                                   int position,
                                                   long id) {

                        if (actionMode != null){
                            return false;
                        }

                        posicaoSelecionada = position;

                        view.setBackgroundColor(Color.LTGRAY);

                        viewSelecionada = view;

                        listView.setEnabled(false);

                        actionMode = startSupportActionMode(mActionModeCallback);

                        return true;
                    }
                });

        textViewCaloriasDiariasTotal = findViewById(R.id.textViewTotalDeCaloriasDiarias);

        dataDeHoje = findViewById(R.id.textViewDataDeHoje);

        corPadrao = textViewCaloriasDiariasTotal.getCurrentTextColor();

        apelidoRefeicao = findViewById(R.id.refeicaoTextview);

        registerForContextMenu(listView);

        setarDataAtual();

        lerPreferenciaTema();

        carregarRefeicoes();

    }

    private void setarDataAtual() {
        dataAtual = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        dataDeHoje.setText(dataAtual);
    }

    private void carregarRefeicoes() {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                RefeicaoDatabase database = RefeicaoDatabase.getDatabase(ListagemDeRefeicoesActivity.this);

                listaRefeicoes = database.refeicaoDao().queryAll();

                        ListagemDeRefeicoesActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new RefeicaoAdapter(getApplicationContext(),
                                R.layout.list_refeicoes_xml, listaRefeicoes);

                        listView.setAdapter(adapter);
                        setarValoresCaloriasDiarias(listaRefeicoes);
                    }
                });
            }
        });
    }

    public void setarValoresCaloriasDiarias(List<Refeicao> lista){
        contadorCaloriasDiarias=0d;
        for(int i=0; i<listaRefeicoes.size();i++){
            contadorCaloriasDiarias = contadorCaloriasDiarias +listaRefeicoes.get(i).getCaloriasRefeicao();
        }

        textViewCaloriasDiariasTotal.setText( String.format("%.0f", contadorCaloriasDiarias));

        if(contadorCaloriasDiarias>2500d){
            textViewCaloriasDiariasTotal.setTextColor(Color.parseColor(getString(R.string.cor_vermelha)));
        }

        if(contadorCaloriasDiarias<=2500d){
            textViewCaloriasDiariasTotal.setTextColor(corPadrao);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.principal_opcoes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        item.setChecked(true);
        switch(item.getItemId()){

            case R.id.menuItemAdicionar:
                chamarActivityCadastro();
                return true;

            case R.id.menuItemSobre:
                chamarActivitySobre();
                return true;

            case R.id.menuItemModoNoturno:
                salvarPreferenciaTema(AppCompatDelegate.MODE_NIGHT_YES);
                alterarTema(opcao);
                return true;

            case R.id.menuItemModoClassico:
                salvarPreferenciaTema(AppCompatDelegate.MODE_NIGHT_NO);
                alterarTema(opcao);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem item;

        switch(opcao){

            case AppCompatDelegate.MODE_NIGHT_YES:
                item = menu.findItem(R.id.menuItemModoNoturno);
                break;

            case AppCompatDelegate.MODE_NIGHT_NO:
                item = menu.findItem(R.id.menuItemModoClassico);
                break;

            default:
                return super.onPrepareOptionsMenu(menu);
        }

        item.setChecked(true);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {

        if (resultCode == RESULT_OK) {

            Bundle bundle = data.getExtras();

            if(bundle!=null){

                if (requestCode == ALTERAR){

                    refeicao = (Refeicao) bundle.getSerializable(REFEICAO);

                    alimentosRefeicao = (ArrayList<Alimento>) bundle.getSerializable(ALIMENTOS);

                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            RefeicaoDatabase database = RefeicaoDatabase.getDatabase(ListagemDeRefeicoesActivity.this);

                            database.refeicaoDao().update(alimentosRefeicao);
                            database.refeicaoDao().update(refeicao);

                        }
                    });
                    carregarRefeicoes();
                    posicaoSelecionada = -1;
                }

                else{

                    if(bundle.getSerializable(REFEICAO)!=null){

                        refeicao = (Refeicao) bundle.getSerializable(REFEICAO);
                        inserirRefeicao(refeicao);
                    }
                }
            }

        }
    }

    public void chamarActivityCadastro(){
        CadastroRefeicaoActivity.novaRefeicao(this);
    }

    private void chamarActivitySobre(){
        SobreActivity.sobre(this);
    }

    private void excluirRefeicao(int posicao){

        String mensagem = getString(R.string.deseja_realmente_apagar_esta_refeicao)
                + "\n";

        RefeicaoDatabase database = RefeicaoDatabase.getDatabase(ListagemDeRefeicoesActivity.this);

        DialogInterface.OnClickListener listener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch(which){
                            case DialogInterface.BUTTON_POSITIVE:

                                AsyncTask.execute(new Runnable() {
                                    @Override
                                    public void run() {

                                        Refeicao refeicao = database.refeicaoDao().queryForId(listaRefeicoes.get(posicao).getId());

                                        contadorCaloriasDiarias = contadorCaloriasDiarias-listaRefeicoes.get(posicao).getCaloriasRefeicao();

                                        database.refeicaoDao().delete(refeicao);

                                        ListagemDeRefeicoesActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                adapter.remove(refeicao);
                                                carregarRefeicoes();
                                            }
                                        });

                                    }

                                });

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:

                                break;
                        }
                    }
                };

        UtilsGUI.confirmaAcao(this, mensagem, listener);

    }

    private void inserirRefeicao(Refeicao refeicao){

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                RefeicaoDatabase database = RefeicaoDatabase.getDatabase(ListagemDeRefeicoesActivity.this);

                List<Alimento> alimentosRefeicao = refeicao.getAlimentos();

                long id = database.refeicaoDao().insert(refeicao);

                for(int i=0;i<alimentosRefeicao.size();i++){
                    alimentosRefeicao.get(i).setRefeicaoId(((int)id));
                }

                database.refeicaoDao().insert(alimentosRefeicao);

                listaRefeicoes.add(refeicao);

                ListagemDeRefeicoesActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        contadorCaloriasDiarias = contadorCaloriasDiarias+refeicao.getCaloriasRefeicao();

                        textViewCaloriasDiariasTotal.setText( String.format("%.0f", contadorCaloriasDiarias));

                        if(contadorCaloriasDiarias>2500d){
                            textViewCaloriasDiariasTotal.setTextColor(Color.parseColor(getString(R.string.cor_vermelha)));
                        }
                        carregarRefeicoes();
                    }
                });
            }
        });

    }

    private void alterarRefeicao() {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                refeicao = listaRefeicoes.get(posicaoSelecionada);

                RefeicaoDatabase database = RefeicaoDatabase.getDatabase(ListagemDeRefeicoesActivity.this);

                alimentosRefeicao = database.refeicaoDao().queryAllRefeicoesComAlimentos(refeicao.getId());
                chamarActivityCadastroParaAlterar(refeicao,alimentosRefeicao);
            }
        });
    }

    public void chamarActivityCadastroParaAlterar(Refeicao refeicao, List<Alimento> alimentos){
        CadastroRefeicaoActivity.alterarRefeicao(this,refeicao,alimentos);
    }

    public void alterarTema(int codigo){

        if(codigo!=AppCompatDelegate.getDefaultNightMode()){
            AppCompatDelegate.setDefaultNightMode(codigo);
            TaskStackBuilder.create(this)
                    .addNextIntent(new Intent(this, ListagemDeRefeicoesActivity.class))
                    .addNextIntent(this.getIntent())
                    .startActivities();
        }

    }

    private void lerPreferenciaTema(){

        SharedPreferences shared = getSharedPreferences(ARQUIVO,
                Context.MODE_PRIVATE);

        opcao = shared.getInt(TEMA, opcao);

        alterarTema(opcao);

    }

    private void salvarPreferenciaTema(int novoValor){

        SharedPreferences shared = getSharedPreferences(ARQUIVO,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = shared.edit();

        editor.putInt(TEMA, novoValor);

        editor.commit();

        opcao = novoValor;

    }

}