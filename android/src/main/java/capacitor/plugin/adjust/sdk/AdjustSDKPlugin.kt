package capacitor.plugin.adjust.sdk;

import android.Manifest
import android.content.Context
import android.util.Log
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustConfig
import com.adjust.sdk.LogLevel
import com.adjust.sdk.AdjustEvent

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginConfig
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import java.util.Locale

@CapacitorPlugin(
        name = "AdjustSDK",
        permissions = [Permission(
        strings = arrayOf(
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                "com.google.android.gms.permission.AD_ID"
        )
)]
)
class AdjustSDKPlugin: Plugin() {

//    private val implementation = AdjustSDK()
    private val adjustTag = "AdjustSDKPlugin"

    override fun load() {
        super.load()

        // init Adjust
        Log.w(adjustTag, "init Adjust SDK");
//        config.getObject("adjust-capacitor-plugin")?.let {
//            val appToken = it.getString("appToken")
//            val environment = it.getString("environment") ?: AdjustConfig.ENVIRONMENT_SANDBOX
//            val logLevel = getLogLevel(it.getString("logLevel"))
//            val autoInit = it.getBoolean("autoInit") ?: false
//
//            // log all val
//            Log.i(adjustTag, "appToken: $appToken")
//            Log.i(adjustTag, "environment: $environment")
//            Log.i(adjustTag, "logLevel: $logLevel")
//            Log.i(adjustTag, "autoInit: $autoInit")
//        }
//
//        Log.i(adjustTag, "init Adjust SDK done")

    }

    private fun getLogLevel(id: String?): LogLevel {
        return when (id?.lowercase(Locale.ROOT)) {
            "none" -> LogLevel.SUPRESS
            "warning" -> LogLevel.WARN
            "info" -> LogLevel.INFO
            "verbose" -> LogLevel.VERBOSE
            else -> LogLevel.ERROR
        }
    }

    // @PluginMethod
    // fun ping(call: PluginCall) {
    //     val value = call.getString("value")

    //     val ret = JSObject().apply {
    //         put("value", value ?: "null")
    //     }
    //     call.resolve(ret)
    // }

    @PluginMethod
    fun  initSDK(call: PluginCall) {
        val appToken = call.getString("appToken")
        // check appToken is not null
        if (appToken == null) {
            call.reject("Must provide an appToken to initialize plugin")
            return
        }

        val logLevel = call.getString("logLevel")

//        activity will be crashed if not in main thread
        val config = AdjustConfig(
            activity,
            appToken,
            call.getString("environment") ?: AdjustConfig.ENVIRONMENT_SANDBOX
        )

        config.setLogLevel(getLogLevel(logLevel))
        config.isSendInBackground = true

        config.setOnAttributionChangedListener { attribution ->
            Log.d(adjustTag, "Attribution callback called!")
            Log.d(adjustTag, "Attribution: $attribution")
        }

        // Set event success tracking delegate.
        config.setOnEventTrackingSucceededListener { eventSuccessResponseData ->
            Log.d(adjustTag, "Event success callback called!")
            Log.d(adjustTag, "Event success data: $eventSuccessResponseData")
        }

        // Set event failure tracking delegate.
        config.setOnEventTrackingFailedListener { eventFailureResponseData ->
            Log.d(adjustTag, "Event failure callback called!")
            Log.d(adjustTag, "Event failure data: $eventFailureResponseData")
        }

//        config.delayStart = 3.0

        // reset session parameters
        Adjust.resetSessionPartnerParameters()
        Adjust.resetSessionCallbackParameters()
        Adjust.onCreate(config)
        Log.d(adjustTag, "adjust is running")

//        val ret = JSObject().apply {
//            put("value", appToken.let { implementation.initSDK(appToken) })
//        }

        call.resolve()
    }

    @PluginMethod
    fun trackEvent(call: PluginCall) {
        val eventToken = call.getString("eventToken") ?: run {
            call.reject("Must provide an eventToken")
            return
        }

        val callbackParams = call.getObject("callbackParams") ?: JSObject()

        val event = AdjustEvent(eventToken)
        for (key in callbackParams.keys()) {
            val value = callbackParams.getString(key)
            if (value != null) {
                event.addCallbackParameter(key, value)
            } else {
                Log.w(adjustTag, "Callback param value could not be cast to String, skipping")
            }
        }

        Adjust.trackEvent(event)
        call.resolve()
    }

    @PluginMethod
    fun trackEventCallbackId(call: PluginCall) {
        val eventToken = call.getString("eventToken") ?: run {
            call.reject("Must provide an eventToken")
            return
        }

        val id = call.getString("id") ?: run {
            call.reject("Must provide an event callback id")
            return
        }

        Log.d(adjustTag, "trackEventCallbackId $eventToken id $id")

        val event = AdjustEvent(eventToken)
        event.callbackId = id
        Adjust.trackEvent(event)
        call.resolve()
    }

    @PluginMethod
    fun trackRevenueEvent(call: PluginCall) {
        val eventToken = call.getString("eventToken") ?: run {
            call.reject("Must provide an eventToken")
            return
        }

        val currency = call.getString("currency") ?: run {
            call.reject("Must provide a currency")
            return
        }

        val amount = call.getDouble("amount") ?: run {
            call.reject("Must provide an amount")
            return
        }

        val orderId = call.getString("orderId")

        val event = AdjustEvent(eventToken)
        event.setRevenue(amount, currency)

        if (!orderId.isNullOrBlank()) {
            event.orderId = orderId
        }
        Adjust.trackEvent(event)
        call.resolve()
    }

    @PluginMethod
    fun addSessionCallbackParameter(call: PluginCall) {
        val key = call.getString("key") ?: run {
            call.reject("Session callback parameter must contain key")
            return
        }

        val value = call.getString("value") ?: run {
            call.reject("Session callback parameter must contain value")
            return
        }

        Adjust.addSessionCallbackParameter(key, value)
        call.resolve()
    }

    @PluginMethod
    fun addSessionPartnerParameter(call: PluginCall) {
        val key = call.getString("key") ?: run {
            call.reject("Session Partner parameter must contain key")
            return
        }

        val value = call.getString("value") ?: run {
            call.reject("Session Partner parameter must contain value")
            return
        }

        Adjust.addSessionCallbackParameter(key, value)
        call.resolve()
    }

    @PluginMethod
    fun getAdid(call: PluginCall) {
        val attribution = Adjust.getAttribution()
        val xadid = Adjust.getAdid()
        val adid = attribution?.adid ?: "unknownAdjustId"
        Log.i(adjustTag, "adid: $adid")
        Log.i(adjustTag, "xadid: $xadid")

        call.resolve(JSObject().put("id", adid))
    }

    @PluginMethod
    fun getIDFA(call: PluginCall) {
        call.resolve(JSObject().put("id", "unknownIDFAID"))
    }

    @PluginMethod
    fun onResume() {
        Adjust.onResume()
    }

    @PluginMethod
    fun onPause() {
        Adjust.onPause()
    }

    @PluginMethod
    fun showTrackingDialog(call: PluginCall) {
        // Noop, showTrackingDialog is only available on iOS implementation
//        call.resolve()
    }

    @PluginMethod
    fun getTrackingStatus(call: PluginCall) {
        // Noop, getTrackingStatus is only available on iOS implementation
//        call.resolve()
    }
}
