package br.com.brasilcap.aws;

import br.com.brasilcap.aws.dto.EmailDTO;
import br.com.brasilcap.aws.exception.EmailException;
import br.com.brasilcap.aws.helper.Constants;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

public class Email {
	
	public String sendEmail(EmailDTO emailDTO) throws EmailException {
		
		try {
	      
			AmazonSimpleEmailService client = 
	          AmazonSimpleEmailServiceClientBuilder.standard()
	            .withRegion(Regions.US_EAST_1).build();
	      SendEmailRequest request = new SendEmailRequest()
	          .withDestination(
	              new Destination().withToAddresses(emailDTO.getTo()))
	          .withMessage(new Message()
	              .withBody(new Body()
	                  .withHtml(new Content()
	                      .withCharset(Constants.Email.CHARSET).withData(emailDTO.getHtmlBody()))
	                  .withText(new Content()
	                      .withCharset(Constants.Email.CHARSET).withData(emailDTO.getTextBody())))
	              .withSubject(new Content()
	                  .withCharset(Constants.Email.CHARSET).withData(emailDTO.getSubject())))
	          .withSource(emailDTO.getFrom());
	          
	      	  if (emailDTO.getConfigSet() != null){
	      		((SendEmailRequest) client).withConfigurationSetName(emailDTO.getConfigSet());
	      	  }
	      	  
	      client.sendEmail(request);
	      
	      return Constants.Email.EMAIL_SENT_MESSAGE;
	      
	    } catch (Exception ex) {
	      throw new EmailException(ex.getMessage());
	    }
	} 
}
