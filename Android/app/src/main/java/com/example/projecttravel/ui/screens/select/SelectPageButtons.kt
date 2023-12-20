package com.example.projecttravel.ui.screens.select

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.projecttravel.R
import com.example.projecttravel.data.uistates.SelectUiState
import com.example.projecttravel.data.uistates.UserPageUiState
import com.example.projecttravel.ui.screens.GlobalErrorDialog
import com.example.projecttravel.ui.screens.GlobalLoadingDialog
import com.example.projecttravel.ui.screens.TextMsgErrorDialog
import com.example.projecttravel.ui.screens.select.selectdialogs.CancelSelectDialog
import com.example.projecttravel.ui.screens.select.selectdialogs.PlanConfirmDialog
import com.example.projecttravel.ui.screens.select.selectdialogs.ResetConfirmDialog
import com.example.projecttravel.ui.viewmodels.PlanPageViewModel
import com.example.projecttravel.ui.viewmodels.SelectPageViewModel
import com.example.projecttravel.ui.viewmodels.UserPageViewModel

@Composable
fun SelectPageButtons (
    userPageUiState: UserPageUiState,
    userPageViewModel: UserPageViewModel,
    planPageViewModel: PlanPageViewModel,
    selectUiState: SelectUiState,
    selectPageViewModel: SelectPageViewModel,
    onCancelButtonClicked: () -> Unit = {},
    onNextButtonClicked: () -> Unit = {},
) {
    var isCancelSelectDialogVisible by remember { mutableStateOf(false) }

    var isResetDialogVisible by remember { mutableStateOf(false) }
    var isPlanConfirmVisible by remember { mutableStateOf(false) }

    var txtErrorMsg by remember { mutableStateOf("") }
    var isTextErrorDialog by remember { mutableStateOf(false) }

    var isLoadingState by remember { mutableStateOf<Boolean?>(null) }
    Surface {
        when (isLoadingState) {
            true -> GlobalLoadingDialog()
            false -> GlobalErrorDialog( onDismiss = { isLoadingState = null } )
            else -> isLoadingState = null
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically, // 수직 가운데 정렬
        horizontalArrangement = Arrangement.Center, // 수평 가운데 정렬
    ) {
        /** Cancel Button go to go back ====================*/
        OutlinedButton(
            modifier = Modifier
                .weight(1f)
                .padding(1.dp),
            shape = RoundedCornerShape(10.dp),
            onClick = { isCancelSelectDialogVisible = true }
        ) {
            Icon(imageVector = Icons.Filled.ArrowBackIos, contentDescription = "ArrowBackIos")
            Text(text = stringResource(R.string.cancel_button))
            if (isCancelSelectDialogVisible) {
                CancelSelectDialog(
                    selectPageViewModel = selectPageViewModel,
                    userPageViewModel = userPageViewModel,
                    onCancelButtonClicked = onCancelButtonClicked,
                    onDismiss = {
                        isCancelSelectDialogVisible = false
                    }
                )
            }
        }
        /** Reset all selected options ====================*/
        Button(
            modifier = Modifier
                .weight(1f)
                .padding(1.dp),
            shape = RoundedCornerShape(10.dp),
            onClick = {
                isResetDialogVisible = true
            }
        ) {
            Icon(imageVector = Icons.Filled.RestartAlt, contentDescription = "RestartAlt")
            Text(stringResource(R.string.reset_button))
            if (isResetDialogVisible) {
                ResetConfirmDialog(
                    selectPageViewModel = selectPageViewModel,
                    onDismiss = {
                        isResetDialogVisible = false
                    }
                )
            }
        }
        /** Next Button to go forward ====================*/
        OutlinedButton(
            modifier = Modifier
                .weight(1f)
                .padding(1.dp),
            shape = RoundedCornerShape(10.dp),
            onClick = {
                if (selectUiState.selectDateRange == null) {
                    txtErrorMsg = "날짜를 고르세요"
                    isTextErrorDialog = true
                } else if (selectUiState.selectTourAttractions.isEmpty()) {
                    txtErrorMsg = "관광지를 1개 이상 고르세요"
                    isTextErrorDialog = true
                } else {
                    isPlanConfirmVisible = true
                }
            }
        ) {
            Text(text = stringResource(R.string.next_button))
            Icon(imageVector = Icons.Filled.ArrowForwardIos, contentDescription = "ArrowForwardIos")
            if (isTextErrorDialog) {
                TextMsgErrorDialog(
                    txtErrorMsg = txtErrorMsg,
                    onDismiss = {
                        isTextErrorDialog = false
                    },
                )
            }
            if (isPlanConfirmVisible) {
                PlanConfirmDialog(
                    planPageViewModel = planPageViewModel,
                    selectUiState = selectUiState,
                    onNextButtonClicked = onNextButtonClicked,
                    onDismiss = {
                        isPlanConfirmVisible = false
                    },
                    onLoadingStarted = {
                        isLoadingState = true
                    },
                    onErrorOccurred = {
                        isLoadingState = false
                    },
                )
            }
        }
    }

    /** ================================================== */
    /** Bottom BackHandler Click Action ====================*/
    if (userPageUiState.isBackHandlerClick) {
        isCancelSelectDialogVisible = true
    }
}
