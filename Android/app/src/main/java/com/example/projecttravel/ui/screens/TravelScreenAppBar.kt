package com.example.projecttravel.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.projecttravel.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/** Composable that displays the topBar */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelAppBar(
    currentScreen: TravelScreen,   // enum class 타입을  매개변수 추가
    navController: NavHostController,   // TravelApp()의 navController 를 전달
    drawerState: DrawerState,
    scope: CoroutineScope,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {
            Text(
                text = "TALARIA",
                fontSize = 50.sp,
                fontFamily = FontFamily(Font(R.font.lobster)),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                Icon(imageVector = Icons.Filled.List, contentDescription = "AppBarHome")
            }
        },
        actions = {// 버튼 오른쪽 정렬
            IconButton(
                onClick = {
                    if (currentScreen != TravelScreen.Page1) { navController.navigate(TravelScreen.Page1.name) }
                }
            ) {
                Image(
                    painter = painterResource(R.drawable.logo_talaria_round),
                    contentDescription = "HomeImage",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.scale(1F)
                )
            }
        },
    )
}