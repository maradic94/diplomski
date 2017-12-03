/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Korisnik
 */
package controllers;

import entity.ClassRoom;
import java.util.LinkedList;


public class ClassroomReservationCombination {

    private LinkedList<ClassRoom> classrooms;
    private int sumCapacity;
    private String combinationDescription = "";

    public void setListClassRooms(LinkedList<ClassRoom> clsRooms) {
        classrooms = new LinkedList<ClassRoom>();
        int size = clsRooms.size();
        for (int i = 0; i < size; i++) {
            ClassRoom c = clsRooms.get(i);
            classrooms.add(c);
            sumCapacity += c.getCapacity();
            combinationDescription += c.getClassRoomName();
            if (i != size - 1) {
                combinationDescription += "  -  ";
            }

        }
    }

    public void dump() {
        System.out.println("Capacity = " + sumCapacity + "sale : " + combinationDescription);
        System.out.println("-----------------------------");
    }

    public LinkedList<ClassRoom> getClassrooms() {
        return classrooms;
    }

    public void setClassrooms(LinkedList<ClassRoom> classrooms) {
        this.classrooms = classrooms;
    }

    public int getSumCapacity() {
        return sumCapacity;
    }

    public void setSumCapacity(int sumCapacity) {
        this.sumCapacity = sumCapacity;
    }

    public String getCombinationDescription() {
        return combinationDescription;
    }

    public void setCombinationDescription(String combinationDescription) {
        this.combinationDescription = combinationDescription;
    }

}
