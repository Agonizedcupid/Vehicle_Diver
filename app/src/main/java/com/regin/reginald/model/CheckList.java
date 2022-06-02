package com.regin.reginald.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;



@DatabaseTable(tableName = "CheckLists")
public class CheckList {

    @DatabaseField(id = true, columnName = "id")
    //@MapFrom("id")
    private int id;

    @DatabaseField(columnName = "checkListId")
    //@MapFrom("checkListId")
    private int checkListId;

    @DatabaseField(columnName = "checkListMessage")
    //@MapFrom("checkListMessage")
    private String checkListMessage;

    public int getId(){ return id; }
    public int getcheckListId(){ return checkListId; }

    public void setId(int id) {
        this.id = id;
    }
    public void setcheckListId(int checkListId) {
        this.checkListId = checkListId;
    }
    public void setcheckListMessage(String checkListMessage) {
        this.checkListMessage = checkListMessage;
    }
    public String getcheckListMessage() {
        return checkListMessage;
    }
}
