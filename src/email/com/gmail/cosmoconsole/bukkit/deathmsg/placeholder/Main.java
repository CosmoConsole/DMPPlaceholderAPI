package email.com.gmail.cosmoconsole.bukkit.deathmsg.placeholder;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.projectiles.ProjectileSource;

import email.com.gmail.cosmoconsole.bukkit.deathmsg.DeathMessageTagListener;
import email.com.gmail.cosmoconsole.bukkit.deathmsg.DeathMessagesPrime;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class Main extends JavaPlugin implements Listener, DeathMessageTagListener {
	DeathMessagesPrime dmp;
	boolean ph;
	boolean mph;
	public void onEnable() {
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)this);
        dmp = (DeathMessagesPrime) Bukkit.getPluginManager().getPlugin("DeathMessagesPrime");
        ph = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
        mph = Bukkit.getPluginManager().isPluginEnabled("MVdWPlaceholderAPI");
        if (ph) {
            dmp.registerTagPrefix(this, "ph", this);
            dmp.registerTagPrefix(this, "phk", this);
            getLogger().info("Found PlaceholderAPI, %ph_ and %phk_ DMP placeholders registered");
        }
        if (mph) {
            dmp.registerTagPrefix(this, "mph", this);
            dmp.registerTagPrefix(this, "mphk", this);
            getLogger().info("Found MVdWPlaceholderAPI, %mph_ and %mphk_ DMP placeholders registered");
        }
	}
    private BaseComponent[] safe_fromLegacyText(String s) {
        if (s == null) return new BaseComponent[] {};
        return TextComponent.fromLegacyText(s);
    }
	@Override
	public TextComponent formatTag(String arg0, Player arg1, DamageCause arg2, Entity arg3) {
		if (ph) {
			if (arg0.length() > 3 && arg0.startsWith("ph_")) {
				return new TextComponent(safe_fromLegacyText(me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(arg1, createTag(arg0.substring(3)))));
			} else if (arg0.length() > 4 && arg0.startsWith("phk_") && arg3 != null) {
			    Entity damager = resolveDamager(arg3);
			    if (damager instanceof Player)
			        return new TextComponent(safe_fromLegacyText(me.clip.placeholderapi.PlaceholderAPI.setPlaceholders((Player)damager, createTag(arg0.substring(4)))));
			}
		}
		if (mph) {
			if (arg0.length() > 4 && arg0.startsWith("mph_")) {
				return new TextComponent(safe_fromLegacyText(be.maximvdw.placeholderapi.PlaceholderAPI.replacePlaceholders(arg1, createTag(arg0.substring(4)))));
			} else if (arg0.length() > 5 && arg0.startsWith("mphk_") && arg3 != null && arg3 instanceof Player) {
			    Entity damager = resolveDamager(arg3);
                if (damager instanceof Player)
                    return new TextComponent(safe_fromLegacyText(be.maximvdw.placeholderapi.PlaceholderAPI.replacePlaceholders((Player)damager, createTag(arg0.substring(5)))));
			}
		}
		return new TextComponent();
	}
    private Entity resolveDamager(Entity arg3) {
        if (arg3 instanceof TNTPrimed) {
            return ((TNTPrimed)arg3).getSource();
        } else if (arg3 instanceof Projectile) {
            ProjectileSource ps = ((Projectile)arg3).getShooter();
            if (ps instanceof Entity)
                return (Entity)ps;
        }
        return arg3;
    }
    private String createTag(String s) {
		if ((s.startsWith("{") && s.endsWith("}"))
				|| (s.startsWith("(") && s.endsWith(")"))
				|| (s.startsWith("[") && s.endsWith("]"))
				|| (s.startsWith("<") && s.endsWith(">"))) {
			return s;
		}
		return "%" + s + "%";
	}
}
