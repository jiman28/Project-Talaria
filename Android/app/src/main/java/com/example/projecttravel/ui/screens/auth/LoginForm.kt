package com.example.projecttravel.ui.screens.auth

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import com.example.projecttravel.model.User
import com.example.projecttravel.ui.screens.auth.api.loginApiCall
import com.example.projecttravel.data.uistates.UserPageUiState
import com.example.projecttravel.ui.viewmodels.UserPageViewModel
import com.example.projecttravel.ui.screens.auth.datastore.DataStore.Companion.dataStore
import com.example.projecttravel.ui.screens.auth.datastore.DataStore.Companion.emailKey
import com.example.projecttravel.ui.screens.auth.datastore.DataStore.Companion.pwdKey
import com.example.projecttravel.ui.screens.GlobalLoadingDialog
import com.example.projecttravel.ui.screens.TextMsgErrorDialog
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@Composable
fun LoginForm(
    userPageUiState: UserPageUiState,
    userPageViewModel: UserPageViewModel,
    onLoginSuccess: () -> Unit,
    onNextButtonClicked: () -> Unit,
) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val dataStore = (context).dataStore

    var credentials by remember { mutableStateOf(Credentials()) }
    var isLoadingState by remember { mutableStateOf<Boolean?>(null) }
    var logInErrorMsg by remember { mutableStateOf("") }

    // 정규 이메일 표현식 패턴
    val emailPattern = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"

    LaunchedEffect(Unit) {
        dataStore.data.collect {preferences ->
            credentials.email = preferences[emailKey] ?: ""
            credentials.pwd = preferences[pwdKey] ?: ""
            if (credentials.email != "" && credentials.pwd != "") {
                scope.launch {
                    isLoadingState = true
                    val sendUser = User(
                        email = credentials.email,
                        password = credentials.pwd, // Consider changing the names here if needed
                    )
                    val userDeferred = async { loginApiCall(sendUser, userPageUiState, userPageViewModel) }
                    val isUserComplete = userDeferred.await()
                    if (isUserComplete) {
                        onLoginSuccess()
                    } else {
                        logInErrorMsg = "로그인 실패\n다시 시도해주세요"
                        isLoadingState = false
                    }
                }
            }
        }
    }

    Surface {
        when (isLoadingState) {
            true -> GlobalLoadingDialog()
            false -> TextMsgErrorDialog( txtErrorMsg = logInErrorMsg, onDismiss = { isLoadingState = null } )
            else -> isLoadingState = null
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp)
    ) {
        LoginField(
            value = credentials.email,
            onChange = { data -> credentials = credentials.copy(email = data) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        PasswordField(
            value = credentials.pwd,
            onChange = { data -> credentials = credentials.copy(pwd = data) },
            submit = {
                if (!checkCredentials(credentials, context)) credentials = Credentials()
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        LabeledCheckbox(
            label = "Remember Me",
            onCheckChanged = {
                credentials = credentials.copy(remember = !credentials.remember)
            },
            isChecked = credentials.remember
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                scope.launch {
                    if (credentials.remember) {
                        dataStore.edit { preferences ->
                            preferences[emailKey] = credentials.email
                            preferences[pwdKey] = credentials.pwd
                        }
                    }
                    isLoadingState = true
                    val sendUser = User(
                        email = credentials.email,
                        password = credentials.pwd, // Consider changing the names here if needed
                    )
                    val userDeferred = async { loginApiCall(sendUser, userPageUiState, userPageViewModel) }
                    val isUserComplete = userDeferred.await()
//                    if (isUserComplete && userUiState.currentLogin != null) { // 로그에는 null 이 들어오는데 정상적으로 데이터는 들어옴. 뭔가 이상함.
                    if (isUserComplete) {
                        onLoginSuccess()
                    } else {
                        logInErrorMsg = "로그인 실패\n아이디나 비밀번호를\n다시 확인해주세요"
                        isLoadingState = false
                    }
                }
            },
            enabled = credentials.isNotEmpty() && credentials.email.matches(emailPattern.toRegex()),
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }
        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { onNextButtonClicked() },
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign in")
        }
        Spacer(modifier = Modifier.height(80.dp))
    }
}

fun checkCredentials(creds: Credentials, context: Context): Boolean {
    return if (creds.isNotEmpty()) {
        true
    } else {
        Toast.makeText(context, "잘못된 형식입니다", Toast.LENGTH_SHORT).show()
        false
    }
}

data class Credentials(
    var email: String = "",
    var pwd: String = "",
    var remember: Boolean = false,
) {
    fun isNotEmpty(): Boolean {
        return email.isNotEmpty() && pwd.isNotEmpty()
    }
}

@Composable
fun LabeledCheckbox(
    label: String,
    onCheckChanged: () -> Unit,
    isChecked: Boolean,
) {
    Row(
        Modifier
            .clickable(
                onClick = onCheckChanged
            )
            .padding(4.dp)
    ) {
        Checkbox(checked = isChecked, onCheckedChange = null)
        Spacer(Modifier.size(6.dp))
        Text(label)
    }
}

@Composable
fun LoginField(
    value: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "E-mail",
    placeholder: String = "Enter your E-mail",
) {

    val focusManager = LocalFocusManager.current
    val leadingIcon = @Composable {
        Icon(
            Icons.Default.Person,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.primary
        )
    }

    TextField(
        value = value,
        onValueChange = onChange,
        modifier = modifier,
        leadingIcon = leadingIcon,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next // 다음 버튼 활성화
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) } // 다음 버튼 클릭시 다음 TextField로 커서 이동
        ),
        placeholder = { Text(placeholder) },
        label = { Text(label) },
        singleLine = true,
        visualTransformation = VisualTransformation.None
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PasswordField(
    value: String,
    onChange: (String) -> Unit,
    submit: () -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Password",
    placeholder: String = "Enter your Password",
) {

    var isPasswordVisible by remember { mutableStateOf(false) }

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

    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        value = value,
        onValueChange = onChange,
        modifier = modifier,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done, // 완료 버튼 활성화
            keyboardType = KeyboardType.Password // 비밀번호 모드 활성화
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                submit()
                keyboardController?.hide() // 완료 버튼 클릭 시 키보드 숨김 (없어도 되지만 다른 함수도 같이 사용해야 할때 필요)
            }
        ),
        placeholder = { Text(placeholder) },
        label = { Text(label) },
        singleLine = true,
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
    )
}
