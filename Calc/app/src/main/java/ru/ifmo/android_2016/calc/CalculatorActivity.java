package ru.ifmo.android_2016.calc;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.StringTokenizer;

public final class CalculatorActivity extends Activity {
    public static final String KEY_OPER = "oper";
    public static final String KEY_IS_SECOND_NUM = "second";
    public static final String KEY_NEED_UPDATE = "update";
    public static final String KEY_INPUT_STR = "input";
    public static final String KEY_RES_STR = "str";

    private Button d0Btn;
    private Button d1Btn;
    private Button d2Btn;
    private Button d3Btn;
    private Button d4Btn;
    private Button d5Btn;
    private Button d6Btn;
    private Button d7Btn;
    private Button d8Btn;
    private Button d9Btn;
    private Button clearBtn;
    private Button addBtn;
    private Button subBtn;
    private Button mulBtn;
    private Button divBtn;
    private Button eqvBtn;

    private TextView inputTv;
    private TextView resultTV;

    private OnClickCalcListener onClickCalcListener;

    private class OnClickCalcListener implements View.OnClickListener {
        private StringBuilder inputStr = new StringBuilder("0");

        private boolean isSecondNum = false;
        private char oper = '\0';
        private boolean needUpdate = false;


        public OnClickCalcListener(@Nullable Bundle state) {
            if (state != null) {
                inputStr = new StringBuilder(state.getCharSequence(KEY_INPUT_STR));
                isSecondNum =state.getBoolean(KEY_IS_SECOND_NUM);
                oper = state.getChar(KEY_OPER);
                needUpdate = state.getBoolean(KEY_NEED_UPDATE);
                resultTV.setText(state.getCharSequence(KEY_RES_STR));
            } else {
                inputStr = new StringBuilder("0");
                isSecondNum = false;
                oper = '\0';
                needUpdate = false;
            }
            inputTv.setText(inputStr);
        }

        private void addDigit(char x) {
            if (needUpdate) {
                clear();
            }
            if ((inputStr.length() == 1 && inputStr.charAt(0) == '0')
                    || (inputStr.length() >= 3 && !Character.isDigit(inputStr.charAt(inputStr.length() - 1))
                    && inputStr.charAt(inputStr.length() - 1) == '0')) {
                inputStr.setCharAt(inputStr.length() - 1, x);
                inputTv.setText(inputStr);
                return;
            }
            if (!Character.isDigit(inputStr.charAt(inputStr.length() - 1))) {
                isSecondNum = true;
            }
            inputStr.append(x);
            inputTv.setText(inputStr);
        }

        private void addOper(char oper) {
            needUpdate = false;
            //if oper
            if (!Character.isDigit(inputStr.charAt(inputStr.length() - 1))) {
                inputStr.setCharAt(inputStr.length() - 1, oper);
                this.oper = oper;
                inputTv.setText(inputStr);
                return;
            }
            if (isSecondNum) {
                calc();
            }
            inputStr.append(oper);
            this.oper = oper;
            inputTv.setText(inputStr);
        }

