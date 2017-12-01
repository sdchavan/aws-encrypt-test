# aws-encrypt-test

This is a simple test created for testing AWS Encryption SDK 


Prerequisites
----
Please refer here for the entire details:

http://docs.aws.amazon.com/encryption-sdk/latest/developer-guide/java.html


To run from command line
----
In root directory:
- mvn package
- java -jar ./target/aws-encrypt-test.jar {kms-arn} {data-to-encrypt} {context key} {context value}

**Parameters:**

kms-arn
- KMS key arn


data-to-encrypt
- data-to-encrypt


Pair of 'context key' and 'context value' 
- This program expects at least one key-value pair to be used for encrpytion context.
- You can add more than one key-value pairs
  
 

