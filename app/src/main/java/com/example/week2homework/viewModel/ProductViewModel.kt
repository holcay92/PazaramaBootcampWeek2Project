package com.example.week2homework.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.week2homework.db.Product
import com.example.week2homework.db.ProductDao
import kotlinx.coroutines.launch

class ProductViewModel(private val dao: ProductDao) : ViewModel() {

    var productList: LiveData<List<Product>> = dao.getAllItems()

    fun insertButton(product: Product) = viewModelScope.launch {
        dao.insertProduct(product)
    }

    fun deleteButton(product: Product) = viewModelScope.launch {
        dao.deleteProduct(product)
    }

    fun updateButton(product: Product) = viewModelScope.launch {
        dao.updateProduct(product)
    }

    fun getProduct(id: Int): LiveData<Product> {
        return dao.getProduct(id)
    }
}
