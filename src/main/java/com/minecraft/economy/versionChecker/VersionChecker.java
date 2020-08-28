package com.minecraft.economy.versionChecker;

import com.minecraft.economy.economyMain.UltiEconomyMain;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.minecraft.economy.utils.Utils.getFiles;

/**
 * The type Version checker.
 */
public class VersionChecker {
    private static String version;
    private static String current_version;

    /**
     * Sets thread.
     */
    public static void setupThread() {
        new BukkitRunnable() {
            @Override
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
                            version = target.split("version: ")[1].split("<")[0];
                            current_version = UltiEconomyMain.getInstance().getDescription().getVersion();
                            int currentVersion = getVersion(current_version);
                            int onlineVersion = getVersion(version);
                            UltiEconomyMain.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "[UltiEconomy] 正在检查更新...");
                            if (currentVersion < onlineVersion) {
                                isOutDate = true;
                                UltiEconomyMain.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.RED + String.format("[UltiEconomy] 经济插件最新版为%s，你的版本是%s！请下载最新版本！", version, current_version));
                                if (UltiEconomyMain.getInstance().getConfig().getBoolean("enable_auto_update")) {
                                    downloadNewVersion();
                                } else {
                                    UltiEconomyMain.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.RED + "[UltiEconomy] 下载地址：https://www.mcbbs.net/thread-1060351-1-1.html");
                                    UltiEconomyMain.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.RED + "[UltiEconomy] 你知道吗？现在UltiEconomy可以自动更新啦！在配置文件中打开自动更新，更新再也不用麻烦！");
                                }
                            }
                            if (!isOutDate) {
                                deleteOldVersion();
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(UltiEconomyMain.getInstance());
    }

    private static void downloadNewVersion() {
        new BukkitRunnable() {

            @Override
            public void run() {
                UltiEconomyMain.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[UltiEconomy] 正在下载更新...");
                if (download()){
                    UltiEconomyMain.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[UltiEconomy] 下载完成, 重启服务器以应用更新！");
                    this.cancel();
                    return;
                }
                UltiEconomyMain.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.GREEN + String.format("[UltiEconomy] 下载失败，请前往 %s 手动下载！", "https://www.mcbbs.net/thread-1060351-1-1.html"));
            }

        }.runTaskAsynchronously(UltiEconomyMain.getInstance());
    }

    private static boolean download (){
        try {
            URL url = new URL("https://raw.githubusercontent.com/wisdommen/wisdommen.github.io/master/collections/UltiEconomy/UltiEconomy-" + version + ".jar");
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream(UltiEconomyMain.getInstance().getDataFolder().getPath().replace("\\UltiEconomy", "") + "\\UltiEconomy-" + version + ".jar");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void deleteOldVersion () {
        List<File> files = getFiles(UltiEconomyMain.getInstance().getDataFolder().getPath().replace("\\UltiEconomy", ""));
        for (File file : files) {
            if (file.getName().contains("UltiEconomy-") && !file.getName().equals("UltiEconomy-" + version + ".jar")) {
                file.delete();
            }
        }
    }

    private static int getVersion(String version) {
        while (version.contains(".")) {
            version = version.replace(".", "");
        }
        return Integer.parseInt(version);
    }
}
