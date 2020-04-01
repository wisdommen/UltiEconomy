package com.minecraft.economy.apis;

public class transfer {

    public static Boolean transferMoney(String payer, String payee, Integer amount){
        try {

            assert amount >= 0;
            if (checkMoney.player_file_exists(payer) && checkMoney.player_file_exists(payee)) {
                return addMoney.addTo(payee, amount) && takeMoney.takeFrom(payer, amount);
            }
        }catch (AssertionError e){
            System.out.println("数额异常:"+e);
        }
        return false;
    }
}
