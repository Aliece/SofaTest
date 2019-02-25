package me.qlong.controller;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class RuleClientApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    Logger logger= LoggerFactory.getLogger(RuleClientApplicationListener.class);

    /**
     * client-健康检查类
     */

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event){

        System.out.println("12222222222222222222222222222222222");
        try {
            byte[] dat =toBytes(event);
            fromBytes(dat,event.getClass());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public <T> T fromBytes(byte[] dat, Type type) throws Exception {
        Hessian2Input input = null;
        try {
            input = new Hessian2Input(new ByteArrayInputStream(dat));
            return (T) input.readObject();
        } finally {
            if (input != null) {
                input.close();
            }
        }
    }

    public byte[] toBytes(Object obj) throws Exception{
        Hessian2Output out = null;
        try {
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            out = new Hessian2Output(byteArray);
            out.setSerializerFactory();
            out.writeObject(obj);
            out.flush();
            return byteArray.toByteArray();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    logger.warn("close object output stream error! {}", e.getMessage(), e);
                }
            }
        }
    }
}
