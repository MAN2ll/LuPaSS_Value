package com.securevault.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.securevault.ui.screens.VaultListScreen

@Composable
fun SecureVaultNavHost() {
    val root = rememberNavController()
    
    NavHost(root, startDestination = "main") {
        composable("main") {
            VaultListScreen(
                onAdd = { /* TODO */ },
                onEdit = { /* TODO */ },
                onLock = { /* TODO */ }
            )
        }
    }
}
