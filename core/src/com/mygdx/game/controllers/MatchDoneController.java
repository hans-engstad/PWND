package com.mygdx.game.controllers;

public class MatchDoneController {

    private Boolean masterWon;
    private Boolean slaveWon;

    public enum Status {
        WON,
        LOST,
        TIE
    }

    private Status status;

    public MatchDoneController(Status status){
        this.status = status;
    }

    public String getTitle (){
        if (status == Status.TIE){
            return "TIE!";
        }
        else if (status == Status.WON){
            return "YOU WON!!!";
        }
        else {
            return "YOU LOST :(";
        }
    }

}
