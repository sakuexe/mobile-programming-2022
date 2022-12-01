package com.example.finalbmi_3

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.finalbmi_3.datastore.StoreUserData
import com.example.finalbmi_3.ui.theme.FinalBMI3Theme
import com.example.finalbmi_3.ui.theme.SettingsCompose
import kotlin.math.pow

// make a dataclass for nav bar's items
data class BarItem(
    val title: String,
    val image: ImageVector,
    val route: String
)

// make an object that is a list of nav bar's items
object NavBarItems {
    val BarItems = listOf(
        BarItem(
            title = "Home",
            image = Icons.Filled.Home,
            route = "home"
        ),
        BarItem(
            title = "History",
            image = Icons.Filled.List,
            route = "history"
        ),
        BarItem(
            title = "Settings",
            image = Icons.Filled.Settings,
            route = "settings"
        ),
    )
}

// creating a variable for SharedPreferences
lateinit var sharedPreferences: SharedPreferences

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            sharedPreferences = getSharedPreferences(this.toString(), Context.MODE_PRIVATE)

            FinalBMI3Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = colorResource(id = R.color.darkGreen)
                ) {
                    MainCompose()
                }
            }
        }
    }
}

@Composable
// this is where the magic happens
fun MainCompose() {
    val navController = rememberNavController()
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        // Foreground color
        contentColor = colorResource(id = R.color.mutedCream),
        // Background color
        color = colorResource(id = R.color.darkGreen),    ) {
        //Bottom navigation bar
        Scaffold (
            content = { NavigationHost(navController = navController)},
            bottomBar = { BottomNavigationBar(
                navController = navController,
            )},
            backgroundColor = colorResource(id = R.color.darkGreen)
        )
    }
}

@Composable
fun NavigationHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        // choose which page to start in
        startDestination = NavRoutes.Home.route,
    ) {
        composable(NavRoutes.Home.route) {

            Home()
        }
        composable(NavRoutes.History.route) {
            HistoryCompose()
        }
        composable(NavRoutes.Settings.route) {
            SettingsCompose()
        }
    }
}

@Composable
fun Home() {
    // context
    val context = LocalContext.current
    // datastore height
    val datastore = StoreUserData(context)
    // get saved height
    val savedHeight = datastore.getHeight.collectAsState(initial = "")

    var userWeight by remember { mutableStateOf("") }
    // BMI
    var BMI by remember { mutableStateOf("") }
    // BMI show toggle
    var BMIstatusText by remember { mutableStateOf("") }

    MainTitle()
    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(modifier = Modifier.height(100.dp))
        TextField(
            placeholder = { Text("Your weight (in kg)")},
            value = userWeight,
            onValueChange = {
                userWeight = it
            },
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = colorResource(id = R.color.lightGreen),
                placeholderColor = colorResource(id = R.color.lightGreen)
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
            ),
        )
        // The "Calculate" button
        Button(
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colorResource(id = R.color.green),
                // contentColor = colorResource(id = R.color.darkGray)
                ),
            onClick = {
                // counter += 1
                if(savedHeight.value != "") {
                    var tempHeight = savedHeight.value.toDouble()
                    val tempWeight = userWeight.toDouble()
                    // convert height (cm) to meters
                    tempHeight /= 100
                    // calculate the BMI: weight(kg) / height^2(m)
                    // format BMI to have 2 decimals
                    BMI = "%.2f".format(tempWeight / tempHeight.pow(2))

                    // show bmi flavor text
                    if(BMIstatusText == "") {
                        BMIstatusText = "Your BMI is:"
                    }
                }

        }) {
            Text(text = "Calculate")
        }
        // Text(text = "DebugText: ${savedHeight.value} BMI: $BMI")
        Text(text = BMIstatusText)
        Text(text = BMI, fontSize = 50.sp, fontWeight = FontWeight.Black)
    }
}

@Composable
fun MainTitle() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Text(
            text = "BMI Assignment #3/3",
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.lightGreen)
        )
        Text(
            text = "BMI Calculator",
            fontWeight = FontWeight.Black,
            fontSize = 40.sp,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FinalBMI3Theme {
        MainCompose()
    }
}