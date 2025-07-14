package com.github.souqly.souqly.Exception;

public abstract class DatabaseException extends RuntimeException {
	private String tableName;

	public DatabaseException(String tableName) {
		super("exception from table " + tableName);
		this.tableName = tableName;
	}

}
