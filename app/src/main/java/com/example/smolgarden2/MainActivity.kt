package com.example.smolgarden2

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.smolgarden2.screens.Achievements
import com.example.smolgarden2.screens.HomeScreen
import com.example.smolgarden2.screens.LoginForm
import com.example.smolgarden2.screens.Settings
import com.example.smolgarden2.screens.Shop
import com.example.smolgarden2.screens.SignupForm
import com.example.smolgarden2.screens.Stats
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        if (auth.currentUser != null) {
            readUserData()
        }
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "splash") {
                composable("splash") {
                    val currentUser = Firebase.auth.currentUser
                    if (currentUser != null) {
                        navController.navigate(Routes.Home.route)
                    } else {
                        navController.navigate(Routes.Settings.route)
                    }
                }
                composable(Routes.Home.route) {
                    HomeScreen(navController = navController)
                }
                composable(Routes.Shop.route) {
                    Shop(navController = navController)
                }
                composable(Routes.Settings.route) {
                    Settings(navController = navController)
                }
                composable(Routes.SignUp.route) {
                    SignupForm(navController = navController)
                }
                composable(Routes.SignIn.route) {
                    LoginForm(navController = navController)
                }
                composable(Routes.Achievements.route) {
                    Achievements(navController = navController)
                }
                composable(Routes.Stats.route) {
                    Stats(navController = navController)
                }
            }
        }
    }

    override fun onStop() {
        if (auth.currentUser != null) {
            writeUserData()
        }
        super.onStop()
    }
}

sealed class Routes(val route: String) {
    data object Home : Routes("home")
    data object Shop : Routes("shop")
    data object Settings : Routes("settings")
    data object SignUp : Routes("signup")
    data object SignIn : Routes("signin")
    data object Achievements : Routes("achv")
    data object Stats : Routes("stats")
}

var coinCount by mutableLongStateOf("0".toLong())
val flowerList = listOf(Flowers.Gardenia, Flowers.Hydrangea, Flowers.Violet, Flowers.Chamomile, Flowers.LilyOfTheAlley,
    Flowers.Lily, Flowers.Gladiolus, Flowers.Petunia, Flowers.Rose, Flowers.Clover, Flowers.Peony, Flowers.Orchid)
var boxOption by mutableIntStateOf(0)
var seedOption by mutableIntStateOf(0)
var plantId = MutableList(6) { -1 }
var growthState = MutableList(6) { "0".toLong() }
var plantCount = MutableList(flowerList.size) { "0".toLong() }
var totalCoins by mutableLongStateOf("0".toLong())

sealed class Flowers(
    val name: String,
    val cost: Int,
    val sellPrice: Int,
    val growTime: Long,
    val growingState: Int = R.drawable.growingpot,
    val fullyGrown: Int,
    val seed: Int,
    var isBought: Boolean = false
) {
    data object Gardenia: Flowers("Gardenia", 0, 5, growTime = 1500,fullyGrown = R.drawable.gardenia, seed = R.drawable.gardeniaseed, isBought = true)
    data object Hydrangea: Flowers("Hydrangea", 10, 10, growTime = 2500,fullyGrown = R.drawable.hydrangea, seed = R.drawable.hydrangeaseed)
    data object Violet: Flowers("Violet", 20, 30, growTime = 3500,fullyGrown = R.drawable.violet, seed = R.drawable.violetseed)
    data object Chamomile: Flowers("Chamomile", 50, 45, growTime = 6500,fullyGrown = R.drawable.chamomile, seed = R.drawable.chamomileseed)
    data object LilyOfTheAlley: Flowers("Lily of the Alley", 100, 70, growTime = 10000,fullyGrown = R.drawable.lilyota, seed = R.drawable.lilyotaseed)
    data object Lily: Flowers("Lily", 250, 100, growTime = 12500,fullyGrown = R.drawable.lily, seed = R.drawable.lilyseed)
    data object Gladiolus: Flowers("Gladiolus", 300, 150, growTime = 15000,fullyGrown = R.drawable.gladiolus, seed = R.drawable.gladiolusseed)
    data object Petunia: Flowers("Petunia", 500, 300, growTime = 17500,fullyGrown = R.drawable.petunia, seed = R.drawable.petuniaseed)
    data object Rose: Flowers("Rose", 1000, 400, growTime = 20000,fullyGrown = R.drawable.rose, seed = R.drawable.roseseed)
    data object Clover: Flowers("Clover", 1250, 450, growTime = 22500,fullyGrown = R.drawable.clover, seed = R.drawable.cloverseed)
    data object Peony: Flowers("Peony", 1500, 500, growTime = 25000,fullyGrown = R.drawable.peony, seed = R.drawable.peonyseed)
    data object Orchid: Flowers("Orchid", 2000, 1000, growTime = 30000,fullyGrown = R.drawable.orchid, seed = R.drawable.orchidseed)
}

fun writeUserData() {
    val db = Firebase.firestore
    val user = Firebase.auth.currentUser

    val bought = mutableListOf<Boolean>()
    flowerList.forEach {
        bought.add(it.isBought)
    }
    val data = hashMapOf(
        "coins" to coinCount,
        "bought" to bought.toList(),
        "plants" to plantId.toList(),
        "state" to growthState.toList(),
        "plantcount" to plantCount.toList(),
        "total" to totalCoins
    )

    db.collection("users").document(user?.uid!!)
        .set(data)
        .addOnSuccessListener { Log.d("writeData", "DocumentSnapshot successfully written!") }
        .addOnFailureListener { e -> Log.w("writeData", "Error writing document", e) }
}

fun readUserData() { // todo coroutine?
    val db = Firebase.firestore
    val user = Firebase.auth.currentUser

    val docRef = db.collection("users").document(user?.uid!!)
    docRef.get()
        .addOnSuccessListener { document ->
            if (document != null) {
                Log.d("readData", "DocumentSnapshot data: ${document.data}")
                coinCount = document.data?.get("coins") as Long
                totalCoins = document.data?.get("total") as Long
                plantId = (document.data?.get("plants") as List<Int>).toMutableList()
                growthState = (document.data?.get("state") as List<Long>).toMutableList()
                plantCount = (document.data?.get("plantcount") as List<Long>).toMutableList()
                val bought = document.data?.get("bought") as List<Boolean>
                for (i in bought.indices) {
                    flowerList[i].isBought = bought[i]
                }
            } else {
                Log.d("readData", "No such document")
            }
        }
        .addOnFailureListener { exception ->
            Log.d("readData", "get failed with ", exception)
        }
}