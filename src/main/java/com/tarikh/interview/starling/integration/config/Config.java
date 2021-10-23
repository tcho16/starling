package com.tarikh.interview.starling.integration.config;

import java.security.cert.X509Certificate;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import okhttp3.OkHttpClient;
import okhttp3.tls.Certificates;
import okhttp3.tls.HandshakeCertificates;

@Configuration
public class Config {
   final X509Certificate starlingCert = Certificates.decodeCertificatePem("-----BEGIN CERTIFICATE-----\n" +
           "MIIGQDCCBSigAwIBAgIMA/4EOE5OkRQN7SNQMA0GCSqGSIb3DQEBCwUAMFAxCzAJ\n" +
           "BgNVBAYTAkJFMRkwFwYDVQQKExBHbG9iYWxTaWduIG52LXNhMSYwJAYDVQQDEx1H\n" +
           "bG9iYWxTaWduIFJTQSBPViBTU0wgQ0EgMjAxODAeFw0yMDExMTExNDQ1NTlaFw0y\n" +
           "MTEyMTMxNDQ1NTlaMIGHMQswCQYDVQQGEwJHQjEXMBUGA1UECBMOR3JlYXRlciBM\n" +
           "b25kb24xDzANBgNVBAcTBkxvbmRvbjERMA8GA1UECxMIU2VjdXJpdHkxHjAcBgNV\n" +
           "BAoTFVNUQVJMSU5HIEJBTksgTElNSVRFRDEbMBkGA1UEAwwSKi5zdGFybGluZ2Jh\n" +
           "bmsuY29tMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA9EhWEA4lUYL0\n" +
           "N8k0OMgosYsjl/TfAZpgDmQb4i4ePwhjMsNeLNcgtalh2bdUpmzW5rlPyQVlMNr1\n" +
           "OI/kXNOqrsmfkl46IPRNhQitzKCAHitA9FbpuPnVQD1sPgslcC8Lop2GjeKFZjp0\n" +
           "fXfvr4Bm5ByvxrQrflXeTNWo3XYPSJM6H/Hbcpf92zQiXg5GU2U3S+EH1TZYJXfH\n" +
           "H7xWJCdXrkFUIM26JqgB5h/Qoyr5yypWy1alVhGD++uYtU7xwQUQflFIvlz9lg1S\n" +
           "FLbXryYR/yv0ZBhwl8zgS11BpNBVYuw/Z6rplqsI0P/OWTHyNtxY/1oyzG2eqQRx\n" +
           "oJZiGA4eqwIDAQABo4IC4DCCAtwwDgYDVR0PAQH/BAQDAgWgMIGOBggrBgEFBQcB\n" +
           "AQSBgTB/MEQGCCsGAQUFBzAChjhodHRwOi8vc2VjdXJlLmdsb2JhbHNpZ24uY29t\n" +
           "L2NhY2VydC9nc3JzYW92c3NsY2EyMDE4LmNydDA3BggrBgEFBQcwAYYraHR0cDov\n" +
           "L29jc3AuZ2xvYmFsc2lnbi5jb20vZ3Nyc2FvdnNzbGNhMjAxODBWBgNVHSAETzBN\n" +
           "MEEGCSsGAQQBoDIBFDA0MDIGCCsGAQUFBwIBFiZodHRwczovL3d3dy5nbG9iYWxz\n" +
           "aWduLmNvbS9yZXBvc2l0b3J5LzAIBgZngQwBAgIwCQYDVR0TBAIwADA/BgNVHR8E\n" +
           "ODA2MDSgMqAwhi5odHRwOi8vY3JsLmdsb2JhbHNpZ24uY29tL2dzcnNhb3Zzc2xj\n" +
           "YTIwMTguY3JsMC8GA1UdEQQoMCaCEiouc3RhcmxpbmdiYW5rLmNvbYIQc3Rhcmxp\n" +
           "bmdiYW5rLmNvbTAdBgNVHSUEFjAUBggrBgEFBQcDAQYIKwYBBQUHAwIwHwYDVR0j\n" +
           "BBgwFoAU+O9/8s14Z6jeb48kjYjxhwMCs+swHQYDVR0OBBYEFPGZ1kJT03n+IecI\n" +
           "ClbebiEZMxfyMIIBAwYKKwYBBAHWeQIEAgSB9ASB8QDvAHYA7sCV7o1yZA+S48O5\n" +
           "G8cSo2lqCXtLahoUOOZHssvtxfkAAAF1t8VTyAAABAMARzBFAiAnHrnk7tNePxi2\n" +
           "BHCA3QWjprAxfsUN7bIlIXIjfPlnPAIhANwFHoDVjAHZI34Ogg96Jcicxi0eUaXM\n" +
           "mVx4g6itJxshAHUA9lyUL9F3MCIUVBgIMJRWjuNNExkzv98MLyALzE7xZOMAAAF1\n" +
           "t8VTjAAABAMARjBEAiB6jNhNoi8BrQPBxIBMJiWPwXcOTb914PdrIEfsg8NZhQIg\n" +
           "DDuervt2u7yxfkFMF0akAhpoe/84WrIBepugSFPo9kUwDQYJKoZIhvcNAQELBQAD\n" +
           "ggEBADngkz7/P80VGmeXoL2Z4CAITk5lYGBWrXyin5mUJGbkdVVPseIbJlHEG4tI\n" +
           "erVw9Qxx5HRWWpAroBo+FsUs1bqdn5PPvzJ0ng1y3ES9ETZ1pfR8pfDfb7A3jK1Q\n" +
           "p3JB/kdzVqqMNA94yxHFfUqG0Dn/Izj2BsW9NcYp+dTe9qXoIUdI+MIrFsUDr3AR\n" +
           "VuZoiuRytXpwlPtvN7eaNEKyHLq61me1TyHBA2lwRKIqOJe+KS8b4w9/eZwzmIqz\n" +
           "/6IEBmC23JJHbPrEUCZpNob0OEL6TVsecOSF8vm97KB4VkJDl1kZypapriF9AfGj\n" +
           "C6bFSvq13su4aKTpMKWjoZPVtcU=\n" +
           "-----END CERTIFICATE-----\n");

