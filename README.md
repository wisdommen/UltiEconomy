# UltiEconomy

使用方法介绍见：https://www.mcbbs.net/thread-1060351-1-1.html

-------

# UltiEconomyAPI介绍

## 如何在你的项目中设置UltiEconomyAPI

首先是将UltiEconomy.jar导入项目。

然后你需要做的是在主类里新建一个UltiEconomy对象。

    private static UltiEconomy economy;

并且你需要一个getter用来在其他类里获取economy。

    public static UltiEconomy getEconomy() {
        return economy;
    }

接着最好在主类里新建一个setup方法，用来检测依赖，虽然可以在plugin.yml中添加依赖，但是如果能弹出自定义的提醒不是更好吗？

    private Boolean setupEconomy(){
        if (getServer().getPluginManager().getPlugin("Economy") == null){
            return false;
        }else {
            economy = new UltiEconomy();
            return true;
        }
    }
    
再接着就需要在:    onEnable() 里添加代码。

在一切开始之前先检测是否找到经济前置：

    if (!setupEconomy()){
        getServer().getConsoleSender().sendMessage("无法找到经济前置插件，关闭本插件。。。");
        getServer().getPluginManager().disablePlugin(this);
        return;
    }
  
OK，一切初始化设置完成，接下来你只要在你的其他类里调用:    getEconomy() 即可获取到UltiEconomy对象。
例如：

    UltiEconomy economy = UltiTools.getEconomy();
    
## 完整代码实例：

### 在主类中：

    public final class ExamplePlugin extends JavaPlugin {
    
        private static UltiEconomy economy;

        public static UltiEconomy getEconomy() {
            return economy;
        }

        private Boolean setupEconomy(){
            if (getServer().getPluginManager().getPlugin("Economy") == null){
                return false;
            }else {
                economy = new UltiEconomy();
                return true;
            }
        }
        
        @Override
        public void onEnable() {
            plugin = this;

            if (!setupEconomy()){
                getServer().getConsoleSender().sendMessage("无法找到经济前置插件，关闭本插件。。。");
                getServer().getPluginManager().disablePlugin(this);
                return;
            }
            
            getServer().getConsoleSender().sendMessage("插件已加载！");
        }
        
        @Override
        public void onDisable() {
            getServer().getConsoleSender().sendMessage("插件已卸载！");
        }
    }

### 在其他类中

    public class subClass implements CommandExecutor {
    
        @Override
        public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;
                UltiEconomy economy = ExamplePlugin.getEconomy();
                if (command.getName().equalsIgnoreCase("money") && strings.length == 0) {
                    player.sendMessage("你有" + economy.checkMoney(player.getName()) + "枚金币！");
                    return true;
                }
            }
            return false;
        }
    }
    
你可以在我的源码中看到更多的例子，这里就不一一列举了。感谢使用！
