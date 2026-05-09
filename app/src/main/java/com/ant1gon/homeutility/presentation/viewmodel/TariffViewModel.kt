package com.ant1gon.homeutility.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ant1gon.homeutility.data.repository.TariffRepository
import com.ant1gon.homeutility.domain.model.Tariff
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TariffViewModel @Inject constructor(
    private val tariffRepository: TariffRepository
) : ViewModel() {

    private val _tariffs = MutableStateFlow<List<Tariff>>(emptyList())
    val tariffs: StateFlow<List<Tariff>> = _tariffs.asStateFlow()

    private val _activeTariff = MutableStateFlow<Tariff?>(null)
    val activeTariff: StateFlow<Tariff?> = _activeTariff.asStateFlow()

    private val _tariffHistory = MutableStateFlow<List<Tariff>>(emptyList())
    val tariffHistory: StateFlow<List<Tariff>> = _tariffHistory.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadTariffsByHousehold(householdId: Long) {
        viewModelScope.launch {
            tariffRepository.getTariffsByHousehold(householdId).collect { tariffs ->
                _tariffs.value = tariffs
            }
        }
    }

    fun loadActiveTariff(
        householdId: Long,
        meterType: String,
        date: LocalDate = LocalDate.now()
    ) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val tariff = tariffRepository.getActiveTariff(householdId, meterType, date)
                _activeTariff.value = tariff
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadTariffHistory(householdId: Long, meterType: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val history = tariffRepository.getTariffHistory(householdId, meterType)
                _tariffHistory.value = history
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createTariff(
        householdId: Long,
        meterType: String,
        effectiveDate: LocalDate,
        basePrice: Double,
        tieredThreshold: Double = 0.0,
        tieredPrice: Double = 0.0
    ) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                tariffRepository.createTariff(
                    householdId = householdId,
                    meterType = meterType,
                    effectiveDate = effectiveDate,
                    basePrice = basePrice,
                    tieredThreshold = tieredThreshold,
                    tieredPrice = tieredPrice
                )
                _error.value = null
                loadTariffsByHousehold(householdId)
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateTariff(tariff: Tariff) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                tariffRepository.updateTariff(tariff)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteTariff(id: Long, householdId: Long) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                tariffRepository.deleteTariff(id)
                _error.value = null
                loadTariffsByHousehold(householdId)
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}
