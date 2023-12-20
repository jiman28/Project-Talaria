package com.example.projecttravel.ui.screens.infomeplan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.projecttravel.data.uistates.PlanUiState
import com.example.projecttravel.model.SpotDtoResponseRead
import com.example.projecttravel.ui.viewmodels.UserPageViewModel

@Composable
fun UserPlanDateList(
    allUserAttrList: List<SpotDtoResponseRead>,
    userPageViewModel: UserPageViewModel,
    planUiState: PlanUiState,
    onDateClick: (String) -> Unit
) {
    val sortedDates = allUserAttrList.sortedBy { it.date }
    LazyRow(
        modifier = Modifier
            .height(160.dp)
            .fillMaxWidth()
            .background(
                color = Color(0xFFD4E3FF),
                shape = RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp)
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        items(sortedDates) { list ->
            val weatherResponseGet = planUiState.dateToWeather.find { it.day == list.date }
            val allUserAttrListByDate = allUserAttrList.find { it.date == list.date }
            val allUserAttrListSize = allUserAttrListByDate?.list?.size

            if (allUserAttrListSize != null) {
                UserPlanCardDate(
                    date = list.date,
                    size = allUserAttrListSize,
                    userPageViewModel = userPageViewModel,
                    weatherResponseGet = weatherResponseGet,
                    onClick = { onDateClick(list.date) } // Update selectedUserPlanDate
                )
            }
        }
    }
}

