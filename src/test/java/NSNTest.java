import org.junit.Assert;
import org.junit.Test;

import br.com.brasilcap.aws.SNS;
import br.com.brasilcap.aws.helper.Constants;

import com.amazonaws.regions.Regions;


public class NSNTest {
	
	private final String topicArn = "";
	
	@Test
	public void envioMensagem(){
		try {
			
			SNS sns = new SNS(Regions.SA_EAST_1);
			
			String retorno = sns.sendMessage(topicArn, "{\"teste\" : \"hello\"}", "testando");
			
			Assert.assertEquals(retorno, Constants.SNS_NOTIFICATION_RETURN_MESSAGE);
			
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
}
