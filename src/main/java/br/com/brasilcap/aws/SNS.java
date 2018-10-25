package br.com.brasilcap.aws;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.json.JSONObject;

import br.com.brasilcap.aws.exception.BrcapAWSException;
import br.com.brasilcap.aws.helper.Constants;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.ListSubscriptionsByTopicResult;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.Subscription;


public class SNS {

	Regions region;
	
	public SNS(Regions region){
		this.region = region;
	}
	
	@SuppressWarnings("deprecation")
	public String sendMessage(String topicArn, JSONObject payload, String subject) throws BrcapAWSException{
		
		AmazonSNSClient snsClient = new AmazonSNSClient();
		long messageId = getRandom();
		
		payload.put(Constants.QUEUE_MONITOR_ID, messageId);
		
		String playloadString = payload.toString();
		PublishRequest publishRequest = new PublishRequest();
		publishRequest.setMessage(playloadString);
		publishRequest.setMessageStructure(Constants.MESSAGE_STRUCTURE_SNS);
		publishRequest.setTargetArn(topicArn);
		publishRequest.setSubject(subject);
		snsClient.setRegion(Region.getRegion(region));
		snsClient.publish(publishRequest);
		
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_FULL);
		SimpleDateFormat sdfSmall = new SimpleDateFormat(Constants.DATE_FORMAT_SIMPLE);
		Dynamo dynamo = new Dynamo(region);
		
		try{
			
			Item itemSns;
			
			itemSns = new Item();
			itemSns.withPrimaryKey(Constants.ItemSnsDynamo.ARN, topicArn)
			.withString(Constants.ItemSnsDynamo.DATE, sdfSmall.format(new Date())+Constants.KEY_SEPARATION+messageId)
			.withString(Constants.ItemSnsDynamo.CRIACAO, sdf.format(new Date()))
			.withLong(Constants.ItemSnsDynamo.MESSAGE_ID, Long.valueOf(String.valueOf(messageId)))
			.withString(Constants.ItemSnsDynamo.OPERATION, Constants.SEND_OPERATION)
			.withString(Constants.ItemSnsDynamo.SUBJECT, subject);
			
			dynamo.put(itemSns, Constants.MONITORING_QUEUE_DYNAMO_DB);
			
			S3 s3 = new S3(region);
			s3.put(Constants.BUCKET_QUEUE_MONITOR, String.valueOf(messageId), payload.toString());
			
			ListSubscriptionsByTopicResult listSubscriptionsByTopicResult = snsClient.listSubscriptionsByTopic(topicArn);
			
			Item itemSqs;
			for(Subscription subscription : listSubscriptionsByTopicResult.getSubscriptions()){
				
				if (subscription.getProtocol().equalsIgnoreCase(Constants.SQS)){
					
					itemSqs = new Item();
					itemSqs.withPrimaryKey(Constants.ItemSnsDynamo.ARN, subscription.getEndpoint())
					.withString(Constants.ItemSnsDynamo.DATE, sdfSmall.format(new Date())+Constants.KEY_SEPARATION+messageId)
					.withString(Constants.ItemSnsDynamo.CRIACAO, sdf.format(new Date()))
					.withLong(Constants.ItemSnsDynamo.MESSAGE_ID, Long.valueOf(String.valueOf(messageId)))
					.withString(Constants.ItemSnsDynamo.OPERATION, Constants.SEND_OPERATION)
					.withString(Constants.ItemSnsDynamo.SUBJECT, subject);
					
					dynamo.put(itemSqs, Constants.MONITORING_QUEUE_DYNAMO_DB);
				}
			}	
			
		}catch(BrcapAWSException ex){
			throw new BrcapAWSException(ex);
		}
		
		return Constants.SNS_NOTIFICATION_RETURN_MESSAGE;
	}
	
	private long getRandom(){
		Calendar calendar = Calendar.getInstance();
		Random random = new Random();
		return (long) Math.floor(calendar.getTimeInMillis() + random.nextInt(100));
	}
}
