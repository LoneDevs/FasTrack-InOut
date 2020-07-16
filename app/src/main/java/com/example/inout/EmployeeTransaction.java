package com.example.inout;

public class EmployeeTransaction {
    private double userSalary,withrawAmount;

    EmployeeTransaction(double us,double wa){
        userSalary=us;
        withrawAmount=wa;
    }
    protected String startTransaction(){

        if (userSalary>=withrawAmount){
            userSalary = userSalary - withrawAmount;
            return "valid";
        }else if (userSalary==0){
            return "Zero Balance";
        }else{
            return "Insufficient Balance !" ;
        }
    }

    protected double getUserSalary(){
        return userSalary;
    }

}
