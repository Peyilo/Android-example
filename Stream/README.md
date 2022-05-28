# 文件存储
    
- **将数据存储到文件中**
    
|Method | Usage |
| --- | --- |
| *openFileOutput( ): FileOutputStream* | Context类提供，接收两个参数，第一个参数是文件名，不能包括路径，因为所有的文件默认存储到`/data/data/\<package name>/files/`目录下，第二个参数是文件的操作模式，主要有两个模式可选，MODE\_PRIVATE是默认的操作方式，表示覆盖写，MODE\_APPEND表示追加写，如果文件不存在则创建文件 |
| *OutputStreamWriter( ): OutStreamWriter* | 从一个FileOutputStream对象创建OutputStreamWriter实例 |
| *BufferedWriter( ): BufferedWriter* | 从一个OpenStreamWriter对象创建BufferedWriter实例 |
| *write(String data)* | BufferedWriter类的方法，用于向文件中写入字符串 |
| *close( ): void* | BufferedWriter类的方法，用于关闭BufferedWriter |
    
- **从文件读取数据**
    
|Method | Usage |
| --- | --- |
| *openFileInput( ): FileInputStream* | Context类提供，只接收一个文件名参数，默认去`/data/data/<package name>/files/`目录下加载这个文件 |
| *InputStreamReader( ): InputStreamWriter* | 从一个FileOutputStream对象创建InputStreamReader实例 |
| *BufferedReader( ): BufferedReader* | 从一个InputStreamReader对象创建BufferedReader实例 |
| *readLine( ): String* | BufferedReader类的方法，读取BufferedReader实例中的内容的一行 |
| *close( ): void* | 关闭BufferedReader实例 |
| *TextUtils.isEmpty(String: str): boolean* | Static，用于判断字符创str是否为空字符串或者null |
