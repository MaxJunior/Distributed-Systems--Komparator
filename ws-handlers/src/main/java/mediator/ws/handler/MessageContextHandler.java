package mediator.ws.handler;

import java.io.ByteArrayOutputStream;
import java.security.cert.Certificate;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.crypto.Cipher;
import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.MessageContext.Scope;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPFaultException;

import org.komparator.security.CryptoUtil;
import static javax.xml.bind.DatatypeConverter.printBase64Binary ;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import pt.ulisboa.tecnico.sdis.ws.cli.CAClient;

/**
 * This SOAPHandler outputs the contents of the message context for inbound and
 * outbound messages.
 */
public class MessageContextHandler implements SOAPHandler<SOAPMessageContext> {

	//
	// Handler interface implementation
	//

	/**
	 * Gets the header blocks that can be processed by this Handler instance. If
	 * null, processes all.
	 */
	@Override
	public Set<QName> getHeaders() {
		return null;
	}

	/**
	 * The handleMessage method is invoked for normal processing of inbound and
	 * outbound messages.
	 */
	@Override
	public boolean handleMessage(SOAPMessageContext smc) {
		System.out.println("MessageContextHandler:");
		
		
		Boolean outboundElement = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

		try {
			if (outboundElement.booleanValue()) {  //A enviar...
				System.out.println("Writing header in outbound SOAP message...");

				// get SOAP envelope
				SOAPMessage msg = smc.getMessage();
				SOAPPart sp = msg.getSOAPPart();
				SOAPEnvelope se = sp.getEnvelope();
				SOAPBody sb = se.getBody();
				
				QName svcn = (QName) smc.get(MessageContext.WSDL_SERVICE);
				QName opn = (QName) smc.get(MessageContext.WSDL_OPERATION);
				
				
				
				
				NodeList children = sb.getFirstChild().getChildNodes();
				
				for (int i = 0; i < children.getLength(); i++) {
					Node argument = children.item(i);
					if (argument.getNodeName().equals("creditCardNr")) {
						String secretArgument = argument.getTextContent();
						
						// cipher message with symmetric key
						/*ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
						byteOut.write(secretArgument.getBytes());
						Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
						cipher.init(Cipher.ENCRYPT_MODE,    );
						byte[] cipheredArgument = cipher.doFinal(byteOut.toByteArray());
						
						String encodedSecretArgument = printBase64Binary(cipheredArgument);*/
						
						
						CAClient ca = new CAClient( "http://sec.sd.rnl.tecnico.ulisboa.pt:8081/ca" );
						String certificateString = ca.getCertificate("A74_Mediator");
						Certificate certificate = CryptoUtil.getX509CertificateFromPEMString(certificateString);
						
						byte[] cipheredArgument = CryptoUtil.asymCipher(secretArgument.getBytes(), certificate.getPublicKey());
						//CryptoUtil ;
						
						String encodedSecretArgument = printBase64Binary(cipheredArgument);
						
						argument.setTextContent(encodedSecretArgument); 
						
						msg.saveChanges();
					}
					//return true;
				}
				
				

			} else {    //A receber...
				System.out.println("Reading header in inbound SOAP message...");

				// get SOAP envelope header
				SOAPMessage msg = smc.getMessage();
				SOAPPart sp = msg.getSOAPPart();
				SOAPEnvelope se = sp.getEnvelope();
				SOAPHeader sh = se.getHeader();

				
				
				

			}
		} catch (Exception e) {
			System.out.print("Caught exception in handleMessage: ");
			System.out.println(e);
			System.out.println("Continue normal processing...");
		}
		
		
		
		
		
		return true;
	}

	/** The handleFault method is invoked for fault message processing. */
	@Override
	public boolean handleFault(SOAPMessageContext smc) {
		//printMessageContext(smc);
		System.out.println("handle fault mediator.....");
		return true;
	}

	/**
	 * Called at the conclusion of a message exchange pattern just prior to the
	 * JAX-WS runtime dispatching a message, fault or exception.
	 */
	@Override
	public void close(MessageContext messageContext) {
		// nothing to clean up
	}

	private void printMessageContext(MessageContext map) {
		System.out.println("Message context: (scope,key,value)");
		try {
			Iterator<String> it = map.keySet().iterator();
			while (it.hasNext()) {
				Object key = it.next();
				Object value = map.get(key);

				String keyString;
				if (key == null)
					keyString = "null";
				else
					keyString = key.toString();

				String valueString;
				if (value == null)
					valueString = "null";
				else
					valueString = value.toString();

				Object scope = map.getScope(keyString);
				String scopeString;
				if (scope == null)
					scopeString = "null";
				else
					scopeString = scope.toString();
				scopeString = scopeString.toLowerCase();

				System.out.println("(" + scopeString + "," + keyString + "," + valueString + ")");
			}

		} catch (Exception e) {
			System.out.printf("Exception while printing message context: %s%n", e);
		}
	}

}
