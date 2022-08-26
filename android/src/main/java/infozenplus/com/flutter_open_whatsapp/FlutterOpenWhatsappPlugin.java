package infozenplus.com.flutter_open_whatsapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.app.Activity;

import androidx.annotation.NonNull;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.BinaryMessenger;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** FlutterOpenWhatsappPlugin */
public class FlutterOpenWhatsappPlugin implements FlutterPlugin, MethodCallHandler {

  Activity context;
  MethodChannel methodChannel;

  private static final String DEBUG_NAME = "FlutterPluginTest";
  private static final String MESSAGE_CHANNEL = "flutter_plugin_test/message";

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    Log.d(DEBUG_NAME, " onAttachedToEngine");
    setupChannels(flutterPluginBinding.getFlutterEngine().getDartExecutor(), flutterPluginBinding.getApplicationContext());
  }

  public static void registerWith(Registrar registrar) {
    Log.d(DEBUG_NAME, " registerWith");
    FlutterOpenWhatsappPlugin plugin = new FlutterOpenWhatsappPlugin();
    plugin.setupChannels(registrar.messenger(), registrar.context());
  }
  private void setupChannels(BinaryMessenger messenger, Context context) {
    Log.d(DEBUG_NAME, "setupChannels");
    this.context = context;
    this.activity = null;
    methodChannel = new MethodChannel(messenger, MESSAGE_CHANNEL);
    methodChannel.setMethodCallHandler(this);
  }
  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    Log.d(DEBUG_NAME, "onMethodCall" + " - " + call.method.toString());
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    } 
    else if(call.method.equalsIgnoreCase("sendSingleMessage")) {

      PackageManager packageManager = context.getPackageManager();
      Intent i = new Intent(Intent.ACTION_VIEW);
      try {
        String mobileNo = call.argument("mobileNo");
        String message = call.argument("message");
        //https://wa.me/919167370647?text=Yes%20We'll%20do%20this%20in%20frag4%20inOCW
        String url = "https://wa.me/" + mobileNo.trim() + "?text=" + message.trim();
        i.setPackage("com.whatsapp");
        i.setData(Uri.parse(url));
        if (i.resolveActivity(packageManager) != null) {
          context.startActivity(i);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    } 
    else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
  }

  /** Plugin registration. */

  // public static void registerWith(Registrar registrar) {
  //   Log.d(DEBUG_NAME, " registerWith");
  //   final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutter_open_whatsapp");
  //   channel.setMethodCallHandler(new FlutterOpenWhatsappPlugin(registrar.activity(), channel));
  // }

  // public FlutterOpenWhatsappPlugin(Activity activity, MethodChannel methodChannel) {
  //   this.context = activity;
  //   this.methodChannel = methodChannel;
  //   this.methodChannel.setMethodCallHandler(this);
  // }

  // @Override
  // public void onMethodCall(MethodCall call, Result result) {
  //   if (call.method.equals("getPlatformVersion")) {
  //     result.success("Android " + android.os.Build.VERSION.RELEASE);
  //   } 
  //   else if(call.method.equalsIgnoreCase("sendSingleMessage")) {

  //     PackageManager packageManager = context.getPackageManager();
  //     Intent i = new Intent(Intent.ACTION_VIEW);
  //     try {
  //       String mobileNo = call.argument("mobileNo");
  //       String message = call.argument("message");
  //       //https://wa.me/919167370647?text=Yes%20We'll%20do%20this%20in%20frag4%20inOCW
  //       String url = "https://wa.me/" + mobileNo.trim() + "?text=" + message.trim();
  //       i.setPackage("com.whatsapp");
  //       i.setData(Uri.parse(url));
  //       if (i.resolveActivity(packageManager) != null) {
  //         context.startActivity(i);
  //       }
  //     } catch (Exception e) {
  //       e.printStackTrace();
  //     }
  //   } 
  //   else {
  //     result.notImplemented();
  //   }
  // }
  
}
