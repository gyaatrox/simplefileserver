package com.gy.fileclient.controller;

import com.alibaba.fastjson.JSONObject;
import com.gy.fileclient.utils.AesUtil;
import com.gy.fileclient.utils.DigestUtil;
import com.gy.fileclient.utils.RsaUtil;
import com.gy.fileclient.utils.SignUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * client调用server端上传、下载、查询接口并且加签
 */
@Controller
@Slf4j
public class FileController {

    @Value("${privateKey}")
    private String privateKey;
    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/upload")
    public String upload(MultipartFile file, Map<String,Object> map){
        log.debug("开始上传文件");
        String url = "http://localhost:8080/upload";
        String tempFilePath = "D:/tempfile/"+file.getOriginalFilename();
        File tempFile = new File(tempFilePath);
        tempFile.mkdirs();
        try {
            file.transferTo(tempFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //在http请求头中构造上传文件内容
        HttpHeaders headers = setHeaderInfo();
        FileSystemResource fileSystemResource = new FileSystemResource(tempFilePath);
        MediaType type = MediaType.parseMediaType("multipart/form-data");
        headers.setContentType(type);
        String fileName = file.getOriginalFilename();
        headers.add("Content-Disposition", fileName);
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("file", fileSystemResource);
        HttpEntity httpEntity = new HttpEntity(form,headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
        tempFile.delete();
        //解析server端返回结果
        JSONObject jsonObject = JSONObject.parseObject(responseEntity.getBody()); //字符串转map
        Map<String,Object> fileMap = jsonObject;//json对象转Map
        List<Map<String,Object>> fileList = new ArrayList<>();
        fileList.add(fileMap);
        map.put("fileUploadReturn",fileList);
        log.debug("上传返回map={}",map);
        return "index";
    }

    @GetMapping("/get")
    public String getInfo(@RequestParam("uuid") String uuid, Map<String,Object> map ){
        //调用server端查询接口
        String url ="http://localhost:8080/get?uuid={1}";
        HttpEntity<String> httpEntity = new HttpEntity<>(null,setHeaderInfo());
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class, uuid);
        log.debug("上传后，server端返回结果={}",responseEntity.getBody());
        //解析server端返回结果
        JSONObject jsonObject = JSONObject.parseObject(responseEntity.getBody()); //字符串转map
        Map<String,Object> fileMap = jsonObject;//json对象转Map
        List<Map<String,Object>> fileList = new ArrayList<>();
        fileList.add(fileMap);
        map.put("fileList",fileList);
        log.debug("查询map={}",map);
        return "index";
    }

    @GetMapping("/download")
    public String download(@RequestParam("uuid") String uuid, HttpServletResponse resp)  {
        //调用server端下载接口
        String url ="http://localhost:8080/download?uuid={1}";
        HttpEntity<String> httpEntity = new HttpEntity<>(null,setHeaderInfo());
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class, uuid);
        log.debug("下载后，server端返回结果={}",responseEntity.getBody());
        //解析server端返回结果
        JSONObject jsonObject = JSONObject.parseObject(responseEntity.getBody()); //字符串转map
        Map<String,Object> fileMap = jsonObject;//json对象转Map
        //获得文件的信息
        String digitEnvelop = (String)fileMap.get("digitEnvelop");
        String originName = (String)fileMap.get("originName");
        String mimeType = (String)fileMap.get("type");
        String data = (String)fileMap.get("data");
        //使用私钥对数字签证解密的aes秘钥，再用aes秘钥对文件解密
        byte[] bytes= null;
        String decrypt= null;
        try {
            bytes = RsaUtil.decryptByPrivateKey(DigestUtil.hex2byte(digitEnvelop.getBytes()), privateKey);
            log.debug("解密后的私钥={}",new String(bytes));
            decrypt = AesUtil.decrypt(data, new String(bytes));
            log.debug("解密后的数据={}",decrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //提示下载文件
        String fileName = null;
        try {
            fileName = new String(originName.getBytes("gbk"), "ISO8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        resp.setContentType(mimeType+";charset=UTF-8");
        resp.setHeader("Content-Disposition", "attachment;filename="+fileName);

        ServletOutputStream outputStream = null;
        try {
            outputStream = resp.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outputStream.write(decrypt.getBytes("utf-8"));
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "index";
    }

    @GetMapping("/list")
    public String listInfo(@RequestParam("count") String count, Map<String,Object> map){
        String url ="http://localhost:8080/list?count={1}";
        HttpEntity<String> httpEntity = new HttpEntity<>(null,setHeaderInfo());
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class, count);
        //解析返回json字符串   json字符串为数组样式
        List<Map> fileInfoList = JSONObject.parseArray(responseEntity.getBody(), Map.class);
        System.out.println(fileInfoList);
        map.put("fileInfoList",fileInfoList);
        log.debug("查询10条信息返回={}",map);
        return "index";
    }

    /**
     * 使用私钥加签
     * 给http请求头添加X-SID、X-signature
     */
    public HttpHeaders setHeaderInfo(){
        String sid = UUID.randomUUID().toString().replaceAll("-", "");
        String signature = null;
        try {
            signature = SignUtil.sign(privateKey.getBytes(),sid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.debug("加签结果：X-SID={},X-signature={}",sid,signature);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-SID",sid);
        headers.add("X-signature",signature);
        return headers;
    }
}
