package com.tarikh.interview.starling.integration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;

@Configuration
public class Config
{
   @Bean
   public OkHttpClient httpClient()
   {
      CertificatePinner certificatePinner = new CertificatePinner.Builder()
                                               .add("api-sandbox.starlingbank.com",
                                                    "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=")
                                               .build();

      return new OkHttpClient.Builder()
                .certificatePinner(certificatePinner)
                .build();
   }
}
//https://stackoverflow.com/questions/69638375/how-to-add-certificate-on-okhttp