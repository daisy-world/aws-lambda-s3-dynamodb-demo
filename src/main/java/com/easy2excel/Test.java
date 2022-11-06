package com.easy2excel;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.easy2excel.entity.Employee;

public class Test {
    private static DynamoDBMapper dynamoDBMapper ;
    private static final String REGION = "ap-south-1";
    private static final String ENDPOINT = "dynamodb.ap-south-1.amazonaws.com";

    public static void main(String[] args) {
     initDynamoDBMapper();
        Employee emp = new Employee();
        emp.setEmpId("100");
        emp.setEmail("lipsa@gmail.com");
        emp.setName("lipsa patra");
        dynamoDBMapper.save(emp);
    }

    private static  void initDynamoDBMapper(){
        AmazonDynamoDB client = null;
        try {
             client = AmazonDynamoDBClientBuilder
                    .standard()
                     .withRegion(Regions.fromName(REGION))
                   // .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(ENDPOINT, REGION))
                   // .withCredentials(new DefaultAWSCredentialsProviderChain())
                   // .disableEndpointDiscovery()
                    .build();
        }catch(Exception ex){
            System.out.println("exception " + ex.getMessage());
        }
        dynamoDBMapper = new DynamoDBMapper(client);
    }
}
