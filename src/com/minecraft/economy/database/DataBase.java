package com.minecraft.economy.database;

import java.util.List;

public interface DataBase {
    void login(String var1, String var2, String var3, String var4, String var5, String var6);

    void connect();

    void close();

    void createTable();

    void addData(List<String> var1);

    void deleteData(String var1);

    void setData(String var1, String var2, Object var3);

    void increaseData(String var1, String var2, Object var3, String var4);

    void reduceData(String var1, String var2, Object var3, String var4);

    Object getData(String var1, String var2);

    List<String> getAllKey();

    boolean isExist(String var1);
}