   @Bean
   public OkHttpClient httpClient() {
      HandshakeCertificates certificates = new HandshakeCertificates.Builder()
              .addTrustedCertificate(starlingCert)
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
      return "eyJhbGciOiJQUzI1NiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAA_31SSY7bMBD8SqDz9ED7dsstH8gDWmTLJkyRAknNZBDk76FEyrIcIzdWVW_V7N-JsDbpE5wFcJr0u3VopFCXAdXtnekpeUvsMviIlKfjmDcDpAPmUPKsgZYjQcPqIqesLoom88H0a056j6q0rbq0eEsEuo0ou66rVgIZ04tyP7TkZH4K7msPbTZ2vMuh7QqCkpUchq6ugadsJCoaXvHO13b6RipkMF42eT1wwLYZoWzKFLAYSijyYqjSrMKyrXyGt_WdMbI2ZKVF1Q1VW0LTYgMl1ejzqxKyssa8zLNhbPLVMNMzrUsJkwKT2hLvDSH_tnPXbXxQONFLwX3NT4LgpJwYBZkzL4V1JyYCzo0fvCcu3B0ExTlk14nukQf-NMLRN1zcVRth_TeCUFx8CL6gDMEDSlQsjsbQcGBaOaNlaLQyUdNqFGZCJ7QCPcK4KG7vkr1330FozRbr9LRbpAlFLCzJD6IuPc6z_LqjLWpCxdFRz0mSL7HDqJkbudXIbGgkQ352-z8pjBG0WSIjvwFHF7P5eEz8V4ypZNgVd3cTOfTTYM883NSIN1MzfhHtUgDRRABHEIgJL9FT0I4nOIPKIjsm9DQMi7z1-0_SQR3dAj4aBrwXWO_B39Yk3FFTaubv4KHCRoBeD-KZjVlGj0Lu4wc_J2qLMsRIzO4E7FkKy7X44T_MwkUfc5y46ObEbXUembAw_-WvShzii1qHGIqyK_FFEoe4sEiTc97gMkfocLtW0IY_9Dyze7Mz-yIf9Ke68462b2L245ma-RipZbDM-BWu17F3eeS2qMcT2n7p-aaSP38Bi6g2fesFAAA.Ls8SV7tNaS-FKXmuZNowXyDvC-FB9fEjBjmeR7V0h2J6KQRW0QcE5Mtkb5-jpBSymj0wovT7drjgrY1z6-cVBYb1Z8gfrMhPF5DSjKlQZks7P-sgxcPIl5CO4FZhd1fIf0uhTQ9NBkP5m9DT8PD00bfvYxLiFUK7AKeZGqTKvfAaJE0X8VjLIuI-IG6fVLBq3ssO-AYPeJavzt-dSptYd7FCtvAMjUzuRT5kI68ixgWKNzf1beu6qXlZ8RloKI6UmLeVhAPkSx5roHAvm00UmTNHKJZ5dEVtcaC437RdjUKq73uiv5B_X5csD3SbzeAHtsnS71kCy2rGpq0WNGAPWK5LaPbeYRXUXKVBs0k_JTBl1cp2HLHMFMvn-RsdaaDtFhdaJqkKA9E2X69Cli2NiOffnHriqxFYXDRiCZS5sPA8pXS5ZBaLc4ibiLwubVdFQ6RxWfpMzN9k6y5LjN05OCMNV6aytNdcts5xL_KsQySBcZeshnTEktGhfQZXUE917fwoyVg9MiDIipmwKzah7pVq8hYqdWVzeFwLQuErA4XXFgtZ3TewbJlQeyWVrKgCO1KvUlc9ro2i_AO6W2pV4A12xBTWECJGZXEebo6NoSRqZS50lka-TNsYPC8NA7PFR4owRI9jZ77qsykAxdLcc6kXiPbK6zDoHu4Z-imSnPY";
   }

}