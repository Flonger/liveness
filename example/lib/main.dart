import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:liveness/liveness.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  Future<void> callLiveness() async {
    await Liveness.callLiveness({'accessKey':'1111','secretKey':'2222'});
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: GestureDetector(
            onTap: (){callLiveness();},
            child: Container(
              height: 40,
              width: 100,
              color: Colors.blue,
              child: Center(
                child: Text(
                    'click to liveness'
                ),
              ),
            ),
          ),
        ),
      ),
    );
  }
}
