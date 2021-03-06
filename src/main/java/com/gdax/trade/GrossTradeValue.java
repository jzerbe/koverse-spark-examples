/*
 * Copyright 2016 Koverse, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gdax.trade;

import com.koverse.sdk.data.SimpleRecord;
import org.apache.spark.api.java.JavaRDD;

public class GrossTradeValue implements java.io.Serializable {

  private static final long serialVersionUID = 8741666028339586272L;
  private final String priceFieldName;
  private final String sizeFieldName;
  private final String tradeIdFieldName;

  /**
   * Constructor that take all configurable named fields.
   * @param priceFieldName Field name that contains the trade price.
   * @param sizeFieldName Field name that contains trade size.
   * @param tradeIdFieldName Field name that contains the trade id.
   */
  public GrossTradeValue(
      final String priceFieldName, final String sizeFieldName,
      final String tradeIdFieldName
  ) {
    this.priceFieldName = priceFieldName;
    this.sizeFieldName = sizeFieldName;
    this.tradeIdFieldName = tradeIdFieldName;
  }

  /**
   * Calculates the gross value of the trade given the "price" and "size" fields.
   *
   * @param inputRecordsRdd input RDD of SimpleRecords
   * @return a JavaRDD of SimpleRecords that has "gross" field in each record
   */
  public JavaRDD<SimpleRecord> calculateGross(JavaRDD<SimpleRecord> inputRecordsRdd) {

    // turn each tuple into an output Record with a "gross" field
    JavaRDD<SimpleRecord> outputRdd = inputRecordsRdd.map(record -> {
      final float tradePrice = Float.parseFloat(record.get(priceFieldName).toString());
      final float tradeSize = Float.parseFloat(record.get(sizeFieldName).toString());
      final String tradeId = record.get(tradeIdFieldName).toString();

      float tradeGross = tradePrice * tradeSize;

      SimpleRecord outputRecord = new SimpleRecord();
      outputRecord.put("gross", tradeGross);
      outputRecord.put("trade_id", tradeId);
      return outputRecord;
    });

    return outputRdd;

  }
}
