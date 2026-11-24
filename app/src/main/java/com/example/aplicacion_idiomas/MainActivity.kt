package com.example.aplicacion_idiomas

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Adb
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.aplicacion_idiomas.ui.theme.Aplicacion_idiomasTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "MainActivity"

// Número con código de país, sin "+", espacios ni guiones (ejemplo: 52 + 10 dígitos)
private const val WHATSAPP_NUMBER = "4498945431"

class MainActivity : ComponentActivity() {

    private var keepSplash = true

    override fun onCreate(savedInstanceState: Bundle?) {
        // El splash se instala ANTES de super.onCreate()
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { keepSplash }

        lifecycleScope.launch {
            delay(3000)
            keepSplash = false
        }

        super.onCreate(savedInstanceState)
        showToast("Función onCreate")
        enableEdgeToEdge()
        setContent {
            Aplicacion_idiomasTheme {
                ViewContainer(
                    onBackClick = { onBackPressedDispatcher.onBackPressed() }
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        showToast("Función onStart")
    }

    override fun onResume() {
        super.onResume()
        showToast("Función onResume")
    }

    override fun onPause() {
        super.onPause()
        showToast("Función onPause")
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        Log.i(TAG, message)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewContainer(onBackClick: () -> Unit = {}) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val mensajeSnackbar = stringResource(id = R.string.snackbar_msg)
    val mensajeWhatsApp = stringResource(id = R.string.whatsapp_message)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "Menú de navegación",
                    modifier = Modifier.padding(16.dp)
                )
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text("Inicio") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.home_screen)) },
                    colors = topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        titleContentColor = Color.White
                    ),
                    actions = {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = stringResource(R.string.favorite_desc),
                                tint = Color.White
                            )
                        }
                        IconButton(onClick = {
                            context.startActivity(Intent(context, SecondActivity::class.java))
                        }) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = stringResource(R.string.share_desc),
                                tint = Color.White
                            )
                        }
                        DropDownMenu(
                            onWhatsAppClick = {
                                sendWhatsApp(context, WHATSAPP_NUMBER, mensajeWhatsApp)
                            }
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = "Abrir menú",
                                tint = Color.White
                            )
                        }
                    }
                )
            },
            bottomBar = {
                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = Color.White
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = stringResource(id = R.string.bottom_app_bar),
                        fontSize = 18.sp
                    )
                }
            },
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar(mensajeSnackbar)
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.fab_desc)
                    )
                }
            }
        ) { innerPadding ->
            // El contenido respeta las barras superior e inferior
            ContentView(modifier = Modifier.padding(innerPadding))
        }
    }
}

fun openWebScreen(context: Context) {
    val url = Uri.parse("https://aguascalientes.tecnm.mx/")
    val intent = Intent(Intent.ACTION_VIEW, url)
    context.startActivity(intent)
}

fun sendWhatsApp(context: Context, toNumber: String, text: String) {
    val url = "https://wa.me/$toNumber?text=${Uri.encode(text)}"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
        setPackage("com.whatsapp")
    }
    try {
        context.startActivity(intent)
    } catch (_: ActivityNotFoundException) {
        // WhatsApp no está instalado en el dispositivo
        Toast.makeText(
            context,
            context.getString(R.string.whatsapp_not_installed),
            Toast.LENGTH_SHORT
        ).show()
    }
}

fun openCamera(context: Context) {
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    try {
        context.startActivity(intent)
    } catch (_: ActivityNotFoundException) {
        Toast.makeText(
            context,
            context.getString(R.string.camera_not_available),
            Toast.LENGTH_SHORT
        ).show()
    }
}

fun openFacebook(context: Context) {
    val url = Uri.parse("https://www.facebook.com/")
    val appIntent = Intent(Intent.ACTION_VIEW, url).apply {
        setPackage("com.facebook.katana")
    }
    try {
        context.startActivity(appIntent)
    } catch (_: ActivityNotFoundException) {
        // Si Facebook no está instalado, se abre en el navegador
        context.startActivity(Intent(Intent.ACTION_VIEW, url))
    }
}

@Composable
fun DropDownMenu(onWhatsAppClick: () -> Unit = {}) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = stringResource(R.string.options_menu),
                tint = Color.White
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.profile)) },
                onClick = {
                    expanded = false
                    openCamera(context)
                },
                leadingIcon = {
                    Icon(Icons.Default.Image, contentDescription = null)
                }
            )

            DropdownMenuItem(
                text = { Text(stringResource(R.string.config)) },
                onClick = {
                    expanded = false
                    openFacebook(context)
                },
                leadingIcon = {
                    Icon(Icons.Default.Adb, contentDescription = null)
                }
            )

            DropdownMenuItem(
                text = { Text(stringResource(R.string.whatsapp_menu_item)) },
                onClick = {
                    expanded = false
                    onWhatsAppClick()
                },
                leadingIcon = {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null)
                }
            )

            HorizontalDivider()

            DropdownMenuItem(
                text = { Text(stringResource(R.string.logout)) },
                onClick = {
                    expanded = false
                    // Acción para Cerrar sesión
                },
                leadingIcon = {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null)
                }
            )
        }
    }
}

@Composable
fun ContentView(modifier: Modifier = Modifier) {
    var counter by rememberSaveable { mutableIntStateOf(0) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Red) // Solo para pruebas; luego usa MaterialTheme.colorScheme.background
    ) {
        item {
            Image(
                painter = painterResource(id = R.drawable.comida),
                contentDescription = stringResource(R.string.food_desc),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            )
        }

        item {
            Row(
                modifier = Modifier.padding(top = 2.dp, start = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.sol),
                    contentDescription = stringResource(R.string.like_desc),
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { counter++ }
                )
                Text(
                    text = counter.toString(),
                    fontSize = 20.sp,
                    color = Color.White,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        item {
            Text(
                text = stringResource(R.string.app_name),
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
        item {
            Text(
                text = stringResource(R.string.nombre),
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
        item {
            Text(
                text = stringResource(R.string.apellido),
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        item {
            val imagenes = listOf(
                R.drawable.soda,
                R.drawable.limonada,
                R.drawable.cabeza,
                R.drawable.chats,
                R.drawable.soda,
                R.drawable.limonada
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                items(imagenes) { img ->
                    Image(
                        painter = painterResource(id = img),
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .padding(5.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, locale = "es-rMX")
@Composable
fun ViewContainerPreview() {
    Aplicacion_idiomasTheme {
        ViewContainer()
    }
}
