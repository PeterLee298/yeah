package com.stickercamera.app.personal.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

/**
 * Created by litingchang on 15-10-4.
 */
public class Test {

    ResponseData<String> responseData = new ResponseData<String>();

    String json = "{ \"result\": true, \"type\": \"Info execute\", \"code\": 200, \"message\": \"Success\", \"data\": { \"id\": 20150717, \"icon\": \"http://youryeah-test.oss-cn-beijing.aliyuncs.com/storage/4030/5281642/VgALDksC6zhwt2oCQMfBUCRHulJ2Z.jpg\", \"title\": \"ffffffew\", \"description\": \"adasdasd\", \"groupId\": 20150716, \"priority\": 85, \"state\": 0, \"createdAt\": 1442576959000, \"updatedAt\": 1442843410000 } }";

    ResponseData<String> responseData2 = JSON.parseObject(json, new TypeReference<ResponseData<String>>(){}.getType());

    ResponseData<String> responseData3 = JSON.parseObject(json, ResponseData.class);


    int i = responseData3.getCode();
}
