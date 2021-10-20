import com.alibaba.fastjson.JSON;
import com.tulin.avro.SimpleBeanAvro;
import com.tulin.protobuf.SimpleBeanProtobuf;
import com.tulin.pojo.SimpleBeanNative;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.junit.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA
 * User: Ginger
 * Date: 2018/8/9
 * Time: 4:59 PM
 */
public class ObjectSerializationTest {
    static final int COUNT = 100000;

    @Test
    public void testFastJson() {
        SimpleBeanNative nativeBean = new SimpleBeanNative();
        nativeBean.setId(1);
        nativeBean.setUrl("www.baidu.com");
        ArrayList<String> list = new ArrayList<String>();
        list.add("zhang san");
        list.add("li si");
        list.add("wang wu");
        nativeBean.setName(list);

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < COUNT; i++) {
            String jsonString = JSON.toJSONString(nativeBean);
            //JSON.parse(jsonString);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("FastJson 程序运行时间：" + (endTime - startTime) + " ms");
    }

    @Test
    public void testNativeSerialization() throws IOException {
        SimpleBeanNative nativeBean = new SimpleBeanNative();
        nativeBean.setId(1);
        nativeBean.setUrl("www.baidu.com");
        ArrayList<String> list = new ArrayList<String>();
        list.add("zhang san");
        list.add("li si");
        list.add("wang wu");
        nativeBean.setName(list);

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < COUNT; i++) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream outputStreamObj = new ObjectOutputStream(outputStream);
            outputStreamObj.writeObject(nativeBean);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Native java 程序运行时间：" + (endTime - startTime) + " ms");
    }

    @Test
    public void testProtobufSerialization() throws IOException {
        SimpleBeanProtobuf.testBuf.Builder builder = SimpleBeanProtobuf.testBuf.newBuilder();
        builder.setID(1);
        builder.setUrl("www.baidu.com");
        builder.addName("zhang san");
        builder.addName("li si");
        builder.addName("wang wu");

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < COUNT; i++) {
            SimpleBeanProtobuf.testBuf info = builder.build();
            byte[] result = info.toByteArray();
            SimpleBeanProtobuf.testBuf testBuf = SimpleBeanProtobuf.testBuf.parseFrom(result);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("protobuf 运行时间：" + (endTime - startTime) + " ms");
    }

    @Test
    public void testAvroSerialization() throws IOException {
        SimpleBeanAvro.Builder avroBuilder = SimpleBeanAvro.newBuilder();
        avroBuilder.setID(1);
        avroBuilder.setUrl("www.baidu.com");
        List<CharSequence> avlist= new ArrayList<CharSequence>();
        avlist.add("zhang san");
        avlist.add("li si");
        avlist.add("wang wu");

        avroBuilder.setName(avlist);

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < COUNT; i++) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            DatumWriter<SimpleBeanAvro> avroDatumWriter = new SpecificDatumWriter<SimpleBeanAvro>(SimpleBeanAvro.class);
            Encoder avencoder = EncoderFactory.get().binaryEncoder(out, null);
            avroDatumWriter.write(avroBuilder.build(), avencoder);
            avencoder.flush();
            out.close();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Avro 运行时间：" + (endTime - startTime) + " ms");
    }
}
