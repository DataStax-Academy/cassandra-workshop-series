package com.datastax.workshop;

public class AstraConnectionInfo {

  private String secureBundleFilename;
  private String userName;
  private String clearPwd;
  private String keyspaceName;

  public AstraConnectionInfo(
      String secureBundleZipfile,
      String userName,
      String pwd,
      String keyspace
  ) {
    this.secureBundleFilename = secureBundleZipfile;
    this.userName = userName;
    this.clearPwd = pwd;
    this.keyspaceName = keyspace;
  }
  
  public String getSecureBundleFilename() {
    return this.secureBundleFilename;
  }

  public String getUserName() {
    return this.userName;
  }

  public String getPassword() {
    return this.clearPwd;
  }

  public String getKeyspace() {
    return this.keyspaceName;
  }
}