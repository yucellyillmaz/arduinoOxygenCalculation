package com.yucellyillmaz.proje;

public class Feed {
    private String Id;
    private String Field;
    private String Create_Time;

    public Feed() {
    }

    public Feed(String id, String field, String create_Time) {
        Id = id;
        Field = field;
        Create_Time = create_Time;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getField() {
        return Field;
    }

    public void setField(String field) {
        Field = field;
    }

    public String getCreate_Time() {
        return Create_Time;
    }

    public void setCreate_Time(String create_Time) {
        Create_Time = create_Time;
    }



    @Override
    public String toString() {
        return "Id : " + Id + " Field : " + Field + " Create Time : " + Create_Time;
    }
}
