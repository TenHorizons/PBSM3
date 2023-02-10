package com.example.pbsm3.ui.commonScreenComponents

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.pbsm3.ui.navhost.Screen
import com.example.pbsm3.ui.navhost.bottomNavItems
import com.example.pbsm3.ui.theme.PBSM3Theme

@Composable
fun PBSBottomNav(
    modifier: Modifier = Modifier,
    onClick:(Screen)->Unit,
    screen: Screen
) {
    var selectedItemIndex by remember { mutableStateOf(0) }

    if(screen == Screen.Login) return
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
                    onClick(item) }
            )
        }
    }
}

@Preview
@Composable
fun BottomNavPreview() {
    PBSM3Theme {
        PBSBottomNav(onClick = {}, screen = Screen.Budget)
    }
}