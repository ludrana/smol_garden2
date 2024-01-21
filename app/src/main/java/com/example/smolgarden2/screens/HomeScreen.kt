package com.example.smolgarden2.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.smolgarden2.R
import com.example.smolgarden2.Routes
import com.example.smolgarden2.boxOption
import com.example.smolgarden2.coinCount
import com.example.smolgarden2.flowerList
import com.example.smolgarden2.growthState
import com.example.smolgarden2.plantCount
import com.example.smolgarden2.plantId
import com.example.smolgarden2.seedOption
import com.example.smolgarden2.totalCoins
import com.example.smolgarden2.writeUserData
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Timer
import kotlin.concurrent.timerTask

class SnackbarAppState(
    val snackbarHostState: SnackbarHostState,
    val snackbarScope: CoroutineScope
) {
    fun showSnackbar(message: String, duration: SnackbarDuration = SnackbarDuration.Short) {
        snackbarScope.launch {
            snackbarHostState.showSnackbar(
                message = message,
                duration = duration
            )
        }
    }
}

@Composable
fun rememberSnackbarAppState(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    snackbarScope: CoroutineScope = rememberCoroutineScope()
) = remember(snackbarHostState, snackbarScope) {
    SnackbarAppState(
        snackbarHostState = snackbarHostState,
        snackbarScope = snackbarScope
    )
}


