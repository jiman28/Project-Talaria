package com.example.projecttravel.ui.screens.searchplace

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projecttravel.R
import com.example.projecttravel.data.uistates.SelectUiState
import com.example.projecttravel.ui.viewmodels.SearchViewModel
import com.example.projecttravel.ui.viewmodels.SelectPageViewModel

@Composable
fun SearchGpsPageButton(
    selectUiState: SelectUiState,
    searchViewModel: SearchViewModel,
    selectPageViewModel: SelectPageViewModel,
    onBackButtonClicked: () -> Unit = {},
    updateUiPageClicked: () -> Unit = {},
) {
    Row(
    horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        /** Cancel DateRangeMenu Button ====================*/
        Button(
            modifier = Modifier
                .weight(1f)
                .padding(1.dp),
            shape = RoundedCornerShape(10.dp),
            onClick = {
                onBackButtonClicked()
            }
        ) {
            Icon(imageVector = Icons.Filled.Clear, contentDescription = "Clear")
            Text(
                text = stringResource(R.string.close_button),
                fontSize = 10.sp,
            )
        }
        /** Reset DateRange Button ====================*/
        OutlinedButton(
            modifier = Modifier
                .weight(1f)
                .padding(1.dp),
            shape = RoundedCornerShape(10.dp),
            onClick = {
                selectPageViewModel.setSearch(null)
                searchViewModel.resetAllSearchUiState()
                updateUiPageClicked()
            }
        ) {
            Icon(imageVector = Icons.Filled.RestartAlt, contentDescription = "RestartAlt")
            Text(
                text = stringResource(R.string.reset_button),
                fontSize = 10.sp,
            )
        }
        /** Confirm DateRange Button  ====================*/
        Button(
            modifier = Modifier
                .weight(1f)
                .padding(1.dp),
            shape = RoundedCornerShape(10.dp),
            onClick = {
                if (selectUiState.selectSearch != null) {
//                    sendInOut(
//                        placeName = searchUiState.searchedPlace?.name,
//                        stateInOut = stateInOut
//                    )
                    selectPageViewModel.addTourAttrSearch(selectUiState.selectSearch)
                    selectPageViewModel.setSearch(null)
                    searchViewModel.resetAllSearchUiState()
                    onBackButtonClicked()
                }
            }) {
            Icon(imageVector = Icons.Filled.Check, contentDescription = "ArrowForwardIos")
            Text(
                text = stringResource(R.string.confirm_button),
                fontSize = 10.sp,
            )
        }
    }
}


