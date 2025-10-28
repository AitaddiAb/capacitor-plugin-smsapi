package dev.abdrahim.capacitor.plugin.sms;

import android.Manifest;
import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.getcapacitor.annotation.PermissionCallback;

@CapacitorPlugin(
    name = "SmsApi",
    permissions = {
        @Permission(alias = "sms_permissions", strings = { Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE })
    }
)
public class SmsApiPlugin extends Plugin {

    private SmsApi implementation;

    @Override
    public void load() {
        super.load();
        implementation = new SmsApi(getContext());
    }

    @PluginMethod
    public void checkPermissions(PluginCall call) {
        boolean granted = implementation.checkPermission();

        JSObject ret = new JSObject();
        ret.put("granted", granted);
        ret.put("message", granted ? "SMS permission is granted" : "SMS permission is denied");
        call.resolve(ret);
    }

    @PluginMethod
    public void requestPermissions(PluginCall call) {
        requestPermissionForAlias("sms_permissions", call, "smsPermissionCallback");
    }

    @PermissionCallback
    private void smsPermissionCallback(PluginCall call) {
        boolean granted = implementation.checkPermission();

        JSObject ret = new JSObject();
        ret.put("granted", granted);
        ret.put("message", granted ? "SMS permission is granted" : "SMS permission is denied");
        call.resolve(ret);
    }

    @PluginMethod
    public void getSIMs(PluginCall call) {
        try {
            java.util.List<java.util.Map<String, Object>> sims = implementation.getAvailableSims();

            // Convert Java Maps to JSObjects for proper JSON serialization
            JSArray simsArray = new JSArray();
            for (java.util.Map<String, Object> sim : sims) {
                JSObject simObject = new JSObject();
                simObject.put("slot", sim.get("slot"));
                simObject.put("carrierName", sim.get("carrierName"));
                simObject.put("displayName", sim.get("displayName"));
                simObject.put("isActive", sim.get("isActive"));
                simsArray.put(simObject);
            }

            // Wrap the array in a JSObject
            JSObject result = new JSObject();
            result.put("sims", simsArray);
            call.resolve(result);
        } catch (Exception e) {
            call.reject("Failed to get SIM information: " + e.getMessage());
        }
    }

    @PluginMethod
    public void sendSMS(PluginCall call) {
        if (!hasRequiredPermissions()) {
            call.reject("SMS permissions not granted.");
            return;
        }

        // Get parameters from the call
        String recipient = call.getString("recipient");
        String content = call.getString("content");
        int sim = call.getInt("sim", 0);

        if (recipient == null || content == null) {
            call.reject("Recipient and content are required");
            return;
        }

        // Send SMS using the implementation
        boolean success = implementation.sendSms(recipient, content, sim);

        JSObject ret = new JSObject();
        if (success) {
            ret.put("success", true);
            ret.put("message", "SMS sent successfully");
        } else {
            ret.put("success", false);
            ret.put("message", "Failed to send SMS");
        }
        call.resolve(ret);
    }
}
