package com.example.genshin.gson;

public class ResponseJson {

    public int retcode;

    public String message;

    public Data data;

    @Override
    public String toString() {
        return "ResponseJson{" +
                "retcode=" + retcode +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
