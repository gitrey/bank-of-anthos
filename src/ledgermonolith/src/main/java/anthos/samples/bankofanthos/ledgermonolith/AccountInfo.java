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

import java.util.Deque;


/**
 * Defines the account Info object used for the LedgerReaderCache.
 *
 * This class represents the account information stored in the cache, including the
 * balance and a list of recent transactions.
 */
public class AccountInfo {
  /** The current balance of the account. */
  Long balance;
  /** The list of recent transactions for the account. */
  Deque<Transaction> transactions;

  /**
   * Constructor for AccountInfo.
   *
   * @param balance The initial balance of the account.
   * @param transactions The initial list of transactions for the account.
   */
  public AccountInfo(Long balance,
    Deque<Transaction> transactions) {
        this.balance = balance;
        this.transactions = transactions;
    }

  /**
   * Retrieves the list of transactions for the account.
   *
   * @return The list of transactions.
   */
  public Deque<Transaction> getTransactions() {
    return transactions;
  }

  /**
   * Retrieves the current balance of the account.
   *
   * @return The account balance.
   */
  public Long getBalance() {
    return balance;
  }
}
