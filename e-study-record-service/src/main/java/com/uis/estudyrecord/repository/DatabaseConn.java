package com.uis.estudyrecord.repository;

/** Singleton design pattern (as in the lecture / pizza example). */
public class DatabaseConn {
    private String conn = "Connecting to DB...";
    private static DatabaseConn instance;

    private DatabaseConn() {
    }

    public static DatabaseConn getInstance() {
        if (instance == null) {
            instance = new DatabaseConn();
        }
        return instance;
    }

    public String getConn() {
        return conn;
    }
}
