import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileNotFoundException
import java.io.InterruptedIOException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

private val client = OkHttpClient().newBuilder()
    .callTimeout(5, TimeUnit.SECONDS)
    .build()

fun main(args: Array<String>) {

    // Состовляем URL
    val url = try {
        val keyApi = System.getenv("WEATHER_API_KEY")
        val (latitude, longitude) = args[1].split(',')
        createURL(latitude, longitude, keyApi)
    } catch (e: ArrayIndexOutOfBoundsException) {
        println("Параметры отсутствуют.")
        return
    } catch (e: IndexOutOfBoundsException) {
        println("Параметры указаны неверно.")
        return
    } catch (e: NullPointerException) {
        println("Отсутствует API key.")
        return
    }

    val temp = kelvinToCelsius(getTemperature(url))
    if (temp == -273) {
        println("При получении температуры произошла ошибка. Попробуйте ещё раз.")
        return
    }

    // Запись в файл, если указан параметр
    if ("-f" in args) {
        try {
            writeFile(args[3], temp)
            println("Температура успешно записана в файл.")
        } catch (e: FileNotFoundException) {
            println("Неверно указан путь к файлу.")
        } catch (e: ArrayIndexOutOfBoundsException) {
            println("Пропущен путь к файлу.")
        }
    } else {
        println(temp)
    }
}

fun writeFile(str: String, temp: Int) {
    File(str).writeText("$temp")
}

fun getTemperature(url: String): Double {
    val request = Request.Builder()
        .url(url)
        .build()
    val response = try {
        client.newCall(request).execute()
    } catch (e: InterruptedIOException) {
        println("Время подключения истекло.")
        return 0.0
    } catch (e: UnknownHostException) {
        println("Нет подключения к серверу.")
        return 0.0
    }

    return when (response.code) {
        in 200..299 -> {
            if (response.body != null) {
                parseJSON(response.body!!.string())
            } else 0.0
        }
        in 400..499 -> {
            println("Ошибка на стороне клиента. Номер ошибки: ${response.code}")
            0.0
        }
        in 500..599 -> {
            println("Ошибка на стороне сервера. Номер ошибки: ${response.code}")
            0.0
        }
        else -> {
            println("Непредвиденная ошибка. Номер ошибки: ${response.code}")
            0.0
        }
    }
}

fun parseJSON(jsonString: String): Double {
    val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()
    val jsonAdapter: JsonAdapter<Temperature> = moshi.adapter(
        Temperature::class.java
    )
    val temperature = jsonAdapter.fromJson(jsonString)
    return temperature?.main?.temp ?: 0.0
}

fun createURL(lat: String, lon: String, key: String): String {
    return "https://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$lon&appid=$key"
}

fun kelvinToCelsius(temp: Double): Int = temp.toInt() - 273