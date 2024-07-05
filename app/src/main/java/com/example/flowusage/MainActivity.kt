package com.example.flowusage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.flowusage.repository.DataProducer
import com.example.flowusage.repository.Listener
import com.example.flowusage.repository.NameInfoRepository
import com.example.flowusage.ui.theme.FlowUsageTheme
import com.example.flowusage.usecase.GetHobbyUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch


private lateinit var callbackForFlow: Callback<Int>

class MainActivity : ComponentActivity() {

    val nameInfoRepo by lazy { NameInfoRepository() }
    val getHobbyUseCase by lazy { GetHobbyUseCase(nameInfoRepo) }

    private val listener: Listener = object : Listener {
        override fun updateDataFromIO(newValue: Int) {
            // call the Callback object and update the value
            println("**** The updated value from IO is $newValue")
            if (::callbackForFlow.isInitialized) {
                callbackForFlow.updateInternalData(newValue)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val producer = DataProducer()
        producer.let {
            it.addListener(listener)
            it.startDataUpdate()
        }
        setContent {
            FlowUsageTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                    collectTheCallback()
                    randomHobby(getHobbyUseCase)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FlowUsageTheme {
        Greeting("Android")
    }
}

fun tryCallbackFlow(): Flow<Int> {
    return callbackFlow {
        callbackForFlow = object : Callback<Int> {
            override fun updateInternalData(newValue: Int) {
                // send the data to viewmodel
                trySend(newValue)
            }
        }
        awaitClose {  }
    }.flowOn(Dispatchers.IO)
}

fun collectTheCallback() {
    GlobalScope.launch(Dispatchers.IO) {
        tryCallbackFlow().collect() {
            println("******* the collect data is $it")
        }
    }
}

fun randomHobby(hobbyUseCase: GetHobbyUseCase) {
    GlobalScope.launch {
        for (i in 1..5) {
            hobbyUseCase.invoke().collect {
                //delay(100)
                println("FLOW**, Collected value is $it")
            }
        }
    }
}

interface Callback<T> {
    fun updateInternalData(newValue: T)
}

