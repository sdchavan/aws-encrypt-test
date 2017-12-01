package com.example.awstest.encryption;
/*
 * Copyright 2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except
 * in compliance with the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.encryptionsdk.AwsCrypto;
import com.amazonaws.encryptionsdk.CryptoResult;
import com.amazonaws.encryptionsdk.kms.KmsMasterKey;
import com.amazonaws.encryptionsdk.kms.KmsMasterKeyProvider;

/**
 * <p>
 * Encrypts and then decrypts a string under a KMS key
 * 
 * <p>
 * Arguments:
 * <ol>
 * <li>Key ARN: For help finding the Amazon Resource Name (ARN) of your KMS
 * customer master key (CMK), see 'Viewing Keys' at
 * http://docs.aws.amazon.com/kms/latest/developerguide/viewing-keys.html
 * <li>String to encrypt
 * </ol>
 */
public class StringExample {
	private static String keyArn;
	private static String contextKey;
	private static String contextValue;
	private static String data;

	public static void main(final String[] args) {

		// Get the key arn and data to be encrypted
		keyArn = args[0];
		data = args[1];
		
		// Build the context from args (always has to be in the key-value form)
		final Map<String, String> context = new HashMap<String, String>(); 
				Collections.singletonMap(contextKey, contextValue);
		for (int i=2; i<args.length; i=i+2 ) {
			context.put(args[i], args[i+1]);
		}
		
		// Instantiate the SDK
		final AwsCrypto crypto = new AwsCrypto();

		// Set up the KmsMasterKeyProvider backed by the default credentials
		final KmsMasterKeyProvider prov = new KmsMasterKeyProvider(keyArn);

		// Encrypt the data
		//
		// Most encrypted data should have an associated encryption context
		// to protect integrity. This sample uses placeholder values.
		//
		// For more information see:
		// blogs.aws.amazon.com/security/post/Tx2LZ6WBJJANTNW/How-to-Protect-the-Integrity-of-Your-Encrypted-Data-by-Using-AWS-Key-Management

		final String ciphertext = crypto.encryptString(prov, data, context).getResult();
		System.out.println("Data to be encrypted: " + data);
		System.out.println("Ciphertext:\n" + ciphertext);

		// Decrypt the data
		final CryptoResult<String, KmsMasterKey> decryptResult = crypto.decryptString(prov, ciphertext);
		// Before returning the plaintext, verify that the customer master key that
		// was used in the encryption operation was the one supplied to the master key
		// provider.
		if (!decryptResult.getMasterKeyIds().get(0).equals(keyArn)) {
			throw new IllegalStateException("Wrong key ID!");
		}

		// Also, verify that the encryption context in the result contains the
		// encryption context supplied to the encryptString method. Because the
		// SDK can add values to the encryption context, don't require that
		// the entire context matches.
		for (final Map.Entry<String, String> e : context.entrySet()) {
			if (!e.getValue().equals(decryptResult.getEncryptionContext().get(e.getKey()))) {
				throw new IllegalStateException("Wrong Encryption Context!");
			}
		}

		// Now we can return the plaintext data
		System.out.println("For verification, data after decrypting the Ciphertext: " + decryptResult.getResult());
	}
}
