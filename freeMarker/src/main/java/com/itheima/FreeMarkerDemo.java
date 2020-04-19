package com.itheima;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class FreeMarkerDemo {
    public static void main(String[] args) throws IOException, TemplateException {
        Configuration configuration = new Configuration(Configuration.getVersion());

//        获取路径名
        String file = FreeMarkerDemo.class.getResource("/").getFile();
        System.out.println(file);

        configuration.setDirectoryForTemplateLoading(new File(file));

//        获取文件名
        Template template = configuration.getTemplate("test.ftl");

//        设置模板出去的路径和名字
        String resource = FreeMarkerDemo.class.getClass().getResource("/out").getFile();

        System.out.println(resource);
        Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(resource+"/test2.html")), "utf-8"));



        Map map = new HashMap();
        map.put("name", "张三");

        template.process(map,writer);


    }
}
