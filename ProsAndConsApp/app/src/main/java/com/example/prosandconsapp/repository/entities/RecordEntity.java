package com.example.prosandconsapp.repository.entities;

import java.util.Date;

public class RecordEntity {
    public long id;
    public Date time;
    public boolean positive;
    public long group_id;

    public RecordEntity(boolean positive, long group_id){
        this.positive = positive;
        this.group_id = group_id;
    }

   public RecordEntity(long id, Date time, boolean positive, long group_id){
        this(positive, group_id);
        this.id = id;
        this.time = time;
    }
}
