package com.example.pbsm3.roomModel.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.pbsm3.roomModel.entities.CategoryBeforeRoom
import com.example.pbsm3.roomModel.repository.CategoryRoomRepository

class CategoryViewModel(private val repository: CategoryRoomRepository): ViewModel() {
    var categoryUiState by mutableStateOf(CategoryUiState())
        private set

    fun updateUiState(categoryDetails: CategoryBeforeRoom) {
        categoryUiState = categoryDetails.toCategoryUiState(isEntryValid = validateInput(categoryDetails))
    }

    suspend fun saveCategory(){
        if(validateInput()){
            repository.insert(categoryUiState.categoryDetails)
        }
    }

    private fun validateInput(
        uiState: CategoryBeforeRoom = categoryUiState.categoryDetails
    ): Boolean {
        return with(uiState){
            name.isNotBlank()
        }
    }
}

data class CategoryUiState(
    val categoryDetails: CategoryBeforeRoom = CategoryBeforeRoom(),
    val isEntryValid: Boolean = false
)

fun CategoryBeforeRoom.toCategoryUiState(isEntryValid: Boolean = false): CategoryUiState =
    CategoryUiState(
        categoryDetails = this,
        isEntryValid = isEntryValid
    )