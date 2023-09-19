package com.example.tipcalculator

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat



private const val INITIAL_TIP_PERCENTAGE=5

class MainActivity : AppCompatActivity() {
    lateinit var seekBar:SeekBar
    lateinit var tipPercent:TextView
    lateinit var baseCost:EditText
    lateinit var tipGiven:TextView
    lateinit var total:TextView
    lateinit var tvTipDescription: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        seekBar=findViewById(R.id.seekBar)
        tipPercent=findViewById(R.id.tvTipPercentage)
        baseCost=findViewById(R.id.etBaseCost)
        tipGiven=findViewById(R.id.tvTipGiven)
        total=findViewById(R.id.tvTotalAmount)
        tvTipDescription=findViewById(R.id.tvTipDescription)

        seekBar.progress= INITIAL_TIP_PERCENTAGE
        tipPercent.text="$INITIAL_TIP_PERCENTAGE%"
        updateTipDescription(INITIAL_TIP_PERCENTAGE)


        seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                tipPercent.text="$progress%"
                computeTipAndTotal()
                updateTipDescription(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        baseCost.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                computeTipAndTotal()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun updateTipDescription(tipPercent: Int) {
        val tipDescription=when(tipPercent){
            in 0..9 ->"Poor"
            in 10..14->"Acceptable"
            in 15..19->"Good"
            in  20..24->"Great"
            else->"Amazing"
        }
        tvTipDescription.text=tipDescription
        val color=ArgbEvaluator().evaluate(
            tipPercent.toFloat()/seekBar.max,
            ContextCompat.getColor(this,R.color.Color_worst),
            ContextCompat.getColor(this,R.color.Color_best)
        ) as Int
        tvTipDescription.setTextColor(color)
    }

    private fun computeTipAndTotal() {

        if(baseCost.text.isEmpty()){
            tipGiven.text=""
            total.text=""
            return
        }
        val baseAmount=baseCost.text.toString().toDouble()
        val tipPercent=seekBar.progress
        val tipAmount=baseAmount*tipPercent/100
        val totalAmount=baseAmount+tipAmount

        tipGiven.text=("%.2f").format(tipAmount)
        total.text=("%.2f").format(totalAmount)
    }
}