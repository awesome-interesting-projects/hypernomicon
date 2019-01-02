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

package org.hypernomicon.view.workMerge;

import static org.hypernomicon.bib.BibData.BibFieldEnum.*;

import java.io.IOException;

import org.hypernomicon.App;
import org.hypernomicon.bib.BibData;
import org.hypernomicon.bib.BibData.BibFieldEnum;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public abstract class BibFieldRow
{
  protected AnchorPane ap;
  protected BibFieldEnum bibFieldEnum;

  public final AnchorPane getAnchorPane() { return ap; }
  
  protected abstract void init(BibFieldEnum bibFieldEnum, AnchorPane ap, BibData bd1, BibData bd2, BibData bd3, BibData bd4);
  public abstract void mergeInto(BibData bd);
  
//---------------------------------------------------------------------------  
//---------------------------------------------------------------------------  

  public static final BibFieldRow create(BibFieldEnum bibFieldEnum, BibData bd1, BibData bd2, BibData bd3, BibData bd4) throws IOException
  {
    FXMLLoader loader;
    
    if ((bibFieldEnum == bfISBNs) || 
        (bibFieldEnum == bfISSNs))                      loader = new FXMLLoader(App.class.getResource("view/workMerge/MergeWorksMultiLineChk.fxml"));
    else if (bibFieldEnum == bfEntryType)               loader = new FXMLLoader(App.class.getResource("view/workMerge/MergeWorksCB.fxml"));
    else if (BibData.bibFieldIsMultiLine(bibFieldEnum)) loader = new FXMLLoader(App.class.getResource("view/workMerge/MergeWorksMultiLine.fxml"));      
    else                                                loader = new FXMLLoader(App.class.getResource("view/workMerge/MergeWorksSingleLine.fxml"));
    
    AnchorPane ap = loader.load();
    BibFieldRow row = loader.getController();
      
    row.init(bibFieldEnum, ap, bd1, bd2, bd3, bd4);
    return row;
  }
  
//---------------------------------------------------------------------------  
//---------------------------------------------------------------------------  

}
