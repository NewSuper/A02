package com.xxx.demo.keep

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.xxx.demo.R
import androidx.compose.foundation.Canvas
import java.lang.Integer.min
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text

import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb

import androidx.compose.ui.text.ExperimentalTextApi

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

@Composable
fun FakeKeep() {
    val times = arrayListOf("3/1-3/6","3/7-3/13","3/14-3/20","3/21-3/27","3/28-4/3","4/4-4/10","4/11-4/17","4/18-4/24","4/25-5/1","5/2-5/8","5/9-5/15","5/16-5/22", "本周")
    val nums = arrayListOf(0, 0, 134, 119, 90, 93, 142, 101, 145, 114, 131, 125, 138)
    // times.map { Random.nextInt(90,150) }
    //  println(nums)

    val listData = List(times.size) { i ->
        ItemData(times[i], nums[i])
    }

    var startIndex by remember {
        mutableStateOf(0)
    }

    val centerData = listData[startIndex + 2]

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {
        Image(painter = painterResource(id = R.drawable.part_tab), contentDescription = "", modifier = Modifier.fillMaxWidth(), contentScale = ContentScale.FillWidth)
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "时长", color = Color.Gray, fontSize = 14.sp)
            Text(text = centerData.content.replace("/","月").replace("-","日至")+"日", color = Color.Gray, fontSize = 14.sp)
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp), verticalAlignment = Alignment.Bottom) {
            Text(text = "${centerData.num}", color = Color.Black, fontSize = 48.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.alignByBaseline())
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "分钟", color = Color.Gray, fontSize = 14.sp, modifier = Modifier.alignByBaseline())
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Column(Modifier.weight(0.3f)) {
                Text(text = "消耗(千卡)", color = Color.Gray, fontSize = 12.sp)
                Text(text = "${(centerData.num* Random.nextDouble(1.4,1.6)).toInt()}", color = Color.Black, fontSize = 24.sp, fontWeight = FontWeight.W600)
            }
            val time = Random.nextInt(3,6)
            Column(Modifier.weight(0.3f)) {
                Text(text = "完成(次)", color = Color.Gray, fontSize = 12.sp)
                Text(text = "$time", color = Color.Black, fontSize = 24.sp, fontWeight = FontWeight.W600)
            }
            Column(Modifier.weight(0.3f)) {
                Text(text = "累计(天)", color = Color.Gray, fontSize = 12.sp)
                Text(text = "${Random.nextInt(3, time+1)}", color = Color.Black, fontSize = 24.sp, fontWeight = FontWeight.W600)
            }
            Spacer(modifier = Modifier.weight(0.1f))
        }
        Statistics(modifier = Modifier
            .fillMaxWidth()
            .height(270.dp)
            .background(Color(250, 250, 250)), listData = listData, startIndex = startIndex)
        Spacer(modifier = Modifier.height(50.dp))
        Button(onClick = {
            startIndex += 1
            if (startIndex == listData.size - 2) startIndex = 0
        }) {
            Text(text = "移动")
        }
    }

}

/**
 * 数据统计图
 * @param modifier Modifier
 * @param listData List<ItemData> 数据
 * @param startIndex Int 最左侧的矩阵对应的index
 */
@Composable
fun Statistics(
    modifier: Modifier,
    listData: List<ItemData>,
    startIndex : Int = 0,
) {
    // 最大的数字，所有矩形以这个为基准计算高度
    val maxNum = listData.maxOf { it.num }
    // 深浅两种绿色
    val lightColor = Color(144, 225, 193)
    val highlightColor = Color(36, 198, 138)
    // 这个rect用于存放paint测出的文字大小
    val rect = remember {
        android.graphics.Rect()
    }
    // 这个paint用于绘制文字
    val paint = remember {
        android.graphics.Paint().apply {
            isAntiAlias = true
            color = android.graphics.Color.GRAY
            textSize = 32f
        }
    }

    Canvas(modifier = modifier){
        // 画布的宽高
        val w = size.width
        val h = size.height
        // 最高的矩形占的高度（3/6)
        val maxH = h / 2
        // 举行的宽度
        val blockW = w / 12f

        for (i in 0 until min(5, listData.size - startIndex)){
            val data = listData[startIndex + i]
            if (i != 2){
                // 画四个浅色矩形
                val blockH = data.num.toFloat() / maxNum * maxH
                drawRect(lightColor, Offset(w / 4f * i - blockW / 2, 0.833f * h - blockH), Size(blockW, blockH))
                drawLine(Color.LightGray, Offset(w / 4f * i, h * 11 / 12), Offset(w / 4f * i, h * 11 / 12 + 16f), 4f)
                // 浅色矩形下方的文字
                paint.color = Color.Gray.toArgb()
                drawIntoCanvas {
                    FunnyCanvasUtils.drawCenterText(it.nativeCanvas, data.content, w / 4f * i , h * 7 / 8 + 2f,  rect, paint)
                }
            }
        }
        // 三条浅色横线
        for (i in 2 until 5){
            drawLine(Color.Black.copy(alpha = 0.5f), Offset(0f, h * i / 6), Offset(w, h * i / 6), pathEffect = PathEffect.dashPathEffect(
                floatArrayOf(10f,10f)))
        }
        // 深色横线
        drawLine(highlightColor, Offset(0f, h * 5 / 6), Offset(w, h * 5 / 6),  4f)
        // 中间深色矩形
        val blockH = listData[startIndex + 2].num.toFloat() / maxNum * maxH
        drawRect(highlightColor, Offset(w / 2 - blockW / 2, 0.833f * h - blockH), Size(blockW, blockH))
        drawLine(highlightColor, Offset(w/2, 20f), Offset(w / 2, 0.833f * h - blockH), 4f)
        // 显示文本的圆角矩形
        drawRoundRect(Color(35,199,136), Offset(w / 2 - blockW * 2 / 3 - 4f, h / 8), Size(blockW * 4f / 3 + 8f, h / 12f), CornerRadius(12f, 12f))
        drawLine(highlightColor, Offset(w / 2f, h * 11 / 12), Offset(w / 2f, h - 20f), 4f)

        // 中间底部文字
        paint.color = Color.Black.toArgb()
        drawIntoCanvas {
            FunnyCanvasUtils.drawCenterText(it.nativeCanvas, listData[startIndex+2].content, w / 2 , h * 7 / 8 + 2f,  rect, paint)
        }

        // 圆角矩形内部文字
        paint.color = Color.White.toArgb()
        drawIntoCanvas {
            FunnyCanvasUtils.drawCenterText(it.nativeCanvas, "${listData[startIndex+2].num} 分钟", w / 2, h / 8 + h / 24f,  rect, paint)
        }
    }
}

data class ItemData(val content :String, val num : Int)