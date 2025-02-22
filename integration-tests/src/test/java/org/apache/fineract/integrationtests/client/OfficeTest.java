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
package org.apache.fineract.integrationtests.client;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import org.apache.fineract.client.models.GetOfficesResponse;
import org.apache.fineract.client.models.PostOfficesRequest;
import org.apache.fineract.integrationtests.common.Utils;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

/**
 * Integration Test for /offices API.
 *
 * @author Michael Vorburger.ch
 */
public class OfficeTest extends IntegrationTest {

    @Test
    @Order(1)
    void createOne() {
        // NB parentId(1) always exists (Head Office)
        // NB name random() because Office Names have to be unique
        // TODO requiring dateFormat(..).locale(..) is dumb :( see https://issues.apache.org/jira/browse/FINERACT-1233
        assertThat(ok(fineractClient().offices.createOffice(new PostOfficesRequest().name(Utils.randomStringGenerator("TestOffice_", 6))
                .parentId(1L).openingDate(LocalDate.now(ZoneId.of("UTC"))).dateFormat("yyyy-MM-dd").locale("en_US"))).getOfficeId())
                .isGreaterThan(0);
    }

    @Test
    @Order(2)
    void retrieveOneExistingInclDateFormat() { // see FINERACT-1220 re. what this tests re. Date Format
        List<GetOfficesResponse> response = ok(fineractClient().offices.retrieveOffices(true, null, null));
        assertThat(response.size()).isGreaterThanOrEqualTo(1);
        assertThat(response.get(0).getOpeningDate()).isNotNull();
    }
}