        private void calc() {
            if (isSecondNum) {
                StringTokenizer separator = new StringTokenizer(inputStr.toString(), "+-*/");
                double first;
                double second;
                try {
                    first = Double.parseDouble(separator.nextToken());
                } catch (NumberFormatException e) {
                    Toast.makeText(CalculatorActivity.this, "Ошибочный формат числа", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    second = Double.parseDouble(separator.nextToken());
                } catch (NumberFormatException e) {
                    Toast.makeText(CalculatorActivity.this, "Ошибочный формат числа", Toast.LENGTH_SHORT).show();
                    return;
                }
                double res = 0;
                switch (oper) {
                    case '+':
                        res = first + second;
                        break;
                    case '-':
                        res = first - second;
                        break;
                    case '*':
                        res = first * second;
                        break;
                    case '/':
                        res = first / second;
                        break;
                }
                String resStr = String.valueOf(res);
                if (resStr.contains(".0")) {
                    resStr = resStr.substring(0, resStr.length() - 2);
                }
                if (res == Double.NEGATIVE_INFINITY || res == Double.POSITIVE_INFINITY) {
                    inputTv.setText("0");
                    inputStr = new StringBuilder("0");
                } else {
                    inputTv.setText(resStr);
                    inputStr = new StringBuilder(resStr);
                }
                resultTV.setText(resStr);
                oper = '\0';
                isSecondNum = false;
                needUpdate = true;
            }
        }

        private void clear() {
            inputStr = new StringBuilder("0");
            inputTv.setText("0");
            resultTV.setText("");
            oper = '\0';
            isSecondNum = false;
            needUpdate = false;

        }

        @Override
        public void onClick(android.view.View v) {
            switch (v.getId()) {
                case R.id.d0:
                    addDigit('0');
                    break;
                case R.id.d1:
                    addDigit('1');
                    break;
                case R.id.d2:
                    addDigit('2');
                    break;
                case R.id.d3:
                    addDigit('3');
                    break;
                case R.id.d4:
                    addDigit('4');
                    break;
                case R.id.d5:
                    addDigit('5');
                    break;
                case R.id.d6:
                    addDigit('6');
                    break;
                case R.id.d7:
                    addDigit('7');
                    break;
                case R.id.d8:
                    addDigit('8');
                    break;
                case R.id.d9:
                    addDigit('9');
                    break;
                case R.id.div:
                    addOper('/');
                    break;
                case R.id.mul:
                    addOper('*');
                    break;
                case R.id.sub:
                    addOper('-');
                    break;
                case R.id.add:
                    addOper('+');
                    break;
                case R.id.clear:
                    clear();
                    break;
                case R.id.eqv:
                    calc();
                    break;
            }
        }

        public Bundle getState() {
            Bundle bundle = new Bundle();
            bundle.putCharSequence(KEY_INPUT_STR, inputStr);
            bundle.putBoolean(KEY_IS_SECOND_NUM, isSecondNum);
            bundle.putBoolean(KEY_NEED_UPDATE, needUpdate);
            bundle.putChar(KEY_OPER, oper);
            bundle.putCharSequence(KEY_RES_STR, resultTV.getText());
            return bundle;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        d0Btn = (Button) findViewById(R.id.d0);
        d1Btn = (Button) findViewById(R.id.d1);
        d2Btn = (Button) findViewById(R.id.d2);
        d3Btn = (Button) findViewById(R.id.d3);
        d4Btn = (Button) findViewById(R.id.d4);
        d5Btn = (Button) findViewById(R.id.d5);
        d6Btn = (Button) findViewById(R.id.d6);
        d7Btn = (Button) findViewById(R.id.d7);
        d8Btn = (Button) findViewById(R.id.d8);
        d9Btn = (Button) findViewById(R.id.d9);
        clearBtn = (Button) findViewById(R.id.clear);
        addBtn = (Button) findViewById(R.id.add);
        subBtn = (Button) findViewById(R.id.sub);
        mulBtn = (Button) findViewById(R.id.mul);
        divBtn = (Button) findViewById(R.id.div);
        eqvBtn = (Button) findViewById(R.id.eqv);

        inputTv = (TextView) findViewById(R.id.input);
        resultTV = (TextView) findViewById(R.id.result);

        onClickCalcListener = new OnClickCalcListener(savedInstanceState);

        d0Btn.setOnClickListener(onClickCalcListener);
        d1Btn.setOnClickListener(onClickCalcListener);
        d2Btn.setOnClickListener(onClickCalcListener);
        d3Btn.setOnClickListener(onClickCalcListener);
        d4Btn.setOnClickListener(onClickCalcListener);
        d5Btn.setOnClickListener(onClickCalcListener);
        d6Btn.setOnClickListener(onClickCalcListener);
        d7Btn.setOnClickListener(onClickCalcListener);
        d8Btn.setOnClickListener(onClickCalcListener);
        d9Btn.setOnClickListener(onClickCalcListener);
        clearBtn.setOnClickListener(onClickCalcListener);
        addBtn.setOnClickListener(onClickCalcListener);
        subBtn.setOnClickListener(onClickCalcListener);
        mulBtn.setOnClickListener(onClickCalcListener);
        divBtn.setOnClickListener(onClickCalcListener);
        eqvBtn.setOnClickListener(onClickCalcListener);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putAll(onClickCalcListener.getState());
    }
}
