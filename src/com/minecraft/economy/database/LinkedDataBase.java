package com.minecraft.economy.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LinkedDataBase implements DataBase {
    private String SqlIp = null;
    private String SqlPort = null;
    private String Sql = null;
    private String SqlTable = null;
    private String SqlUser = null;
    private String SqlPasd = null;
    private String[] fields;
    private Connection connect = null;
    private Statement statement = null;
    private ResultSet res = null;
    private PreparedStatement ps = null;
    private String key;
    private List<String> field = new ArrayList();

    public LinkedDataBase(String[] fields) {
        this.fields = fields;
        String[] var5 = fields;
        int var4 = fields.length;

        for(int var3 = 0; var3 < var4; ++var3) {
            String filed = var5[var3];
            this.field.add(filed);
        }

        this.key = this.field.get(0);
    }

    public void login(String ip, String port, String user, String password, String database, String table) {
        this.SqlIp = ip;
        this.SqlPort = port;
        this.Sql = database;
        this.SqlUser = user;
        this.SqlPasd = password;
        this.SqlTable = table;
    }

    public void connect() {
        String url = "jdbc:mysql://" + this.SqlIp + ":" + this.SqlPort + "/" + this.Sql + "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
        String user = this.SqlUser;
        String password = this.SqlPasd;

        try {
            this.connect = DriverManager.getConnection(url, user, password);
            this.statement = this.connect.createStatement();
            this.ps = this.connect.prepareStatement("create table if not exists " + this.SqlTable + "(" + this.getFields() + ")");
        } catch (SQLException var5) {
            var5.printStackTrace();
        }

    }

    public void close() {
        try {
            if (this.res != null) {
                this.res.close();
                this.res = null;
            }
        } catch (SQLException var486) {
            var486.printStackTrace();
        } finally {
            try {
                if (this.ps != null) {
                    this.ps.close();
                    this.ps = null;
                }
            } catch (SQLException var484) {
                var484.printStackTrace();
            } finally {
                try {
                    if (this.statement != null) {
                        this.statement.close();
                        this.statement = null;
                    }
                } catch (SQLException var482) {
                    var482.printStackTrace();
                } finally {
                    try {
                        if (this.connect != null) {
                            this.connect.close();
                            this.connect = null;
                        }
                    } catch (SQLException var481) {
                        var481.printStackTrace();
                    }

                }

            }

        }

    }

    public void createTable() {
        try {
            this.ps.executeUpdate();
            this.ps = this.connect.prepareStatement("alter table " + this.SqlTable + " convert to character set utf8");
            this.ps.executeUpdate();
        } catch (SQLException var2) {
            var2.printStackTrace();
        }

    }

    public void addData(List<String> data) {
        try {
            PreparedStatement ps = this.connect.prepareStatement("select * from " + this.SqlTable + " where " + this.key + "=?");
            ps.setString(1, data.get(0));
            this.res = ps.executeQuery();
            if (this.res.next()) {
                ps = this.connect.prepareStatement("update " + this.SqlTable + " " + this.getSettingSentence(this.field, data) + " where " + this.key + "=?");
                ps.setString(1, data.get(0));
                ps.executeUpdate();
            } else {
                ps = this.connect.prepareStatement("insert into " + this.SqlTable + " values(" + this.getInsertSentence(data) + ")");
                ps.executeUpdate();
            }
        } catch (SQLException var3) {
            var3.printStackTrace();
        }

    }

    public void deleteData(String keyData) {
        try {
            PreparedStatement ps = this.connect.prepareStatement("delete from " + this.SqlTable + " where " + this.key + "=?");
            ps.setString(1, keyData);
            ps.executeUpdate();
        } catch (SQLException var3) {
            var3.printStackTrace();
        }

    }

    public void setData(String keydata, String value, Object valuedata) {
        try {
            this.res = this.statement.executeQuery("select 1 from " + this.SqlTable + " where " + this.key + "='" + keydata + "'");
            if (!this.res.next()) {
                this.statement.executeUpdate("insert into " + this.SqlTable + " values('" + keydata + "','" + valuedata + "')");
            } else {
                this.res = this.statement.executeQuery("select " + value + " from " + this.SqlTable + " where " + this.key + "='" + keydata + "'");
                this.statement.executeUpdate("update " + this.SqlTable + " set " + value + "='" + valuedata + "' where " + this.key + "='" + keydata + "'");
            }
        } catch (SQLException var5) {
            var5.printStackTrace();
        }

    }

    public void increaseData(String keydata, String value, Object valuedata, String datatype) {
        try {
            this.res = this.statement.executeQuery("select "+value+" from " + this.SqlTable + " where " + this.key + "='" + keydata + "'");
            if (this.res.next()) {
                Object temp = this.res.getString(1);
                if (datatype.equals("int")){
                    int data = Integer.parseInt(temp.toString());
                    int valueupdated = ((int)valuedata)+data;
                    if (!this.res.next()) {
                        this.res = this.statement.executeQuery("select " + value + " from " + this.SqlTable + " where " + this.key + "='" + keydata + "'");
                        this.statement.executeUpdate("update " + this.SqlTable + " set " + value + "='" + valueupdated + "' where " + this.key + "='" + keydata + "'");
                    }
                }else if (datatype.equals("double")){
                    double data = Double.parseDouble(temp.toString());
                    double valueupdated = ((double)valuedata)+data;
                    if (!this.res.next()) {
                        this.res = this.statement.executeQuery("select " + value + " from " + this.SqlTable + " where " + this.key + "='" + keydata + "'");
                        this.statement.executeUpdate("update " + this.SqlTable + " set " + value + "='" + valueupdated + "' where " + this.key + "='" + keydata + "'");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void reduceData(String keydata, String value, Object valuedata, String datatype) {
        try {
            this.res = this.statement.executeQuery("select "+value+" from " + this.SqlTable + " where " + this.key + "='" + keydata + "'");
            if (this.res.next()) {
                Object temp = this.res.getString(1);
                if (datatype.equals("int")){
                    int data = Integer.parseInt(temp.toString());
                    int valueupdated = data-((int)valuedata);
                    if (!this.res.next()) {
                        this.res = this.statement.executeQuery("select " + value + " from " + this.SqlTable + " where " + this.key + "='" + keydata + "'");
                        this.statement.executeUpdate("update " + this.SqlTable + " set " + value + "='" + valueupdated + "' where " + this.key + "='" + keydata + "'");
                    }
                }else if (datatype.equals("double")){
                    double data = Double.parseDouble(temp.toString());
                    double valueupdated = data-((double)valuedata);
                    if (!this.res.next()) {
                        this.res = this.statement.executeQuery("select " + value + " from " + this.SqlTable + " where " + this.key + "='" + keydata + "'");
                        this.statement.executeUpdate("update " + this.SqlTable + " set " + value + "='" + valueupdated + "' where " + this.key + "='" + keydata + "'");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Object getData(String keydata, String value) {
        try {
            this.res = this.statement.executeQuery("select " + value + " from " + this.SqlTable + " where " + this.key + "='" + keydata + "'");
            if (this.res.next()) {
                Object data = this.res.getString(1);
                return data;
            }
        } catch (SQLException var4) {
            var4.printStackTrace();
        }

        return null;
    }

    public List<String> getAllKey() {
        try {
            PreparedStatement ps = this.connect.prepareStatement("select * from " + this.SqlTable);
            this.res = ps.executeQuery();
            ArrayList args = new ArrayList();

            while(this.res.next()) {
                args.add(this.res.getString(1));
            }

            return args;
        } catch (SQLException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public boolean isExist(String keydata) {
        try {
            this.res = this.statement.executeQuery("select 1 from " + this.SqlTable + " where " + this.key + "='" + keydata + "'");
            return this.res.next();
        } catch (SQLException var3) {
            var3.printStackTrace();
            return false;
        }
    }

    private String getFields() {
        StringBuilder builder = new StringBuilder();
        int i = 0;
        String[] var6;
        int var5 = (var6 = this.fields).length;

        for(int var4 = 0; var4 < var5; ++var4) {
            String arg = var6[var4];
            ++i;
            builder.append(arg + " TEXT" + (i == this.fields.length ? "" : ","));
        }

        return builder.toString();
    }

    private String getSettingSentence(List<String> fields, List<String> data) {
        StringBuilder builder = new StringBuilder();

        for(int i = 0; i < fields.size(); ++i) {
            builder.append("set " + fields.get(i) + "='" + data.get(i) + "'" + (i == fields.size() - 1 ? "" : " , "));
        }

        return builder.toString();
    }

    private String getInsertSentence(List<String> data) {
        StringBuilder builder = new StringBuilder();
        Iterator var4 = data.iterator();

        while(var4.hasNext()) {
            String arg = (String)var4.next();
            builder.append("'" + arg + "'" + (data.indexOf(arg) == data.size() - 1 ? "" : ","));
        }

        return builder.toString();
    }
}
