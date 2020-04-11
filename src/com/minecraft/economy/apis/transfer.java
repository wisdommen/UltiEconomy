package com.minecraft.economy.apis;

public class transfer {

    public static Boolean transferMoney(String payer, String payee, Integer amount){
        try {
            assert amount >= 0;
                return addMoney.addTo(payee, amount) && takeMoney.takeFrom(payer, amount);
        }catch (AssertionError e){
            System.out.println("数额异常:"+e);
        }
        return false;
    }

    public static Boolean transferMoneyToBank(String player_name, Integer amount){
        try {
            assert amount >= 0;
                return addMoney.addToBank(player_name, amount) && takeMoney.takeFrom(player_name, amount);
        }catch (AssertionError e){
            System.out.println("数额异常:"+e);
        }
        return false;
    }

    public static Boolean transferBankToMoney(String player_name, Integer amount){
        try {
            assert amount >= 0;
                return addMoney.addTo(player_name, amount) && takeMoney.takeFromBank(player_name, amount);
        }catch (AssertionError e){
            System.out.println("数额异常:"+e);
        }
        return false;
    }
}
