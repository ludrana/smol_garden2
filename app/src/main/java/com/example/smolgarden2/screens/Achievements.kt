package com.example.smolgarden2.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
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
import androidx.compose.ui.res.colorResource
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
import com.example.smolgarden2.flowerList
import com.example.smolgarden2.plantCount
import com.example.smolgarden2.totalCoins
import com.example.smolgarden2.writeUserData
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Achievements(navController: NavController) {
    val text = remember { mutableStateOf(true) }
    val scroll = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val drawerScope = rememberCoroutineScope()
    val snackbarAppState: SnackbarAppState = rememberSnackbarAppState()
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
                                navController.navigate(Routes.Home.route) {popUpTo(Routes.Home.route) {inclusive = true} }
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
                                navController.navigate(Routes.Shop.route) {popUpTo(Routes.Home.route) {inclusive = false} }
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
                                navController.navigate(Routes.Stats.route) {popUpTo(Routes.Home.route) {inclusive = false} }
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
                                    "Achievements",
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
                        )
                    },
                ) { innerPadding ->
                    LazyColumn(
                        modifier = Modifier
                            .padding(innerPadding)
                            .background(Color.Transparent),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        items(flowerList.size) { index ->
                            for (j in 0..2) {
                                AchievementItemFlowers(tier = j, index = index)
                            }
                        }
                        items(3) {
                            AchievementItemCoins(tier = it)
                        }
                    }
                }
            }
        }
    }
}

val tierColors = listOf(R.color.bronze, R.color.silver, R.color.gold)
val tierCountF = listOf(1, 6, 13)
val tierCountC = listOf(69, 420, 5000)

@Composable
fun AchievementItemFlowers(tier: Int, index: Int) {
    if (plantCount[index] >= tierCountF[tier]) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = flowerList[index].fullyGrown),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(50.dp)
                    .border(BorderStroke(4.dp, colorResource(id = tierColors[tier])))
                    .padding(5.dp)
            )
            Text(
                text = "Sell ${flowerList[index].name} * ${tierCountF[tier]}",
                fontSize = 30.sp,
                color = Color(0xff220142),
                modifier = Modifier
                    .padding(10.dp)
            )
        }
    }
}

@Composable
fun AchievementItemCoins(tier: Int) {
    if (totalCoins >= tierCountC[tier]) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.coin),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(50.dp)
                    .border(BorderStroke(4.dp, colorResource(id = tierColors[tier])))
                    .padding(5.dp)
            )
            Text(
                text = "Gain ${tierCountC[tier]} coins",
                fontSize = 30.sp,
                color = Color(0xff220142),
                modifier = Modifier
                    .padding(10.dp)
            )
        }
    }
}