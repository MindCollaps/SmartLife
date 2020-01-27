package mindcollaps.utils;

import mindcollaps.engines.Engine;

import java.io.*;

public class FileUtils {

    Engine engine;

    public FileUtils(Engine engine) {
        this.engine = engine;
    }

    public String home = System.getProperty("user.dir");

    public String getHome() {
        return home + "/botmanagerhome";
    }

    public Object loadObject(String path) throws Exception {
        String filePath = new File(path).getAbsolutePath();
        File file = new File(filePath);
        System.out.println("\n[File loader] Loading Object Flile: " + filePath);
        FileInputStream stream = null;
        ObjectInputStream objStream = null;
        Object obj = null;

        if (!file.exists()) {
            System.out.println("The File was never created!");
            throw new Exception("File was never created!");
        }


        stream = new FileInputStream(file);


        objStream = new ObjectInputStream(stream);

        obj = objStream.readObject();

        objStream.close();

        stream.close();


        return obj;
    }

    public void saveOject(String path, Object obj) throws Exception {
        String filePath = new File(path).getAbsolutePath();
        File file = new File(filePath);
        System.out.println("\n[File loader] Save Object File: " + filePath);
        FileOutputStream stream = null;
        ObjectOutputStream objStream = null;

        createFileRootAndFile(file);


        stream = new FileOutputStream(file);


        objStream = new ObjectOutputStream(stream);

        objStream.writeObject(obj);
        objStream.flush();

        objStream.close();

        stream.close();

    }


    private void createFileRootAndFile(File file) {
        String pas = file.getAbsolutePath().replace("\\", "/");
        String[] path = pas.split("/");
        String pat = path[0];
        for (int i = 1; i < path.length - 1; i++) {
            pat = pat + "/" + path[i];
        }
        File dir = new File(pat);
        if (!dir.mkdirs()) {
            try {
                dir.mkdirs();
            } catch (Exception e) {
            }
        }
        createFiles(file);
    }

    private void createFiles(File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
            }
        }
    }
}
