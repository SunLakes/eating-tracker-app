/*
 * Copyright (c) 2023. http://t.me/mibal_ua
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

package com.example.sunlakesEatingTracker.model;

/**
 * @version 2.0
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class Entry {

    private int braceletId;

    private int dayId;

    private int eatingId;

    public Entry(int braceletId, int dayId, int eatingId) {
        this.braceletId = braceletId;
        this.dayId = dayId;
        this.eatingId = eatingId;
    }

    private Entry() {
    }

    public int getBraceletId() {
        return braceletId;
    }

    public int getDayId() {
        return dayId;
    }

    public int getEatingId() {
        return eatingId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Entry{");
        sb.append("braceletId=").append(braceletId);
        sb.append(", day=").append(dayId);
        sb.append(", eating=").append(eatingId);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Entry entry = (Entry) o;
        return braceletId == entry.braceletId &&
               dayId == entry.dayId &&
               eatingId == entry.eatingId;
    }
}
