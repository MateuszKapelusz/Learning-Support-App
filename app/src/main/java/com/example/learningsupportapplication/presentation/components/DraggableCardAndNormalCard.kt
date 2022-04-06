package com.example.learningsupportapplication.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs

enum class SwipeResult {
    ACCEPTED,
    REJECTED
}

@Composable
fun DraggableCardAndNormalCard(
    item: Any,
    modifier: Modifier = Modifier,
    check: MutableState<Boolean>,
    onButtonClick: () -> Unit,
    onSwiped:  (Any,Any) -> Unit,
    content1: @Composable () -> Unit,
    content2: @Composable () -> Unit,
) {

    when (check.value) {

        false -> {
            DraggableCard(
                item = item,
                modifier = modifier,
                check = check,
                //onSwiped = onSwiped,
                content = content1
            )
        }
        true -> {
            NormalCard(
                item = item,
                check = check,
                modifier = modifier,
                onButtonClick = { onButtonClick() },
                onClick = onSwiped,
                content2 = content2
            )

        }
    }

}

@Composable
fun NormalCard(
    item: Any,
    modifier: Modifier = Modifier,
    check: MutableState<Boolean>,
    onButtonClick: () -> Unit,
    onClick: (Any,Any) -> Unit,
    content2: @Composable () -> Unit,
) {
    Card(
        elevation = 16.dp,
        modifier = modifier
    ) {

        content2()
    }
    Button(onClick = { onButtonClick() }) {

    }
    onClick(check,item)
}

@Composable
fun DraggableCard(
    item: Any,
    modifier: Modifier = Modifier,
    check: MutableState<Boolean>,
    //onSwiped: (Any,Any) -> Unit,
    content: @Composable () -> Unit,

    ) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val swipeXLeft = -(screenWidth.value * 3.2).toFloat()
    val swipeXRight = (screenWidth.value * 3.2).toFloat()
    val swipeYTop = -1000f
    val swipeYBottom = 1000f
    val swipeX = remember { Animatable(0f) }
    val swipeY = remember { Animatable(0f) }
    swipeX.updateBounds(swipeXLeft, swipeXRight)
    swipeY.updateBounds(swipeYTop, swipeYBottom)


    if (abs(swipeX.value) < swipeXRight - 50f) {
        val rotationFraction = (swipeX.value / 60).coerceIn(-40f, 40f)
        Card(
            elevation = 16.dp,
            modifier = modifier
                .dragContent(
                    swipeX = swipeX,
                    swipeY = swipeY,
                    maxX = swipeXRight,
                    onSwiped = { _, _ -> }
                )
                .graphicsLayer(
                    translationX = swipeX.value,
                    translationY = swipeY.value,
                    rotationZ = rotationFraction,
                )
                .clip(RoundedCornerShape(16.dp))
        ) {
            content()
        }
    } else {

        val swipeResult =
            if (swipeX.value > 0) {
                SwipeResult.ACCEPTED
                check.value = true

            } else {
                SwipeResult.REJECTED
                check.value = true

            }
      //  onSwiped(swipeResult, item)

    }

}


fun Modifier.dragContent(
    swipeX: Animatable<Float, AnimationVector1D>,
    swipeY: Animatable<Float, AnimationVector1D>,
    maxX: Float,
    onSwiped: (Any, Any) -> Unit
): Modifier = composed {
    val coroutineScope = rememberCoroutineScope()
    pointerInput(Unit) {
        this.detectDragGestures(
            onDragCancel = {
                coroutineScope.apply {
                    launch { swipeX.animateTo(0f) }
                    launch { swipeY.animateTo(0f) }
                }
            },
            onDragEnd = {
                coroutineScope.apply {
                    if (abs(swipeX.targetValue) < abs(maxX) / 4) {
                        launch {
                            swipeX.animateTo(0f, tween(400))
                        }
                        launch {
                            swipeY.animateTo(0f, tween(400))
                        }
                    } else {
                        launch {
                            if (swipeX.targetValue > 0) {
                                swipeX.animateTo(maxX, tween(400))
                            } else {
                                swipeX.animateTo(-maxX, tween(400))
                            }
                        }
                    }
                }
            }
        ) { change, dragAmount ->
            change.consumePositionChange()
            coroutineScope.apply {
                launch { swipeX.animateTo(swipeX.targetValue + dragAmount.x) }
                launch { swipeY.animateTo(swipeY.targetValue + dragAmount.y) }
            }
        }
    }
}