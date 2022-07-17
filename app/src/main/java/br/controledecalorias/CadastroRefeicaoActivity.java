package br.controledecalorias;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.controledecalorias.entity.Alimento;
import br.controledecalorias.entity.Refeicao;
import br.controledecalorias.persistencia.RefeicaoDatabase;

public class CadastroRefeicaoActivity extends AppCompatActivity {

    public static ArrayList<Alimento> listaAlimentos;
    private static Date dataAnterior;
    private TextView caloriasTotal;
    private TextView quantidadeItens;
    private Button adicionar;
    private Button remover;
    private Button botaoHorario;
    static int hora, minuto;
    private int    modo;
    private Boolean adicionarAoContadorDeCalorias=false;
    private static Double contadorCaloriasDiarias=0d;
    private Refeicao refeicao;
    private Boolean ajustouHorario=false;
    private Boolean alterar=false;

    private Spinner spinnerTipoRefeicao;
    private AlimentoAdapter adapter;
    private EditText editTextApelidoRefeicao;
    public static String REFEICAO = "REFEICAO";
    public static String ALIMENTOS = "ALIMENTOS";
    public static final String MODO    = "MODO";
    public static final int    NOVO    = 1;
    public static final int    ALTERAR    = 2;
    private RadioGroup radioGroupStatusDieta;
    private CheckBox checkboxVerificaRefeicao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_refeicao);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();

        caloriasTotal = findViewById(R.id.textViewTotalCaloriasRefeicao);

        adicionar = findViewById(R.id.botaoAdicionarId);

        remover = findViewById(R.id.botaoRemoverId);

        spinnerTipoRefeicao = findViewById(R.id.spinnerTipoRefeicao);

        radioGroupStatusDieta = findViewById(R.id.radioGroupStatusDieta);

        editTextApelidoRefeicao = findViewById(R.id.editTextApelidoRefeicao);

        checkboxVerificaRefeicao = findViewById(R.id.checkBoxPrimeiraRefeicao);

        botaoHorario = findViewById(R.id.botaoHorario);

        popularSpinner();

        if(bundle!=null){

            modo = bundle.getInt(MODO, NOVO);

            if (modo == NOVO){
                listaAlimentos = new ArrayList<>();
                refeicao = new Refeicao();

                popularAlimentos();

            }else{
                alterar=true;
                refeicao = (Refeicao) bundle.getSerializable(REFEICAO);
                List<Alimento> alimentos = (ArrayList<Alimento>) bundle.getSerializable(ALIMENTOS);
                listaAlimentos = new ArrayList<>(alimentos);

                botaoHorario.setText(String.format(Locale.getDefault(), "%02d:%02d",refeicao.getDataRefeicao().getHours(), refeicao.getDataRefeicao().getMinutes()));
                ajustouHorario=true;
                checkboxVerificaRefeicao.setChecked(refeicao.getPrimeiraRefeicaoDoDia().booleanValue());

                if(refeicao.getStatusDieta().equals(getString(R.string.sim))){
                    radioGroupStatusDieta.check(R.id.radioButtonStatusDietaSim);
                }

                if(refeicao.getStatusDieta().equals(getString(R.string.nao))){
                    radioGroupStatusDieta.check(R.id.radioButtonStatusDietaNao);
                }

                editTextApelidoRefeicao.setText(refeicao.getApelidoRefeicao());

                if(refeicao.getTipoRefeicao().equals(getString(R.string.cafe_da_manha))){
                    spinnerTipoRefeicao.setSelection(0);
                }

                if(refeicao.getTipoRefeicao().equals(getString(R.string.almoco))){
                    spinnerTipoRefeicao.setSelection(1);
                }

                if(refeicao.getTipoRefeicao().equals(getString(R.string.janta))){
                    spinnerTipoRefeicao.setSelection(2);
                }
            }

            adapter = new AlimentoAdapter(getApplicationContext(),
                    R.layout.list_item_xml, listaAlimentos);

            ListView listview = findViewById(R.id.listViewAlimento);
            listview.setAdapter(adapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    Alimento alimento =  (Alimento) listview.getItemAtPosition(position);

                    Toast.makeText(getApplicationContext(), alimento.getNomeAlimento() +getString(R.string.foi_clicado), Toast.LENGTH_SHORT).show();
                }
            });
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cadastro_opcoes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.menuItemSalvar:
                try {
                    cadastrarRefeicao();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;

            case android.R.id.home:
                voltarParaActivityListagem();
                return true;

            case R.id.menuItemLimpar:
                limparValores();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void voltarParaActivityListagem() {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void ajustarHorarioRefeicao(View view)
    {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                hora = selectedHour;
                minuto = selectedMinute;
                botaoHorario.setText(String.format(Locale.getDefault(), "%02d:%02d",hora, minuto));
                ajustouHorario=true;
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, /*style,*/ onTimeSetListener, hora, minuto, true);

        timePickerDialog.setTitle(getString(R.string.selecione_horario));
        timePickerDialog.show();
    }

    public static void novaRefeicao(AppCompatActivity activity){
        Intent intent = new Intent(activity, CadastroRefeicaoActivity.class);

        intent.putExtra(MODO, NOVO);

        activity.startActivityForResult(intent, NOVO);
    }

    public static void alterarRefeicao(AppCompatActivity activity, Refeicao refeicao, List<Alimento> alimentos){
        Intent intent = new Intent(activity, CadastroRefeicaoActivity.class);
        dataAnterior = refeicao.getDataRefeicao();
        intent.putExtra(MODO, ALTERAR);
        intent.putExtra(REFEICAO, (Serializable) refeicao);
        intent.putExtra(ALIMENTOS, (Serializable) alimentos);

        activity.startActivityForResult(intent, ALTERAR);
    }

    public void popularSpinner(){
        ArrayList<String> lista = new ArrayList<>();
        lista.add(getString(R.string.cafe_da_manha));
        lista.add(getString(R.string.almoco));
        lista.add(getString(R.string.janta));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,lista);
        spinnerTipoRefeicao.setAdapter(adapter);
    }

    public void cadastrarRefeicao() throws Exception {

        if(alterar==false){
            refeicao.setDataRefeicao(new Date());
            refeicao.getDataRefeicao().setHours(hora);
            refeicao.getDataRefeicao().setMinutes(minuto);
            refeicao.getDataRefeicao().setSeconds(0);
        }
        else{
            Integer auxHora = hora;
            Integer auxMinuto = minuto;

            refeicao.getDataRefeicao().setHours(auxHora);
            refeicao.getDataRefeicao().setMinutes(auxMinuto);
            refeicao.getDataRefeicao().setSeconds(0);

        }

        refeicao.setAlimentos(listaAlimentos);

        salvarStatusDieta(refeicao);
        salvarTipoRefeicao(refeicao);
        salvarApelidoRefeicao(refeicao);
        salvarStatusPrimeiraRefeicao(refeicao);

        Double contadorCalorias=0d;

        int quantidade=0;
        for(int i=0; i<listaAlimentos.size();i++){
            contadorCalorias = contadorCalorias + listaAlimentos.get(i).getQuantidade()* listaAlimentos.get(i).getCalorias();
            quantidade = quantidade+ listaAlimentos.get(i).getQuantidade();
        }

        if(quantidade==0){
            Toast.makeText(getApplicationContext(), R.string.favor_inserir_alimentos, Toast.LENGTH_SHORT).show();
        }
        else if(refeicao.getApelidoRefeicao().equals("")){
            Toast.makeText(getApplicationContext(), R.string.favor_inserir_apelido, Toast.LENGTH_SHORT).show();
        }
        else if(ajustouHorario==false){
            Toast.makeText(getApplicationContext(), R.string.favor_inserir_horario_refeicao, Toast.LENGTH_SHORT).show();
        }
        else{
            refeicao.setCaloriasRefeicao(contadorCalorias);

            Intent intent = new Intent();
            intent.putExtra(REFEICAO, (Serializable) refeicao);
            intent.putExtra(ALIMENTOS, (Serializable) listaAlimentos);
            setResult(RESULT_OK,intent);
            finish();
        }
    }

    private void salvarStatusPrimeiraRefeicao(Refeicao refeicao) {
        if(checkboxVerificaRefeicao.isChecked()) {
            refeicao.setPrimeiraRefeicaoDoDia(true);
        }
        else {
            refeicao.setPrimeiraRefeicaoDoDia(false);
        }

    }

    private void salvarApelidoRefeicao(Refeicao refeicao) {
        refeicao.setApelidoRefeicao(editTextApelidoRefeicao.getText().toString());
    }

    private void salvarTipoRefeicao(Refeicao refeicao) {
        String tipoDeRefeicao = (String) spinnerTipoRefeicao.getSelectedItem();
        refeicao.setTipoRefeicao(tipoDeRefeicao);
    }

    private void salvarStatusDieta(Refeicao refeicao) throws Exception {
        switch(radioGroupStatusDieta.getCheckedRadioButtonId()){
            case R.id.radioButtonStatusDietaNao:
                refeicao.setStatusDieta(getString(R.string.nao));
                break;

            case R.id.radioButtonStatusDietaSim:
                refeicao.setStatusDieta(getString(R.string.sim));
                break;
            default:
                Toast.makeText(this, R.string.obrigatorio_informar_se_esta_de_dieta,Toast.LENGTH_SHORT).show();
                throw new Exception(String.valueOf(R.string.obrigatorio_informar_se_esta_de_dieta));
        }
    }

    private void limparValores() {

        editTextApelidoRefeicao.setText("");
        radioGroupStatusDieta.clearCheck();
        checkboxVerificaRefeicao.setChecked(false);
        spinnerTipoRefeicao.setSelection(0);

        botaoHorario.setText(getString(R.string.horario));
        ajustouHorario=false;

        for(int i=0; i<listaAlimentos.size();i++){
            listaAlimentos.get(i).setQuantidade(0);
        }
        adapter.notifyDataSetChanged();
        Toast.makeText(getApplicationContext(), R.string.limpou_valores, Toast.LENGTH_SHORT).show();

    }
    private void popularAlimentos(){

        Alimento alimento1 = new Alimento();
        alimento1.setQuantidade(0);
        alimento1.setNomeAlimento(getString(R.string.pao));
        alimento1.setTipoDeAlimento(getString(R.string.carboidratos));
        alimento1.setCalorias(265d);
        alimento1.setIdImagemAlimento(R.drawable.pao);

        Alimento alimento2 = new Alimento();
        alimento2.setQuantidade(0);
        alimento2.setNomeAlimento(getString(R.string.arroz));
        alimento2.setTipoDeAlimento(getString(R.string.carboidratos));
        alimento2.setCalorias(44d);
        alimento2.setIdImagemAlimento(R.drawable.arroz);

        Alimento alimento3 = new Alimento();
        alimento3.setQuantidade(0);
        alimento3.setNomeAlimento(getString(R.string.feijao));
        alimento3.setTipoDeAlimento(getString(R.string.leguminosas_e_oleaginosas));
        alimento3.setCalorias(70d);
        alimento3.setIdImagemAlimento(R.drawable.feijao);

        Alimento alimento4 = new Alimento();
        alimento4.setQuantidade(0);
        alimento4.setNomeAlimento(getString(R.string.azeite));
        alimento4.setTipoDeAlimento(getString(R.string.leguminosas_e_oleaginosas));
        alimento4.setCalorias(119d);
        alimento4.setIdImagemAlimento(R.drawable.azeite);

        Alimento alimento5 = new Alimento();
        alimento5.setQuantidade(0);
        alimento5.setNomeAlimento(getString(R.string.cenoura));
        alimento5.setTipoDeAlimento(getString(R.string.verduras_e_legumes));
        alimento5.setCalorias(77d);
        alimento5.setIdImagemAlimento(R.drawable.cenoura);

        Alimento alimento6 = new Alimento();
        alimento6.setQuantidade(0);
        alimento6.setNomeAlimento(getString(R.string.cerveja));
        alimento6.setTipoDeAlimento(getString(R.string.carboidratos));
        alimento6.setCalorias(148d);
        alimento6.setIdImagemAlimento(R.drawable.cerveja);

        Alimento alimento7 = new Alimento();
        alimento7.setQuantidade(0);
        alimento7.setNomeAlimento(getString(R.string.chocolate));
        alimento7.setTipoDeAlimento(getString(R.string.acucares_e_doces));
        alimento7.setCalorias(500d);
        alimento7.setIdImagemAlimento(R.drawable.chocolate);

        Alimento alimento8 = new Alimento();
        alimento8.setQuantidade(0);
        alimento8.setNomeAlimento(getString(R.string.refrigerante));
        alimento8.setTipoDeAlimento(getString(R.string.acucares_e_doces));
        alimento8.setCalorias(147d);
        alimento8.setIdImagemAlimento(R.drawable.refrigerante);

        Alimento alimento9 = new Alimento();
        alimento9.setQuantidade(0);
        alimento9.setNomeAlimento(getString(R.string.tilapia));
        alimento9.setTipoDeAlimento(getString(R.string.carnes_e_ovos));
        alimento9.setCalorias(111d);
        alimento9.setIdImagemAlimento(R.drawable.tilapia);

        Alimento alimento10 = new Alimento();
        alimento10.setQuantidade(0);
        alimento10.setNomeAlimento(getString(R.string.tomate));
        alimento10.setTipoDeAlimento(getString(R.string.verduras_e_legumes));
        alimento10.setCalorias(11d);
        alimento10.setIdImagemAlimento(R.drawable.tomate);

        listaAlimentos.add(alimento1);
        listaAlimentos.add(alimento2);
        listaAlimentos.add(alimento3);
        listaAlimentos.add(alimento4);
        listaAlimentos.add(alimento5);
        listaAlimentos.add(alimento6);
        listaAlimentos.add(alimento7);
        listaAlimentos.add(alimento8);
        listaAlimentos.add(alimento9);
        listaAlimentos.add(alimento10);

    }

    @Override
    public void onBackPressed() {
        voltarParaActivityListagem();
    }
}