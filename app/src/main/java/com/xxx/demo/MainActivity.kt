package com.xxx.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.xxx.demo.game.Game

import com.xxx.demo.theme.ComposeDemoTheme
import com.xxx.demo.keep.FakeKeep

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeDemoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ParentView()
                }
            }
        }
    }
}

// 这是 Kotlin 插件的问题，可以正常编译运行
val pages: List<Pair<String, @Composable ()->Unit>> =
    arrayListOf(
        "2048小游戏" to { Game() },
        "高仿Keep周界面（自定义绘制）" to { FakeKeep() },

//        "自定义布局（3）：固有特性测量（最大）" to {
//            val text = arrayOf("Funny","Salty","Fish","is","Very","Salty")
//            VerticalLayoutWithIntrinsic(
//                Modifier
//                    .width(IntrinsicSize.Max)
//                    .padding(12.dp)
//                    .background(MaterialColors.Yellow100)) {
//                text.forEach {
//                    Text(text = it, fontSize = 24.sp)
//                }
//            }
//        },

    )


@OptIn(ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)
@Composable
fun ParentView() {
    var content: (@Composable () -> Unit)? by remember {
        mutableStateOf(null)
    }
    AnimatedContent(
        targetState = content, Modifier.fillMaxSize(),
        transitionSpec = {
            slideIntoContainer(
                AnimatedContentScope.SlideDirection.Right,
                tween(500)
            ) with fadeOut() + slideOutOfContainer(
                AnimatedContentScope.SlideDirection.Left,
                tween(500)
            )
        },
    ) {
        when (it) {
            null -> LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                // 整体内边距
                contentPadding = PaddingValues(8.dp, 8.dp),
                // item 和 item 之间的纵向间距
                verticalArrangement = Arrangement.spacedBy(4.dp),
                // item 和 item 之间的横向间距
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(pages, key = { _, p -> p.first }) { i, pair ->
                    Card(
                        modifier = Modifier.clickable { content = pair.second },
                        shape = RoundedCornerShape(4.dp),
                      //  backgroundColor = rememberRandomColor().copy(0.8f)
                    ) {
                        Text(
                            text = pair.first, modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(CenterHorizontally)
                                .padding(16.dp), color = Color.Blue
                        )
                    }
                }
            }
            else -> Box(
                Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                BackHandler {
                    content = null
                }
                it()
            }
        }
    }
}