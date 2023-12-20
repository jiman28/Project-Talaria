package com.example.projecttravel.ui.screens.searchplace

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.projecttravel.data.uistates.SearchUiState
import com.example.projecttravel.ui.viewmodels.SearchViewModel

/** Shows InOutButton ====================*/
@Composable
fun InOutSelect(
    searchUiState: SearchUiState,
    searchViewModel: SearchViewModel,
) {
    Column (
        verticalArrangement = Arrangement.Center, // 수직 가운데 정렬
        horizontalAlignment = Alignment.CenterHorizontally, // 수평 가운데 정렬
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically, // 수직 가운데 정렬
            horizontalArrangement = Arrangement.Center, // 수평 가운데 정렬
            modifier = Modifier.fillMaxWidth(),
        ) {
            var stateRadioButton by remember { mutableStateOf(searchUiState.inOutCheckerBoolean) }
//            Spacer(modifier = Modifier.padding(2.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically, // 수직 가운데 정렬
                horizontalArrangement = Arrangement.Center, // 수평 가운데 정렬
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(3.dp),
            ) {
                RadioButton(
                    selected = stateRadioButton,
                    onClick = {
                        stateRadioButton = true
                        searchViewModel.setInOutBoolean(true)
                        searchViewModel.setInOutChecker("0")
                    },
                    modifier = Modifier.semantics { contentDescription = "In" }.size(10.dp)
                )
                Text(text = "  In")
            }
            Row(
                verticalAlignment = Alignment.CenterVertically, // 수직 가운데 정렬
                horizontalArrangement = Arrangement.Center, // 수평 가운데 정렬
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(3.dp),
            ) {
                RadioButton(
                    selected = !stateRadioButton,
                    onClick = {
                        stateRadioButton = false
                        searchViewModel.setInOutBoolean(false)
                        searchViewModel.setInOutChecker("1")
                    },
                    modifier = Modifier.semantics { contentDescription = "Out" }.size(10.dp)
                )
                Text(text = "  Out")
            }
        }
    }
}
