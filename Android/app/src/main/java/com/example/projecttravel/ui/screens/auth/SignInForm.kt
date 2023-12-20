package com.example.projecttravel.ui.screens.auth

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
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
import com.example.projecttravel.model.SendSignIn
import com.example.projecttravel.ui.screens.GlobalLoadingDialog
import com.example.projecttravel.ui.screens.TextMsgErrorDialog
import com.example.projecttravel.ui.screens.auth.api.signInApiCall
import com.example.projecttravel.ui.viewmodels.UserPageViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@Composable
fun SignInForm(
    userPageViewModel: UserPageViewModel,
    onNextButtonClicked: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var signCredentials by remember { mutableStateOf(SignCredentials()) }
    var isLoadingState by remember { mutableStateOf<Boolean?>(null) }
    var signInErrorMsg by remember { mutableStateOf("") }

    // 정규 이메일 표현식 패턴
    val emailPattern = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"

    Column {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp, vertical = 50.dp)
        ) {

            SignIdField(
                value = signCredentials.name,
                onChange = { data -> signCredentials = signCredentials.copy(name = data) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            SignPasswordField(
                value = signCredentials.pwd,
                onChange = { data -> signCredentials = signCredentials.copy(pwd = data) },
                label = "Password",
                placeholder = "Enter your Password",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            SignPasswordField(
                value = signCredentials.pwdCheck,
                onChange = { data -> signCredentials = signCredentials.copy(pwdCheck = data) },
                label = "Password Check",
                placeholder = "Check your Password",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            SignEmailField(
                value = signCredentials.email,
                onChange = { data -> signCredentials = signCredentials.copy(email = data) },
                submit = {
                    if (!checkSignCredentials(signCredentials, context)) signCredentials =
                        SignCredentials()
                },
                modifier = Modifier.fillMaxWidth()
            )


            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    scope.launch {
                        if (signCredentials.pwd == signCredentials.pwdCheck) {
                            isLoadingState = true
                            val sendSignIn = SendSignIn(
                                email = signCredentials.email,
                                name = signCredentials.name, // Consider changing the names here if needed
                                password = signCredentials.pwd, // Consider changing the names here if needed
                            )
                            val signInDeferred =
                                async { signInApiCall(sendSignIn) }
                            val signInComplete = signInDeferred.await()

                            when (signInComplete) {
                                "a" -> {
                                    isLoadingState = null
                                    userPageViewModel.setCurrentSignIn(sendSignIn)
                                    onNextButtonClicked()
                                }

                                "b" -> {
                                    signInErrorMsg = "중복된 E-mail입니다"
                                    isLoadingState = false
                                }

                                "d" -> {
                                    signInErrorMsg = "회원가입 실패\n다시 시도해주세요 "
                                    isLoadingState = false
                                }

                                else -> {
                                    signInErrorMsg = "오류 발생"
                                    isLoadingState = false
                                }
                            }
                        } else {
                            Toast.makeText(context, "비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                enabled = signCredentials.isNotEmpty() && signCredentials.email.matches(emailPattern.toRegex()),
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign in")
            }
        }
    }
    Surface {
        when (isLoadingState) {
            true -> GlobalLoadingDialog()
            false -> TextMsgErrorDialog(
                txtErrorMsg = signInErrorMsg,
                onDismiss = { isLoadingState = null })

            else -> isLoadingState = null
        }
    }
}

fun checkSignCredentials(signCredentials: SignCredentials, context: Context): Boolean {
    if (signCredentials.isNotEmpty()) {
        return true
    } else {
        Toast.makeText(context, "모든 항목을 채우세요", Toast.LENGTH_SHORT).show()
        return false
    }
}

data class SignCredentials(
    var name: String = "",
    var pwd: String = "",
    var pwdCheck: String = "",
    var email: String = "",
) {
    fun isNotEmpty(): Boolean {
        return name.isNotEmpty() && email.isNotEmpty() && pwd.isNotEmpty() && pwdCheck.isNotEmpty()
    }
}

@Composable
fun SignIdField(
    value: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Name",
    placeholder: String = "Enter your Name",
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
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        ),
        placeholder = { Text(placeholder) },
        label = { Text(label) },
        singleLine = true,
        visualTransformation = VisualTransformation.None
    )
}

@Composable
fun SignPasswordField(
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignEmailField(
    value: String,
    onChange: (String) -> Unit,
    submit: () -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Email",
    placeholder: String = "Enter your Email",
) {

    val leadingIcon = @Composable {
        Icon(
            Icons.Default.Person,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.primary
        )
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        value = value,
        onValueChange = onChange,
        modifier = modifier,
        leadingIcon = leadingIcon,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
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
        visualTransformation = VisualTransformation.None
    )
}
