package com.kevinhomorales.botcakotlin.NetworkManager.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ProductsResponse (
    val products: MutableList<Product>,
    val page: Int,
    val pages: Int
): Serializable

data class Product (
    val productID: String,
    val name: String,
    val price: String,
    val finalPrice: String? = null,
    val productSlug: String,
    val discount: Any? = null,
    val categoryID: String,
    val createdBy: Int? = null,
    val isActive: Boolean,
    val description: String,
    val rating: String,
    val countRating: Long,
    val totalGrades: Long,
    val createdAt: String,
    val updatedAt: String,
    val category: CategoryData,
    val sizes: MutableList<Size>,
    val colors: MutableList<Color>,
    val isFav: Boolean
): Serializable

data class CategoryData (
    val name: String,
    val description: String
): Serializable

data class Size (
    @SerializedName("size_product_ID")
    val sizeProductID: String,

    val size: String,
    val available: Boolean,
    val stock: Int,
    val productID: String,
    val createdAt: String,
    val updatedAt: String,

    @SerializedName("colors_sizes")
    val colorsSizes: List<ColorsSize>
): Serializable

data class ColorsSize (
    @SerializedName("color_size_product_ID")
    val colorSizeProductID: String,

    @SerializedName("color_product_ID")
    val colorProductID: String,

    @SerializedName("size_product_ID")
    val sizeProductID: String,

    val stock: Int,
    val createdAt: String,
    val updatedAt: String
): Serializable

data class Color (
    val images: List<String>,

    @SerializedName("color_product_ID")
    val colorProductID: String,

    val code: String,
    val label: String,
    val productID: String,
    val createdAt: String,
    val updatedAt: String
): Serializable