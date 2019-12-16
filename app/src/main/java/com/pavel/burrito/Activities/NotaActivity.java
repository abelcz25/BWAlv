package com.pavel.burrito.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.pavel.burrito.Adaptador.AdaptadorRe;
import com.pavel.burrito.Lista.Nota;
import com.pavel.burrito.Lista.NoteList;
import com.pavel.burrito.R;

import java.util.Arrays;

import static java.lang.System.gc;

public class NotaActivity extends AppCompatActivity {

    private NoteList notas;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AdaptadorRe adaptador;
    private FloatingActionButton fabAdd, fabOrder, fabSearch;
    private EditText edtTitulo, edtNota, edtBusqueda;
    private int count = 0, temp;
    Nota arreglo[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota);

        Toast.makeText(NotaActivity.this, "Welcome!! To burrito assistant", Toast.LENGTH_SHORT).show();

        //Edit Text
        edtNota = findViewById(R.id.edtNota);
        edtTitulo = findViewById(R.id.edtTitulo);
        edtBusqueda = findViewById(R.id.busqueda);

        //FAB
        fabAdd = findViewById(R.id.fabAdd);
        fabOrder = findViewById(R.id.fabOrder);
        fabSearch = findViewById(R.id.fabSearch);
        fabSearch.setEnabled(false);
        //ColorStateList csl = new ColorStateList(new int[][]{new int[0]}, new int[]{Color});

        //Instanciamos lista de notas
        notas = new NoteList();

        recyclerView = findViewById(R.id.recyclerView); //Se obtiene el view desde lek layout
        layoutManager = new LinearLayoutManager(this);

