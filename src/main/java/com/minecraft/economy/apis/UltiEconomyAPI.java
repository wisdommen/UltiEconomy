package com.minecraft.economy.apis;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * The interface Ulti economy api.
 */
public interface UltiEconomyAPI {

    /**
     * 非必要请勿使用！
     *
     * @param player_name 玩家名
     * @return 玩家数据文件 player file
     */
    public File getPlayerFile(String player_name);

    /**
     * 非必要请勿使用！
     *
     * @param file 文件
     * @return 玩家数据yml配置对象 yaml configuration
     */
    public YamlConfiguration loadConfig(File file);

    /**
     * 非必要请勿使用！
     *
     * @param player_name 玩家名
     * @return 玩家数据文档是否存在 boolean
     */
    public Boolean playerFileExists(String player_name);

    /**
     * 获取玩家现金
     *
     * @param player_name 玩家名
     * @return 玩家的现金数量 ，如果没有找到玩家就返回-1
     */
    public Integer checkMoney(String player_name);

    /**
     * 获取玩家存款
     *
     * @param player_name 玩家名
     * @return 玩家的存款数量 ，如果没有找到玩家就返回-1
     */
    public Integer checkBank(String player_name);

    /**
     * 添加金额到玩家现金
     *
     * @param player_name 玩家名，你想给钱的那个人
     * @param amount      数额，请不要输入负数
     * @return 是否添加成功 boolean
     */
    public Boolean addTo(String player_name, Integer amount);

    /**
     * 添加金额到玩家存款
     *
     * @param player_name 玩家名，你想给钱的那个人
     * @param amount      数额，请不要输入负数
     * @return 是否添加成功 boolean
     */
    public Boolean addToBank(String player_name, Integer amount);

    /**
     * 从玩家现金中取钱
     *
     * @param player_name 玩家名，你想取走钱的那个人
     * @param amount      数额，请不要输入负数
     * @return 是否取走成功 boolean
     */
    public Boolean takeFrom(String player_name, Integer amount);

    /**
     * 从玩家银行中取钱
     *
     * @param player_name 玩家名，你想取走钱的那个人
     * @param amount      数额，请不要输入负数
     * @return 是否取走成功 boolean
     */
    public Boolean takeFromBank(String player_name, Integer amount);

    /**
     * 玩家间转账
     *
     * @param payer  付款人名称
     * @param payee  收款人名称
     * @param amount 数额
     * @return 是否转账成功 boolean
     */
    public Boolean transferMoney(String payer, String payee, Integer amount);

    /**
     * 从一个玩家的现金转钱到存款
     *
     * @param player_name 玩家名
     * @param amount      数额
     * @return 是否成功 boolean
     */
    public Boolean transferMoneyToBank(String player_name, Integer amount);

    /**
     * 从一个玩家的存款转钱到现金
     *
     * @param player_name 玩家名
     * @param amount      数额
     * @return 是否成功 boolean
     */
    public Boolean transferBankToMoney(String player_name, Integer amount);
}
