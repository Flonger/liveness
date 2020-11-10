import 'dart:async';

import 'package:flutter/services.dart';

class Liveness {
  static const MethodChannel _channel =
      const MethodChannel('liveness');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<String> callLiveness(Map<String, String> params) async {
    final String livenessid =  await _channel.invokeMethod('callLiveness',params);
    return livenessid;
  }

}