        callAdapter();

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //Boton de agregar notas
                String titulo = edtTitulo.getText().toString();
                String nota = edtNota.getText().toString();
                if (!titulo.equals("") && !nota.equals("")) {
                    notas.add(new Nota(titulo, nota));

                    adaptador.notifyItemInserted(count);
                    layoutManager.scrollToPosition(count);

                    if (temp != count) {
                        count = temp;
                        count--;
                        temp--;
                    }

                    temp++;
                    count++;

                } else {
                    Toast.makeText(NotaActivity.this, "Fill all the blanks", Toast.LENGTH_SHORT).show();
                }

            }
        });

        fabOrder.setOnClickListener(new View.OnClickListener() { //Boton para ordenar
            @Override
            public void onClick(View v) {
                Toast.makeText(NotaActivity.this, "Order", Toast.LENGTH_SHORT).show();
                arreglo = new Nota[notas.size()];
                for (int i = 0; i < arreglo.length; i++) {
                    arreglo[i] = notas.getAt(i);
                }
                notas = noteOrder(arreglo);
                callAdapter();

                fabSearch.setEnabled(true);
                ColorStateList csl = new ColorStateList(new int[][]{new int[0]}, new int[]{0xff8000});
                fabSearch.setBackgroundTintList(csl);

            }
        });

        fabSearch.setOnClickListener(new View.OnClickListener() { //Boton para buscar
            @Override
            public void onClick(View v) {
                String busq = edtBusqueda.getText().toString();
                try{
                    Toast.makeText(NotaActivity.this , "Search" , Toast.LENGTH_SHORT).show();
                    char busqC = busq.charAt(0);
                    int pFind = binSearch(arreglo,busqC);
                    if (pFind!= -1){
                        //FUNCION PARA MOSTRAR LA NOTA, COMO SI HUBIERA DADO CLIC EN ELLA
                        showNote(pFind+1);
                        Toast.makeText(NotaActivity.this , "Element in position: "+ (pFind+1), Toast.LENGTH_SHORT).show();
                    }else
                        Toast.makeText(NotaActivity.this , "Cannot find any element" , Toast.LENGTH_SHORT).show();
                    //Toast.makeText(NotaActivity.this , posicion , Toast.LENGTH_SHORT).show();
                }catch (Exception e) {
                    Toast.makeText(NotaActivity.this, "First enter a text to search", Toast.LENGTH_SHORT).show();
                }}
        });

    }

    public static int binSearch(Nota [] arreglo, char val){ //Busqueda binaria
        return binSearchRec(arreglo, val, 0, arreglo.length - 1);
    }

    private static int binSearchRec(Nota [] arreglo, char val,int lo, int hi){
        if (lo <= hi){
            int mid = lo + ((hi-lo)/2);
            if (val == arreglo[mid].getsTitulo().toLowerCase().charAt(0)){
                return mid;
            }else if(val > arreglo[mid].getsTitulo().toLowerCase().charAt(0)){//SI ES MAYOR

                return binSearchRec(arreglo, val, mid + 1, hi);

            }else{//SI ES MENOR
                return binSearchRec(arreglo, val, lo, mid - 1);
            }
        }else
            return -1;
    }

    //Ordena alfabeticamente
    public static NoteList noteOrder(Nota[] arreglo) {
        long now = System.nanoTime();
        Nota[] arregloCopia = Arrays.copyOf(arreglo, arreglo.length);
        NoteList lista = new NoteList();
        while(lista.size() < arreglo.length) {
            Nota menor = null;
            String menorTitulo = "";
            int index = -1;
            for (int i = 0; i < arregloCopia.length; i++) {
                Nota nota = arreglo[i];
                if (arregloCopia[i] != null) {
                    String notaTitulo = nota.getsTitulo().toLowerCase();
                    if ((!menorTitulo.equals("") && notaTitulo.charAt(0) < menorTitulo.charAt(0)) || menorTitulo.equals("")) {
                        if (index != -1) {
                            arregloCopia[index] = menor;
                        }
                        menor = nota;
                        menorTitulo = notaTitulo;
                        index = i;
                        arregloCopia[i] = null;
                    }
                }
            }
            lista.add(menor);
        }
//        for (int i = 0; i < arreglo.length; i++) {
//            Nota temp = arreglo[i];
//            for (insP = i; insP > 0; insP--) {
//                int previo = insP - 1;
//                if (arreglo[previo].getsTitulo().toLowerCase().charAt(0) > temp.getsTitulo().toLowerCase().charAt(0)){
//                    //Swap
//                    arreglo[insP] = arreglo[previo];
//
//                }else{
//                    break;
//                }
//            }
//
//            arreglo[insP] = temp;
//        }
        System.out.println((double) (System.nanoTime() - now) / 1_000_000_000.0);
        return lista;
    }

    public void erase(int position) {
        try {
            notas.eraseAt(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
        adaptador.notifyItemRemoved(position);
        temp = count;
        count = position;
    }

    public void cancelar() {
        Toast.makeText(NotaActivity.this , "NOT EVEN HAPPEN" , Toast.LENGTH_SHORT).show();
    }
    public void showNote(int position){
        Nota v = notas.getAt(position);
        Intent intent = new Intent(NotaActivity.this , DataActivity.class);
        intent.putExtra("Title",v.getsTitulo());
        intent.putExtra("Note" , v.getsNota());
        startActivity(intent);
    }

    public void callAdapter (){ //Funcion para crear el adaptador

        adaptador = new AdaptadorRe(notas, R.layout.cardview, new AdaptadorRe.OnItemClickListener() {
            @Override
            public void click(Nota nota, int position) { //La interface que se hizo desde el adaptador
                final int pos = position;
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(NotaActivity.this);
                dialogo1.setTitle("Warning");
                dialogo1.setMessage("Are you sure to delete this note?");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Open note", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialogo1, int id) {
                        showNote(pos);
                    }
                });
                dialogo1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        cancelar();
                    }
                });
                dialogo1.setNeutralButton("Erase", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        erase(pos);
                    }
                });
                dialogo1.show();
            }

            @Override
            public void clickLong(Nota nota, final int position) {
                Toast.makeText(NotaActivity.this , "NOT EVEN HAPPEN" , Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(layoutManager); //
        recyclerView.setAdapter(adaptador);
    }
}
