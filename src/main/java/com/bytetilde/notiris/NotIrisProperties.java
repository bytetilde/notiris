package com.bytetilde.notiris;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public final class NotIrisProperties {
  public static final String FILE_NAME = "notiris.properties";
  public static final String MESH = "notiris.mesh";
  public static final boolean MESH_DEFAULT = false;
  public static final String MESH_WINDOW_WIDTH = "notiris.mesh.window.width";
  public static final int MESH_WINDOW_WIDTH_DEFAULT = 7;
  public static final String MESH_WINDOW_HEIGHT = "notiris.mesh.window.height";
  public static final int MESH_WINDOW_HEIGHT_DEFAULT = 5;
  public static final String MESH_WINDOW_DEPTH = "notiris.mesh.window.depth";
  public static final int MESH_WINDOW_DEPTH_DEFAULT = 7;
  private static volatile Map<String, String> entries = Map.of();
  private NotIrisProperties() {}
  public static void load(Path shadersDir) {
    String contents;
    try {
      contents = Files.readString(shadersDir.resolve(FILE_NAME), StandardCharsets.ISO_8859_1);
    } catch(NoSuchFileException e) {
      entries = Map.of();
      NotIris.LOGGER.debug("no notiris.properties in {}", shadersDir);
      return;
    } catch(IOException e) {
      entries = Map.of();
      NotIris.LOGGER.warn("could not read notiris.properties: {}", e.getMessage());
      return;
    }
    Properties props = new Properties();
    try {
      props.load(new StringReader(contents));
    } catch(IOException | IllegalArgumentException e) {
      entries = Map.of();
      NotIris.LOGGER.warn("could not parse notiris.properties: {}", e.getMessage());
      return;
    }
    Map<String, String> next = new LinkedHashMap<>();
    for(String name : props.stringPropertyNames()) next.put(name, props.getProperty(name));
    entries = Collections.unmodifiableMap(next);
    NotIris.LOGGER.info("loaded {} entries from notiris.properties", next.size());
  }
  public static void clear() {
    entries = Map.of();
  }
  public static boolean has(String key) {
    return entries.containsKey(key);
  }
  public static String get(String key) {
    return entries.get(key);
  }
  public static String get(String key, String fallback) {
    return entries.getOrDefault(key, fallback);
  }
  public static boolean getBoolean(String key, boolean fallback) {
    String value = entries.get(key);
    if(value == null) return fallback;
    if(value.equalsIgnoreCase("true")) return true;
    if(value.equalsIgnoreCase("false")) return false;
    NotIris.LOGGER.warn("invalid boolean for {}: {}", key, value);
    return fallback;
  }
  public static int getInt(String key, int fallback) {
    String value = entries.get(key);
    if(value == null) return fallback;
    try {
      return Integer.parseInt(value);
    } catch(NumberFormatException e) {
      NotIris.LOGGER.warn("invalid int for {}: {}", key, value);
      return fallback;
    }
  }
  public static float getFloat(String key, float fallback) {
    String value = entries.get(key);
    if(value == null) return fallback;
    try {
      return Float.parseFloat(value);
    } catch(NumberFormatException e) {
      NotIris.LOGGER.warn("invalid float for {}: {}", key, value);
      return fallback;
    }
  }
  public static boolean mesh() {
    return getBoolean(MESH, MESH_DEFAULT);
  }
  public static int meshWindowWidth() {
    return getInt(MESH_WINDOW_WIDTH, MESH_WINDOW_WIDTH_DEFAULT);
  }
  public static int meshWindowHeight() {
    return getInt(MESH_WINDOW_HEIGHT, MESH_WINDOW_HEIGHT_DEFAULT);
  }
  public static int meshWindowDepth() {
    return getInt(MESH_WINDOW_DEPTH, MESH_WINDOW_DEPTH_DEFAULT);
  }
  public static Set<String> keys() {
    return entries.keySet();
  }
  public static int size() {
    return entries.size();
  }
  public static boolean isEmpty() {
    return entries.isEmpty();
  }
}
