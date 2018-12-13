package com.bookexchange.app.utils;

import org.jasypt.digest.PooledStringDigester;
import org.jasypt.digest.StringDigester;
import org.jasypt.digest.config.SimpleStringDigesterConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class JasyptEncryptor {
    public JasyptEncryptor() {}

    @Bean("jasyptStringEDigester")
    public StringDigester strongTextEncryptor() {
        /**
         * 经验总结: 注意区分开encryptor和digester的区别, 前者可以decrypt, 简直扯淡
         * 如果需要解密, 要么使用PBE或者DES这种对称加密, 要么就要用StrongEncryptor, 然后下载JCE安装(对应的provider)
         * 后者千万注意要把provider给去掉, 真tm操蛋
         */
        PooledStringDigester encryptor = new PooledStringDigester();
        SimpleStringDigesterConfig config = new SimpleStringDigesterConfig();
        config.setAlgorithm("SHA-256");
        config.setPoolSize("1");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
        return encryptor;
    }
}
