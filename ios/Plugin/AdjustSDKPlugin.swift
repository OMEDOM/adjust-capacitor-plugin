import Foundation
import Capacitor
import Adjust

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(AdjustSDKPlugin)
public class AdjustSDKPlugin: CAPPlugin, AdjustDelegate {
    
    let adjustTag = "AdjustSDKPlugin"

    // @objc func echo(_ call: CAPPluginCall) {
    //     let value = call.getString("value") ?? ""
    //     print(value)
    //     call.resolve([
    //         "value": value
    //     ])
    // }
    
    private func getLogLevel(_ id: String?) -> ADJLogLevel {
        switch id?.lowercased() {
        case "none":
            return ADJLogLevelSuppress
        case "warning":
            return ADJLogLevelWarn
        case "info":
            return ADJLogLevelInfo
        case "verbose":
            return ADJLogLevelVerbose
        default:
            return ADJLogLevelError
        }
    }
    
//    public func adjustEventTrackingSucceeded(_ eventSuccessResponseData: ADJEventSuccess?) {
//        if let successData = eventSuccessResponseData {
//            // Handle success data
//            let successMessage = "Event tracking succeeded: \(successData.eventToken ?? "")"
//            print(successMessage)
//        }
//    }
//    
//    
//    public func adjustEventTrackingFailed(_ eventFailureResponseData: ADJEventFailure?) {
//        if let successData = eventFailureResponseData {
//            // Handle success data
//            let successMessage = "Event tracking fail: \(successData.eventToken ?? "")"
//            print(successMessage)
//        }
//    }
//    
//    public func adjustSessionTrackingSucceeded(_ sessionSuccessResponseData: ADJSessionSuccess?) {
//        if let successData = sessionSuccessResponseData {
//            // Handle success data
//            let successMessage = "Session tracking succeeded: \(successData.message ?? "")"
//            print(successMessage)
//        }
//    }
    
    
    @objc func initSDK(_ call: CAPPluginCall) {
        guard let appToken = call.getString("appToken") else {
            call.reject("Must provide an appToken to initialize plugin")
            return
        }
        
        
        let config = ADJConfig(appToken: appToken, environment: call.getString("environment") ?? ADJEnvironmentSandbox)
        config?.logLevel = getLogLevel(call.getString("logLevel"))
        config?.sendInBackground = true
        
        Adjust.resetSessionPartnerParameters()
        Adjust.resetSessionCallbackParameters()
        Adjust.appDidLaunch(config)

        print("\(self.adjustTag): adjust is running")
        call.resolve()
        
    }
    
    @objc func trackEvent(_ call: CAPPluginCall) {
        guard let eventToken = call.getString("eventToken") else {
            call.reject("Must provide an eventToken")
            return
        }

        let callbackParams = call.getObject("callbackParams") ?? [:]
        let event = ADJEvent(eventToken: eventToken)

        for (key, value) in callbackParams {
            if let stringValue = value as? String {
                event?.addCallbackParameter(key, value: stringValue)
            } else {
                print("\(self.adjustTag): Callback param value could not be cast to String, skipping")
            }
        }

        Adjust.trackEvent(event)
        call.resolve()
    }
    
    @objc func trackEventCallbackId(_ call: CAPPluginCall) {
        guard let eventToken = call.getString("eventToken") else {
            call.reject("Must provide an eventToken")
            return
        }

        guard let id = call.getString("id") else {
            call.reject("Must provide an event callback id")
            return
        }

        print("\(adjustTag): trackEventCallbackId \(eventToken) id \(id)")

        let event = ADJEvent(eventToken: eventToken)
        event?.setCallbackId(id)
        Adjust.trackEvent(event)

        call.resolve()
    }
    
    @objc func trackRevenueEvent(_ call: CAPPluginCall) {
        guard let eventToken = call.getString("eventToken") else {
            call.reject("Must provide an eventToken")
            return
        }

        guard let currency = call.getString("currency") else {
            call.reject("Must provide a currency")
            return
        }

        guard let amount = call.getDouble("amount") else {
            call.reject("Must provide an amount")
            return
        }

        let orderId = call.getString("orderId")

        let event = ADJEvent(eventToken: eventToken)
        event?.setRevenue(amount, currency: currency)

        if let orderId = orderId, !orderId.isEmpty {
            event?.setTransactionId(orderId)
        }

        Adjust.trackEvent(event)

        call.resolve()
    }
    
    @objc func getAdid(_ call: CAPPluginCall) {
        let attribution = Adjust.attribution
        let xadid = Adjust.adid()
        let adid = attribution()?.adid ?? "unknownAdjustId"

        print("\(adjustTag): adid: \(adid)")
        print("\(adjustTag): xadid: \(String(describing: xadid))")

        let result = ["id": adid]
        call.resolve(result)
    }

    @objc func getIDFA(_ call: CAPPluginCall) {
        let idfa = Adjust.idfa()
        
        let result = ["id": idfa]
        call.resolve(result as PluginCallResultData)
    }

}
