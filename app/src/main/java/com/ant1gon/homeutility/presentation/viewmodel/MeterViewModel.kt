package com.ant1gon.homeutility.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ant1gon.homeutility.data.repository.MeterRepository
import com.ant1gon.homeutility.domain.model.Meter
import com.ant1gon.homeutility.domain.model.MeterType
import com.ant1gon.homeutility.domain.model.WaterType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MeterViewModel @Inject constructor(
    private val meterRepository: MeterRepository
) : ViewModel() {

    private val _meters = MutableStateFlow<List<Meter>>(emptyList())
    val meters: StateFlow<List<Meter>> = _meters.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadMetersByHousehold(householdId: Long) {
        viewModelScope.launch {
            meterRepository.getMetersByHousehold(householdId).collect { meters ->
                _meters.value = meters
            }
        }
    }

    fun createMeter(
        householdId: Long,
        customName: String,
        meterType: MeterType,
        location: String = "",
        electricityZones: Int = 1,
        waterType: WaterType? = null,
        tariffId: Long? = null
    ) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                meterRepository.createMeter(
                    householdId = householdId,
                    customName = customName,
                    meterType = meterType,
                    location = location,
                    electricityZones = electricityZones,
                    waterType = waterType,
                    tariffId = tariffId
                )
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateMeter(meter: Meter) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                meterRepository.updateMeter(meter)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteMeter(id: Long) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                meterRepository.deleteMeter(id)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
