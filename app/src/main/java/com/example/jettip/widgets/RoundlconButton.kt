package com.example.jettip.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


val IconbuttonSizeModifier = Modifier.size(40.dp)
@Composable
fun RounIconButton(
    modifier: Modifier = Modifier,
    imagemVector: ImageVector,
    onClick: () -> Unit,
    tint: Color = Color.Black.copy(alpha = 0.8f),
    backgroundColor: Color = Color.White,
    elevation: Dp = 4.dp
){
    ElevatedCard (modifier = modifier
        .padding(all = 4.dp)
        .clickable { onClick.invoke() }
        .then(IconbuttonSizeModifier),
           shape = CircleShape,
           colors = CardDefaults.cardColors(backgroundColor),
        elevation = CardDefaults.cardElevation(elevation)) {
            Icon(modifier = Modifier.padding(10.dp),
                imageVector = imagemVector,
                contentDescription = "Plus or minus icon",
                tint = tint)



       }

}