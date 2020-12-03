package com.company;

import java.io.*;
import java.util.List;
import java.util.Scanner;

public class Main
{
    private static int A, B;
    private static BankaFigure banka = new BankaFigure();

    public static final String MAIN_FILE_NAME = "s_last_save.lab";
    public static final String FILES_EXTENSION = ".lab";

    public static void main(String[] args)
    {
        //do last save backup
        banka.doFileBackup(MAIN_FILE_NAME);
        System.out.println();

        //search for existing files
        System.out.println("Available files to load:");
        File[] saves = BankaFigure.getSavesList(".", FILES_EXTENSION);
        int counter = 0;
        for (File f : saves)
        {
            ++counter;
            System.out.println(counter + ". " + f.getName());
        }

        boolean flag = counter > 0;
        if(!flag)
        {
            flag = true;
            System.out.println("No suitable files found\n");
        }
        else
        {
            System.out.println("\nEnter a number of file to load. Enter '0' to skip.");
            int f = readInteger(0, counter);
            if (f != 0)
            {

                banka.deserializeFastJSON(saves[f - 1].getName());
                flag = false;
            }
            else
            {
                System.out.println("File loading skipped.");
                flag = true;
            }
        }

        if(flag)
        {
            //handle user input
            System.out.println("Enter B value");
            B = readInteger();
            System.out.println("Enter A value");
            A = readInteger();

            //fill storage with figures
            System.out.println("Filling lists with figures...");
            banka.fillListRandomly(Circle.class, B);
            banka.fillListRandomly(Cone.class, A);
        }

        //print figures info
        System.out.println("Circle list:");
        banka.printList(Circle.class);
        System.out.println("Cone list:");
        banka.printList(Cone.class);

        //do tasks
        doTask(Circle.class);
        doTask(Cone.class);

        //save data to file
        //storage.save(MAIN_FILE_NAME);
        if(flag) banka.serializeFastJSON(MAIN_FILE_NAME);
        System.out.println("Data saved to file '" + MAIN_FILE_NAME + "'");

        System.out.println("\nJSON serialization:");
        banka.serialize("json-" +MAIN_FILE_NAME);
        banka.deserialize("json-" +MAIN_FILE_NAME);
        System.out.println("Circle list:");
        banka.printList(Circle.class);
        System.out.println("Cone list:");
        banka.printList(Cone.class);
    }

    public static void doTask(Class<?> figureClass)
    {
        if(figureClass == Circle.class)
        {
            System.out.println("Processing the Circle...");
            List<Circle> list = banka.getFigureList(figureClass);
            if(list != null)
            {
                float averageCircle = 0;
                for (Circle tr : list)
                {
                    averageCircle += tr.getArea();
                }
                averageCircle /= list.size();
                System.out.println("Average square equals " + averageCircle);

                int lowerSquareValuesCount = 0;
                for (Circle tr : list)
                {
                    if (tr.getArea() < averageCircle) lowerSquareValuesCount++;
                }
                System.out.println("The amount of Circle with lower square values equals " + lowerSquareValuesCount + "\n");
            }
        }
        else if(figureClass == Cone.class)
        {
            System.out.println("Processing the Cone...");
            List<Cone> list = banka.getFigureList(figureClass);
            if(list != null)
            {
                double maxConeValume = 0;
                int coneNumber = 1;
                for(Cone cone : list){
                    if(maxConeValume < cone.GetVolume()) {
                        maxConeValume = cone.GetVolume();
                        coneNumber++;
                    }
                }
                System.out.println("Cone numbered " + coneNumber + " has the largest area: " + maxConeValume);
            }
        }
        else
        {
            System.out.println("Error! No task for class <" + figureClass.toString() + ">");
        }
    }

    public static int readInteger()
    {
        return readInteger(1, 9999);
    }

    public static int readInteger(int minValue, int maxValue)
    {
        Scanner input = new Scanner(System.in);
        while(true)
        {
            try
            {
                int result = Integer.parseInt(input.next());
                if(result >= minValue)
                {
                    if(result <= maxValue) return result;
                    else
                    {
                        System.out.println("Value must be less than " + (maxValue + 1));
                    }
                }
                else
                {
                    System.out.println("Value must be greater than " + (minValue - 1));
                }
            }
            catch (Exception e)
            {
                System.out.println("Enter a number, please");
            }
        }
    }
}
