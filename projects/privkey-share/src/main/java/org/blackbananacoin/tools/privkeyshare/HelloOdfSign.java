/*
 * Copyright 2014 Y12STUDIO
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

		String pathEmail = "testdata/sign_hello_world.odt";
		String pathCitizen = "testdata/notupload/citzen_hello_world.odt";
		String pathY12Studio = "testdata/notupload/y12studio_hello_world.odt";
		String pathY12StudioAndCitizen = "testdata/notupload/y12studio_and_citizen_hello_world.odt";

		OdfDocument doc = OdfDocument.loadDocument(new File(
				pathY12StudioAndCitizen));
		for (String fileEntry : doc.getPackage().getFilePaths()) {
			// System.out.println(fileEntry);
			if (fileEntry.startsWith("META-INF/documentsignatures.xml")) {
				// we found signature file create DocumentSignatureGroup and att
				// to list
				// parse file to find all Document signatures
				org.w3c.dom.Document docSig = doc.getPackage()
						.getDom(fileEntry);
				Node nodeSig = docSig.getFirstChild();
				System.out.println(nodeSig.getNodeName());
				System.out.println(nodeSig.getChildNodes().getLength());

				for (int i = 0; i < nodeSig.getChildNodes().getLength(); i++) {
					Node n = nodeSig.getChildNodes().item(i);
					Element sigElement = (Element) n;

					System.out.println(sigElement.getNodeName());
					if (sigElement.getNodeName().equals("Signature")) {

						X509Certificate cert = parseSigGetCertOnly(sigElement);

						// https://github.com/pruiz/signserver/blob/master/signserver/modules/SignServer-Lib-ODFDOM/src/main/java/org/odftoolkit/odfdom/pkg/signature/DocumentSignature.java
						DOMValidateContext dvc = new DOMValidateContext(
								cert.getPublicKey(), sigElement);

						dvc.setURIDereferencer(new ODFURIDereferencer(doc,
								factory.getURIDereferencer()));

						javax.xml.crypto.dsig.XMLSignature sig = factory
								.unmarshalXMLSignature(dvc);

						System.out.println(sig);
						try {

							// validate signature
							boolean validate = sig.validate(dvc);
							System.out.println("VALIDATE RESULT:" + validate);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}

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
