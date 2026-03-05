package com.example.obdapp.ui.bluetooth

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BluetoothDisabled
import androidx.compose.material.icons.filled.BluetoothSearching
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.obdapp.bluetoothmanager.BluetoothViewModel
import com.example.obdapp.ui.engine.textPrimary
import com.example.obdapp.ui.navigation.Route
import com.example.obdapp.ui.theme.AppBgDark


@Composable
fun BluetoothPermissionAnimatedScreen(
    onRequestPermission: () -> Unit
) {
    val AccentBlue = Color(0xFF007AFF)
    val AppBg = Color(0xFFF5F5F5)
    val textPrimary = Color(0xFF1A1A1A)
    val textSecondary = Color(0xFF555555)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBg)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(28.dp),
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)

        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Icon(
                    imageVector = Icons.Default.BluetoothSearching,
                    contentDescription = null,
                    tint = AccentBlue,
                    modifier = Modifier.size(100.dp)
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    "Permission Required",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    "We need Bluetooth and Location permission to scan nearby car devices.\n" +
                            "Location is required by Android and is not used for tracking.",
                    fontSize = 16.sp,
                    color = textSecondary,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = onRequestPermission,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentBlue)
                ) {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = Color.White)
                    Spacer(Modifier.width(8.dp))
                    Text("Allow Permissions", color = Color.White)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BluetoothFlowScreen(
    navController: NavController,
    viewModel: BluetoothViewModel = viewModel()
) {
    val context = LocalContext.current
    val activity = context as Activity

    var permissionGranted by remember {
        mutableStateOf(hasAllPermissions(context))
    }

    var bluetoothEnabled by remember {
        mutableStateOf(isBluetoothEnabled())
    }

    val devices = remember { mutableStateListOf<BluetoothDevice>() }

    var showSettingsDialog by remember { mutableStateOf(false) }

    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val granted = result.values.all { it }

        if (granted) {
            permissionGranted = true
        } else {
            val permanentlyDenied = requiredPermissions().any { permission ->
                !ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
            }

            if (permanentlyDenied) {
                showSettingsDialog = true
            }
        }
    }

    // Bluetooth enable launcher
    val enableBluetoothLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        bluetoothEnabled = isBluetoothEnabled()
    }

    LaunchedEffect(bluetoothEnabled) {
        if (bluetoothEnabled) {
            startBluetoothScan(context) { device ->
                if (!devices.any { it.address == device.address }) {
                    devices.add(device)
                }
            }
        }
    }

    // ===== Scaffold =====
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bluetooth Devices") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppBgDark,   // 👈 same as background
                    titleContentColor = textPrimary,
                    navigationIconContentColor = textPrimary,
                    actionIconContentColor = textPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            when {
                !permissionGranted -> {
                    BluetoothPermissionAnimatedScreen {
                        permissionLauncher.launch(requiredPermissions())
                    }
                }

                !bluetoothEnabled -> {
                    EnableBluetoothScreen {
                        val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                        enableBluetoothLauncher.launch(intent)
                    }
                }

                else -> {
                    BluetoothScanScreen(
                        devices = devices,
                        viewModel = viewModel,
                        navController = navController
                    ) {
                        navController.navigate(Route.Home.name)
                    }
                }
            }

            // Settings dialog
            if (showSettingsDialog) {
                AlertDialog(
                    onDismissRequest = {},
                    title = { Text("Permission Required") },
                    text = { Text("Please enable permissions in Settings") },
                    confirmButton = {
                        TextButton(onClick = {
                            val intent = Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", context.packageName, null)
                            )
                            context.startActivity(intent)
                        }) {
                            Text("Open Settings")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun EnableBluetoothScreen(
    onEnableBluetooth: () -> Unit
) {
    val AccentBlue = Color(0xFF007AFF)
    val AppBg = Color(0xFFF5F5F5)
    val textPrimary = Color(0xFF1A1A1A)
    val textSecondary = Color(0xFF555555)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBg),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(28.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            elevation = CardDefaults.cardElevation(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Icon(
                    imageVector = Icons.Default.BluetoothDisabled,
                    contentDescription = null,
                    tint = Color.Red,
                    modifier = Modifier.size(100.dp)
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    "Bluetooth is turned off",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    "Please enable Bluetooth to connect to your vehicle.",
                    fontSize = 16.sp,
                    color = textSecondary,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = onEnableBluetooth,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentBlue)
                ) {
                    Icon(
                        Icons.Default.PowerSettingsNew,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Turn On Bluetooth", color = Color.White)
                }
            }
        }
    }
}
fun requiredPermissions(): Array<String> {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        arrayOf(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.ACCESS_FINE_LOCATION // still recommended for some devices
        )
    } else {
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }
}
fun hasAllPermissions(context: Context): Boolean {
    return requiredPermissions().all {
        ContextCompat.checkSelfPermission(
            context, it
        ) == PackageManager.PERMISSION_GRANTED
    }
}
fun isBluetoothEnabled(): Boolean {
    val adapter = BluetoothAdapter.getDefaultAdapter()
    return adapter != null && adapter.isEnabled
}