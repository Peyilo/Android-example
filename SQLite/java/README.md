## **SQLite数据库存储**

- **继承SQLiteOpenHelper抽象类**
    
    - **构造方法**
        
        `SQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)` 
        
        数据库名name，第三个参数允许在查询数据的时候返回一个自定义Cursor，一般都是填null,第四个参数是当前数据库的版本号
    
    - **抽象方法**
        
        - `void onCreate(SQLiteDatabase db)` ：在这里执行创建数据库的逻辑
        
        - `void onUpgrade(SQLiteDatabase db, int oldVrsion, int newVersion)` ： 当版本号比旧版本号大，就会执行OnUpgrade( )，注意的是，需要先将旧的表删掉再创建更新，否则创建时发现表已经存在就会报错
    
    - **实例方法**
    
        - `getReadableDatabase( )` ：创建或者打开一个数据库(不存在则创建，存在则打开)，返回一个SQLiteDatabase实例
  
        - `getWritableDatavbase( )` ： 和getReadableDatabase()相似
       
        - `execSQL(String: SQLStatement)` : SQLiteDatabase的实例方法，执行指定的SQL语句
      
    - **添加数据**
    
        `insert( )` 

        有三个参数第一个是表名，第二个用于指定数据在未指定添加数据的情况下给某些可为空的列自动复制，一般直接填null即可，第三个参数是一个ContentValues对象，它提供了一系列的put( )方法重载，用于向ContentValues添加数据，只需要将表中的每个列名以及相应的待添加数据传入即可
    
    - **更新数据**
        
        `update(table String, values: ContentValues, selection: String, selectionArgs: String[])`
    
    - **删除数据**
        
        `delete(table: String, selection: String, selctionArgs: String[])`
    
    - **查询数据**
        
        `query(table, columns, selection, selectionArgs, groupBy, having, orderBy): Cursor`

        | **Argurement** | **Usage** | **SQL Statement** |
        | --- | --- | --- |
        | table | 指定查询的表名 | from table_name |
        | columns | 指定查询的列名,指定为null就查询所有列 | select column1,column2 |
        | selection | 指定where约束条件，用于约束行，指定为null就查询所有行 | where column = value |
        | selectionArgs | 为where占位符提供具体的值 | -   |
        | groupBy | 指定需要groupBy的列，指定为null就表示不进行group by操作 | group by column |
        | having | 对group by后的结果进一步约束，null表示不进行过滤 | having column = value |

        - **Cursor对象的实例方法**

        |Method|Usage|
        |---|---|
        | moveToFirst( ) | 将指针移动到第一行的位置，如果成功就返回true|
        | moveToNext( ) | 将指针移动到下一行，如果移动到了||
        | getString(columnIndex: int) | 根据列标获取数据，还提供getInt()等一系列的方法|
        | getColumnIndex(columnName: String) | 根据给定的列名获取列标|
        | close() | 关闭Cursor|
