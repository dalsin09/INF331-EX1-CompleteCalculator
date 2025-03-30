package com.example.completecalc;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;




public class MainActivity extends AppCompatActivity {

    private String display = "";
    private EditText editText;
    private Button button0, button1, button2, button3, button4, button5, button6, button7, button8, button9;
    private Button buttonC, buttonPercent, buttonDivide, buttonTimes, buttonMinus, buttonAdd, buttonEqual, buttonDot, buttonInverter;
    private ImageButton buttonBackSpace;
    private double number1, number2;
    private String operador;
    private int operadorPos;
    private boolean operadorPressionado = false ,porcentagemPressionado1 =false, porcentagemPressionado2 =false, igualPressionado = false;


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
        editText = findViewById(R.id.textView);
        button0 = findViewById(R.id.num0);
        button1 = findViewById(R.id.num1);
        button2 = findViewById(R.id.num2);
        button3 = findViewById(R.id.num3);
        button4 = findViewById(R.id.num4);
        button5 = findViewById(R.id.num5);
        button6 = findViewById(R.id.num6);
        button7 = findViewById(R.id.num7);
        button8 = findViewById(R.id.num8);
        button9 = findViewById(R.id.num9);
        buttonAdd = findViewById(R.id.add);
        buttonMinus = findViewById(R.id.minus);
        buttonTimes = findViewById(R.id.times);
        buttonDivide = findViewById(R.id.divide);
        buttonPercent = findViewById(R.id.percentual);
        buttonBackSpace = findViewById(R.id.backspace);
        buttonInverter = findViewById(R.id.inverter);
        buttonEqual = findViewById(R.id.equal);
        buttonDot = findViewById(R.id.dot);
        buttonC = findViewById(R.id.c);

        editText.requestFocus();
        editText.setShowSoftInputOnFocus(false);
        editText.setOnClickListener(v -> {
            v.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        });

