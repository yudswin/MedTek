package com.medtek.main

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.Manifest
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.medtek.main.core.HomeScreen
import com.medtek.main.core.presentation.timer.service.FocusSessionService
import com.medtek.main.greeting.GreetingScreen
import com.medtek.main.survey.EntryScreen
import com.medtek.main.survey.viewmodel.EntryViewModel
import com.medtek.main.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalAnimationApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var isBound by mutableStateOf(false)

    private lateinit var focusSessionService: FocusSessionService
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as FocusSessionService.FocusSessionBinder
            focusSessionService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            isBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                if (isBound) {
                    MainNavHost(focusSessionService)
                }
            }
        }
        requestPermissions(Manifest.permission.POST_NOTIFICATIONS)
    }

    override fun onStart() {
        super.onStart()
        Intent(this, FocusSessionService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    private fun requestPermissions(vararg permissions: String) {
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result ->
            result.entries.forEach {
                Log.d("MainActivity", "${it.key} = ${it.value}")
            }
        }
        requestPermissionLauncher.launch(permissions.asList().toTypedArray())
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        isBound = false
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainNavHost(focusSessionService: FocusSessionService) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "entry"
    ) {
        composable("entry") {
            val entryViewModel: EntryViewModel = hiltViewModel()
            EntryScreen(
                viewModel = entryViewModel,
                navController = navController
            )
        }

        composable("home") {
            HomeScreen(
                navController = navController,
                focusSessionService = focusSessionService
            )
        }

        composable("greeting") {
            GreetingScreen(
                onClose = {
                    navController.navigate("home") {
                        popUpTo("greeting")
                    }
                },
                onActionClick = {}
            )
        }
    }
}




