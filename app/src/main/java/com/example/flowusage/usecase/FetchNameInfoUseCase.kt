package com.example.flowusage.usecase

import com.example.flowusage.repository.NameInfoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FetchNameInfoUseCase(private val nameInfoRepository: NameInfoRepository) {
    operator fun invoke() {
        val stateFlow: StateFlow<String> = MutableStateFlow("")
//        val result = nameInfoRepository.changeName()
    }
}