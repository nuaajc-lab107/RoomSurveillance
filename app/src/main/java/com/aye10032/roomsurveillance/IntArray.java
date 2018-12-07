package com.aye10032.roomsurveillance;

import java.util.Arrays;

public class IntArray {

    private int[] elements;

    public IntArray(){
        elements = new int[0];
    }

    public int size(){
        return elements.length;
    }

    public void add(int element){
        int[] newArr = new int[elements.length + 1];

        for (int i = 0; i < elements.length; i++) {
            newArr[i] = elements[i];
        }
        newArr[elements.length] = element;

        elements = newArr;
    }

    public void show(){
        System.out.println(Arrays.toString(elements));
    }

    public void delete(int index){
        if (index < 0 || index > elements.length-1){
            throw new RuntimeException("index out of bounds");
        }else {
            int[] newArr = new int[elements.length - 1];
            for (int i = 0; i < newArr.length; i++) {
                if (i < index){
                    newArr[i] = elements[i];
                }else {
                    newArr[i] = elements[i + 1];
                }
            }
            elements = newArr;
        }
    }

    public int getelement(int index){
        if (index < 0 || index > elements.length - 1){
            throw new RuntimeException("Invalid index" + index +"!, end" + (elements.length - 1));
        }else
            return elements[index];
    }

    public int search(int target){
        int index;
        for (int i = 0; i < elements.length; i++) {
            if (elements[i] == target){
                index = i;
                return index;
            }
        }
        return -1;
    }

    public void insert(int index, int element){
        int[] newArr = new int[elements.length + 1];

        for (int i = 0; i < newArr.length; i++) {
            if (i < index){
                newArr[i] = elements[i];
            }else if (i == index){
                newArr[i] = element;
            }else if (i > index){
                newArr[i] = elements[i - 1];
            }
        }

        elements = newArr;
    }

    public void replace(int index, int element){
        elements[index] = element;
    }

    public void empty(){
        int[] newArr = new int[0];
        elements = newArr;
    }

}
