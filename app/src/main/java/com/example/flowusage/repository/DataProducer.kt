package com.example.flowusage.repository

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.CopyOnWriteArrayList

// Mock the network or database service to send the data back to usecase.
class DataProducer {
    private val listeners: CopyOnWriteArrayList<Listener> = CopyOnWriteArrayList<Listener>()

    fun addListener(listener: Listener) {
        listeners.takeIf { !it.contains(listener) }?.run { this.add(listener) }
    }

    fun removeListener(listener: Listener) {
        listeners.takeIf { it.contains(listener) }?.run { this.remove(listener) }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun startDataUpdate() {
        GlobalScope.launch(Dispatchers.IO) {
            for (i in 1..10) {
                delay(2000)
                listeners.forEach {
                    it.updateDataFromIO(1000 + i)
                }
            }
        }
    }
}