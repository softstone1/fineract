/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.fineract.portfolio.loanaccount.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.fineract.infrastructure.event.business.domain.loan.transaction.LoanAccrualAdjustmentTransactionBusinessEvent;
import org.apache.fineract.infrastructure.event.business.domain.loan.transaction.LoanAccrualTransactionCreatedBusinessEvent;
import org.apache.fineract.infrastructure.event.business.domain.loan.transaction.LoanTransactionBusinessEvent;
import org.apache.fineract.infrastructure.event.business.service.BusinessEventNotifierService;
import org.apache.fineract.portfolio.loanaccount.domain.Loan;
import org.apache.fineract.portfolio.loanaccount.domain.LoanTransaction;

@RequiredArgsConstructor
public class LoanAccrualTransactionBusinessEventServiceImpl implements LoanAccrualTransactionBusinessEventService {

    private final BusinessEventNotifierService businessEventNotifierService;

    @Override
    public void raiseBusinessEventForAccrualTransactions(Loan loan, List<Long> existingTransactionIds) {
        for (final LoanTransaction transaction : loan.getLoanTransactions()) {
            if (transaction.isNotReversed() && (transaction.isAccrual() || transaction.isAccrualAdjustment())
                    && !existingTransactionIds.contains(transaction.getId())) {
                LoanTransactionBusinessEvent businessEvent = transaction.isAccrual()
                        ? new LoanAccrualTransactionCreatedBusinessEvent(transaction)
                        : new LoanAccrualAdjustmentTransactionBusinessEvent(transaction);
                businessEventNotifierService.notifyPostBusinessEvent(businessEvent);
            }
        }
    }
}
