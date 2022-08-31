package mc.carlton.freerpg.serverInfo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import mc.carlton.freerpg.FreeRPG;
import mc.carlton.freerpg.configStorage.ConfigLoad;
import org.apache.logging.log4j.Level;
import org.bukkit.plugin.Plugin;

public class RunTimeData {

  static ArrayList<Double> breakBlockTrackedBlockCheckTimes = new ArrayList<>();
  static ArrayList<Double> breakBlockConditionalsTimes = new ArrayList<>();
  static ArrayList<Double> leftCLickConditionalsTimes = new ArrayList<>();
  static ArrayList<Double> changeEXPTimes = new ArrayList<>();
  static ArrayList<Double> flintFinderTimes = new ArrayList<>();
  static ArrayList<Double> diggingTreasureDropTimes = new ArrayList<>();
  static ArrayList<Double> doubleDropTimes = new ArrayList<>();
  static ArrayList<Double> logXPDropTimes = new ArrayList<>();
  static ArrayList<Double> logBookDropTimes = new ArrayList<>();
  static ArrayList<Double> leavesDropTimes = new ArrayList<>();
  static ArrayList<Double> timedHasteTimes = new ArrayList<>();
  static ArrayList<Double> miningTreasureDropTimes = new ArrayList<>();
  static ArrayList<Double> miningWastelessHasteTimes = new ArrayList<>();
  static ArrayList<Double> veinMinerTimes = new ArrayList<>();
  static ArrayList<Double> leafBlowerTimes = new ArrayList<>();
  static ArrayList<Double> storeBlockFaceTimes = new ArrayList<>();


  public void logRunTimeData() {
    Plugin plugin = FreeRPG.getPlugin(FreeRPG.class);
    File runTimeDataFolder = new File(plugin.getDataFolder(), File.separator + "RunTimes");
    if (!runTimeDataFolder.exists()) {
      runTimeDataFolder.mkdir();
    }
    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");//dd/MM/yyyy
    Date now = new Date();
    String strDate0 = sdfDate.format(now);
    String strDate = strDate0.replaceAll(" ", "_");
    File f = new File(runTimeDataFolder, "RunTimeData_" + strDate + ".dat");
    f.setReadable(true, false);
    f.setWritable(true, false);
    String path = f.getPath();
    try {
      if (!f.exists()) {
        f.createNewFile();
        try (FileWriter fileWriter = new FileWriter(path)) {
          ArrayList<ArrayList<Double>> allData = getAllData();
          for (int i = 0; i < allData.size(); i++) {
            writeHeader(fileWriter, i);
            writeBody(fileWriter, allData.get(i));
          }

        } catch (IOException exception) {
          FreeRPG.log(Level.ERROR, exception.getMessage());
        }
      }
    } catch (IOException error) {
      error.printStackTrace();
    }
  }

  public void addTime(long time, String timeGroup) {
    double timeInSeconds = (double) time / 1000.0;
    if (timeGroup.equalsIgnoreCase("BreakBlocktrackedBlocks")) {
      breakBlockTrackedBlockCheckTimes.add(timeInSeconds);
    } else if (timeGroup.equalsIgnoreCase("BreakBlockconditionals")) {
      breakBlockConditionalsTimes.add(timeInSeconds);
    } else if (timeGroup.equalsIgnoreCase("leftClickConditionals")) {
      leftCLickConditionalsTimes.add(timeInSeconds);
    } else if (timeGroup.equalsIgnoreCase("changeEXP")) {
      changeEXPTimes.add(timeInSeconds);
    } else if (timeGroup.equalsIgnoreCase("flintFinder")) {
      flintFinderTimes.add(timeInSeconds);
    } else if (timeGroup.equalsIgnoreCase("diggingTreasureDrop")) {
      diggingTreasureDropTimes.add(timeInSeconds);
    } else if (timeGroup.equalsIgnoreCase("doubleDrop")) {
      doubleDropTimes.add(timeInSeconds);
    } else if (timeGroup.equalsIgnoreCase("logXP")) {
      logXPDropTimes.add(timeInSeconds);
    } else if (timeGroup.equalsIgnoreCase("logBook")) {
      logBookDropTimes.add(timeInSeconds);
    } else if (timeGroup.equalsIgnoreCase("leaves")) {
      leavesDropTimes.add(timeInSeconds);
    } else if (timeGroup.equalsIgnoreCase("woodcuttingHaste")) {
      timedHasteTimes.add(timeInSeconds);
    } else if (timeGroup.equalsIgnoreCase("miningTreasureDrop")) {
      miningTreasureDropTimes.add(timeInSeconds);
    } else if (timeGroup.equalsIgnoreCase("wastelessHaste")) {
      miningWastelessHasteTimes.add(timeInSeconds);
    } else if (timeGroup.equalsIgnoreCase("veinMiner")) {
      veinMinerTimes.add(timeInSeconds);
    } else if (timeGroup.equalsIgnoreCase("leafBlower")) {
      leafBlowerTimes.add(timeInSeconds);
    } else if (timeGroup.equalsIgnoreCase("blockFace")) {
      storeBlockFaceTimes.add(timeInSeconds);
    }
  }

