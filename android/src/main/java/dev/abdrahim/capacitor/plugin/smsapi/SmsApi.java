package dev.abdrahim.capacitor.plugin.sms;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.SmsManager;
import androidx.core.content.ContextCompat;

public class SmsApi {

    private Context context;

    public SmsApi(Context context) {
        this.context = context;
    }

    public java.util.List<java.util.Map<String, Object>> getAvailableSims() {
        java.util.List<java.util.Map<String, Object>> sims = new java.util.ArrayList<>();

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                android.telephony.SubscriptionManager subManager = android.telephony.SubscriptionManager.from(context);
                if (subManager != null && subManager.getActiveSubscriptionInfoList() != null) {
                    java.util.List<android.telephony.SubscriptionInfo> subscriptions = subManager.getActiveSubscriptionInfoList();

                    for (int i = 0; i < subscriptions.size(); i++) {
                        android.telephony.SubscriptionInfo info = subscriptions.get(i);
                        java.util.Map<String, Object> sim = new java.util.HashMap<>();

                        sim.put("slot", i);
                        sim.put("carrierName", info.getCarrierName() != null ? info.getCarrierName().toString() : "Unknown");
                        sim.put("displayName", info.getDisplayName() != null ? info.getDisplayName().toString() : "SIM " + (i + 1));
                        sim.put("isActive", true);

                        sims.add(sim);
                        android.util.Log.d("SmsApi", "SIM " + i + ": " + sim.get("displayName") + " (" + sim.get("carrierName") + ")");
                    }
                }
            } else {
                // For older Android versions, just return default SIM
                java.util.Map<String, Object> defaultSim = new java.util.HashMap<>();
                defaultSim.put("slot", 0);
                defaultSim.put("carrierName", "Default");
                defaultSim.put("displayName", "Default SIM");
                defaultSim.put("isActive", true);
                sims.add(defaultSim);
            }
        } catch (Exception e) {
            android.util.Log.e("SmsApi", "Failed to get SIM info: " + e.getMessage());
            e.printStackTrace();
        }

        android.util.Log.d("SmsApi", "Found " + sims.size() + " SIM(s)");
        return sims;
    }

    public boolean checkPermission() {
        int smsPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS);
        int phonePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);

        boolean smsGranted = smsPermission == PackageManager.PERMISSION_GRANTED;
        boolean phoneGranted = phonePermission == PackageManager.PERMISSION_GRANTED;

        return smsGranted && phoneGranted;
    }

    public boolean sendSms(String phoneNumber, String message, int sim) {
        try {
            // Validate inputs
            if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
                android.util.Log.e("SmsApi", "Phone number is null or empty");
                return false;
            }
            if (message == null || message.trim().isEmpty()) {
                android.util.Log.e("SmsApi", "Message is null or empty");
                return false;
            }

            // Check permission first
            if (!checkPermission()) {
                android.util.Log.e("SmsApi", "SMS permission not granted");
                return false;
            }

            // Get SMS manager based on Android version and SIM slot
            SmsManager manager;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
                manager = SmsManager.getDefault();
                android.util.Log.d("SmsApi", "Using default SMS manager (Android < 5.1)");
            } else {
                try {
                    // Try to get subscription manager to check available SIM slots
                    android.telephony.SubscriptionManager subManager = android.telephony.SubscriptionManager.from(context);
                    if (subManager != null && subManager.getActiveSubscriptionInfoList() != null) {
                        java.util.List<android.telephony.SubscriptionInfo> subscriptions = subManager.getActiveSubscriptionInfoList();
                        android.util.Log.d("SmsApi", "Available SIM slots: " + subscriptions.size());

                        if (subscriptions.size() > sim && sim >= 0) {
                            int subscriptionId = subscriptions.get(sim).getSubscriptionId();
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                                manager = SmsManager.getSmsManagerForSubscriptionId(subscriptionId);
                            } else {
                                manager = context.getSystemService(SmsManager.class).createForSubscriptionId(subscriptionId);
                            }
                            android.util.Log.d("SmsApi", "Using SIM slot: " + sim + " (subscription ID: " + subscriptionId + ")");
                        } else {
                            android.util.Log.w("SmsApi", "SIM slot " + sim + " not available, using default");
                            manager = SmsManager.getDefault();
                        }
                    } else {
                        android.util.Log.w("SmsApi", "No subscription manager available, using default");
                        manager = SmsManager.getDefault();
                    }
                } catch (Exception e) {
                    android.util.Log.w("SmsApi", "Failed to get SIM slot " + sim + ", using default: " + e.getMessage());
                    manager = SmsManager.getDefault();
                }
            }

            // Create explicit intents for Android 14+ compatibility
            Intent sentIntent = new Intent("SMS_SENT");
            sentIntent.setPackage(context.getPackageName());

            int flags = PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE;
            PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, sentIntent, flags);

            android.util.Log.d("SmsApi", "Sending SMS to: " + phoneNumber + ", message length: " + message.length());

            // Send SMS
            manager.sendTextMessage(phoneNumber, null, message, sentPI, null);

            android.util.Log.d("SmsApi", "SMS sent successfully");
            return true;
        } catch (Exception e) {
            android.util.Log.e("SmsApi", "Failed to send SMS: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
