import java.io.*;
import java.util.ArrayList;

public class Copy {
    public static void main(String[] args) {
        try (FileInputStream fis = new FileInputStream("/a.tet");
             BufferedInputStream bis = new BufferedInputStream(fis);
             FileOutputStream fos = new FileOutputStream("/a.txt");
             BufferedOutputStream bos = new BufferedOutputStream(fos)){
            int size = 0;
            byte[] b = new byte[1024];
            while((size = bis.read(b,0,b.length))!=-1){
                bos.write(b,0,size);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try(FileInputStream fis = new FileInputStream("");
            BufferedInputStream bis = new BufferedInputStream(fis);
            FileOutputStream fos = new FileOutputStream("");
            BufferedOutputStream bos = new BufferedOutputStream(fos)){
            int size = 0;
            byte[] b = new byte[1024];
            while ((size = bis.read(b,0,b.length))!=-1){
                bos.write(b,0,size);
            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getAllFiles(File file, ArrayList<String> res){
        File[] files= file.listFiles();
        if(files ==null){
            return;
        }
        for(File f:files){
            if(f.isDirectory()){
                res.add(f.getPath());
                getAllFiles(f,res);
            }else{
                res.add(f.getPath());
            }
        }
    }
}
