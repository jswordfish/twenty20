package com.twenty20.jsf.managedBeans;

public class Tab {
	private String title;
    private String content;

    public Tab() {
    }
    
    public Tab(String title) {
        this.title = title;
    }

    public Tab(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
}
