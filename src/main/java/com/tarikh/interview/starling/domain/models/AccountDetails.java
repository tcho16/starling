package com.tarikh.interview.starling.domain.models;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
@Getter
@Builder
@Data
public class AccountDetails
{
   private String accountHolderUId;
   private String accountUId;
   private String categoryId;
}
