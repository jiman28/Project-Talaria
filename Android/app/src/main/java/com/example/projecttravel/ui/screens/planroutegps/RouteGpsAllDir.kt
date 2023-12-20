package com.example.projecttravel.ui.screens.planroutegps

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.WbCloudy
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.example.projecttravel.R
import com.example.projecttravel.model.SpotDtoResponse
import com.example.projecttravel.ui.screens.DefaultAppFontContent

@Composable
fun RouteGpsAllDir(
    currentDayTripAttrs: SpotDtoResponse,
) {
    val context = LocalContext.current

    // 위치 허용 확인 (Boolean 값)
    val locationPermissionGranted =
        ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

    var myPositionSwitchChecked by remember { mutableStateOf(false) }

    if (locationPermissionGranted) {
        Row(
            verticalAlignment = Alignment.CenterVertically, // 수직 가운데 정렬
            horizontalArrangement = Arrangement.Center, // 수평 가운데 정렬
            modifier = Modifier
                .padding(2.dp)
                .background(
                    color = Color(0xFFD4E3FF),
                    shape = RoundedCornerShape(10.dp)
                )
                .fillMaxWidth() // 화면 가로 전체를 차지하도록 함 (정렬할 때 중요하게 작용)
        ) {
            Column(
                modifier = Modifier
                    .weight(3f),
                verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
                horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
            ) {
                if (myPositionSwitchChecked) {
                    Row (
                        verticalAlignment = Alignment.CenterVertically, // 수직 가운데 정렬
                        horizontalArrangement = Arrangement.Center, // 수평 가운데 정렬
                    ) {
                        Icon(imageVector = Icons.Filled.ArrowDownward, contentDescription = "ArrowDownward")
                        Text(
                            text = " 경로에 현재 위치 ON",
                            fontSize = 25.sp,   // font 의 크기
                            fontWeight = FontWeight.Thin,  // font 의 굵기
                            fontFamily = DefaultAppFontContent(),  // font 의 글씨체(커스텀)
                            style = MaterialTheme.typography.bodyMedium,  //font 의 스타일
                            textAlign = TextAlign.Center, // 텍스트 내용을 compose 가운데 정렬
                        )
                    }

                } else {
                    Row (
                        verticalAlignment = Alignment.CenterVertically, // 수직 가운데 정렬
                        horizontalArrangement = Arrangement.Center, // 수평 가운데 정렬
                    ) {
                        Icon(imageVector = Icons.Filled.ArrowDownward, contentDescription = "ArrowDownward")
                        Text(
                            text = " 경로에 현재 위치 OFF",
                            fontSize = 25.sp,   // font 의 크기
                            fontWeight = FontWeight.Thin,  // font 의 굵기
                            fontFamily = DefaultAppFontContent(),  // font 의 글씨체(커스텀)
                            style = MaterialTheme.typography.titleLarge,  //font 의 스타일
                            textAlign = TextAlign.Center, // 텍스트 내용을 compose 가운데 정렬
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
                horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
            ) {
                Switch(
                    checked = myPositionSwitchChecked,
                    onCheckedChange = { newCheckedState ->
                        myPositionSwitchChecked = newCheckedState
                    },
                    thumbContent = if (myPositionSwitchChecked) {
                        {
                            Icon(
                                imageVector = Icons.Filled.LocationOn,
                                contentDescription = null,
                                modifier = Modifier.size(SwitchDefaults.IconSize),
                            )
                        }
                    } else {
                        {
                            Icon(
                                imageVector = Icons.Filled.LocationOff,
                                contentDescription = null,
                                modifier = Modifier.size(SwitchDefaults.IconSize),
                            )
                        }
                    },
                )
            }
        }
    }
    Column(
        verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
        horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
        modifier = Modifier
            .padding(2.dp)
            .background(
                color = Color(0xFFD4E3FF),
                shape = RoundedCornerShape(10.dp)
            )
            .fillMaxWidth() // 화면 가로 전체를 차지하도록 함 (정렬할 때 중요하게 작용)
            .clickable {
                if (myPositionSwitchChecked) {
                    val myPosition = "My+Location"  // 진짜 위치
//                    val myPosition = "바비칸 센터"   // 임시 확인용 위치
                    val uriBuilder =
                        StringBuilder("https://www.google.co.in/maps/dir/$myPosition")
                    for (i in 0 until currentDayTripAttrs.list.size) {
                        uriBuilder.append("/${currentDayTripAttrs.list[i].name}")
                    }
                    val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(uriBuilder.toString()))
                    // Check if there's a mapping app available before starting the activity
                    Log.d("jimanLog=111queryCheck", uriBuilder.toString())
                    context.startActivity(mapIntent)
                } else {
                    if (currentDayTripAttrs.list.size >= 2) {
                        // Build the geo URI for the route
                        val uriBuilder =
                            StringBuilder("https://www.google.co.in/maps/dir/${currentDayTripAttrs.list[0].name}")
                        for (i in 1 until currentDayTripAttrs.list.size) {
                            uriBuilder.append("/${currentDayTripAttrs.list[i].name}")
                        }
                        val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(uriBuilder.toString()))
                        // Check if there's a mapping app available before starting the activity
                        Log.d("jimanLog=111queryCheck", uriBuilder.toString())
                        context.startActivity(mapIntent)
                    } else {
                        // Build the geo URI for the route
                        val uriBuilder = StringBuilder("geo:0,0?q=${currentDayTripAttrs.list[0].name}")
                        val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(uriBuilder.toString()))
                        // Check if there's a mapping app available before starting the activity
                        Log.d("jimanLog=111queryCheck", uriBuilder.toString())
                        context.startActivity(mapIntent)
                    }
                }
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically, // 수직 가운데 정렬
            horizontalArrangement = Arrangement.Center, // 수평 가운데 정렬
            modifier = Modifier.padding(5.dp)
        ) {
            Text(
                text = "전체 경로 확인",
                fontSize = 30.sp,   // font 의 크기
                lineHeight = 30.sp, // 줄 간격 = fontSize 와 맞춰야 글이 겹치지 않는다
                fontWeight = FontWeight.Thin,  // font 의 굵기
                style = MaterialTheme.typography.bodyMedium,  //font 의 스타일
                fontFamily = DefaultAppFontContent(),  // font 의 글씨체(커스텀)
                textAlign = TextAlign.Center, // 텍스트 내용을 compose 가운데 정렬
                color = Color.Blue,
                modifier = Modifier
            )
            Spacer(modifier = Modifier.padding(5.dp))
            Image(
                painter = painterResource(R.drawable.logo_google_maps),
                contentDescription = "logo_google_maps",
                contentScale = ContentScale.Fit,
            )
        }
    }
}