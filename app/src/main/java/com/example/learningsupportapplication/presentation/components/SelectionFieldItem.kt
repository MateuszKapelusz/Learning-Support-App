package com.example.learningsupportapplication.presentation.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.learningsupportapplication.ui.theme.BORDER_SIZE
import com.example.learningsupportapplication.ui.theme.LARGE_PADDING
import com.example.learningsupportapplication.ui.theme.SMALL_PADDING

@Composable
fun SelectionFieldItem(
    titleText: String,
    buttonName: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .width(322.dp)
            .height(96.dp),
        shape = RoundedCornerShape(LARGE_PADDING),
        border = BorderStroke(BORDER_SIZE, Color.Black),

        ) {
        Row(
            modifier = Modifier
                .padding(all = SMALL_PADDING),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            Text(
                modifier = Modifier
                    .padding(start = SMALL_PADDING),
                text = titleText,
                fontSize = MaterialTheme.typography.subtitle1.fontSize,
                fontWeight = FontWeight.Bold

            )

            Button(
                modifier = Modifier
                    .width(95.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                shape = RoundedCornerShape(SMALL_PADDING),
                border = BorderStroke(BORDER_SIZE, Color.Black),
                onClick = { onClick() }
            ) {
                Text(
                    text = buttonName,
                    color = Color.Black
                )
            }
        }
    }
}

@Preview
@Composable
fun SelectionFieldPreview() {
    SelectionFieldItem("text", "test", onClick = {})
}