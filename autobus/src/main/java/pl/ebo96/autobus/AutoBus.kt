package pl.ebo96.autobus

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

object AutoBus {

    private val events = HashMap<String, MutableLiveData<Any>>()

    @Suppress("UNCHECKED_CAST")
    private fun register(key: String, single: Boolean) {
        val liveData = events[key]
        if (liveData == null) {
            events[key] = if (single) {
                SingleLiveEvent()
            } else {
                MutableLiveData()
            }
        } else {
            if (single && liveData !is SingleLiveEvent) {
                events[key] = SingleLiveEvent()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> observe(
        key: String,
        lifecycleOwner: LifecycleOwner,
        single: Boolean = false,
        result: (data: T?) -> Unit
    ) {
        register(key, single)
        events[key]?.observe(lifecycleOwner, Observer {
            result(it as? T)
        })
    }

    fun <T : Any> post(key: String, value: T?) {
        events[key]?.value = value
    }
}