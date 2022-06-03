# 启动活动

### 单向传递启动

1. **显式Intent启动**

```java
Intent intent = new Intent(MainActivity.this, SecondActivity.class);
startActivity(intent)

/** #Class Context
 * public abstract void startActivity(@RequiresPermission Intent intent);
 * 
 * #Class Intent
 * Intent(Context packageContext, Class<?> class)
 */ 
```

2. **隐式Intent启动**

- AndroidManifest.xml

```xml
<activity android:name=".SecondActivity"
    android:exported="false">
    <intent-filter>
        <action android:name="com.example.app.MY_ACTION"/>
        <action android:name="android.intent.category.DEFUALT">
    </intent-filter>
</activity>
```

- 启动新活动

```java
//必须同时匹配上action, category才会启动
Intent intent = new Intent("com.example.app.MY_ACTION");
startActivity(intent);

/** #Class Intent
 * public static final String CATEGORY_DEFAULT = "android.intent.category.DEFAULT";
 * 这是一种默认的category，调用startActivity时会自动将其添加到Intent中
 * 
 * public Intent(String action)
 * @param action The Intent action, such as ACTION_VIEW.
 */ 
```

3. **更多隐式Intent用法**

- **打开网页**

```java
Intent intent = new Intent(Intent.ACTION_VIEW);
intent.setData(Uri.parse("http://www.baidu.com"));
//这会调用系统浏览器打开网址
startActivity(intent);

/** #Class Intent
 * public static final String ACTION_VIEW = "android.intent.action.VIEW";
 * 
 * public @NonNull Intent setData(@Nullable Uri data)
 */ 
```
- **打开拨号界面**
  
```java
Intent intent = new Intent(Intent.ACTION_VIEW);
intent.setData(Uri.parse("tel:10086"));
//tel协议，这会打开拨号界面，并填充号码
startActivity(intent);
```

- **data标签**

只有<data>标签中指定的内容和Intent携带的Data完全一致时，当前活动才可以响应该Intent

|Attribute|Usage|
|-|-|
|android:schema|用于指定数据的协议部分，如上例中的http部分|
|android:host|用于指定数据的主机名部分，如上例中的www.baidu.com部分|
|android:port|用于指定数据的端口部分，一般紧随主机名后|
|android:path|用于指定主机口和端口之后的部分，如一段网址中跟在域名之后的部分|
|android:mimeType|用于指定可以处理的数据类型，允许使用通配符的方式进行指定|

4. **向下一个活动传递数据**

- **MainActivity传递数据**

```java
String data = "Hello SecondActivity";
Intent intent = new Intent(MainActivity.this, SecondActivity.class);
intent.puExtra("extra_data", data);
startActivity(intent);
```

- **SecondActivity获取数据**

```java
Intent intent = getIntent();
String data = intent.getStringExtra("extra_data");

/** # Class Activity
 * public Intent getIntent()
 * @return Return the intent that started this activity.
 */
```

### 双向传递启动

#### **旧式API用法**

- **MainActivity启动SecondActivity**

```java
Intent intent = new Intent(MainActivity.this, SecondActivity.class);
startActivityForResult(intent, 1);
```

- **SecondActivity返回数据**

```java
Intent intent = new Intent();
intent.putExtra("data_return", "Hello MainActivity");
setResult(RESULT_OK, intent);
```

- **MainActivity处理返回结果**

```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data){
    switch(requestCode){
        case 1:
            if(resultCode == RESULT_OK){
                String returnedData = data.getStringExtra("data_return");
                ...
            }
            ...
        ...
    }
}
```

- **方法清单**

```java
/** # Class Activity
 * @param requestCode The integer request code originally supplied to
 *                    startActivityForResult(), allowing you to identify who this
 *                    result came from.
 * @param resultCode The integer result code returned by the child activity
 *                   through its setResult().
 * @param data An Intent, which can return result data to the caller
 *               (various data can be attached to Intent "extras").
 */
protected void onActivityResult(int requestCode, int resultCode, Intent data)
```

```java
/**
 * Call this to set the result that your activity will return to its caller.
 */ 
public final void setResult(int resultCode, Intent data)
```

```java
/**
 * Same as calling {@link #startActivityForResult(Intent, int, Bundle)}
 * with no options.
 *
 * @param intent The intent to start.
 * @param requestCode If >= 0, this code will be returned in
 *                    onActivityResult() when the activity exits.
 */
public void startActivityForResult(Intent intent, int requestCode)
```

#### 新式API用法

- **官方示例**

```java
ActivityResultLauncher<String> mGetContent = registerForActivityResult(new GetContent(),
    new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri uri) {
            // Handle the returned Uri
        }
});

@Override
public void onCreate(@Nullable savedInstanceState: Bundle) {
    // ...

    Button selectButton = findViewById(R.id.select_button);

    selectButton.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
            // Pass in the mime type you'd like to allow the user to select
            // as the input
            mGetContent.launch("image/*");
        }
    });
}
```

- **常用用法**

```java

registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
        new ActivityResultCallBack<ActivityResult>(){
            @Override
            public void onActivityResult(ActivityResult result){
                Intent data = result.getData();
                int resultCode = result.getResultCode();
            }
        }).launch(new Intent(context, MyActivity.class));

/** launch(), 输入Intent, ActivityCallBack处理返回结果
 * ActivityResultContracts.StartActivityForResult 是官方提供用来处理回调数据的
 * 跳转到MyActivity后，调用setResult()传递数据，这部分和以前一样
 */
```

- **方法清单**

```java
/** #Class ComponentActivity实现了接口的抽象方法 */
public final ActivityResultLauncher<I> <I, O> registerForActivityResult(
    ActivityResultContract<I, O> contract,
    ActivityResultCallback<O> callback
)
```
