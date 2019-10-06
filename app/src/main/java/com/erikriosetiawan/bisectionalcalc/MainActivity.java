package com.erikriosetiawan.bisectionalcalc;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextValueA;
    private EditText editTextValueB;
    private EditText editTextTolerance;
    private Button btnShowResult;
    private RecyclerView rvResult;

    private ArrayList<String> rootIterations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootIterations = new ArrayList<>();
        initView();
        setRecyclerList();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_show_result) {
            if (btnShowResult.getText().toString().equalsIgnoreCase(getString(R.string.tampilkan_hasil))) {
                btnShowResult.setText(getString(R.string.reset_data));
                double valueA = toDouble(editTextValueA.getText().toString());
                double valueB = toDouble(editTextValueB.getText().toString());
                double tolerance = toDouble(editTextTolerance.getText().toString());
                bisectional(valueA, valueB, tolerance);
            } else {
                btnShowResult.setText(getString(R.string.tampilkan_hasil));
                editTextValueA.setText("");
                editTextValueB.setText("");
                editTextTolerance.setText("");
                rootIterations.clear();
            }
        }
    }

    private void initView() {
        editTextValueA = findViewById(R.id.edt_a);
        editTextValueB = findViewById(R.id.edt_b);
        editTextTolerance = findViewById(R.id.edt_tolerance);
        btnShowResult = findViewById(R.id.btn_show_result);
        btnShowResult.setOnClickListener(this);
        rvResult = findViewById(R.id.rv_results);
    }

    private double function(double x) {
        return Math.pow(x, 2) + 4 * x + 2;
    }

    @SuppressLint("DefaultLocale")
    private void bisectional(double a, double b, double tolerance) {
        if (function(a) * function(b) >= 0) {
            editTextValueA.setError("Nilai yang dimasukkan tidak valid");
            editTextValueB.setError("Nilai yang dimasukkan tidak valid");
        } else {
            double c;
            while ((b - a) >= tolerance) {
                c = (a + b) / 2;

                if (function(c) == 0) {
                    break;
                } else if (function(c) * function(a) < 0) {
                    b = c;
                } else {
                    a = c;
                }
                rootIterations.add(String.format("Nilai akarnya adalah : %.4f", c));
            }
        }
    }

    private void setRecyclerList() {
        ResultAdapter resultAdapter = new ResultAdapter(this, rootIterations);
        rvResult.setAdapter(resultAdapter);
        rvResult.setLayoutManager(new LinearLayoutManager(this));
        resultAdapter.notifyDataSetChanged();
    }

    private double toDouble(String value) {
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.ENGLISH);
        try {
            Number number = numberFormat.parse(value);
            return number.doubleValue();
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
