package wbh.ema.sodoku;

import android.app.AlertDialog;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import wbh.ema.sodoku.databinding.ActivityMainBinding;

import android.view.Menu;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private  Sodoku4x4 sodoku4x4_board = new Sodoku4x4();

    private int difficultyLevel = 1;

    private static String INCORRECT_VALUE_TOAST = "Übernahme des Werts nicht möglich";


    private final int buttonIDs [][] =
            {{R.id.button0_0 , R.id.button0_1, R.id.button1_0, R.id.button1_1},
                    {R.id.button0_2, R.id.button0_3, R.id.button1_2, R.id.button1_3},
                    {R.id.button2_0, R.id.button2_1, R.id.button3_0, R.id.button3_1},
                    {R.id.button2_2, R.id.button2_3, R.id.button3_2, R.id.button3_3}};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        //Initialisierung des Spielfelds bei Start
        fillButtons(difficultyLevel);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.refresh){
            sodoku4x4_board = new Sodoku4x4();
            fillButtons(difficultyLevel);
            calculateNumEmptyFields(7);
        }else if(id == R.id.action_settings){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Wähle die Schwierigkeit");
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_difficulty, null);
            final NumberPicker picker = (NumberPicker) dialogView.findViewById(R.id.number_picker);
            builder.setView(dialogView).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    difficultyLevel = picker.getValue();
                }
            })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {}
                    });
            picker.setMinValue(1);
            picker.setMaxValue(7);
            picker.setValue(difficultyLevel);
            builder.create();
            builder.show();
        } else if(id == R.id.version){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Informationen zur Version");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {}
                    });
            builder.setMessage("Die installierte Version der App ist: " + BuildConfig.VERSION_NAME +" \nDie App wurde von Timo Schmitz entwickelt!");
            builder.create();
            builder.show();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Berechnet die Anzahl der zu löschenden Felder
     * auf Basis des Schwierigkeitsgrades
     *
     * @param  difficultyLevel Schwierigekeitslevel (1-7)
     * @return      Anzahl der leeren Felder
     */
    public int calculateNumEmptyFields(int difficultyLevel){
        int emptyFields = 9;
        for (int i = 0; i < difficultyLevel-1; i++) {
            emptyFields++;
        }
        return emptyFields;
    }

    /**
     * Initialisierung des Spielfeldes und der Button mit deren Eigenschaften
     *
     * @param  difficultyLevel Schwierigekeitslevel (1-7)
     */
    public void fillButtons(int difficultyLevel){
        sodoku4x4_board.clearRandomFields(calculateNumEmptyFields(difficultyLevel));

        for (int i = 0; i < buttonIDs[0].length; i++) {
            for (int j = 0; j < buttonIDs.length; j++) {
                Button button = (Button) findViewById(buttonIDs[i][j]);
                button.setBackgroundColor(Color.parseColor("#d9dadb"));
                int number = sodoku4x4_board.getValue(i,j);
                if(number > 0){
                    button.setClickable(false);
                    button.setText(Integer.toString(number));
                    button.setTextColor(Color.parseColor("#808080"));
                }else{
                    button.setClickable(true);
                    button.setText("...");
                    button.setTextColor(Color.parseColor("#000000"));
                }
                //setze Tags für Zeile und Spalte
                button.setTag(R.id.row, i);
                button.setTag(R.id.col, j);
            }
        }
    }

    /**
     * Prüf-Funktion für Spielende und Anzeige
     * des Spielende-Alert-Dialogs
     */
    public void solvedCheck(){
        if(sodoku4x4_board.solved()){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Gratulation!");
            builder.setMessage("Du hast gewonnen!");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {}
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    /**
     * onClick-Funktion für jeden Spielfeld-Button (mit Auswahl-
     * möglichkeiten 1,2,3,4 und Feld leeren)
     */
    public void onButtonClick(View view){
        String[] values = {"1","2","3","4", "Feld leeren"}; // mögliche Werte zur Auswahl
        int selectedOption = 0;
        int row = (int) view.getTag(R.id.row);
        int col = (int) view.getTag(R.id.col);
        
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Zeile " + (row+1)+ " Spalte " + (col+1) + ": Wert eingeben/verändern");
        builder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {}
        });
        builder.setSingleChoiceItems(values, selectedOption, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which != 4) {
                    //prüfen, ob angeklickter Wert vailde + einfügen und anzeigen des Wertes als Button-Text
                    if (sodoku4x4_board.trySetValue(row, col, Integer.parseInt(values[which]))) {
                        Button button = (Button) findViewById(view.getId());
                        button.setText(values[which]);
                        dialog.dismiss();
                        solvedCheck();
                    }else {
                        //Wenn Spielregel verletzt => Toast mit Fehler anzeigen
                        Toast toast = Toast.makeText(getApplicationContext(), INCORRECT_VALUE_TOAST, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }else{
                    //Leert den Wert Feldes und setzt Button zurück
                    Button button = (Button) findViewById(view.getId());
                    button.setText("...");
                    sodoku4x4_board.clearValue(row,col);
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}