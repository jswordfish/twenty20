package com.twenty20.jsf.managedBeans;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.RequestScoped;
import javax.inject.Named;

@Named(value="me")
@RequestScoped
public class TestCdi implements Serializable {
  private String name;
  
  public String getName() {
    return name;
  }
 
  public void setName(String name) {
    this.name = name;
  }
 
  @PostConstruct
  public void init() {
    name = "Jo Desmet";
  }
}