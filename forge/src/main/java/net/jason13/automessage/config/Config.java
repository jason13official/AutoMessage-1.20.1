package net.jason13.automessage.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;

public class Config
{
  public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
  
  public static final ForgeConfigSpec.ConfigValue<List<String>> MESSAGES;
  public static final ForgeConfigSpec.ConfigValue<List<String>> LINKS;
  public static final ForgeConfigSpec.ConfigValue<List<Integer>> INTERVALS;
  public static final ForgeConfigSpec.ConfigValue<List<Integer>> SOFT_LIMITS;
  public static final ForgeConfigSpec.ConfigValue<List<Integer>> HARD_LIMITS;
  public static final ForgeConfigSpec.ConfigValue<Boolean> AM_ENABLED;
  
  public static ArrayList<String> defaultMessages = new ArrayList<>();
  public static ArrayList<String> defaultLinks = new ArrayList<>();
  public static ArrayList<Integer> defaultIntervals = new ArrayList<>();
  public static ArrayList<Integer> defaultSoftLimits = new ArrayList<>();
  public static ArrayList<Integer> defaultHardLimits = new ArrayList<>();
  public static Boolean defaultAMEnabledValue = false;
  
  private static void buildDefaultConfigValues() {
    defaultMessages.add(0, "Default message #1! Sends after 5 seconds, only once ever!");
    defaultLinks.add(0, "https://www.google.com");
    defaultIntervals.add(0, 5);
    defaultSoftLimits.add(0, 1);
    defaultHardLimits.add(0, 1);
    
    defaultMessages.add(1, "Default message #2! Sends after 6 seconds, 5 times every session!");
    defaultLinks.add(1, "https://www.yahoo.com");
    defaultIntervals.add(1, 6);
    defaultSoftLimits.add(1, 5);
    defaultHardLimits.add(1, 0);
  }
  
  public static final ForgeConfigSpec SPEC;
  
  static
  {
    buildDefaultConfigValues();
    
    BUILDER.push("AutoMessage");
    
    MESSAGES = BUILDER.comment(" The messages to be sent").define("MESSAGES", defaultMessages);
    LINKS = BUILDER.comment(" Should we attach a link to this message?").define("LINKS", defaultLinks);
    INTERVALS = BUILDER.comment(" How frequently / after how long should the message be sent?").define("INTERVALS", defaultIntervals);
    SOFT_LIMITS = BUILDER.comment(" How many times should the message be sent per session? 0 = infinite").define("SOFT_LIMITS", defaultSoftLimits);
    HARD_LIMITS = BUILDER.comment(" How many times should the message be sent EVER? 0 = infinite").define("HARD_LIMITS", defaultHardLimits);
    AM_ENABLED = BUILDER.comment(" Should AutoMessage be Enabled? default: false").define("AM_ENABLED", defaultAMEnabledValue);
    
    BUILDER.pop();
    
    SPEC = BUILDER.build();
  }
  
}
