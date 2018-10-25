import org.junit.Assert;
import org.junit.Test;

import br.com.brasilcap.aws.SQS;
import br.com.brasilcap.aws.dto.RetornoSQSMonitorDTO;
import br.com.brasilcap.aws.helper.Constants;

import com.amazonaws.regions.Regions;

public class SQSTest {
	
	private final String url = "";
	
	@Test
	public void leituraFila(){
		try {
			
			SQS sqs = new SQS(Regions.SA_EAST_1);
			 
			RetornoSQSMonitorDTO dto = sqs.getMessage(url);
			
			Assert.assertNotNull(dto.getArn());
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	public void leituraFilaComRemocao(){
		try {
			
			SQS sqs = new SQS(Regions.SA_EAST_1);
			
			RetornoSQSMonitorDTO dto = sqs.getMessage(url);
			
			if (dto != null){
				
				String retorno = sqs.deleteMessage(url, dto.getReceiptHandle(), dto.getArn(), dto.getMessageId(), dto.getSubject());
			
				Assert.assertEquals(retorno, Constants.MESSAGE_DELETED);
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
}
