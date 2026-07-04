package com.example.expensetracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.room.model.Category
import com.example.expensetracker.room.repository.CategoryRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val repository: CategoryRepository
) : ViewModel() {

    val getAllCategories: StateFlow<List<Category>> = repository.allCategories
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun addCategory(title: String, color: Long, icon: String = "receipt") {
        viewModelScope.launch {
            repository.insert(Category(title = title, color = color, icon = icon))
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            repository.delete(category)
        }
    }

    fun updateCategory(category: Category) {
        viewModelScope.launch {
            repository.update(category)
        }
    }
}

class CategoryViewModelFactory(private val repository: CategoryRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return CategoryViewModel(repository) as T
    }
}
