## 广播

#### 动态注册

必须打开应用才可以接收广播

- 继承**BroadcastReceiver**类并重写`public abstract void onReceive(Context context, Intent intent)`
```java
class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
        ...
    }
}
```

- 注册广播
```java
//接收器+过滤器
MyBroadReceiver receiver = new MyBroadcastReceiver();
IntentFilter filter = new IntentFilter();
filter.addAction("com.example.my_action");
registerReceiver(receiver, filter);
```

- 注销广播
```java
unregisterReceiver(receiver)
```

#### 静态注册

可以实现开机自启

- 继承**BroadcastReceiver**类并重写`public abstract void onReceive(Context context, Intent intent)`
```java
class MyBroadcastReceiver extends BroadcastReceiver{
    @Override
    ...
}
```

- **AndroidManifest.xml**注册广播并实现过滤
```xml
<application>
    <receiver
        android:name=".MyBroadcastReceiver"
        android:exported="true"
        android:enabled="true">
        <intent-filter>
            <action android:name="com.example.my_action"/>
        </intent-filter>
    </receiver>
</application>
```

#### 发送广播

- 标准广播Nomal Broadcasts

```java
Intent intent = new Intent("com.example.my_action");
sendBroadcast(intent);
```

- 有序广播Ordered Broadcasts

    - 发送
    ```java
    Intent intent = new Intent("com.example.my_action");
    sendOrderedBroadcast(intent);
    ```

    - 优先级设置
    ```xml
    <intent-filter android:priority="100">
    ...
    </intent-filter>
    ```

    - 截断
    `abortBroadcast()`

#### 本地广播

- 注册、注销、发送

`LocalBroadcastManager.getInstance(Context context)`

```java
LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
//发送本地广播
manager.sendBroadcast(intent);
//注册本地广播
manager.registerReceiver(receiver, filter);
//注销本地广播
manager.ubregisterReceiver(receiver);
```
