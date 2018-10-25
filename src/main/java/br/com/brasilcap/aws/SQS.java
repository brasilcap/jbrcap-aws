package br.com.brasilcap.aws;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import br.com.brasilcap.aws.dto.RetornoSQSMonitorDTO;
import br.com.brasilcap.aws.exception.BrcapAWSException;
import br.com.brasilcap.aws.helper.Constants;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.GetQueueAttributesRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;

public class SQS {

	final AmazonSQS sqs;
	Regions region;
	SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_FULL);
	SimpleDateFormat sdfSmall = new SimpleDateFormat(Constants.DATE_FORMAT_SIMPLE);
	Dynamo dynamo;
	
	public SQS(Regions region){
		this.region = region;
		dynamo = new Dynamo(region);
		sqs = AmazonSQSClientBuilder.defaultClient();
	}

	public RetornoSQSMonitorDTO getMessage(String queueUrl) throws BrcapAWSException {

		Message foundMessage = null;
		RetornoSQSMonitorDTO retornoSQSMonitorDTO = null;
		
		ReceiveMessageRequest receive_request = new ReceiveMessageRequest().withQueueUrl(queueUrl).withWaitTimeSeconds(
				20).withMaxNumberOfMessages(1).withSdkRequestTimeout(25000);

		List<Message> messages = sqs.receiveMessage(receive_request).getMessages();

		if (messages != null && !messages.isEmpty()) {
			foundMessage = messages.get(0);
			
			GetQueueAttributesRequest getQueueAttributesRequest = new GetQueueAttributesRequest();
			getQueueAttributesRequest.setQueueUrl(queueUrl);
			
			List<String> queueAttributes = new ArrayList<String>();
			queueAttributes.add(Constants.QUEUE_ARN);
			
			GetQueueAttributesResult queueAttributesResult = sqs.getQueueAttributes(queueUrl, queueAttributes);
			
			JSONObject jsonMessage = new JSONObject(foundMessage.getBody());
			
			Long queueMonitorId = new JSONObject(jsonMessage.getString(Constants.MESSAGE)).getLong(Constants.QUEUE_MONITOR_ID);
			
			retornoSQSMonitorDTO = new RetornoSQSMonitorDTO();
			
			retornoSQSMonitorDTO.setArn(queueAttributesResult.getAttributes().get(Constants.QUEUE_ARN));
			retornoSQSMonitorDTO.setBodyMessage(foundMessage);
			retornoSQSMonitorDTO.setCode(Constants.SUCESS_CODE);
			retornoSQSMonitorDTO.setMessage(Constants.MESSAGE_NOT_FOUND);
			retornoSQSMonitorDTO.setMessageId(String.valueOf(queueMonitorId));
			retornoSQSMonitorDTO.setReceiptHandle(foundMessage.getReceiptHandle());
			retornoSQSMonitorDTO.setSubject(jsonMessage.getString(Constants.SUBJECT));
			
			Item itemSqs;
			
			itemSqs = new Item();
			itemSqs.withPrimaryKey(Constants.ItemSnsDynamo.ARN, queueAttributesResult.getAttributes().get(Constants.QUEUE_ARN))
			.withString(Constants.ItemSnsDynamo.DATE, sdfSmall.format(new Date())+Constants.KEY_SEPARATION+queueMonitorId)
			.withString(Constants.ItemSnsDynamo.CRIACAO, sdf.format(new Date()))
			.withLong(Constants.ItemSnsDynamo.MESSAGE_ID, queueMonitorId)
			.withString(Constants.ItemSnsDynamo.OPERATION, Constants.READ_OPERATION)
			.withString(Constants.ItemSnsDynamo.SUBJECT, jsonMessage.getString("Subject"));
			
			dynamo.put(itemSqs, Constants.MONITORING_QUEUE_DYNAMO_DB);
		}

		return retornoSQSMonitorDTO;
	}

	public String deleteMessage(String queueUrl, String receiptHandle, String arn, String messageId, String subject) throws BrcapAWSException {
		sqs.deleteMessage(queueUrl, receiptHandle);
		
		Item itemSqs;
		
		itemSqs = new Item();
		itemSqs.withPrimaryKey(Constants.ItemSnsDynamo.ARN, arn)
		.withString(Constants.ItemSnsDynamo.DATE, sdfSmall.format(new Date())+Constants.KEY_SEPARATION+messageId)
		.withString(Constants.ItemSnsDynamo.CRIACAO, sdf.format(new Date()))
		.withLong(Constants.ItemSnsDynamo.MESSAGE_ID, Long.valueOf(String.valueOf(messageId)))
		.withString(Constants.ItemSnsDynamo.OPERATION, Constants.DELETE_OPERATION)
		.withString(Constants.ItemSnsDynamo.SUBJECT, subject);
		
		dynamo.put(itemSqs, Constants.MONITORING_QUEUE_DYNAMO_DB);
		
		return Constants.MESSAGE_DELETED;
	}
}
