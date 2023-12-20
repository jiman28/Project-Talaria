package com.example.projecttravel.ui.screens.planroutegps

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.example.projecttravel.R
import com.example.projecttravel.data.uistates.PlanUiState
import com.example.projecttravel.data.uistates.UserPageUiState
import com.example.projecttravel.model.SpotDto
import com.example.projecttravel.model.SpotDtoResponse
import com.example.projecttravel.model.SpotDtoResponseRead
import com.example.projecttravel.ui.viewmodels.PlanPageViewModel
import com.example.projecttravel.ui.viewmodels.UserPageViewModel
import com.example.projecttravel.ui.screens.DefaultAppFontContent
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun RouteGpsPage(
    userPageUiState: UserPageUiState,
    userPageViewModel: UserPageViewModel,
    planUiState: PlanUiState,
    planPageViewModel: PlanPageViewModel,
    onBackButtonClicked: () -> Unit = {},
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

    // 들어오는 변수 확인
    val currentDayTripAttrs: SpotDtoResponse? =
        when (planUiState.checkSingleDayGps) {
            is SpotDtoResponseRead -> {
                val spotDtoResponse = SpotDtoResponse(
                    date = planUiState.checkSingleDayGps.date,
                    list = planUiState.checkSingleDayGps.list.map { spotDtoRead ->
                        SpotDto(
                            pk = spotDtoRead.pk,
                            name = spotDtoRead.name,
                            img = spotDtoRead.img,
                            lan = spotDtoRead.lan,
                            lat = spotDtoRead.lat,
                            inOut = spotDtoRead.inOut.toString(), // Int를 String으로 변환
                            cityId = spotDtoRead.cityId.toString() // Int를 String으로 변환
                        )
                    }
                )
                spotDtoResponse
            }

            is SpotDtoResponse -> planUiState.checkSingleDayGps
            else -> null
        }

    // 마커를 눌렀을 경우 나오는 다이알로그
    var showPlaceInfo by remember { mutableStateOf(false) }
    var selectedPlaceMarker by remember { mutableStateOf<SpotDto?>(null) }
    if (showPlaceInfo) {
        selectedPlaceMarker?.let {
            PlaceViewDialog(
                selectedPlaceMarker = it,
                onDismiss = { showPlaceInfo = false },
            )
        }
    }

    Column(
        verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
        horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
        modifier = Modifier
            .padding(start = 15.dp, end = 15.dp) // 원하는 여백을 추가(start = 15.dp, end = 15.dp, ...)
            .fillMaxWidth() // 화면 가로 전체를 차지하도록 함 (정렬할 때 중요하게 작용)
    ) {
        if (currentDayTripAttrs != null) {
            /** GoogleMap Marker Position ====================*/
            // 모든 마커 위치 리스트 (LatLng)
            val markerPositions = currentDayTripAttrs.list.map { attrs ->
                stringToLatLng(attrs.lat, attrs.lan)
            }
            // 모든 마커 의 중심점 계산
            val centerLat = markerPositions.map { it.latitude }.average()
            val centerLng = markerPositions.map { it.longitude }.average()
            val centerPosition = LatLng(centerLat, centerLng)

            Column(
                verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
                horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
                modifier = Modifier
                    .padding(15.dp) // 원하는 여백을 추가(start = 15.dp, end = 15.dp, ...)
                    .fillMaxWidth() // 화면 가로 전체를 차지하도록 함 (정렬할 때 중요하게 작용)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
                    horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
                    modifier = Modifier
                        .fillMaxWidth() // 화면 가로 전체를 차지하도록 함 (정렬할 때 중요하게 작용)
                ) {
                    Text(
                        text = "관광지 정보",
                        fontSize = 50.sp,   // font 의 크기
                        lineHeight = 50.sp, // 줄 간격 = fontSize 와 맞춰야 글이 겹치지 않는다
                        fontWeight = FontWeight.ExtraBold,  // font 의 굵기
                        fontFamily = DefaultAppFontContent(),  // font 의 글씨체(커스텀)
                        style = MaterialTheme.typography.titleLarge,  //font 의 스타일
                        textAlign = TextAlign.Center, // 텍스트 내용을 compose 가운데 정렬
                        modifier = Modifier
                            .fillMaxWidth() // 화면 가로 전체를 차지하도록 함 (정렬할 때 중요하게 작용)
                    )
                    if (!locationPermissionGranted) {
                        Text(
                            text = "위치 정보에 동의하면 자신의 위치를 볼 수 있습니다.",
                            fontSize = 15.sp,   // font 의 크기
                            lineHeight = 15.sp, // 줄 간격 = fontSize 와 맞춰야 글이 겹치지 않는다
                            fontWeight = FontWeight.Thin,  // font 의 굵기
                            style = MaterialTheme.typography.bodyMedium,  //font 의 스타일
                            fontFamily = DefaultAppFontContent(),  // font 의 글씨체(커스텀)
                            textAlign = TextAlign.Center, // 텍스트 내용을 compose 가운데 정렬
                            color = Color.Red,
                            modifier = Modifier
                                .fillMaxWidth() // 화면 가로 전체를 차지하도록 함 (정렬할 때 중요하게 작용)
                        )
                    }
                }

                Divider(thickness = dimensionResource(R.dimen.thickness_divider3))

                Column {
                    RouteGpsAllDir(
                        currentDayTripAttrs = currentDayTripAttrs,
                    )
                }
                /** GoogleMap Composable ====================*/
                Column {


                    /** GoogleMap Camera Position - 카메라 위치 조정 ====================*/
                    // zoom(float) : zoomIn = num up/ zoomOut = num down
                    val cameraPositionState = rememberCameraPositionState {
                        position = CameraPosition.fromLatLngZoom(centerPosition, 11f)
                    }
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState,
                        properties = MapProperties(
                            isMyLocationEnabled = locationPermissionGranted,    // 내 위치(허가할 시에만)
                            isTrafficEnabled = true,    // 교통정보
                        ),
                    ) {
                        currentDayTripAttrs.list.forEach { attrs ->
                            Marker(
                                state = MarkerState(
                                    position = stringToLatLng(
                                        attrs.lat,
                                        attrs.lan
                                    ),
                                ),
                                alpha = 3.0f,
                                anchor = Offset(0.5f, 0.5f),
                                icon = BitmapDescriptorFactory.defaultMarker(
                                    when (attrs.inOut) {
                                        "0" -> BitmapDescriptorFactory.HUE_BLUE
                                        "1" -> BitmapDescriptorFactory.HUE_RED
                                        else -> 0f
                                    }
                                ), // 마커 아이콘 설정
                                title = attrs.name,
                                snippet = when (attrs.inOut) {
                                    "0" -> "실내 활동"
                                    "1" -> "실외 활동"
                                    else -> "None"
                                },
                                onInfoWindowClick = {
                                    showPlaceInfo = true
                                    selectedPlaceMarker = attrs
                                },
                            )
                        }
                    }
                }
            }
        } else {
            Text(text = "당일에 선택한 관광지가 없습니다")
        }
    }
}

// 위도(Latitude)와 경도(Longitude) 값을 저장하는 LatLng 객체로 변환
fun stringToLatLng(lat: String, lng: String): LatLng {
    val latitude = lat.toDouble()
    val longitude = lng.toDouble()
    return LatLng(latitude, longitude)
}

