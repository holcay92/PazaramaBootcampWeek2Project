package com.example.week2homework.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.* // ktlint-disable no-wildcard-imports
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.* // ktlint-disable no-wildcard-imports
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.week2homework.R
import com.example.week2homework.db.Product
import com.example.week2homework.db.ProductDatabase
import com.example.week2homework.viewModel.ProductViewModel
import com.example.week2homework.viewModel.ProductViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Column {
                TopAppBar()
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val context = LocalContext.current
    val dao = ProductDatabase.getInstance(context).productDao()
    val factory = ProductViewModelFactory(dao)
    val productViewModel = factory.create(ProductViewModel::class.java)
    val productList = ProductViewModel(dao).productList.observeAsState(listOf()).value
    var productName by remember { mutableStateOf("") }
    var productPrice by remember { mutableStateOf("") }
    var productDescription by remember { mutableStateOf("") }
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    Column(
        modifier = Modifier
            .fillMaxSize().background(Color.DarkGray)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Product Name", modifier = Modifier.weight(1f))
            TextField(value = productName, onValueChange = { productName = it })
        }
        Spacer(modifier = Modifier.fillMaxWidth().height(16.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Product Price", modifier = Modifier.weight(1f))
            TextField(value = productPrice, onValueChange = { productPrice = it })
        }
        Spacer(modifier = Modifier.fillMaxWidth().height(16.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Product Description", modifier = Modifier.weight(1f))
            TextField(value = productDescription, onValueChange = { productDescription = it })
        }
        Spacer(modifier = Modifier.fillMaxWidth().height(16.dp))
        Row {
            FloatingActionButton(
                onClick = {
                    if (productName.isNotEmpty() && productPrice.toInt() > 0) {
                        createProductObject(
                            productName,
                            productPrice.toInt(),
                            productDescription,
                            productViewModel,
                        )
                    } else {
                        Toast.makeText(context, "please fill all the fields", Toast.LENGTH_SHORT)
                            .show()
                    }

                    productName = ""
                    productPrice = ""
                    productDescription = ""
                },
                modifier = Modifier.weight(1f),
                containerColor = Color.Blue,
            ) {
                Text(
                    text = "Add Product",
                    modifier = Modifier.padding(20.dp),
                    color = Color.White,
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            FloatingActionButton(
                onClick = {
                    updateProduct(selectedProduct!!, productViewModel)

                    // Clear the selection
                    selectedProduct = null
                },
                modifier = Modifier.weight(1f),
                containerColor = Color.Blue,
            ) {
                Text(
                    text = "Update Product",
                    modifier = Modifier.padding(20.dp),
                    color = Color.White,
                )
            }
        }

        ProductList(productList, productViewModel) { product ->
            // Set the selected product when a product item is clicked
            selectedProduct = product

            // Display the selected product's details in the text fields
            productName = product.name
            productPrice = product.price.toString()
            productDescription = product.description ?: ""
        }
    }
}

@Composable
fun TopAppBar() {
    Text(text = "Pazarama week 2 Project", modifier = Modifier.padding(20.dp))
}

@Composable
fun ProductList(
    products: List<Product>,
    productVM: ProductViewModel,
    onItemClick: (Product) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(top = 10.dp, bottom = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(products.size) {
            ProductItem(product = products[it], productVM, onItemClick = { })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductItem(product: Product, productVM: ProductViewModel, onItemClick: () -> Unit) {
    Card(
        onClick = onItemClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(3.dp)
            .background(colorResource(id = R.color.white)),
        elevation = CardDefaults.cardElevation(10.dp),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, colorResource(id = R.color.black)),

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth().height(50.dp)
                .background(colorResource(id = R.color.white))
                .border(
                    1.dp,
                    colorResource(id = R.color.black),
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(modifier = Modifier.width(10.dp))
            product.name.let {
                Text(
                    text = it,
                    modifier = Modifier.weight(1f)
                        .align(Alignment.CenterVertically),
                    color = colorResource(id = R.color.black),
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                modifier = Modifier.weight(1f)
                    .wrapContentHeight()
                    .align(Alignment.CenterVertically),
            ) {
                Text(
                    text = product.price.toString(),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    color = colorResource(id = R.color.black),
                )
                Spacer(modifier = Modifier.height(10.dp))
                product.description?.let {
                    Text(
                        text = it,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        color = colorResource(id = R.color.black),
                    )
                }
            }
            IconButton(
                onClick = { deleteProductObject(product, productVM) },
                modifier = Modifier.size(44.dp).align(Alignment.CenterVertically).weight(0.5f),
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = null,
                    modifier = Modifier.size(34.dp),
                    tint = colorResource(R.color.black),
                )
            }
        }
    }
}

fun createProductObject(
    name: String,
    price: Int,
    description: String,
    viewModel: ProductViewModel,
) {
    val newProduct = Product(0, name, price, description)

    viewModel.insertButton(newProduct)
}

fun deleteProductObject(
    product: Product,
    viewModel: ProductViewModel,
) {
    viewModel.deleteButton(product)
}

fun updateProduct(product: Product, viewModel: ProductViewModel) {
    viewModel.updateButton(product)
}
