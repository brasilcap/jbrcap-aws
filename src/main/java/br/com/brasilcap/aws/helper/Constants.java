package br.com.brasilcap.aws.helper;

public interface Constants {
	
	/** TOKEN. */
	String MESSAGE_STRUCTURE_SNS = "text";
	String DATE_FORMAT_FULL = "yyyy-MM-dd hh:mm:ss:SSS";
	String DATE_FORMAT_SIMPLE = "yyyy-MM-dd";
	String SEND_OPERATION = "S";
	String READ_OPERATION = "R";
	String DELETE_OPERATION = "D";
	String KEY_SEPARATION = "#";
	String SQS = "sqs";
	String MONITORING_QUEUE_DYNAMO_DB = "darwin-queue-monitor";
	String BUCKET_QUEUE_MONITOR = "brasilcap-darwin-queue-monitor";
	String SNS_NOTIFICATION_RETURN_MESSAGE = "Notificação realizada com sucesso!";
	String QUEUE_ARN = "QueueArn";
	String MESSAGE = "Message";
	String QUEUE_MONITOR_ID = "QueueMonitorId";
	String SUCESS_CODE =  "200";
	String MESSAGE_NOT_FOUND = "message found";
	String MESSAGE_DELETED = "Mensagem removida com sucesso!";
	String SUBJECT = "Subject";
	
	public interface ItemSnsDynamo {
		String ARN = "arn";
		String DATE = "date";
		String CRIACAO = "criacao";
		String MESSAGE_ID = "messageId";
		String OPERATION = "operation";
		String SUBJECT = "subject";
	}
	
	public interface DynamoExceptionMessages {
		String DELETE_FAIL = "Falha em delete no dynamo.";
		String GET_FAIL = "Falha em get no dynamo.";
		String PUT_FAIL = "Falha em put no dynamo.";
		String UPDATE_FAIL = "Falha em update no dynamo.";
	}
	
	public interface Email {
		String EMAIL_SENT_MESSAGE = "Email sent!";
		String CHARSET = "UTF-8";
	}
}
