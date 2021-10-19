package com.tarikh.interview.starling.integration.adapters;

import java.net.URI;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.tarikh.interview.starling.domain.TransactionQueryPort;
import com.tarikh.interview.starling.domain.models.TimestampDuration;
import com.tarikh.interview.starling.domain.models.Transaction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class TransactionQueryAdapter implements TransactionQueryPort
{
   private final WebClient client = WebClient.create();

   //https://api-sandbox.starlingbank.com/api/v2/feed/account/31d945e6-7655-458a-a90a-9db53a491181/category/a8f35c14-d7b4-4563-9d3d-04f391959cee/transactions-between?minTransactionTimestamp=2020-06-01T12%3A34%3A56.000Z&maxTransactionTimestamp=2021-10-20T12%3A34%3A56.000Z
   @Override
   public List<Transaction> queryForTransactionsBasedOnTimeframe(TimestampDuration timestampDuration)
   {
   log.info("queryForTransactionsBasedOnTimeframe:+ calling the transaction query endpoint");
      String response = client.get()
                              .uri("https://api-sandbox.starlingbank.com/api/v2/feed/account/" +
                                   timestampDuration.getAccountDetails().getAccountUId() +
                                   "/category/" + timestampDuration.getAccountDetails().getCategoryId() + "/transactions-between?minTransactionTimestamp=2020-06-01T12%3A34%3A56.000Z&maxTransactionTimestamp=2021-10-20T12%3A34%3A56.000Z")
                              .header("Authorization", "Bearer eyJhbGciOiJQUzI1NiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAA_31Sy5LbIBD8lS2dd7ZkWdbrtrf8QD5gBINNGYEKkDdbqfx7kECW5bhyo7vn0TPM70w6l3UZjhI4DebDebRK6nOP-vrBzJC9Z27qQ0TOcyGKuoe8xwJKfqih4UhQs-pY0KE6HutDCKZfY9YFVNaBqfL3TKKPRFVX7UwgY2bS_odRnOxPyUPtvjmIlrcFNO2RoGQlh76tKuA5E0THmp94G2p7cyW9ZpRNXTQCxCkXwU1TQqAqaAXlTVUUVVFSyAhjfTJGzsWs_Hhq-1OIrRusoaQKAZtTCYeywqIsDr2oi3lgZkaalxKdAlPGEe8sIX9buctiHzQO9FLw3-OTIDlpL4Uku-eVdH7HJMC5DcY74tLfQVS8R3YZ6B654S8rPb3h5C_GShe-EaTm8ib5hCoG96hQs2SNoeXAjPbWqNhoZpJmtJB2QC-NBhMWPWnu7pK7d19BbM0m582wjkgDylRYUTCizx2Oo_q-oyVqQM3RU8dJUSixwqTZK_l5kNGSIEvBu_ufFG1EbVTIKGzA09kuczwm_iumVLLsgut0A3kMbrBjAS5qwstQI34TrVIEaYgItiCQA57TTFHbnuAtaodscxho6Cd17dafpI3aukW8NYx4LTDfQ7itQfqtpjIs3MFDhYUAMx_EM5uyrBFSrfbjPDtqibLESI5-B9xeist1eAsf5uBsNh87Lk2z45Y6j0xcWPjyVyU28UWtTYxF2YX4pIhDWliiyfsw4DQm6HG5VjCWP_Tcs2uzPfsiH8yXvvOelm9i7vZMjVwkauods2GF83WsXR65JerxhJZfer6p7M9fP0q5_usFAAA.O1-Pm70fq5kDllRp08L2mY0qfVoEyOn-fNJqv9OXLv27nQZUfW91DncXCaQR0_J04SAdRiA51zhoQQ6GSI7s59CvcHhEH8gxjesEJQE-PvV2XezkBSgyi05DsFJsW1cty7pZcUKWKYdITIxxC7zXtZdAikuBtcmTWxk1ANdRKDedhIsGcR3jV1Vnx9FgoaYT0zDD3JmR_pcNn9svViFBU3wGkaALIcckZwQzDDgAepqVnjcMZ7Hp3eDf0lCwMG0j0XjjaKo63OSJ2CTT6V8dnTwl1nGcJr7Cb3suKelXfebp034wrh-jlPFwaTOi50HFEzIqE_fvD7ngWYt2Rd-zSOhmr16IKU_2vqUY4qkRXM_sfl9rJJ4WK-sxHLPss0yU6NIrDKuCCJxj7TeSDf78l-GQ3ot12CGDDIIJz76gGmoTfWhJvfqhH0E6wywCZcQ15FDX-6UySlhfWGjNPW5OF6hUpc2y559t2xHXLoWEKhVs1ti3cMmi8jXRjsbTjB_Rq9N-DE1m9YNszd6Rzt66AEUdhGBybEdvcJOQhGn3KXmzne9fwt_VR0u9jp5cInKUuPDf029cODa5GAOPe2Ie5vX3egbdWRHVzRKXmfZo2_F14HevoagcEW2d1OwKhiKPspsGt04OrRj9qUkng03agdKH-UfcEhQOtCFkuNGJJh4")
                              .header("Accept", String.valueOf(MediaType.APPLICATION_JSON))
                              .retrieve()
                              .bodyToMono(String.class)
                              .block();

      System.out.println(response);
      return null;
   }
}
