package com.zsx.utils;

import com.zsx.annotation.EncryptTransaction;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * @author zhousx
 * @create 2023/10/01-22:45
 **/
@Component
public class DecryptImpl implements IDecryptUtil {

    /**
     * 解密
     *
     * @param result resultType的实例
     */
    @Override
    public <T> T decrypt(T result) throws IllegalAccessException {
        //取出resultType的类
        Class<?> resultClass = result.getClass();
        Field[] declaredFields = resultClass.getDeclaredFields();
        for (Field field : declaredFields) {
            //取出所有被DecryptTransaction注解的字段
            EncryptTransaction encryptTransaction = field.getAnnotation(EncryptTransaction.class);
            if (!Objects.isNull(encryptTransaction)) {
                field.setAccessible(true);
                Object object = field.get(result);
                //String的解密
                if (object instanceof String) {
                    String value = (String) object;
                    //对注解的字段进行逐一解密
                    try {
                        field.set(result, DBAESUtil.decrypt(value));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return result;
    }
}
