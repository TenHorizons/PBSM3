package com.example.pbsm3.ui.commonScreenComponents

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.pbsm3.ui.navhost.Screen
import com.example.pbsm3.ui.navhost.bottomNavItems
import com.example.pbsm3.ui.theme.PBSM3Theme

@Composable
fun PBSBottomNav(
    modifier: Modifier = Modifier,
    onClick:(Int, Screen)->Unit
) {
    var selectedItemIndex by remember { mutableStateOf(0) }

    NavigationBar(modifier = modifier) {
        bottomNavItems.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon!!,
                        contentDescription = item.route)
                },
                label = { Text(item.route) },
                selected = selectedItemIndex == index,
                onClick = {
                    selectedItemIndex = index
                    onClick(index,item) }
            )
        }
    }
}

@Preview
@Composable
fun BottomNavPreview() {
    PBSM3Theme {
        PBSBottomNav(onClick = {_,_->})
    }
}