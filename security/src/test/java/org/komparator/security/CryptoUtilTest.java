package org.komparator.security;

import java.io.*;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

import java.util.*;

import org.junit.*;

//import pt.ulisboa.tecnico.sdis.cert.CertUtil;
import pt.ulisboa.tecnico.sdis.ws.cli.CAClient;

import static org.junit.Assert.*;
import static javax.xml.bind.DatatypeConverter.printHexBinary;
public class CryptoUtilTest {

    // static members
	private final String plainText = "This is the plain text!";
	/** Plain text bytes. */
	private final byte[] plainBytes = plainText.getBytes();
	private static final String ASYM_ALGO = "RSA";
	/** Asymmetric cryptography key size. */
	private static final int ASYM_KEY_SIZE = 2048;
	private static final String ASYM_CIPHER = "RSA/ECB/PKCS1Padding";
	//private static String publicKey  = "1nsecure";
	//private static String privateKey = "ins3cur3";
	
    final static String CA_CERTIFICATE = "ca.cer";
	final static String CERTIFICATE = "example.cer";
	
    // one-time initialization and clean-up
    @BeforeClass
    public static void oneTimeSetUp() {
       
    }

    @AfterClass
    public static void oneTimeTearDown() {
        // runs once after all tests in the suite
    }

    // members

    // initialization and clean-up for each test
    @Before
    public void setUp() {
        // runs before each test
    }

    @After
    public void tearDown()  {
        // runs after each test
    }

    // tests
    


	/*@Test
	public void testCertificateCheck() throws Exception {
		Certificate certificate = CryptoUtil.getX509CertificateFromResource(CERTIFICATE);
		Certificate caCertificate = CryptoUtil.getX509CertificateFromResource(CA_CERTIFICATE);

		boolean result = CryptoUtil.verifySignedCertificate(certificate, caCertificate);
		assertTrue(result);
	}*/

	
	@Test
	public void testCertificateFromWrongCA() throws Exception {
		Certificate certificate = CryptoUtil.getX509CertificateFromResource(CERTIFICATE);
		// using wrong certificate here
		Certificate caCertificate = CryptoUtil.getX509CertificateFromResource(CERTIFICATE);

		boolean result = CryptoUtil.verifySignedCertificate(certificate, caCertificate);
		assertFalse(result);
	}
    
    
    
    
    
    
    @Test
    public void test() throws Exception {
    	/*System.out.print("TEST ");
		System.out.print(ASYM_CIPHER);
		System.out.println(" cipher with public, decipher with private");

		System.out.print("Text: ");
		System.out.println(plainText);
		System.out.print("Bytes: ");
		System.out.println(printHexBinary(plainBytes));
		

		System.out.println("Ciphering  with public key...");
	
		System.out.println("Ciphered bytes:");*/
		
		// generate an RSA key pair
		/*KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ASYM_ALGO);
		keyGen.initialize(ASYM_KEY_SIZE);
		KeyPair keyPair = keyGen.generateKeyPair();*/
		
		
		
		CAClient ca = new CAClient( "http://sec.sd.rnl.tecnico.ulisboa.pt:8081/ca" );
		
		String certificateString = ca.getCertificate("A74_Mediator");
		Certificate certificate = CryptoUtil.getX509CertificateFromPEMString(certificateString);
		
		Certificate caCertif = CryptoUtil.getX509CertificateFromResource("ca.cer");
		//Certificate caCertificate = CryptoUtil.getX509CertificateFromResource("ca.cer");
		
		//System.out.println("certificado mediator :    " + certificate.toString());
		//System.out.println("certificado ca :    " + caCertif.toString());
		
		//System.out.println("assinatura ca :    " + caCertif.getPublicKey());
		
		if(!CryptoUtil.verifySignedCertificate(certificate, caCertif)){
			System.out.println("Wrong Signed Certificate....");
			throw new Exception();
		}
		
		PrivateKey privateKey = CryptoUtil.getPrivateKeyFromKeyStoreResource( "A74_Mediator.jks", "5gMInS8H".toCharArray(), "a74_mediator" /*a74_mediator*/ , "5gMInS8H".toCharArray());
		PublicKey publicKey = CryptoUtil.getPublicKeyFromCertificate(certificate);
		
		//KeyPair keyPair = CryptoUtil.verifySignedCertificate(certif, caCertificate);
		
		
		
		
		byte[] cipheredMensage = CryptoUtil.asymCipher(plainBytes, publicKey);
		//System.out.println(printHexBinary(cipheredMensage));
		
		//System.out.println("Deciphering  with private key...");
		byte[] discipheredMensage = CryptoUtil.asymDecipher(cipheredMensage, privateKey);
		//System.out.println("Deciphered bytes:");
		//System.out.println(printHexBinary(discipheredMensage));

		//System.out.print("Text: ");
		String newPlainText = new String(discipheredMensage);
		//System.out.println(newPlainText);
		
		

		assertEquals(plainText, newPlainText);
    }

}
