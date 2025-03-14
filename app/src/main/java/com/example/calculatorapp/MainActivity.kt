package com.example.calculatorapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import java.util.*
import java.math.BigDecimal

class MainActivity : ComponentActivity() {
    lateinit var oldWorkingTV: TextView
    lateinit var curWorkingTV: TextView
    private var canAddDecimal = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        oldWorkingTV = findViewById<TextView>(R.id.oldWorkingTV)
        curWorkingTV = findViewById<TextView>(R.id.curWorkingTV)
    }

    fun backSpaceAction(view: View) {
        val length = curWorkingTV.text.length
        if (length > 0) {
            curWorkingTV.text = curWorkingTV.text.subSequence(0, length - 1)
        }
//        var workings = curWorkingTV.text.toString()
//        if (workings.isNotEmpty()) {
//            workings = workings.dropLast(1)
//            curWorkingTV.text = workings
//        }
    }

    fun numberAction(view: View) {
        if(view is Button){
            if(view.text == "."){
                if(canAddDecimal){
                    curWorkingTV.append(view.text)
                    canAddDecimal = false
                }
            }else{
                curWorkingTV.append(view.text)
            }

        }
    }

    private fun canAddOperation(operator: String): Boolean{
        if(curWorkingTV.text.isEmpty()) {
            if(operator in "x/%") return false
            else return true
        }
        if(curWorkingTV.text.last() in "+-x/%"){
            return false
        }
        return true
    }
    fun operationAction(view: View) {
        if (view is Button && canAddOperation(view.text.toString())) {
            curWorkingTV.append(view.text)
            canAddDecimal = true
        }
    }
    fun equalAction(view: View) {
        val processText = curWorkingTV.text
        if(processText.isNotEmpty() && processText.last().isDigit()){
            val result: BigDecimal? = evaluateExpression(processText.toString())

            if(result != null){
                oldWorkingTV.text = processText
                if (result.stripTrailingZeros().scale() <= 0) {
                    curWorkingTV.text = result.toInt().toString() // Nếu là số nguyên
                } else {
                    curWorkingTV.text = result.toPlainString() // Nếu là số thực, in đầy đủ
                }
            }
        }else{
            Toast.makeText(
                this@MainActivity,
                "Invalid input",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

//    private fun getResult(): CharSequence? {
//        val digitsOperatorsList = digitsOperator()
//        if(digitsOperatorsList.isEmpty()) return ""
//
//        val modulationList = modulationCalculate(digitsOperatorsList)
//        if(modulationList.isEmpty()) return ""
//
//        val timeDivisonList = timDivisionCalculate(modulationList)
//        if(timeDivisonList.isEmpty()) return ""
//
//        val result = addSubtractCalculate(timeDivisonList)
//
//        if(result == result.toInt().toBigDecimal()){
//            return result.toInt().toString()
//        }
//
//        return result.toString()
//    }
//    private fun digitsOperator(): MutableList<Any> {
//        val list = mutableListOf<Any>()
//        var currentDigit = ""
//        var text = curWorkingTV.text
//        if(text[0] == '-'){
//            currentDigit += '-'
//            text = text.substring(1)
//        }else if(text[0] == '+'){
//            text = text.substring(1)
//        }else if(text[0] == 'x' || text[0] == '/'){
//            return list
//        }
//
//        for(character in text){
//            if(character.isDigit() || character == '.'){
//                currentDigit += character
//            }else{
//                list.add(currentDigit.toBigDecimal())
//                currentDigit = ""
//                list.add(character)
//            }
//        }
//
//        if(currentDigit != ""){
//            list.add(currentDigit.toBigDecimal())
//        }
//
//        return list
//    }
//
//    private fun modulationCalculate(passedList: MutableList<Any>): MutableList<Any> {
//        var newList = mutableListOf<Any>()
//
//
//        return newList
//    }
//
//    private fun timDivisionCalculate(passedList: MutableList<Any>): MutableList<Any> {
//        var list = passedList
//        while(list.contains('x') || list.contains('/')) {
//            list = calcTimesDiv(list)
//        }
//
//        return list
//    }
//
//    private fun addSubtractCalculate(passedList: MutableList<Any>): BigDecimal {
//        var result = passedList[0] as BigDecimal
//        for(i in passedList.indices){
//            if(passedList[i] is Char && i != passedList.lastIndex){
//                val operator = passedList[i]
//                val nextDigit = passedList[i + 1] as BigDecimal
//                if(operator == '+'){
//                    result += nextDigit
//                }
//                if(operator == '-'){
//                    result -= nextDigit
//                }
//            }
//        }
//
//        return result
//
//    }
//
//
//    private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any> {
//        val newList = mutableListOf<Any>()
//        var restartIndex = passedList.size
//        for(i in passedList.indices){
//            if(passedList[i] is Char && i != passedList.lastIndex && i < restartIndex){
//                val operator = passedList[i]
//                val prevDigit = passedList[i - 1] as BigDecimal
//                val nextDigit = passedList[i + 1] as BigDecimal
//
//                when(operator){
//                    'x' -> {
//                        newList.add(prevDigit*nextDigit)
//                    }
//                    '/' -> {
//                        newList.add(prevDigit/nextDigit)
//                    }
//                    else -> {
//                        newList.add(prevDigit)
//                        newList.add(operator)
//                    }
//                }
//            }
//            if(i > restartIndex)  {
//                newList.add(passedList[i])
//            }
//        }
//
//        return newList
//    }

    fun evaluateExpression(expression: String): BigDecimal? {
        val numStack = Stack<BigDecimal>()
        val opStack = Stack<Char>()

        fun mod(a: BigDecimal, b: BigDecimal): BigDecimal? {
            if(b == BigDecimal.ZERO){
                Toast.makeText(
                    this@MainActivity,
                    "Unable to mod for 0",
                    Toast.LENGTH_SHORT
                ).show()
                return null
            }

            return a%b
        }

        fun div(a: BigDecimal, b: BigDecimal): BigDecimal? {
            if(b == BigDecimal.ZERO){
                Toast.makeText(
                    this@MainActivity,
                    "Unable to divide for 0",
                    Toast.LENGTH_SHORT
                ).show()
                return null
            }
            return a/b
        }

        fun applyOperator() {
            if (numStack.size < 2 || opStack.isEmpty()) return

            val b = numStack.pop()
            val a = numStack.pop()
            val op = opStack.pop()

            val result: BigDecimal? = when (op) {
                '+' -> a + b
                '-' -> a - b
                'x' -> a * b
                '/' -> div(a, b)
                '%' -> mod(a, b)
                else -> throw IllegalArgumentException("Unknown operator: $op")
            }
            numStack.push(result)
        }

        var i = 0
        var allowNegative = true  // Cho phép số âm khi bắt đầu hoặc sau toán tử
        while (i < expression.length) {
            val ch = expression[i]

            when {
                // Xử lý số thực (có dấu chấm thập phân)
                ch.isDigit() || ch == '.' || (allowNegative && (ch == '-' || ch == '+')) -> {
                    val sb = StringBuilder()
                    if (ch == '-' || ch == '+') {
                        sb.append(ch)  // Ghi nhận dấu của số
                        i++
                    }
                    while (i < expression.length && (expression[i].isDigit() || expression[i] == '.')) {
                        sb.append(expression[i])
                        i++
                    }
                    numStack.push(sb.toString().toBigDecimal())
                    i-- // Lùi lại tránh bỏ qua ký tự tiếp theo
                    allowNegative = false  // Sau số thì không thể có dấu '-' đơn lẻ
                }

                ch in "+-x/%" -> {
                    while (opStack.isNotEmpty() && precedence(opStack.peek()) >= precedence(ch)) {
                        applyOperator()
                    }
                    opStack.push(ch)
                    allowNegative = true  // Cho phép số âm sau toán tử
                }
            }
            i++
        }

        // Xử lý toán tử còn lại
        while (opStack.isNotEmpty()) {
            applyOperator()
        }

        return numStack.pop()
    }

    // Xác định độ ưu tiên toán tử
    fun precedence(op: Char): Int {
        return when (op) {
            '%' -> 3
            'x', '/' -> 2
            '+', '-' -> 1
            else -> 0
        }
    }




    fun allClearAction(view: View) {
        oldWorkingTV.text = ""
        curWorkingTV.text = ""
        canAddDecimal = true
    }

    fun clearAction(view: View) {
        oldWorkingTV.text = ""
    }


    fun resverseSignAction(view: View) {
        if(curWorkingTV.text.isNotEmpty()){
            var i : Int = curWorkingTV.text.length - 1
            while(i >= 0){
                if(curWorkingTV.text[i] == '+'){
                    curWorkingTV.text = curWorkingTV.text.substring(0, i) + "-" + curWorkingTV.text.substring(i + 1)
                    return
                }
                if(curWorkingTV.text[i] == '-'){
                    curWorkingTV.text = curWorkingTV.text.substring(0, i) + "+" + curWorkingTV.text.substring(i + 1)
                    return
                }
                --i
            }

            curWorkingTV.text = "-" + curWorkingTV.text
        }
    }
}
