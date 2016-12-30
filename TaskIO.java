package ua.sumdu.j2se.bohdanbutko.tasks;

import javax.swing.text.html.HTMLDocument;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by Bogdan Butko on 12/23/2016.
 */
public class TaskIO {

    public static void write(TaskList tasks, OutputStream out){

        if(tasks.size() == 0 || out == null){
            return;
        }

        DataOutputStream dataOut = new DataOutputStream(out);
        Iterator i = tasks.iterator();

        try{
            dataOut.writeInt(tasks.size());
        }
        catch (IOException e){
            e.printStackTrace();
        }

        try{

            while(i.hasNext()){

                Task task = (Task) i.next();

                dataOut.writeInt(task.getTitle().length());
                dataOut.writeUTF(task.getTitle());
                dataOut.writeBoolean(task.isActive());
                dataOut.writeLong(task.getRepeatInterval().getTime());

                if(task.isRepeated()){

                    dataOut.writeLong(task.getStartTime().getTime());
                    dataOut.writeLong(task.getEndTime().getTime());
                }
                else{

                    dataOut.writeLong(task.getTime().getTime());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {

            if(dataOut != null){

                try {
                    dataOut.flush();
                    dataOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void write(TaskList tasks, Writer out) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd H:m:S.sss");
        BufferedWriter writer = new BufferedWriter(out);

        try{

            writer.write("" + tasks.size());
            writer.write(System.getProperty("line.separator"));

            Iterator i = tasks.iterator();

            while(i.hasNext()){

                Task task = (Task) i.next();
                String result;

                if(task.isRepeated()){

                    result = "\"" + task.getTitle() + "\"" + " from [" + format.format(task.getStartTime()) + "] to [" + format.format(task.getEndTime()) + "] every [" + makeRepeatIntervalFormat(task.getRepeatInterval().getTime()) + "]" + ((task.isActive()) ? "" : " inactive") + (i.hasNext() ? ";" : ".");
                }
                else {
                    result = "\"" + task.getTitle() + "\"" + " at [" + format.format(task.getTime()) + "]" + ((task.isActive()) ? "" : " inactive") + (i.hasNext() ? ";" : ".");
                }

                writer.write(result);
                writer.write(System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(writer != null){
                try {
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void read(TaskList tasks, InputStream in){

        DataInputStream dataIn = new DataInputStream(in);

        try{

            int totalLength = dataIn.readInt();

            for(int i = 0; i < totalLength; i++){

                int titleLength = dataIn.readInt();
                String title = dataIn.readUTF();
                Boolean isActive = dataIn.readBoolean();
                Date repeatInterval = new Date(dataIn.readLong());

                Task task;

                if(repeatInterval.getTime() > 0){

                    Date startTime = new Date(dataIn.readLong());
                    Date endTime = new Date(dataIn.readLong());

                    task = new Task(title, startTime, endTime, repeatInterval);
                }
                else{

                    task = new Task(title, new Date(dataIn.readLong()));
                }

                tasks.add(task);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(dataIn != null){

                try {
                    dataIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void read(TaskList tasks, Reader in){

        BufferedReader reader = new BufferedReader(in);

        try{

            int size = Integer.parseInt(reader.readLine());

            SimpleDateFormat dateFormat = new SimpleDateFormat();
            dateFormat.applyPattern("yyyy-MM-dd H:m:S.sss");

            for(int i = 0; i < size; i++){

                String stringTask = reader.readLine();
                String[] spliteTitle = stringTask.split("\"");
                String title = spliteTitle[1];
                String[] splitTime = spliteTitle[2].split("]");

                Task task = null;

                if(splitTime.length == 4){

                    String[] splitStart = splitTime[0].split("\\[");
                    Date startTime = dateFormat.parse(splitStart[1]);

                    String[] splitEnd = splitTime[1].split("\\[");
                    Date endTime = dateFormat.parse(splitEnd[1]);

                    String[] splitRepeatInterval = splitTime[2].split("\\[");
                    splitRepeatInterval = splitRepeatInterval[1].split(" ");

                    int days = Integer.parseInt(splitRepeatInterval[0]);
                    int hours = Integer.parseInt(splitRepeatInterval[2]);
                    int minutes = Integer.parseInt(splitRepeatInterval[4]);
                    int seconds = Integer.parseInt(splitRepeatInterval[6]);

                    int repeatInterval = seconds + minutes * 60 + hours * 60 * 60 + days * 24 * 60 * 60;

                    Boolean isActive = !splitTime[3].contains("inactive");

                    task = new Task(title, startTime, endTime, new Date(repeatInterval));
                    task.setActive(isActive);
                }
                else{

                    String[] splitStart = splitTime[0].split("\\[");
                    Date startTime = dateFormat.parse(splitStart[1]);
                    Boolean isActive = !splitTime[1].contains("inactive");
                    task = new Task(title, startTime);
                    task.setActive(isActive);
                }

                tasks.add(task);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void writeBinary(TaskList tasks, File file){

        try {
            write(tasks, new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void writeText(TaskList tasks, File file){

        try {
            write(tasks, new FileWriter(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readBinary(TaskList tasks, File file){

        try {
            read(tasks, new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void readText(TaskList tasks, File file){

        try {
            read(tasks, new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String makeRepeatIntervalFormat(long repeatInterval){

        long days = (repeatInterval / 86400);
        repeatInterval = repeatInterval - (days * 86400);

        long hours = (repeatInterval / 3600);
        repeatInterval = repeatInterval - (hours * 3600);

        long minutes = (repeatInterval / 60);
        long seconds = repeatInterval - (minutes * 60);

        return "" + (days + ((days > 1) ? " days " : " day ")) + (hours + ((hours > 1) ? " hours " : " hour ")) + (minutes + ((minutes > 1) ? " minutes " : " minute ")) + (seconds + ((seconds > 1) ? " seconds" : " second"));
    }
}