  public Map<String, Double> getTimesStatistics(ArrayList<Double> times) {
    if (times.isEmpty()) {
      return null;
    }
    double sum = 0;
    double max = times.get(0);
    double min = times.get(0);
    for (double time : times) {
      sum += time;
      if (time > max) {
        max = time;
      } else if (time < min) {
        min = time;
      }
    }
    double average = sum / times.size();
    double sqAvgDiff = 0;
    for (double time : times) {
      sqAvgDiff += (time - average) * (time - average);
    }
    double var = sqAvgDiff / times.size();
    double stDev = Math.sqrt(var);
    Map<String, Double> data = new HashMap<>();
    data.put("total", Double.valueOf(times.size()));
    data.put("sum", roundThreePlaces(sum));
    data.put("max", roundThreePlaces(max));
    data.put("min", roundThreePlaces(min));
    data.put("avg", roundThreePlaces(average));
    data.put("stDev", roundThreePlaces(stDev));
    return data;

  }

  public double roundThreePlaces(double number) {
    return Math.round(number * 1000) / 1000.0;
  }

  public ArrayList<ArrayList<Double>> getAllData() {
    ArrayList<ArrayList<Double>> allData = new ArrayList<>();
    allData.add(breakBlockTrackedBlockCheckTimes);
    allData.add(breakBlockConditionalsTimes);
    allData.add(leftCLickConditionalsTimes);
    allData.add(changeEXPTimes);
    allData.add(flintFinderTimes);
    allData.add(diggingTreasureDropTimes);
    allData.add(doubleDropTimes);
    allData.add(logXPDropTimes);
    allData.add(logBookDropTimes);
    allData.add(leavesDropTimes);
    allData.add(timedHasteTimes);
    allData.add(miningTreasureDropTimes);
    allData.add(miningWastelessHasteTimes);
    allData.add(veinMinerTimes);
    allData.add(leafBlowerTimes);
    allData.add(storeBlockFaceTimes);
    return allData;
  }

  public void writeHeader(FileWriter fileWriter, int i) throws IOException {
    String horizontalLine =
        "---------------------------------------------------------------" + "\n";
    fileWriter.write(horizontalLine);
    switch (i) {
      case 0:
        fileWriter.write("Break Block Tracked Blocks Check" + "\n");
        break;
      case 1:
        fileWriter.write("Break Block Conditionals" + "\n");
        break;
      case 2:
        fileWriter.write("Left Click Conditionals" + "\n");
        break;
      case 3:
        fileWriter.write("Change EXP" + "\n");
        break;
      case 4:
        fileWriter.write("Flint Finder" + "\n");
        break;
      case 5:
        fileWriter.write("Digging Treasure Drop" + "\n");
        break;
      case 6:
        fileWriter.write("Double Drop" + "\n");
        break;
      case 7:
        fileWriter.write("Log XP Drop" + "\n");
        break;
      case 8:
        fileWriter.write("Log Book Drop" + "\n");
        break;
      case 9:
        fileWriter.write("Leaves Drop" + "\n");
        break;
      case 10:
        fileWriter.write("Woodcutting Timed Haste" + "\n");
        break;
      case 11:
        fileWriter.write("Mining Treasure Drop" + "\n");
        break;
      case 12:
        fileWriter.write("Mining Wasteless Haste" + "\n");
        break;
      case 13:
        fileWriter.write("Vein Miner" + "\n");
        break;
      case 14:
        fileWriter.write("Leaf Blower" + "\n");
        break;
      case 15:
        fileWriter.write("Store Block Face" + "\n");
        break;
      default:
        break;
    }
    fileWriter.write(horizontalLine);
  }

  public void writeBody(FileWriter fileWriter, ArrayList<Double> data) throws IOException {
    String horizontalBreak =
        "><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><" + "\n";
    Map<String, Double> statistics = getTimesStatistics(data);
    if (statistics != null) {
      fileWriter.write("Total # of Times:   " + ((int) Math.round(statistics.get("total"))) + "\n");
      fileWriter.write("Total Time Taken:   " + (statistics.get("sum")) + " s" + "\n");
      fileWriter.write("Maximum Time Taken: " + (statistics.get("max")) + " s" + "\n");
      fileWriter.write("Minimum Time Taken: " + (statistics.get("min")) + " s" + "\n");
      fileWriter.write("Average Time Taken: " + (statistics.get("avg")) + " s" + "\n");
      fileWriter.write("Standard Deviation: " + (statistics.get("stDev")) + " s" + "\n");
      fileWriter.write(horizontalBreak);
    }
    ConfigLoad configLoad = new ConfigLoad();
    if (configLoad.isVerboseRunTimeData()) {
      for (int i = 0; i < data.size(); i++) {
        fileWriter.write(i + ": " + data.get(i) + " s" + "\n");
      }
    }
    fileWriter.write("\n");
    fileWriter.write("\n");

  }
}
