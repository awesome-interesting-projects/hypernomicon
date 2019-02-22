/*
 * Copyright 2015-2019 Jason Winning
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
 *
 */

package org.hypernomicon.view.populators;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.hypernomicon.queryEngines.QueryEngine.QueryType;
import org.hypernomicon.view.wrappers.HyperTableCell;
import org.hypernomicon.view.wrappers.HyperTableRow;

public class QueryTypePopulator extends Populator
{
  @Override public CellValueType getValueType() { return CellValueType.cvtQueryType; }

  //---------------------------------------------------------------------------

  @Override public List<HyperTableCell> populate(HyperTableRow row, boolean force)
  {
    List<HyperTableCell> list = new ArrayList<>();

    EnumSet.allOf(QueryType.class).forEach(queryType -> list.add(new HyperTableCell(queryType.getCode(),
                                                                                    queryType.getCaption(),
                                                                                    queryType.getRecordType())));

    return list;
  }

//---------------------------------------------------------------------------
//---------------------------------------------------------------------------

}
