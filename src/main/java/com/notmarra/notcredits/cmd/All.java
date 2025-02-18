package  com.notmarra.notcredits.cmd;

public class All {
    public static void execute(CommandSender sender, double amount) {
        boolean isConsole = !(sender instanceof Player);
        Player player = isConsole ? null : (Player) sender;

        if (!isConsole && !player.hasPermission("notcredits.all")) {
            Message.sendMessage(player, "no_perm", false, null);
            return;
        }

        List<Player> players = GetPlayers.get();

        Bukkit.getScheduler().runTaskAsynchronously(NotCredits.getInstance(), () -> {
            for (Player p : players) {
                double credits = DatabaseManager.getInstance(NotCredits.getInstance()).getBalance(targetPlayerId);
                double actualAmount = amount;
                credits += amount;
                DatabaseManager.getInstance(NotCredits.getInstance()).setBalance(p.getUniqueId().toString(), credits);

                //TODO: Add message
            }
            Map<String, String> replacements = new HashMap<>();
            replacements.put("amount", Numbers.formatBalance(amount));
            Message.sendMessage(player, "all", isConsole, replacements);
        });
    }
}