        button0.setOnClickListener(v -> adicionarNumero("0"));
        button1.setOnClickListener(v -> adicionarNumero("1"));
        button2.setOnClickListener(v -> adicionarNumero("2"));
        button3.setOnClickListener(v -> adicionarNumero("3"));
        button4.setOnClickListener(v -> adicionarNumero("4"));
        button5.setOnClickListener(v -> adicionarNumero("5"));
        button6.setOnClickListener(v -> adicionarNumero("6"));
        button7.setOnClickListener(v -> adicionarNumero("7"));
        button8.setOnClickListener(v -> adicionarNumero("8"));
        button9.setOnClickListener(v -> adicionarNumero("9"));
        buttonC.setOnClickListener(v -> limparTudo());
        buttonBackSpace.setOnClickListener(v -> apagarUltimoCaractere());
        buttonDot.setOnClickListener(v -> adicionarNumero("."));
        buttonInverter.setOnClickListener(v -> inverter());
        buttonAdd.setOnClickListener(v -> adicionarOperador("+"));
        buttonMinus.setOnClickListener(v -> adicionarOperador("-"));
        buttonTimes.setOnClickListener(v -> adicionarOperador("*"));
        buttonDivide.setOnClickListener(v -> adicionarOperador("÷"));
        buttonPercent.setOnClickListener(v -> adicionarPorcentagem("%"));
        buttonEqual.setOnClickListener(v -> calcularResultado());


    }

    private void adicionarPorcentagem(String s) {
        if(igualPressionado) igualPressionado = false;
        if (display.contains("%") ||
                (operadorPressionado && display.substring(operadorPos + 1).isEmpty()) ||
                (!operadorPressionado && display.isEmpty())) {
            return;
        }
        if(!operadorPressionado){
            porcentagemPressionado1 = true;
        }else porcentagemPressionado2 = true;
        display+="%";
        atualizarDisplay();
    }

    private void adicionarOperador(String op) {
        if (display.isEmpty() || operadorPressionado) return;
        if(igualPressionado) igualPressionado = false;

        operadorPos = display.length();
        operador= op;
        operadorPressionado = true;
        display += operador;
        atualizarDisplay();
    }
    private void calcularResultado() {
        if (!operadorPressionado || display.isEmpty()) return;

        try {
            //extrai os numeros da string
            double number1, number2;
            if(porcentagemPressionado1){
                number1 = Double.parseDouble(display.substring(0,operadorPos-1));
                number1 /=100;
                number2 = Double.parseDouble(display.substring(operadorPos + 1));
            } else if (porcentagemPressionado2) {
                number1 = Double.parseDouble(display.substring(0,operadorPos));
                number2 = Double.parseDouble(display.substring(operadorPos + 1 , display.length()-1));
                number2 =(number1 * number2)/ 100;
            }else {
                number1 = Double.parseDouble(display.substring(0, operadorPos));
                number2 = Double.parseDouble(display.substring(operadorPos + 1));
            }


            // Verificação de divisão por zero
            if (operador.equals("÷")) {
                operador = "/";
                if(number2 == 0) {
                    display = "ERROR";
                    atualizarDisplay();
                    limparEstado();
                    return;
                }
            }

            double resultado = 0;
            switch (operador) {
                case "+": resultado = number1 + number2; break;
                case "-": resultado = number1 - number2; break;
                case "*": resultado = number1 * number2; break;
                case "/": resultado = number1 / number2; break;
            }

            // Formatação do resultado
            display = formatarResultado(resultado);
            igualPressionado = true;
            atualizarDisplay();
            operador = "";
            porcentagemPressionado1 = porcentagemPressionado2= operadorPressionado = false;
        } catch (Exception e) {
            display = "ERROR";
            atualizarDisplay();
            limparEstado();
        }
    }

    private String formatarResultado(double valor) {
        if (valor % 1 == 0) {
            return String.valueOf((int) valor);
        } else {
            return String.valueOf(valor);
        }
    }

    private void limparEstado() {
        operadorPressionado = false;
        igualPressionado = false;
        operador = "";
        porcentagemPressionado1 = false;
        porcentagemPressionado2=false;
    }


    private void inverter() {
        if(igualPressionado) igualPressionado = false;
        if (display.isEmpty() || display.equals("0")) return;

        if (!operadorPressionado) {
            // Inverte o primeiro número
            if (display.charAt(0) == '-') {
                display = display.substring(1);
            } else {
                display = "-" + display;
            }
        } else {
            // Inverte o segundo número
            String numero2 = display.substring(operadorPos + 1);
            if (numero2.startsWith("-")) {
                numero2 = numero2.substring(1);
            } else {
                numero2 = "-" + numero2;
            }
            display = display.substring(0, operadorPos + 1) + numero2;
        }
        atualizarDisplay();
    }

    private void apagarUltimoCaractere() {
        if (display.isEmpty()) return;
        if(igualPressionado) igualPressionado = false;
        char ultimoChar = display.charAt(display.length() - 1);
        display = display.substring(0, display.length() - 1);
        if (ultimoChar == '+' || ultimoChar == '-' || ultimoChar == '*' || ultimoChar == '÷') {
            operadorPressionado = false;
            operador = "";
        } else if (ultimoChar == '%') {
            porcentagemPressionado1 = porcentagemPressionado2=false;
        }
        atualizarDisplay();
    }

    private void limparTudo() {
        display = "";
        number1 = 0;
        number2 = 0;
        operador = "";
        operadorPressionado = false;
        porcentagemPressionado1 = porcentagemPressionado2=false;
        igualPressionado = false;
        atualizarDisplay();
    }

    private void adicionarNumero(String numero) {
        if(igualPressionado){
            display = "";
            igualPressionado = false;
        }
        display += numero;
        atualizarDisplay();
    }

    private void atualizarDisplay() {
        editText.setText(display);
        editText.setSelection(editText.getText().length());
    }


}