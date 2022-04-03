package com.evans.swinspector;

public class AccountMonster {
    private int mid;
    private int master_id;
    private String unit_name;

    public AccountMonster(int master_id, String unit_name) {
        this.master_id = master_id;
        this.unit_name = unit_name;
    }

    public int getMaster_id() {
        return master_id;
    }

    public void setMaster_id(int master_id) {
        this.master_id = master_id;
    }
}
