package com.xxx.demo.game

import GameOverDialog
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xxx.demo.game.util.dragDetector
import com.xxx.demo.game.util.rememberDragOffset

private val EMPTY_CRLL_ARR = (0 until 4).map { arrayOf(0, 0, 0, 0).toMutableList() }
private val CELL_WITH = 85.dp
private val CELL_PADDING = 2.dp

@Composable
fun Game() {
    var isGameOver by remember { mutableStateOf(false) }
    val cellArr by remember { mutableStateOf(EMPTY_CRLL_ARR) }
    var transparentText by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .dragDetector(dragOffset = rememberDragOffset(),
                onDragFinished = {
                    actionOnMove(it, cellArr)
                    //修改transparentText 触发页面更新
                    transparentText = System
                        .currentTimeMillis()
                        .toString()
                    if (!canMove(cellArr)) {
                        isGameOver = true
                    }
                })
            .padding(10.dp)
    ) {
        Text(
            "2048", fontSize = 40.sp, modifier = Modifier.padding(
                bottom = 20.dp
            )
        )
        Text(transparentText, color = Color.White)//存在意义：每次要更新游戏界面时，触发该控件更新倒到更新整个游戏界面的目的
        CellBoard(cellArr)
        if (isGameOver) {
            GameOverDialog(title = "游戏结束", onConfirmListener = {
                isGameOver = false
                startGame(cellArr)
            },
                onDismissListener = {
                    isGameOver = false
                })
        }
    }
}


private fun actionOnMove(it: Offset, cellArr: List<MutableList<Int>>) {
    if (it.y < -100) {
        moveUp(cellArr)
    } else if (it.y > 100) {
        moveDown(cellArr)
    } else if (it.x < -100) {
        moveLeft(cellArr)
    } else if (it.x > 100) {
        moveRight(cellArr)
    }
}

@Composable
private fun CellBoard(cellArr: List<MutableList<Int>>) {
    Column(
        modifier = Modifier
            .background(color = Color(0xffbbada0))
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        for (i in 0 until 4) {
            Row(modifier = Modifier.align(alignment = Alignment.CenterHorizontally)) {
                for (j in 0 until 4) {
                    CellView(cellArr[i][j])
                }
            }
        }
    }
}

@Composable
private fun CellView(cellValue: Int) {
    var cellValueStr = ""
    if (cellValue > 0) {
        cellValueStr = cellValue.toString()
    }
    Text(
        text = cellValueStr,
        fontSize = 30.sp,
        modifier = Modifier
            .padding(CELL_PADDING)
            .width(CELL_WITH)
            .height(CELL_WITH)
            .background(
                color = Color(getCellBackgroundColor(cellValue)),
                shape = RoundedCornerShape(4.dp)
            )
            .wrapContentSize(),
    )
}

private fun getCellBackgroundColor(cellValue: Int): Long {
    when (cellValue) {
        2 -> return 0XFFEEE4DA
        4 -> return 0XFFEDE0C8
        8 -> return 0XFFF26179
        16 -> return 0XFFF59563
        32 -> return 0XFFF67C5F
        64 -> return 0XFFF65E36
        128 -> return 0XFFEDCF72
        256 -> return 0XFFEDCC61
        512 -> return 0XFF90C000
        1024 -> return 0XFF3365A5
        2048 -> return 0XFF90C000
        4096 -> return 0XFF60B0C0
        8192 -> return 0XFF9030C0
        else -> return 0XFFCDC1B4
    }
}