package com.uis.examregistration.repository;

/**
 * Singleton design pattern (as shown in the lecture / pizza example).
 * Ensures a single shared point of access for a logical DB connection holder.
 */
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
