/*
 * Copyright 2020, Google LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package anthos.samples.bankofanthos.ledgermonolith;

import static anthos.samples.bankofanthos.ledgermonolith.ExceptionMessages.EXCEPTION_MESSAGE_DUPLICATE_TRANSACTION;
import static anthos.samples.bankofanthos.ledgermonolith.ExceptionMessages.EXCEPTION_MESSAGE_INSUFFICIENT_BALANCE;
import static anthos.samples.bankofanthos.ledgermonolith.ExceptionMessages.EXCEPTION_MESSAGE_WHEN_AUTHORIZATION_HEADER_NULL;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import java.util.Collection;
import java.util.Deque;
import java.util.concurrent.ExecutionException;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *  This class is a REST controller that handles requests related to transactions,
 *  balances, and transaction history. It combines the functionalities of the
 *  original microservices into a single monolith service.
+ */
+@RestController
+/**
+ *  This class is a REST controller that handles requests related to transactions,
+ *  balances, and transaction history. It combines the functionalities of the
+ *  original microservices into a single monolith service.
  */
 @RestController
 public final class LedgerMonolithController {

@@ -215,10 +215,12 @@
     }
 
     /**
-     * Modified getAvailableBalance - instead of making an external
-     * API call like in the microservice version,
-     * get balance from local cache.
+     * Modified getAvailableBalance - instead of making an external API call like in the microservice version, get balance from local cache.
      */
+     /**
+      * This method retrieves the available balance for a given account from the local cache.
+      *
+      * @param accountId The ID of the account for which to retrieve the balance.
+      * @return The available balance for the specified account.
+      */
     protected Long getAvailableBalance(String accountId) {
         LOGGER.debug("Retrieving balance for transaction sender");
         Long balance = Long.valueOf(-1);
@@ -234,7 +236,7 @@
     // BEGIN BALANCE READER
 
     /**
-     * Return the balance for the specified account.
+     * Returns the balance for the specified account.
      *
      * The currently authenticated user must be allowed to access the account.
      *
@@ -285,7 +287,7 @@
 
 
     /**
-     * Retrieves a list of transactions for the specified account.
+     * Retrieves a list of transactions for the specified account.
      *
      * This endpoint returns a list of transactions for a given account. The currently authenticated user must be allowed to access the account. The transaction history is retrieved from the local cache.
      *
