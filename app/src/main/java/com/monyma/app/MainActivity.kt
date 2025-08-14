package com.monyma.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.monyma.app.ui.MonymaTheme
import com.monyma.feature.dashboard.DashboardScreen
import com.monyma.feature.transactions.TransactionsScreen
import com.monyma.feature.budgets.BudgetsScreen
import com.monyma.feature.reports.ReportsScreen
import com.monyma.feature.settings.SettingsScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			MonymaTheme {
				var currentTab by remember { mutableStateOf(BottomTab.Dashboard) }

				Scaffold(
					topBar = {
						TopAppBar(title = { Text(text = currentTab.title) })
					},
					bottomBar = {
						NavigationBar {
							BottomTab.entries.forEach { tab ->
								NavigationBarItem(
									selected = currentTab == tab,
									onClick = { currentTab = tab },
									icon = { Icon(tab.icon, contentDescription = tab.title) },
									label = { Text(tab.title) }
								)
							}
						}
					}
				) { paddingValues ->
					when (currentTab) {
						BottomTab.Dashboard -> DashboardScreen(modifier = Modifier.fillMaxSize())
						BottomTab.Transactions -> TransactionsScreen(modifier = Modifier.fillMaxSize())
						BottomTab.Budgets -> BudgetsScreen(modifier = Modifier.fillMaxSize())
						BottomTab.Reports -> ReportsScreen(modifier = Modifier.fillMaxSize())
						BottomTab.Settings -> SettingsScreen(modifier = Modifier.fillMaxSize())
					}
				}
			}
		}
	}
}

enum class BottomTab(val title: String, val icon: ImageVector) {
	Dashboard("Dashboard", Icons.Filled.Home),
	Transactions("Transactions", Icons.Filled.List),
	Budgets("Budgets", Icons.Filled.Wallet),
	Reports("Reports", Icons.Filled.Assessment),
	Settings("Settings", Icons.Filled.Settings)
}