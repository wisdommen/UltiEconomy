package com.minecraft.economy.utils;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * The type Database player tools.
 */
public class DatabasePlayerTools {

    private static final String table = "player_economy_data";
    private static final String primaryID = "Name";

    private DatabasePlayerTools(){}

    /**
     * Is player exist boolean.
     *
     * @param playerName the player name
     * @return the boolean
     */
    public static boolean isPlayerExist(String playerName){
        return DatabaseUtils.isRecordExists(table, primaryID, playerName);
    }

    /**
     * Get player data string.
     *
     * @param playerName the player name
     * @param field      the field
     * @return the string
     */
    public static String getPlayerData(String playerName, String field){
        return DatabaseUtils.getData(primaryID, playerName, table, field);
    }

    /**
     * Get keys list.
     *
     * @return the list
     */
    public static @NotNull List<String> getKeys(){
        return DatabaseUtils.getKeys(table, primaryID);
    }

    /**
     * Update player data boolean.
     *
     * @param playerName the player name
     * @param field      the field
     * @param value      the value
     * @return the boolean
     */
    public static boolean updatePlayerData(String playerName, String field, String value){
        return DatabaseUtils.updateData(table, field, primaryID, playerName, value);
    }

    /**
     * Insert player data boolean.
     *
     * @param dataMap the data map
     * @return the boolean
     */
    public static boolean insertPlayerData(Map<String, String> dataMap){
        return DatabaseUtils.insertData(table, dataMap);
    }

    /**
     * Increase player data standby prepared statement.
     *
     * @param field      the field
     * @param playerName the player name
     * @param amount     the amount
     * @return the prepared statement
     */
    public static UUID increasePlayerDataStandby(String field, String playerName, int amount){
        return DatabaseUtils.increaseDataStandby(table, field, primaryID, playerName, String.valueOf(amount));
    }

    /**
     * Increase player data boolean.
     *
     * @param field         the field
     * @param playerName    the player name
     * @param amount        the amount
     * @param statementList the statement list
     * @return the boolean
     */
    public static boolean increasePlayerData(String field, String playerName, int amount, List<UUID> statementList){
        return DatabaseUtils.increaseData(table, field, primaryID, playerName, String.valueOf(amount), false, statementList);
    }

    /**
     * Increase player data boolean.
     *
     * @param field      the field
     * @param playerName the player name
     * @param amount     the amount
     * @return the boolean
     */
    public static boolean increasePlayerData(String field, String playerName, int amount){
        return DatabaseUtils.increaseData(table, field, primaryID, playerName, String.valueOf(amount));
    }

    /**
     * Decrease player data standby prepared statement.
     *
     * @param field      the field
     * @param playerName the player name
     * @param amount     the amount
     * @return the prepared statement
     */
    public static UUID decreasePlayerDataStandby(String field, String playerName, int amount){
        return DatabaseUtils.decreaseDataStandby(table, field, primaryID, playerName, String.valueOf(amount));
    }

    /**
     * Decrease player data boolean.
     *
     * @param field         the field
     * @param playerName    the player name
     * @param amount        the amount
     * @param statementList the statement list
     * @return the boolean
     */
    public static boolean decreasePlayerData(String field, String playerName, int amount, List<UUID> statementList){
        return DatabaseUtils.decreaseData(table, field, primaryID, playerName, String.valueOf(amount), false, statementList);
    }

    /**
     * Decrease player data boolean.
     *
     * @param field      the field
     * @param playerName the player name
     * @param amount     the amount
     * @return the boolean
     */
    public static boolean decreasePlayerData(String field, String playerName, int amount){
        return DatabaseUtils.decreaseData(table, field, primaryID, playerName, String.valueOf(amount));
    }
}
