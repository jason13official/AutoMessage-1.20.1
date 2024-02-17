package net.jason13.automessage.config;

import net.jason13.automessage.util.toml.Toml;
import net.jason13.automessage.util.toml.TomlWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Config
{
  
  private static final String CONFIG_PATH = "config/automessage-common.toml";
  public static final File CONFIG_FILE = new File(CONFIG_PATH);
  
  public static boolean configFound()
  {
    return (CONFIG_FILE.isFile() && !CONFIG_FILE.isDirectory());
  }
  
  public static Toml readTomlFromFile()
  {
    return new Toml().read(CONFIG_FILE);
  }
  
  public static void initialize()
  {
    TomlWriter tomlWriter = new TomlWriter();
    ConfigObject dataObject = new ConfigObject();
    
    List<Object> defaultMessages = new ArrayList<>();
    List<Object> defaultLinks = new ArrayList<>();
    List<Object> defaultIntervals = new ArrayList<>();
    List<Object> defaultSoftLimits = new ArrayList<>();
    List<Object> defaultHardLimits = new ArrayList<>();
    
    dataObject.setAm_enabled(false);
    
    defaultMessages.add("Default message #1! Sends after 5 seconds, only once ever!");
    defaultLinks.add("https://www.google.com");
    defaultIntervals.add(5);
    defaultSoftLimits.add(1);
    defaultHardLimits.add(1);
    
    defaultMessages.add("Default message #2! Sends after 6 seconds, 5 times every session!");
    defaultLinks.add("https://www.yahoo.com");
    defaultIntervals.add(6);
    defaultSoftLimits.add(5);
    defaultHardLimits.add(0);
    
    dataObject.setMessages(defaultMessages);
    dataObject.setLinks(defaultLinks);
    dataObject.setIntervals(defaultIntervals);
    dataObject.setSoft_limits(defaultSoftLimits);
    dataObject.setHard_limits(defaultHardLimits);
    
    try
    {
      FileWriter fileWriter = new FileWriter(CONFIG_PATH);
      tomlWriter.write(dataObject, fileWriter);
      fileWriter.close();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  private static class ConfigObject
  {
    private List<Object> messages;
    private List<Object> links;
    private List<Object> intervals;
    private List<Object> soft_limits;
    private List<Object> hard_limits;
    private boolean am_enabled;
    
    public List<Object> getMessages()
    {
      return messages;
    }
    
    public void setMessages(List<Object> messages)
    {
      this.messages = messages;
    }
    
    public List<Object> getLinks()
    {
      return links;
    }
    
    public void setLinks(List<Object> links)
    {
      this.links = links;
    }
    
    public List<Object> getIntervals()
    {
      return intervals;
    }
    
    public void setIntervals(List<Object> intervals)
    {
      this.intervals = intervals;
    }
    
    public List<Object> getSoft_limits()
    {
      return soft_limits;
    }
    
    public void setSoft_limits(List<Object> soft_limits)
    {
      this.soft_limits = soft_limits;
    }
    
    public List<Object> getHard_limits()
    {
      return hard_limits;
    }
    
    public void setHard_limits(List<Object> hard_limits)
    {
      this.hard_limits = hard_limits;
    }
    
    public boolean getAm_enabled()
    {
      return am_enabled;
    }
    
    public void setAm_enabled(boolean am_enabled)
    {
      this.am_enabled = am_enabled;
    }
  }
  
}
