## SharedPreferences存储
    
- **获取SharedPreferences对象**
    
Method | Usage |
| --- | --- |
| *getSharedPreferences()* | Context类提供，接收两个参数，第一个参数用于指定文件的名称，如果指定的名称不存在就会创建一个，SharedPreferences文件都是存放在`/data/data/<package name>/shared_prefs/`目录下，第二个参数填MODE_PRIVATE，只有这一种模式可选，是默认操作模式，表示只有当前应用程序才可以对这个SharedPreferences文件进行读写 |
| *getPreferences()* | Activity类提供，只接收一个操作模式参数，这个方法会自动将**当前活动的类名**作为SharedPreferences的文件名 |
| *getDefaultPreferences()* | PreferenceManager类的静态方法，接收一个Context参数，并自动将**当前程序的包名**作为前缀命名文件 |
    
- **存储数据**

  - 调用SharedPreferences对象的**edit()方法**获取一个**SharedPreferences.Editor**对象

  - 向SharedPreferences.Editor对象使用**putInt()** 、**putString()** 等方法添加数据，put方法接收两个参数，第一个是键(String)，第二个是键值
  
  - 调用SharedPreferences.Editor对象的**apply()** 方法完成提交
  
  - 调用SharedPreferences的实例方法**clear()** 可以清空Editor
  
- **读取数据**

  获取SharedPreferences实例后，直接调用实例方法**getInt()** 、**getString()** 获取数据，get方法有两个参数，第一个参数是键(String)，第二个参数是默认值，若没有匹配到键就返回默认值
