package com.ant1gon.homeutility.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ant1gon.homeutility.data.repository.HouseholdRepository
import com.ant1gon.homeutility.domain.model.Household
import com.ant1gon.homeutility.domain.model.OwnershipType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HouseholdViewModel @Inject constructor(
    private val householdRepository: HouseholdRepository
) : ViewModel() {

    private val _households = MutableStateFlow<List<Household>>(emptyList())
    val households: StateFlow<List<Household>> = _households.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadHouseholds()
    }

    private fun loadHouseholds() {
        viewModelScope.launch {
            householdRepository.getAllHouseholds().collect { households ->
                _households.value = households
            }
        }
    }

    fun createHousehold(
        name: String,
        address: String = "",
        ownershipType: OwnershipType,
        rentCost: Double = 0.0,
        maintenanceFeeMontly: Double = 0.0
    ) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                householdRepository.createHousehold(
                    name = name,
                    address = address,
                    ownershipType = ownershipType,
                    rentCost = rentCost,
                    maintenanceFeeMontly = maintenanceFeeMontly
                )
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateHousehold(household: Household) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                householdRepository.updateHousehold(household)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteHousehold(id: Long) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                householdRepository.deleteHousehold(id)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
