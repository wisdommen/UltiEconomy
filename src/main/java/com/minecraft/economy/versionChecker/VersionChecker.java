package com.minecraft.economy.versionChecker;

import com.minecraft.economy.economyMain.UltiEconomyMain;
import org.bukkit.ChatColor;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * The type Version checker.
 */
public class VersionChecker {

    /**
     * Sets thread.
     */
    public static void setupThread() {
        Thread checkVersionThread = new Thread() {
            public void run() {
                try {
                    //连接
                    HttpURLConnection connection = (HttpURLConnection) new URL("https://wisdommen.github.io").openConnection();
                    connection.setDoInput(true);
                    connection.setRequestMethod("GET");
                    //伪装
                    connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                    //获取输入流
                    InputStream input = connection.getInputStream();
                    //将字节输入流转换为字符输入流
                    InputStreamReader streamReader = new InputStreamReader(input, StandardCharsets.UTF_8);
                    //为字符输入流添加缓冲
                    BufferedReader br = new BufferedReader(streamReader);
                    // 读取返回结果
                    String data = br.readLine();
                    while (data != null) {
                        //获取带有附件id的文本
                        if (data.contains("UltiEconomy")) {
                            boolean isOutDate = false;
                            String target = br.readLine();
                            //获取版本
                            String version = target.split("version: ")[1].split("<")[0];
                            String current_version = UltiEconomyMain.getInstance().getDescription().getVersion();
                            List<String> current_version_list = Arrays.asList(current_version.split("\\."));
                            List<String> online_version_list = Arrays.asList(version.split("\\."));
                            UltiEconomyMain.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "[UltiEconomy] 正在检查更新...");
                            for (int i = 0; i < 3; i++) {
                                String a = current_version_list.get(i);
                                String b = online_version_list.get(i);
                                if (Integer.parseInt(a) < Integer.parseInt(b)) {
                                    if (i <= 1) {
                                        UltiEconomyMain.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.RED + "[UltiEconomy] 经济插件有 重要 更新，请到mcbbs上下载最新版本！");
                                    } else {
                                        UltiEconomyMain.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.RED + "[UltiEconomy] 经济插件有更新，请到mcbbs上下载最新版本！");
                                    }
                                    UltiEconomyMain.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "[UltiEconomy] 下载地址：https://www.mcbbs.net/thread-1060351-1-1.html");
                                    isOutDate = true;
                                    break;
                                }
                            }
                            if (!isOutDate) {
                                UltiEconomyMain.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "[UltiEconomy]太棒了！你的插件是最新的！保持最新的版本可以为你带来最好的体验！");
                            }
                            break;
                        }
                        data = br.readLine();
                    }
                    // 释放资源
                    br.close();
                    streamReader.close();
                    input.close();
                    connection.disconnect();
                    this.interrupt();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    this.interrupt();
                } catch (IOException e) {
                    e.printStackTrace();
                    this.interrupt();
                }
            }
        };
        checkVersionThread.start();
    }

}
