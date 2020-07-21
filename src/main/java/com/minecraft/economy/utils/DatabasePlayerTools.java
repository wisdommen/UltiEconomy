package com.minecraft.economy.utils;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

public class DatabasePlayerTools {

    private static final String table = "player_economy_data";
    private static final String primaryID = "Name";

    private DatabasePlayerTools(){}

    public static boolean isPlayerExist(String playerName){
        return DatabaseUtils.isRecordExists(table, primaryID, playerName);
    }

    public static String getPlayerData(String playerName, String field){
        return DatabaseUtils.getData(primaryID, playerName, table, field);
    }

    public static List<String> getKeys(){
        return DatabaseUtils.getKeys(table, primaryID);
    }

    public static boolean updatePlayerData(String playerName, String field, String value){
        return DatabaseUtils.updateData(table, field, primaryID, playerName, value);
    }

    public static boolean insertPlayerData(Map<String, String> dataMap){
        return DatabaseUtils.insertData(table, dataMap);
    }

    public static PreparedStatement increasePlayerDataStandby(String field, String playerName, int amount){
        return DatabaseUtils.increaseDataStandby(table, field, primaryID, playerName, String.valueOf(amount));
    }

    public static boolean increasePlayerData(String field, String playerName, int amount, List<PreparedStatement> statementList){
        return DatabaseUtils.increaseData(table, field, primaryID, playerName, String.valueOf(amount), false, statementList);
    }

    public static boolean increasePlayerData(String field, String playerName, int amount){
        return DatabaseUtils.increaseData(table, field, primaryID, playerName, String.valueOf(amount));
    }

    public static PreparedStatement decreasePlayerDataStandby(String field, String playerName, int amount){
        return DatabaseUtils.decreaseDataStandby(table, field, primaryID, playerName, String.valueOf(amount));
    }

    public static boolean decreasePlayerData(String field, String playerName, int amount, List<PreparedStatement> statementList){
        return DatabaseUtils.decreaseData(table, field, primaryID, playerName, String.valueOf(amount), false, statementList);
    }

    public static boolean decreasePlayerData(String field, String playerName, int amount){
        return DatabaseUtils.decreaseData(table, field, primaryID, playerName, String.valueOf(amount));
    }
}
