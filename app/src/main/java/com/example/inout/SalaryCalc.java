package com.example.inout;
/*
 * SalaryCalc.java
 *
 * Copyright 2020 Ganesan Koundappan <ganesankoundappan@viruchith.local>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 *
 *
 */

public class SalaryCalc{

    private int WAGE_PER_HOUR = 200;
    private double InTime,OutTime;
    private double salary = 0;

    protected double calculateSalary(){
        salary = (OutTime-InTime)*WAGE_PER_HOUR;

        return Math.round(salary);


    }

    protected String validateInterval(float hIn,float mIn,float hOut,float mOut){
        InTime = hIn+(mIn/60);
        OutTime = hOut+(mOut/60);

        System.out.println("In:"+InTime);
        System.out.println("Ou:"+OutTime);


        if(OutTime-InTime>0.5){

            return "valid";

        }else{

            return "Invalid Time Interval chosen ! ";

        }

    }

}
