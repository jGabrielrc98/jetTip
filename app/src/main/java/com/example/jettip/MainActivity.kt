package com.example.jettip


import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jettip.componets.InputField
import com.example.jettip.ui.theme.JetTipTheme
import com.example.jettip.util.calculateTotalPerPerson
import com.example.jettip.util.calculateTotalTip
import com.example.jettip.widgets.RounIconButton


@OptIn(ExperimentalComposeUiApi::class)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetTipTheme {
                MyApp {
                    MainContent()
                }
            }
        }
    }
}
@Composable
fun MyApp(content: @Composable () -> Unit){
    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        content()
    }
}
@Preview
@Composable
fun TopHeader(totalPerPerson: Double = 0.0){
    Surface(modifier = Modifier
        .fillMaxWidth()
        .padding(15.dp)
        .height(150.dp)
        .clip(shape = CircleShape.copy(all = CornerSize(12.dp))),
        color = Color(0xFFB55BFC)
    ) {
        Column(modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            val total = "%.2f".format(totalPerPerson)
            Text(text = "Total Por Pessoa",
                style = MaterialTheme.typography.titleMedium,)
            Text(text = "$$total", style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold)

        }

    }
}

@ExperimentalComposeUiApi
@Preview
@Composable
fun MainContent() {
    BillForm{billAmt ->
        Log.d("AMT","MainContent: {${billAmt.toInt()}}")
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(modifier: Modifier = Modifier,
             onValchange: (String) -> Unit = {})
{

    val totalBillState = remember {
        mutableStateOf("")
    }
    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    val slinderPositionState = remember {
        mutableStateOf( 0f)
    }
    val tipPercentage = (slinderPositionState.value * 100).toInt()

    val splitByState = remember {
        mutableStateOf(1)
    }
    val range = IntRange(start = 1, endInclusive = 100)

    val tipAmountState = remember {
        mutableStateOf(0.0)
    }

    val totalperPersonState = remember {
        mutableStateOf(0.0)
    }
    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()){
        TopHeader(totalPerPerson = totalperPersonState.value)

        Surface(modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth(),
            shape = RoundedCornerShape(corner = CornerSize(8.dp)),
            border = BorderStroke(width = 1.dp, color = Color.LightGray)
        ) {

            Column(modifier = Modifier.padding(6.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start){
                InputField(valueState = totalBillState ,
                    labelId ="Insira a conta" ,
                    enabled = true,
                    onAction = KeyboardActions {
                        if(!validState) return@KeyboardActions
                        onValchange(totalBillState.value.trim())

                        keyboardController?.hide()
                    })

                if (validState) {
                    Row (modifier = Modifier.padding(3.dp),
                        horizontalArrangement = Arrangement.Start){
                        Text(text = "Dividir",
                            modifier = Modifier.align(
                                alignment = Alignment.CenterVertically
                            ))
                        Spacer(modifier = Modifier.width(120.dp))
                        Row (modifier = Modifier
                            .padding(horizontal = 3.dp),
                            horizontalArrangement = Arrangement.End){

                            RounIconButton(
                                imagemVector = Icons.Default.Remove,
                                onClick = {
                                    splitByState.value =
                                        if(splitByState.value > 1) splitByState.value - 1
                                    else 1
                                    totalperPersonState.value =
                                        calculateTotalPerPerson(totalBill = totalBillState.value.toDouble(),
                                            splitBy = splitByState.value,
                                            tipPercentage = tipPercentage)
                                })

                            Text(text = "${splitByState.value}",
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .padding(start = 9.dp, end = 9.dp)
                            )

                            RounIconButton(
                                imagemVector = Icons.Default.Add,
                                onClick = {
                                    if(splitByState.value < range.last) {
                                        splitByState.value = splitByState.value + 1
                                        totalperPersonState.value =
                                            calculateTotalPerPerson(totalBill = totalBillState.value.toDouble(),
                                                splitBy = splitByState.value,
                                                tipPercentage = tipPercentage)
                                    }})

                        }
                    }

                    //Tip Row
                    Row(modifier = Modifier
                        .padding(horizontal = 3.dp,
                            vertical = 12.dp)){
                        Text(text = "Gorgeja",
                            modifier = Modifier.align(
                                alignment = Alignment.CenterVertically))
                        Spacer(modifier = Modifier.width(200.dp))

                        Text(text = "$ ${tipAmountState.value}")
                    }
                    Column(verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "$tipPercentage %")
                        Spacer(modifier = Modifier.height(14.dp))

                        //Slinder
                        Slider(value = slinderPositionState.value,
                            onValueChange = {
                                    newVal ->
                                slinderPositionState.value = newVal
                                tipAmountState.value =
                                    calculateTotalTip(totalBill = totalBillState.value.toDouble(),
                                        tipPercentage)
                                totalperPersonState.value =
                                    calculateTotalPerPerson(totalBill = totalBillState.value.toDouble(),
                                        splitBy = splitByState.value,
                                        tipPercentage = tipPercentage)


                            },
                            modifier = Modifier.padding(start = 16.dp,
                                end = 16.dp),
                        )
                    }
                }else {
                    Box() {}
                }
            }

        }
    }

}




@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JetTipTheme {
        MyApp {

        }
    }
}