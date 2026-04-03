package com.example.expensetracker.room.repository

import com.example.expensetracker.room.dao.CategoryDao
import com.example.expensetracker.room.model.Category
import kotlinx.coroutines.flow.Flow

class CategoryRepository(private val dao: CategoryDao) {
    val allCategories: Flow<List<Category>> = dao.getAllCategories()

    suspend fun insert(category: Category) = dao.insertCategory(category)

    suspend fun update(category: Category) = dao.updateCategory(category)

    suspend fun delete(category: Category) = dao.deleteCategory(category)
}
