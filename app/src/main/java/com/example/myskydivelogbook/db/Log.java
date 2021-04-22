package com.example.myskydivelogbook.db;


import androidx.room.Entity;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

@Entity(tableName="Logs")
public class Log {

        public Log(int jumpNum, String date, String location, String aircraft, String equipment, String altitude, String delay,
                   String wind, String target, String signature, String notes) {

            this.jumpNum = jumpNum;
            this.date = date;
            this.location = location;
            this.aircraft = aircraft;
            this.equipment = equipment;
            this.altitude = altitude;
            this.delay = delay;
            this.wind = wind;
            this.target = target;
            this.signature = signature;
            this.notes = notes;
        }

        @PrimaryKey(autoGenerate = false)
        @ColumnInfo(name = "jumpNum")
        public int jumpNum;

        @ColumnInfo(name = "date")
        public String date;

        @ColumnInfo(name = "location")
        public String location;

        @ColumnInfo(name = "aircraft")
        public String aircraft;

        @ColumnInfo(name = "equipment")
        public String equipment;

        @ColumnInfo(name = "altitude")
        public String altitude;

        @ColumnInfo(name = "delay")
        public String delay;

        @ColumnInfo(name = "wind")
        public String wind;

        @ColumnInfo(name = "target")
        public String target;

        @ColumnInfo(name = "signature")
        public String signature;

        @ColumnInfo(name = "notes")
        public String notes;
}
