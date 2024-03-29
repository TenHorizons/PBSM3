package com.example.pbsm3.ui.commonScreenComponents

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.pbsm3.Screen
import com.example.pbsm3.bottomNavItems
import com.example.pbsm3.theme.PBSM3Theme

@Composable
fun PBSBottomNav(
    modifier: Modifier = Modifier,
    onClick:(Screen)->Unit,
    screen: Screen
) {
    var selectedItemIndex by remember { mutableStateOf(0) }

    if(screen !in bottomNavItems) return
    NavigationBar(modifier = modifier) {
        bottomNavItems.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon!!,
                        contentDescription = item.name)
                },
                label = { Text(item.name) },
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