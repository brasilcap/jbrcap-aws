package br.com.brasilcap.aws;

import br.com.brasilcap.aws.exception.BrcapAWSException;
import br.com.brasilcap.aws.helper.Constants;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;

public class Dynamo {
	
	private final AmazonDynamoDB client;
	private final DynamoDB dynamoDB;
	
	public Dynamo(Regions region){
		client = AmazonDynamoDBClientBuilder.standard().withRegion(region).build();
		dynamoDB = new DynamoDB(client);
	}
	
	public DeleteItemOutcome delete(DeleteItemSpec deleteItemSpec, String tableName) throws BrcapAWSException{
		DeleteItemOutcome deleteItemOutcome = null;
		
		try {
			Table table = dynamoDB.getTable(tableName);
			deleteItemOutcome = table.deleteItem(deleteItemSpec);
		}catch (Exception e){
			throw new BrcapAWSException(Constants.DynamoExceptionMessages.DELETE_FAIL, e);
		}
		
		return deleteItemOutcome;
	}
	
	public Item get(String tableName, String keyName, Object key) throws BrcapAWSException{
		Item itemRetorno = null;
		
		try {
			Table table = dynamoDB.getTable(tableName);
			itemRetorno = table.getItem(keyName, key);
		}catch (Exception e){
			throw new BrcapAWSException(Constants.DynamoExceptionMessages.GET_FAIL, e);
		}
		
		return itemRetorno;
	}
	
	public PutItemOutcome put(Item item, String tableName) throws BrcapAWSException{
		PutItemOutcome putItemOutcome = null;
		
		try {
			Table table = dynamoDB.getTable(tableName);
			putItemOutcome = table.putItem(item);
		}catch (Exception e){
			throw new BrcapAWSException(Constants.DynamoExceptionMessages.PUT_FAIL, e);
		}
		
		return putItemOutcome;
	}
	
	public UpdateItemOutcome update(String tableName, UpdateItemSpec updateItemSpec) throws BrcapAWSException{
		UpdateItemOutcome updateItemOutcome = null;
		
		try {
			Table table = dynamoDB.getTable(tableName);
			updateItemOutcome = table.updateItem(updateItemSpec);
		}catch (Exception e){
			throw new BrcapAWSException(Constants.DynamoExceptionMessages.UPDATE_FAIL, e);
		}
		
		return updateItemOutcome;
	}
}
