package org.blackbananacoin.tools.privkeyshare;

import java.io.File;
import java.io.InputStream;
import java.security.cert.X509Certificate;

import javax.xml.crypto.Data;
import javax.xml.crypto.OctetStreamData;
import javax.xml.crypto.URIDereferencer;
import javax.xml.crypto.URIReference;
import javax.xml.crypto.URIReferenceException;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMValidateContext;

import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.keys.keyresolver.KeyResolverException;
import org.apache.xml.security.signature.XMLSignatureException;
import org.odftoolkit.odfdom.doc.OdfDocument;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class HelloOdfSign {

	// This is important!
	static {
		org.apache.xml.security.Init.init();
	}

	public static class ODFURIDereferencer implements URIDereferencer {
		OdfDocument odfDoc;
		URIDereferencer defaultURIDereferencer;

		public ODFURIDereferencer(OdfDocument pOdfDocument,
				URIDereferencer pDefaultURIDereferencer) {
			odfDoc = pOdfDocument;
			defaultURIDereferencer = pDefaultURIDereferencer;
		}

		@Override
		public Data dereference(URIReference arg0, XMLCryptoContext arg1)
				throws URIReferenceException {

			String partPath = arg0.getURI().toString();
			System.out.println(partPath + " in package ? "
					+ odfDoc.getPackage().contains(partPath));

			// see if our document contains this part, if not dereference using
			// default dereferencer
			if (!odfDoc.getPackage().contains(partPath)) {
				return defaultURIDereferencer.dereference(arg0, arg1);
			}

			// return part content as octet stream data
			InputStream is = odfDoc.getPackage().getInputStream(partPath);
			OctetStreamData retData = new OctetStreamData(is);

			return retData;

		}
	}

	public static void main(String[] args) throws Exception {
		// https://weblogs.java.net/blog/mullan/archive/2008/02/using_jsr_105_w.html
		XMLSignatureFactory factory = XMLSignatureFactory.getInstance("DOM",
				new org.jcp.xml.dsig.internal.dom.XMLDSigRI());

		OdfDocument doc = OdfDocument.loadDocument(new File(
				"testdata/sign_hello_world.odt"));
		for (String fileEntry : doc.getPackage().getFilePaths()) {
			// System.out.println(fileEntry);
			if (fileEntry.startsWith("META-INF/documentsignatures.xml")) {
				// we found signature file create DocumentSignatureGroup and att
				// to list
				// parse file to find all Document signatures
				org.w3c.dom.Document docSig = doc.getPackage()
						.getDom(fileEntry);
				Node nodeSig = docSig.getFirstChild();
				Element sigElement = (Element) nodeSig.getFirstChild();
				System.out.println(sigElement.getNodeName());

				X509Certificate cert = parseSigGetCertOnly(sigElement);

				// https://github.com/pruiz/signserver/blob/master/signserver/modules/SignServer-Lib-ODFDOM/src/main/java/org/odftoolkit/odfdom/pkg/signature/DocumentSignature.java
				DOMValidateContext dvc = new DOMValidateContext(
						cert.getPublicKey(), sigElement);

				dvc.setURIDereferencer(new ODFURIDereferencer(doc, factory
						.getURIDereferencer()));

				javax.xml.crypto.dsig.XMLSignature sig = factory
						.unmarshalXMLSignature(dvc);

				System.out.println(sig);

				// validate signature
				boolean validate = sig.validate(dvc);
				System.out.println("VALIDATE RESULT:" + validate);

			}
		}
	}

	private static X509Certificate parseSigGetCertOnly(Element sigElement)
			throws XMLSignatureException, XMLSecurityException, Exception,
			KeyResolverException {
		org.apache.xml.security.signature.XMLSignature signature = new org.apache.xml.security.signature.XMLSignature(
				sigElement, "");

		KeyInfo ki = signature.getKeyInfo();
		if (ki == null) {
			throw new Exception("Did not find KeyInfo");
		}

		X509Certificate cert = signature.getKeyInfo().getX509Certificate();

		System.out.println(cert);
		// boolean valid = signature.checkSignatureValue(cert);
		return cert;
	}

}
