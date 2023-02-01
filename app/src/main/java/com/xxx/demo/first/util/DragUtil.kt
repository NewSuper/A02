package com.xxx.demo.first.util

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput

@Composable
internal fun rememberDragOffset() = remember {
    mutableStateOf(Offset(0f,0f))
}

internal fun Modifier.dragDetector(
    dragOffset: MutableState<Offset>,
    onDragFinished: (Offset) -> Unit,
) = pointerInput(Unit) {
    detectDragGestures(
        onDragStart = { dragOffset.value = Offset(0f, 0f) },
        onDragEnd = { onDragFinished(dragOffset.value) })
    { change, dragAmount ->
        dragOffset.value += Offset(dragAmount.x, dragAmount.y)
    }
}