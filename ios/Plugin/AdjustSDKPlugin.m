#import <Foundation/Foundation.h>
#import <Capacitor/Capacitor.h>

// Define the plugin using the CAP_PLUGIN Macro, and
// each method the plugin supports using the CAP_PLUGIN_METHOD macro.
CAP_PLUGIN(AdjustSDKPlugin, "AdjustSDK",
           CAP_PLUGIN_METHOD(initSDK, CAPPluginReturnNone);
           CAP_PLUGIN_METHOD(trackEvent, CAPPluginReturnNone);
           CAP_PLUGIN_METHOD(trackEventCallbackId, CAPPluginReturnNone);
           CAP_PLUGIN_METHOD(trackRevenueEvent, CAPPluginReturnNone);
           CAP_PLUGIN_METHOD(getAdid, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(getIDFA, CAPPluginReturnPromise);
)
