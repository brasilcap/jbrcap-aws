package br.com.brasilcap.aws;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import br.com.brasilcap.aws.exception.BrcapAWSException;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
	
public class S3 {
	
	final AmazonS3 s3;
	Regions region;
	
	public S3(Regions region){
		this.region = region;
		s3 = AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                .build();
	}
	
	public BufferedReader get(String bucketName, String key ) throws BrcapAWSException, IOException{
        S3Object fullObject = null;
        try {
        	
            fullObject = s3.getObject(new GetObjectRequest(bucketName, key));
            
            return new BufferedReader(new InputStreamReader(fullObject.getObjectContent()));
        }
        catch(AmazonServiceException e) {
        	throw new BrcapAWSException(e);
        }
        catch(SdkClientException e) {
        	throw new BrcapAWSException(e);
        }
        finally {
            if(fullObject != null) {
                fullObject.close();
            }
        }
	}
	
	public String getObjectAsString(String bucket, String key){
		String object = s3.getObjectAsString(bucket, key);

		return object;

	}
	
	public void putObject(String bucketName, String keyName, String filePath) throws BrcapAWSException{
		final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
		
		try {
			
		    s3.putObject(bucketName, keyName,  new File(filePath));
		
		} catch (AmazonServiceException e) {
			throw new BrcapAWSException(e);
		}
	}
	
	public PutObjectResult put(String bucketName, String keyName, String conteudo) throws BrcapAWSException{
		try {
		
			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentLength(conteudo.length());
			InputStream stream = new ByteArrayInputStream(conteudo.getBytes(StandardCharsets.UTF_8));
			
			return s3.putObject(bucketName, keyName, stream, meta);
	    
		} catch (AmazonServiceException e) {
			throw new BrcapAWSException(e);
		}
	}
	
	public S3Object getObject(String bucketName, String key) throws BrcapAWSException, IOException{
        try {
        	return s3.getObject(bucketName, key);
        }
        catch(AmazonServiceException e) {
        	throw new BrcapAWSException(e);
        }
        catch(SdkClientException e) {
        	throw new BrcapAWSException(e);
        }
	}
}
