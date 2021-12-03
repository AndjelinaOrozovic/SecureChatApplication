package org.unibl.etf.chatApp;

import org.unibl.etf.beans.UserBean;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Calendar;
import java.util.Date;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.bouncycastle.util.encoders.Base64;

public class DigitalCertificates {

	public static final String BC_PROVIDER = "BC";
	public static final String KEY_ALGORITHM = "RSA";
	public static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
	public static final String KEYSTORE_TYPE = "PKCS12";
	public static final String KEYSTORE_PASS = "sigurnost";
	public static final String CERTS_PATH = "C:\\Users\\andje\\eclipse-workspace\\ChatApplication\\certs\\";

	public DigitalCertificates() {
		super();
	}
	
	public static boolean generateRoot() {
		boolean result = false;
		
		try {
			Security.addProvider(new BouncyCastleProvider());
			
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -1);
			Date startDate = calendar.getTime();
			calendar.add(Calendar.YEAR, 2);
			Date endDate = calendar.getTime();
			BigInteger certSN = new BigInteger(Long.toString(new SecureRandom().nextLong()));
			
			RSAPrivateKey rootPrivateKey = loadPrivateKey(CERTS_PATH + "root\\rootkey.key");
			PublicKey rootPublicKey = loadPublicKey(CERTS_PATH + "root\\rootpublic.key");
			
			
			String subjectName = "CN=" + "ChatApplication";
			X500Name certSubject = new X500Name(subjectName);
			
			JcaContentSignerBuilder contentSignerBuilder = new JcaContentSignerBuilder(SIGNATURE_ALGORITHM).setProvider(BC_PROVIDER);
			ContentSigner contentSigner = contentSignerBuilder.build(rootPrivateKey);
			
			X509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(certSubject, certSN, startDate, endDate, certSubject, rootPublicKey);
			
			certBuilder.addExtension(Extension.basicConstraints, true, new BasicConstraints(true));
			certBuilder.addExtension(Extension.extendedKeyUsage, true, new ExtendedKeyUsage(new KeyPurposeId[]{KeyPurposeId.id_kp_serverAuth}));
			
			X509CertificateHolder certHolder = certBuilder.build(contentSigner);
			X509Certificate cert = new JcaX509CertificateConverter().setProvider(BC_PROVIDER).getCertificate(certHolder);
			
			writeCertToFile(cert, CERTS_PATH + "root\\" + "root" + ".crt");
			exportToPkcs12(cert, rootPrivateKey, "root", CERTS_PATH + "root\\" + "root" + ".pfx");
			
			result = true;
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return result;		
	}

	public static boolean generateCertificate(String username) {
		boolean result = false;

		System.out.println("Certs path: " + CERTS_PATH);
		System.out.println("Username: " + username);

		try {

			Security.addProvider(new BouncyCastleProvider());

			X509Certificate rootCert = loadCertificate(CERTS_PATH + "root\\root.crt");
			RSAPrivateKey rootKey = loadPrivateKey(CERTS_PATH + "root\\rootkey.key");
			X500Name rootCertIssuer = new X500Name("CN=" + "ChatApplication");

			KeyPair keyPair = generateUserKeyPair();

			JcaContentSignerBuilder contentSignerBuilder = new JcaContentSignerBuilder(SIGNATURE_ALGORITHM)
					.setProvider(BC_PROVIDER);
			ContentSigner contentSigner = contentSignerBuilder.build(rootKey);

			X500Name certSubject = new X500Name("CN=" + username + "-cert");
			PKCS10CertificationRequestBuilder reqBuilder = new JcaPKCS10CertificationRequestBuilder(certSubject,
					keyPair.getPublic());
			PKCS10CertificationRequest certReq = reqBuilder.build(contentSigner);

			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -1);
			Date startDate = calendar.getTime();
			calendar.add(Calendar.YEAR, 2);
			Date endDate = calendar.getTime();

			BigInteger certSN = new BigInteger(Long.toString(new SecureRandom().nextLong()));

			X509v3CertificateBuilder certBuilder = new X509v3CertificateBuilder(rootCertIssuer, certSN, startDate,
					endDate, certReq.getSubject(), certReq.getSubjectPublicKeyInfo());
			certBuilder.addExtension(Extension.basicConstraints, true, new BasicConstraints(false));

			X509CertificateHolder certHolder = certBuilder.build(contentSigner);
			X509Certificate cert = new JcaX509CertificateConverter().setProvider(BC_PROVIDER)
					.getCertificate(certHolder);

			cert.verify(rootCert.getPublicKey(), BC_PROVIDER);
			writeCertToFile(cert, CERTS_PATH + username + ".crt");
			exportToPkcs12(cert, keyPair.getPrivate(), username, CERTS_PATH + username + ".pfx");

			result = true;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static void sendGeneratedCertificate(String username) {
		String certName = username + ".pfx";
		String certPath = CERTS_PATH + certName;
		String email = UserBean.getUsersEmail(username);
		MailSendingClass.sendCertificateWithMail(email, certName, certPath);
	}

	private static KeyPair generateUserKeyPair() throws Exception {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM, BC_PROVIDER);
		keyPairGenerator.initialize(2048);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		return keyPair;
	}

	private static void exportToPkcs12(Certificate cert, PrivateKey privateKey, String alias, String filePath)
			throws Exception {
		FileOutputStream fs = new FileOutputStream(filePath);
		KeyStore keyStore = KeyStore.getInstance(KEYSTORE_TYPE, BC_PROVIDER);
		keyStore.load(null, null);
		keyStore.setKeyEntry(alias, privateKey, null, new Certificate[] { cert });
		keyStore.store(fs, KEYSTORE_PASS.toCharArray());
	}

	private static void writeCertToFile(X509Certificate cert, String filePath) throws Exception {
		FileOutputStream fs = new FileOutputStream(filePath);
		byte[] bytes = cert.getEncoded();
		fs.write(bytes);
		fs.close();
	}

	private static RSAPrivateKey loadPrivateKey(String keyPath) throws Exception {
		String privateKeyPEM = readKeyFile(keyPath);
		privateKeyPEM = privateKeyPEM.replace("-----BEGIN RSA PRIVATE KEY-----\n", "");
		privateKeyPEM = privateKeyPEM.replace("-----END RSA PRIVATE KEY-----", "");

		byte[] bytes = Base64.decode(privateKeyPEM);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
		RSAPrivateKey privateKey = (RSAPrivateKey) KeyFactory.getInstance(KEY_ALGORITHM).generatePrivate(keySpec);

		return privateKey;
	}
	
	private static PublicKey loadPublicKey(String keyPath) throws Exception {
		byte[] keyBytes = Files.readAllBytes(Paths.get(keyPath));

	    X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
	    
	    KeyFactory kf = KeyFactory.getInstance("RSA");
	    return kf.generatePublic(spec);
	}

	private static String readKeyFile(String keyPath) throws IOException {
		String keyContent = "";
		String line = "";

		BufferedReader br = new BufferedReader(new FileReader(keyPath));
		while ((line = br.readLine()) != null) {
			keyContent += line + "\n";
		}
		br.close();

		return keyContent;
	}

	public static X509Certificate loadCertificate(String certPath) throws Exception {
		CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
		return (X509Certificate) certFactory.generateCertificate(new FileInputStream(certPath));
	}

}