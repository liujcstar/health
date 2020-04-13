package com.itheima;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.Test;



//七牛云图片上传
public class ImgTest {

    @Test
    public void testUpload(){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
//...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
        String accessKey = "C2wehn7GQ1MqHAqDYU3prnSjBRF1fNv-6i64uDWp";
        String secretKey = "_AJ_pM7h-BKTWzFAtua-RPruDpqMiURvlankUuKS";
        String bucket = "health-up";
//      这里将测试图片存放在项目中，方便测试代码的可移植性(getResource取到的项目目录是字节码文件的目录，存在target目录下)
        String localFilePath = ImgTest.class.getResource("/abc.png").getPath();
//      默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;
        Auth auth = Auth.create(accessKey, secretKey);
//      upToken：上传令牌，动态生成的令牌只能存在1-2分钟，即使访问数据被拦截，被破坏的程度可以降低
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }
}
