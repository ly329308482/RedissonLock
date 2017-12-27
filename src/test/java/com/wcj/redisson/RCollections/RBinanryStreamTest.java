package com.wcj.redisson.RCollections;

import com.wcj.redisson.lock.manager.RedissionClientManager;
import com.wcj.redisson.lock.util.PrintUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RBinaryStream;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;

import java.io.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by chengjie.wang on 2017/12/26.
 */

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class RBinanryStreamTest {

    @Test
    public void streamPutTest(){
        RedissonClient redissionClient = RedissionClientManager.getRedissionClient();
        RBinaryStream stream = redissionClient.getBinaryStream("ownerStream");
        File file = new File("C:\\Users\\chengjie.wang\\Desktop\\模板\\自营-费用维护结算明细模板1.csv");
        byte[] buffer = null;
        FileInputStream fileInputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int line;
            while((line = fileInputStream.read()) != -1){
                byteArrayOutputStream.write(b,0,line);
            }
            buffer = byteArrayOutputStream.toByteArray();
            stream.set(buffer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fileInputStream) {
                    fileInputStream.close();
                }
                if (null != byteArrayOutputStream) {
                    byteArrayOutputStream.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @Test
    public void streamgetTest(){
        RedissonClient redissionClient = RedissionClientManager.getRedissionClient();
        RBinaryStream stream = redissionClient.getBinaryStream("ownerStream");
        InputStream inputStream = stream.getInputStream();
        OutputStream outputStream = stream.getOutputStream();
        byte[] bytes = stream.get();
        InputStreamReader inputStreamReader = null;
        try {
            String str = new String(bytes,"utf-8");
            PrintUtil.printInfo(str);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        BufferedReader bufferedReader = null;
        try{
//            UTF-8 ,gbk ,gb2312
            inputStreamReader = new InputStreamReader(inputStream,"gb2312");
            bufferedReader = new BufferedReader(inputStreamReader);
            String line  = "";
           while((line = bufferedReader.readLine()) != null){
               if(StringUtils.isEmpty(line)){
                   continue;
               }
               PrintUtil.printInfo(line);
           }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != inputStreamReader) {
                    inputStreamReader.close();
                }
                if (null != bufferedReader) {
                    bufferedReader.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    @Test
    public void readFile() {
        File file = new File("C:\\Users\\chengjie.wang\\Desktop\\模板\\自营-费用维护结算明细模板1.csv");
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try{
            FileInputStream fileInputStream = new FileInputStream(file);
            inputStreamReader = new InputStreamReader(fileInputStream,"gbk");
            bufferedReader = new BufferedReader(inputStreamReader);
            String line  = "";
            while((line = bufferedReader.readLine()) != null){
                if(StringUtils.isEmpty(line)){
                    continue;
                }
                PrintUtil.printInfo(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != inputStreamReader) {
                    inputStreamReader.close();
                }
                if (null != bufferedReader) {
                    bufferedReader.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    @Test
    public void testWriteArray() throws IOException {
        RedissonClient redissionClient = RedissionClientManager.getRedissionClient();
        RBinaryStream stream = redissionClient.getBinaryStream("ownerStreamtest");
        OutputStream os = stream.getOutputStream();
        byte[] value = {1, 2, 3, 4, 5, 6};
        os.write(value);

        byte[] s = stream.get();
        assertThat(s).isEqualTo(value);
    }


}


