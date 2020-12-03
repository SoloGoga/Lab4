package com.company;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class BankaFigure
{
    private List<Circle> circleList = new ArrayList<>();
    private List<Cone> coneList = new ArrayList<>();

    private static Random random = new Random();

    public void addFigure(Circle circle)
    {
        circleList.add(circle);
    }

    public void addFigure(Cone cone)
    {
        coneList.add(cone);
    }

    public List getFigureList(Class<?> figureClass)
    {
        if(figureClass == Circle.class)
        {
            return circleList;
        }
        else if(figureClass == Cone.class)
        {
            return coneList;
        }
        else
        {
            System.out.println("Error! Figure storage doesn't contains a class (" + figureClass.toString() + ") !");
            return null;
        }
    }

    public void fillListRandomly(Class<?> figureClass, int count)
    {
        if(figureClass == Circle.class)
        {
            for(int i = 0; i < count; i++)
            {
                Circle tr = getRandomTriangle();
                addFigure(tr);
            }
        }
        else if(figureClass == Cone.class)
        {
            for(int i = 0; i < count; i++)
            {
                Cone pr = getRandomPrism();
                addFigure(pr);
            }
        }
        else
        {
            System.out.println("Error! Figure storage doesn't contains a class (" + figureClass.toString() + ") !");
        }
    }

    public void printList(Class<?> figureClass)
    {
        List list = getFigureList(figureClass);
        if(list == null || list.isEmpty() || !(list.get(0) instanceof IFigure))
        {
            System.out.println("Error! Can't print a figures list!");
        }
        else
        {
            int counter = 0;
            for (Object f : list)
            {
                counter++;
                System.out.println((counter) + ".\n" + ((IFigure)f).getInfo() + "\n");
            }
        }
    }

    public void clear()
    {
        circleList.clear();
        coneList.clear();
    }

    public static Circle getRandomTriangle()
    {
        return new Circle((float)random.nextInt(20) + 1);
    }

    public static Cone getRandomPrism()
    {
        return new Cone((float)random.nextInt(20) + 1, (float)random.nextInt(20) + 1);
    }

    public void serialize(String fileName)
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(fos);

            out.writeObject(circleList);
            out.writeChars(";");
            out.writeObject(coneList);

            out.close();
            fos.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void deserialize(String fileName)
    {
        try
        {
            FileInputStream fis = new FileInputStream(fileName);
            ObjectInputStream input = new ObjectInputStream(fis);

            this.circleList = (ArrayList<Circle>)input.readObject();
            input.readChar();
            this.coneList = (ArrayList<Cone>)input.readObject();

            input.close();
            fis.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        catch(ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public void serializeFastJSON(String fileName)
    {
        try
        {
            FileWriter fw = new FileWriter(fileName);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(JSON.toJSONString(this.circleList) + "\n");
            bw.write(JSON.toJSONString(this.coneList));
            bw.close();
            fw.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void deserializeFastJSON(String fileName)
    {
        try
        {
            Scanner scanner = new Scanner(new FileReader(fileName));
            this.clear();
            ArrayList<JSONObject> JSONlist = JSON.parseObject(scanner.nextLine(), ArrayList.class);
            for (JSONObject obj : JSONlist)
            {
                this.circleList.add(new Circle(obj.getIntValue("Radius")));
            }
            JSONlist = JSON.parseObject(scanner.nextLine(), ArrayList.class);
            for (JSONObject obj : JSONlist)
            {
                this.coneList.add(new Cone(obj.getIntValue("Radius"), obj.getIntValue("Height")));
            }
            scanner.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void doFileBackup(String mainFileName)
    {
        File f = new File(mainFileName);
        if(!f.exists())
        {
            return;
        }

        String backupName = "save-";
        backupName += new SimpleDateFormat("dd.MM.yyyy-HH.mm.ss").format(new Date());
        backupName += Main.FILES_EXTENSION;

        deserializeFastJSON(mainFileName);
        serializeFastJSON(backupName);
        System.out.println("Created last save backup file '" + backupName + "'");
        this.clear();
    }

    public static File[] getSavesList(String directory, String extension)
    {
        File dir = new File(directory);
        File [] files = dir.listFiles(new FilenameFilter()
        {
            @Override
            public boolean accept(File dir, String name)
            {
                return name.endsWith(extension);
            }
        });
        return files;
    }
}
