package com.datastax.demo.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** utility class to parse files. */
public class FileUtils {

  /** Hide default. */
  private FileUtils() {}

  /** Load file content as a String. */
  public static String readFileIntoString(String filename) {
    try (Scanner s = new Scanner(new File(filename))) {
      return s.useDelimiter("\\Z").next();
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Cannot find the file", e);
    }
  }

  /** Load each line as a row. */
  public static List<String> readFileIntoList(String filename) {
    try (Stream<String> lines = Files.lines(Paths.get(filename))) {
      return lines.collect(Collectors.toList());
    } catch (IOException e1) {
      throw new IllegalArgumentException("Cannot read file", e1);
    }
  }
}
