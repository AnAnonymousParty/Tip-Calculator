package com.example.tipcalculator;

import static android.widget.Toast.*;
import static com.example.tipcalculator.Enums.TipRoundingMode.ROUNDUP;
import static java.lang.String.*;
import static java.lang.String.valueOf;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;


public class MainActivity extends AppCompatActivity  {
    private Button addABuckBtn;
    private Button exitBtn;
    private Button takeABuckBtn;

    private CheckBox cashCB;

    private EditText postDiscountAmtFld;
    private EditText preDiscountAmtFld;

    private RadioGroup radioBtns;

    private RadioButton noRoundBtn;
    private RadioButton roundDownBtn;
    private RadioButton roundUpBtn;

    private SeekBar tipPercentSlider;

    private TextView tipAmtFld;
    private TextView tipPercentageFld;
    private TextView totalAmtFld;

    private Enums.TipRoundingMode tipRoundingMode;

    private int extraBucks;

    /**
     * onCreate
     *
     * @param savedInstanceState Bundle object containing the app state.
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

        Init();
    }

    /**
     * onCreate
     *
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * AddEventListeners
     * <p></p>
     * Add listeners for the UI elements we handle.
     */
    private void AddEventListeners() {
        addABuckBtn.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ++extraBucks;

                if (extraBucks > 0) {
                    takeABuckBtn.setEnabled(true);
                }

                try {
                    CalculateTip();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        cashCB.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    CalculateTip();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        exitBtn.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });

