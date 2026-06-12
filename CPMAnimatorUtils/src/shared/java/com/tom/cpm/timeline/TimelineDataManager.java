package com.tom.cpm.timeline;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class TimelineDataManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    
    public static class AnimData {
        public Map<Integer, Integer> colors = new HashMap<>();
    }

    private static Map<String, AnimData> currentProjectData = new HashMap<>();
    private static String lastLoadedPath = "";

    public static void loadForProject(String projectPath) {
        if (projectPath == null || projectPath.equals("untitled")) {
            currentProjectData = new HashMap<>();
            return;
        }
        if (projectPath.equals(lastLoadedPath)) return;
        
        File sidecar = new File(projectPath + ".timeline");
        if (!sidecar.exists()) {
            currentProjectData = new HashMap<>();
            lastLoadedPath = projectPath;
            return;
        }

        try (FileReader reader = new FileReader(sidecar)) {
            currentProjectData = GSON.fromJson(reader, new TypeToken<Map<String, AnimData>>(){}.getType());
            if (currentProjectData == null) currentProjectData = new HashMap<>();
            lastLoadedPath = projectPath;
        } catch (Exception e) {
            currentProjectData = new HashMap<>();
        }
    }

    public static void saveForProject(String projectPath) {
        if (projectPath == null || projectPath.equals("untitled")) return;
        File sidecar = new File(projectPath + ".timeline");
        try (FileWriter writer = new FileWriter(sidecar)) {
            GSON.toJson(currentProjectData, writer);
        } catch (Exception ignored) {}
    }

    public static void setFrameColor(String projectPath, String animName, int frameIdx, int color) {
        loadForProject(projectPath);
        currentProjectData.computeIfAbsent(animName, k -> new AnimData()).colors.put(frameIdx, color);
        saveForProject(projectPath);
    }

    public static Integer getFrameColor(String projectPath, String animName, int frameIdx) {
        loadForProject(projectPath);
        AnimData anim = currentProjectData.get(animName);
        return (anim != null) ? anim.colors.get(frameIdx) : null;
    }
}
