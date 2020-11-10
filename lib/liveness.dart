import 'dart:async';

import 'package:flutter/services.dart';

class Liveness {
  static const MethodChannel _channel =
      const MethodChannel('liveness');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<void> callLiveness(Map<String, String> params) async {
    await _channel.invokeMethod('callLiveness',params);
  }

}
