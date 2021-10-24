package com.tarikh.interview.starling.integration.config;

import java.io.*;
import java.nio.file.Files;
import java.security.cert.X509Certificate;
import java.util.stream.Collectors;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import okhttp3.OkHttpClient;
import okhttp3.tls.Certificates;
import okhttp3.tls.HandshakeCertificates;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Configuration
public class Config {

   @NonNull
   @Value("${starling.accessToken}")
   String accessToken;

   @Bean
   public OkHttpClient httpClient() throws IOException {
      File resource = new ClassPathResource("starling-sandbox-api-certificate.crt").getFile();
      String cert = new String(Files.readAllBytes(resource.toPath()));

      HandshakeCertificates certificates = new HandshakeCertificates.Builder()
              .addTrustedCertificate(Certificates.decodeCertificatePem(cert))
              .addPlatformTrustedCertificates()
              .build();

      return new OkHttpClient.Builder()
              .sslSocketFactory(certificates.sslSocketFactory(), certificates.trustManager())
              .build();
   }

   @Bean
   public String starlingAccountUrl() {
      return "https://api-sandbox.starlingbank.com/api/v2/accounts";
   }

   @Bean
   public String starlingTransactionUrl() {
      return "https://api-sandbox.starlingbank.com/api/v2/feed/account/{accountUId}/category/{categoryUId}/transactions-between";
   }

   @Bean
   public String starlingGoalUrl() {
      return "https://api-sandbox.starlingbank.com/api/v2/account/{accountUId}/savings-goals";
   }

   @Bean
   public String starlingGoalAddMoneyUrl() {
      return "https://api-sandbox.starlingbank.com/api/v2/account/{accountUid}/savings-goals/{savingsGoalUid}/add-money/{transferUid}";
   }

   @Bean
   public String accessToken() {
      return accessToken;}

}