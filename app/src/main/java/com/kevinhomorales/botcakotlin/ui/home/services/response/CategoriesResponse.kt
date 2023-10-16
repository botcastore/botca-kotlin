package com.kevinhomorales.botcakotlin.ui.home.services.response

import java.io.Serializable

data class CategoriesResponse (
    val categorys: MutableList<Category>,
    val page: Int,
    val pages: Int
): Serializable

data class Category (
    val categoryID: String,
    val name: String,
    val description: String,
    val categorySlug: String,
    val imageURL: String,
    val isActive: Boolean,
    val createdBy: String? = null,
    val createdAt: String,
    val updatedAt: String
): Serializable