val onSeedChange = { change: Int ->
    seedOption = change
}
val onBoxChange = { change: Int ->
    boxOption = change
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val text = remember { mutableStateOf(true) }
    val scroll = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val drawerScope = rememberCoroutineScope()
    val snackbarAppState: SnackbarAppState = rememberSnackbarAppState()
    val lazyListState: LazyListState = rememberLazyListState()
    key(text.value.toString()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    painterResource(id = R.drawable.blur),
                    contentScale = ContentScale.FillBounds
                ),
        ) {
            ModalNavigationDrawer(
                drawerContent = {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Menu",
                            modifier = Modifier
                                .padding(15.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontSize = 50.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xffe17100)
                        )
                        ClickableText(
                            AnnotatedString("Garden"),
                            style = TextStyle(
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Bold,
                                textDecoration = TextDecoration.Underline,
                                color = Color(0xffffaa00)
                            ),
                            modifier = Modifier
                                .padding(10.dp),
                            onClick = {
                                drawerScope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }
                        )
                        ClickableText(
                            AnnotatedString("Shop"),
                            style = TextStyle(
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Bold,
                                textDecoration = TextDecoration.Underline,
                                color = Color(0xffeabf00)
                            ),
                            modifier = Modifier
                                .padding(10.dp),
                            onClick = {
                                navController.navigate(Routes.Shop.route)
                                drawerScope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }
                        )
                        ClickableText(
                            AnnotatedString("Achievements"),
                            style = TextStyle(
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Bold,
                                textDecoration = TextDecoration.Underline,
                                color = Color(0xffeabf00)
                            ),
                            modifier = Modifier
                                .padding(10.dp),
                            onClick = {
                                navController.navigate(Routes.Achievements.route)
                                drawerScope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }
                        )
                        ClickableText(
                            AnnotatedString("Stats"),
                            style = TextStyle(
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Bold,
                                textDecoration = TextDecoration.Underline,
                                color = Color(0xffeabf00)
                            ),
                            modifier = Modifier
                                .padding(10.dp),
                            onClick = {
                                navController.navigate(Routes.Stats.route)
                                drawerScope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        ClickableText(
                            AnnotatedString("Log out"),
                            style = TextStyle(
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Bold,
                                textDecoration = TextDecoration.Underline,
                                color = Color(0xffeabf00)
                            ),
                            modifier = Modifier
                                .padding(10.dp),
                            onClick = {
                                writeUserData()
                                Firebase.auth.signOut()
                                navController.navigate(Routes.Settings.route) {popUpTo(Routes.Home.route) {inclusive = true} }
                            }
                        )
                    }
                },
                drawerState = drawerState,
                scrimColor = Color.Cyan
            ) {
                Scaffold(
                    snackbarHost = {
                        SnackbarHost(hostState = snackbarAppState.snackbarHostState)
                    },
                    containerColor = Color.Transparent,
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    "Smol Garden 2",
                                    fontSize = 35.sp,
                                    color = Color(0xff000d5e),
                                    fontFamily = FontFamily.Cursive
                                )
                            },
                            scrollBehavior = scroll,
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color(0xff23fc6f)
                            ),
                            navigationIcon = {
                                IconButton(onClick = {
                                    drawerScope.launch {
                                        drawerState.apply {
                                            if (isClosed) open() else close()
                                        }
                                    }
                                }) {
                                    Icon(Icons.Filled.Menu, contentDescription = "Меню", tint = Color(0xff000d5e))
                                }
                            },
                            actions = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.coin),
                                        contentDescription = "coin",
                                        modifier = Modifier.size(33.dp),
                                        contentScale = ContentScale.Fit
                                    )
                                    Text(
                                        text = coinCount.toString(),
                                        fontSize = 30.sp,
                                        color = Color(0xffeabf00),
                                        modifier = Modifier.padding(5.dp),
                                        fontFamily = FontFamily.Cursive
                                    )
                                }
                            }
                        )
                    },
                    bottomBar = {
                        BottomAppBar(
                            modifier = Modifier.height(120.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(flowerList[seedOption].name,
                                    fontSize = 30.sp,
                                    color = Color.Magenta,
                                    modifier = Modifier.padding(5.dp),
                                    fontFamily = FontFamily.Cursive
                                )
                                LazyRow(
                                    state = lazyListState,
                                    modifier = Modifier.fillMaxSize(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    for (index in flowerList.indices) {
                                        item { BottomItem(id = index) }
                                    }
                                }
                            }
                        }
                    },
                    floatingActionButton = {
                        FloatingActionButton(onClick = {
                            if (plantId[boxOption] == -1) {
                                plantId[boxOption] = seedOption
                                val time = System.currentTimeMillis()
                                growthState[boxOption] = time + flowerList[seedOption].growTime
                                text.value = !text.value
                            }
                        }) {
                            Icon(Icons.Default.Add, contentDescription = "Add")
                        }
                    },
                    floatingActionButtonPosition = FabPosition.Center
                ) { innerPadding ->
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier
                            .padding(innerPadding)
                            .background(Color.Transparent)
                            .offset(0.dp, 150.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        items(plantId.size) { index ->
                            GardenItem(index) { message, duration ->
                                snackbarAppState.showSnackbar(
                                    message = message,
                                    duration = duration
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GardenItem(index: Int, showSnackbar: (String, SnackbarDuration) -> Unit) {
    val text = remember { mutableStateOf(true) }
    key(text.value.toString()) {
        if ((growthState[index] != "0".toLong()) and (growthState[index] >= System.currentTimeMillis())) {
            val timer = Timer()
            val task = timerTask {
                text.value = !text.value
            }
            timer.schedule(task, Date(growthState[index]))
        }
        Box(modifier = Modifier
            .size(100.dp)
            .background(
                if (index == boxOption) {
                    Color(0xfff1baff)
                } else {
                    Color.Transparent
                }
            )
            .padding(0.dp, 10.dp)
            .clickable {
                if ((growthState[index] != "0".toLong()) and (growthState[index] <= System.currentTimeMillis())) {
                    growthState[index] = "0".toLong()
                    coinCount += flowerList[plantId[index]].sellPrice
                    totalCoins += flowerList[plantId[index]].sellPrice
                    plantCount[plantId[index]]++
                    showSnackbar(
                        "+${flowerList[plantId[index]].sellPrice} coins",
                        SnackbarDuration.Short
                    )
                    plantId[index] = -1
                    text.value = !text.value
                }
                onBoxChange(index)
            }
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp, 20.dp)
                    .align(Alignment.BottomCenter)
                    .background(Color.Black)
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .paint(
                        painterResource(id = getFlowerImg(index)),
                        contentScale = ContentScale.Fit
                    )
            )
        }
    }
}

fun getFlowerImg(boxIndex: Int): Int {
    return if (plantId[boxIndex] == -1) {
        R.drawable.emptypot
    }
    else {
        val flowerIndex = plantId[boxIndex]
        if (growthState[boxIndex] <= System.currentTimeMillis()) {
            flowerList[flowerIndex].fullyGrown
        }
        else {
            flowerList[flowerIndex].growingState
        }
    }
}

@Composable
fun BottomItem(id: Int) {
    Box(
        modifier = Modifier
            .padding(5.dp)
            .size(60.dp)
            .clickable(
                enabled = flowerList[id].isBought
            ) {
                onSeedChange(id)
            }
            .background(
                if (!flowerList[id].isBought) {
                    Color.LightGray
                } else if (id == seedOption) {
                    Color(0xfff1baff)
                } else {
                    Color.Transparent
                }
            )
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = flowerList[id].seed),
            contentDescription = null,
            contentScale = ContentScale.Fit
        )
    }
}