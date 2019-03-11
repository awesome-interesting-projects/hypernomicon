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

package org.hypernomicon.view.dialogs;

import static org.hypernomicon.model.HyperDB.*;
import static org.hypernomicon.model.records.HDT_RecordType.*;
import static org.hypernomicon.util.Util.*;
import static org.hypernomicon.util.Util.MessageDialogType.*;
import static org.hypernomicon.view.wrappers.HyperTableColumn.HyperCtrlType.*;

import java.util.ArrayList;
import java.util.List;

import org.hypernomicon.model.Exceptions.SearchKeyException;
import org.hypernomicon.model.records.HDT_Base;
import org.hypernomicon.model.records.HDT_Concept;
import org.hypernomicon.model.records.HDT_Glossary;
import org.hypernomicon.model.records.HDT_Term;
import org.hypernomicon.view.populators.CustomRecordPopulator;
import org.hypernomicon.view.populators.StandardPopulator;
import org.hypernomicon.view.wrappers.HyperCB;
import org.hypernomicon.view.wrappers.HyperTableCell;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class SelectConceptDialogController extends HyperDialog
{
  @FXML private ComboBox<HyperTableCell> cbTerm, cbGlossary;
  @FXML private Button btnCreate;
  @FXML private TextField tfSearchKey;

  private HyperCB hcbTerm, hcbGlossary;
  private boolean createNew, alreadyChanging = false;
  private HDT_Term term;

//---------------------------------------------------------------------------
//---------------------------------------------------------------------------

  public static SelectConceptDialogController create(String title, HDT_Concept oldConcept)
  {
    SelectConceptDialogController scd = HyperDialog.create("SelectConceptDialog.fxml", title, true);
    scd.init(oldConcept);
    return scd;
  }

//---------------------------------------------------------------------------
//---------------------------------------------------------------------------

  private void init(HDT_Concept oldConcept)
  {
    hcbTerm = new HyperCB(cbTerm, ctDropDownList, new StandardPopulator(hdtTerm), null, false);

    CustomRecordPopulator pop = new CustomRecordPopulator(hdtGlossary, (row, force) ->
    {
      List<HDT_Base> glossaries = new ArrayList<>();

      HDT_Term term = hcbTerm.selectedRecord();
      if (term == null) return glossaries;

      if (oldConcept == null)
      {
        term.concepts.forEach(curConcept -> glossaries.add(curConcept.glossary.get()));
      }
      else
      {
        List<HDT_Glossary> termGlossaries = term.getGlossaries();

        db.glossaries.forEach(glossary -> {
          if (termGlossaries.contains(glossary) == false)
            glossaries.add(glossary); });
      }

      return glossaries;
    });

    hcbGlossary = new HyperCB(cbGlossary, ctDropDownList, pop, null);

    hcbTerm.addBlankEntry();

    cbTerm.getSelectionModel().selectedItemProperty().addListener((observable, oldCell, newCell) ->
    {
      if (alreadyChanging) return;

      alreadyChanging = true;

      HDT_Term term = HyperTableCell.getRecord(newCell);
      List<HyperTableCell> glossaryCells = hcbGlossary.populate(true);

      if ((term != null) && (oldConcept == null))
      {
        if (glossaryCells.stream().anyMatch(cell -> cell.getID() == 1))
          hcbGlossary.selectID(1);
      }
      else
      {
        if (glossaryCells.stream().anyMatch(cell -> cell.getID() == oldConcept.glossary.getID()))
          hcbGlossary.selectID(1);
      }

      alreadyChanging = false;
    });

    btnCreate.setOnAction(event -> btnCreateClick());
    createNew = false;
  }

  public HDT_Term     getTerm()      { return term; }
  public boolean      getCreateNew() { return createNew; }
  public HDT_Glossary getGlossary()  { return hcbGlossary.selectedRecord(); }

//---------------------------------------------------------------------------
//---------------------------------------------------------------------------

  private void btnCreateClick()
  {
    if (tfSearchKey.getText().length() == 0)
    {
      messageDialog("Unable to create term record: search key of term cannot be zero-length.", mtError);
      safeFocus(tfSearchKey);
      return;
    }

    term = db.createNewBlankRecord(hdtTerm);

    try
    {
      term.setSearchKey(tfSearchKey.getText(), true, false);
    }
    catch (SearchKeyException e)
    {
      if (e.getTooShort())
        messageDialog("Unable to create term record: search key must be at least 3 characters.", mtError);
      else
        messageDialog("Unable to create term record: search key already exists.", mtError);

      db.deleteRecord(hdtTerm, term.getID());
      term = null;

      safeFocus(tfSearchKey);

      return;
    }

    okClicked = true;
    createNew = true;
    dialogStage.close();
  }

//---------------------------------------------------------------------------
//---------------------------------------------------------------------------

  @Override protected boolean isValid()
  {
    if (hcbTerm.selectedRecord() == null)
    {
      messageDialog("You must select a term.", mtError);
      safeFocus(cbTerm);
      return false;
    }

    if (hcbGlossary.selectedRecord() == null)
    {
      messageDialog("You must select a glossary.", mtError);
      safeFocus(cbGlossary);
      return false;
    }

    term = hcbTerm.selectedRecord();

    return true;
  }

//---------------------------------------------------------------------------
//---------------------------------------------------------------------------

}
