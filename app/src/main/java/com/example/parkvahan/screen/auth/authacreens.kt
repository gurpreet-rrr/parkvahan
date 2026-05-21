package com.example.parkvahan.screen.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.parkvahan.data.model.UserRole
import com.example.parkvahan.ui.theme.ParkVahanColors
import com.example.parkvahan.viewmodel.AuthViewModel

// ═══════════════════════════════════════════════════════════════════════════════
// LOGIN SCREEN
// ═══════════════════════════════════════════════════════════════════════════════
@Composable
fun LoginScreen(
    onLoginAsOwner       : () -> Unit,
    onLoginAsUser        : () -> Unit,
    onNavigateToRegister : () -> Unit,
    viewModel            : AuthViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current

    var email          by remember { mutableStateOf("") }
    var password       by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var selectedRole   by remember { mutableStateOf(UserRole.USER) }

    val uiState by viewModel.uiState.collectAsState()

    // ✅ Navigate on success based on selected role
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            if (selectedRole == UserRole.OWNER) onLoginAsOwner()
            else onLoginAsUser()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF080808))
    ) {
        // Background glow
        Box(
            modifier = Modifier
                .size(400.dp)
                .align(Alignment.TopEnd)
                .offset(x = 100.dp, y = (-60).dp)
                .background(
                    Brush.radialGradient(
                        listOf(ParkVahanColors.Primary.copy(alpha = 0.10f), Color.Transparent)
                    ),
                    CircleShape
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(72.dp))

            // Logo
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(ParkVahanColors.Primary, Color(0xFFE65100))
                        )
                    )
            ) {
                Text("P", color = Color.Black, fontSize = 30.sp, fontWeight = FontWeight.Black)
            }

            Spacer(Modifier.height(24.dp))

            Text(
                "Welcome Back",
                color      = Color.White,
                fontSize   = 28.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-0.5).sp
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "Sign in to continue",
                color    = Color.White.copy(alpha = 0.45f),
                fontSize = 14.sp
            )

            Spacer(Modifier.height(36.dp))

            // Role selector
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White.copy(alpha = 0.06f))
                    .border(1.dp, Color.White.copy(alpha = 0.10f), RoundedCornerShape(14.dp))
            ) {
                Row(modifier = Modifier.fillMaxSize()) {
                    listOf(UserRole.USER to "User", UserRole.OWNER to "Owner").forEach { (role, label) ->
                        val isSelected = selectedRole == role
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .padding(4.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    if (isSelected) Brush.horizontalGradient(
                                        listOf(ParkVahanColors.Primary, Color(0xFFE65100))
                                    ) else Brush.horizontalGradient(
                                        listOf(Color.Transparent, Color.Transparent)
                                    )
                                )
                                .clickable { selectedRole = role }
                        ) {
                            Text(
                                label,
                                color      = if (isSelected) Color.Black else Color.White.copy(alpha = 0.5f),
                                fontSize   = 14.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(28.dp))

            // Email
            AuthTextField(
                value       = email,
                onValueChange = { email = it },
                placeholder = "Email address",
                leadingIcon = {
                    Icon(Icons.Default.Email, null,
                        tint     = ParkVahanColors.Primary.copy(alpha = 0.7f),
                        modifier = Modifier.size(20.dp))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction    = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )

            Spacer(Modifier.height(14.dp))

            // Password
            AuthTextField(
                value         = password,
                onValueChange = { password = it },
                placeholder   = "Password",
                leadingIcon   = {
                    Icon(Icons.Default.Lock, null,
                        tint     = ParkVahanColors.Primary.copy(alpha = 0.7f),
                        modifier = Modifier.size(20.dp))
                },
                trailingIcon  = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            null,
                            tint     = Color.White.copy(alpha = 0.4f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction    = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
            )

            Spacer(Modifier.height(10.dp))

            // Forgot password
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                Text(
                    "Forgot password?",
                    color      = ParkVahanColors.Primary.copy(alpha = 0.8f),
                    fontSize   = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier   = Modifier.clickable { }
                )
            }

            Spacer(Modifier.height(28.dp))

            // Error
            AnimatedVisibility(visible = uiState.error != null) {
                Text(
                    uiState.error ?: "",
                    color     = ParkVahanColors.Error,
                    fontSize  = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier  = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                )
            }

            // Sign in button
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(ParkVahanColors.Primary, Color(0xFFE65100))
                        )
                    )
                    .clickable(enabled = !uiState.isLoading) {
                        viewModel.login(email.trim(), password, selectedRole)
                    }
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        color       = Color.Black,
                        modifier    = Modifier.size(24.dp),
                        strokeWidth = 2.5.dp
                    )
                } else {
                    Text(
                        "Sign In",
                        color      = Color.Black,
                        fontSize   = 16.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Spacer(Modifier.height(28.dp))

            // Divider
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier          = Modifier.fillMaxWidth()
            ) {
                Divider(modifier = Modifier.weight(1f), color = Color.White.copy(alpha = 0.10f))
                Text("  or  ", color = Color.White.copy(alpha = 0.3f), fontSize = 12.sp)
                Divider(modifier = Modifier.weight(1f), color = Color.White.copy(alpha = 0.10f))
            }

            Spacer(Modifier.height(28.dp))

            // Register link
            Row {
                Text(
                    "Don't have an account? ",
                    color    = Color.White.copy(alpha = 0.45f),
                    fontSize = 14.sp
                )
                Text(
                    "Register",
                    color      = ParkVahanColors.Primary,
                    fontSize   = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier   = Modifier.clickable { onNavigateToRegister() }
                )
            }

            Spacer(Modifier.height(48.dp))
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// REGISTER SCREEN
// ═══════════════════════════════════════════════════════════════════════════════
@Composable
fun RegisterScreen(
    onRegisteredAsOwner : () -> Unit,
    onRegisteredAsUser  : () -> Unit,
    onNavigateBack      : () -> Unit,
    viewModel           : AuthViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current

    var name            by remember { mutableStateOf("") }
    var email           by remember { mutableStateOf("") }
    var password        by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible  by remember { mutableStateOf(false) }
    var confirmVisible   by remember { mutableStateOf(false) }
    var selectedRole    by remember { mutableStateOf(UserRole.USER) }

    val uiState         by viewModel.uiState.collectAsState()
    val passwordsMatch  = confirmPassword.isEmpty() || password == confirmPassword
    val passwordStrength = getPasswordStrength(password)

    // ✅ Navigate on success based on selected role
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            if (selectedRole == UserRole.OWNER) onRegisteredAsOwner()
            else onRegisteredAsUser()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF080808))
    ) {
        // Background glow
        Box(
            modifier = Modifier
                .size(350.dp)
                .align(Alignment.TopStart)
                .offset(x = (-80).dp, y = (-80).dp)
                .background(
                    Brush.radialGradient(
                        listOf(Color(0xFF1DBF73).copy(alpha = 0.09f), Color.Transparent)
                    ),
                    CircleShape
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(56.dp))

            // Back button
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.07f))
                        .clickable { onNavigateBack() }
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint     = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(Modifier.height(28.dp))

            // Header
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text(
                        "Create Account",
                        color         = Color.White,
                        fontSize      = 28.sp,
                        fontWeight    = FontWeight.Black,
                        letterSpacing = (-0.5).sp
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Join ParkVahan today",
                        color    = Color.White.copy(alpha = 0.4f),
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(Modifier.height(28.dp))

            // Role selector
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White.copy(alpha = 0.06f))
                    .border(1.dp, Color.White.copy(alpha = 0.10f), RoundedCornerShape(14.dp))
            ) {
                Row(modifier = Modifier.fillMaxSize()) {
                    listOf(UserRole.USER to ("User" to "👤"), UserRole.OWNER to ("Owner" to "🏢"))
                        .forEach { (role, pair) ->
                            val (label, emoji) = pair
                            val isSelected = selectedRole == role
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .padding(4.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        if (isSelected) Brush.horizontalGradient(
                                            listOf(ParkVahanColors.Primary, Color(0xFFE65100))
                                        ) else Brush.horizontalGradient(
                                            listOf(Color.Transparent, Color.Transparent)
                                        )
                                    )
                                    .clickable { selectedRole = role }
                            ) {
                                Row(
                                    verticalAlignment     = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(emoji, fontSize = 14.sp)
                                    Text(
                                        label,
                                        color      = if (isSelected) Color.Black else Color.White.copy(alpha = 0.5f),
                                        fontSize   = 14.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                }
                            }
                        }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Name
            AuthTextField(
                value         = name,
                onValueChange = { name = it },
                placeholder   = "Full name",
                leadingIcon   = {
                    Icon(Icons.Default.Person, null,
                        tint     = ParkVahanColors.Primary.copy(alpha = 0.7f),
                        modifier = Modifier.size(20.dp))
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )

            Spacer(Modifier.height(14.dp))

            // Email
            AuthTextField(
                value         = email,
                onValueChange = { email = it },
                placeholder   = "Email address",
                leadingIcon   = {
                    Icon(Icons.Default.Email, null,
                        tint     = ParkVahanColors.Primary.copy(alpha = 0.7f),
                        modifier = Modifier.size(20.dp))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction    = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )

            Spacer(Modifier.height(14.dp))

            // Password
            AuthTextField(
                value         = password,
                onValueChange = { password = it },
                placeholder   = "Password",
                leadingIcon   = {
                    Icon(Icons.Default.Lock, null,
                        tint     = ParkVahanColors.Primary.copy(alpha = 0.7f),
                        modifier = Modifier.size(20.dp))
                },
                trailingIcon  = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            null,
                            tint     = Color.White.copy(alpha = 0.4f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction    = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )

            // Password strength bar
            AnimatedVisibility(visible = password.isNotEmpty()) {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        repeat(4) { i ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(3.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(
                                        if (i < passwordStrength.level) passwordStrength.color
                                        else Color.White.copy(alpha = 0.12f)
                                    )
                            )
                        }
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(
                        passwordStrength.label,
                        color      = passwordStrength.color,
                        fontSize   = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            // Confirm password
            AuthTextField(
                value         = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder   = "Confirm password",
                leadingIcon   = {
                    Icon(
                        if (confirmPassword.isNotEmpty() && passwordsMatch)
                            Icons.Default.CheckCircle else Icons.Default.Lock,
                        null,
                        tint     = if (confirmPassword.isNotEmpty() && passwordsMatch)
                            ParkVahanColors.Success
                        else ParkVahanColors.Primary.copy(alpha = 0.7f),
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon  = {
                    IconButton(onClick = { confirmVisible = !confirmVisible }) {
                        Icon(
                            if (confirmVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            null,
                            tint     = Color.White.copy(alpha = 0.4f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                visualTransformation = if (confirmVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction    = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
            )

            AnimatedVisibility(visible = confirmPassword.isNotEmpty() && !passwordsMatch) {
                Text(
                    "Passwords don't match",
                    color    = ParkVahanColors.Error,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp).fillMaxWidth()
                )
            }

            Spacer(Modifier.height(28.dp))

            // Error
            AnimatedVisibility(visible = uiState.error != null) {
                Text(
                    uiState.error ?: "",
                    color     = ParkVahanColors.Error,
                    fontSize  = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier  = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                )
            }

            // Register button
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(ParkVahanColors.Primary, Color(0xFFE65100))
                        )
                    )
                    .clickable(
                        enabled = !uiState.isLoading &&
                                name.isNotBlank() &&
                                email.isNotBlank() &&
                                password.isNotBlank() &&
                                password == confirmPassword
                    ) {
                        viewModel.register(name.trim(), email.trim(), password, selectedRole)
                    }
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        color       = Color.Black,
                        modifier    = Modifier.size(24.dp),
                        strokeWidth = 2.5.dp
                    )
                } else {
                    Text(
                        "Create Account",
                        color         = Color.Black,
                        fontSize      = 16.sp,
                        fontWeight    = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // Sign in link
            Row {
                Text(
                    "Already have an account? ",
                    color    = Color.White.copy(alpha = 0.45f),
                    fontSize = 14.sp
                )
                Text(
                    "Sign In",
                    color      = ParkVahanColors.Primary,
                    fontSize   = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier   = Modifier.clickable { onNavigateBack() }
                )
            }

            Spacer(Modifier.height(48.dp))
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// SHARED — Reusable text field for auth screens
// ═══════════════════════════════════════════════════════════════════════════════
@Composable
fun AuthTextField(
    value                : String,
    onValueChange        : (String) -> Unit,
    placeholder          : String,
    leadingIcon          : @Composable (() -> Unit)? = null,
    trailingIcon         : @Composable (() -> Unit)? = null,
    visualTransformation : VisualTransformation     = VisualTransformation.None,
    keyboardOptions      : KeyboardOptions           = KeyboardOptions.Default,
    keyboardActions      : KeyboardActions           = KeyboardActions.Default,
) {
    OutlinedTextField(
        value         = value,
        onValueChange = onValueChange,
        modifier      = Modifier.fillMaxWidth(),
        placeholder   = {
            Text(placeholder, color = Color.White.copy(alpha = 0.3f), fontSize = 14.sp)
        },
        leadingIcon          = leadingIcon,
        trailingIcon         = trailingIcon,
        visualTransformation = visualTransformation,
        keyboardOptions      = keyboardOptions,
        keyboardActions      = keyboardActions,
        singleLine           = true,
        shape                = RoundedCornerShape(14.dp),
        colors               = OutlinedTextFieldDefaults.colors(
            focusedTextColor      = Color.White,
            unfocusedTextColor    = Color.White,
            focusedBorderColor    = ParkVahanColors.Primary.copy(alpha = 0.7f),
            unfocusedBorderColor  = Color.White.copy(alpha = 0.12f),
            cursorColor           = ParkVahanColors.Primary,
            focusedContainerColor = Color.White.copy(alpha = 0.04f),
            unfocusedContainerColor = Color.White.copy(alpha = 0.04f)
        )
    )
}

// ═══════════════════════════════════════════════════════════════════════════════
// PASSWORD STRENGTH HELPER
// ═══════════════════════════════════════════════════════════════════════════════
data class PasswordStrengthData(
    val level : Int,
    val label : String,
    val color : androidx.compose.ui.graphics.Color
)

fun getPasswordStrength(password: String): PasswordStrengthData {
    if (password.length < 6) return PasswordStrengthData(1, "Too short",  ParkVahanColors.Error)
    var score = 0
    if (password.length >= 8)              score++
    if (password.any { it.isUpperCase() }) score++
    if (password.any { it.isDigit() })     score++
    if (password.any { !it.isLetterOrDigit() }) score++
    return when (score) {
        0, 1 -> PasswordStrengthData(1, "Weak",   ParkVahanColors.Error)
        2    -> PasswordStrengthData(2, "Fair",   ParkVahanColors.Warning)
        3    -> PasswordStrengthData(3, "Good",   ParkVahanColors.Success)
        else -> PasswordStrengthData(4, "Strong", ParkVahanColors.Info)
    }
}