package com.easy2excel;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.easy2excel.entity.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class LambdaHandler implements RequestHandler<S3Event, String> {

    private AmazonS3 s3client;
    private DynamoDBMapper dynamoDBMapper;


    @Override
    public String handleRequest(S3Event s3Event, Context context) {
        String bucketName = s3Event.getRecords().get(0).getS3().getBucket().getName();
        String fileName = s3Event.getRecords().get(0).getS3().getObject().getKey();
        try {
            initS3Client();
            InputStream inputStream = s3client.getObject(bucketName, fileName).getObjectContent();
            String content = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            context.getLogger().log("file content ::: " + content);
            //read content from s3 bucket & save to dynamoDB table
            Employee employee = new ObjectMapper().readValue(content, Employee.class);
            initDynamoDB();
            dynamoDBMapper.save(employee);
            context.getLogger().log("successfully save data to dynamoDB");

        } catch (IOException e) {
            return "Error while reading file from S3 :::" + e.getMessage();
        }

        return "successfully save data to dynamoDB";
    }

    private void initS3Client() {
        s3client = AmazonS3ClientBuilder
                  .standard()
                  .build();
    }

    private void initDynamoDB(){
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
        dynamoDBMapper = new DynamoDBMapper(client);
    }


}
