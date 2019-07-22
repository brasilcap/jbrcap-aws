package br.com.brasilcap.aws;

import java.io.IOException;
import java.nio.ByteBuffer;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
	
public class KMS {
	AWSKMS kmsClient = AWSKMSClientBuilder.defaultClient();
	
	public String decrypt(byte[] cypherText){
		ByteBuffer buffer = ByteBuffer.allocate(cypherText.length);
		buffer.put(cypherText);
		buffer.flip();
		
		DecryptRequest req = new DecryptRequest().withCiphertextBlob(buffer);
		ByteBuffer plainText = kmsClient.decrypt(req).getPlaintext();

		String properties = new String(plainText.array());
		return properties;
	}
	
	public String decryptFromS3(S3Object s3Object) throws IOException{
		return this.decrypt(IOUtils.toByteArray(s3Object.getObjectContent()));
	}
}
