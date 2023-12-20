package com.example.projecttravel.ui.screens.plantrip

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DoneAll
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.projecttravel.R
import com.example.projecttravel.data.uistates.PlanUiState
import com.example.projecttravel.ui.screens.GlobalErrorDialog
import com.example.projecttravel.ui.screens.GlobalLoadingDialog
import com.example.projecttravel.data.uistates.UserPageUiState
import com.example.projecttravel.ui.viewmodels.UserPageViewModel
import com.example.projecttravel.ui.screens.plantrip.plandialogs.ResetPlanDialog
import com.example.projecttravel.ui.screens.plantrip.plandialogs.SavePlanDialog
import com.example.projecttravel.ui.viewmodels.PlanPageViewModel
import com.example.projecttravel.ui.viewmodels.SelectPageViewModel

@Composable
fun PlanPageButtons (
    userPageUiState: UserPageUiState,
    userPageViewModel: UserPageViewModel,
    planUiState: PlanUiState,
    planPageViewModel: PlanPageViewModel,
    selectPageViewModel: SelectPageViewModel,
    onCancelButtonClicked: () -> Unit,
    onPlanCompleteClicked: () -> Unit,
) {
    var isResetPlanDialogVisible by remember { mutableStateOf(false) }
    var isSavePlanDialogVisible by remember { mutableStateOf(false) }

    var isLoadingState by remember { mutableStateOf<Boolean?>(null) }
    Surface {
        when (isLoadingState) {
            true -> GlobalLoadingDialog()
            false -> GlobalErrorDialog( onDismiss = { isLoadingState = null } )
            else -> isLoadingState = null
        }
    }

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        /** Cancel Button go to go back ====================*/
        Button(
            modifier = Modifier
                .weight(1f)
                .padding(1.dp),
            shape = RoundedCornerShape(10.dp),
            onClick = {
                isResetPlanDialogVisible = true
            }
        ) {
            Icon(imageVector = Icons.Filled.Clear, contentDescription = "Clear")
            Text(stringResource(R.string.cancel_button))
            if (isResetPlanDialogVisible) {
                ResetPlanDialog(
                    planPageViewModel = planPageViewModel,
                    userPageViewModel = userPageViewModel,
                    onCancelButtonClicked = onCancelButtonClicked,
                    onDismiss = {
                        isResetPlanDialogVisible = false
                    }
                )
            }
        }
        /** Sending All selections to DataBase ====================*/
        OutlinedButton(
            modifier = Modifier
                .weight(1f)
                .padding(1.dp),
            shape = RoundedCornerShape(10.dp),
            onClick = {
                isSavePlanDialogVisible = true
            }
        ) {
            Icon(imageVector = Icons.Filled.DoneAll, contentDescription = "DoneAll")
            Text(stringResource(R.string.complete_button))
            if (isSavePlanDialogVisible) {
                SavePlanDialog(
                    userPageUiState = userPageUiState,
                    planUiState = planUiState,
                    planPageViewModel = planPageViewModel,
                    selectPageViewModel = selectPageViewModel,
                    onPlanCompleteClicked = onPlanCompleteClicked,
                    onDismiss = {
                        isSavePlanDialogVisible = false
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
        isResetPlanDialogVisible = true
    }
}
