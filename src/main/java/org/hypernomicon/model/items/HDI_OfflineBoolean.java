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

package org.hypernomicon.model.items;

import org.hypernomicon.model.HDI_Schema;
import org.hypernomicon.model.HyperDB.Tag;
import org.hypernomicon.model.records.HDT_RecordState;
import org.hypernomicon.model.records.HDT_RecordType;
import static org.hypernomicon.model.records.HDT_RecordState.*;
import static org.hypernomicon.util.Util.*;

import java.util.LinkedHashMap;

public class HDI_OfflineBoolean extends HDI_OfflineBase
{
  boolean boolValue = false;

  public HDI_OfflineBoolean(HDI_Schema newSchema, HDT_RecordState recordState)
  {
    super(newSchema, recordState);
  }

  public boolean get()                 { return boolValue; }
  public void    set(boolean newValue) { boolValue = newValue; }
  
//---------------------------------------------------------------------------
//---------------------------------------------------------------------------
  
  @Override public void setFromXml(Tag tag, String nodeText, HDT_RecordType objType, int objID, LinkedHashMap<Tag, HDI_OfflineBase> nestedItems)
  {
    boolValue = parseBoolean(nodeText);
  }

//---------------------------------------------------------------------------
//---------------------------------------------------------------------------

  @Override public void writeToXml(Tag tag, StringBuilder xml)
  {
    writeBooleanTag(xml, tag, boolValue);
  }

//---------------------------------------------------------------------------
//---------------------------------------------------------------------------

}
