package com.example.androidcase2

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

class MainActivity : ComponentActivity() {
    private lateinit var kategoriRepository: KategoriRepository
    private lateinit var parcaRepository: ParcaRepository
    private lateinit var kategorikParcalar: MutableList<Parca>
    var flag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        kategoriRepository = KategoriRepository(this)
        parcaRepository = ParcaRepository(this)

        setContent {
            DataFilteringSampleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    kategorikParcalar = parcaRepository.ParcalarByKategoriID(1)
                    Log.e("TAG", kategorikParcalar.toString())

                    MainScreen()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun MainScreen() {
        val kategoriler = kategoriRepository.GetKategoriler()
        val expanded = remember { mutableStateOf(false) }
        val secilenKategori = remember { mutableStateOf(kategoriler[0]) }

        Column(
            Modifier
                .fillMaxSize()
                .background(Color.DarkGray),
            Arrangement.Top,
            Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Magenta)
                    .height(35.dp)
            ) {
                Row(
                    Modifier
                        .clickable {
                            expanded.value = !expanded.value
                        }
                        .align(Alignment.TopStart)
                ) {
                    Text(text = secilenKategori.value.Aciklama)
                    Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = null)
                }
                DropdownMenu(expanded = expanded.value,
                    onDismissRequest = { expanded.value = false }) {
                    kategoriler.forEach { category ->
                        DropdownMenuItem(onClick = {
                            secilenKategori.value = category
                            expanded.value = false
                            kategorikParcalar = parcaRepository.ParcalarByKategoriID(secilenKategori.value.K_ID)
                        }) {
                            Text(text = category.Aciklama)
                        }
                    }
                }
            }
            ParcaListesiGoster(lst = kategorikParcalar)
            val showDialog = remember { mutableStateOf(false) }
            Scaffold(
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { showDialog.value = true },
                        modifier = Modifier
                            .size(64.dp)
                            .shadow(4.dp, CircleShape)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Ekle",
                            tint = Color.Blue
                        )
                    }
                }
            ) {
                if (showDialog.value) {
                    CustomDialog(value = "", setShowDialog = {
                        showDialog.value = it
                    }, {
                        Log.i("HomePage", "HomePage : $it")
                    }, secilenKategori)
                }
            }
        }
    }

    @Composable
    fun ParcaGoster(p: Parca) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            Text(text = p.Adi, style = MaterialTheme.typography.h6)
            Spacer(
                Modifier
                    .padding(1.dp)
                    .fillMaxWidth(0.7F)
                    .background(Color.DarkGray)
                    .height(0.5.dp)
            )
            Text(text = "Stok Adedi: ${p.StokAdedi}", style = MaterialTheme.typography.body1)
            Spacer(
                Modifier
                    .padding(1.dp)
                    .fillMaxWidth(0.7F)
                    .background(Color.DarkGray)
                    .height(0.5.dp)
            )
            Text(text = "Fiyatı: ${p.Fiyati} TL", style = MaterialTheme.typography.body1)
            Spacer(
                Modifier
                    .padding(1.dp)
                    .fillMaxWidth(0.7F)
                    .background(Color.Red)
                    .height(1.5.dp)
            )
        }
    }

    @Composable
    fun ParcaListesiGoster(lst: List<Parca>) {
        LazyColumn(
            Modifier.border(3.dp, Color.Blue),
            contentPadding = PaddingValues(5.dp),
            verticalArrangement = Arrangement.Bottom,
            userScrollEnabled = true
        ) {
            items(lst) { parca ->
                ParcaGoster(p = parca)
            }
        }
    }

    private fun toastMessage(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CustomDialog(
        value: String,
        setShowDialog: (Boolean) -> Unit,
        setValue: (String) -> Unit,
        secilenKategori: MutableState<Kategori>
    ) {
        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun CustomDialog(
            value: String,
            setShowDialog: (Boolean) -> Unit,
            setValue: (String) -> Unit,
            secilenKategori: MutableState<Kategori>
        ) {
            val txtFieldError = remember { mutableStateOf("") }
            val textName = remember { mutableStateOf("") }
            val textStock = remember { mutableStateOf("") }
            val textPrice = remember { mutableStateOf("") }
            val context = LocalContext.current

            Dialog(onDismissRequest = { setShowDialog(false) }) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Set value",
                                    style = TextStyle(
                                        fontSize = 24.sp,
                                        fontFamily = FontFamily.Default,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Icon(
                                    imageVector = Icons.Filled.Clear,
                                    contentDescription = "",
                                    tint = colorResource(android.R.color.darker_gray),
                                    modifier = Modifier
                                        .width(30.dp)
                                        .height(30.dp)
                                        .clickable { setShowDialog(false) }
                                )
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        BorderStroke(
                                            width = 2.dp,
                                            color = colorResource(
                                                id = if (txtFieldError.value.isEmpty())
                                                    R.color.holo_green_light
                                                else
                                                    R.color.holo_red_dark
                                            )
                                        ),
                                        shape = RoundedCornerShape(50)
                                    ),
                                colors = TextFieldDefaults.textFieldColors(
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.Edit,
                                        contentDescription = "",
                                        tint = colorResource(android.R.color.holo_green_light),
                                        modifier = Modifier
                                            .width(20.dp)
                                            .height(20.dp)
                                    )
                                },
                                placeholder = { Text(text = "Enter name") },
                                value = textName.value,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                onValueChange = {
                                    textName.value = it
                                }
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        BorderStroke(
                                            width = 2.dp,
                                            color = colorResource(
                                                id = if (txtFieldError.value.isEmpty())
                                                    R.color.holo_green_light
                                                else
                                                    R.color.holo_red_dark
                                            )
                                        ),
                                        shape = RoundedCornerShape(50)
                                    ),
                                colors = TextFieldDefaults.textFieldColors(
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.Settings,
                                        contentDescription = "",
                                        tint = colorResource(android.R.color.holo_green_light),
                                        modifier = Modifier
                                            .width(20.dp)
                                            .height(20.dp)
                                    )
                                },
                                placeholder = { Text(text = "Enter stock") },
                                value = textStock.value,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                onValueChange = {
                                    textStock.value = it
                                }
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        BorderStroke(
                                            width = 2.dp,
                                            color = colorResource(
                                                id = if (txtFieldError.value.isEmpty())
                                                    R.color.holo_green_light
                                                else
                                                    R.color.holo_red_dark
                                            )
                                        ),
                                        shape = RoundedCornerShape(50)
                                    ),
                                colors = TextFieldDefaults.textFieldColors(
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.Info,
                                        contentDescription = "",
                                        tint = colorResource(android.R.color.holo_green_light),
                                        modifier = Modifier
                                            .width(20.dp)
                                            .height(20.dp)
                                    )
                                },
                                placeholder = { Text(text = "Enter price") },
                                value = textPrice.value,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                onValueChange = {
                                    textPrice.value = it
                                }
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                                Button(
                                    onClick = {
                                        if (textName.value.isEmpty() || textStock.value.isEmpty() || textPrice.value.isEmpty()) {
                                            toastMessage(context, "There is an empty value")
                                            return@Button
                                        }
                                        setValue(textName.value)
                                        setShowDialog(false)
                                        flag = true
                                        parcaRepository.ParcaEkle(
                                            Parca(
                                                P_ID = -1,
                                                Kategori_ID = secilenKategori.value.K_ID,
                                                Adi = textName.value,
                                                StokAdedi = textStock.value.toInt(),
                                                Fiyati = textPrice.value.toLong()
                                            )
                                        )
                                        kategorikParcalar =
                                            parcaRepository.ParcalarByKategoriID(secilenKategori.value.K_ID)
                                    },
                                    shape = RoundedCornerShape(50.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp)
                                ) {
                                    Text(text = "Done")
                                }
                            }
                        }
                    }
                }
            }
        }

    }
}
