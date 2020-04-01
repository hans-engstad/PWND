package com.mygdx.game.models;

import java.util.HashMap;
import java.util.Map;

public class Lane {

    private Cell[] cells;
    private String key;

    public Lane (String key){
        this.key = key;

        cells = new Cell[] {
                new Cell(key + "-cell0"),
                new Cell(key + "-cell1"),
                new Cell(key + "-cell2"),
                new Cell(key + "-cell3"),
                new Cell(key + "-cell4"),
                new Cell(key + "-cell5")
        };
    }

    public Lane(Map<String, Object> data){

        this.key = (String) data.get("key");

        if (data.containsKey("cells")){
            this.cells = new Cell[((Map) data.get("cells")).size()];

            int i = 0;
            for (Object cellData : ((Map) data.get("cells")).values()){
                this.cells[i] = new Cell((Map) cellData);
                i++;
            }
        }
    }

    public Cell getCell(int index){
        return cells[index];
    }

    public Cell[] getCells(){
        return cells;
    }

    public String getKey(){
        return this.key;
    }

    public Map serialize(){
        Map<String, Object> lane = new HashMap<>();

        Map<String, Object> cellsMap = new HashMap<>();
        for (Cell c: cells) {
            cellsMap.put(c.getKey(), c.serialize());
        }

        lane.put("key", this.key);
        lane.put("cells", cellsMap);
        return lane;
    }

}
