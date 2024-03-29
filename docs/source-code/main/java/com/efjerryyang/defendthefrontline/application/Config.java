package com.efjerryyang.defendthefrontline.application;

public class Config {
    public final static double BOMB_PROP_GENERATION_SIMPLE = 0.33;
    public final static double BLOOD_PROP_GENERATION_SIMPLE = 0.33;
    public final static double BULLET_PROP_GENERATION_SIMPLE = 0.33;
    public final static double BOMB_PROP_GENERATION_MEDIUM = 0.3;
    public final static double BLOOD_PROP_GENERATION_MEDIUM = 0.3;
    public final static double BULLET_PROP_GENERATION_MEDIUM = 0.3;
    public final static double BOMB_PROP_GENERATION_DIFFICULT = 0.2;
    public final static double BLOOD_PROP_GENERATION_DIFFICULT = 0.2;
    public final static double BULLET_PROP_GENERATION_DIFFICULT = 0.3;
    public final static String host = "146.190.48.125";
    public final static int port = 4467;
    public static int screenWidth;
    public static int screenHeight;

    public static boolean online = false;
    private static int gameLevel = 1;
    private static boolean enableAudio = true;
    private static int score = 0;
    private static int propValidMaxTime = (int) (2000 / (5 + gameLevel));
    private static boolean gameOver = false;

    public static int getGameLevel() {
        return gameLevel;
    }

    public static void setGameLevel(int gameLevel) {
        Config.gameLevel = gameLevel;
    }

    public static boolean isEnableAudio() {
        return enableAudio;
    }

    public static int getScore() {
        return score;
    }

    public static void setScore(int score) {
        Config.score = score;
    }

    public static boolean getEnableAudio() {
        return Config.enableAudio;
    }

    public static void setEnableAudio(boolean enableAudio) {
        Config.enableAudio = enableAudio;
    }

    public static int getPropValidMaxTime() {
        return propValidMaxTime;
    }

    public static void setPropValidMaxTime(double level) {
        Config.propValidMaxTime = (int) (2000 / (5 + level));
    }

    public static void setGameOver(boolean gameOver) {
        Config.gameOver = gameOver;
    }

    public static boolean getGameOver() {
        return gameOver;
    }
}