        radioBtns.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(@NonNull RadioGroup group, int checkedId) {
                //noinspection LoopStatementThatDoesntLoop
                for (;;) {
                    if (noRoundBtn.getId() == checkedId) {
                        tipRoundingMode = Enums.TipRoundingMode.NOROUNDING;

                        break;
                    }

                    if (roundDownBtn.getId() == checkedId) {
                        tipRoundingMode = Enums.TipRoundingMode.ROUNDDOWN;

                        break;
                    }

                    if (roundUpBtn.getId() == checkedId) {
                        tipRoundingMode = ROUNDUP;

                        break;
                    }

                    tipRoundingMode = Enums.TipRoundingMode.NOROUNDING;

                    break;
                }

                try {
                    CalculateTip();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        postDiscountAmtFld.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            private String currentPostDisc = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (true != s.toString().equals(currentPostDisc)) {
                    postDiscountAmtFld.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[$,.]", "");

                    double parsed = Double.parseDouble(cleanString);

                    String formatted = NumberFormat.getCurrencyInstance().format((parsed / 100));

                    currentPostDisc = formatted;

                    postDiscountAmtFld.setText(formatted);
                    postDiscountAmtFld.setSelection(formatted.length());
                    postDiscountAmtFld.addTextChangedListener(this);

                    try {
                        CalculateTip();
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        preDiscountAmtFld.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            private String currentPreDisc = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (true != s.toString().equals(currentPreDisc)) {
                    preDiscountAmtFld.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[$,.]", "");

                    double parsed = Double.parseDouble(cleanString);

                    String formatted = NumberFormat.getCurrencyInstance().format((parsed / 100));

                    currentPreDisc = formatted;

                    preDiscountAmtFld.setText(formatted);
                    preDiscountAmtFld.setSelection(formatted.length());
                    preDiscountAmtFld.addTextChangedListener(this);

                    postDiscountAmtFld.setText(formatted);
                    postDiscountAmtFld.setSelection(formatted.length());

                    try {
                        CalculateTip();
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        tipPercentSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                tipPercentageFld.setText(valueOf(progress));

                try {
                    CalculateTip();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        takeABuckBtn.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (0 != extraBucks) {
                    --extraBucks;
                }

                if (0 == extraBucks) {
                    takeABuckBtn.setEnabled(false);

                    makeText(getApplicationContext(), "Are you cheap, mean, or did the service really suck!", LENGTH_SHORT).show();
                }

                try {
                    CalculateTip();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void CalculateTip() throws ParseException {
        NumberFormat format = NumberFormat.getCurrencyInstance();

        String preDiscAmtStr  = valueOf(preDiscountAmtFld.getText());
        String postDiscAmtStr = valueOf(postDiscountAmtFld.getText());

        Number preDiscAmt = 0.0;

        try {
            preDiscAmt = format.parse(preDiscAmtStr);
        } catch (Exception ignored) {

        }

        Number postDiscAmt = 0.0;

        try {
            postDiscAmt = format.parse(postDiscAmtStr);
        } catch (Exception ex) {

        }

        String tipPercentStr = valueOf(tipPercentageFld.getText());

        Double tipPercentVal = 0.0;

        try {
            tipPercentVal = Double.parseDouble(tipPercentStr);
        } catch (Exception ex) {

        }

        Double   tip = preDiscAmt.doubleValue() * tipPercentVal / 100.00;
        Double total = postDiscAmt.doubleValue() + tip;

        switch (tipRoundingMode) {
            case NOROUNDING: {
                // All that needs to be done has been done.
            }
            break;

            case ROUNDDOWN: {
                long  iPart  = (long) total.doubleValue();

                tip -= (total - iPart);
            }
            break;

            case ROUNDUP: {
                long  iPart  = (long) total.doubleValue();
                double fPart = total - iPart;
                double adj   = 1.00 - fPart;

                tip   += adj;
            }
            break;

            default: {

            }
            break;
        }

        tip += extraBucks;

        if (true == cashCB.isChecked()) {
            switch (tipRoundingMode) {
                case NOROUNDING: {
                    // All that needs to be done has been done.
                }
                break;

                case ROUNDDOWN: {
                    long  iPart  = (long) tip.doubleValue();

                    tip = (double) iPart;
                }
                break;

                case ROUNDUP: {
                    long  iPart  = (long) tip.doubleValue();
                    double fPart = tip - iPart;

                    if (fPart > 0.0) {
                        tip = (double) iPart + 1;
                    }
                }
                break;

                default: {

                }
                break;
            }

            tipAmtFld.setText(format(Locale.US, "%.2f", tip));
            totalAmtFld.setText(postDiscountAmtFld.getText());
        } else {
            total = postDiscAmt.doubleValue() + tip;

            tipAmtFld.setText(format(Locale.US, "%.2f", tip));
            totalAmtFld.setText(format(Locale.US, "%.2f", total));
        }
    }

    private void Init() {
        InitVariables();
        AddEventListeners();
        InitUI();
    }

    private void InitVariables() {
        tipRoundingMode = Enums.TipRoundingMode.NOROUNDING;

        extraBucks = 0;

        addABuckBtn        = (Button)findViewById(R.id.B_AddABuck);
        cashCB             = (CheckBox)findViewById(R.id.CB_Cash);
        exitBtn            = (Button)findViewById(R.id.B_Exit);
        noRoundBtn         = (RadioButton)findViewById(R.id.RB_NoRound);
        radioBtns          = (RadioGroup)findViewById(R.id.RG_RoundingBtns);
        roundDownBtn       = (RadioButton)findViewById(R.id.RB_RoundDown);
        roundUpBtn         = (RadioButton)findViewById(R.id.RB_RoundUp);
        postDiscountAmtFld = (EditText)findViewById(R.id.ET_PostDiscountAmount);
        preDiscountAmtFld  = (EditText)findViewById(R.id.ET_PreDiscountAmount);
        takeABuckBtn       = (Button)findViewById(R.id.B_TakeABuck);
        tipAmtFld          = (TextView)findViewById(R.id.ET_TipAmt);
        tipPercentageFld   = (TextView)findViewById(R.id.TV_TipPercent);
        tipPercentSlider   = (SeekBar)findViewById(R.id.SB_TipPercent);
        totalAmtFld        = (TextView)findViewById(R.id.ET_TotalAmt);
    }

    private void InitUI() {
        tipPercentageFld.setText(valueOf(tipPercentSlider.getProgress()));

        switch (tipRoundingMode) {
            case NOROUNDING: {
                radioBtns.check(R.id.RB_NoRound);

                noRoundBtn.setSelected(true);
                roundDownBtn.setSelected(false);
                roundUpBtn.setSelected(false);
            }
            break;

            case ROUNDDOWN: {
                radioBtns.check(R.id.RB_RoundDown);

                noRoundBtn.setSelected(false);
                roundDownBtn.setSelected(true);
                roundUpBtn.setSelected(false);
            }
            break;

            case ROUNDUP: {
                radioBtns.check(R.id.RB_RoundUp);

                noRoundBtn.setSelected(false);
                roundDownBtn.setSelected(false);
                roundUpBtn.setSelected(true);
            }
            break;

            default: {
                radioBtns.check(R.id.RB_NoRound);

                noRoundBtn.setSelected(true);
                roundDownBtn.setSelected(false);
                roundUpBtn.setSelected(false);
            }
            break;
        }
    }
}