package com.CS460.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 * Eli Holter
 * CS460
 * Lab 1: Calculator
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //define our TextViews and MaterialButtons
    TextView solutionTV, resultTV;

    MaterialButton buttonC, buttonAC, buttonOpenBracket, buttonCloseBracket;
    MaterialButton button0, button1, button2, button3, button4, button5, button6, button7, button8, button9;
    MaterialButton buttonAdd, buttonSubtract, buttonMultiply, buttonDivide, buttonEquals, buttonDecimal;

    //String[] buttonNames = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "c", "ac", "open_bracket", "close_bracket", "add", "subtract", "multiply", "divide", "equals", "."};

    /**
     * This function runs when the program is stared. On creation of this object, do this stuff
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //initialize every relevant component for the program to run
        resultTV = findViewById(R.id.result_tv);
        solutionTV = findViewById(R.id.solution_tv);
        assignButton(buttonC, R.id.button_c);
        assignButton(buttonAC, R.id.button_ac);
        assignButton(buttonOpenBracket, R.id.button_open_bracket);
        assignButton(buttonCloseBracket, R.id.button_close_bracket);
        assignButton(button0, R.id.button_0);
        assignButton(button1, R.id.button_1);
        assignButton(button2, R.id.button_2);
        assignButton(button3, R.id.button_3);
        assignButton(button4, R.id.button_4);
        assignButton(button5, R.id.button_5);
        assignButton(button6, R.id.button_6);
        assignButton(button7, R.id.button_7);
        assignButton(button8, R.id.button_8);
        assignButton(button9, R.id.button_9);
        assignButton(buttonAdd, R.id.button_add);
        assignButton(buttonSubtract, R.id.button_subtract);
        assignButton(buttonMultiply, R.id.button_multiply);
        assignButton(buttonDivide, R.id.button_divide);
        assignButton(buttonDecimal, R.id.button_decimal);
        assignButton(buttonEquals, R.id.button_equals);

        resultTV.setText(R.string.name);
        solutionTV.setText(R.string.CWUID);

    }

    /**
     * On click of any button with this as its set click listener, do this stuff
     * @param view The view that was clicked. In this case every view is a MaterialButton
     */
    @Override
    public void onClick(View view) {
        //get the view as a MaterialButton, get its text and get the text in solutionTV
        MaterialButton button = (MaterialButton) view;
        String buttonText = button.getText().toString();
        String dataToCalculate = solutionTV.getText().toString();

        //switch on button text to handle AC, C and =
        switch (buttonText) {
            case "AC":
                solutionTV.setText("");
                resultTV.setText("0.0");
                return;
            case "=":
                solutionTV.setText(resultTV.getText());
                return;
            case "C":
                //to avoid some weird errors in the solutionTV, added in some logic to prevent substring when there aren't enough chars
                if (dataToCalculate.length() > 1) {
                    dataToCalculate = dataToCalculate.substring(0, dataToCalculate.length() - 1);
                } else {
                    dataToCalculate = "0";
                }
                break;
            default:
                //to avoid some weird interpretation issues, disallow leading zeros on non-decimal numbers
                if (!dataToCalculate.equals("0")) {
                    dataToCalculate += buttonText;
                } else {
                    dataToCalculate = buttonText;
                }
                break;
        }

        solutionTV.setText(dataToCalculate);
        String finalResult = getResults(dataToCalculate);

        //display the result when no errors occur
        if(!finalResult.equals("Err")) {
            resultTV.setText(finalResult);
        }
    }

    /**
     * Assign buttons easily and give them an OnClickListener (this)
     * @param button the defined button to be assigned
     * @param id the id of the component to assign
     */
    public void assignButton(MaterialButton button, int id) {
        button = findViewById(id);
        button.setOnClickListener(this);
    }

    /**
     * Get the results of the given equation using some javascript for java string evaluation
     * @param data equation to be evaluated
     * @return result of the calculation
     */
    public String getResults(String data) {
        String returnString = "init";
        try {
            //javascript magic
            Context context = Context.enter();
            context.setOptimizationLevel(-1);
            Scriptable scriptable = context.initStandardObjects();
            returnString = context.evaluateString(scriptable, data,"Javascript", 1, null).toString();
        } catch(Exception e) {
            returnString = "Err";
        }

        return returnString;
    }
}