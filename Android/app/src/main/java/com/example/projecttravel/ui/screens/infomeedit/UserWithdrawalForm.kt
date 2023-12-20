package com.example.projecttravel.ui.screens.infomeedit

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.edit
import androidx.navigation.NavHostController
import com.example.projecttravel.data.uistates.UserPageUiState
import com.example.projecttravel.ui.viewmodels.UserPageViewModel
import com.example.projecttravel.model.SendSignIn
import com.example.projecttravel.ui.screens.GlobalLoadingDialog
import com.example.projecttravel.ui.screens.TextMsgErrorDialog
import com.example.projecttravel.ui.screens.TravelScreen
import com.example.projecttravel.ui.screens.auth.datastore.DataStore
import com.example.projecttravel.ui.screens.auth.datastore.DataStore.Companion.dataStore
import com.example.projecttravel.ui.screens.infomeedit.meeditapi.withdrawalUserCall
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@Composable
fun UserWithdrawalForm(
    userPageUiState: UserPageUiState,
    userPageViewModel: UserPageViewModel,
    navController: NavHostController,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val dataStore = (context).dataStore

    var withdrawalCredentials by remember { mutableStateOf(WithdrawalCredentials()) }
    var sendSignIn by remember { mutableStateOf<SendSignIn?>(null) }

    var isWithdrawalUserDialogVisible by remember { mutableStateOf(false) }

    var isLoadingState by remember { mutableStateOf<Boolean?>(null) }
    var withdrawalErrorMsg by remember { mutableStateOf("") }
    Surface {
        when (isLoadingState) {
            true -> GlobalLoadingDialog()
            false -> TextMsgErrorDialog(
                txtErrorMsg = withdrawalErrorMsg,
                onDismiss = { isLoadingState = null })
            else -> isLoadingState = null
        }
    }

    Column {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp, vertical = 50.dp)
        ) {
            Text("회원탈퇴를 위해 비밀번호를 입력하세요.")
            Spacer(modifier = Modifier.height(20.dp))
            WithdrawalPasswordField(
                value = withdrawalCredentials.pwd,
                onChange = { data -> withdrawalCredentials = withdrawalCredentials.copy(pwd = data) },
                label = "Password",
                placeholder = "Enter your Password",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            WithdrawalPasswordField(
                value = withdrawalCredentials.pwdCheck,
                onChange = { data -> withdrawalCredentials = withdrawalCredentials.copy(pwdCheck = data) },
                label = "Password Check",
                placeholder = "Check your Password",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (withdrawalCredentials.pwd == withdrawalCredentials.pwdCheck) {
                        if (userPageUiState.currentLogin != null) {
                            sendSignIn = SendSignIn(
                                email = userPageUiState.currentLogin.email,
                                name = userPageUiState.currentLogin.name,
                                password = withdrawalCredentials.pwd,
                            )
                        }
                        isWithdrawalUserDialogVisible = true
                    } else {
                        Toast.makeText(context, "비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show()
                    }
                },
                enabled = withdrawalCredentials.isNotEmpty(),
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("회원 정보 변경")
            }
        }
    }

    if (isWithdrawalUserDialogVisible) {
        sendSignIn?.let {
            WithdrawalConfirmDialog(
                sendSignIn = it,
                userPageViewModel = userPageViewModel,
                navController = navController,
                onDismiss = {
                    isWithdrawalUserDialogVisible = false
                },
                onLoadingStarted = {
                    isLoadingState = true
                },
                onErrorOccurred = {
                    withdrawalErrorMsg = "비밀번호를 다시 한번 확인하세요"
                    isLoadingState = false
                },
            )
        }
    }
}

data class WithdrawalCredentials(
    var pwd: String = "",
    var pwdCheck: String = "",
) {
    fun isNotEmpty(): Boolean {
        return pwd.isNotEmpty() && pwdCheck.isNotEmpty()
    }
}

@Composable
fun WithdrawalPasswordField(
    value: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String,
    placeholder: String,
) {

    var isPasswordVisible by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val leadingIcon = @Composable {
        Icon(
            Icons.Default.Key,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.primary
        )
    }
    val trailingIcon = @Composable {
        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
            Icon(
                if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }

    TextField(
        value = value,
        onValueChange = onChange,
        modifier = modifier,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Password
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        ),

        placeholder = { Text(placeholder) },
        label = { Text(label) },
        singleLine = true,
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
    )
}

/** ===================================================================== */
/** LogOutDialog to ask whether to logout or not ====================*/
@Composable
fun WithdrawalConfirmDialog(
    sendSignIn: SendSignIn,
    userPageViewModel: UserPageViewModel,
    navController: NavHostController,
    onLoadingStarted: () -> Unit,
    onErrorOccurred: () -> Unit,
    onDismiss: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val dataStore = (context).dataStore
    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        text = {
            Text(
                text = "정말 회원을\n탈퇴 하시겠습니까?",
                fontSize = 30.sp,
                lineHeight = 30.sp,
                textAlign = TextAlign.Center, // 텍스트 내용 가운데 정렬
                modifier = Modifier
                    .padding(10.dp) // 원하는 여백을 추가).
                    .fillMaxWidth() // 화면 가로 전체를 차지하도록 함
            )
        },
        confirmButton = {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(
                    onClick = {
                        scope.launch {
                            onLoadingStarted()
                            val signInDeferred = async { withdrawalUserCall(sendSignIn) }
                            val withdrawalComplete = signInDeferred.await()
                            if (withdrawalComplete) {
                                userPageViewModel.resetUser()
                                dataStore.edit { preferences ->
                                    preferences[DataStore.emailKey] = ""
                                    preferences[DataStore.pwdKey] = ""
                                }
                                navController.navigate(TravelScreen.Page0.name)
                            } else {
                                onErrorOccurred()
                            }
                        }
                    }
                ) {
                    Text(text = "확인", fontSize = 20.sp)
                }
                TextButton(
                    onClick = {
                        onDismiss()
                    }
                ) {
                    Text(text = "취소", fontSize = 20.sp)
                }
            }
        },
    )
}

