## **LitePal操作数据库**

#### LitePal项目GitHub地址

<https://github.com/guolindev/LitePal>

#### Android Studio配置

- **build.gradle配置**
    
    **在app/build.gradle文件的dependencies闭包中添加包依赖**
  
    ```xml
    //官方推荐,但似乎因为某些原因会使AndroidMenifest.xml中添加的android:name='org.litepal.LitePalApplication'标红
    implementation 'org.litepal.guolindev:core:3.2.3'
    ```
    - 将对应的jar包放在lib文件夹下,右键jar包选择Add As Library,会自动配置builde.gradle文件
  
    - 直接从指定文件夹导入jar包,右键app文件夹选择Open Module Setting选项(快捷键F4)，选择dependencies,在这里添加依赖包,或者直接在builde.gradle添加以下类似内容
    
    ```gradle
    implementation files('D:\\Development Kit\\Android Studio SDK\\LitePal-master\\downloads\\litepal-2.0.0-src.jar')
    ```

- **在app/src/main目录下新建一个assets目录,在该目录下新建litepal.xml**
    ```xml
    <?xml version="1.0" encoding="UTF-8" ?>
    <litepal>
        <dbname value="BookStore"></dbname>
        <version value="1"></version>
        <list>
          <mapping class="com.examlple.app.Book"></mapping>
        </list>
    </litepal>
    ```
- **在AndroidManifest.xml添加android:name**
    ```xml
    <application
        android:name="org.litepal.LitePalApplication"
        ...
        </activity>
    </application>
    ```
    作用是设置全局Context，也可以通过`public static void initialize(Context context)#LitePal`设置(尽可能早地设置)

- **创建数据库**
  ```java
    //Connector类
    public static SQLiteDatabase getDatabase() {
		return getWritableDatabase();
	  }

    //LitePal类
    public static SQLiteDatabase getDatabase() {
        synchronized (LitePalSupport.class) {
            return Connector.getDatabase();
        }
    }
  ```

- **继承LitePalSupport类**
  
  获取CRUD操作方法

- **添加数据**
  ```java
    //@return If the model is saved successfully, return true. Any exception happens, return false.
    public boolean save()

    //LitePal类，将集合内的数据添加到数据库当中
    public static <T extends LitePalSupport> void saveAll(Collection<T> collection)

    //eg
    book.save()
  ```

- **更新数据**
  ```java
  /** @param id Which record to update.
    * @return The number of rows affected.
  */
    public int update(long id)
    public int updateAll(String... conditions)
  
  /**
    * When updating database with {@link LitePalSupport#update(long)}, you must
	* use this method to update a field into default value. Use setXxx with
	* default value of the model won't update anything. <br>
  */
    public void setToDefault(String fieldName)

    //eg
    book.updateAll("name = ? and author = ?", "Programming", "Anvei")
  ```

- **删除数据**
  ```java
    //LitePal类
    public static int deleteAll(Class<?> modelClass, String... conditions)
    public static int deleteAll(String tableName, String... conditions)

    //eg
    LitePal.deleteAll(Book.class, "price < ?", "15")
  ```

- **查询数据**
  
  ```java
    //@return An object with data of first row, or null.
    public static <T> T findFirst(Class<T> modelClass)

    //@return An object with data of last row, or null.
    public static <T> T findLast(Class<T> modelClass)

    //@return An object list with found data from database, or an empty list.
    public static <T> List<T> findAll(Class<T> modelClass, long... ids)

    //eg
    List<Book> books = LitePal.findAll(Book.class)
  ```

  - **连缀查询**
  ```java
    //限定要查询的列以后，对于find()所返回列表中所有的对象，没有查询的数据域表现为默认值
    public static FluentQuery select(String... columns)
    //修饰条件
    public static FluentQuery where(String... conditions)
    //设置排序方式
    public static FluentQuery order(String column)
    //限制查询返回的行数
    public static FluentQuery limit(int value)
    //设置偏移量
    public static FluentQuery offset(int value)
    
    //FluentQuery类提供过的方法
    public <T> List<T> find(Class<T> modelClass)

    //eg
    List<Book> books = LitePal.select("name", "author", "pages")
                                    .where("pages > ?", "400")
                                    .order("pages")
                                    .limit(10)
                                    .offset(10)
                                    .find(Book.class);

    
  ```
