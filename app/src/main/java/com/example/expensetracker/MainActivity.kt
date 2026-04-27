package com.example.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.expensetracker.room.database.DatabaseProvider
import com.example.expensetracker.room.repository.CategoryRepository
import com.example.expensetracker.room.repository.ExpenseRepository
import com.example.expensetracker.room.repository.SettingsRepository
import com.example.expensetracker.ui.components.MenuItem
import com.example.expensetracker.ui.layouts.GeneralLayout
import com.example.expensetracker.ui.layouts.ScreenLayout
import com.example.expensetracker.ui.screens.home.HomeScreen
import com.example.expensetracker.ui.screens.expenseDetail.ExpenseDetailScreen
import com.example.expensetracker.ui.screens.search.SearchScreen
import com.example.expensetracker.ui.screens.setting.SettingScreen
import com.example.expensetracker.ui.screens.statistic.StatisticScreen
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme
import com.example.expensetracker.viewmodel.SettingViewModel
import com.example.expensetracker.viewmodel.BudgetViewModelFactory
import com.example.expensetracker.viewmodel.CategoryViewModel
import com.example.expensetracker.viewmodel.CategoryViewModelFactory
import com.example.expensetracker.viewmodel.ExpenseViewModel
import com.example.expensetracker.viewmodel.ExpenseViewModelFactory
import com.example.expensetracker.viewmodel.StatisticViewModel
import com.example.expensetracker.viewmodel.StatisticViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExpenseTrackerTheme {
                App()
            }
        }
    }
}

data class TopBarConfig(val title: String, val showBack: Boolean = false)

object Routes {
    const val HOME = "home"
    const val SETTING = "setting"
    const val EXPENSEDETAIL = "expense/{id}"
    const val SEARCH = "search"
    const val STATISTIC = "statistic"

    val bottomBarRoutes = setOf(HOME, SETTING, SEARCH, STATISTIC)

    val topBarRoutes: Map<String, TopBarConfig> = mapOf(
        "expense/" to TopBarConfig(title = "Expense Detail", showBack = true)
    )
}

val navItems = listOf(
    MenuItem(Routes.HOME, "Home", Icons.Outlined.Home, Icons.Filled.Home),
    MenuItem(Routes.SETTING, "Setting", Icons.Outlined.Settings, Icons.Filled.Settings),
    MenuItem(Routes.STATISTIC, "Statistic", Icons.Outlined.Analytics, Icons.Filled.Analytics),
    MenuItem(Routes.SEARCH, "Search", Icons.Outlined.Search, Icons.Filled.Search),
)

@Composable
fun App() {
    val context = LocalContext.current
    val db = remember { DatabaseProvider.getDatabase(context) }
    val expenseRepo = remember { ExpenseRepository(db.expenseDao()) }
    val categoryRepo = remember { CategoryRepository(db.categoryDao()) }
    val settingsRepo = remember { SettingsRepository(db.settingDao()) }
    val navController = rememberNavController()
    val currentRoute =
        navController.currentBackStackEntryAsState().value?.destination?.route
            ?: Routes.HOME
    val expenseViewModel: ExpenseViewModel = viewModel(
        factory = ExpenseViewModelFactory(expenseRepo)
    )

    val categoryViewModel: CategoryViewModel = viewModel(
        factory = CategoryViewModelFactory(categoryRepo)
    )

    val settingViewModel: SettingViewModel = viewModel(
        factory = BudgetViewModelFactory(expenseRepo, settingsRepo)
    )

    val statisticViewModel: StatisticViewModel = viewModel(
        factory = StatisticViewModelFactory(expenseRepo, categoryRepo, settingsRepo)
    )

    val currencySymbol by settingViewModel.currencySymbol.collectAsState()

    GeneralLayout(
        navController = navController,
        currentRoute = currentRoute
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            ) {
            composable(Routes.HOME) {
                ScreenLayout(paddingValues) {
                    HomeScreen(
                        expenseViewModel,
                        categoryViewModel,
                        settingViewModel,
                        navController,
                    )
                }

            }

            composable(Routes.SETTING) {
                ScreenLayout(paddingValues) {
                    SettingScreen(categoryViewModel, settingViewModel, expenseViewModel)
                }
            }

            composable(Routes.EXPENSEDETAIL) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")?.toInt() ?: 0
                ScreenLayout(paddingValues) {
                    ExpenseDetailScreen(id, expenseViewModel, categoryViewModel, currencySymbol)
                }
            }

            composable(Routes.SEARCH) {
                ScreenLayout(paddingValues) {
                    SearchScreen(expenseViewModel, categoryViewModel, navController, currencySymbol)
                }
            }

            composable(Routes.STATISTIC) {
                ScreenLayout(paddingValues) {
                    StatisticScreen(statisticViewModel)
                }
            }


        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    ExpenseTrackerTheme {
        App()
    }
}