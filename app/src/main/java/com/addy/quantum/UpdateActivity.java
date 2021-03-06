package com.addy.quantum;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateActivity extends AppCompatActivity {

    private EditText expense_name_input, expense_amount_input;
    private Button update_button, delete_button;

    // To store values from intent string extra
    private String expense_id, expense_name, expense_amount, expense_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        // init vars
        expense_name_input = findViewById(R.id.expense_name_input);
        expense_amount_input = findViewById(R.id.expense_amount_input);
        update_button = findViewById(R.id.update_button);
        delete_button = findViewById(R.id.delete_button);

        // Method to extract values from intent and then put those values in EditTexts as well as in local String variables
        getAndSetIntentExtra();

        // onclick of update button, send data from EditText to DBHelper class
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper databaseHelper = new DatabaseHelper(UpdateActivity.this);    // get values from EditText and pass
                expense_name = expense_name_input.getText().toString();
                expense_amount = expense_amount_input.getText().toString();
                expense_date = getDate();
                boolean updateDone = databaseHelper.updateExpense(expense_id, expense_name, expense_amount, expense_date);

                // Check whether new values are successfully updated or not
                if(updateDone){
                    Toast.makeText(UpdateActivity.this,"Successfully Updated :)", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(UpdateActivity.this,"Something went wrong!!", Toast.LENGTH_SHORT).show();
                }
                setResult(RESULT_OK, getIntent());
                finish();
            }
        });

        // onclick of delete button, delete a row from DB using id field
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper databaseHelper = new DatabaseHelper(UpdateActivity.this);
                boolean isDeleted = databaseHelper.deleteExpense(expense_id);

                // Check whether required row is deleted or not
                if(isDeleted){
                    Toast.makeText(UpdateActivity.this, "Successfully deleted :)",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(UpdateActivity.this, "Failed to delete :(",Toast.LENGTH_SHORT).show();
                }
                setResult(RESULT_OK, getIntent());
                finish();
            }
        });
    }
    public String getDate(){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.format(date);
    }

    public void getAndSetIntentExtra(){
        // Check whether they have string extra or not
        if(getIntent().hasExtra("id") && getIntent().hasExtra("name") && getIntent().hasExtra("amount")){
            expense_id = getIntent().getStringExtra("id");
            expense_name = getIntent().getStringExtra("name");
            expense_amount = getIntent().getStringExtra("amount");

            // Also set these values into EditText, it'll be easy for understanding
            expense_name_input.setText(expense_name);
            expense_amount_input.setText(expense_amount);
        }
        else{
            Toast.makeText(UpdateActivity.this, "No data !!", Toast.LENGTH_SHORT).show();
        }
    }
}