/*
 * Copyright 2015-2020 Jason Winning
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

package org.hypernomicon.dialogs;

import org.hypernomicon.model.records.HDT_RecordType;
import org.hypernomicon.view.wrappers.HyperTableCell;

import static org.hypernomicon.util.Util.*;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.MouseButton;
import javafx.util.StringConverter;

public class ValueSelectDlgCtrlr extends HyperDlg
{
  @FXML public ListView<HyperTableCell> listView;

//---------------------------------------------------------------------------
//---------------------------------------------------------------------------

  public static ValueSelectDlgCtrlr build(List<HyperTableCell> list)
  {
    return ((ValueSelectDlgCtrlr) create("ValueSelectDlg", "Choose a Value", true)).init(list);
  }

//---------------------------------------------------------------------------
//---------------------------------------------------------------------------

  private ValueSelectDlgCtrlr init(List<HyperTableCell> list)
  {
    if (collEmpty(list)) return this;

    HDT_RecordType objType = HyperTableCell.getCellType(list.get(0));

    listView.setItems(FXCollections.observableArrayList(list));

    StringConverter<HyperTableCell> strConv = new StringConverter<>()
    {
      @Override public String toString(HyperTableCell cell)     { return HyperTableCell.getCellText(cell); }
      @Override public HyperTableCell fromString(String string) { return new HyperTableCell(-1, string, objType); }
    };

    listView.setCellFactory(TextFieldListCell.forListView(strConv));

    listView.setOnMouseClicked(mouseEvent ->
    {
      if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && (mouseEvent.getClickCount() == 2))
        btnOkClick();
    });

    return this;
  }

//---------------------------------------------------------------------------
//---------------------------------------------------------------------------

  @Override protected boolean isValid()
  {
    return listView.getSelectionModel().getSelectedItem() != null ? true : falseWithWarningMessage("Select a record.", listView);
  }

//---------------------------------------------------------------------------
//---------------------------------------------------------------------------

}
