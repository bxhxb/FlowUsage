package com.example.flowusage.usecase

import com.example.flowusage.repository.NameInfoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GetHobbyUseCase(val nameInfoRepository: NameInfoRepository) {
    operator fun invoke(): Flow<String> {
        return flow {
            val hobby = nameInfoRepository.getNewHobby()
            println("FLOW**, emit value")
            emit(hobby)
        }.catch {ex ->
            println(ex)
        }.buffer(1).flowOn(Dispatchers.IO)
    }
}