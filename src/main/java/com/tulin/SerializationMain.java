package com.tulin;

import com.alibaba.fastjson.JSON;
import com.tulin.avro.SimpleBeanAvro;
import com.tulin.protobuf.SimpleBeanProtobuf;
import com.tulin.pojo.SimpleBeanNative;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA
 * User: Ginger
 * Date: 2018/8/9
 * Time: 6:41 PM
 */
public class SerializationMain {
    private static Logger logger = LoggerFactory.getLogger(SerializationMain.class);
    public static void main(String[] argv) throws IOException {
        //初始化pojo对象
        SimpleBeanNative nativeBean = new SimpleBeanNative();
        nativeBean.setId(1);
        nativeBean.setUrl("www.baidu.com");
        ArrayList<String> list = new ArrayList<String>();
        list.add("zhang san");
        list.add("li si");
        list.add("wang wu");
        nativeBean.setName(list);
        //FastJson

        System.out.println(nativeBean.toString());

        logger.info("FastJson's length: " + JSON.toJSONBytes(nativeBean).length);
        //原生的序列化方法
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream outputStreamObj = new ObjectOutputStream(outputStream);
        outputStreamObj.writeObject(nativeBean);

        logger.info("Native java's serializer length: " + outputStream.toByteArray().length);
        //base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        String base64String = encoder.encode(outputStream.toByteArray());
        logger.info("Base64's serializer length: " + base64String.getBytes().length);

        //Protobuf
        SimpleBeanProtobuf.testBuf.Builder builder = SimpleBeanProtobuf.testBuf.newBuilder();
        builder.setID(1);
        builder.setUrl("www.baidu.com");
        builder.addName("zhang san");
        builder.addName("li si");
        builder.addName("wang wu");
        SimpleBeanProtobuf.testBuf info = builder.build();
        byte[] result = info.toByteArray();
        logger.info("google protobuf length: " + result.length);

        //Avro
        SimpleBeanAvro.Builder avroBuilder = SimpleBeanAvro.newBuilder();
        avroBuilder.setID(1);
        avroBuilder.setUrl("www.baidu.com");
        List<CharSequence> avlist= new ArrayList<CharSequence>();
        avlist.add("zhang san");
        avlist.add("li si");
        avlist.add("wang wu");

        avroBuilder.setName(avlist);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        DatumWriter<SimpleBeanAvro> avroDatumWriter = new SpecificDatumWriter<SimpleBeanAvro>(SimpleBeanAvro.class);
        Encoder avencoder = EncoderFactory.get().binaryEncoder(out, null);
        avroDatumWriter.write(avroBuilder.build(), avencoder);
        avencoder.flush();
        out.close();
        logger.info("Avro's serializer length: " + out.toByteArray().length);
    }
}
