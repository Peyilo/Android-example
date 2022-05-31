## **LitePal操作数据库**

#### LitePal项目GitHub地址

<https://github.com/guolindev/LitePal>

#### Android Studio配置

- **build.gradle配置**
    
    **在app/build.gradle文件的dependencies闭包中添加包依赖**

    ```java
    implementation 'org.litepal.guolindev:core:3.2.3'
    ```
- **在app/src/main目录下新建一个assets目录,在该目录下新建litepal.xml**
    ```xml
    <?xml version="1.0" encoding="UTF-8" ?>
    <litepal>
        <dbname value="BookStore"></dbname>
        <version value="1"></version>
        <list>

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
- **创建数据库**
  ```java
    Connector.getDatabase()
  ```
- **添加数据**
  ```java
    book.save()
  ```
- **更新数据**
  ```java
    book.updateAll("name = ? and author = ?", "Programming", "Anvei")
  ```
- **删除数据**
  ```java
    LitePal.deleteAll(Book.class, "price < ?", "15")
  ```
- **查询数据**
  
  ```java
    List<Book> books = LitePal.findAll(Book.class)

    List<Book> firstBook = LitePal.findFirst(Book.class)

    List<Book> lastBook = LitePal.findLast(Book.class)
  ```

  - **连缀查询**
  ```java
    select()

    where()

    order()

    limit()

    offset()

    eg:
    List<Book> books = LitePal.select("name", "author", "pages)
                                    .where("pages > ?", "400")
                                    .order("pages")
                                    .limit(10)
                                    .offset(10)
                                    .find(Book.class);

    
  ```
